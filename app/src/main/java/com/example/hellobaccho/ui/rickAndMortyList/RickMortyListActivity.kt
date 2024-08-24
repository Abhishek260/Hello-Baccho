package com.example.hellobaccho.ui.rickAndMortyList

import EpisodeBottomSheet
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hellobaccho.R
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.databinding.ActivityRickMortyListBinding
import com.example.hellobaccho.interfaces.OnCardClick
import com.example.hellobaccho.ui.cardDetail.CardDetail
import com.example.hellobaccho.ui.rickAndMortyList.adapters.RickMortyAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RickMortyListActivity @Inject constructor() : BaseActivity(), OnCardClick<Any> {

    private lateinit var activityBinding: ActivityRickMortyListBinding
    private val viewModel: RickMortyViewModel by viewModels()
    private var rickMortyList: List<Result> = ArrayList()
    private var rvAdapter: RickMortyAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var resultData: Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityRickMortyListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar as Toolbar)
        setUpToolbar("Rick Morty Data")
        val getPageNo = intent.getStringExtra("pageNo")
        getRickMortyList(getPageNo)
        setObservers()
        searchItem()
        swipeRefresh(getPageNo)
    }

    private fun setObservers() {
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }

        viewModel.viewDialogLiveData.observe(this) { show ->
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        viewModel.rickMortyLiveData.observe(this) { rickData ->
            rickMortyList = rickData
            setupRecyclerView()
        }
    }

    private fun getRickMortyList(pageNo: String?) {
        viewModel.getRickMortyList(pageNo)
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvRickMortyCard.layoutManager = manager
        }
        rvAdapter = RickMortyAdapter(rickMortyList, this)
        activityBinding.rvRickMortyCard.adapter = rvAdapter
    }

    private fun searchItem(): Boolean {
        activityBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                rvAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter?.filter?.filter(newText)
                return false
            }
        })
        return true
    }

    private fun swipeRefresh(pageNo: String?) {
        activityBinding.swipeRefreshLayout.setOnRefreshListener {
            refreshData(pageNo)
        }
    }

    private fun refreshData(pageNo: String?) {
        getRickMortyList(pageNo)
        lifecycleScope.launch {
            delay(1500)
            activityBinding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getEpisodesById(id: Int): List<String>? {
        val result = rickMortyList.find { it.id == id }
        return result?.episode
    }

    override fun onClick(data: Any, clickType: String) {
        if (clickType == "Episode") {
            val model = data as Result
            val url = model.url
            val episode = getEpisodesById(model.id)
            showEpisodeBottomSheet("Episodes", episode)
        } else if (clickType == "Episode") {
            val model = data as Result
            Log.d("URL", model.url)
            showDialog(model.url)
        }
    }

    override fun onCardClick(data: Any, clickType: String, index: Int) {
        if (clickType == "Card") {
            resultData = rickMortyList.elementAt(index)
            val gson = Gson()
            try {
                val jsonString = gson.toJson(resultData)
                val intent = Intent(this, CardDetail::class.java)
                intent.putExtra("CardData", jsonString)
                startActivity(intent)
            } catch (ex: Exception) {
                errorToast("Data Conversion Error: " + ex.message)
            }
        }
    }

    private fun showDialog(model: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Episode Selected")
        builder.setMessage(model)
        playSound()
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showEpisodeBottomSheet(title: String, episodes: List<String>?) {
        val composeView = activityBinding.root.findViewById<ComposeView>(R.id.composeView)
        composeView.apply {
            visibility = View.VISIBLE
            setContent {
                EpisodeBottomSheet(
                    title = title,
                    episodeUrls = episodes,
                    onCardClick = { episodeUrl ->
                        Log.d("URL", episodeUrl)
                        showDialog(episodeUrl)
                    },
                    onDismiss = {
                        visibility = View.GONE
                    }
                )
            }
        }
    }
}
