package logicInterface;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import java.util.List;
import models.Ruta;

public interface RutaManager {

    public <T> T filterDistanciaMenor_XML(Class<T> responseType, String distancia) throws WebApplicationException;

    public <T> T filterDistanciaMenor_JSON(Class<T> responseType, String distancia) throws WebApplicationException;

    public <T> T filterDistanciaIgual_XML(Class<T> responseType, String distancia) throws WebApplicationException;

    public <T> T filterDistanciaIgual_JSON(Class<T> responseType, String distancia) throws WebApplicationException;

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    public <T> T filterTiempoMayor_XML(Class<T> responseType, String tiempo) throws WebApplicationException;

    public <T> T filterTiempoMayor_JSON(Class<T> responseType, String tiempo) throws WebApplicationException;

    public <T> T filterTiempoIgual_XML(Class<T> responseType, String tiempo) throws WebApplicationException;

    public <T> T filterTiempoIgual_JSON(Class<T> responseType, String tiempo) throws WebApplicationException;

    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    public void remove(String id) throws WebApplicationException;

    public <T> T filterDistanciaMayor_XML(Class<T> responseType, String distancia) throws WebApplicationException;

    public <T> T filterDistanciaMayor_JSON(Class<T> responseType, String distancia) throws WebApplicationException;

    public String countREST() throws WebApplicationException;

    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    public void create_XML(Object requestEntity) throws WebApplicationException;

    public void create_JSON(Object requestEntity) throws WebApplicationException;

    public <T> T filterBy2Dates_XML(Class<T> responseType, String firstDate, String secondDate) throws WebApplicationException;

    public <T> T filterBy2Dates_JSON(Class<T> responseType, String firstDate, String secondDate) throws WebApplicationException;

    public <T> T filterTiempoMenor_XML(Class<T> responseType, String tiempo) throws WebApplicationException;

    public <T> T filterTiempoMenor_JSON(Class<T> responseType, String tiempo) throws WebApplicationException;

    public void close();

}
