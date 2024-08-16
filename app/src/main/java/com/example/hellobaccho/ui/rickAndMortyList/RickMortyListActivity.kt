package com.example.hellobaccho.ui.rickAndMortyList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hellobaccho.R
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.databinding.ActivityRickMortyListBinding
import com.example.hellobaccho.ui.rickAndMortyList.models.RickMortyListModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class RickMortyListActivity @Inject constructor(): BaseActivity() {
    private lateinit var activityBinding: ActivityRickMortyListBinding
    private val viewModel: RickMortyViewModel by viewModels()
    private var rickMortyList: List<Result> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityRickMortyListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        getRickMortyList("2");
        setObservers()
    }


    private fun setObservers(){

        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }

        viewModel.rickMortyLiveData.observe(this){ rickData->
            rickMortyList = rickData
        }
    }

    private fun getRickMortyList(pageNo:String?){
        viewModel.getRickMortyList(pageNo)
    }
}