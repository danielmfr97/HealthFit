package com.daniel.ramos.projetotcc.model.repositories

import com.daniel.ramos.projetotcc.model.entities.Paciente

class PacienteRepository: RealmRepository<Paciente>(Paciente::class.java) {

}