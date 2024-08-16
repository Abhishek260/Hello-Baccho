package com.example.hellobaccho.ui.rickAndMortyList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.databinding.ActivityRickMortyListBinding
import com.example.hellobaccho.interfaces.OnCardClick
import com.example.hellobaccho.ui.rickAndMortyList.adapters.RickMortyAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}