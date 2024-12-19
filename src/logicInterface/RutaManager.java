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
 *
 * @author Borja
 */
public interface RutaManager {

    public void addRuta() throws createException; // El tipo de entrada será un objeto Ruta

    public Ruta updateRuta(Ruta ruta) throws updateException; // El tipo de entrada será un objeto Ruta

    public Ruta deleteRuta(Integer idRuta) throws deleteException; // Entrada: ID de la ruta

    public ArrayList<Ruta> selectAll() throws selectException;

    public ArrayList<Ruta> filterByDates(Date firstDate, Date secondDate) throws selectException;

    public ArrayList<Ruta> filterTiempo(FilterTypeRuta filterType, Integer tiempo) throws selectException;

    public ArrayList<Ruta> filterDistancia(FilterTypeRuta filterType, Integer distancia) throws selectException;
}
