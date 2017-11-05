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
public class AlumnoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener,OnDateSelectedListener{

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

        if (editTextCatedra.getText().toString().equals("") || editTextCarrera.getText().toString().equals("")
                || editTextMateria.getText().toString().equals("")) {
            Toast.makeText(this, "Deben completarse todos los campos", Toast.LENGTH_LONG).show();
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
        editTextDomicilio.setFocusable(false);;
        editTextEmail.setFocusable(false);
        editTextFNac.setFocusable(false);
        editTextTelefono.setFocusable(false);
        findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(false);
        String[] datos = {editTextNombre.getText().toString(),editTextApellido.getText().toString(),editTextDomicilio.getText().toString(),
                editTextEmail.getText().toString(),editTextFNac.getText().toString(),editTextTelefono.getText().toString()};
        CHTTPRequest.postRequest(RequestTaskIds.MODIFICAR_DATOS_PERSONALES,URLs.MODIFICAR_DATOS_PERSONALES
                ,new JSONBuilder().modificarDatosPersonales(datos)).execute().addListener(this);
    }

    /**
     * Envía una request al servidor para ver las inscripciones
     * a materias que el alumno tiene disponible.
     */
    public void inscripcionesDisponibles() {
        CHTTPRequest.postRequest(RequestTaskIds.MATERIAS_DISPONIBLES,URLs.MATERIAS_DISPONIBLES,
                new JSONBuilder().requestBasico()).execute().addListener(this);
        LinearLayout inscripciones = (LinearLayout) findViewById(R.id.layoutInscripDisp);
        inscripciones.removeAllViews();
        layoutInscripciones.setVisibility(View.VISIBLE);
    }

    /**
     * Resetea los editText.
     */
    private void blanquearCampos() {
        editTextCarrera.setText("");
        editTextCatedra.setText("");
        editTextMateria.setText("");
    }

