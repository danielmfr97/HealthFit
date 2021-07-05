package com.daniel.ramos.projetotcc.model.repositories

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.exceptions.RealmMigrationNeededException
import java.lang.IllegalArgumentException

open class RealmRepository<T : RealmObject>(var clazz: Class<T>?) {

    val realm: Realm = Realm.getDefaultInstance()

    fun salvar(`object`: T) {
        realm.executeTransaction { realm: Realm ->
            realm.copyToRealmOrUpdate(
                `object`
            )
        }
    }

    fun deletarTodasEntidades() {
        realm.apply {
            beginTransaction()
            removeAllChangeListeners()
            commitTransaction()
            beginTransaction()
            deleteAll()
            commitTransaction()
        }
    }

    fun novo(primaryKey: String?): T {
        return if (primaryKey == null)
            realm.createObject(clazz)
        else
            realm.createObject(
                clazz,
                primaryKey
            )
    }

    fun atualizarObject(atualizarListener: AtualizarListener) {
        realm.beginTransaction()
        atualizarListener.atualizar()
        realm.commitTransaction()
    }

    fun cancelarTransactions() {
        if (realm.isInTransaction) realm.cancelTransaction()
    }

    fun procurarPorId(id: String?): T? {
        return realm.where(clazz).equalTo("id", id).findFirst()
    }

    fun deletar(id: String?) {
        realm.executeTransaction {
            procurarPorId(id)?.deleteFromRealm()
        }
    }

    fun procurarTodos(): RealmResults<T> {
        return realm.where(clazz).findAll()
    }

    interface AtualizarListener {
        fun atualizar()
    }

}