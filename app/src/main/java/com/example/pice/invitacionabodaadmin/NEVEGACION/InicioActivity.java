package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pice.invitacionabodaadmin.CLASES.ConstructorInvitado;
import com.example.pice.invitacionabodaadmin.CLASES.ControladorAcompanante;
import com.example.pice.invitacionabodaadmin.CLASES.Globals;
import com.example.pice.invitacionabodaadmin.INICIO.LoginActivity;
import com.example.pice.invitacionabodaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity{
    ListView lista;
    EditText et_busInvitado;
    ImageButton iv_qr, iv_casa;
    String [] acompa;
    String ultima, despues;
    int idNovio = 0, y;
    private List<ConstructorInvitado> listaAcompanante;
    private ControladorAcompanante adapter;
    boolean ok2 = false;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        idNovio = Globals.getInstance().id;

        progress = new ProgressDialog(InicioActivity.this);
        progress.setTitle("Cargando");
        progress.setMessage("Espere por favor...");
        progress.show();

        iv_qr = (ImageButton) findViewById(R.id.iv_qr);
        iv_casa = (ImageButton) findViewById(R.id.iv_casa);
        et_busInvitado = (EditText) findViewById(R.id.et_invitado);
        lista = (ListView) findViewById(R.id.lv_invitados);

        getInvitados();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ConstructorInvitado posicion = (ConstructorInvitado) lista.getAdapter().getItem(position);
                y = posicion.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(InicioActivity.this);

                RequestQueue requestQueue2 = Volley.newRequestQueue(InicioActivity.this);
                String url2 = "http://invitacionaboda.com/WebService/getAcompanantes.php?idInvitado="+y;
                JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("acompanantes");
                            if(jsonArray.length() != 0){
                                ok2 = true;
                                acompa = new String[jsonArray.length()];
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject acompanante = jsonArray.getJSONObject(i);
                                    String nombre = acompanante.getString("nombre");
                                    acompa[i] = nombre;
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(InicioActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(InicioActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                requestQueue2.add(jsObjectRequest);

                ultima = posicion.getMesa();
                despues = posicion.getNombre();
                String [] mientras = despues.split(":".trim());
                despues = mientras[1];

                String ape = posicion.getApellido();
                String [] mien = ape.split(":".trim());
                ape = mien[1];

                String segundo = posicion.getApodo();
                String [] otro = segundo.split(":".trim());
                segundo = otro[1];

                if(segundo.equals("sin apodo.")){
                    builder.setTitle(despues+" "+ape);
                }
                else{
                    builder.setTitle(segundo);
                }


                final View v_scaner = getLayoutInflater().inflate(R.layout.scaner_vista, null);
                TextView nm = (TextView) v_scaner.findViewById(R.id.nom);
                TextView ap = (TextView) v_scaner.findViewById(R.id.ape);

                TextView acompanantes = (TextView) v_scaner.findViewById(R.id.acompanantes);
                if(!ok2){
                    acompanantes.setText("No tiene acompañantes.");
                }
                else{
                    int u = 0;
                    for(int i = 0; i < acompa.length; i++){
                        u = i;
                        acompanantes.setText((acompanantes.getText().toString())+acompa[i]);
                        if((u+1) != acompa.length){
                            acompanantes.setText((acompanantes.getText().toString())+", ");
                        }
                    }
                }
                TextView mesa = (TextView) v_scaner.findViewById(R.id.mesa);
                mesa.setText(ultima);
                nm.setText("Nombre: "+despues);
                ap.setText("Apellido: "+ape);
                builder.setView(v_scaner);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button confirmar = (Button) v_scaner.findViewById(R.id.btn_confirmar);

                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestQueue requestQueue2 = Volley.newRequestQueue(InicioActivity.this);
                        String url = "http://invitacionaboda.com/WebService/actualizarAsistencia.php?idInvitado="+y;
                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.length() > 15){
                                    Toast.makeText(InicioActivity.this, "Error al confirmar la asistencia1.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String[] separated = response.split(",");
                                    if(separated[0].equals("registrado")){
                                        Toast.makeText(InicioActivity.this, "Asistencia confirmada.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(InicioActivity.this, "Error al confirmar la asistencia.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(InicioActivity.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue2.add(stringRequest2);
                    }
                });

                Button cancelar = (Button) v_scaner.findViewById(R.id.btn_cancel);

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                    }
                });
            }
        });



        iv_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(InicioActivity.this);
                progress.setTitle("Cargando");
                progress.setMessage("Espere por favor...");
                progress.show();
                getInvitadosBus();
            }
        });

    }

    public void getInvitados(){
        RequestQueue requestQueue = Volley.newRequestQueue(InicioActivity.this);
        String url = "http://invitacionaboda.com/WebService/getInformacionInvitados.php?idNovio="+idNovio;
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("invitados");
                    if(jsonArray.length() == 0){
                        Toast.makeText(InicioActivity.this, "No tienes invitados.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        listaAcompanante = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject acompanante = jsonArray.getJSONObject(i);
                            int id = Integer.valueOf(acompanante.getString("idInvitado"));
                            String apodo = acompanante.getString("apodo");
                            String nombre = acompanante.getString("nombre");
                            String aPaterno = acompanante.getString("aPaterno");
                            String mesa = acompanante.getString("mesa");
                            if(mesa.equals("")){
                                mesa = "sin asignar.";
                            }
                            if(apodo.equals("")){
                                apodo = " sin apodo.";
                            }
                            if(nombre.equals("")){
                                nombre = "sin nombre.";
                            }
                            if(aPaterno.equals("")){
                                aPaterno = "sin apellido.";
                            }
                            if(mesa.equals("")){
                                mesa = "sin asignar.";
                            }
                            listaAcompanante.add(new ConstructorInvitado(id, "Apodo: " + apodo, "Mesa: " + mesa, "Nombre: " + nombre, "Apellido: " + aPaterno));
                        }
                        adapter = new ControladorAcompanante(InicioActivity.this, listaAcompanante);
                        lista.setAdapter(adapter);
                        Bundle back_menu;
                        back_menu = getIntent().getExtras();
                        if (back_menu!=null){
                            boolean b_bmenulat = false;
                            b_bmenulat = back_menu.getBoolean("boleano");
                            if(!b_bmenulat){
                                progress.dismiss();
                            }

                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(InicioActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progress.dismiss();
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(InicioActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            });
        requestQueue.add(jsObjectRequest);
    }

    public void getInvitadosBus(){
        lista.setAdapter(null);
        String busqueda = et_busInvitado.getText().toString();
        busqueda = busqueda.replace(" ", "_");
        RequestQueue requestQueue = Volley.newRequestQueue(InicioActivity.this);
        String url = "http://invitacionaboda.com/WebService/invitadosFiltro.php?idNovio="+idNovio+"&txtTexto="+busqueda;
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("invitados");
                    if(jsonArray.length() == 0){
                        Toast.makeText(InicioActivity.this, "No tienes invitados.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        listaAcompanante = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject acompanante = jsonArray.getJSONObject(i);
                            int id = Integer.valueOf(acompanante.getString("idInvitado"));
                            String apodo = acompanante.getString("apodo");
                            String nombre = acompanante.getString("nombre");
                            String aPaterno = acompanante.getString("aPaterno");
                            String mesa = acompanante.getString("mesa");
                            if(mesa.equals("")){
                                mesa = "sin asignar.";
                            }
                            if(apodo.equals("")){
                                apodo = " sin apodo.";
                            }
                            if(nombre.equals("")){
                                nombre = "sin nombre.";
                            }
                            if(aPaterno.equals("")){
                                aPaterno = "sin apellido.";
                            }
                            listaAcompanante.add(new ConstructorInvitado(id, "Apodo: " + apodo, "Mesa: " + mesa, "Nombre: " + nombre, "Apellido: " + aPaterno));
                        }
                        adapter = new ControladorAcompanante(InicioActivity.this, listaAcompanante);
                        lista.setAdapter(adapter);
                        progress.dismiss();
                    }

                } catch (JSONException e) {
                    Toast.makeText(InicioActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progress.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InicioActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });
        requestQueue.add(jsObjectRequest);
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InicioActivity.this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Está seguro de cerrar la sesión?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent cerrar = new Intent(InicioActivity.this, LoginActivity.class);
                startActivity(cerrar);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

}
