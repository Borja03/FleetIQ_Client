package factories;

import logicInterface.EnvioManager;
import service.EnvioRESTClient;

/**
 * Fábrica para la obtención de instancias de {@link EnvioManager}.
 * <p>
 * Esta clase implementa el patrón Singleton para garantizar que solo 
 * exista una única instancia de {@code EnvioManager} durante la ejecución 
 * de la aplicación. Si no hay una instancia previa, se crea una nueva 
 * utilizando {@link EnvioRESTClient}.
 * </p>
 *
 * @author Alder
 */
public class EnvioFactory {

    // Instancia única de EnvioManager
    private static EnvioManager envioManager;

    /**
     * Obtiene una instancia única de {@code EnvioManager}.
     * <p>
     * Si no existe una instancia previa, se inicializa con una 
     * implementación de {@link EnvioRESTClient}. En futuras llamadas,
     * se devuelve la misma instancia ya creada.
     * </p>
     *
     * @return Instancia única de {@code EnvioManager}.
     */
    public static EnvioManager getEnvioInstance() {
        if (envioManager == null) {
            envioManager = (EnvioManager) new EnvioRESTClient();
        }
        return envioManager;
    }
}
