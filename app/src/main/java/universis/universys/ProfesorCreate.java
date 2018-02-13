package universis.universys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/**
 * Esta clase es un Activity que se ejecuta cuando el usuario que se logeó es un profesor.
 * Se encarga de todas las tareas que puede realizar el profesor con la aplicación.
 */
public class ProfesorCreate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener{

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
    private LinearLayout layoutDatosPersonales;
    private LinearLayout layoutVerNotas;
    private LinearLayout layoutSolicitudes;
    private LinearLayout layoutAlumnoNota;
    private Button botonEnviarRequest;
    private RelativeLayout.LayoutParams posicionBoton;

    /**
     * Resetea los layouts, checkea que estén completos todos los campos
     *y envia la request dependiendo del item del menu que se haya seleccionado.
     *@param v View recibida al presionar el boton.
     */
    public void enviarRequest(View v) {
        tablaNotas.removeAllViews();
        tablaAsistencias.removeAllViews();
        sVNotas.setVisibility(View.INVISIBLE);
        sVAsistencias.setVisibility(View.INVISIBLE);
        if (TextUtils.isEmpty(editTextCatedra.getText().toString()) || TextUtils.isEmpty(editTextCarrera.getText().toString())
                || TextUtils.isEmpty(editTextMateria.getText().toString())) {
            Error.mostrar(Error.CAMPOS_INCOMPLETOS_ERROR);
        }
        else if (itemMenu == R.id.nav_verNotas) {
            enviarRequest(RequestTaskIds.NOTAS_PROFESOR,URLs.NOTAS_PROFESOR);
        }
        else if (itemMenu == R.id.nav_cargarNotas) {
            if (TextUtils.isEmpty(editTextAlumno.getText().toString()) || TextUtils.isEmpty(editTextNota.getText().toString())) {
                Error.mostrar(Error.CAMPOS_INCOMPLETOS_ERROR);
            }
            else if (Integer.parseInt( editTextNota.getText().toString())>10 || Integer.parseInt( editTextNota.getText().toString())<1) {
                Error.mostrar(Error.NOTA_INVALIDA);
            }
            else {
                enviarRequest(RequestTaskIds.CARGAR_NOTAS,URLs.CARGAR_NOTAS);
            }
        }
        else {
            enviarRequest(RequestTaskIds.VER_ASISTENCIAS,URLs.VER_ASISTENCIAS);
        }
    }

    /**
     * Hace el request.
     * @param id Identificador de la request.
     * @param url Direccion web a dónde enviar la request.
     */
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

    /**
     * Envía la request VER_SOLICITUDES al servidor.
     */
    public void requestVerSolicitudes() {
        TableLayout tablaSolicitudes = (TableLayout) findViewById(R.id.tablaSolicitudes);
        tablaSolicitudes.removeAllViews();
        HorizontalScrollView scrollViewTablaSolicitudes = (HorizontalScrollView) findViewById(R.id.sVTablaSolicitudes);
        scrollViewTablaSolicitudes.setVisibility(View.INVISIBLE);
        CHTTPRequest.postRequest(RequestTaskIds.VER_SOLICITUDES,URLs.VER_SOLICITUDES
                ,new JSONBuilder().requestBasico()).execute().addListener(this);
    }

    /**
     * Envía la request ACEPTAR_SOLICITUDES al servidor.
     * @param v View recibida al clickear el botón enviarSolicitudes.
     */
    public void requestEnviarSolicitudes(View v) {
        CHTTPRequest.postRequest(RequestTaskIds.ACEPTAR_SOLICITUDES,URLs.ACEPTAR_SOLICITUDES
                ,new JSONBuilder().enviarSolicitudesInscripcion(estadoSolicitud)).execute().addListener(this);
    }

    /**
     * Habilita la edición de los campos y el botón buttonEnviarDatosProfesor.
     * @param v View recibida al presionar el botón buttonModifDatosProfesor.
     */
    public void modificarDatosProfesor(View v) {
        editTextNombre.setFocusableInTouchMode(true);
        editTextApellido.setFocusableInTouchMode(true);
        editTextDomicilio.setFocusableInTouchMode(true);
        editTextEmail.setFocusableInTouchMode(true);
        editTextFNac.setFocusableInTouchMode(true);
        editTextTelefono.setFocusableInTouchMode(true);
        findViewById(R.id.buttonEnviarDatosProfesor).setEnabled(true);
    }

    /**
     * Resetea los editText.
     */
    private void blanquearCampos() {
        editTextCarrera.setText("");
        editTextCatedra.setText("");
        editTextMateria.setText("");
        editTextNota.setText("");
        editTextAlumno.setText("");
    }

    /**
     * Deshabilita la edición de los campos, lee los valores cargados en los campos
     * y envia la request.
     * @param v View recibida al presionar el botón buttonEnviarDatosProfesor.
     */
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

