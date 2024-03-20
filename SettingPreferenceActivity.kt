package com.sehatin.ittp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.sehatin.ittp.R.string.setting_page
import com.sehatin.ittp.databinding.ActivitySettingPreferenceBinding

class SettingPreferenceActivity : AppCompatActivity(), View.OnClickListener {
    private val binding: ActivitySettingPreferenceBinding by lazy {
        ActivitySettingPreferenceBinding.inflate(layoutInflater)
    }
    private lateinit var settingModel: SettingModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settingModel = intent.parcelable<SettingModel>("SETTING") as SettingModel
        binding.btnSave.setOnClickListener(this)
        showPreferencesInForm()

        supportActionBar?.apply {
            title = getString(setting_page)
            setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_save) {
            binding.apply {
                val name = edtName.text.toString().trim()
                val gender = rgGender.checkedRadioButtonId == R.id.rb_laki
                val email = edtEmail.text.toString().trim()
                val age = edtAge.text.toString().trim()
                val phoneNo = edtPhone.text.toString().trim()
                val isDarkTheme = rgIsDarkTheme.checkedRadioButtonId == R.id.rb_yes

                if (name.isEmpty()) {
                    edtName.error = getString(R.string.field_required)
                    return
                }


                if (email.isEmpty()) {
                    edtEmail.error = getString(R.string.field_required)
                    return
                }

                if (!isValidEmail(email)) {
                    edtEmail.error = getString(R.string.email_is_not_valid)
                    return
                }

                if (age.isEmpty()) {
                    edtAge.error = getString(R.string.field_required)
                    return
                }

                if (phoneNo.isEmpty()) {
                    edtPhone.error = getString(R.string.field_required)
                    return
                }

                if (!TextUtils.isDigitsOnly(phoneNo)) {
                    edtPhone.error = getString(R.string.field_digit_only)
                    return
                }

                saveSetting(name, gender, email, age, phoneNo, isDarkTheme)

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_RESULT, settingModel)
                setResult(RESULT_CODE, resultIntent)

                finish()
            }
        }
    }
    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
    private fun isValidEmail(email: CharSequence): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun showPreferencesInForm() {
        binding.apply {
            edtName.setText(settingModel.name)
            if (settingModel.gender) {
                rbLaki.isChecked = true
            } else {
                rbPerempuan.isChecked = true
            }
            edtEmail.setText(settingModel.email)
            edtAge.setText(settingModel.age.toString())
            edtPhone.setText(settingModel.phoneNumber)
            if (settingModel.isDarkTheme) {
                rbYes.isChecked = true
            } else {
                rbNo.isChecked = true
            }
        }
    }
    private fun saveSetting(
        name: String,
        gender: Boolean,
        email: String,
        age: String,
        phoneNo: String,
        darkTheme: Boolean
    ) {
        val settingPreference = SettingPreference(this)
        settingModel.name = name
        settingModel.gender = gender
        settingModel.email = email
        settingModel.age = age.toInt()
        settingModel.phoneNumber = phoneNo
        settingModel.isDarkTheme = darkTheme
        settingPreference.setSetting(settingModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}