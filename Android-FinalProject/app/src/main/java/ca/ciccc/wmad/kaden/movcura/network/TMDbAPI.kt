package ca.ciccc.wmad.kaden.movcura.network

import ca.ciccc.wmad.kaden.movcura.BuildConfig
import ca.ciccc.wmad.kaden.movcura.global.TMDbConfData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val DEFAULT_LANG = "en-CA"
private const val DEFAULT_REGION = "CA"
private const val DEFAULT_PAGE = 1

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL).build()

interface TMDbApiService {
    @GET("movie/{movie_id}?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getMovieDetailAsync(
        @Path("movie_id") movieID: Int,
        @Query("language") lang: String = DEFAULT_LANG
    ): Deferred<MovieDetail>

    @GET("movie/{movie_id}?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getMovieRecommendations(
        @Path("movie_id") movieID: Int,
        @Query("language") lang: String = DEFAULT_LANG,
        @Query("page") page: Int = DEFAULT_PAGE
    ): Deferred<TodayMovies>

    @GET("trending/movie/day?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getTrendMoviesAsync(): Deferred<TodayMovies>

    @GET("movie/now_playing?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getNowPlayingMoviesAsync(
        @Query("language") lang: String = DEFAULT_LANG,
        @Query("region") region: String = DEFAULT_REGION,
        @Query("page") page: Int = DEFAULT_PAGE
    ): Deferred<TodayMovies>

    @GET("movie/upcoming?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getUpcomingMoviesAsync(
        @Query("language") lang: String = DEFAULT_LANG,
        @Query("region") region: String = DEFAULT_REGION,
        @Query("page") page: Int = DEFAULT_PAGE
    ): Deferred<TodayMovies>

    @GET("movie/popular?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getPopularMoviesAsync(
        @Query("language") lang: String = DEFAULT_LANG,
        @Query("region") region: String = DEFAULT_REGION,
        @Query("page") page: Int = DEFAULT_PAGE
    ): Deferred<TodayMovies>

    @GET("configuration?api_key=${BuildConfig.THE_MOVIE_DB_API_TOKEN}")
    fun getConfigurationAsync(): Deferred<TMDbConfData>
}

object TMDbAPI {
    val retrofitService: TMDbApiService by lazy {
        retrofit.create(TMDbApiService::class.java)
    }
}