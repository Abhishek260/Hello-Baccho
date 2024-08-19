package com.example.hellobaccho.ui.detailActivity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.example.hellobaccho.R
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.databinding.ActivityDetailBinding
import com.example.hellobaccho.interfaces.OnCardClick
import com.example.hellobaccho.ui.rickAndMortyList.RickMortyViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity @Inject constructor(): BaseActivity(), OnCardClick<Any> {

    private lateinit var activityBinding: ActivityDetailBinding
    private val viewModel: RickMortyViewModel by viewModels()
    private  var cardData: Result? = null
    private var name : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar as Toolbar)
        setUpToolbar("Rick Morty Detail")
        getCardData()
        setupUI()
    }



    private fun setupUI(){
        val image = activityBinding.imageData
        loadImage(image,cardData?.image)
        activityBinding.cardId.text = cardData?.id.toString()
        activityBinding.tvName.text = cardData?.name
        activityBinding.tvStatus.text = cardData?.status
        activityBinding.tvSpecies.text = cardData?.species
        activityBinding.tvType.text = cardData?.type
        activityBinding.tvGendor.text = cardData?.gender
        activityBinding.episodeNo.text = cardData?.episode?.size.toString()
        val createdDate = formatDateTime(cardData?.created.toString())
        activityBinding.createdOn.text = createdDate
        activityBinding.tvOrigin.text = cardData?.origin?.name

    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    private fun getCardData() {

        if (intent != null) {
            val jsonString = intent.getStringExtra("CardData")
            if (jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<Result>() {}.type
                val resultData: Result =
                    gson.fromJson(jsonString.toString(), listType)
                cardData = resultData;
                if (resultData != null) {
                    name = resultData?.name
                } else {
                    errorToast("Something went wrong, Please try again.")
                    finish()
                }
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
