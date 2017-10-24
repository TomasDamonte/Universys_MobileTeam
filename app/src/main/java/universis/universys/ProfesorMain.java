package universis.universys;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfesorMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestListener {

    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextDomicilio;
    private EditText editTextEmail;
    private EditText editTextFNac;
    private EditText editTextTelefono;
    private EditText editTextCatedra;
    private EditText editTextCarrera;
    private EditText editTextMateria;

    public void enviarRequest(View v) {
        TableLayout tablaNotas = (TableLayout) findViewById(R.id.tablaNotas);
        tablaNotas.removeAllViews();
        ScrollView scrollViewTablaNotas = (ScrollView) findViewById(R.id.ScrollViewTablaNotas);
        scrollViewTablaNotas.setVisibility(View.INVISIBLE);
        if (editTextCatedra.getText().toString().equals("") || editTextCarrera.getText().toString().equals("") || editTextMateria.getText().toString().equals("")) {
            Toast.makeText(this, "Deben completarse todos los campos", Toast.LENGTH_LONG).show();
        } else {
            CHTTPRequest.postRequest(RequestTaskIds.NOTAS_PROFESOR,URLs.NOTAS_PROFESOR
                    ,new JSONBuilder().fichadaAlumno(editTextCatedra.getText().toString()
                            ,editTextCarrera.getText().toString(), editTextMateria.getText().toString())).execute().addListener(this);
        }

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
       // getMenuInflater().inflate(R.menu.profesor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        LinearLayout layoutDatosPersonales = (LinearLayout) findViewById(R.id.layoutDatosPersonales);
        LinearLayout layoutVerNotas = (LinearLayout) findViewById(R.id.layoutVerNotas);

        if (id == R.id.nav_datosPersonales){
            CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES
                    ,new JSONBuilder().consultaDatosPersonales()).execute().addListener(this);
            layoutVerNotas.setVisibility(View.INVISIBLE);
            layoutDatosPersonales.setVisibility(View.VISIBLE);
        }

        if (id == R.id.nav_verNotas) {
            layoutDatosPersonales.setVisibility(View.INVISIBLE);
            layoutVerNotas.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onResponse(CHTTPRequest request, String response) {
        String errorId = null;
        try {
            errorId = request.getJsonResponse().getString(Error.ERROR_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
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

        else if(request.getTaskId() == RequestTaskIds.NOTAS_PROFESOR) {
            if(errorId.equals(Error.SUCCESS)) {
                try {
                    crearTablaNotas(request.getJsonResponse().getJSONArray("alumnos"));
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void crearTablaNotas(JSONArray datos) throws JSONException {
        ScrollView scrollViewTablaNotas = (ScrollView) findViewById(R.id.ScrollViewTablaNotas);
        scrollViewTablaNotas.setVisibility(View.VISIBLE);
        TableLayout tabla = (TableLayout)findViewById(R.id.tablaNotas);
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
        tabla.addView(fila);
        tabla.setPadding(5,5,5,5);
        tabla.setBackgroundColor(Color.BLACK);

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
            tabla.addView(fila);
        }
    }
}
