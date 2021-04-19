package app.bensalcie.movieappnew.data.vo


import com.google.gson.annotations.SerializedName

data class Movie(

    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("id")
    val id: Int
)