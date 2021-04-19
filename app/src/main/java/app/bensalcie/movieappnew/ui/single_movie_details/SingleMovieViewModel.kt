package app.bensalcie.movieappnew.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.bensalcie.movieappnew.data.repository.NetworkState
import app.bensalcie.movieappnew.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository: MovieDetailsRepository,private val movieId:Int):ViewModel(){
    private val compositeDisposable=CompositeDisposable()
    val movieDetails : LiveData<MovieDetails> by lazy {

        //by lazy for better perfomance
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)

    }

    val movieDetailsNetworkState : LiveData<NetworkState> by lazy {

        //by lazy for better perfomance
        movieRepository.getMovieDetailsNetworkState()

    }

    override fun onCleared() {
        super.onCleared()
        //prevent any memory leaks
        compositeDisposable.dispose()
    }
}