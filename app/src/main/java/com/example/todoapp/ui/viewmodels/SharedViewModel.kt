package com.example.todoapp.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.data.repository.ToDoRepository
import com.example.todoapp.util.Actions
import com.example.todoapp.util.RequestState
import com.example.todoapp.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

    val id: MutableState<Int> = mutableStateOf(0)
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")

    val action: MutableState<Actions> = mutableStateOf(Actions.NO_ACTION)


    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTask: StateFlow<RequestState<List<ToDoTask>>> = _allTask

    private val _searchTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTask: StateFlow<RequestState<List<ToDoTask>>> = _searchTask


    fun getAllTask() {
        _allTask.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository.getAllTask.collect {
                    _allTask.value = RequestState.Success(it)
                }
            }

        } catch (e: Exception) {
            _allTask.value = RequestState.Error(e)

        }
    }
    fun searchDatabase(searchQuery: String) {
        _searchTask.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository.searchQuery(searchQuery= "%$searchQuery%")
                    .collect{searchedTask->
                        _searchTask.value = RequestState.Success(searchedTask)

                    }
            }

        } catch (e: Exception) {
            _searchTask.value = RequestState.Error(e)

        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask


    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task


            }
        }
    }


    fun updateTaskFields(selectedTask: ToDoTask?) {

        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {

            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW

        }

    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < 20) {
            title.value = newTitle
        }
    }

    fun validiate(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }


    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {

            val todoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.addTask(todoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
     viewModelScope.launch(Dispatchers.IO) {
         val task = ToDoTask(
             id = id.value,
             title = title.value,
             description = description.value,
             priority = priority.value
         )
         repository.updateTask(toDoTask = task)
     }
    }
    private fun deleteTask() {
     viewModelScope.launch(Dispatchers.IO) {
         val task = ToDoTask(
             id = id.value,
             title = title.value,
             description = description.value,
             priority = priority.value
         )
         repository.deleteTask(toDoTask = task)
     }
    }
    fun deleteAllTask() {
        viewModelScope.launch {
            repository.deleteAlltask()
        }
    }

    fun handleDatabase(actions: Actions) {
        when (actions) {
            Actions.ADD -> { addTask() }
            Actions.UPDATE -> { updateTask() }
            Actions.DELETE -> { deleteTask() }
            Actions.DELETE_ALL -> { deleteAllTask() }
            Actions.UNDO -> { addTask() }
            else->{}
        }
        this.action.value = Actions.NO_ACTION
    }
}

