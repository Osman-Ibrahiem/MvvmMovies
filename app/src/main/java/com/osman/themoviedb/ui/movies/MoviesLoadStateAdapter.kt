package com.osman.themoviedb.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osman.themoviedb.R
import com.osman.themoviedb.databinding.ItemLoaderBinding

class MoviesLoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<MoviesLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState,
    ) = holder.bind(loadState)

    class LoadStateViewHolder(
        parent: ViewGroup,
        private val retry: () -> Unit,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loader, parent, false)
    ) {
        private val binding = ItemLoaderBinding.bind(itemView)

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.message.text = loadState.error.localizedMessage
            }

            binding.loading.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
            binding.message.isVisible = loadState is LoadState.Error

            binding.btnRetry.setOnClickListener { retry() }
        }
    }
}