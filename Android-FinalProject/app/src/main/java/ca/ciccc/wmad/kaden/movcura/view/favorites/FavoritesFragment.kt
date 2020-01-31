package ca.ciccc.wmad.kaden.movcura.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFavoritesBinding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

}
