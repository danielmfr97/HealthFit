package com.daniel.ramos.projetotcc.presenter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.utils.Constants
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.fragment.ExercicioIniciadoFragment
import org.json.JSONObject
import java.lang.Exception
import java.util.*


class ExercicioIniciadoPresenter(private val view: ExercicioIniciadoFragment) {
    private var lastMessageWrited: String = ""
    private val myBlue = ModelFactory.getBluetoothServiceA
    private val exercicioModel = ModelFactory.getExercicioModel
    private val pacienteModel = ModelFactory.getPacienteModel
    private val resultadoModel = ModelFactory.getResultadoModel

    private var paciente: Paciente? = null
    private var exercicio: Exercicio? = null

    private val sb = StringBuilder()

    init {
        setBluetoothHandler()
        setPacienteAndExercicio()
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_WRITE -> {
                    val writeBuf = msg.obj as ByteArray
                    val writeMessage = String(writeBuf)
                    lastMessageWrited = writeMessage
                    Log.i("TESTANDO", "Write: $writeMessage")
                }
                Constants.MESSAGE_READ -> {
                    //TODO: Quando recbeer 'p' nao contabilizar resultado
                    val readBuf = msg.obj as ByteArray
                    val readMessage = String(readBuf, 0, msg.arg1)
                    //TODO: Quando começar com D ignorar
                    //TODO: Setar flag para caso haja parada ignorar os dados
                    sb.append(readMessage)
                    val endOfLineIndex = sb.indexOf("\n") // determine the end-of-line
                    if (endOfLineIndex > 0) {                                            // if end-of-line,
                        val sbprint = sb.substring(0, endOfLineIndex) // extract string
                        Log.i("TESTANDO", "Read: $sbprint")
                        if (isMensagemValida(sbprint))
                            stopAndSaveResult(sbprint)
                        sb.delete(0, sb.length)
                    }
                }
            }
        }
    }

    private fun isMensagemValida(sbprint: String) : Boolean {
        return !sbprint.contains("deserialized") && (lastMessageWrited.isNotEmpty() && !lastMessageWrited.equals("p"))
    }

    fun setBluetoothHandler() {
        myBlue.setmHandlerSecundario(mHandler)
    }

    private fun setPacienteAndExercicio() {
        paciente = pacienteModel.getPacientePorId(view.pacienteId)
        exercicio = exercicioModel.getExercicioPorId(view.exercicioId)
    }

    fun pararExercicio() {
        myBlue.write("p".toByteArray())
    }

    fun inicializarExercicio() {
        when (exercicio!!.tipoExercicio) {
            0 -> iniciarExercicioSequencia()
            1 -> iniciarExercicioAleatorio()
        }
    }

    // TIPO EXERCICIO 0
    private fun iniciarExercicioSequencia() {
        Log.i("TESTE", "Iniciou o exercicio sequencia")
        val jsonObject = JSONObject()
        val exercicio = exercicioModel.getExercicioPorId(view.exercicioId)!!
        jsonObject.put("tipoExercicio", exercicio.tipoExercicio)
        jsonObject.put("quantidadeCiclos", exercicio.ciclosExercicio)
        jsonObject.put("tempoRandom", 0)
        jsonObject.put("timeout", 0)
        jsonObject.put("sensor1", exercicio.sensor1)
        jsonObject.put("sensor2", exercicio.sensor2)
        jsonObject.put("sensor3", exercicio.sensor3)
        jsonObject.put("sensor4", exercicio.sensor4)
        jsonObject.put("start", true)
        myBlue.write(jsonObject.toString().toByteArray())
        view.startCronometro()
    }

    // TIPO EXERCICIO 1
    private fun iniciarExercicioAleatorio() {
        Log.i("TESTE", "Iniciou o exercicio aleatorio")
        val jsonObject = JSONObject()
        val exercicio = exercicioModel.getExercicioPorId(view.exercicioId)!!
        jsonObject.put("tipoExercicio", exercicio.tipoExercicio)
        jsonObject.put("quantidadeCiclos", 0)
        jsonObject.put("tempoRandom", exercicio.tempoRandom?.times(1000).toString())
        jsonObject.put(
            "timeout", if (exercicio.timeout != null)
                exercicio.timeout?.times(1000).toString() else "100000"
        )
        jsonObject.put("sensor1", exercicio.sensor1)
        jsonObject.put("sensor2", exercicio.sensor2)
        jsonObject.put("sensor3", exercicio.sensor3)
        jsonObject.put("sensor4", exercicio.sensor4)
        jsonObject.put("start", true)
        myBlue.write(jsonObject.toString().toByteArray())
        view.startCronometro()
    }

    private fun stopAndSaveResult(sbprint: String) {
        view.pararContador()
        MainActivity.instance?.onBackPressed()
        MainActivity.openToastShort("Exercício concluído")
        try {
            val jsonObject = JSONObject(sbprint)
            val resultado = Resultado()
            resultado.id = UUID.randomUUID().toString()
            resultado.paciente_id = paciente?.id.toString()
            resultado.exercicio_id = exercicio?.id.toString()
            resultado.tempo_total = jsonObject.optString("tempoTotal")
            resultado.acertos = jsonObject.optString("acertos")
            resultado.erros = jsonObject.optString("erros")
            resultado.created = Date().time
            resultadoModel.salvarResultado(resultado)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}