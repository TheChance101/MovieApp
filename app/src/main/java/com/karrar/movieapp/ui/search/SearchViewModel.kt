package com.karrar.movieapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.data.remote.State
import com.karrar.movieapp.data.remote.repository.MovieRepository
import com.karrar.movieapp.domain.models.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel(), MediaInteractionListener{
    private val _media = MutableLiveData<State<List<Media>>>()
    val media: LiveData<State<List<Media>>> get() = _media

    val searchText = MutableStateFlow("")
    val mediaType = MutableStateFlow("movie")


    init {
//        viewModelScope.launch {
//            mediaType.combine(searchText){ type, text ->
//                "${type},${text}"
//            }.debounce(1000).collect{getMedia(it.substringBefore(','), it.substringAfter(','))}
//        }
        viewModelScope.launch {
            searchText.debounce(1000).collect{
                getMedia(mediaType.value,it)
            }
        }
    }

    private fun getMedia(type: String, query: String){
        viewModelScope.launch {
            movieRepository.searchWithType(type, query).collect{
                _media.postValue(it)
            }
        }
    }

    fun getMovies(){
        viewModelScope.launch {
            mediaType.emit("movie")
            getMedia(mediaType.value,searchText.value)
        }
    }

    fun getSeries(){
        viewModelScope.launch {
            mediaType.emit("tv")
            getMedia(mediaType.value,searchText.value)
        }
    }

    fun getActors(){
        viewModelScope.launch {
            mediaType.emit("person")
            getMedia(mediaType.value,searchText.value)
        }
    }

    override fun onClickMedia(mediaID: Int) {

    }
}