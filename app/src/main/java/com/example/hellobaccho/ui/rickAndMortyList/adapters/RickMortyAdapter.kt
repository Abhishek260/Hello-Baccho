package com.example.hellobaccho.ui.rickAndMortyList.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hellobaccho.R
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.databinding.ItemRickMortyListBinding
import com.example.hellobaccho.interfaces.OnCardClick
import com.squareup.picasso.Picasso
import java.util.Locale
import javax.inject.Inject

class RickMortyAdapter @Inject constructor(
    private val rickMortyList: List<Result>,
    private val onCardClick: OnCardClick<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var filterList: List<Result> = rickMortyList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemRickMortyListBinding.inflate(inflater, parent, false)
        return RickMortyViewHolder(layoutBinding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataHolder = holder as RickMortyAdapter.RickMortyViewHolder
        val dataModel: Result = filterList[position]
        dataHolder.onBind(dataModel, onCardClick)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    inner class RickMortyViewHolder(
        private val layoutBinding: ItemRickMortyListBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(layoutBinding.root) {

        fun onBind(rickMortyData: Result, onCardClick: OnCardClick<Any>) {
            layoutBinding.rickMortyData = rickMortyData
            layoutBinding.clickBtn.setOnClickListener {
                onCardClick.onClick(rickMortyData, "Episode")
            }
            layoutBinding.rickMortyDataCard.setOnClickListener {
                onCardClick.onCardClick(rickMortyData,"Card",adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                filterList = if (charString.isEmpty()) {
                    rickMortyList
                } else {
                    rickMortyList.filter {
                        it.id?.toString()?.contains(charString.lowercase(Locale.getDefault())) == true ||
                                it.name?.lowercase(Locale.getDefault())?.contains(charString.lowercase(Locale.getDefault())) == true
                    }
                }
                return FilterResults().apply { values = filterList }
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filterList = filterResults.values as List<Result>
                notifyDataSetChanged()
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.emptylogo) // Optional placeholder image
            .error(R.drawable.emptylogo)       // Optional error image
            .into(view)
    }
}
