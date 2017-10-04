package universis.universys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class VistaInicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public void logIn(View view) {
        Intent i = new Intent(this, LoginActivity.class );
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheHelper.context = this;
        setContentView(R.layout.activity_vista_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        WebView webHistoria = (WebView) findViewById(R.id.webHistoria);
        webHistoria.setVisibility(View.VISIBLE);
        webHistoria.loadUrl("http://www.ub.edu.ar/institucional.php");
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
     //   getMenuInflater().inflate(R.menu.vista_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    /*  if (id == R.id.action_settings) {
            return true;
        }
    */
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        LinearLayout layoutContacto = (LinearLayout) findViewById(R.id.layoutContacto);
        LinearLayout layoutPlanesEstudio = (LinearLayout) findViewById(R.id.layoutPlanesEstudio);
        WebView webCalendario = (WebView) findViewById(R.id.webCalendario);
        WebView webHistoria = (WebView) findViewById(R.id.webHistoria);
        int id = item.getItemId();
        if (id == R.id.nav_contacto) {
            layoutPlanesEstudio.setVisibility(View.INVISIBLE);
            webCalendario.setVisibility(View.INVISIBLE);
            webHistoria.setVisibility(View.INVISIBLE);
            layoutContacto.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_calendario) {
            layoutContacto.setVisibility(View.INVISIBLE);
            layoutPlanesEstudio.setVisibility(View.INVISIBLE);
            webHistoria.setVisibility(View.INVISIBLE);
            webCalendario.setVisibility(View.VISIBLE);
            webCalendario.loadUrl("http://www.ub.edu.ar/academico.php?opcion=calendarios");
        } else if (id == R.id.nav_planEstudio) {
            layoutContacto.setVisibility(View.INVISIBLE);
            webCalendario.setVisibility(View.INVISIBLE);
            webHistoria.setVisibility(View.INVISIBLE);
            layoutPlanesEstudio.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_historia) {
            layoutContacto.setVisibility(View.INVISIBLE);
            layoutPlanesEstudio.setVisibility(View.INVISIBLE);
            webCalendario.setVisibility(View.INVISIBLE);
            webHistoria.setVisibility(View.VISIBLE);
            webHistoria.loadUrl("http://www.ub.edu.ar/institucional.php");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
