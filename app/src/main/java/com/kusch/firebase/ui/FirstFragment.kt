package com.kusch.firebase.ui

import androidx.fragment.app.Fragment
import com.kusch.firebase.databinding.FragmentFirstBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {
    private val adapter: PressureAdapter by lazy { PressureAdapter() }
    private val homeViewModel by viewModel<PressureViewModel>()

    override fun initViews() {
        binding.pressureRecycler.adapter = adapter
        homeViewModel.ldPressures.observe(viewLifecycleOwner) { adapter.setData(it) }
        homeViewModel.requestPressures()

        binding.fab.setOnClickListener {
            val addDialog = DialogPressure.newInstance()
            addDialog.actOnSave = { homeViewModel.addPressure(it) }
            addDialog.show(childFragmentManager, DialogPressure.TAG)
        }
    }
}