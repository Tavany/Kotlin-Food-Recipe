package com.sehatin.ittp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sehatin.ittp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                Activity.RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        val view = binding.mainLayout
                        Snackbar.make(view, "Google sign in failed.", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

//        supportActionBar?.title = getString(R.string.main_title)
//        mSettingPreference = SettingPreference(this)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.RECEIVE_SMS),
                SMS_REQUEST_CODE
            )
        }

        binding.btnSign.setOnClickListener{
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

//        binding.btnReg.setOnClickListener {
//            val intent = Intent(this, ActivityRegisterBinding::class.java)
//            intent.putExtra("SETTING", settingModel)
//            resultLauncher.launch(intent)
//        }

        binding.apply{

//            btnSign.setOnClickListener(this@LoginScreen)
            btnEmail.setOnClickListener {
                signInGoogle()
            }
            btnPhone.setOnClickListener{
                startActivity(Intent(this@MainActivity, PhoneAuthActivity::class.java))
            }
            tvSignUp.setOnClickListener {
                startActivity(Intent(this@MainActivity,SignUpActivity::class.java))
            }
            btnSign.setOnClickListener {
                val email = InputEmail.editText?.text.toString()
                val password = InputPassword.editText?.text.toString()
                if (email.isEmpty()) {
                    InputEmail.error = "Email cannot be empty!"
                }
                if (password.isEmpty()) {
                    InputPassword.error = "Password cannot be empty!"
                }

                else {

                    signIn(
                        binding.inputEmail.text.toString(),
                        binding.inputPassword.text.toString()
                    )

                    val isDetect = intent.getBooleanExtra(EXTRA_SMS_REGIST, false)
                    if (isDetect) {
                        val moveWithDataIntent = Intent(this@MainActivity, LoadingActivity::class.java)
                        moveWithDataIntent.putExtra(LoadingActivity.FULL_NAME, email)
                        startActivity(moveWithDataIntent)
                    }
                }

                return@setOnClickListener
            }

        }
        auth = Firebase.auth
//        showExistingPreferences()
    }
    private fun signIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, activity_home::class.java))
                finish()
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
    private fun validateForm(): Boolean {
        var valid = true
        val email = binding.inputEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.inputEmail.error = "Required."
            valid = false
        } else {
            binding.inputEmail.error = null
        }
        val password = binding.inputPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.inputPassword.error = "Required."
            valid = false
        } else {
            binding.inputPassword.error = null
        }
        return valid
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val view = binding.mainLayout
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snackbar.make(view, "Authentication Success.", Snackbar.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, activity_home::class.java))
                finish()
            } else {
                Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults:
    IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_REQUEST_CODE) {
            when (PackageManager.PERMISSION_GRANTED) {
                grantResults[0] -> Toast.makeText(this, "Sms receiver permission diterima", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Sms receiver permission ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        private const val SMS_REQUEST_CODE = 101

        const val EXTRA_SMS_REGIST = "extra_sms_regist"
    }
}