    /**
     * Inicializa el Activity y los atributos de la clase.
     * @param savedInstanceState Variable recibida al ejecutarse el Activity
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
        calendarioAlumno.setOnDateChangedListener(this);
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
            case R.id.nav_calendario:
                CHTTPRequest.postRequest(RequestTaskIds.CALENDARIO_ALUMNO,URLs.CALENDARIO_ALUMNO,
                        new JSONBuilder().requestBasico()).execute().addListener(this);
                layoutInscripciones.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutFichadaAlumno.setVisibility(View.INVISIBLE);
                calendarioAlumno.setDateSelected(calendarioAlumno.getSelectedDate(),false);
                calendarioAlumno.setSelectedDate(new CalendarDay().getDate());
                layoutCalendario.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_datosPersonales:
                CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES,
                        new JSONBuilder().requestBasico()).execute().addListener(this);
                layoutInscripciones.setVisibility(View.INVISIBLE);
                layoutCalendario.setVisibility(View.INVISIBLE);
                layoutFichadaAlumno.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_inscripcion:
                inscripcionesDisponibles();
                layoutCalendario.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutFichadaAlumno.setVisibility(View.INVISIBLE);
                layoutInscripciones.setVisibility(View.VISIBLE);
                break;
            default:
                if(itemMenu == R.id.nav_asistencias) textViewOpcion.setText("Asistencias");
                else if(itemMenu == R.id.nav_notas) textViewOpcion.setText("Notas");
                else if(itemMenu == R.id.nav_horarios) textViewOpcion.setText("Horarios");
                else textViewOpcion.setText("Darse de baja");
                layoutInscripciones.setVisibility(View.INVISIBLE);
                layoutCalendario.setVisibility(View.INVISIBLE);
                layoutDatosPersonales.setVisibility(View.INVISIBLE);
                layoutFichadaAlumno.setVisibility(View.VISIBLE);
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
                case RequestTaskIds.CALENDARIO_ALUMNO:
                    taskCalendario(request);
                    break;
                case RequestTaskIds.DATOS_PERSONALES:
                    taskDatosPersonales(request);
                    break;
                case RequestTaskIds.FICHADA_ALUMNO:
                    try {
                        taskFichadaAlumno(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case RequestTaskIds.NOTA_ALUMNO:
                    textViewNota.setVisibility(View.VISIBLE);
                    try {
                        textViewNota.setText("Nota: " + request.getJsonResponse().getString("nota"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case RequestTaskIds.HORARIO_ALUMNO:
                    linearLayoutHorarios.setVisibility(View.VISIBLE);
                    try {
                        mostrarHorarios(request.getJsonResponse());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case RequestTaskIds.MODIFICAR_DATOS_PERSONALES:
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_LONG).show();
                    break;
                case RequestTaskIds.MATERIAS_DISPONIBLES:
                    try {
                        taskMateriasDisponibles(request.getJsonResponse());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case RequestTaskIds.INSCRIPCION_MATERIA:
                    Toast.makeText(this, "Inscripción realizada exitosamente!", Toast.LENGTH_SHORT).show();
                    inscripcionesDisponibles();
                    break;
                case RequestTaskIds.BAJA_MATERIA:
                    Toast.makeText(this, "Te has dado te baja exitosamente!", Toast.LENGTH_SHORT).show();
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
     * Se ejecuta cuando el servidor respondió a la request Calendario_Alumno.
     * @dias Se guardan qué días tienen eventos.
     * @calendarEvents Se guardan los eventos asignados a cada día.
     * @param request Resquest de la cual se obtiene la respuesta del servidor.
     */
    private void taskCalendario(CHTTPRequest request) {
        JSONArray eventos = new JSONArray();
        try {
            eventos = request.getJsonResponse().getJSONArray("eventos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashSet<CalendarDay> dias = new HashSet<>();
        calendarEvents = new HashMap<>();
        try {
            for (int i=0;i<eventos.length();i++) {
                dias.add(new CalendarDay(new Date(eventos.getJSONObject(i).getString("fecha"))));
                String evento;
                if(calendarEvents.get(new Date(eventos.getJSONObject(i).getString("fecha"))) == null) evento = eventos.getJSONObject(i).getString("evento");
                else evento = calendarEvents.get(new Date(eventos.getJSONObject(i).getString("fecha"))) + ", " + eventos.getJSONObject(i).getString("evento");
                calendarEvents.put(new Date(eventos.getJSONObject(i).getString("fecha")),evento);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        calendarioAlumno.addDecorator(new CalendarDecorator(dias,Color.RED));
    }

    /**
     * Se ejecuta cuando el servidor respondió a la request Materias_Disponibles.
     * Muestra las materias con sus horarios y cátedras a las cuales al alumno puede inscribirse.
     * Se genera un botón 'Inscribirse' por cada materia.
     * @param datos Respuesta del servidor.
     * @idCursada Se guarda a qué cursada corresponde cada botón.
     * @throws JSONException Por si ocurre algún error al leer el JSON.
     */
    private void taskMateriasDisponibles(JSONObject datos) throws JSONException {
        JSONArray inscripcionesDisponibles = datos.getJSONArray("inscripcionesDisponibles");
        LinearLayout inscripciones = (LinearLayout) findViewById(R.id.layoutInscripDisp);
        idCursada = new HashMap<>();
        for(int i=0;i<inscripcionesDisponibles.length();i++) {
            String texto = "";
            EditText editText = new EditText(this);
            JSONObject materias = inscripcionesDisponibles.getJSONObject(i);
            texto = texto +"Materia: " + materias.getString("nombre") + "\n";
            JSONArray catedras = materias.getJSONArray("catedras");
            for(int j=0;j<catedras.length();j++) {
                JSONObject cursadas = catedras.getJSONObject(j);
                texto = texto + "Catedra: " + cursadas.getString("catedra")+ "\n";
                JSONArray horarios = cursadas.getJSONArray("cursadas");
                for(int k=0;k<horarios.length();k++) {
                    JSONObject clase = horarios.getJSONObject(k);
                    texto = texto + "Horarios:\n" + mostrarHorarios(clase) + "\n";
                    editText.setText(texto);
                    Button inscribir = new Button(this);
                    inscribir.setId(i+j+k);
                    editText.setFocusable(false);
                    inscribir.setText("Inscribirse");
                    idCursada.put(i+j+k,clase.getString("idCursada"));
                    inscribir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CHTTPRequest.postRequest(RequestTaskIds.INSCRIPCION_MATERIA,URLs.INSCRIPCION_MATERIA,
                                    new JSONBuilder().inscripcionAMateria(idCursada.get(v.getId()))).execute().addListener(AlumnoMain.this);
                        }
                    });
                    inscripciones.addView(editText);
                    inscripciones.addView(inscribir);
                }
            }
        }

    }

    /**
     * Decodifica un array de horarios en hexadecimal.
     * @param datos Respuesta del servidor.
     * @return String resultante del array de horarios decodificado.
     * @throws JSONException Por si ocurre algún error al leer el JSON.
     */
    private String mostrarHorarios(JSONObject datos) throws JSONException {
        JSONArray JSONArrayHorarios = datos.getJSONArray("horario");
        String texto = "Teoría:\n";
        for(int i=0;i<JSONArrayHorarios.length();i++) {
            String teoria = JSONArrayHorarios.getString(i).split(";")[0];
            if((teoria.charAt(0)+"").equals("0")) texto = texto + "Lunes de ";
            else if((teoria.charAt(0)+"").equals("1")) texto = texto + "Martes de ";
            else if((teoria.charAt(0)+"").equals("2")) texto = texto + "Miercoles de ";
            else if((teoria.charAt(0)+"").equals("3")) texto = texto + "Jueves de ";
            else if((teoria.charAt(0)+"").equals("4")) texto = texto + "Viernes de ";
            else if((teoria.charAt(0)+"").equals("5")) texto = texto + "Sábado de ";
            texto = texto + (Integer.parseInt(teoria.charAt(1)+"", 16) + 7) + " a ";
            texto = texto + (Integer.parseInt(teoria.charAt(2)+"", 16) + 7) + "hs.\n";
        }
        texto = texto + "\nPráctica:\n";
        for(int i=0;i<JSONArrayHorarios.length();i++) {
            String teoria = JSONArrayHorarios.getString(i).split(";")[1];
            if((teoria.charAt(0)+"").equals("0")) texto = texto + "Lunes de ";
            else if((teoria.charAt(0)+"").equals("1")) texto = texto + "Martes de ";
            else if((teoria.charAt(0)+"").equals("2")) texto = texto + "Miércoles de ";
            else if((teoria.charAt(0)+"").equals("3")) texto = texto + "Jueves de ";
            else if((teoria.charAt(0)+"").equals("4")) texto = texto + "Viernes de ";
            else if((teoria.charAt(0)+"").equals("5")) texto = texto + "Sábado de ";
            texto = texto + (Integer.parseInt(teoria.charAt(1)+"", 16) + 7) + " a ";
            texto = texto + (Integer.parseInt(teoria.charAt(2)+"", 16) + 7) + "hs.\n";
        }
        editTextHorario.setText(texto);
        return texto;
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

    /**
     * Crea y muestra una tabla con los campos "FECHA"[dd/mm/aaaa] y "PRESENTE" [SI/NO]
     * Dependiendo del valor del campo "PRESENTE" la fila tiene Background rojo [NO] o verde [SI].
     * @param request Request que contiene la respuesta del servidor.
     * @throws JSONException Por si ocurre algún error al leer el JSON.
     */
    private void taskFichadaAlumno(CHTTPRequest request) throws JSONException {
        JSONArray datos = request.getJsonResponse().getJSONArray("fichadas");
        ScrollView sVTablaFichadas = (ScrollView) findViewById(R.id.sVTablaFichadas);
        sVTablaFichadas.setVisibility(View.VISIBLE);
        TableLayout tabla = (TableLayout)findViewById(R.id.tablaFichadas);
        TableRow fila = new TableRow(this);
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
            presente = setPadding(presente);
            fecha = setPadding(fecha);
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

    /**
     * Llamdo al clickearse un día del calendario.
     * Muestra los eventos asignados al día clickeado.
     * @param widget El calendario.
     * @param date Día clickeado.
     * @param selected
     */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        textViewEvento.setText(calendarEvents.get(date.getDate()));
    }
}
