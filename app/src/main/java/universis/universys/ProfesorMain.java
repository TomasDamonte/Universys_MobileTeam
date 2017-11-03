package universis.universys;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProfesorMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener, View.OnClickListener {

    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextDomicilio;
    private EditText editTextEmail;
    private EditText editTextFNac;
    private EditText editTextTelefono;
    private EditText editTextCatedra;
    private EditText editTextAlumno;
    private EditText editTextNota;
    private EditText editTextCarrera;
    private EditText editTextMateria;
    private HashMap<Integer,String> idSolicitud;
    private LinkedHashMap<String,String> estadoSolicitud;
    private int itemMenu;
    private TextView textViewOpcion;
    private ScrollView sVNotas;
    private TableLayout tablaNotas;
    private ScrollView sVAsistencias;
    private TableLayout tablaAsistencias;
    private FrameLayout frameLayoutRespuesta;


    public void enviarRequest(View v) {
        tablaNotas.removeAllViews();
        tablaAsistencias.removeAllViews();
        sVNotas.setVisibility(View.INVISIBLE);
        sVAsistencias.setVisibility(View.INVISIBLE);
        if (editTextCatedra.getText().toString().equals("") || editTextCarrera.getText().toString().equals("") || editTextMateria.getText().toString().equals("")) {
            Toast.makeText(this, "Deben completarse todos los campos", Toast.LENGTH_LONG).show();
        }
        else if (itemMenu == R.id.nav_verNotas) {
            enviarRequest(RequestTaskIds.NOTAS_PROFESOR,URLs.NOTAS_PROFESOR);
        }
        else if (itemMenu == R.id.nav_cargarNotas) {
            if (editTextAlumno.getText().toString().equals("") || editTextNota.getText().toString().equals("")) {
                Toast.makeText(this, "Deben completarse todos los campos", Toast.LENGTH_LONG).show();
            }
            else if (Integer.parseInt( editTextNota.getText().toString())>10 || Integer.parseInt( editTextNota.getText().toString())<1) {
                Toast.makeText(this, "Nota inválida", Toast.LENGTH_SHORT).show();
            }
            else {
                enviarRequest(RequestTaskIds.CARGAR_NOTAS,URLs.CARGAR_NOTAS);
            }
        }
        else {
            enviarRequest(RequestTaskIds.VER_ASISTENCIAS,URLs.VER_ASISTENCIAS);
        }
    }

    public void enviarRequest(int id, String url) {
        if (itemMenu == R.id.nav_cargarNotas){
            CHTTPRequest.postRequest(id, url, new JSONBuilder().requestGenerico(editTextCatedra.getText().toString()
                            ,editTextCarrera.getText().toString(), editTextMateria.getText().toString()
                            ,editTextAlumno.getText().toString(),editTextNota.getText().toString())).execute().addListener(this);
        }
        else {
            CHTTPRequest.postRequest(id, url, new JSONBuilder().requestGenerico(editTextCatedra.getText().toString()
                    , editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);
        }
    }

    public void requestVerSolicitudes() {
        TableLayout tablaSolicitudes = (TableLayout) findViewById(R.id.tablaSolicitudes);
        tablaSolicitudes.removeAllViews();
        HorizontalScrollView scrollViewTablaSolicitudes = (HorizontalScrollView) findViewById(R.id.sVTablaSolicitudes);
        scrollViewTablaSolicitudes.setVisibility(View.INVISIBLE);
        CHTTPRequest.postRequest(RequestTaskIds.VER_SOLICITUDES,URLs.VER_SOLICITUDES
                ,new JSONBuilder().requestBasico()).execute().addListener(this);
    }

    public void requestEnviarSolicitudes(View v) {
        CHTTPRequest.postRequest(RequestTaskIds.ACEPTAR_SOLICITUDES,URLs.ACEPTAR_SOLICITUDES
                ,new JSONBuilder().enviarSolicitudesInscripcion(estadoSolicitud)).execute().addListener(this);
    }

    public void modificarDatosProfesor(View v) {
        editTextNombre.setFocusableInTouchMode(true);
        editTextApellido.setFocusableInTouchMode(true);
        editTextDomicilio.setFocusableInTouchMode(true);
        editTextEmail.setFocusableInTouchMode(true);
        editTextFNac.setFocusableInTouchMode(true);
        editTextTelefono.setFocusableInTouchMode(true);
        findViewById(R.id.buttonEnviarDatosProfesor).setEnabled(true);
    }

    private void blanquearCampos() {
        editTextCarrera.setText("");
        editTextCatedra.setText("");
        editTextMateria.setText("");
        editTextNota.setText("");
        editTextAlumno.setText("");
    }

    public void enviarDatosProfesor(View v) {
        editTextNombre.setFocusable(false);
        editTextApellido.setFocusable(false);
        editTextDomicilio.setFocusable(false);;
        editTextEmail.setFocusable(false);
        editTextFNac.setFocusable(false);
        editTextTelefono.setFocusable(false);
        findViewById(R.id.buttonEnviarDatosProfesor).setEnabled(false);
        String[] datos = {editTextNombre.getText().toString(),editTextApellido.getText().toString(),editTextDomicilio.getText().toString(),
        editTextEmail.getText().toString(),editTextFNac.getText().toString(),editTextTelefono.getText().toString()};
        CHTTPRequest.postRequest(RequestTaskIds.MODIFICAR_DATOS_PERSONALES,URLs.MODIFICAR_DATOS_PERSONALES
                ,new JSONBuilder().modificarDatosPersonales(datos)).execute().addListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheHelper.CONTEXT = this;
        setContentView(R.layout.activity_profesor_main);
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
        editTextCatedra = (EditText) findViewById(R.id.editTextCatedra);
        editTextCarrera = (EditText) findViewById(R.id.editTextCarrera);
        editTextMateria = (EditText) findViewById(R.id.editTextMateria);
        editTextAlumno = (EditText) findViewById(R.id.editTextAlumno);
        editTextNota = (EditText) findViewById(R.id.editTextNota);
        textViewOpcion = (TextView) findViewById(R.id.textViewOpcion);
        tablaNotas = (TableLayout) findViewById(R.id.tablaNotas);
        tablaAsistencias = (TableLayout) findViewById(R.id.tablaAsistencias);
        sVNotas = (ScrollView) findViewById(R.id.scrollViewTablaNotas);
        sVAsistencias = (ScrollView) findViewById(R.id.scrollViewTablaAsistencias);
        frameLayoutRespuesta = (FrameLayout) findViewById(R.id.frameLayoutRespuesta);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        blanquearCampos();
        itemMenu = item.getItemId();
        LinearLayout layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        LinearLayout layoutVerNotas = (LinearLayout) findViewById(R.id.layoutVerNotas);
        LinearLayout layoutSolicitudes = (LinearLayout) findViewById(R.id.layoutSolicitudes);
        LinearLayout layoutAlumno = (LinearLayout) findViewById(R.id.linearLayoutAlumno);
        LinearLayout layoutNota = (LinearLayout) findViewById(R.id.linearLayoutNota);
        frameLayoutRespuesta.setVisibility(View.INVISIBLE);

        if (itemMenu == R.id.nav_datosPersonales){
            CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES
                    ,new JSONBuilder().requestBasico()).execute().addListener(this);
            layoutVerNotas.setVisibility(View.INVISIBLE);
            layoutSolicitudes.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        }
        else if (itemMenu == R.id.nav_verNotas || itemMenu == R.id.nav_asistencias) {
            if (itemMenu == R.id.nav_verNotas) textViewOpcion.setText("Ver Notas");
            else textViewOpcion.setText("Ver Asistencias");
            layoutAlumno.setVisibility(View.INVISIBLE);
            layoutNota.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutSolicitudes.setVisibility(View.INVISIBLE);
            layoutVerNotas.setVisibility(View.VISIBLE);
        }
        else if (itemMenu == R.id.nav_solicitudes) {
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutVerNotas.setVisibility(View.INVISIBLE);
            requestVerSolicitudes();
            layoutSolicitudes.setVisibility(View.VISIBLE);
        }

        else {
            textViewOpcion.setText("Cargar Nota");
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutSolicitudes.setVisibility(View.INVISIBLE);
            layoutAlumno.setVisibility(View.VISIBLE);
            layoutNota.setVisibility(View.VISIBLE);
            layoutVerNotas.setVisibility(View.VISIBLE);
        }
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onResponse(CHTTPRequest request, String response) {
        frameLayoutRespuesta.setVisibility(View.VISIBLE);
        String errorId = "";
        try {
            errorId = request.getJsonResponse().getString(Error.ERROR_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!errorId.equals(Error.SUCCESS))
            Error.mostrar(errorId);
        else {
            switch (request.getTaskId()){

                case RequestTaskIds.DATOS_PERSONALES:
                    taskDatosPersonales(request);
                    break;

                case RequestTaskIds.NOTAS_PROFESOR:
                    try {
                        taskNotasProfesor(request.getJsonResponse().getJSONArray("alumnos"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case RequestTaskIds.VER_SOLICITUDES:
                    try {
                        crearTablaSolicitudes(request.getJsonResponse().getJSONArray("solicitudes"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case RequestTaskIds.ACEPTAR_SOLICITUDES:
                    Toast.makeText(this,"Acción realizada correctamente",Toast.LENGTH_SHORT).show();
                    break;

                case RequestTaskIds.MODIFICAR_DATOS_PERSONALES:
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_LONG).show();
                    break;

                case RequestTaskIds.VER_ASISTENCIAS:
                    try {
                        crearTablaAsistencias(request.getJsonResponse().getJSONArray("asistencias"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case RequestTaskIds.CARGAR_NOTAS:
                    Toast.makeText(this,"Nota cargada correctamente",Toast.LENGTH_SHORT).show();
                    blanquearCampos();
                    break;
            }
        }
        return false;
    }

    private void taskDatosPersonales(CHTTPRequest request) {
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

    private void crearTablaAsistencias (JSONArray datos) throws JSONException {
        sVAsistencias.setVisibility(View.VISIBLE);
        TableRow fila = new TableRow(this);
        TextView nombre = new TextView(this);
        TextView asistencias = new TextView(this);

        nombre.setText("NOMBRE");
        asistencias.setText("ASISTENCIAS");
        nombre.setTextColor(Color.WHITE);
        asistencias.setTextColor(Color.WHITE);
        nombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        fila.addView(nombre);
        fila.addView(asistencias);
        fila.setBackgroundColor(Color.DKGRAY);
        tablaAsistencias.addView(fila);
        tablaAsistencias.setPadding(5,5,5,5);
        tablaAsistencias.setBackgroundColor(Color.BLACK);

        for(int i=0; i<datos.length();i++){
            fila = new TableRow(this);
            nombre = new TextView(this);
            asistencias = new TextView(this);
            nombre.setTextColor(Color.BLACK);
            asistencias.setTextColor(Color.BLACK);
            asistencias.setPadding(10,10,10,10);
            nombre.setPadding(10,10,10,10);
            JSONObject dato = datos.getJSONObject(i);
            nombre.setText(dato.getString("nombre"));
            asistencias.setText(dato.getString("asistencias"));
            fila.setPadding(5,5,5,5);
            asistencias.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            fila.setBackgroundColor(Color.WHITE);
            fila.addView(nombre);
            fila.addView(asistencias);
            tablaAsistencias.addView(fila);
        }
    }

    private void taskNotasProfesor(JSONArray datos) throws JSONException {
        sVNotas.setVisibility(View.VISIBLE);
        TableRow fila = new TableRow(this);
        TextView nombre = new TextView(this);
        TextView nota = new TextView(this);

        nombre.setText("NOMBRE");
        nota.setText("NOTA");
        nombre.setTextColor(Color.WHITE);
        nota.setTextColor(Color.WHITE);
        nombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        fila.addView(nombre);
        fila.addView(nota);
        fila.setBackgroundColor(Color.DKGRAY);
        tablaNotas.addView(fila);
        tablaNotas.setPadding(5,5,5,5);
        tablaNotas.setBackgroundColor(Color.BLACK);

        for(int i=0; i<datos.length();i++){
            fila = new TableRow(this);
            nombre = new TextView(this);
            nota = new TextView(this);
            nombre.setTextColor(Color.BLACK);
            nota.setTextColor(Color.BLACK);
            nota.setPadding(10,10,10,10);
            nombre.setPadding(10,10,10,10);
            JSONObject dato = datos.getJSONObject(i);
            nombre.setText(dato.getString("nombre"));
            nota.setText(dato.getString("nota"));
            fila.setPadding(5,5,5,5);
            nota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            fila.setBackgroundColor(Color.WHITE);
            fila.addView(nombre);
            fila.addView(nota);
            tablaNotas.addView(fila);
        }
    }

    private void crearTablaSolicitudes(JSONArray datos) throws JSONException {
        HorizontalScrollView scrollViewSolicitudes = (HorizontalScrollView) findViewById(R.id.sVTablaSolicitudes);
        scrollViewSolicitudes.setVisibility(View.VISIBLE);
        TableLayout tablaSolicitudes = (TableLayout)findViewById(R.id.tablaSolicitudes);
        TableRow fila = new TableRow(this);
        TextView tVcatedra = new TextView(this);
        TextView tVcarrera = new TextView(this);
        TextView tVmateria = new TextView(this);
        TextView tValumno = new TextView(this);
        TextView tVaceptar = new TextView(this);
        TableRow.LayoutParams margenes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        margenes.setMargins(4, 4, 4, 4);
        tablaSolicitudes.setPadding(5,5,5,5);
        tablaSolicitudes.setBackgroundColor(Color.BLACK);
        tVaceptar.setTextColor(Color.WHITE);
        tVcatedra.setTextColor(Color.WHITE);
        tVcarrera.setTextColor(Color.WHITE);
        tVmateria.setTextColor(Color.WHITE);
        tValumno.setTextColor(Color.WHITE);
        tVaceptar.setPadding(10,10,10,10);
        tVcatedra.setPadding(10,10,10,10);
        tVcarrera.setPadding(10,10,10,10);
        tVmateria.setPadding(10,10,10,10);
        tValumno.setPadding(10,10,10,10);
        tVcatedra.setText("CATEDRA");
        tVcarrera.setText("CARRERA");
        tVmateria.setText("MATERIA");
        tValumno.setText("ALUMNO");
        tVaceptar.setText("ACCION");
        fila.addView(tVaceptar,margenes);
        fila.addView(tValumno,margenes);
        fila.addView(tVmateria,margenes);
        fila.addView(tVcarrera,margenes);
        fila.addView(tVcatedra,margenes);
        tVaceptar.setBackgroundColor(Color.DKGRAY);
        tVcatedra.setBackgroundColor(Color.DKGRAY);
        tVcarrera.setBackgroundColor(Color.DKGRAY);
        tVmateria.setBackgroundColor(Color.DKGRAY);
        tValumno.setBackgroundColor(Color.DKGRAY);
        fila.setBackgroundColor(Color.BLACK);
        tablaSolicitudes.addView(fila);
        idSolicitud = new HashMap<>();
        estadoSolicitud = new LinkedHashMap<>();
        for (int i=0;i<datos.length();i++) {
            fila = new TableRow(this);
            tVcatedra = new TextView(this);
            tVcarrera = new TextView(this);
            tVmateria = new TextView(this);
            tValumno = new TextView(this);
            ToggleButton accion = new ToggleButton(this);
            accion.setTextOn("ACEPTAR");
            accion.setTextOff("RECHAZAR");
            accion.setId(i);
            accion.setOnClickListener(this);
            tVcatedra.setTextColor(Color.BLACK);
            tVcarrera.setTextColor(Color.BLACK);
            tVmateria.setTextColor(Color.BLACK);
            tValumno.setTextColor(Color.BLACK);
            tVcatedra.setBackgroundColor(Color.WHITE);
            tVcarrera.setBackgroundColor(Color.WHITE);
            tVmateria.setBackgroundColor(Color.WHITE);
            tValumno.setBackgroundColor(Color.WHITE);
            tVcatedra.setPadding(10,10,10,10);
            tVcarrera.setPadding(10,10,10,10);
            tVmateria.setPadding(10,10,10,10);
            tValumno.setPadding(10,10,10,10);
            JSONObject dato = datos.getJSONObject(i);
            idSolicitud.put(i,dato.getString("idSolicitud"));
            tVcatedra.setText(dato.getString("catedra"));
            tVcarrera.setText(dato.getString("carrera"));
            tVmateria.setText(dato.getString("materia"));
            tValumno.setText(dato.getString("alumno"));
            fila.setBackgroundColor(Color.BLACK);
            fila.addView(accion,margenes);
            fila.addView(tValumno,margenes);
            fila.addView(tVmateria,margenes);
            fila.addView(tVcarrera,margenes);
            fila.addView(tVcatedra,margenes);
            tablaSolicitudes.addView(fila);
        }
    }

    @Override
    public void onClick(View v) {
        ToggleButton boton = (ToggleButton) v;
        if(boton.isChecked()) {
            estadoSolicitud.put(idSolicitud.get(boton.getId()),"aceptada");
        }
        if(!boton.isChecked()) {
            estadoSolicitud.put(idSolicitud.get(boton.getId()),"rechazada");
        }
    }
}
