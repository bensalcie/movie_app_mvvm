package app.bensalcie.movieappnew.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.bensalcie.movieappnew.R
import app.bensalcie.movieappnew.data.api.TheMovieDbClient
import app.bensalcie.movieappnew.data.api.TheMovieDbInterface
import app.bensalcie.movieappnew.data.repository.NetworkState
import app.bensalcie.movieappnew.ui.popularmovie.MoviePageListRepository
import app.bensalcie.movieappnew.ui.popularmovie.TopRatedMoviePagedListAdapter

class DashboardFragment : Fragment() {
    private lateinit var viewModel: DashboardViewModel
    private lateinit var movieRepository: MoviePageListRepository
    private lateinit var  root: View
    private lateinit var ctx: Context
    private lateinit var txt_error_popular:TextView
    private lateinit var progress_bar_popular: ProgressBar
    private lateinit var rv_movie_list: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ctx= container!!.context
        root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        rv_movie_list = root.findViewById(R.id.rv_movie_list)
        txt_error_popular = root.findViewById(R.id.txt_error_popular)
        progress_bar_popular = root.findViewById(R.id.progress_bar_popular)

        val apiService : TheMovieDbInterface = TheMovieDbClient.getClient()
        movieRepository = MoviePageListRepository(apiService)
        viewModel=getViewModel()
        val movieAdapter = TopRatedMoviePagedListAdapter(ctx)
        val gridLayoutManager = GridLayoutManager(ctx,3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
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

        viewModel.moviePagedList.observe(viewLifecycleOwner,{
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner,{
            progress_bar_popular.visibility= if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =  if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })


        return root
    }
    private fun getViewModel(): DashboardViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(movieRepository) as T
            }
        })[DashboardViewModel::class.java]
    }

}