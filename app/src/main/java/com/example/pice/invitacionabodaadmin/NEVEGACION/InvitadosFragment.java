package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.Volley;
import com.example.pice.invitacionabodaadmin.CLASES.ConstructorInvitado;
import com.example.pice.invitacionabodaadmin.CLASES.ControladorAcompanante;
import com.example.pice.invitacionabodaadmin.CLASES.Globals;
import com.example.pice.invitacionabodaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvitadosFragment extends Fragment {
    View view;
    ListView lista;
    EditText et_busInvitado;
    ImageButton iv_casa;
    String [] acompa;
    String ultima, despues;
    int idNovio = 0, y;
    private List<ConstructorInvitado> listaAcompanante;
    private ControladorAcompanante adapter;
    boolean ok2 = false;
    ProgressDialog progress;

    TextView nm, ap, mesa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invitados, container, false);

        idNovio = Globals.getInstance().id;
        progress = new ProgressDialog(getContext());
        progress.setTitle("Cargando");
        progress.setMessage("Espere por favor...");
        progress.show();
        iv_casa = (ImageButton) view.findViewById(R.id.iv_casa);
        et_busInvitado = (EditText) view.findViewById(R.id.et_invitado);
        lista = (ListView) view.findViewById(R.id.lv_invitados);
        getInvitados();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                ConstructorInvitado posicion = (ConstructorInvitado) lista.getAdapter().getItem(position);
                y = posicion.getId();

                int getIdInvitado = posicion.getId();
                Globals.getInstance().idInvitado = getIdInvitado;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
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
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                requestQueue2.add(jsObjectRequest);

                ultima = posicion.getMesa();

                despues = posicion.getNombre();
                String [] mientras = despues.split(":".trim());
                despues = mientras[1];
                Globals.getInstance().nombre = despues;

                String ape = posicion.getApellido();
                String [] mien = ape.split(":".trim());
                ape = mien[1];
                Globals.getInstance().apePaterno = ape;

                String segundo = posicion.getApodo();
                String [] otro = segundo.split(":".trim());
                segundo = otro[1];
                Globals.getInstance().apodo = segundo;

                if(segundo.equals("sin apodo.")){
                    builder.setTitle(despues+" "+ape);
                }
                else{
                    builder.setTitle(segundo);
                }
                final View v_scaner = getLayoutInflater().inflate(R.layout.scaner_vista, null);
                nm = (TextView) v_scaner.findViewById(R.id.nom);
                ap = (TextView) v_scaner.findViewById(R.id.ape);
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
                mesa = (TextView) v_scaner.findViewById(R.id.mesa);
                mesa.setText(ultima);
                nm.setText("Nombre: "+despues);
                ap.setText("Apellido: "+ape);
                builder.setView(v_scaner);
                final AlertDialog dialoga = builder.create();
                dialoga.show();
                Button modificar = (Button) v_scaner.findViewById(R.id.btn_modificar);
                modificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Globals.getInstance().nombre = nm.getText().toString();
                        //Globals.getInstance().apePaterno = ap.getText().toString();

                        String mesaSplit = mesa.getText().toString();
                        String[] splitProccess = mesaSplit.split(":");
                        String mesaSplitSuccess = splitProccess[1];
                        mesaSplitSuccess = mesaSplitSuccess.trim();

                        if (!mesaSplitSuccess.equals("sin asignar.")){
                            Globals.getInstance().cltNumMesa = false;
                            Globals.getInstance().mesa = Integer.parseInt(mesaSplitSuccess);
                        }else{
                            Globals.getInstance().cltNumMesa = true;
                        }

                        dialoga.dismiss();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contenedor, new ModificarFragment()).commit();
                    }
                });


                Button borrar = (Button) v_scaner.findViewById(R.id.btn_borrar);
                borrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                        builder.setTitle("Borrar invitado");
                        builder.setMessage("¿Está seguro de borrar este invitado de su lista?");
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                //eliminacion de registro

                                        final int idInvitado = Globals.getInstance().idInvitado;
                                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                        String URL = "http://invitacionaboda.com/WebService/eliminarInvitados.php?idInvitado="+idInvitado;
                                        //Toast.makeText(getContext(), "URL: "+URL, Toast.LENGTH_LONG).show();
                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(getContext(), "Registro eliminado exitosamente. ", Toast.LENGTH_LONG).show();
                                                dialoga.cancel();
                                                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                                fragmentManager.beginTransaction().replace(R.id.contenedor, new InvitadosFragment()).commit();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getContext(), "THIS IS ERROR: "+ error.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });
                                        requestQueue.add(jsonObjectRequest);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                Button cancelar = (Button) v_scaner.findViewById(R.id.btn_cancel);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialoga.cancel();
                    }
                });
            }
        });
        iv_casa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(getContext());
                progress.setTitle("Cargando");
                progress.setMessage("Espere por favor...");
                progress.show();
                getInvitadosBus();
            }
            private void getInvitadosBus() {
                lista.setAdapter(null);
                String busqueda = et_busInvitado.getText().toString();
                busqueda = busqueda.replace(" ", "_");

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                String url = "http://invitacionaboda.com/WebService/invitadosFiltro.php?idNovio="+idNovio+"&txtTexto="+busqueda;
                JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("invitados");
                            if(jsonArray.length() == 0){
                                Toast.makeText(getContext(), "No tienes invitados.", Toast.LENGTH_LONG).show();
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
                                adapter = new ControladorAcompanante(getContext(), listaAcompanante);
                                lista.setAdapter(adapter);
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progress.dismiss();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        });
                requestQueue.add(jsObjectRequest);
            }
        });
        return view;
    }
    private void getInvitados() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://invitacionaboda.com/WebService/getInformacionInvitados.php?idNovio="+idNovio;
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("invitados");
                    if(jsonArray.length() == 0){
                        Toast.makeText(getContext(), "No tienes invitados.", Toast.LENGTH_LONG).show();
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
                            listaAcompanante.add(new ConstructorInvitado(id, "Apodo: " + apodo, "Mesa: " +mesa, "Nombre: " + nombre, "Apellido: " + aPaterno));
                        }
                        adapter = new ControladorAcompanante(getContext(), listaAcompanante);
                        lista.setAdapter(adapter);
                        Bundle back_menu;
                        back_menu = getActivity().getIntent().getExtras();
                        if (back_menu!=null){
                            boolean b_bmenulat = false;
                            b_bmenulat = back_menu.getBoolean("boleano");
                            if(!b_bmenulat){
                                progress.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progress.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });
        requestQueue.add(jsObjectRequest);
    }
}