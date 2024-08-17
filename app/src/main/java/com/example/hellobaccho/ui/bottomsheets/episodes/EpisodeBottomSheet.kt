package com.example.hellobaccho.ui.bottomsheets.episodes

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hellobaccho.R
import com.example.hellobaccho.base.BaseFragment
import com.example.hellobaccho.databinding.EpisodeBottomSheetBinding
import com.example.hellobaccho.databinding.ItemEpisodeListBinding
import com.example.hellobaccho.interfaces.OnCardClick
import com.example.hellobaccho.ui.rickAndMortyList.RickMortyListActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeBottomSheet  @Inject constructor(): BaseFragment(), OnCardClick<Any> {

    private lateinit var layoutBinding: EpisodeBottomSheetBinding
    private var onCardClick:OnCardClick<Any> = this
    private var onEpisodeSelected:OnCardClick<Any>? = null
    private var title: String = ""
    private var episodeClickType:String? = null
    private var rvAdapter: EpisodesListAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var episodes:List<String>? = null

    companion object{
        const val TAG = "EPISODE_BOTTOM_SHEET"
        const val EPISODE_CLICK_TYPE = "EPISODE_LIST_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            onCardSelected: OnCardClick<Any>,
            episodes:List<String>?
        ): EpisodeBottomSheet {
            val instance: EpisodeBottomSheet = EpisodeBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onEpisodeSelected = onCardSelected
            instance.episodes = episodes
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = EpisodeBottomSheetBinding.inflate(layoutInflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            layoutBinding.toolbarTitle.text = title
        successToast(episodes?.size.toString())

        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet: FrameLayout =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;
            layoutBinding.extraSpace.minimumHeight =
                Resources.getSystem().displayMetrics.heightPixels / 2

            behavior.isDraggable = false

        }
        layoutBinding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                rvAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter?.filter?.filter(newText)
                return false
            }

        })
        setupRecyclerView()
        close()
    }

    override fun onClick(data: Any, clickType: String) {
        if (onEpisodeSelected != null) {
            if (clickType == EpisodesListAdapter.EPISODE_SELECTION_CLICK) {
                onEpisodeSelected?.onClick(data, clickType)
                dismiss()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        onEpisodeSelected = null
    }

    private fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }
    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = EpisodesListAdapter(episodes, onCardClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }
}