package universis.universys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

/**
 * Esta clase es un Activity que se ejecuta cuando el usuario clickea el botón de LogIn.
 * Se encarga de la validación del usuario y de derivarlo al activity correspondinte
 * dependiendo de si es Alumno o Profesor.
 */
public class LoginActivity extends AppCompatActivity{

    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public static String ID_SESION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheHelper.CONTEXT = this;
        setContentView(R.layout.activity_login);
        //Prepara el formulario de login.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mPasswordView.setText("");
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Intenta logearse con la cuenta especificada en el formulario de login.
     * Si hay errores en el formulario(email invalido, contraseña muy corta),
     * se muestran los errores y no se realiza la autenticación con el servidor.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset de errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Comprueba validez de contraseña.
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Comprueba validez de email.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            //Hubo un error, no se hace login contra el servidor
            //y se enfoca el primer campo con error.
            focusView.requestFocus();
        } else {
            //Muestra un spinner de progreso y se inicia el login contra el servidor.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
        }
    }

    /**
     * Condiciones de validez del email.
     * @param email El email a validar.
     * @return TRUE si es válido, FALSE si es inválido.
     */


    //cambiar la validez de caracteres para ingreso de usuario
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Condiciones de validez de la cpntraseña.
     * @param password La contraseña a validar.
     * @return TRUE si es válida, FALSE si es inválida.
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Este método se encarga del spinner de progreso.
     * @param show TRUE para mostrar, FALSE para esconder.
     */
    private void showProgress(final boolean show) {
        //Dependiendo de la versión de Android del celular que ejecuta la aplicación
        //setea los parámetros del spinner para que sea compatible.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Esta clase se encarga de la autenticación de usuario contra el servidor.
     */
    public class UserLoginTask implements IRequestListener {
        UserLoginTask(String email, String password) {
           CHTTPRequest.postRequest(RequestTaskIds.LOGIN,URLs.LOGIN,new JSONBuilder().logIn(email,password)).execute().addListener(this);
        }

        /**
         * Este método se ejecuta al recibir una respuesta del servidor.
         * Analiza la respuesta y ejecuta la activity correspondiente al tipo
         * de usuario (alumno/profesor) o muestra un mensaje de error si lo hubo.
         * @param request Request a la cual el servidor respondió.
         * @param response Respuesta del servidor.
         */
        @Override
        public boolean onResponse(CHTTPRequest request, String response) {
            String errorId = "";
            String rol = "";
            mAuthTask = null;
            showProgress(false);
            try {
                errorId = request.getJsonResponse().getString(Error.ERROR_ID);
                LoginActivity.ID_SESION = request.getJsonResponse().getString("idSesion");
                rol = request.getJsonResponse().getString("rol");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (errorId.equals(Error.SUCCESS)) {
                if(rol.equals("profesor")){
                    Intent i = new Intent(getApplicationContext(), ProfesorMain.class );
                    startActivity(i);
                }
                else if (rol.equals("alumno")){
                    Intent i = new Intent(getApplicationContext(), AlumnoMain.class );
                    startActivity(i);
                }
                // Nuevo
                else{
                    Intent i = new Intent(getApplicationContext(), AdminMain.class );
                    startActivity(i);
                }

            }
            else {
                if(errorId.equals(Error.EMAIL_ERROR)){
                    mEmailView.setError(Error.EMAIL_ERROR_TEXT);
                    mEmailView.requestFocus();
                }
                else if(errorId.equals(Error.PASSWORD_ERROR)) {
                    mPasswordView.setError(Error.PASSWORD_ERROR_TEXT);
                    mPasswordView.requestFocus();
                }
                else Error.mostrar(errorId);
            }
            return false;
        }
    }
}

