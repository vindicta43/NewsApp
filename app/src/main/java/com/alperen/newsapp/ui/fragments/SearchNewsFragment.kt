package com.alperen.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.newsapp.R
import com.alperen.newsapp.adapters.NewsAdapter
import com.alperen.newsapp.databinding.FragmentSearchNewsBinding
import com.alperen.newsapp.ui.NewsActivity
import com.alperen.newsapp.ui.NewsViewModel
import com.alperen.newsapp.util.Constants
import com.alperen.newsapp.util.Resource
import com.alperen.newsapp.util.hide
import com.alperen.newsapp.util.show
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class SearchNewsFragment : Fragment() {

    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(layoutInflater)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        var job: Job? = null

        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }

        newsAdapter.itemClickListener { article ->
            SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article)
                .apply {
                    findNavController().navigate(this)
                }
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { newsResponse ->
            when (newsResponse) {
                is Resource.Success -> {
                    binding.paginationProgressBar.hide()
                    newsResponse.data?.let { newsData ->
                        newsAdapter.differ.submitList(newsData.articles)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.hide()
                    newsResponse.message?.let { message ->
                        Snackbar.make(
                            binding.root,
                            "An error occurred: $message",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}