package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentExerciciosBinding
import com.daniel.ramos.projetotcc.databinding.FragmentRelatoriosBinding
import com.daniel.ramos.projetotcc.presenter.ExerciciosPresenter
import com.daniel.ramos.projetotcc.presenter.RelatoriosPresenter

class ExerciciosFragment : Fragment() {
    private var _binding: FragmentExerciciosBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ExerciciosPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciciosBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        return binding.root
    }

    private fun inicializarPresenter() {
        presenter = ExerciciosPresenter(this)
    }

}