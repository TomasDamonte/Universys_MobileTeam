package universis.universys;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class Tab3Calendario extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3calendario, container, false);
        WebView webView=(WebView)rootView.findViewById(R.id.webCalendario);
        webView.loadUrl("http://www.ub.edu.ar/academico.php?opcion=calendarios");
        return rootView;
    }
}
