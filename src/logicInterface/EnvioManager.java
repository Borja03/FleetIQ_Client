/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterface;

import exception.selectException;
import exception.deleteException;
import exception.updateException;
import exception.createException;
import java.util.Date;

/**
 *
 * @author Alder
 */
public interface EnvioManager {
    public void addEnvio() throws createException;//El tipo de entrada será un objeto envio
    public void updateEnvio() throws updateException;//El tipo de entrada será un objeto envio y el de salida tambien
    public void deleteEnvio(Integer id) throws deleteException;//El tipo de salida sera un objeto Envio
    public void selectAll() throws selectException;//salida array envios
    public void filterByDates(Date firstDate,Date secondDate) throws selectException;//salida array envios
    public void filterEstado() throws selectException;//Parametro entrada será un tipo del enum y salida un array de envios
    public void filterNumPaquetes(Integer numPaquetes) throws selectException;//Salida array envios
}
