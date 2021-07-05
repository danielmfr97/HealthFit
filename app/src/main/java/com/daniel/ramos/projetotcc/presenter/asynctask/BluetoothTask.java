package com.daniel.ramos.projetotcc.presenter.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

public class BluetoothTask {
    private Context context;
    private Carregamento carregamento;

    public BluetoothTask(Context context, Carregamento carregamento) {
        this.context = context;
        this.carregamento = carregamento;
    }
    private class Carregando extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carregamento.antesDeCarregar();
        }

        @Override
        protected String doInBackground(Void... voids) {
            SystemClock.sleep(300/2);
            carregamento.duranteCarregamento();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            carregamento.depoisDeCarregado();
        }
    }

    public abstract static class Carregamento {
        public void antesDeCarregar(){}
        public void duranteCarregamento(){}
        public void depoisDeCarregado(){}
    }

}
