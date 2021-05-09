package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.daniel.ramos.projetotcc.databinding.FragmentNovoExercicioBinding
import com.daniel.ramos.projetotcc.presenter.NovoExercicioPresenter
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio

class NovoExercicioFragment : Fragment() {
    private var _binding: FragmentNovoExercicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: NovoExercicioPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNovoExercicioBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarTipoExercicio()
        return binding.root
    }

    private fun inicializarPresenter() {
        presenter = NovoExercicioPresenter(this)
    }

    private fun configurarTipoExercicio() {
        registerForContextMenu((binding.menuTipoExercicio.editText as? AutoCompleteTextView)!!)
        (binding.menuTipoExercicio.editText as? AutoCompleteTextView)?.apply {
            setAdapter(presenter.getAdapterTipoExercicio())
            onItemClickListener = onExercicioSelecionado
        }
    }

    private val onExercicioSelecionado =
        AdapterView.OnItemClickListener { _, _, position, _ -> binding.menuTipoExercicio.helperText = TipoExercicio.values()[position].descricao }

}
