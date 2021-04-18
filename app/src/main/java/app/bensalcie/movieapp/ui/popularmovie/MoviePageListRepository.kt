package app.bensalcie.movieapp.ui.popularmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.bensalcie.movieapp.data.api.POST_PER_PAGE
import app.bensalcie.movieapp.data.api.TheMovieDbInterface
import app.bensalcie.movieapp.data.repository.*
import app.bensalcie.movieapp.data.vo.Movie
import app.bensalcie.movieapp.data.vo.TopRatedResponse
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService:TheMovieDbInterface) {
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var topratedmoviePagedList: LiveData<PagedList<TopRatedResponse.Result>>

    lateinit var movieDataSourceFactory: MovieDataSourceFactory
    lateinit var topRatedMovieDataSourceFactory: TopRatedMovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable):LiveData<PagedList<Movie>>{
        movieDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)
        val config =PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory,config).build()
        return moviePagedList
    }



    fun fetchLiveTopRatedMoviePagedList(compositeDisposable: CompositeDisposable):LiveData<PagedList<TopRatedResponse.Result>>{
        topRatedMovieDataSourceFactory = TopRatedMovieDataSourceFactory(apiService,compositeDisposable)
        val config =PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        topratedmoviePagedList = LivePagedListBuilder(topRatedMovieDataSourceFactory,config).build()
        return topratedmoviePagedList
    }
    fun  getTopRatedNetworkState():LiveData<NetworkState> {

        return Transformations.switchMap(topRatedMovieDataSourceFactory.movieLiveDataSource,TopRatedMovieDataSource::networkState)
    }
    fun  getNetworkState():LiveData<NetworkState> {

        return Transformations.switchMap(movieDataSourceFactory.movieLiveDataSource,MovieDataSource::networkState)
    }
}