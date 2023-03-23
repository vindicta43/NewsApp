package com.alperen.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.newsapp.R
import com.alperen.newsapp.adapters.NewsAdapter
import com.alperen.newsapp.databinding.FragmentBreakingNewsBinding
import com.alperen.newsapp.ui.NewsActivity
import com.alperen.newsapp.ui.NewsViewModel
import com.alperen.newsapp.util.Resource
import com.alperen.newsapp.util.hide
import com.alperen.newsapp.util.show
import com.google.android.material.snackbar.Snackbar

class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        newsAdapter.itemClickListener { article ->
            BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
                .apply {
                    findNavController().navigate(this)
                }
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { newsResponse ->
            Log.d("myViewModelData", "data retrieved")
            when (newsResponse) {
                is Resource.Success -> {
                    binding.apply {
                        paginationProgressBar.hide()
                        refreshLayout.isRefreshing = false
                    }
                    newsResponse.data?.let { newsData ->
                        newsAdapter.differ.submitList(newsData.articles)
                    }
                }
                is Resource.Error -> {
                    binding.apply {
                        paginationProgressBar.hide()
                        refreshLayout.isRefreshing = false
                    }
                    newsResponse.message?.let { message ->
                        Snackbar.make(
                            binding.root, "An error occurred: $message", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.show()
                }
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getBreakingNews("us")
            viewModel.breakingNewsPage = 1
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}