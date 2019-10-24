package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pice.invitacionabodaadmin.CLASES.Globals;
import com.example.pice.invitacionabodaadmin.R;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class ScannerQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mscaner;
    private static final int REQUEST_CAMERA = 1;
    String nombre, mesa_s,  idInvitado, apodo, apellido;
    int id;
    boolean ok = true, ok2 = false;
    String [] acompa;
    int idNovio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);
        idNovio = Globals.getInstance().id;

        mscaner = new ZXingScannerView(this);
        setContentView(mscaner);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                //Toast.makeText(getApplicationContext(), "¡Bien!, permiso concedido.", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }

    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permiso concedido, ahora puedes acceder a la cámara", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permiso denegado, no puede acceder y cámara", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("Debe permitir el acceso a ambos permisos",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ScannerQR.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {

        Uri uri = Uri.parse(result.getText());
        String ejemplo = uri.getQueryParameter("adc");

        if(ejemplo == null){
            Toast.makeText(ScannerQR.this, "Código QR inválido.", Toast.LENGTH_LONG).show();
        }
        else{
            final String [] res = result.getText().split("=");
            id = Integer.valueOf(res[1]);
            id = id/1981;

            RequestQueue requestQueue3 = Volley.newRequestQueue(ScannerQR.this);
            String url3 = "http://invitacionaboda.com/WebService/datos.php?idInvitado="+id+"&idNovio="+idNovio;
            JsonObjectRequest jsObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("invitado");
                        if(jsonArray.length() == 0){
                            Toast.makeText(ScannerQR.this, "No se obtuvieron resultados.", Toast.LENGTH_LONG).show();
                            ok = false;
                        }
                        else{
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject acompanante = jsonArray.getJSONObject(i);
                                idInvitado = acompanante.getString("idInvitado");
                                nombre = acompanante.getString("nombre");
                                mesa_s = acompanante.getString("mesa");
                                apodo = acompanante.getString("apodo");
                                apellido = acompanante.getString("aPaterno");
                                if(apodo.equals("")){
                                    apodo = "sin apodo";
                                }
                                if(apellido.equals("")){
                                    apellido = "sin apellido.";
                                }
                                if(nombre.equals("")){
                                    nombre = "sin nombre.";
                                }
                                if(mesa_s.equals("")){
                                    mesa_s = "sin asignar.";
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(ScannerQR.this);
                                RequestQueue requestQueue2 = Volley.newRequestQueue(ScannerQR.this);
                                String url2 = "http://invitacionaboda.com/WebService/getAcompanantes.php?idInvitado="+id;
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
                                            Toast.makeText(ScannerQR.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ScannerQR.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                requestQueue2.add(jsObjectRequest);

                                if(apodo.equals("sin apodo.")){
                                    builder.setTitle(nombre+" "+apellido);
                                }
                                else{
                                    builder.setTitle(apodo);
                                }
                                View v_scaner = getLayoutInflater().inflate(R.layout.scaner_vista, null);
                                TextView acompanantes = (TextView) v_scaner.findViewById(R.id.acompanantes);
                                TextView nm = (TextView) v_scaner.findViewById(R.id.nom);
                                TextView ap = (TextView) v_scaner.findViewById(R.id.ape);
                                if(!ok2){
                                    acompanantes.setText("No tiene acompañantes.");
                                }
                                else{
                                    int y = 0;
                                    for(int k = 0; k < acompa.length; k++){
                                        y = k;
                                        acompanantes.setText((acompanantes.getText().toString())+acompa[k]);
                                        if((y+1) != acompa.length){
                                            acompanantes.setText((acompanantes.getText().toString())+", ");
                                        }
                                    }
                                }
                                TextView mesa = (TextView) v_scaner.findViewById(R.id.mesa);
                                mesa.setText("Mesa: "+mesa_s);
                                nm.setText("Nombre: "+nombre);
                                ap.setText("Apellido: "+apellido);
                                builder.setView(v_scaner);
                                final AlertDialog dialog = builder.create();
                                dialog.show();

                                Button confirmar = (Button) v_scaner.findViewById(R.id.btn_confirmar);
                                confirmar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(ScannerQR.this);
                                        String url = "http://invitacionaboda.com/WebService/actualizarAsistencia.php?idInvitado="+id;
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response.length() > 15){
                                                    Toast.makeText(ScannerQR.this, "Error al confirmar la asistencia1.", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    String[] separated = response.split(",");
                                                    if(separated[0].equals("registrado")){
                                                        Toast.makeText(ScannerQR.this, "Asistencia confirmada.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(ScannerQR.this, "Error al confirmar la asistencia.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ScannerQR.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
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


                                Button modificar = (Button) v_scaner.findViewById(R.id.btn_modificar);
                                modificar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Intent intent = new Intent(ScannerQR.this, ScannerQR.class);
                                        //startActivity(intent);

                                        FragmentManager fragmentManager = Objects.requireNonNull(ScannerQR.this).getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contenedor, new ModificarFragment()).commit();

                                        //Toast.makeText(ScannerQR.this, "No entra a modificar", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                Button borrar = (Button) v_scaner.findViewById(R.id.btn_borrar);
                                borrar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScannerQR.this);
                                        builder.setTitle("Borrar invitado");
                                        builder.setMessage("¿Está seguro de borrar a: de la lista de invitados?");
                                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //eliminacion de registro
                                                Toast.makeText(ScannerQR.this, "Invitado eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                                                //FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                                FragmentManager fragmentManager = Objects.requireNonNull(getSupportFragmentManager());
                                                fragmentManager.beginTransaction().replace(R.id.contenedor, new InvitadosFragment()).commit();
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                FragmentManager fragmentManager = Objects.requireNonNull(ScannerQR.this).getSupportFragmentManager();
                                                fragmentManager.beginTransaction().replace(R.id.contenedor, new InvitadosFragment()).commit();

                                            }
                                        });
                                        android.app.AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }
                                });



                            }
                        }

                    } catch (JSONException e) {
                        Toast.makeText(ScannerQR.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ScannerQR.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            requestQueue3.add(jsObjectRequest3);
        }

        mscaner.resumeCameraPreview(ScannerQR.this);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mscaner == null) {
                    mscaner = new ZXingScannerView(this);
                    setContentView(mscaner);
                }
                mscaner.setResultHandler(this);
                mscaner.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mscaner.stopCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mscaner.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mscaner.stopCamera();
    }

}
