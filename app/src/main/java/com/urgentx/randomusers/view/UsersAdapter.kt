package com.urgentx.randomusers.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urgentx.randomusers.R
import com.urgentx.randomusers.model.User
import kotlinx.android.synthetic.main.user_item.view.*

class UsersAdapter() : PagedListAdapter<User, UserViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User? = getItem(position)
        // Note that "user" is a placeholder if it's null.
        holder.bindTo(user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserViewHolder(v)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.id == newItem.id

            // If you use the "==" operator, make sure that the object implements
            // .equals(). Alternatively, write custom data comparison logic here.
            override fun areContentsTheSame(
                oldItem: User, newItem: User
            ) = oldItem == newItem
        }
    }
}

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name = view.user_item_name
    val gender = view.user_item_gender
    val dob = view.user_item_dob
    val picture = view.user_item_pic
    fun bindTo(user: User?) {
        name.text = user?.name?.let { "${it.title} ${it.first} ${it.last}" }
        gender.text = user?.gender
        dob.text = user?.dob?.date.toString()
        Glide.with(itemView.context).load(user?.picture?.highestRes() ?: "").into(picture)
        picture.setOnClickListener {
            (itemView.context as? AppCompatActivity)?.let { activity ->
                user?.let {
                    DetailDialogFragment.newInstance(it).show(activity.supportFragmentManager, "")
                }
            }
        }
    }
}
