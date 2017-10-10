package universis.universys;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AlumnoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public void modificarDatosAlumno(View v) {
        findViewById(R.id.editTextNombre).setEnabled(true);
        findViewById(R.id.editTextApellido).setEnabled(true);
        findViewById(R.id.editTextDomicilio).setEnabled(true);
        findViewById(R.id.editTextEmail).setEnabled(true);
        findViewById(R.id.editTextFNac).setEnabled(true);
        findViewById(R.id.editTextTelefono).setEnabled(true);
        findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(true);
    }

    public void enviarDatosAlumno(View v) {
        findViewById(R.id.editTextNombre).setEnabled(false);
        findViewById(R.id.editTextApellido).setEnabled(false);
        findViewById(R.id.editTextDomicilio).setEnabled(false);
        findViewById(R.id.editTextEmail).setEnabled(false);
        findViewById(R.id.editTextFNac).setEnabled(false);
        findViewById(R.id.editTextTelefono).setEnabled(false);
        findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.alumno_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        LinearLayout layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        LinearLayout layoutCalendario = (LinearLayout) findViewById(R.id.layoutCalendario);


        if (id == R.id.nav_calendario) {
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutCalendario.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_datosPersonales){
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
