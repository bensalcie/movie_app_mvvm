package app.bensalcie.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.bensalcie.movieapp.data.api.TheMovieDbInterface
import app.bensalcie.movieapp.data.vo.MovieDetails
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(private val apiService: TheMovieDbInterface,private val compositeDisposable:CompositeDisposable) {
    private val _networkState = MutableLiveData<NetworkState>()
     val networkState  : LiveData<NetworkState>
    get() = _networkState//with this, no need implement get function to get network state


    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
     val downloadedMovieDetailsResponse  : LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse//with this, no need implement get function to get network state

    fun fetchMovieDetails(movieId: Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(

                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({

                        _downloadedMovieDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("MovieDataSource", it1) }

                    })
            )

        }catch (e:Exception){
            e.message?.let { Log.e("MovieDataSource", it) }

        }
    }
}