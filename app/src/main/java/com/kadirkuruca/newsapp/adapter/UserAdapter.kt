package com.kadirkuruca.newsapp.adapter


import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.Glide
import com.kadirkuruca.newsapp.R
import com.kadirkuruca.newsapp.data.model.AllUsers
import com.kadirkuruca.newsapp.data.model.SocketUser
import com.kadirkuruca.newsapp.util.decodeImage
import kotlinx.android.synthetic.main.item_list_users.view.*

class UserAdapter :
    BaseUserAdapter(R.layout.item_list_users) {
    override val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataList[position] as AllUsers.Data
        holder.itemView.apply {
//            image.setImageBitmap(decodeImage(user.img))
            Glide.with(image.context)
                .load(user.img)
                .into(image)
            usernaem.text = user.fullname
//            isOnline.setCardBackgroundColor(
//                ResourcesCompat.getColor(
//                    context.resources,
//                    if (user.isOnline) R.color.colorGreen else R.color.grey,
//                    null
//                )
//            )
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(user, false)
                }
            }
        }
    }
}