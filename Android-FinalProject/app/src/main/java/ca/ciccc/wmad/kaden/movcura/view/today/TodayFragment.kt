package ca.ciccc.wmad.kaden.movcura.view.today

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.database.favorite.FavoriteDB
import ca.ciccc.wmad.kaden.movcura.databinding.FragmentTodayBinding
import ca.ciccc.wmad.kaden.movcura.global.TMDbConfiguration

class TodayFragment : Fragment() {

    lateinit var nowPlayingAdapter: TodayAdapter
    lateinit var upcomingAdapter: TodayAdapter
    lateinit var trendAdapter: TodayAdapter

    @SuppressLint("ClickableViewAccessibility")
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
                if (isChecked) {
                    viewModel.insertFavorite(data)
                } else {
                    viewModel.deleteFavorite(data)
                }
            },
            TodayAdapter.OnClickListener {
                it?.let {
                    viewModel.onMovieDetailClicked("${TMDbConfiguration.baseDetailUrl}${it.id}")
                    nowPlayingAdapter.initPosterCover()
                }
                it ?: let {
                    upcomingAdapter.initPosterCover()
                    trendAdapter.initPosterCover()
                }

            })
        upcomingAdapter = TodayAdapter(
            TodayAdapter.OnCheckedChangeListener { data, isChecked ->
                if (isChecked) {
                    viewModel.insertFavorite(data)
                } else {
                    viewModel.deleteFavorite(data)
                }
            },
            TodayAdapter.OnClickListener {
                it?.let {
                    viewModel.onMovieDetailClicked("${TMDbConfiguration.baseDetailUrl}${it.id}")
                    upcomingAdapter.initPosterCover()
                }
                it ?: let {
                    nowPlayingAdapter.initPosterCover()
                    trendAdapter.initPosterCover()
                }
            })
        trendAdapter = TodayAdapter(
            TodayAdapter.OnCheckedChangeListener { data, isChecked ->
                if (isChecked) {
                    viewModel.insertFavorite(data)
                } else {
                    viewModel.deleteFavorite(data)
                }
            },
            TodayAdapter.OnClickListener {
                it?.let {
                    viewModel.onMovieDetailClicked("${TMDbConfiguration.baseDetailUrl}${it.id}")
                    trendAdapter.initPosterCover()
                }
                it ?: let {
                    nowPlayingAdapter.initPosterCover()
                    upcomingAdapter.initPosterCover()
                }
            })

        binding.layoutTodayNowPlaying.layoutTodayMovieList.adapter = nowPlayingAdapter
        binding.layoutTodayUpcoming.layoutTodayMovieList.adapter = upcomingAdapter
        binding.layoutTodayTrendMovies.layoutTodayMovieList.adapter = trendAdapter

        binding.layoutTodayPickContent.textViewTodayPickTitle.text =
            getString(R.string.fragment_today_pick_title)
        binding.layoutTodayNowPlaying.textViewTodayListTitle.text =
            getString(R.string.fragment_today_layout_now_playing_title)
        binding.layoutTodayUpcoming.textViewTodayListTitle.text =
            getString(R.string.fragment_today_layout_upcoming_title)
        binding.layoutTodayTrendMovies.textViewTodayListTitle.text =
            getString(R.string.fragment_today_layout_trend_movie_title)

        binding.layoutTodayPickContent.scrollViewPickOverview.setOnTouchListener { v: View, m: MotionEvent ->
            binding.scrollViewTodayMain.requestDisallowInterceptTouchEvent(true)
            false
        }

        binding.layoutTodayPickContent.layoutTodayPickMovieInform.setOnClickListener {
            viewModel.onMovieDetailClicked(
                "${TMDbConfiguration.baseDetailUrl}${binding.layoutTodayPickContent.movieDetail?.id}")
        }

        viewModel.movieDetail.observe(this, Observer {
            it?.let {
                binding.layoutTodayPickContent.movieDetail = it
                binding.executePendingBindings()

                binding.layoutTodayPickContent.todayPickReleaseYear.text = it.releaseDate.substring(0, 4)
                binding.layoutTodayPickContent.textViewTodayPickMovieTitle.text = it.title

            }
        })

        viewModel.navigateToDetail.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                    TodayFragmentDirections.actionTodayFragmentToDetailFragment(it)
                )
                viewModel.onMovieDetailNavigated()
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
