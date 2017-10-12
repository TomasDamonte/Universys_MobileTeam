package universis.universys;

import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

public class AlumnoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener {

    EditText editTextNombre;
    EditText editTextApellido;
    EditText editTextDomicilio;
    EditText editTextEmail;
    EditText editTextFNac;
    EditText editTextTelefono;


    public void modificarDatosAlumno(View v) {
        editTextNombre.setFocusableInTouchMode(true);
        editTextApellido.setFocusableInTouchMode(true);
        editTextDomicilio.setFocusableInTouchMode(true);
        editTextEmail.setFocusableInTouchMode(true);
        editTextFNac.setFocusableInTouchMode(true);
        editTextTelefono.setFocusableInTouchMode(true);
       // findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(true);
    }

    public void enviarDatosAlumno(View v) {
        editTextNombre.setFocusable(false);
        editTextApellido.setFocusable(false);
        editTextDomicilio.setFocusable(false);;
        editTextEmail.setFocusable(false);
        editTextFNac.setFocusable(false);
        editTextTelefono.setFocusable(false);
       // findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheHelper.context = this;
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

        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextApellido = (EditText) findViewById(R.id.editTextApellido);
        editTextDomicilio = (EditText) findViewById(R.id.editTextDomicilio);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextFNac = (EditText) findViewById(R.id.editTextFNac);
        editTextTelefono = (EditText) findViewById(R.id.editTextTelefono);
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
            CHTTPRequest.postRequest(RequestTaskIds.DATOSALUMNO,URLs.DATOSALUMNO,new JSONBuilder().consDatosAlumno()).execute().addListener(this);
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onResponse(CHTTPRequest request, String response) {
        if(request.getTaskId() == RequestTaskIds.DATOSALUMNO) {
            try {
                editTextNombre.setText(request.getJsonResponse().getString("nombre"));
                editTextApellido.setText(request.getJsonResponse().getString("apellido"));
                editTextEmail.setText(request.getJsonResponse().getString("mail"));
                editTextFNac.setText(request.getJsonResponse().getString("fNac"));
                editTextDomicilio.setText(request.getJsonResponse().getString("domicilio"));
                editTextTelefono.setText(request.getJsonResponse().getString("telefono"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
