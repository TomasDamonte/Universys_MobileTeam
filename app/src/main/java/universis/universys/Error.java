package universis.universys;

import android.widget.Toast;

public class Error {

    public static final String SUCCESS = "200";
    public static final String EMAIL_ERROR = "680";
    public static final String PASSWORD_ERROR = "777";
    public static final String SECION_DUPLICADA = "799";
    public static final String CACHE_ERROR = "4516";
    public static final String CARRERA_ERROR = "1001";
    public static final String MATERIA_ERROR = "1002";
    public static final String CATEDRA_ERROR = "1003";
    public static final String EMAIL_REPETIDO_ERROR = "1004";
    public static final String CAMPOS_INCOMPLETOS_ERROR = "1005";
    public static final String ACEPTAR_SOLICITUDES_ERROR = "1006";



    public static final String EMAIL_ERROR_TEXT = "Email incorrecto";
    public static final String PASSWORD_ERROR_TEXT = "Contraseña incorrecta";
    public static final String SECION_DUPLICADA_TEXT = "Error: Sesion duplicada";
    public static final String CACHE_ERROR_TEXT = "Sin internet y sin datos guardados";
    public static final String CATEDRA_ERROR_TEXT = "Catedra incorrecta";
    public static final String CARRERA_ERROR_TEXT = "Carrera incorrecta";
    public static final String MATERIA_ERROR_TEXT = "Materia incorrecta";
    public static final String EMAIL_REPETIDO_ERROR_TEXT = "Email en uso";
    public static final String CAMPOS_INCOMPLETOS_ERROR_TEXT = "Se deben completar todos los campos";
    public static final String ERROR_NO_IDENTIFICADO = "Error no idesntificado";


    public static final String ERROR_ID = "errorId";

    public static void mostrar(String id) {
        switch (id) {

            case Error.CACHE_ERROR:
                Toast.makeText(CacheHelper.CONTEXT,Error.CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
                break;

            case Error.CATEDRA_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, Error.CATEDRA_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;

            case Error.CARRERA_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, Error.CARRERA_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;

            case Error.MATERIA_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, Error.MATERIA_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;

            case Error.EMAIL_REPETIDO_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, Error.EMAIL_REPETIDO_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;

            case Error.CAMPOS_INCOMPLETOS_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, Error.CAMPOS_INCOMPLETOS_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(CacheHelper.CONTEXT, Error.ERROR_NO_IDENTIFICADO, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
