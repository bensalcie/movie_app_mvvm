package app.bensalcie.movieappnew.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import app.bensalcie.movieappnew.data.api.TheMovieDbInterface
import app.bensalcie.movieappnew.data.vo.TopRatedResponse
import io.reactivex.disposables.CompositeDisposable

class TopRatedMovieDataSourceFactory(private val apiService : TheMovieDbInterface, private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int, TopRatedResponse.Result>() {
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
     val movieLiveDataSource = MutableLiveData<TopRatedMovieDataSource>()
    override fun create(): DataSource<Int, TopRatedResponse.Result> {
        val movieDataSource = TopRatedMovieDataSource(apiService,compositeDisposable)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource

    }
}