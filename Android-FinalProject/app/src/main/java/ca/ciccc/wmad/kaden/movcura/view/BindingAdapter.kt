package ca.ciccc.wmad.kaden.movcura.view

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.global.TMDbConfiguration
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("movieTitleString")
fun setMovieTitle(textView: TextView, movieTitle: String?) {
    movieTitle?.let { textView.text = it }
}

@BindingAdapter("setTextString")
fun setTextString(textView: TextView, string: String?) {
    string?.let { textView.text = it }
}

@BindingAdapter("posterPathString")
fun setPosterPath(imageView: ImageView, posterPath: String?) {
    posterPath?.let {
        val imgUri =
            "${TMDbConfiguration.baseImageUrl}w500$posterPath".toUri()
                .buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}