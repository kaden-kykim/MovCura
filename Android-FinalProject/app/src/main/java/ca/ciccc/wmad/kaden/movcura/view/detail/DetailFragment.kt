package ca.ciccc.wmad.kaden.movcura.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.ciccc.wmad.kaden.movcura.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.webViewMovieDetail.settings.javaScriptEnabled = true
        binding.webViewMovieDetail.webViewClient = WebViewClient()

        arguments?.let {
            binding.webViewMovieDetail.loadUrl(DetailFragmentArgs.fromBundle(arguments!!).movieUrl)
        }

        return binding.root
    }

}
