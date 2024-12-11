/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterface;

import exception.createException;
import exception.deleteException;
import exception.selectException;
import exception.updateException;
import java.time.LocalDate;
import java.util.List;
import models.Paquete;
import models.PaqueteSize;

/**
 *
 * @author Omar
 */
public interface PaqueteManager {
    
    public void addPaquete(Paquete paquete) throws createException;
    public void updatePaquete(Integer idPaquete) throws updateException;
    public void deletePaquete(Integer idPaquete) throws deleteException;
    
    public List<Paquete> findAllPaquetes() throws selectException;
    public List<Paquete> findAllPaquetesBySize(PaqueteSize size) throws selectException;
    public List<Paquete> findAllPaquetesEntreDates(LocalDate firstDate,LocalDate secondDate) throws selectException;
    public List<Paquete> findAllPaquetesByName(String senderOrReciverName) throws selectException;

}
    
    

