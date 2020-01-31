package ca.ciccc.wmad.kaden.movcura.view.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.database.favorite.FavoriteDB
import ca.ciccc.wmad.kaden.movcura.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    lateinit var nowPlayingAdapter: TodayAdapter
    lateinit var upcomingAdapter: TodayAdapter
    lateinit var trendAdapter: TodayAdapter

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

        nowPlayingAdapter = TodayAdapter(
            TodayAdapter.OnCheckedChangeListener { data, isChecked ->
                Toast.makeText(context, "Toggle: $isChecked", Toast.LENGTH_SHORT).show()
            },
            TodayAdapter.OnClickListener {
                it?.let {
                    Toast.makeText(context, "Click on ${it.title}", Toast.LENGTH_SHORT).show()
                    nowPlayingAdapter.initPosterCover()
                }
                it?:let {
                    upcomingAdapter.initPosterCover()
                    trendAdapter.initPosterCover()
                }

            })
        upcomingAdapter = TodayAdapter(
            TodayAdapter.OnCheckedChangeListener { data, isChecked ->

            },
            TodayAdapter.OnClickListener {
                it?.let {
                    Toast.makeText(context, "Click on ${it.title}", Toast.LENGTH_SHORT).show()
                    upcomingAdapter.initPosterCover()
                }
                it?:let {
                    nowPlayingAdapter.initPosterCover()
                    trendAdapter.initPosterCover()
                }
            })
        trendAdapter = TodayAdapter(
            TodayAdapter.OnCheckedChangeListener { data, isChecked ->

            },
            TodayAdapter.OnClickListener {
                it?.let {
                    Toast.makeText(context, "Click on ${it.title}", Toast.LENGTH_SHORT).show()
                    trendAdapter.initPosterCover()
                }
                it?:let {
                    nowPlayingAdapter.initPosterCover()
                    upcomingAdapter.initPosterCover()
                }
            })

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
