package ca.ciccc.wmad.kaden.movcura.view.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.ciccc.wmad.kaden.movcura.data.SimpleMovieData
import ca.ciccc.wmad.kaden.movcura.databinding.ListViewTodayMovieBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodayAdapter :
    ListAdapter<SimpleMovieData, TodayAdapter.SimpleMovieDataViewHolder>(SimpleMovieDataDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleMovieDataViewHolder {
        return SimpleMovieDataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SimpleMovieDataViewHolder, position: Int) {
        val simpleMovieData = getItem(position) as SimpleMovieData
        holder.bind(simpleMovieData)
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
}

class SimpleMovieDataDiffCallback : DiffUtil.ItemCallback<SimpleMovieData>() {
    override fun areItemsTheSame(oldItem: SimpleMovieData, newItem: SimpleMovieData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SimpleMovieData, newItem: SimpleMovieData): Boolean {
        return oldItem == newItem
    }

}