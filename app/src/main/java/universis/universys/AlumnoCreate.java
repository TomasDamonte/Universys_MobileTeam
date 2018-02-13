package universis.universys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Esta clase es un Activity que se ejecuta cuando el usuario que se logeó es un alumno.
 * Se encarga de todas las tareas que puede realizar el alumno con la aplicación.
 */
public class AlumnoCreate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener{

    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextDomicilio;
    private EditText editTextEmail;
    private EditText editTextFNac;
    private EditText editTextTelefono;
    private EditText editTextCatedra;
    private EditText editTextCarrera;
    private EditText editTextMateria;
    private TextView textViewOpcion;
    private TextView textViewEvento;
    private EditText editTextHorario;
    private int itemMenu;
    private TextView textViewNota;
    private HashMap<Date,String> calendarEvents;
    private FrameLayout frameLayoutRespuesta;
    private MaterialCalendarView calendarioAlumno;
    private LinearLayout linearLayoutHorarios;
    private HashMap<Integer,String> idCursada;
    private LinearLayout layoutDatosPersonales;
    private LinearLayout layoutCalendario;
    private LinearLayout layoutFichadaAlumno;
    private LinearLayout layoutInscripciones;

    /**
     * Resetea los layouts, checkea que estén completos todos los campos
     *y envia la request dependiendo del item del menu que se haya seleccionado.
     *@param v View recibida al presionar el boton.
     */
    public void enviarRequest(View v) {

        TableLayout tabla = (TableLayout) findViewById(R.id.tablaFichadas);
        tabla.removeAllViews();
        ScrollView sVTablaFichadas = (ScrollView) findViewById(R.id.sVTablaFichadas);
        sVTablaFichadas.setVisibility(View.INVISIBLE);
        textViewNota.setVisibility(View.INVISIBLE);
        linearLayoutHorarios.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(editTextCatedra.getText().toString()) || TextUtils.isEmpty(editTextCarrera.getText().toString())
                || TextUtils.isEmpty(editTextMateria.getText().toString())) {
            Error.mostrar(Error.CAMPOS_INCOMPLETOS_ERROR);
        }
        else {
            if(itemMenu == R.id.nav_asistencias) {
                enviarRequest(RequestTaskIds.FICHADA_ALUMNO, URLs.FICHADA_ALUMNO);
            }
            else if(itemMenu == R.id.nav_notas) {
                enviarRequest(RequestTaskIds.NOTA_ALUMNO, URLs.NOTA_ALUMNO);
            }
            else if (itemMenu == R.id.nav_horarios) {
                enviarRequest(RequestTaskIds.HORARIO_ALUMNO, URLs.HORARIO_ALUMNO);
            }
            else if (itemMenu == R.id.nav_baja) {
                enviarRequest(RequestTaskIds.BAJA_MATERIA, URLs.BAJA_MATERIA);
            }
        }
    }

    /**
     * Hace el request.
     * @param id Identificador de la request.
     * @param url Direccion web a dónde enviar la request.
     */
    public void enviarRequest(int id, String url) {
        CHTTPRequest.postRequest(id, url, new JSONBuilder().requestGenerico(editTextCatedra.getText().toString()
                ,editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);
    }

    /**
     * Habilita la edición de los campos y el botón buttonEnviarDatosAlumno.
     * @param v View recibida al presionar el botón.
     */
    public void modificarDatosAlumno(View v) {
        editTextNombre.setFocusableInTouchMode(true);
        editTextApellido.setFocusableInTouchMode(true);
        editTextDomicilio.setFocusableInTouchMode(true);
        editTextEmail.setFocusableInTouchMode(true);
        editTextFNac.setFocusableInTouchMode(true);
        editTextTelefono.setFocusableInTouchMode(true);
        findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(true);
    }

    /**
     * Deshabilita la edición de los campos, lee los valores cargados en los campos
     * y envia la request.
     * @param v View recibida al presionar el botón.
     */
    public void enviarDatosAlumno(View v) {
        editTextNombre.setFocusable(false);
        editTextApellido.setFocusable(false);
        editTextDomicilio.setFocusable(false);
        editTextEmail.setFocusable(false);
        editTextFNac.setFocusable(false);
        editTextTelefono.setFocusable(false);
        findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(false);
        String[] datos = {editTextNombre.getText().toString(),editTextApellido.getText().toString(),editTextDomicilio.getText().toString(),
                editTextEmail.getText().toString(),editTextFNac.getText().toString(),editTextTelefono.getText().toString()};
        CHTTPRequest.postRequest(RequestTaskIds.MODIFICAR_DATOS_PERSONALES,URLs.MODIFICAR_DATOS_PERSONALES
                ,new JSONBuilder().modificarDatosPersonales(datos)).execute().addListener(this);
    }

    private void blanquearCampos() {
        editTextCarrera.setText("");
        editTextCatedra.setText("");
        editTextMateria.setText("");
    }
    /**
     * Inicializa el Activity y los atributos de la clase.
     * @param savedInstanceState Parámetro recibido al ejecutarse el Activity
     */
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
        calendarioAlumno = (MaterialCalendarView) findViewById(R.id.calendarioAlumno);
        editTextCatedra = (EditText) findViewById(R.id.editTextCatedra);
        editTextCarrera = (EditText) findViewById(R.id.editTextCarrera);
        editTextMateria = (EditText) findViewById(R.id.editTextMateria);
        editTextHorario = (EditText) findViewById(R.id.editTextHorarios);
        textViewNota = (TextView) findViewById(R.id.textViewNota);
        frameLayoutRespuesta = (FrameLayout) findViewById(R.id.frameLayoutRespuesta);
        linearLayoutHorarios = (LinearLayout) findViewById(R.id.linearLayoutHorarios);
        textViewOpcion = (TextView) findViewById(R.id.textViewOpcion);
        textViewEvento = (TextView) findViewById(R.id.textViewEvento);
        layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        layoutCalendario = (LinearLayout) findViewById(R.id.layoutCalendario);
        layoutFichadaAlumno = (LinearLayout) findViewById(R.id.layoutFichadaAlumno);
        layoutInscripciones = (LinearLayout) findViewById(R.id.layoutInscripciones);

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
        itemMenu = item.getItemId();
        blanquearCampos();
        frameLayoutRespuesta.setVisibility(View.INVISIBLE);
        switch (itemMenu){
            case R.id.nav_datosPersonales:
                CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES,
                        new JSONBuilder().requestBasico()).execute().addListener(this);
                layoutInscripciones.setVisibility(View.INVISIBLE);
                layoutCalendario.setVisibility(View.INVISIBLE);
                layoutFichadaAlumno.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.VISIBLE);
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
            case RequestTaskIds.MODIFICAR_DATOS_PERSONALES:
                Toast.makeText(this, "Datos guardados", Toast.LENGTH_LONG).show();
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

}