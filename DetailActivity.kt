package com.sehatin.ittp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sehatin.ittp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myData = parcelable<MyData>(EXTRA_MY_DATA)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        binding.apply {
            toolbarLayout.title = myData?.name.toString()
            fab.setOnClickListener {
                val moveWithObjectIntent = Intent(this@DetailActivity, UlasanActivity::class.java)
                moveWithObjectIntent.putExtra(MapsActivity.EXTRA_MY_DATA, myData)
                startActivity(moveWithObjectIntent)
            }
            contentScrolling.tvDetailDescription.text = myData?.description.toString()
            Glide.with(this@DetailActivity)
                .load(myData?.photo)
                .apply(RequestOptions().override(700, 700))
                .into(ivDetailPhoto)
        }
    }

    private inline fun <reified T : Parcelable> Activity.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") intent.getParcelableExtra(key) as? T
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_MY_DATA = "extra_my_data"
    }
}