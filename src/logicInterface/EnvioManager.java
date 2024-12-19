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

import java.util.Date;

/**
 *
 * @author Alder
 */
public interface EnvioManager {
    public void addEnvio() throws CreateException;//El tipo de entrada será un objeto envio
    public void updateEnvio() throws UpdateException;//El tipo de entrada será un objeto envio y el de salida tambien
    public void deleteEnvio(Integer id) throws DeleteException;//El tipo de salida sera un objeto Envio
    public void selectAll() throws SelectException;//salida array envios
    public void filterByDates(Date firstDate,Date secondDate) throws SelectException;//salida array envios
    public void filterEstado() throws SelectException;//Parametro entrada será un tipo del enum y salida un array de envios
    public void filterNumPaquetes(Integer numPaquetes) throws SelectException;//Salida array envios
}
