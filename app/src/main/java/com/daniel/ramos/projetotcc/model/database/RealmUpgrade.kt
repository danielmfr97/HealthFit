package com.daniel.ramos.projetotcc.model.database

import io.realm.DynamicRealm
import io.realm.RealmConfiguration
import io.realm.RealmMigration

/**
 * O objetivo dessa classe é fazer a migração do banco quando necessário
 *
 * 1. Sempre que necessitar migração adicione um novo caso ao when() com o numero da versao do banco antigo
 * 2. Somar 1 ao valor de NEXT_VERSION. Identifica versao do banco para migração
 */

const val NEXT_VERSION: Int = 0
class RealmUpgrade : RealmMigration{
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var oldVersion = oldVersion.toInt()
        var newVersion = newVersion.toInt()

        while(oldVersion < newVersion) {
            when (oldVersion) {

            }
        }
    }

    companion object {
        fun getRealmConfig(): RealmConfiguration? {
            return RealmConfiguration.Builder()
                .schemaVersion(NEXT_VERSION.toLong())
                .name("healthfit.realm")
                .migration(RealmUpgrade())
                .build()
        }

        fun getRealmConfigNoMigration(): RealmConfiguration? {
            return RealmConfiguration.Builder()
                .schemaVersion(NEXT_VERSION.toLong())
                .name("healthfit.realm")
                .deleteRealmIfMigrationNeeded()
                .build()
        }
    }
}