    /**
     * Inicializa el Activity y los atributos de la clase.
     * @param savedInstanceState Parámetro recibido al ejecutarse el Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheHelper.CONTEXT = this;
        setContentView(R.layout.activity_profesor_create);
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
        layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        frameLayoutRespuesta = (FrameLayout) findViewById(R.id.frameLayoutRespuesta);
        layoutVerNotas = (LinearLayout) findViewById(R.id.layoutVerNotas);
        layoutSolicitudes = (LinearLayout) findViewById(R.id.layoutSolicitudes);
        layoutAlumnoNota = (LinearLayout) findViewById(R.id.layoutAlumnoNota);
        botonEnviarRequest = (Button) findViewById(R.id.buttonEnviarRequest);
        posicionBoton = (RelativeLayout.LayoutParams)botonEnviarRequest.getLayoutParams();

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    /**
     * Al presionar el botón 'Atras' en el celular:
     * Si el menu está desplegado, lo contrae.
     * Si no, ejecuta el Activity padre.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Agrega opciones al menu del Action Bar(desactivado).
     * Este método no se utiliza. Sin embargo debe estar presente ya que es parte de la interfaz.
     * @param menu Menu recibido al ejecutarse el Activity.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Maneja las acciones a realizarse al clickear un item del menu
     * del Action Bar (desactivado).
     * Este método no se utiliza. Sin embargo debe estar presente ya que es parte de la interfaz.
     * @param item Item clickeado.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Maneja las acciones a realizarse al clickear un item del Navigation Bar.
     * @param item Item clickeado.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        blanquearCampos();
        itemMenu = item.getItemId();
        posicionBoton.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        posicionBoton.removeRule(RelativeLayout.BELOW);
        botonEnviarRequest.setLayoutParams(posicionBoton);
        frameLayoutRespuesta.setVisibility(View.INVISIBLE);

        switch (itemMenu) {
            case R.id.nav_datosPersonales:
                CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES
                        ,new JSONBuilder().requestBasico()).execute().addListener(this);
                layoutVerNotas.setVisibility(View.INVISIBLE);
                layoutSolicitudes.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_solicitudes:
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutVerNotas.setVisibility(View.INVISIBLE);
                requestVerSolicitudes();
                layoutSolicitudes.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_cargarNotas:
                textViewOpcion.setText("Cargar Nota");
                posicionBoton.addRule(RelativeLayout.BELOW,R.id.layoutAlumnoNota);
                posicionBoton.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                botonEnviarRequest.setLayoutParams(posicionBoton);
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutSolicitudes.setVisibility(View.INVISIBLE);
                layoutAlumnoNota.setVisibility(View.VISIBLE);
                layoutVerNotas.setVisibility(View.VISIBLE);
                break;
            default:
                if (itemMenu == R.id.nav_verNotas) textViewOpcion.setText("Ver Notas");
                else textViewOpcion.setText("Ver Asistencias");
                layoutAlumnoNota.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutSolicitudes.setVisibility(View.INVISIBLE);
                layoutVerNotas.setVisibility(View.VISIBLE);
                break;
        }
        closeDrawer();
        return true;
    }

    /**
     * Contrae el Navigation Menu.
     */
    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * Este método se ejecuta al recibir una respuesta del servidor.
     * Realiza la acción correspondiente a la request a la cual el servidor respondió.
     * @param request Request a la cual el servidor respondió.
     * @param response Respuesta del servidor.
     */
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

    /**
     * Se ejecuta cuando el servidor respondió a la request Datos_Personales.
     * Muestra en los EditText el valor correspondiente a cada campo.
     * @param request Resquest de la cual se obtiene la respuesta del servidor.
     */
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

    /**
     * Crea una tabla que muestra el nombre de alumno y el porcentaje de asistencia a la materia.
     * @param datos Respuesta del servidor.
     * @throws JSONException Por si ocurre algún error al leer el JSON.
     */
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
            asistencias = setPadding(asistencias);
            nombre = setPadding(nombre);
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

    /**
     * Crea una tabla que muestra el nombre del alumno y la nota que tiene en la materia.
     * @param datos Respuesta del servidor.
     * @throws JSONException Por si ocurre algún error al leer el JSON.
     */
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
            nota = setPadding(nota);
            nombre = setPadding(nombre);
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

    /**
     * Crea una tabla que muestra las solicitudes de inscripcion pendientes que tiene el profesor.
     * @param datos Respuesta del servidor.
     * @throws JSONException Por si ocurre algún error al leer el JSON.
     */
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
        tVaceptar = setPadding(tVaceptar);
        tVcatedra = setPadding(tVcatedra);
        tVcarrera =setPadding(tVcarrera);
        tVmateria = setPadding(tVmateria);
        tValumno = setPadding(tValumno);
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
            accion.setOnClickListener(new View.OnClickListener() {
                /**
                 * Actualiza el estado de la solicitud.
                 * @estadoSolicitud Guarda el estado de la solicitud.
                 * @param view Vista que disparó el listener.
                 */
                @Override
                public void onClick(View view) {
                    ToggleButton boton = (ToggleButton) view;
                    if(boton.isChecked())
                        estadoSolicitud.put(idSolicitud.get(boton.getId()),"aceptada");
                    else
                        estadoSolicitud.put(idSolicitud.get(boton.getId()),"rechazada");
                }
            });
            tVcatedra.setTextColor(Color.BLACK);
            tVcarrera.setTextColor(Color.BLACK);
            tVmateria.setTextColor(Color.BLACK);
            tValumno.setTextColor(Color.BLACK);
            tVcatedra.setBackgroundColor(Color.WHITE);
            tVcarrera.setBackgroundColor(Color.WHITE);
            tVmateria.setBackgroundColor(Color.WHITE);
            tValumno.setBackgroundColor(Color.WHITE);
            tVcatedra = setPadding(tVcatedra);
            tVcarrera =setPadding(tVcarrera);
            tVmateria = setPadding(tVmateria);
            tValumno = setPadding(tValumno);
            JSONObject dato = datos.getJSONObject(i);
            //Guarda a qué botón está asignada cada solicitud.
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

    /**
     * Setea el Padding del textView recibido.
     * @param tV TextView al cual se le seteará el Padding.
     * @return TextView con el Padding seteado.
     */
    private TextView setPadding(TextView tV){
        tV.setPadding(10,10,10,10);
        return tV;
    }
}
