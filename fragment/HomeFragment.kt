package com.sehatin.ittp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sehatin.ittp.MyData
import com.sehatin.ittp.R
import com.sehatin.ittp.adapter.CardViewMyDataAdapter
import com.sehatin.ittp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val lists = ArrayList<MyData>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvMydata?.setHasFixedSize(true)
        lists.addAll(getListMyDatas())
        showRecyclerCardView()
    }
    private fun showRecyclerCardView() {
        binding?.apply {
            rvMydata.layoutManager = LinearLayoutManager(context)
            val cardViewMyDataAdapter = CardViewMyDataAdapter(lists)
            rvMydata.adapter = cardViewMyDataAdapter
        }
    }

    private fun getListMyDatas(): ArrayList<MyData> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listMyData = ArrayList<MyData>()
        for (position in dataName.indices) {
            val MyData = MyData(
                dataName[position],
                dataDescription[position],
                dataPhoto.getResourceId(position, -1)
            )
            listMyData.add(MyData)
        }
        return listMyData
    }
}