package com.example.cimon_chilimonitoring.ui.blog

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cimon_chilimonitoring.data.remote.response.ResultsItemBlog
import com.example.cimon_chilimonitoring.databinding.ItemCardViewBlogBinding
import com.example.cimon_chilimonitoring.ui.blog.detail.BlogDetailActivity
import java.text.SimpleDateFormat
import java.util.Locale

class BlogAdapter : ListAdapter<ResultsItemBlog, BlogAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardViewBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    class MyViewHolder(val binding: ItemCardViewBlogBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(blog: ResultsItemBlog) {
            with(binding){
                tvItemTitle.text = blog.title
                tvItemDate.text = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(blog.createdAt)
                    ?.let {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                            it
                        )
                    }
                tvItemSource.text = blog.description
            }
            Glide.with(itemView.context)
                .load(blog.imageUrl)
                .into(binding.imgPoster)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, BlogDetailActivity::class.java).apply {
                    putExtra(BlogDetailActivity.EXTRA_BLOG, blog)
                }
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    companion object {
        // untuk memeriksa apakah suatu data masih sama atau tidak
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItemBlog>() {
            override fun areItemsTheSame(oldItem: ResultsItemBlog, newItem: ResultsItemBlog): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ResultsItemBlog, newItem: ResultsItemBlog): Boolean {
                return oldItem == newItem
            }
        }
    }
}