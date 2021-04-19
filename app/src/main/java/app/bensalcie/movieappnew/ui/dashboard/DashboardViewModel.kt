package app.bensalcie.movieappnew.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import app.bensalcie.movieappnew.data.repository.NetworkState
import app.bensalcie.movieappnew.data.vo.TopRatedResponse
import app.bensalcie.movieappnew.ui.popularmovie.MoviePageListRepository
import io.reactivex.disposables.CompositeDisposable

class DashboardViewModel(private val movieRepository: MoviePageListRepository):ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val moviePagedList : LiveData<PagedList<TopRatedResponse.Result>> by lazy {
        movieRepository.fetchLiveTopRatedMoviePagedList(compositeDisposable)
    }
    val networkState:LiveData<NetworkState>by lazy {
        movieRepository.getTopRatedNetworkState()
    }
    fun listIsEmpty(): Boolean {

        return moviePagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}