package com.daniel.ramos.projetotcc.presenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.daniel.ramos.projetotcc.databinding.ListCoresBinding
import com.daniel.ramos.projetotcc.presenter.enums.Cores

class ColorArrayAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val values: List<Cores>
) : ArrayAdapter<Cores>(context, layoutResource, values) {

    private class ViewHolder internal constructor(binding: ListCoresBinding) {
        var view: View = binding.root
        val binding: ListCoresBinding = binding
    }

    override fun getItem(position: Int): Cores = values[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val holder: ViewHolder
        if (convertView == null) {
            val itemBinding = ListCoresBinding.inflate(LayoutInflater.from(context), parent, false)
            holder = ViewHolder(itemBinding)
            holder.view = itemBinding.root
            holder.view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val cor = values[position]

        holder.binding.tvCor.text = cor.nome
        holder.binding.ivCor.setBackgroundResource(cor.rgb)

        return holder.view
    }
}