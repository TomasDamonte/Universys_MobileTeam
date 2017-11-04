package universis.universys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.HashSet;

public class VistaInicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public void logIn(View view) {
        Intent i = new Intent(this, LoginActivity.class );
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheHelper.CONTEXT = this;
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

        cargarCalendarioPublico();
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
        // Handle navigation view item clicks here.
        LinearLayout layoutContacto = (LinearLayout) findViewById(R.id.layoutContacto);
        LinearLayout layoutCalendario = (LinearLayout) findViewById(R.id.layoutCalendario);
        LinearLayout layoutHistoria = (LinearLayout) findViewById(R.id.layoutHistoria);
        int id = item.getItemId();
        switch (id){
            case R.id.nav_contacto:
                layoutCalendario.setVisibility(View.INVISIBLE);
                layoutHistoria.setVisibility(View.INVISIBLE);
                layoutContacto.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_calendario:
                layoutContacto.setVisibility(View.INVISIBLE);
                layoutHistoria.setVisibility(View.INVISIBLE);
                layoutCalendario.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_historia:
                layoutContacto.setVisibility(View.INVISIBLE);
                layoutCalendario.setVisibility(View.INVISIBLE);
                layoutHistoria.setVisibility(View.VISIBLE);
                break;
        }
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void cargarCalendarioPublico() {

        MaterialCalendarView calendarioPublico = (MaterialCalendarView) findViewById(R.id.calendarioPublico);
        HashSet<CalendarDay> dias = new HashSet<>();
        for(int i=15;i<=30;i++){
            dias.add(new CalendarDay(2017,6,i));
        }
        for(int i=1;i<=30;i++){
            dias.add(new CalendarDay(2017,11,i));
        }
        calendarioPublico.addDecorator(new CalendarDecorator(dias, Color.RED));

        dias = new HashSet<>();
        for(int i= 2;i<=5;i++){
            for (int j=1;j<=31;j++){
                dias.add(new CalendarDay(2017,i,j));
            }
        }
        for(int i= 7;i<=10;i++){
            for (int j=1;j<=31;j++){
                dias.add(new CalendarDay(2017,i,j));
            }
        }
        calendarioPublico.addDecorator(new CalendarDecorator(dias, Color.GREEN));
    }
}


