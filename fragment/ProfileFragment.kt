package com.sehatin.ittp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.sehatin.ittp.*
import com.sehatin.ittp.SettingPreference
import com.sehatin.ittp.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    companion object {
        const val REQUEST_CAMERA = 100
    }


    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private lateinit var settingModel: SettingModel
    private lateinit var mSettingPreference: SettingPreference
    private lateinit var imageUri: Uri
    private var _binding: FragmentProfileBinding? = null


    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null && result.resultCode == SettingPreferenceActivity.RESULT_CODE) {
                settingModel =
                    result.data?.parcelable<SettingModel>(SettingPreferenceActivity.EXTRA_RESULT) as SettingModel
                populateView(settingModel)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.imageProfile.setOnClickListener {
//            intentCamera()
//        }
//    }

    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)

        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        ref.putBytes(image)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            binding.imageProfile.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPage.setOnClickListener {
            val intent = Intent(requireContext(), SettingPreferenceActivity::class.java)
            intent.putExtra("SETTING", settingModel)
            resultLauncher.launch(intent)
//            binding.btnRating.setOnClickListener {
//                val intent = Intent(requireContext(), UlasanActivity::class.java)
//                intent.putExtra("SETTING", settingModel)
//                resultLauncher.launch(intent)
//            }
            binding.imageProfile.setOnClickListener {
                intentCamera()
            }
        }

        (activity as activity_home).supportActionBar?.title = "Title"
        mSettingPreference = SettingPreference(requireContext())

        showExistingPreferences()
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun showExistingPreferences() {
        settingModel = mSettingPreference.getSetting()
        populateView(settingModel)
    }

    private fun populateView(settingModel: SettingModel) {

        val activity = activity as? AppCompatActivity

        with(binding) {
            tvName.text = settingModel.name.toString().ifEmpty { getString(R.string.empty_message) }
            tvGender.text =
                if (settingModel.gender) getString(R.string.laki) else getString(R.string.perempuan)
            tvEmail.text =
                settingModel.email.toString().ifEmpty { getString(R.string.empty_message) }
            tvPhone.text =
                settingModel.phoneNumber.toString().ifEmpty { getString(R.string.empty_message) }
            tvAge.text = settingModel.age.toString().ifEmpty { getString(R.string.empty_message) }
            tvTheme.text =
                if (settingModel.isDarkTheme) getString(R.string.dark) else getString(R.string.light)
        }
        if (settingModel.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            activity?.delegate?.applyDayNight()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            activity?.delegate?.applyDayNight()
        }
    }
}