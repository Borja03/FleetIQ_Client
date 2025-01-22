package logicInterface;

import javax.ws.rs.ClientErrorException;
import java.util.List;
import javax.ws.rs.core.GenericType;

public interface EnvioRutaVehiculoManager {

    // Método para contar el total de registros de EnvioRutaVehiculo
    String count() throws ClientErrorException;

    // Método para editar un registro específico de EnvioRutaVehiculo en formato XML
    void edit_XML(Object requestEntity, String id) throws ClientErrorException;

    // Método para editar un registro específico de EnvioRutaVehiculo en formato JSON
    void edit_JSON(Object requestEntity, String id) throws ClientErrorException;

    // Método para obtener un registro específico de EnvioRutaVehiculo en formato XML
    <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException;

    // Método para obtener un registro específico de EnvioRutaVehiculo en formato JSON
    <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException;

    // Método para obtener un rango de registros en formato XML
    <T> T findRange_XML(Class<T> responseType, String from, String to) throws ClientErrorException;

    // Método para obtener un rango de registros en formato JSON
    <T> T findRange_JSON(Class<T> responseType, String from, String to) throws ClientErrorException;

    // Método para crear un nuevo registro de EnvioRutaVehiculo en formato XML
    void create_XML(Object requestEntity) throws ClientErrorException;

    // Método para crear un nuevo registro de EnvioRutaVehiculo en formato JSON
    void create_JSON(Object requestEntity) throws ClientErrorException;

    // Método para obtener todos los registros de EnvioRutaVehiculo en formato XML
    <T> T findAll_XML(GenericType<T> responseType) throws ClientErrorException;

    // Método para obtener todos los registros de EnvioRutaVehiculo en formato JSON
    <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException;

    // Método para contar los registros por un rutaId específico
    String countByRutaId(String rutaId) throws ClientErrorException;

    // Método para eliminar un registro específico de EnvioRutaVehiculo por su id
    void remove(String id) throws ClientErrorException;
}
