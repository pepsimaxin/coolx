package com.gorgeous.coolx

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gorgeous.coolx.databinding.FragmentDialogBinding
import com.gorgeous.coolx.dialog.BottomDialog
import com.gorgeous.coolx.dialog.CenterDialog
import com.gorgeous.coolx.dialog.FloatDialog
import com.gorgeous.coolx.dialog.SpecialDialog
import coolx.appcompat.interfaces.XClickIntention

/**
 * Dialog Demo
 */
class DialogFragment : Fragment(), XClickIntention {
    private var _binding: FragmentDialogBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = DialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setOnClickListener(
            binding.bottomDialog,
            binding.centerDialog,
            binding.floatDialog,
            binding.specialDialog
        )

        return root
    }

    override fun onClick(v: View) {
        val context = requireContext()
        val intent = Intent(context, when (v.id) {
            R.id.bottomDialog -> BottomDialog::class.java
            R.id.centerDialog -> CenterDialog::class.java
            R.id.floatDialog -> FloatDialog::class.java
            R.id.specialDialog -> SpecialDialog::class.java
            else -> return
        })
        context.startActivity(intent)
    }

    override fun <V : View?> findViewById(id: Int): V {
        return findViewById(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}