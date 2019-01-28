package com.aar.app.moneymanager.common.easyadapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SimpleAdapterDelegate<T>(modelClass: Class<T>, @param:LayoutRes private val mLayoutRes: Int,
                               private val mBinder: Binder<T>, private val mItemClickListener: OnItemClickListener<T>?) : AdapterDelegate<T, SimpleAdapterDelegate.ViewHolder>(modelClass) {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(mLayoutRes, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(model: T, holder: ViewHolder) {
        holder.itemView.setOnClickListener { v -> mItemClickListener?.onClick(model, v) }
        mBinder.bind(model, holder)
    }

    interface Binder<T> {
        fun bind(model: T, holder: ViewHolder)
    }

    interface OnItemClickListener<T> {
        fun onClick(model: T, view: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cache = SparseArray<View>()

        fun <T : View> find(resId: Int): T {
            var v: View? = cache.get(resId)
            if (v != null) {
                return v as T
            }
            v = itemView.findViewById(resId)
            cache.put(resId, v)
            return v as T
        }
    }
}
