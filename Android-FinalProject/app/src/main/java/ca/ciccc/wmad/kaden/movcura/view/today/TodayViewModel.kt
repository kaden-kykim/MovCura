package ca.ciccc.wmad.kaden.movcura.view.today

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import ca.ciccc.wmad.kaden.movcura.data.SimpleMovieData
import ca.ciccc.wmad.kaden.movcura.database.favorite.Favorite
import ca.ciccc.wmad.kaden.movcura.database.favorite.FavoriteDBDao
import ca.ciccc.wmad.kaden.movcura.network.MovieDetail
import ca.ciccc.wmad.kaden.movcura.network.TMDbAPI
import ca.ciccc.wmad.kaden.movcura.network.TodayMovies
import kotlinx.coroutines.*
import kotlin.random.Random

private var nowPlayingList: TodayMovies? = null
private var upcomingList: TodayMovies? = null
private var trendMovieList: TodayMovies? = null

class TodayViewModel(private val database: FavoriteDBDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val favorites = database.getAllFavoriteMoviesByMovieId()
    private val favoritesMap = favorites.value?.map { it.movieID to it }?.toMap()

    init {
        getPopularMovies()

        try {
            nowPlayingList?.let { _nowPlayingMovies.value = getListFromAPIData(it) }
                ?: getNowPlayingMovies()
            upcomingList?.let { _upcomingMovies.value = getListFromAPIData(it) }
                ?: getUpcomingMovies()
            trendMovieList?.let { _trendMovies.value = getListFromAPIData(it) }
                ?: getTrendMovies()
        } catch (e: Exception) {
            // Reload
            getNowPlayingMovies()
            getUpcomingMovies()
            getTrendMovies()
            getPopularMovies()
        }
    }

    private val _navigateToDetail = MutableLiveData<String>()
    val navigateToDetail: LiveData<String>
        get() = _navigateToDetail

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail>
        get() = _movieDetail

    private var _nowPlayingMovies = MutableLiveData<List<SimpleMovieData>>()
    val nowPlayingMovies: LiveData<List<SimpleMovieData>>
        get() = _nowPlayingMovies

    private var _upcomingMovies = MutableLiveData<List<SimpleMovieData>>()
    val upcomingMovies: LiveData<List<SimpleMovieData>>
        get() = _upcomingMovies

    private var _trendMovies = MutableLiveData<List<SimpleMovieData>>()
    val trendMovies: LiveData<List<SimpleMovieData>>
        get() = _trendMovies

    private fun getMovieDetail(movieID: Int) {
        Log.i("Test", "Call getMovie Detail($movieID)")
        uiScope.launch {
            val getMovieDetailDeferred = TMDbAPI.retrofitService.getMovieDetailAsync(movieID)
            try {
                val value = getMovieDetailDeferred.await()
                Log.i("Test", value.toString())
                _movieDetail.value = value
            } catch (e: Exception) {
                Log.i("Test", "Error: ${e.message}")
                _movieDetail.value = null
            }
        }
    }

    private fun getNowPlayingMovies() {
        uiScope.launch {
            val getNowPlayingMovies = TMDbAPI.retrofitService.getNowPlayingMoviesAsync()
            try {
                val nowPlaying = getNowPlayingMovies.await()
                nowPlayingList = nowPlaying
                _nowPlayingMovies.value = getListFromAPIData(nowPlaying)
            } catch (e: Exception) {
                _nowPlayingMovies.value = null
            }
        }
    }

    private fun getUpcomingMovies() {
        uiScope.launch {
            val getUpcomingMovies = TMDbAPI.retrofitService.getUpcomingMoviesAsync()
            try {
                val upcoming = getUpcomingMovies.await()
                upcomingList = upcoming
                _upcomingMovies.value = getListFromAPIData(upcoming)
            } catch (e: Exception) {
                _upcomingMovies.value = null
            }
        }
    }

    private fun getTrendMovies() {
        uiScope.launch {
            val getTrendMovies = TMDbAPI.retrofitService.getTrendMoviesAsync()
            try {
                val trend = getTrendMovies.await()
                trendMovieList = trend
                _trendMovies.value = getListFromAPIData(trend)
            } catch (e: Exception) {
                _trendMovies.value = null
            }
        }
    }

    private fun getPopularMovies() {
        uiScope.launch {
            val getPopularMovie = TMDbAPI.retrofitService.getPopularMoviesAsync()
            try {
                val popular = getPopularMovie.await()
                val popularMovies = popular.results
                getMovieDetail(popularMovies[Random.nextInt(popularMovies.size)].id.toInt())
            } catch (e: Exception) {
                getMovieDetail(330457)
            }
        }
    }

    private fun getListFromAPIData(movies: TodayMovies): MutableList<SimpleMovieData> {
        val movieList = mutableListOf<SimpleMovieData>()
        movies.results.forEach {
            movieList.add(
                SimpleMovieData(
                    it.id.toInt(),
                    it.title,
                    it.posterPath,
                    favoritesMap?.containsKey(it.id.toLong()) ?: false
                )
            )
        }
        return movieList
    }

    fun onMovieDetailClicked(url: String) {
        _navigateToDetail.value = url
    }

    fun onMovieDetailNavigated() {
        _navigateToDetail.value = null
    }

    fun insertFavorite(simpleMovieData: SimpleMovieData) {
        val favorite = Favorite(
            movieID = simpleMovieData.id.toLong(),
            movieTitle = simpleMovieData.title,
            posterPath = simpleMovieData.posterPath
        )
        uiScope.launch {
            insert(favorite)
        }
    }

    fun deleteFavorite(simpleMovieData: SimpleMovieData) {
        uiScope.launch {
            favoritesMap?.get(simpleMovieData.id.toLong())?.let {
                delete(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private suspend fun insert(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            database.insert(favorite)
        }
    }

    private suspend fun delete(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            database.delete(favorite)
        }
    }
}

class TodayViewModelFactory(
    private val dataSource: FavoriteDBDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
            return TodayViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
