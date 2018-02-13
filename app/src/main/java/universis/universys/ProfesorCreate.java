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

public class ProfesorCreate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener {

    /**
     * *
     * Habilita la edición de los campos y el botón buttonEnviarDatosProfesor.
     *
     * @param v View recibida al presionar el botón buttonModifDatosProfesor.
     */

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
    private HashMap<Integer, String> idSolicitud;
    private LinkedHashMap<String, String> estadoSolicitud;
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
     *
     * @param v View recibida al presionar el botón buttonEnviarDatosProfesor.
     */

    public void enviarDatosProfesor(View v) {
        editTextNombre.setFocusable(false);
        editTextApellido.setFocusable(false);
        editTextDomicilio.setFocusable(false);
        ;
        editTextEmail.setFocusable(false);
        editTextFNac.setFocusable(false);
        editTextTelefono.setFocusable(false);
        findViewById(R.id.buttonEnviarDatosProfesor).setEnabled(false);
        String[] datos = {editTextNombre.getText().toString(), editTextApellido.getText().toString(), editTextDomicilio.getText().toString(),
                editTextEmail.getText().toString(), editTextFNac.getText().toString(), editTextTelefono.getText().toString()};
        CHTTPRequest.postRequest(RequestTaskIds.MODIFICAR_DATOS_PERSONALES, URLs.MODIFICAR_DATOS_PERSONALES
                , new JSONBuilder().modificarDatosPersonales(datos)).execute().addListener(this);
    }

    /**
     * Inicializa el Activity y los atributos de la clase.
     *
     * @param savedInstanceState Parámetro recibido al ejecutarse el Activity
     */
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
        layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        frameLayoutRespuesta = (FrameLayout) findViewById(R.id.frameLayoutRespuesta);
        layoutVerNotas = (LinearLayout) findViewById(R.id.layoutVerNotas);
        layoutSolicitudes = (LinearLayout) findViewById(R.id.layoutSolicitudes);
        layoutAlumnoNota = (LinearLayout) findViewById(R.id.layoutAlumnoNota);
        botonEnviarRequest = (Button) findViewById(R.id.buttonEnviarRequest);
        posicionBoton = (RelativeLayout.LayoutParams) botonEnviarRequest.getLayoutParams();

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
     *
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
     *
     * @param item Item clickeado.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Maneja las acciones a realizarse al clickear un item del Navigation Bar.
     *
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
                CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES, URLs.DATOS_PERSONALES
                        , new JSONBuilder().requestBasico()).execute().addListener(this);
                layoutVerNotas.setVisibility(View.INVISIBLE);
                layoutSolicitudes.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_solicitudes:
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutVerNotas.setVisibility(View.INVISIBLE);
                layoutSolicitudes.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_cargarNotas:
                textViewOpcion.setText("Cargar Nota");
                posicionBoton.addRule(RelativeLayout.BELOW, R.id.layoutAlumnoNota);
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
     *
     * @param request  Request a la cual el servidor respondió.
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
        if (!errorId.equals(Error.SUCCESS)) {
            Error.mostrar(errorId);}
        else {
            switch (request.getTaskId()) {
                case RequestTaskIds.MODIFICAR_DATOS_PERSONALES:
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_LONG).show();
                    break;
            }

        }
        return false;
    }



/**
 * Setea el Padding del textView recibido.
 * @param tV TextView al cual se le seteará el Padding.
 * @return TextView con el Padding seteado.
 */

    private TextView setPadding(TextView tV) {
        tV.setPadding(10, 10, 10, 10);
        return tV;
    }
}