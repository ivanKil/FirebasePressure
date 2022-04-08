package com.kusch.firebase.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kusch.firebase.R
import com.kusch.firebase.databinding.DialogPressureBinding
import com.kusch.firebase.model.Pressure
import java.time.LocalDateTime


class DialogPressure : DialogFragment() {
    private var _binding: DialogPressureBinding? = null
    private val binding get() = _binding!!
    var actOnSave: ((Pressure) -> Unit)? = null
    private var pressure: Pressure? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: DialogPressureBinding =
            DialogPressureBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root)
            .setMessage(getString(R.string.add_measure))
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> dismiss() }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                actOnSave?.let {
                    it(
                        Pressure(
                            null, LocalDateTime.now(),
                            binding.editPressureHigh.text.toString().toInt(),
                            binding.editPressureLow.text.toString().toInt(),
                            binding.pulse.text.toString().toInt(),
                        )
                    )
                }
            }
            .create()
        return dialog
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
        fun newInstance() = DialogPressure()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
