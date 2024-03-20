package com.sehatin.ittp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sehatin.ittp.databinding.ActivityLoadingBinding
import kotlinx.coroutines.delay

class LoadingActivity : AppCompatActivity() {
    private val binding: ActivityLoadingBinding by lazy {
        ActivityLoadingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fullname = intent.getStringExtra(FULL_NAME)
        binding.lblFullname.text = "Hello $fullname"

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, activity_home::class.java)
            startActivity(intent)
        }, 2000)
    }

    companion object {
        const val FULL_NAME = "full_name"
    }
}