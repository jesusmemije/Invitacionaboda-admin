package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pice.invitacionabodaadmin.CLASES.Globals;
import com.example.pice.invitacionabodaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StatusFragment extends Fragment {
    View view;
    TextView tvInvTotal, tvInvEnviadas, tvBolTotal, tvBolConfirmados, tvQrTotal, tvQrConfirmados, tvAsistencias, tvMisa;
    int idNovio;
    List<String> datosGenerales;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status, container, false);
        tvInvTotal = (TextView) view.findViewById(R.id.tvInvTotal);
        tvInvEnviadas = (TextView) view.findViewById(R.id.tvInvEnviadas);
        tvBolTotal = (TextView) view.findViewById(R.id.tvBolTotal);
        tvBolConfirmados = (TextView) view.findViewById(R.id.tvBolConfirmados);
        tvQrTotal = (TextView) view.findViewById(R.id.tvQrTotal);
        tvQrConfirmados = (TextView) view.findViewById(R.id.tvQrConfirmados);
        tvAsistencias = (TextView) view.findViewById(R.id.tvAsistencias);
        tvMisa = (TextView) view.findViewById(R.id.tvMisa);
        idNovio = Globals.getInstance().id;

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://invitacionaboda.com/WebService/getReporte.php?idNovio="+idNovio;
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("reporte");
                    if(jsonArray.length() != 0){
                        String a[] = new String[jsonArray.length()];
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject reporte= jsonArray.getJSONObject(i);
                            String invitacionesTotal = reporte.getString("invitaciones");
                            String invitacionesEnviadas = reporte.getString("invitacionEnviada");
                            String boletosTotal = reporte.getString("boletos");
                            String boletosConfirmados = reporte.getString("boletosConfirmados");
                            String qrTotal = reporte.getString("QREnviado");
                            String qrConfirmados = reporte.getString("QRConfirmado");
                            String asistencias = reporte.getString("Asistira");
                            String soloMisa = reporte.getString("soloMisa");

                            tvInvTotal.setText("No. de invitaciones: "+invitacionesTotal);
                            tvInvEnviadas.setText("Invitaciones enviadas: "+invitacionesEnviadas);
                            tvBolTotal.setText("No. de boletos: "+boletosTotal);
                            tvBolConfirmados.setText("Boletos confirmados: "+boletosConfirmados);
                            tvQrTotal.setText("Códigos QR enviados: "+qrTotal);
                            tvQrConfirmados.setText("Códigos QR confirmados: "+qrConfirmados);
                            tvAsistencias.setText("Asistencias confirmadas: "+asistencias);
                            tvMisa.setText("Asistencias confirmadas sólo a misa: "+soloMisa);

                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsObjectRequest);




        return view;
    }


}
