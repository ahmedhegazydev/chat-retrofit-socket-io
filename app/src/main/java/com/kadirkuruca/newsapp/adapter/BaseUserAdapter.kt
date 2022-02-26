package com.kadirkuruca.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kadirkuruca.newsapp.data.model.Group
import com.kadirkuruca.newsapp.data.model.SocketUser


abstract class BaseUserAdapter(
    private val layoutId: Int
) : RecyclerView.Adapter<BaseUserAdapter.UserViewHolder>() {

    class UserViewHolder(item: View) : RecyclerView.ViewHolder(item)


    protected val diffCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is SocketUser && newItem is SocketUser)
                return oldItem.id == newItem.id
            else if (oldItem is Group && newItem is Group) {
                return oldItem.id == newItem.id
            }
            return false
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is SocketUser && newItem is SocketUser)
                return oldItem.hashCode() == newItem.hashCode()
            else if (oldItem is Group && newItem is Group) {
                return oldItem.hashCode() == newItem.hashCode()
            }
            return false
        }
    }

    protected abstract val differ: AsyncListDiffer<Any>

    var dataList
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }


    protected var onItemClickListener: ((Any, Boolean) -> Unit)? = null

    fun setItemClickListener(listener: (Any, Boolean) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}