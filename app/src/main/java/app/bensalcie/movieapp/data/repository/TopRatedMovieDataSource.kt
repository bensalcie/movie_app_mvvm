package app.bensalcie.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import app.bensalcie.movieapp.data.api.FIRST_PAGE
import app.bensalcie.movieapp.data.api.TheMovieDbInterface
import app.bensalcie.movieapp.data.vo.Movie
import app.bensalcie.movieapp.data.vo.TopRatedResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TopRatedMovieDataSource(private val apiService :TheMovieDbInterface, private val compositeDisposable: CompositeDisposable): PageKeyedDataSource<Int, TopRatedResponse.Result>() {
    /**
     * Load initial data.
     *
     *
     * This method is called first to initialize a PagedList with data. If it's possible to count
     * the items that can be loaded by the DataSource, it's recommended to pass the loaded data to
     * the callback via the three-parameter
     * [LoadInitialCallback.onResult]. This enables PagedLists
     * presenting data from this source to display placeholders to represent unloaded items.
     *
     *
     * [LoadInitialParams.requestedLoadSize] is a hint, not a requirement, so it may be may be
     * altered or ignored.
     *
     * @param params Parameters for initial load, including requested load size.
     * @param callback Callback that receives initial load data.
     */

    private var page = FIRST_PAGE
    val networkState : MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, TopRatedResponse.Result>
    ) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getTopRated(page)
                .subscribeOn(Schedulers.io())
                .subscribe({

                    callback.onResult(it.results, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)

                }, {
                    networkState.postValue(NetworkState.ERROR)
                    it.message?.let { it1 -> Log.e("MovieDataSource", it1) }

                })
        )

    }

    /**
     * Prepend page with the key specified by [LoadParams.key].
     *
     *
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     *
     *
     * Data may be passed synchronously during the load method, or deferred and called at a
     * later time. Further loads going down will be blocked until the callback is called.
     *
     *
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
     * and prevent further loading.
     *
     * @param params Parameters for the load, including the key for the new page, and requested load
     * size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TopRatedResponse.Result>) {
    }

    /**
     * Append page with the key specified by [LoadParams.key].
     *
     *
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     *
     *
     * Data may be passed synchronously during the load method, or deferred and called at a
     * later time. Further loads going down will be blocked until the callback is called.
     *
     *
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
     * and prevent further loading.
     *
     * @param params Parameters for the load, including the key for the new page, and requested load
     * size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TopRatedResponse.Result>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getTopRated(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({

                 if (it.totalPages>=params.key){


                     callback.onResult(it.results,  params.key + 1)
                     networkState.postValue(NetworkState.LOADED)
                 }else{

                     networkState.postValue(NetworkState.ENDOFLIST)
                 }

                }, {
                    networkState.postValue(NetworkState.ERROR)
                    it.message?.let { it1 -> Log.e("MovieDataSource", it1) }

                })
        )
    }
}