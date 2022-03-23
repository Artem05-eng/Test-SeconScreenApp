package com.example.test_app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_app.adapter.PagingPhotoAdapter
import com.example.test_app.app.App
import com.example.test_app.data.Photo
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFragment: Fragment(R.layout.list_fragment) {

    @Inject
    lateinit var viewModel: ListViewModel
    private var photoAdapter: PagingPhotoAdapter? = null
    private val mistake = SingleLiveEvent<Throwable?>()
    private var list = emptyList<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoAdapter = PagingPhotoAdapter (mistake, { position -> navigate(list[position]) })
        list = photoAdapter?.snapshot()?.items ?: emptyList()
        with(listPhoto) {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        mistake.observe(viewLifecycleOwner) {
            if (it!= null) {
                Toast.makeText(requireContext(), "Connection error!", Toast.LENGTH_SHORT).show()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPhotosFromNetworkandDB().collectLatest {
                photoAdapter?.submitData(it)
                list = photoAdapter?.snapshot()?.items ?: emptyList()
            }
        }
        photoAdapter?.addLoadStateListener { state ->
            val error = (state.refresh == LoadState.Error(Throwable())) || (state.append == LoadState.Error(Throwable()))
            lentaError.isVisible = error && !(state.refresh == LoadState.Loading)
            listPhoto.isVisible = (state.refresh != LoadState.Loading) && !error
            lentaProgress.isVisible = state.refresh == LoadState.Loading
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        photoAdapter = null
    }

    private fun navigate(photo: Photo) {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment(data = photo)
        findNavController().navigate(action)
    }
}