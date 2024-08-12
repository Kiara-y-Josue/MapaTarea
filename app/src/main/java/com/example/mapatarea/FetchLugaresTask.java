package com.example.mapatarea;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchLugaresTask extends AsyncTask<Void, Void, ArrayList<lugarturistico>> {

    private OnLugaresFetchedListener listener;

    public FetchLugaresTask(OnLugaresFetchedListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<lugarturistico> doInBackground(Void... voids) {
        ArrayList<lugarturistico> lugares = new ArrayList<>();
        try {
            URL url = new URL("https://turismoquevedo.com/lugar_turistico/json_getlistadoMapa?lat=-1.04544038451566&lng=-79.48597144719564&radio=3");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder jsonResult = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonResult.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonResult.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                lugarturistico lugar = new lugarturistico();
                lugar.setNombre(jsonObject.getString("nombre"));
                lugar.setUbicacion(jsonObject.getString("ubicacion"));
                lugar.setLogo(jsonObject.getString("logo"));
                lugar.setLatitud(jsonObject.getDouble("latitud"));
                lugar.setLongitud(jsonObject.getDouble("longitud"));

                lugares.add(lugar);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lugares;
    }

    @Override
    protected void onPostExecute(ArrayList<lugarturistico> lugares) {
        listener.onLugaresFetched(lugares);
    }

    public interface OnLugaresFetchedListener {
        void onLugaresFetched(ArrayList<lugarturistico> lugares);
    }
}