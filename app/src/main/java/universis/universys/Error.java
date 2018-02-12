package universis.universys;

import android.widget.Toast;

/**
 * Esta clase se encarga del manejo de errores.
 */
public class Error {

    public static final String SUCCESS = "200";
    public static final String EMAIL_ERROR = "680";
    public static final String PASSWORD_ERROR = "777";
    public static final String SESION_DUPLICADA = "799";
    public static final String CACHE_ERROR = "4516";
    public static final String CARRERA_ERROR = "1001";
    public static final String MATERIA_ERROR = "1002";
    public static final String CATEDRA_ERROR = "1003";
    public static final String EMAIL_REPETIDO_ERROR = "1004";
    public static final String CAMPOS_INCOMPLETOS_ERROR = "1005";
    public static final String NOTA_INVALIDA = "1006";

    public static final String EMAIL_ERROR_TEXT = "Email incorrecto";
    public static final String PASSWORD_ERROR_TEXT = "Contraseña incorrecta";
    public static final String SESION_DUPLICADA_TEXT = "Error: Sesión duplicada";
    public static final String CACHE_ERROR_TEXT = "Sin internet y sin datos guardados";
    public static final String CATEDRA_ERROR_TEXT = "Catedra incorrecta";
    public static final String CARRERA_ERROR_TEXT = "Carrera incorrecta";
    public static final String MATERIA_ERROR_TEXT = "Materia incorrecta";
    public static final String EMAIL_REPETIDO_ERROR_TEXT = "Email en uso";
    public static final String CAMPOS_INCOMPLETOS_ERROR_TEXT = "Deben completarse todos los campos";
    public static final String ERROR_NO_IDENTIFICADO = "Error no identificado";
    public static final String NOTA_INVALIDA_TEXT = "Nota inválida";

    public static final String ERROR_ID = "errorId";

    /**
     * Muestra un Toast con el mensaje correspondiente al id del error.
     * @param id Id del error.
     */
    public static void mostrar(String id) {
        switch (id) {
            case CACHE_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, CACHE_ERROR_TEXT,Toast.LENGTH_SHORT).show();
                break;
            case CATEDRA_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, CATEDRA_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;
            case CARRERA_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, CARRERA_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;
            case MATERIA_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, MATERIA_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;
            case EMAIL_REPETIDO_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, EMAIL_REPETIDO_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;
            case CAMPOS_INCOMPLETOS_ERROR:
                Toast.makeText(CacheHelper.CONTEXT, CAMPOS_INCOMPLETOS_ERROR_TEXT, Toast.LENGTH_SHORT).show();
                break;
            case SESION_DUPLICADA:
                Toast.makeText(CacheHelper.CONTEXT, SESION_DUPLICADA_TEXT,Toast.LENGTH_SHORT).show();
                break;
            case NOTA_INVALIDA:
                Toast.makeText(CacheHelper.CONTEXT, NOTA_INVALIDA_TEXT,Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(CacheHelper.CONTEXT, Error.ERROR_NO_IDENTIFICADO, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
