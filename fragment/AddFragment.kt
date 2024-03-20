package com.sehatin.ittp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import com.sehatin.ittp.QuoteAdapter
import com.sehatin.ittp.QuoteAddUpdateActivity
import com.sehatin.ittp.R
import com.sehatin.ittp.data.Quote
import com.sehatin.ittp.databinding.FragmentAddBinding
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private lateinit var quoteAdapter: QuoteAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!


    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val intent = result.data
            when (result.resultCode) {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        val rvQuotes: RecyclerView = view.findViewById(R.id.rv_quotes)
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fab_add)

        // Inisialisasi tampilan
        rvQuotes.layoutManager = LinearLayoutManager(requireContext())
        rvQuotes.setHasFixedSize(true)

        quoteAdapter = QuoteAdapter(object : QuoteAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedNote: Quote?, position: Int?) {
                val intent = Intent(requireContext(), QuoteAddUpdateActivity::class.java)
                intent.putExtra(EXTRA_POSITION, position)
                intent.putExtra(EXTRA_QUOTE, selectedNote)
                resultLauncher.launch(intent)
            }
        })
        rvQuotes.adapter = quoteAdapter


        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), QuoteAddUpdateActivity::class.java)
            resultLauncher.launch(intent)
        }

        requireActivity().title = "Quotes"

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
                    Toast.makeText(requireContext(), "Error adding document", Toast.LENGTH_SHORT).show()

                }
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvQuotes, message, Snackbar.LENGTH_SHORT).show()
    }

}