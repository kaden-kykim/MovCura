package ca.ciccc.wmad.kaden.movcura.network

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("id")
    val id: Double,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("overview")
    val overview: String
) {
    override fun toString(): String {
        return "MovieDetail(id=$id,\n" +
                "title='$title',\n" +
                "posterPath='$posterPath',\n" +
                "backdropPath='$backdropPath',\n" +
                "releaseDate='$releaseDate',\n" +
                "overview='$overview')"
    }
}

data class TodayMovies(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total_results")
    val totalResults: Double
) {
    data class Result(
        @SerializedName("id")
        val id: Double,
        @SerializedName("title")
        val title: String,
        @SerializedName("poster_path")
        val posterPath: String
    )
}