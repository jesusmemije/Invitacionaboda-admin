package com.example.pice.invitacionabodaadmin.INICIO;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pice.invitacionabodaadmin.CLASES.Globals;
import com.example.pice.invitacionabodaadmin.NEVEGACION.MenuActivity;
import com.example.pice.invitacionabodaadmin.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_usuario, et_password;
    Button btn_login;
    ProgressDialog progress;
    boolean ok = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_iniciar);
        et_usuario = (EditText) findViewById(R.id.et_usuario);
        et_password = (EditText) findViewById(R.id.et_password);

        //cjmc12@hotmail.com
        //admin

        et_usuario.setText("cjmc12@hotmail.com");
        et_password.setText("admin");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                login.execute("");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class Login extends AsyncTask<String, String, String> {

        String mensaje = "";
        String usuario_s = et_usuario.getText().toString();
        String contrasena_s = et_usuario.getText().toString();

        @Override
        protected String doInBackground(String... params) {

            if (usuario_s.trim().equals("")||contrasena_s.trim().equals("")) {
                mensaje = "Completa todos los campos.";
            }
            return mensaje;
        }

        @Override
        protected void onPostExecute(String r) {
            if(!r.equals("")) {
                Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            }
            else {
                progress = new ProgressDialog(LoginActivity.this);
                progress.setTitle("Cargando");
                progress.setMessage("Espere por favor...");
                progress.show();
                CargarWebService();
            }
            super.onPostExecute(r);
        }
    }

    public void CargarWebService(){
        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String url = "http://invitacionaboda.com/WebService/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String [] separado = response.split(",");
                if(separado[0].equals("sin_conexion")){
                    Toast.makeText(LoginActivity.this, "No hay conexión.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (separado[0].equals("datos_incorrectos")) {
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta.", Toast.LENGTH_LONG).show();
                    } else {
                        Globals.getInstance().id = Integer.valueOf(separado[0]);
                        Globals.getInstance().usuarioCorreo =  et_usuario.getText().toString();

                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        intent.putExtra("boleano", ok);
                        startActivity(intent);

                    }
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String user = et_usuario.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                Map<String, String> parametros = new HashMap<>();
                parametros.put("correo", user);
                parametros.put("password", password);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
