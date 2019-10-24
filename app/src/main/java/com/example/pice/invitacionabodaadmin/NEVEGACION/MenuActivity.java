package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pice.invitacionabodaadmin.CLASES.Globals;
import com.example.pice.invitacionabodaadmin.INICIO.LoginActivity;
import com.example.pice.invitacionabodaadmin.R;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    TextView tv_usuarioCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //con esto generamos el usuario en el header del MenuActivity
        //----------------------------------------------------------------------------------
        View hView = navigationView.getHeaderView(0);
        tv_usuarioCorreo = (TextView) hView.findViewById(R.id.tv_usuarioCorreo);
        tv_usuarioCorreo.setText(Globals.getInstance().usuarioCorreo);
        //----------------------------------------------------------------------------------
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Está seguro de cerrar la sesión?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent cerrar = new Intent(MenuActivity.this, LoginActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MenuActivity; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_acerca) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new AcercadeFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

       if (id == R.id.nav_principal) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).commit();

        } else if (id == R.id.nav_status) {
           fragmentManager.beginTransaction().replace(R.id.contenedor, new StatusFragment()).commit();

       } else if (id == R.id.nav_listado) {
           fragmentManager.beginTransaction().replace(R.id.contenedor, new ListadoFragment()).commit();

       } else if (id == R.id.nav_agregar) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new AgregarFragment()).commit();

        }else if (id == R.id.nav_editar) {
           fragmentManager.beginTransaction().replace(R.id.contenedor, new InvitadosFragment()).commit();

       } else if (id == R.id.nav_lector) {
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new LectorFragment()).commit();
           Intent intent = new Intent(MenuActivity.this, ScannerQR.class);
           startActivity(intent);

        } else if (id == R.id.nav_cerrar) {
           android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
           builder.setTitle("Cerrar sesión");
           builder.setMessage("¿Está seguro de cerrar la sesión?");
           builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   Intent cerrar = new Intent(MenuActivity.this, LoginActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
