package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.ramos.projetotcc.databinding.FragmentAboutUsBinding

private var _binding: FragmentAboutUsBinding? = null
private val binding get() = _binding!!

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}