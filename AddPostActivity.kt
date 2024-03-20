package com.sehatin.ittp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddPostActivity : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

//        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Post Pictures")
//
//        save_new_post_btn.setOnClickListener { uploadImage()}
//
//        CropImage.activity()
//            .setAspectRatio(1,1)
//            .start(this@AddPostActivity)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == CropImage.CROP_IMAGE)
//    }
    }
}