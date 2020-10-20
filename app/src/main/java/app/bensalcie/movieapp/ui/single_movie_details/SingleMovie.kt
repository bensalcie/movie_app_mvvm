package app.bensalcie.movieapp.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import app.bensalcie.movieapp.R
import app.bensalcie.movieapp.data.api.POSTER_BASE_URL
import app.bensalcie.movieapp.data.api.TheMovieDbClient
import app.bensalcie.movieapp.data.api.TheMovieDbInterface
import app.bensalcie.movieapp.data.repository.NetworkState
import app.bensalcie.movieapp.data.vo.MovieDetails
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId =intent.getIntExtra("id",1)
        val apiService :TheMovieDbInterface= TheMovieDbClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)
        viewModel=getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.movieDetailsNetworkState.observe(this, {
            progress_bar.visibility= if (it== NetworkState.LOADING)View.VISIBLE else View.GONE
            txt_error.visibility= if (it==NetworkState.ERROR)View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(movie: MovieDetails) {

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_title.text=movie.title
        movie_budget.text= movie.budget.toString()
        movie_overview.text=movie.overview
        ratingbar.rating = movie.voteAverage.toFloat()
//        movie_rating.text = movie.voteAverage.toString()
        movie_release_date.text=movie.releaseDate
        movie_revenue.text = formatCurrency.format(movie.revenue)
        movie_runtime.text = movie.runtime.toString()+" minutes "
        movie_tagline.text =movie.tagline

        val moviePosterUrl = POSTER_BASE_URL+movie.posterPath
        Glide.with(this).load(moviePosterUrl).into(iv_movie_poster)
    }

    private fun getViewModel (movieId:Int):SingleMovieViewModel{
        return ViewModelProviders.of(this,object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}