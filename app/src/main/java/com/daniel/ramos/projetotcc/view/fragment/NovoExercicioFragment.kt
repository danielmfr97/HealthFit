package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentNovoExercicioBinding
import com.daniel.ramos.projetotcc.presenter.NovoExercicioPresenter
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class NovoExercicioFragment : Fragment() {
    private var _binding: FragmentNovoExercicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: NovoExercicioPresenter

    private var numeroCiclos = 0
    private var listaSensores = mutableListOf<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNovoExercicioBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarTipoExercicio()
        configurarTimeout()
        configurarNumCiclos()
        configurarBotoes()
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
        AdapterView.OnItemClickListener { _, _, position, _ ->
            binding.menuTipoExercicio.helperText = TipoExercicio.values()[position].descricao
            limparCamposComuns()
            if (TipoExercicio.values()[position].nome == TipoExercicio.ALEATORIO.nome) {
                configurarExercicioAleatorio()
            }
            if (TipoExercicio.values()[position].nome == TipoExercicio.SEQUENCIA.nome) {
                configurarExercicioSequencia()
            }
        }

    private fun limparCamposComuns() {
        numeroCiclos = 0
    }

    private fun configurarExercicioAleatorio() {
        desabilitarRadioGroup(false)
        habilitarTodosFitSpots(false)
    }

    private fun configurarExercicioSequencia() {
        desabilitarRadioGroup(true)
        habilitarTodosFitSpots(true)
    }

    private fun desabilitarRadioGroup(b: Boolean) {
        val radio = binding.rgTimeout
        radio.clearCheck()
        binding.rbNao.isChecked = b
        for (i in 0 until radio.childCount) {
            radio.getChildAt(i).isClickable = !b
        }
    }

    private fun habilitarTodosFitSpots(b: Boolean) {
        val llFitSpots = binding.llFitSpots
        for (i in 0 until llFitSpots.childCount) {
            val child = llFitSpots.getChildAt(i)
            when (child) {
                is SwitchMaterial -> {
                    child.isChecked = b
                    child.isClickable = !b
                }
            }
        }
    }

    private fun configurarTimeout() {
        binding.rgTimeout.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbSim.id -> {
                    binding.timeOut.visibility = View.VISIBLE
                }
                binding.rbNao.id -> {
                    binding.timeOut.visibility = View.GONE
                }
                else -> binding.timeOut.visibility = View.GONE
            }
        }
    }

    private fun configurarNumCiclos() {
        val numCiclos = binding.ciclosExercicio
        numCiclos.integerNumber.text = numeroCiclos.toString()
        numCiclos.tvHeader.setText(R.string.headerCiclos)
        numCiclos.tvSubtitle.setText(R.string.subtitleCiclos)
        numCiclos.increase.setOnClickListener {
            if (numeroCiclos in 0..3)
                numeroCiclos += 1
            numCiclos.integerNumber.text = numeroCiclos.toString()
        }
        numCiclos.decrease.setOnClickListener {
            if (numeroCiclos in 1..4)
                numeroCiclos -= 1
            numCiclos.integerNumber.text = numeroCiclos.toString()
        }
    }

    private fun configurarBotoes() {
        binding.btnSalvar.setOnClickListener {
            salvarDados()
            listaSensores.clear()
        }
    }

    private fun salvarDados() {
        if (isDadosValidos()) {
            presenter.salvarExercicio()
            MainActivity.instance!!.onBackPressed()
        } else
            MainActivity.openToastShort("Informações inválidas")
    }

    fun getNomeExercicio(): String {
        return binding.nomeExercicio.editText!!.text.toString()
    }

    fun getTipoExercicio(): String {
        return binding.autocompelteTipoExercicio.text.toString()
    }

    fun getNumeroCiclos(): Int {
        return numeroCiclos
    }

    fun getTimeoutOption(): Boolean {
        return binding.rgTimeout.checkedRadioButtonId == binding.rbSim.id
    }

    fun getTimeout(): Long? {
        var tempoInMs: Long? = null
        if (!binding.timeOut.editText!!.text.toString().isEmpty())
            tempoInMs = binding.timeOut.editText!!.text.toString().toLong()
        return tempoInMs
    }

    fun getSensor1(): Boolean {
        return binding.firstFitSpot.isChecked
    }
    fun getSensor2(): Boolean {
        return binding.secondFitSpot.isChecked
    }
    fun getSensor3(): Boolean {
        return binding.thirdFitSpot.isChecked
    }
    fun getSensor4(): Boolean {
        return binding.fourthFitSpot.isChecked
    }

    private fun isDadosValidos(): Boolean {
        var isValido = true
        var fitSpotsAtivos = 0

        // Validar Nome Exercicio
        if (binding.nomeExercicio.editText!!.text.isNullOrEmpty()) {
            isValido = false
            binding.nomeExercicio.error = "Informe um nome"
        }

        // Validar Tipo Exercicio
        if (binding.autocompelteTipoExercicio.text.isNullOrEmpty()) {
            isValido = false
            binding.menuTipoExercicio.error = "Informe o tipo de exercício"
        }

        // Validar número de ciclos
        if (getNumeroCiclos() == 0) {
            isValido = false
            binding.ciclosExercicio.tvHeader.error = "Informe um valor maior que 0"
        }

        // Validar timeout
        if (binding.rgTimeout.checkedRadioButtonId == -1) {
            isValido = false
            binding.rbNao.error = "Escolha uma opção"
            if (binding.timeOut.editText!!.text.isNullOrEmpty()) {
                isValido = false
                binding.timeOut.error = "Informe o tempo de timeout"
            }
        }

        // Validar sensores (pelo menos dois devem estar selecionados
        getFitSpotsList().forEach {
            if (it)
                fitSpotsAtivos++
        }
        if (fitSpotsAtivos < 2) {
            isValido = false
            MainActivity.openToastShort("Selecione pelo menos 2 FitSpots")
        }

        return isValido
    }

    fun getFitSpotsList(): List<Boolean> {
        listaSensores.add(getSensor1())
        listaSensores.add(getSensor2())
        listaSensores.add(getSensor3())
        listaSensores.add(getSensor4())
        return listaSensores
    }
}
