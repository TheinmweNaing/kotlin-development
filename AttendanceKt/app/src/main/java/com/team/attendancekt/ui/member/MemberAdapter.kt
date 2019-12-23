package com.team.attendancekt.ui.member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.attendancekt.BR
import com.team.attendancekt.R
import com.team.attendancekt.model.entity.Member

class MemberAdapter : ListAdapter<Member, MemberAdapter.MemberViewHolder>(DIFF_CALLBACK) {

    interface AdapterItemClickListener {
        fun onClick(position: Int)
    }

    var adapterItemClickListener: AdapterItemClickListener? = null

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Member>() {
            override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem.equals(newItem)
            }

        }
    }

    inner class MemberViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterItemClickListener?.onClick(adapterPosition)
            }
        }

        fun bind(obj: Member) {
            binding.setVariable(BR.obj, obj)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_member, parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAt(position: Int) = getItem(position)

}