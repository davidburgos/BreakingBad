package com.breakingbad.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.breakingbad.R
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A Detail [Fragment] to display the character information.
 */
class DetailFragment : Fragment(R.layout.fragment_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_second.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
