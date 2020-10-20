package app.bensalcie.movieapp.ui.popularmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import app.bensalcie.movieapp.R
import app.bensalcie.movieapp.data.api.TheMovieDbClient
import app.bensalcie.movieapp.data.api.TheMovieDbInterface
import app.bensalcie.movieapp.data.repository.NetworkState
import app.bensalcie.movieapp.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var movieRepository:MoviePageListRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiService :TheMovieDbInterface = TheMovieDbClient.getClient()
        movieRepository = MoviePageListRepository(apiService)
        viewModel=getViewModel()
        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,3)
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            /**
             * Returns the number of span occupied by the item at `position`.
             *
             * @param position The adapter position of the item
             * @return The number of spans occupied by the item at the provided position
             */
            override fun getSpanSize(position: Int): Int {

                val viewType = movieAdapter.getItemViewType(position)
                if (viewType==movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }

        }
        rv_movie_list.layoutManager =gridLayoutManager
        rv_movie_list.hasFixedSize()
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this,{
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this,{
            progress_bar_popular.visibility= if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =  if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })


    }

    private fun getViewModel():MainActivityViewModel{
        return ViewModelProviders.of(this, object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
               @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository)as T
            }
        })[MainActivityViewModel::class.java]
    }
}