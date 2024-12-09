/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterfaces;

import exceptions.selectException;
import exceptions.createException;
import exceptions.updateException;
import exceptions.deleteException;
import java.sql.Time;

import java.util.Date;

/**
 *
 * @author Borja
 */
public interface RutaManager {
    public void addRuta() throws createException;//El tipo de entrada será un objeto ruta
    public void updateRuta() throws updateException;//El tipo de entrada será un objeto ruta y el de salida tambien
    public void deleteRuta(Integer id) throws deleteException;//El tipo de salida sera un objeto Ruta
    public void selectAll() throws selectException;//salida array ruta
    public void filterByDates(Date firstDate,Date secondDate) throws selectException;//salida array rutas
    public void filterTiempo(Time tiempo) throws selectException;//Parametro entrada será un tipo del enum y salida un array de rutas
    public void filterDistacia(Integer distancia) throws selectException;//Salida array rutas
}
