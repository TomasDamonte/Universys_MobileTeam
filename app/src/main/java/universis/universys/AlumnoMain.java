package universis.universys;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

    public void enviarRequest(int id, String url) {
        CHTTPRequest.postRequest(id, url, new JSONBuilder().requestGenerico(editTextCatedra.getText().toString()
                        ,editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);
    }

    public void modificarDatosAlumno(View v) {
        editTextNombre.setFocusableInTouchMode(true);
        editTextApellido.setFocusableInTouchMode(true);
        editTextDomicilio.setFocusableInTouchMode(true);
        editTextEmail.setFocusableInTouchMode(true);
        editTextFNac.setFocusableInTouchMode(true);
        editTextTelefono.setFocusableInTouchMode(true);
        findViewById(R.id.buttonEnviarDatosAlumno).setEnabled(true);

    }

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

    public void inscripcionesDisponibles() {
        CHTTPRequest.postRequest(RequestTaskIds.MATERIAS_DISPONIBLES,URLs.MATERIAS_DISPONIBLES,
                new JSONBuilder().requestBasico()).execute().addListener(this);
        LinearLayout layoutInscripciones = (LinearLayout) findViewById(R.id.layoutInscripciones);
        LinearLayout inscripciones = (LinearLayout) findViewById(R.id.layoutInscripDisp);
        inscripciones.removeAllViews();
        layoutInscripciones.setVisibility(View.VISIBLE);
    }

    private void blanquearCampos() {
        editTextCarrera.setText("");
        editTextCatedra.setText("");
        editTextMateria.setText("");
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        itemMenu = item.getItemId();
        blanquearCampos();
        LinearLayout layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        LinearLayout layoutCalendario = (LinearLayout) findViewById(R.id.layoutCalendario);
        LinearLayout layoutFichadaAlumno = (LinearLayout) findViewById(R.id.layoutFichadaAlumno);
        LinearLayout layoutInscripciones = (LinearLayout) findViewById(R.id.layoutInscripciones);
        frameLayoutRespuesta.setVisibility(View.INVISIBLE);

        if (itemMenu == R.id.nav_calendario) {
          CHTTPRequest.postRequest(RequestTaskIds.CALENDARIO_ALUMNO,URLs.CALENDARIO_ALUMNO,
                    new JSONBuilder().requestBasico()).execute().addListener(this);
            layoutInscripciones.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            calendarioAlumno.setDateSelected(calendarioAlumno.getSelectedDate(),false);
            calendarioAlumno.setSelectedDate(new CalendarDay().getDate());
            layoutCalendario.setVisibility(View.VISIBLE);
        } else if (itemMenu == R.id.nav_datosPersonales){
            CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES,
                    new JSONBuilder().requestBasico()).execute().addListener(this);
            layoutInscripciones.setVisibility(View.INVISIBLE);
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        } else if (itemMenu == R.id.nav_asistencias || itemMenu == R.id.nav_notas || itemMenu == R.id.nav_horarios
                || itemMenu == R.id.nav_baja) {
            if(itemMenu == R.id.nav_asistencias) textViewOpcion.setText("Asistencias");
            else if(itemMenu == R.id.nav_notas) textViewOpcion.setText("Notas");
            else if(itemMenu == R.id.nav_horarios) textViewOpcion.setText("Horarios");
            else textViewOpcion.setText("Darse de baja");
            layoutInscripciones.setVisibility(View.INVISIBLE);
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.VISIBLE);
        }
        else if (itemMenu == R.id.nav_inscripcion) {
            inscripcionesDisponibles();
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            layoutInscripciones.setVisibility(View.VISIBLE);
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
        if(request.getTaskId() == RequestTaskIds.CALENDARIO_ALUMNO) {
            if(errorId.equals(Error.SUCCESS)) {
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
                calendarioAlumno.addDecorator(new CalendarDecorator(dias));
            } else if(errorId.equals(Error.CACHE_ERROR)) Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
        }
        else if(request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
            if(errorId.equals(Error.SUCCESS)) {
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
            } else if(errorId.equals(Error.CACHE_ERROR)) Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
        }
        else if (request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
            try {
                errorId = request.getJsonArrayResponse().getJSONObject(0).getString(Error.ERROR_ID);
                if(errorId.equals(Error.SUCCESS))
                    crearTablaAsistencias(request.getJsonArrayResponse());
                else if(errorId.equals(Error.CATEDRA_ERROR)) Toast.makeText(this,Error.CATEDRA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
                else if(errorId.equals(Error.CARRERA_ERROR)) Toast.makeText(this,Error.CARRERA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
                else if(errorId.equals(Error.MATERIA_ERROR)) Toast.makeText(this,Error.MATERIA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                if(errorId.equals(Error.CACHE_ERROR)) Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            }
        }
        else if (request.getTaskId() == RequestTaskIds.NOTA_ALUMNO) {
            if(errorId.equals(Error.SUCCESS)) {
                textViewNota.setVisibility(View.VISIBLE);
                try {
                    textViewNota.setText("Nota: " + request.getJsonResponse().getString("nota"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(errorId.equals(Error.CATEDRA_ERROR)) Toast.makeText(this,Error.CATEDRA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CARRERA_ERROR)) Toast.makeText(this,Error.CARRERA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.MATERIA_ERROR)) Toast.makeText(this,Error.MATERIA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CACHE_ERROR)) Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
        }
        else if(request.getTaskId() == RequestTaskIds.HORARIO_ALUMNO) {
            if(errorId.equals(Error.SUCCESS)) {
                linearLayoutHorarios.setVisibility(View.VISIBLE);
                try {
                    mostrarHorarios(request.getJsonResponse());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(errorId.equals(Error.CATEDRA_ERROR)) Toast.makeText(this,Error.CATEDRA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CARRERA_ERROR)) Toast.makeText(this,Error.CARRERA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.MATERIA_ERROR)) Toast.makeText(this,Error.MATERIA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CACHE_ERROR)) Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
        }
        else if (request.getTaskId() == RequestTaskIds.MODIFICAR_DATOS_PERSONALES) {
            if (errorId.equals(Error.SUCCESS))
                Toast.makeText(this, "Datos guardados", Toast.LENGTH_LONG).show();
            else if (errorId.equals(Error.EMAIL_REPETIDO_ERROR))
                Toast.makeText(this, Error.EMAIL_REPETIDO_ERROR_TEXT, Toast.LENGTH_SHORT).show();
            else if (errorId.equals(Error.CAMPOS_INCOMPLETOS_ERROR))
                Toast.makeText(this, Error.CAMPOS_INCOMPLETOS_ERROR_TEXT, Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CACHE_ERROR))
                Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
        }
        else if (request.getTaskId() == RequestTaskIds.MATERIAS_DISPONIBLES){
            if (errorId.equals(Error.SUCCESS)) {
                try {
                    mostrarInscripciones(request.getJsonResponse());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (errorId.equals(Error.CACHE_ERROR))
                Toast.makeText(this, Error.CACHE_ERROR_TEXT, Toast.LENGTH_SHORT).show();
        }
        else if (request.getTaskId() == RequestTaskIds.INSCRIPCION_MATERIA) {
            if (errorId.equals(Error.SUCCESS)) {
                Toast.makeText(this,"Inscripción realizada exitosamente!",Toast.LENGTH_SHORT).show();
                inscripcionesDisponibles();
            }
            else if (errorId.equals(Error.CACHE_ERROR))
                Toast.makeText(this, Error.CACHE_ERROR_TEXT, Toast.LENGTH_SHORT).show();
        }
        else if (request.getTaskId() == RequestTaskIds.BAJA_MATERIA) {
            if (errorId.equals(Error.SUCCESS)) {
                blanquearCampos();
                Toast.makeText(this,"Te has dado te baja exitosamente!",Toast.LENGTH_SHORT).show();
            }
            else if(errorId.equals(Error.CATEDRA_ERROR)) Toast.makeText(this,Error.CATEDRA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CARRERA_ERROR)) Toast.makeText(this,Error.CARRERA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.MATERIA_ERROR)) Toast.makeText(this,Error.MATERIA_ERROR_TEXT,Toast.LENGTH_SHORT).show();
            else if(errorId.equals(Error.CACHE_ERROR)) Toast.makeText(this,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void mostrarInscripciones(JSONObject datos) throws JSONException {
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
                    inscribir.setId(i);
                    editText.setFocusable(false);
                    inscribir.setText("Inscribirse");
                    idCursada.put(i,clase.getString("idCursada"));
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void crearTablaAsistencias(JSONArray datos) throws JSONException {
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

        for(int i=1; i<datos.length();i++){
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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        textViewEvento.setText(calendarEvents.get(date.getDate()));
    }
}
