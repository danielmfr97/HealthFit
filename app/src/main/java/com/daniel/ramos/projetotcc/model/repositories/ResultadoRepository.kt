package com.daniel.ramos.projetotcc.model.repositories

import com.daniel.ramos.projetotcc.model.entities.Resultado

class ResultadoRepository: RealmRepository<Resultado>(Resultado::class.java)  {
}