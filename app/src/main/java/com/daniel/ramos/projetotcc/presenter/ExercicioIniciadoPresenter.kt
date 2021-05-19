package com.daniel.ramos.projetotcc.presenter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio
import com.daniel.ramos.projetotcc.presenter.utils.Constants
import com.daniel.ramos.projetotcc.presenter.utils.Constants.Companion.EXERCICIO_FINALIZADO
import com.daniel.ramos.projetotcc.presenter.utils.Constants.Companion.INICIAR_EXERCICIO
import com.daniel.ramos.projetotcc.presenter.utils.RegexUtils
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.fragment.ExercicioIniciadoFragment
import java.util.*

class ExercicioIniciadoPresenter(private val view: ExercicioIniciadoFragment) {
    private val myBlue = ModelFactory.getBluetoothServiceA
    private val exercicioModel = ModelFactory.getExercicioModel
    private val pacienteModel = ModelFactory.getPacienteModel
    private val resultadoModel = ModelFactory.getResultadoModel

    private var paciente: Paciente? = null
    private var exercicio: Exercicio? = null

    init {
        setBluetoothHandler()
        setPacienteAndExercicio()
    }

    fun setBluetoothHandler() {
        val mHandler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                val activity = this
                when (msg.what) {
                    Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                        BluetoothServiceA.STATE_CONNECTED -> {
                            MainActivity.openToastShort("Device conectado")
                        }
                        BluetoothServiceA.STATE_CONNECTING -> MainActivity.openToastShort("conectando...")
                        BluetoothServiceA.STATE_LISTEN, BluetoothServiceA.STATE_NONE -> MainActivity.openToastShort(
                            "Não conectado"
                        )
                    }
                    Constants.MESSAGE_WRITE -> {
                        val writeBuf = msg.obj as ByteArray
                        val writeMessage = String(writeBuf)
                        MainActivity.openToastShort("TESTANDO: Write: $writeMessage")
                        Log.i("TESTANDO", "Write: $writeMessage")
                    }
                    Constants.MESSAGE_READ -> {
                        val readBuf = msg.obj as ByteArray
                        val readMessage = String(readBuf, 0, msg.arg1)
                        val messageList = RegexUtils.removerVirgulasAsStringArray(readMessage)
                        if (readMessage == INICIAR_EXERCICIO.toString()) {
                            view.startCronometro()
                        }

                        if (readMessage == EXERCICIO_FINALIZADO.toString()) {
                            if (exercicio?.tipoExercicio.equals(TipoExercicio.ALEATORIO.nome)) {
                                val resultado: Resultado = Resultado()
                                resultado.id = UUID.randomUUID().toString()
                                resultado.exercicio_id = exercicio?.id!!
                                resultado.paciente_id = paciente?.id!!
//                                resultado.acertos =
//                                resultado.erros =
//                                    resultado.tempo_total =
                                resultado.created = Date()

                            }

                            if (exercicio?.tipoExercicio.equals(TipoExercicio.SEQUENCIA.nome)) {

                            }
                        }
                    }
                    Constants.MESSAGE_DEVICE_NAME -> {
                    }
                    Constants.MESSAGE_TOAST -> {
                        MainActivity.openToastShort("Impossivel conectar'")
                    }
                    Constants.MESSAGE_DEVICE_OFFLINE -> {
                    }
                }
            }
        }
        myBlue.setmHandler(mHandler)
    }

    private fun setPacienteAndExercicio() {
        paciente = pacienteModel.getPacientePorId(view.pacienteId)
        exercicio = exercicioModel.getExercicioPorId(view.exercicioId)
    }

    fun pararExercicio() {
        myBlue.write("p".toByteArray())
    }
}