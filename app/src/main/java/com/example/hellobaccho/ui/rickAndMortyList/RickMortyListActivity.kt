package com.example.hellobaccho.ui.rickAndMortyList
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.databinding.ActivityRickMortyListBinding
import com.example.hellobaccho.interfaces.OnCardClick
import com.example.hellobaccho.ui.rickAndMortyList.adapters.RickMortyAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hellobaccho.ui.bottomsheets.episodes.EpisodeBottomSheet
import com.example.hellobaccho.ui.bottomsheets.episodes.EpisodesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class RickMortyListActivity @Inject constructor(): BaseActivity(),OnCardClick<Any>{
    private lateinit var activityBinding: ActivityRickMortyListBinding
    private val viewModel: RickMortyViewModel by viewModels()
    private var rickMortyList: List<Result> = ArrayList()
    private var rvAdapter:RickMortyAdapter? = null
    private lateinit var manager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityRickMortyListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar as Toolbar)
        setUpToolbar("Rick Morty Data")
        val getPageNo = intent.getStringExtra("pageNo")
        getRickMortyList(getPageNo);
        setObservers()
        searchItem()
        swipeRefresh(getPageNo)
    }


    private fun setObservers(){

        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }

        viewModel.viewDialogLiveData.observe(this, Observer { show ->
            if(show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        viewModel.rickMortyLiveData.observe(this){ rickData->

            rickMortyList = rickData
            setupRecyclerView()

        }
    }

    private fun getRickMortyList(pageNo:String?){
        viewModel.getRickMortyList(pageNo)
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvRickMortyCard.layoutManager = manager
        }
        rvAdapter = RickMortyAdapter(rickMortyList,this)
        activityBinding.rvRickMortyCard.adapter = rvAdapter
    }

    fun searchItem(): Boolean {
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

    private fun swipeRefresh(pageNo: String?){
        activityBinding.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            refreshData(pageNo)

        })
    }

    private fun refreshData(pageNo:String?) {
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

        if (clickType=="Episode"){
            val model = data as Result
            val episode = getEpisodesById(model.id)
//            successToast(episode?.size.toString())
            episodeBottomSheet(
                mContext = this,
                title ="Episodes",
                onCardClick = this,
                episodes = episode
            )
        }
        else if (clickType==EpisodesListAdapter.EPISODE_SELECTION_CLICK){
            val model = data as String
            Log.d("URL", model)
            showDialog(model)
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
//        Sir we can set negative and positive both buttons and can perform action accordingly

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    private fun episodeBottomSheet(mContext: Context, title: String, onCardClick: OnCardClick<Any>,episodes:List<String>?) {
        val bottomSheetDialog = EpisodeBottomSheet.newInstance(mContext, title, onCardClick,episodes)
        bottomSheetDialog.show(supportFragmentManager, EpisodeBottomSheet.TAG)
    }
}