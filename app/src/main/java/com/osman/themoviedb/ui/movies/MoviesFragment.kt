package com.osman.themoviedb.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.osman.themoviedb.base.BaseFragment
import com.osman.themoviedb.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<MoviesViewModel>() {

    private var _binding: FragmentMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val moviesListAdapter by lazy {
        MoviesListAdapter {
        }.also {
            it.withLoadStateHeaderAndFooter(
                header = MoviesLoadStateAdapter(it::retry),
                footer = MoviesLoadStateAdapter(it::retry)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObservers()
        initAdapter()
        bindEvents()

    }

    private fun initView() {
        // initial recyclerView
        binding.rcMoviesList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcMoviesList.adapter = moviesListAdapter
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMoviesList().observe(viewLifecycleOwner, {
                moviesListAdapter.submitData(lifecycle, it)
            })
        }
    }

    private fun initAdapter() {
        moviesListAdapter.addLoadStateListener { loadState ->
            // show empty list
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && moviesListAdapter.itemCount == 0
            binding.tvNoResults.isVisible = isListEmpty

            // Only show the list if refresh succeeds.
            binding.rcMoviesList.isVisible = loadState.source.refresh is LoadState.NotLoading

            // Show loading spinner during initial load or refresh.
            handleLoading(loadState.source.refresh is LoadState.Loading)

            // Show the retry state if initial load or refresh fails.
            binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error

            /**
             * loadState.refresh - represents the load state for loading the PagingData for the first time.
             * loadState.prepend - represents the load state for loading data at the start of the list.
             * loadState.append - represents the load state for loading data at the end of the list.
             * */
            // If we have an error, show a toast
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    it.error.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun bindEvents() {
        with(binding) {
            rcMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    if (dy < 0)
                        fab.show()
                    else fab.hide()

                    val scrollPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    refreshLayout.isEnabled = scrollPosition == 0
                    if (scrollPosition == 0) fab.hide()
                }
            })

            refreshLayout.setOnRefreshListener {
                moviesListAdapter.refresh()
            }

            btnRetry.setOnClickListener {
                moviesListAdapter.retry()
            }

            fab.setOnClickListener {
                rcMoviesList.smoothScrollToPosition(0)
            }
        }
    }

    private fun handleLoading(loading: Boolean) {
        with(binding) {
            refreshLayout.isRefreshing = loading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}