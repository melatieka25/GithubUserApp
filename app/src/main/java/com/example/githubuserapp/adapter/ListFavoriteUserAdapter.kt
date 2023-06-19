package com.example.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ItemRowUserBinding
import com.example.githubuserapp.favorite.FavoriteUser

class ListFavoriteUserAdapter(private val listFavoriteUser: ArrayList<FavoriteUser>) :
    RecyclerView.Adapter<ListFavoriteUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatarUrl, url) = listFavoriteUser[position]
        with(holder.binding) {
            tvItemName.text = username
            tvItemDescription.text = url
            Glide.with(holder.itemView.context).load(avatarUrl).into(userAvatarImg)
        }


        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavoriteUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavoriteUser.size

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {}

}