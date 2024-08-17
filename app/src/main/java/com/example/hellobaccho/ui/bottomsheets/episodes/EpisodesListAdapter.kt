package com.example.hellobaccho.ui.bottomsheets.episodes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.hellobaccho.databinding.ItemEpisodeListBinding
import com.example.hellobaccho.interfaces.OnCardClick
import java.util.Locale
import javax.inject.Inject

class EpisodesListAdapter  constructor(
    private val episodesList: List<String>? = null,
    private val onCardClick: OnCardClick<Any>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {

    companion object {
        const val EPISODE_SELECTION_CLICK: String = "EPISODE_LIST_CLICK"
    }

    private var filterList: List<String>? = null

    init {
        filterList = episodesList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemEpisodeListBinding.inflate(inflater, parent, false)
        return EpisodeListViewHolder(layoutBinding, parent.context)
    }

    override fun getItemCount(): Int {
        return filterList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as EpisodesListAdapter.EpisodeListViewHolder
        val episode: String? = filterList?.get(position)
        if (episode != null) {
            dataHolder.onBind(episode, onCardClick)
        }
    }

    inner class EpisodeListViewHolder(
        private val layoutBinding: ItemEpisodeListBinding,
        private val mContext: Context
    ) : RecyclerView.ViewHolder(layoutBinding.root) {
        fun onBind(episodes: Any, onCardClick: OnCardClick<Any>?) {
            val displayName = getEpisodeTitle(episodes.toString())
            layoutBinding.episodeName.text = displayName
            layoutBinding.layout.setOnClickListener {
                onCardClick?.onClick(episodes, EPISODE_SELECTION_CLICK)
            }
        }
        private fun getEpisodeTitle(url: String): String {
            val episodeNumber = url.substringAfterLast("/")
            return " Episode $episodeNumber"
        }
    }


    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = episodesList
                } else {
                    val filteredList: MutableList<String> = ArrayList()
                    if (episodesList != null) {
                        for (row in episodesList) {
                            if (row.lowercase().contains(charString.lowercase(Locale.getDefault()))) {
                                filteredList.add(row)
                            }
                        }
                    }
                    filterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                filterList = filterResults.values as List<String>
                notifyDataSetChanged()
            }
        }
    }
}
