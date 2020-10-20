package app.bensalcie.movieapp.data.api

import app.bensalcie.movieapp.data.vo.MovieDetails
import app.bensalcie.movieapp.data.vo.MovieReponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbInterface {
    @GET("movie/popular")
    //single is one type of observable in rxjava
    fun getPopularMovie(@Query ("page")page:Int):Single<MovieReponse>

    @GET("movie/{movie_id}")
    //single is one type of observable in rxjava
    fun getMovieDetails(@Path("movie_id")id:Int):Single<MovieDetails>


}