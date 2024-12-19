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
import java.sql.Time;


import java.util.Date;

/**
 *
 * @author Borja
 */
public interface RutaManager {
    public void addRuta() throws CreateException;//El tipo de entrada será un objeto ruta
    public void updateRuta() throws UpdateException;//El tipo de entrada será un objeto ruta y el de salida tambien
    public void deleteRuta(Integer id) throws DeleteException;//El tipo de salida sera un objeto Ruta
    public void selectAll() throws SelectException;//salida array ruta
    public void  filterByDates(Date firstDate,Date secondDate) throws SelectException;//salida array rutas
    public void filterTiempo(Time tiempo) throws SelectException;//Parametro entrada será un tipo del enum y salida un array de rutas
    public void filterDistacia(Integer distancia) throws SelectException;//Salida array rutas
}
