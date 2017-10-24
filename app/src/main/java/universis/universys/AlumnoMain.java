package universis.universys;

import android.graphics.Color;
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

    public void enviarRequest(View v) {

        TableLayout tabla = (TableLayout) findViewById(R.id.tablaFichadas);
        tabla.removeAllViews();
        ScrollView sVTablaFichadas = (ScrollView) findViewById(R.id.sVTablaFichadas);
        sVTablaFichadas.setVisibility(View.INVISIBLE);
        textViewNota.setVisibility(View.INVISIBLE);
        linearLayoutHorarios.setVisibility(View.INVISIBLE);

        if (editTextCatedra.getText().toString().equals("") || editTextCarrera.getText().toString().equals("") || editTextMateria.getText().toString().equals("")) {
            Toast.makeText(this, "Deben completarse todos los campos", Toast.LENGTH_LONG).show();
        } else {
            if(itemMenu == R.id.nav_asistencias) {
                CHTTPRequest.postRequest(RequestTaskIds.FICHADA_ALUMNO, URLs.FICHADA_ALUMNO
                        ,new JSONBuilder().fichadaAlumno(editTextCatedra.getText().toString()
                                ,editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);
            } else if(itemMenu == R.id.nav_notas) {
                CHTTPRequest.postRequest(RequestTaskIds.NOTA_ALUMNO, URLs.NOTA_ALUMNO
                        ,new JSONBuilder().fichadaAlumno(editTextCatedra.getText().toString()
                                ,editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);

            } else if (itemMenu == R.id.nav_horarios) {
                CHTTPRequest.postRequest(RequestTaskIds.HORARIO_ALUMNO, URLs.HORARIO_ALUMNO
                        ,new JSONBuilder().fichadaAlumno(editTextCatedra.getText().toString()
                                ,editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);
            }
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
        itemMenu = item.getItemId();
        LinearLayout layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        LinearLayout layoutCalendario = (LinearLayout) findViewById(R.id.layoutCalendario);
        LinearLayout layoutFichadaAlumno = (LinearLayout) findViewById(R.id.layoutFichadaAlumno);
        frameLayoutRespuesta.setVisibility(View.INVISIBLE);

        if (itemMenu == R.id.nav_calendario) {
          CHTTPRequest.postRequest(RequestTaskIds.CALENDARIO_ALUMNO,URLs.CALENDARIO_ALUMNO,
                    new JSONBuilder().consultaDatosPersonales()).execute().addListener(this);
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            calendarioAlumno.setDateSelected(calendarioAlumno.getSelectedDate(),false);
            calendarioAlumno.setSelectedDate(new CalendarDay().getDate());
            layoutCalendario.setVisibility(View.VISIBLE);
        } else if (itemMenu == R.id.nav_datosPersonales){
            CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES,
                    new JSONBuilder().consultaDatosPersonales()).execute().addListener(this);
            layoutCalendario.setVisibility(View.INVISIBLE);
            layoutFichadaAlumno.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        } else if (itemMenu == R.id.nav_asistencias || itemMenu == R.id.nav_notas || itemMenu == R.id.nav_horarios) {
            if(itemMenu == R.id.nav_asistencias) textViewOpcion.setText("Asistencias");
            else if(itemMenu == R.id.nav_notas) textViewOpcion.setText("Notas");
            else textViewOpcion.setText("Horarios");
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
        frameLayoutRespuesta.setVisibility(View.VISIBLE);
        String errorId = null;
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
                for (int i=0;i<eventos.length();i++) {
                    try {
                        dias.add(new CalendarDay(new Date(eventos.getJSONObject(i).getString("fecha"))));
                        String evento;
                        if(calendarEvents.get(new Date(eventos.getJSONObject(i).getString("fecha"))) == null) evento = eventos.getJSONObject(i).getString("evento");
                        else evento = calendarEvents.get(new Date(eventos.getJSONObject(i).getString("fecha"))) + ", " + eventos.getJSONObject(i).getString("evento");
                        calendarEvents.put(new Date(eventos.getJSONObject(i).getString("fecha")),evento);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

        } else if (request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
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
        } else if (request.getTaskId() == RequestTaskIds.NOTA_ALUMNO) {

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

        } else if(request.getTaskId() == RequestTaskIds.HORARIO_ALUMNO) {
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
        return false;
    }

    private void mostrarHorarios(JSONObject datos) throws JSONException {

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
    }

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
