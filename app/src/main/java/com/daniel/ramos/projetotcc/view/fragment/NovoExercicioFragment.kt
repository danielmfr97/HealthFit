package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.daniel.ramos.projetotcc.MyApplication
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentNovoExercicioBinding
import com.daniel.ramos.projetotcc.presenter.NovoExercicioPresenter
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio
import com.daniel.ramos.projetotcc.view.activity.MainActivity

class NovoExercicioFragment : Fragment() {
    private var _binding: FragmentNovoExercicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: NovoExercicioPresenter

    private var numeroDispositivos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNovoExercicioBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarTipoExercicio()
        configurarTimeout()
        configurarNumDispositivos()
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
            }
        }
    }

    private fun configurarNumDispositivos() {
        val numDispositivos = binding.numDispositivos
        numDispositivos.integerNumber.text = numeroDispositivos.toString()
        numDispositivos.tvHeader.setText(R.string.headerNumeroDispositivos)
        numDispositivos.tvSubtitle.setText(R.string.subtitleNumeroDispositivos)
        numDispositivos.increase.setOnClickListener {
            if (numeroDispositivos in 0..3)
                numeroDispositivos += 1
            numDispositivos.integerNumber.text = numeroDispositivos.toString()
        }
        numDispositivos.decrease.setOnClickListener {
            if (numeroDispositivos in 1..4)
                numeroDispositivos -= 1
            numDispositivos.integerNumber.text = numeroDispositivos.toString()
        }
    }

    private fun configurarBotoes() {
        binding.btnSalvar.setOnClickListener {
            salvarDados()
        }

        binding.btnSalvarEIniciar.setOnClickListener {
            salvarDados()
            //TODO: Iniciar exercicio
        }
    }

    private fun salvarDados() {
        if (isDadosValidos()) {
            presenter.salvarExercicio()
            MainActivity.instance!!.onBackPressed()
        }
        else
            MainActivity.openToastShort("Informações inválidas")
    }

    fun getNomeExercicio(): String {
        return binding.nomeExercicio.editText!!.text.toString()
    }

    fun getTipoExercicio(): String {
        return binding.autocompelteTipoExercicio.text.toString()
    }

    fun getDuracaoExercicio(): Long {
        return binding.duracaoExercicio.editText!!.text.toString().toLong()
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

    fun getDelayContarError(): Long {
        return binding.delayContarError.editText!!.text.toString().toLong()
    }

    fun getNumeroFitSpots(): Int {
        return numeroDispositivos
    }

    private fun isDadosValidos(): Boolean {
        var isValido = true

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

        // Validar duração exercicio
        if (binding.duracaoExercicio.editText!!.text.isNullOrEmpty()) {
            isValido = true
            binding.duracaoExercicio.error = "Informe a duração do exercício"
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

        // Validar tempo ignora ação
        if (binding.delayContarError.editText!!.text.isNullOrEmpty()) {
            isValido = false
            binding.delayContarError.error = "Informe o limiar"
        }

        // Validar numero de dispositivos
        if (numeroDispositivos == 0){
            isValido = false
            binding.numDispositivos.tvHeader.error = "Impossível salvar exercício com 0 FitSpots"
        }

        return isValido
    }
}
