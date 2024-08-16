package com.example.hellobaccho.ui.rickAndMortyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hellobaccho.base.BaseViewModel
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.ui.rickAndMortyList.models.RickMortyListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RickMortyViewModel @Inject constructor(private val _repo:RickMortyRepository): BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData

    val rickMortyLiveData: LiveData<ArrayList<Result>>
        get() = _repo.rickMortyLiveData


    fun getRickMortyList(pageNo:String?){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getRickMortyList(pageNo)
        }
    }
}