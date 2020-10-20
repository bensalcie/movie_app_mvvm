package app.bensalcie.movieapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import app.bensalcie.movieapp.data.api.TheMovieDbInterface
import app.bensalcie.movieapp.data.repository.MovieDetailsNetworkDataSource
import app.bensalcie.movieapp.data.repository.NetworkState
import app.bensalcie.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDbInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun  fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int):LiveData<MovieDetails>{
        movieDetailsNetworkDataSource= MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return  movieDetailsNetworkDataSource.networkState
    }

    //if you want to cache to local storage here is where you will do it.
}