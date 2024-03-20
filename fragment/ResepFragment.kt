package com.sehatin.ittp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sehatin.ittp.R
import com.sehatin.ittp.adapter.ListMyDataAdapter
import com.sehatin.ittp.databinding.FragmentResepBinding
import com.sehatin.ittp.mydataku

class ResepFragment : Fragment() {
    private val list = ArrayList<mydataku>()
    private var _binding: FragmentResepBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResepBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.RvMyData?.setHasFixedSize(true)
        list.addAll(getListMyDatas())
        showRecyclerCardView()
    }
    private fun showRecyclerCardView() {
        binding?.apply {
            RvMyData.layoutManager = LinearLayoutManager(context)
            val listMyDataAdapter = ListMyDataAdapter(list)
            RvMyData.adapter = listMyDataAdapter
        }
    }

    private fun getListMyDatas(): ArrayList<mydataku> {
        val dataNames = resources.getStringArray(R.array.data_nama1)
        val dataDescriptions = resources.getStringArray(R.array.data_description1)
        val dataPhotos = resources.obtainTypedArray(R.array.data_photo1)
        val dataLat = resources.getStringArray(R.array.data_lat)
        val dataLang = resources.getStringArray(R.array.data_lang)
        val listMyDatas = ArrayList<mydataku>()
        for (position in dataNames.indices) {
            val myDataku = mydataku(
                dataNames[position],
                dataDescriptions[position],
                dataPhotos.getResourceId(position, -1),
                dataLat[position].toDouble(),
                dataLang[position].toDouble()
            )
            listMyDatas.add(myDataku)
        }
        return listMyDatas
    }

}
