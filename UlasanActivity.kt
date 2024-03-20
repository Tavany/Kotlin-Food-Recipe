package com.sehatin.ittp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sehatin.ittp.Helper.EXTRA_POSITION
import com.sehatin.ittp.Helper.EXTRA_QUOTE
import com.sehatin.ittp.Helper.RESULT_ADD
import com.sehatin.ittp.Helper.RESULT_DELETE
import com.sehatin.ittp.Helper.RESULT_UPDATE
import com.sehatin.ittp.data.Quote
import com.sehatin.ittp.databinding.ActivityUlasanBinding
import kotlinx.coroutines.launch

class UlasanActivity : AppCompatActivity() {
    private val binding: ActivityUlasanBinding by lazy {
        ActivityUlasanBinding.inflate(layoutInflater)
    }
    private lateinit var quoteAdapter: QuoteAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                when (it.resultCode) {
                    RESULT_ADD -> {
                        loadQuotes()
                        showSnackBarMessage("Satu item berhasil ditambahkan")
                    }
                    RESULT_UPDATE -> {
                        loadQuotes()
                        showSnackBarMessage("Satu item berhasil diubah")
                    }
                    RESULT_DELETE -> {
                        loadQuotes()
                        showSnackBarMessage("Satu item berhasil dihapus")
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firestore = Firebase.firestore
        auth = Firebase.auth

        supportActionBar?.title = "Ulasan"
        binding.rvQuotes.apply {
            binding.rvQuotes.layoutManager = LinearLayoutManager(this@UlasanActivity)
            binding.rvQuotes.setHasFixedSize(true)
            quoteAdapter = QuoteAdapter(object : QuoteAdapter.OnItemClickCallback {
                override fun onItemClicked(selectedNote: Quote?, position: Int?) {
                    val intent =
                        Intent(this@UlasanActivity, QuoteAddUpdateActivity::class.java)
                    intent.putExtra(EXTRA_POSITION, position)
                    intent.putExtra(EXTRA_QUOTE, selectedNote)
                    resultLauncher.launch(intent)

                }
            })
            adapter = quoteAdapter
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, QuoteAddUpdateActivity::class.java)
            resultLauncher.launch(intent)
        }

        loadQuotes()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        loadQuotes()
    }

    private fun loadQuotes() {
        lifecycleScope.launch {
            binding.progressbar.visibility = View.VISIBLE
            val quoteList = ArrayList<Quote>()
            val currentUser = auth.currentUser
            firestore.collection("quotes")
                .whereEqualTo("uid", currentUser?.uid)
                .get()
                .addOnSuccessListener { result ->
                    binding.progressbar.visibility = View.INVISIBLE
                    for (document in result) {
                        val id = document.id
                        val title = document.get("title").toString()
                        val descriptions = document.get("description").toString()
                        val category = document.get("category").toString()
                        val date = document.get("date") as com.google.firebase.Timestamp
                        quoteList.add(Quote(id, title, descriptions, category, date))
                    }
                    if (quoteList.size > 0) {
                        binding.rvQuotes.adapter = quoteAdapter
                        quoteAdapter.differ.submitList(quoteList)
                    } else {
                        quoteAdapter.differ.submitList(ArrayList())
                        showSnackBarMessage("Tidak ada data saat ini")
                    }
                }
                .addOnFailureListener {
                    binding.progressbar.visibility = View.INVISIBLE
                    Toast.makeText(
                        this@UlasanActivity, "Error adding document", Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvQuotes, message, Snackbar.LENGTH_SHORT).show()
    }

}