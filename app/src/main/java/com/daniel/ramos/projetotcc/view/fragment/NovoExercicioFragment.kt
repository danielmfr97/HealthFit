package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentMeusPacientesBinding
import com.daniel.ramos.projetotcc.databinding.FragmentNovoExercicioBinding
import com.daniel.ramos.projetotcc.presenter.MeusPacientesPresenter
import com.daniel.ramos.projetotcc.presenter.NovoExercicioPresenter
import com.daniel.ramos.projetotcc.presenter.enums.Cores
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
//        configurarSelecaoCor()
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

//    private fun configurarSelecaoCor() {
//        registerForContextMenu((binding.menuSelecionarCor.editText as? AutoCompleteTextView)!!)
//        (binding.menuSelecionarCor.editText as? AutoCompleteTextView)?.apply {
//            setAdapter(presenter.getAdapterSelecionarCor())
//            onItemClickListener = onCorSelecionada
//        }
//    }

    private val onExercicioSelecionado =
        AdapterView.OnItemClickListener { _, _, position, _ -> binding.menuTipoExercicio.helperText = TipoExercicio.values()[position].descricao }

//    private val onCorSelecionada =
//        AdapterView.OnItemClickListener { _, _, position, _ ->
//            binding.menuSelecionarCor.helperText = "O dispositivo utilizará a cor ${Cores.values()[position].nome} para sinalização"
//        }
}
