package ca.ciccc.wmad.kaden.movcura.view.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.database.favorite.FavoriteDB
import ca.ciccc.wmad.kaden.movcura.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val binding = FragmentTodayBinding.inflate(inflater)
        val dataSource = FavoriteDB.getInstance(application).favoriteDBDao
        val viewModel =
            ViewModelProvider(this, TodayViewModelFactory(dataSource, application))
                .get(TodayViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val nowPlayingAdapter = TodayAdapter()
        val upcomingAdapter = TodayAdapter()
        val trendAdapter = TodayAdapter()

        binding.layoutTodayNowPlaying.layoutTodayMovieList.adapter = nowPlayingAdapter
        binding.layoutTodayUpcoming.layoutTodayMovieList.adapter = upcomingAdapter
        binding.layoutTodayTrendMovies.layoutTodayMovieList.adapter = trendAdapter

        binding.layoutTodayNowPlaying.layoutTodayListTitle.text =
            getString(R.string.fragment_today_layout_now_playing_title)
        binding.layoutTodayUpcoming.layoutTodayListTitle.text =
            getString(R.string.fragment_today_layout_upcoming_title)
        binding.layoutTodayTrendMovies.layoutTodayListTitle.text =
            getString(R.string.fragment_today_layout_trend_movie_title)

        viewModel.movieDetail.observe(this, Observer {
            it?.let {
                binding.textViewMovieDetail.text = it.toString()
            }
        })

        viewModel.nowPlayingMovies.observe(this, Observer {
            it?.let {
                nowPlayingAdapter.submitSimpleMovieList(it)
            }
        })

        viewModel.upcomingMovies.observe(this, Observer {
            it?.let {
                upcomingAdapter.submitSimpleMovieList(it)
            }
        })

        viewModel.trendMovies.observe(this, Observer {
            it?.let {
                trendAdapter.submitSimpleMovieList(it)
            }
        })

        return binding.root
    }
}
