package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Objects;

public class ModificarFragment extends Fragment {
    View view;
    EditText et_apodo, et_nombre, et_apellidoPaterno, et_apellidoMaterno, et_mesa, et_boletos;
    Button btn_actualizar;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    Spinner spinner;
    List<String> tipoInvitado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modificar, container, false);

        et_apodo = (EditText) view.findViewById(R.id.et_apodo);
        et_nombre = (EditText) view.findViewById(R.id.et_nombre);
        et_apellidoPaterno = (EditText) view.findViewById(R.id.et_apellidoPaterno);
        et_apellidoMaterno = (EditText) view.findViewById(R.id.et_apellidoMaterno);
        et_mesa = (EditText) view.findViewById(R.id.et_mesa);
        et_boletos = (EditText) view.findViewById(R.id.et_boletos);
        spinner = view.findViewById(R.id.spinner);

        getTiposInvitado();

        et_apodo.setText(Globals.getInstance().apodo);
        et_nombre.setText(Globals.getInstance().nombre);
        et_apellidoPaterno.setText(Globals.getInstance().apePaterno);

        // 1. Obtenemos el valor de la variable de control global, (true or false).
        // 2. Hacer la condicional.
        if (!Globals.getInstance().cltNumMesa){
            et_mesa.setText(String.valueOf(Globals.getInstance().mesa));
        }

        Button btn_actualizar = (Button) view.findViewById(R.id.btn_actualizar);
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });

        Button cancelar = (Button) view.findViewById(R.id.btn_cancel);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new InvitadosFragment()).commit();
            }
        });

        return view;
    }

    public void actualizarDatos(){
        String apodo = et_apodo.getText().toString();
        String nombre = et_nombre.getText().toString();
        String apellidoPaterno = et_apellidoPaterno.getText().toString();
        String apellidoMaterno = et_apellidoMaterno.getText().toString();
        String mesa = et_mesa.getText().toString();
        String boletos = et_boletos.getText().toString();
        int tipoInv = spinner.getSelectedItemPosition();
        int idIn = Integer.parseInt(Globals.getInstance().b[tipoInv]);

        connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getContext(), "Sin conexión a internet", Toast.LENGTH_SHORT).show();
        } else {
            if (networkInfo.isConnected()) {

                int idInvitado = Globals.getInstance().idInvitado;
                apodo = apodo.trim();
                nombre = nombre.trim();
                apellidoPaterno = apellidoPaterno.trim();
                apellidoMaterno = apellidoMaterno.trim();
                mesa = mesa.trim();
                boletos = boletos.trim();

                String newApodo = apodo.replace(' ','_');
                String newNombre = nombre.replace(' ','_');
                String newApePaterno = apellidoPaterno.replace(' ','_');
                String newApeMaterno = apellidoMaterno.replace(' ','_');

                //Toast.makeText(getContext(), "idNovio: "+ idNovio + "\nApodo: "+apodo+"\nNombre: "+nombre+"\nPaterno: "+apellidoPaterno+"\nMaterno: "+apellidoMaterno+"\nMesa: "+mesa+"\nBoleto: "+boletos+"\nFIN.", Toast.LENGTH_LONG).show();
                String URL = "http://invitacionaboda.com/WebService/actualizarInvitados.php?idInvitado="+idInvitado+"&apodo="+newApodo+"&nombre="+newNombre+"&aPaterno="+newApePaterno+"&aMaterno="+newApeMaterno+"&mesa="+mesa+"&boletos="+boletos+"&idTipoInvitado="+idIn+" ";
                //Toast.makeText(getContext(), "URL: "+URL, Toast.LENGTH_LONG).show();
                final JSONObject emptyJsonObject = new JSONObject();
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(), "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contenedor, new InvitadosFragment()).commit();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "ERROR: "+ error, Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
            } else{
                Toast.makeText(getContext(), "¡Algo salió mal al tratar de conectarse a la red!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getTiposInvitado(){
        final int idNovio = Globals.getInstance().id;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://invitacionaboda.com/WebService/getTiposInvitado.php?idNovios="+idNovio;
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("acompanantes");
                    if(jsonArray.length() == 0){
                        Toast.makeText(getContext(), "No tienes invitados.", Toast.LENGTH_LONG).show();
                    }else{
                        //tipoInvitado = new ArrayList<>();
                        String a[] = new String[jsonArray.length()];
                        Globals.getInstance().b = new String[jsonArray.length()];
                        //a[0]= "Seleccione tipo de invitado";

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tipo = jsonArray.getJSONObject(i);
                            int idTipoInvitado = tipo.getInt("idTipoInvitado");
                            String tipoInvitado = tipo.getString("tipoInvitado");

                            a[i] = tipoInvitado;
                            Globals.getInstance().b[i] = String.valueOf(idTipoInvitado);
                            if (i == jsonArray.length()-1) {

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, a);
                                spinner.setAdapter(spinnerArrayAdapter);

                            }

                        }
                        //Toast.makeText(getContext(), ""+Arrays.toString(a), Toast.LENGTH_SHORT).show();

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
    }

}
