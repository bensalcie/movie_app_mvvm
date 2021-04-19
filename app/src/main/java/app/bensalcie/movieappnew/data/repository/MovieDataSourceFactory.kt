package app.bensalcie.movieappnew.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import app.bensalcie.movieappnew.data.api.TheMovieDbInterface
import app.bensalcie.movieappnew.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService : TheMovieDbInterface, private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int, Movie>() {
    /**
     * Create a DataSource.
     *
     *
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     *
     *
     * [LivePagedListBuilder] for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * `LiveData<PagedList>` to observers.
     *
     * @return the new DataSource.
     */
     val movieLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource

    }
}