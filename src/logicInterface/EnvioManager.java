
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicInterface;

import exception.SelectException;
import exception.CreateException;
import exception.UpdateException;
import exception.DeleteException;

import java.util.ArrayList;
import java.util.Date;
import models.Envio;

/**
 * Interface que define las operaciones para gestionar envíos.
 * Proporciona métodos para añadir, actualizar, eliminar y filtrar envíos en el sistema.
 * Cada método puede lanzar una excepción específica si ocurre un error durante su ejecución.
 *
 * @author Alder
 */
public interface EnvioManager {
    /**
     * Añade un nuevo envío al sistema.
     *
     * @throws createException si ocurre un error al crear el envío.
     */
    public void addEnvio() throws CreateException;

    /**
     * Actualiza los datos de un envío existente.
     *
     * @param envio el objeto Envio con los datos actualizados.
     * @return el objeto Envio actualizado.
     * @throws updateException si ocurre un error durante la actualización del envío.
     */
    public Envio updateEnvio(Envio envio) throws UpdateException;

    /**
     * Elimina un envío del sistema basado en su identificador.
     *
     * @param id del identificador del envío a eliminar.
     * @return el objeto Envio eliminado.
     * @throws deleteException si ocurre un error al eliminar el envío.
     */
    public Envio deleteEnvio(Integer id) throws DeleteException;

    /**
     * Obtiene una lista de todos los envíos registrados en el sistema.
     *
     * @return una lista de objetos Envio.
     * @throws selectException si ocurre un error al obtener los datos.
     */
    public ArrayList<Envio> selectAll() throws SelectException;

    /**
     * Filtra los envíos registrados en el sistema por un rango de fechas.
     *
     * @param firstDate la fecha inicial del rango.
     * @param secondDate la fecha final del rango.
     * @return una lista de objetos Envio que cumplen con el rango de fechas.
     * @throws selectException si ocurre un error al filtrar los datos.
     */
    public ArrayList<Envio> filterByDates(Date firstDate, Date secondDate) throws SelectException;

    /**
     * Filtra los envíos registrados en el sistema por estado.
     *
     * @param estado el estado a filtrar (por ejemplo, "PREPARACION", "EN_REPARTO", "ENTREGADO").
     * @return una lista de objetos Envio que tienen el estado especificado.
     * @throws selectException si ocurre un error al filtrar los datos.
     */
    public ArrayList<Envio> filterEstado(String estado) throws SelectException;

    /**
     * Filtra los envíos registrados en el sistema por el número de paquetes.
     *
     * @param numPaquetes el número de paquetes a filtrar.
     * @return una lista de objetos Envio que cumplen con el número de paquetes especificado.
     * @throws selectException si ocurre un error al filtrar los datos.
     */
    public ArrayList<Envio> filterNumPaquetes(Integer numPaquetes) throws SelectException;
}
