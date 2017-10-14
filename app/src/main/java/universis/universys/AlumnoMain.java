package universis.universys;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class AlumnoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener {

    EditText editTextNombre;
    EditText editTextApellido;
    EditText editTextDomicilio;
    EditText editTextEmail;
    EditText editTextFNac;
    EditText editTextTelefono;
    EditText editTextCatedra;
    EditText editTextCarrera;
    EditText editTextMateria;
    CalendarView calendarioAlumno;

    public void requestAsistenciasAlumno(View v) {
        TableLayout tabla = (TableLayout)findViewById(R.id.tablaFichadas);
        tabla.removeAllViews();
        if(editTextCatedra.getText().toString().equals("") || editTextCarrera.getText().toString().equals("") || editTextMateria.getText().toString().equals("")) {
            Toast.makeText(this,"Deben completarse los tres campos",Toast.LENGTH_LONG).show();
        } else {
            CHTTPRequest.postRequest(RequestTaskIds.FICHADA_ALUMNO,URLs.FICHADA_ALUMNO,
                    new JSONBuilder().fichadaAlumno(editTextCatedra.getText().toString(),editTextCarrera.getText().toString(),editTextMateria.getText().toString())).execute().addListener(this);
        }
    }

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
        CacheHelper.CONTEXT = this;
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
        calendarioAlumno = (CalendarView) findViewById(R.id.calendarioAlumno);
        editTextCatedra = (EditText) findViewById(R.id.editTextCatedra);
        editTextCarrera = (EditText) findViewById(R.id.editTextCarrera);
        editTextMateria = (EditText) findViewById(R.id.editTextMateria);
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
        LinearLayout layoutFichadaAlumno = (LinearLayout) findViewById(R.id.layoutFichadaAlumno);

        if (id == R.id.nav_calendario) {
          /*  CHTTPRequest.postRequest(RequestTaskIds.CALENDARIO_ALUMNO,URLs.CALENDARIO_ALUMNO,
                    new JSONBuilder().consultaDatosPersonales()).execute().addListener(this);*/
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            layoutCalendario.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_datosPersonales){
            CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES,
                    new JSONBuilder().consultaDatosPersonales()).execute().addListener(this);
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_asistencias) {
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onResponse(CHTTPRequest request, String response) {
        if(request.getTaskId() == RequestTaskIds.CALENDARIO_ALUMNO) {

        }
        else if(request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
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

        } else if (request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
            try {
                crearTablaAsistencias(request.getJsonArrayResponse());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void crearTablaAsistencias(JSONArray datos) throws JSONException {

        TableLayout tabla = (TableLayout)findViewById(R.id.tablaFichadas);
        TableRow fila = new TableRow(this);
      //  fila.setLayoutParams(tabla.getLayoutParams());
        TextView fecha = new TextView(this);
        TextView presente = new TextView(this);

        fecha.setText("FECHA");
        presente.setText("PRESENTE");
        fecha.setTextColor(Color.WHITE);
        presente.setTextColor(Color.WHITE);
        fecha.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        fila.addView(fecha);
        fila.addView(presente);
        fila.setBackgroundColor(Color.DKGRAY);
        tabla.addView(fila);
        tabla.setPadding(5,5,5,5);
        tabla.setBackgroundColor(Color.BLACK);

        for(int i=0; i<datos.length();i++){
            fila = new TableRow(this);
            fecha = new TextView(this);
            presente = new TextView(this);
            fecha.setTextColor(Color.BLACK);
            presente.setTextColor(Color.BLACK);
            presente.setPadding(10,10,10,10);
            fecha.setPadding(10,10,10,10);
            JSONObject dato = datos.getJSONObject(i);
            fecha.setText(dato.getString("fecha"));
            presente.setText(dato.getString("presente"));
            fila.setPadding(5,5,5,5);
            presente.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            fila.setBackgroundColor(Color.WHITE);
            if(dato.getString("presente").equals("SI")){
                fecha.setBackgroundColor(Color.GREEN);
                presente.setBackgroundColor(Color.GREEN);
            } else {
                fecha.setBackgroundColor(Color.RED);
                presente.setBackgroundColor(Color.RED);
            }
            fila.addView(fecha);
            fila.addView(presente);
            tabla.addView(fila);
        }
    }
}
