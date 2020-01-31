package ca.ciccc.wmad.kaden.movcura.view.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.ciccc.wmad.kaden.movcura.data.SimpleMovieData
import ca.ciccc.wmad.kaden.movcura.databinding.ListViewTodayMovieBinding
import kotlinx.android.synthetic.main.list_view_today_movie.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodayAdapter(
    private val onCheckedChangeListener: OnCheckedChangeListener,
    private val onClickListener: OnClickListener
) :
    ListAdapter<SimpleMovieData, TodayAdapter.SimpleMovieDataViewHolder>(SimpleMovieDataDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private var tmpCover: FrameLayout? = null
    private var tmpPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleMovieDataViewHolder {
        tmpPosition = -1
        tmpCover = null
        return SimpleMovieDataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SimpleMovieDataViewHolder, position: Int) {
        val simpleMovieData = getItem(position) as SimpleMovieData
        holder.itemView.setOnClickListener {
            if (it.layout_list_view_movie_cover.visibility == View.GONE) {
                initPosterCover()
                it.layout_list_view_movie_cover.visibility = View.VISIBLE
                tmpCover = it.layout_list_view_movie_cover
                tmpPosition = position
                onClickListener.onClick(null)
            } else {
                onClickListener.onClick(simpleMovieData)
            }
        }
        holder.itemView.toggleButton_favorite.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener.onCheckedChanged(simpleMovieData, isChecked)
        }
        if (tmpPosition == position) {
            holder.itemView.layout_list_view_movie_cover.visibility = View.VISIBLE
        } else {
            holder.itemView.layout_list_view_movie_cover.visibility = View.GONE
        }

        holder.bind(simpleMovieData)
    }

    fun initPosterCover() {
        tmpCover?.visibility = View.GONE
        tmpPosition = -1
        tmpCover = null
    }

    fun submitSimpleMovieList(list: List<SimpleMovieData>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    class SimpleMovieDataViewHolder private constructor(private var binding: ListViewTodayMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(simpleMovieData: SimpleMovieData) {
            binding.simpleMovieData = simpleMovieData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SimpleMovieDataViewHolder {
                return SimpleMovieDataViewHolder(
                    ListViewTodayMovieBinding.inflate(LayoutInflater.from(parent.context))
                )
            }
        }
    }

    class OnClickListener(val clickListener: (simpleMovieData: SimpleMovieData?) -> Unit) {
        fun onClick(simpleMovieData: SimpleMovieData?) = clickListener(simpleMovieData)
    }

    class OnCheckedChangeListener(
        val checkedChangeListener:
            (SimpleMovieData: SimpleMovieData, isChecked: Boolean) -> Unit
    ) {
        fun onCheckedChanged(simpleMovieData: SimpleMovieData, isChecked: Boolean) =
            checkedChangeListener(simpleMovieData, isChecked)
    }
}

class SimpleMovieDataDiffCallback : DiffUtil.ItemCallback<SimpleMovieData>() {
    override fun areItemsTheSame(oldItem: SimpleMovieData, newItem: SimpleMovieData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SimpleMovieData, newItem: SimpleMovieData): Boolean {
        return oldItem == newItem
    }

}