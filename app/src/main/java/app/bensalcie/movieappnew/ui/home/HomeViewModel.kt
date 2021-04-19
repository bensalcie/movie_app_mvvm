package app.bensalcie.movieappnew.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import app.bensalcie.movieappnew.data.repository.NetworkState
import app.bensalcie.movieappnew.data.vo.Movie
import app.bensalcie.movieappnew.ui.popularmovie.MoviePageListRepository
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(private val movieRepository: MoviePageListRepository):ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }
    val networkState:LiveData<NetworkState>by lazy {
        movieRepository.getNetworkState()
    }
    fun listIsEmpty(): Boolean {

        return moviePagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}