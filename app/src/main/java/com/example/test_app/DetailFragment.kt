package com.example.test_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment: Fragment(R.layout.detail_fragment) {

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photo = args.data
        photographerInfo.text = photo.photographer
        comment.text = """
            ${photo.alt}
        """.trimIndent()
        Glide.with(this)
            .load(photo.src.original)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(detailImage)
    }

}