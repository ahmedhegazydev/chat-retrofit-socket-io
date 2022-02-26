package com.task.newsapp.adapter


import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import com.task.newsapp.R
import com.task.newsapp.data.model.SocketUser
import com.task.newsapp.util.decodeImage
import kotlinx.android.synthetic.main.item_list_users.view.*

class UserAdapter :
    BaseUserAdapter(R.layout.item_list_users) {
    override val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = dataList[position] as AllUsers.Data
        val user = dataList[position] as SocketUser
        holder.itemView.apply {
            image.setImageBitmap(decodeImage(user.image))
//            Glide.with(image.context)
//                .load(user.img)
//                .into(image)
            usernaem.text = user.name
            isOnline.setCardBackgroundColor(
                ResourcesCompat.getColor(
                    context.resources,
                    if (user.isOnline) R.color.colorGreen else R.color.grey,
                    null
                )
            )
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(user, false)
                }
            }
        }
    }
}