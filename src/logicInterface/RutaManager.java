/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterface;

import exception.selectException;
import exception.createException;
import exception.updateException;
import exception.deleteException;
import java.util.ArrayList;

import java.util.Date;
import models.FilterTypeRuta;
import models.Ruta;

/**
 * Interfaz que define las operaciones CRUD y de filtrado para gestionar las rutas en el sistema.
 * Proporciona métodos para añadir, actualizar, eliminar, seleccionar todas las rutas 
 * y realizar filtros específicos basados en fechas, tiempo y distancia.
 * 
 * @author Borja
 */
public interface RutaManager {

    /**
     * Añade una nueva ruta al sistema.
     *
     * @throws createException si ocurre un error al crear la ruta.
     */
    public void addRuta() throws createException;

    /**
     * Actualiza una ruta existente en el sistema.
     *
     * @param ruta el objeto {@link Ruta} que contiene los datos actualizados de la ruta.
     * @return el objeto {@link Ruta} actualizado.
     * @throws updateException si ocurre un error durante la actualización de la ruta.
     */
    public Ruta updateRuta(Ruta ruta) throws updateException;

    /**
     * Elimina una ruta del sistema utilizando su identificador único.
     *
     * @param idRuta el identificador único de la ruta a eliminar.
     * @return el objeto {@link Ruta} eliminado.
     * @throws deleteException si ocurre un error durante la eliminación de la ruta.
     */
    public Ruta deleteRuta(Integer idRuta) throws deleteException;

    /**
     * Selecciona y devuelve todas las rutas almacenadas en el sistema.
     *
     * @return una lista de objetos {@link Ruta} que representan todas las rutas disponibles.
     * @throws selectException si ocurre un error al seleccionar las rutas.
     */
    public ArrayList<Ruta> selectAll() throws selectException;

    /**
     * Filtra las rutas basándose en un rango de fechas.
     *
     * @param firstDate la fecha inicial del rango.
     * @param secondDate la fecha final del rango.
     * @return una lista de objetos {@link Ruta} que se encuentran dentro del rango de fechas especificado.
     * @throws selectException si ocurre un error durante el filtrado de rutas por fechas.
     */
    public ArrayList<Ruta> filterByDates(Date firstDate, Date secondDate) throws selectException;

    /**
     * Filtra las rutas basándose en el tiempo y un tipo de filtro específico.
     *
     * @param filterType el tipo de filtro definido por {@link FilterTypeRuta}.
     * @param tiempo el tiempo en minutos para realizar el filtrado.
     * @return una lista de objetos {@link Ruta} que cumplen con los criterios de tiempo y tipo de filtro.
     * @throws selectException si ocurre un error durante el filtrado de rutas por tiempo.
     */
    public ArrayList<Ruta> filterTiempo(FilterTypeRuta filterType, Integer tiempo) throws selectException;

    /**
     * Filtra las rutas basándose en la distancia y un tipo de filtro específico.
     *
     * @param filterType el tipo de filtro definido por {@link FilterTypeRuta}.
     * @param distancia la distancia en kilómetros para realizar el filtrado.
     * @return una lista de objetos {@link Ruta} que cumplen con los criterios de distancia y tipo de filtro.
     * @throws selectException si ocurre un error durante el filtrado de rutas por distancia.
     */
    public ArrayList<Ruta> filterDistancia(FilterTypeRuta filterType, Integer distancia) throws selectException;
}

