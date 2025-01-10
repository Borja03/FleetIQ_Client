package service;

import models.Envio;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cliente REST para interactuar con el servicio Envio utilizando XML.
 */
public class EnvioRESTClient {

    private static final String BASE_URL = "http://localhost:8080/FleetIQ_Server/rest/envio";

    public List<Envio> selectAll() throws IOException, JAXBException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }

        InputStream xmlResponse = connection.getInputStream();
        JAXBContext jaxbContext = JAXBContext.newInstance(EnvioListWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        EnvioListWrapper wrapper = (EnvioListWrapper) unmarshaller.unmarshal(xmlResponse);
        connection.disconnect();
        return wrapper.getEnvios();
    }

    public Envio getEnvioById(int id) throws IOException, JAXBException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }

        InputStream xmlResponse = connection.getInputStream();
        JAXBContext jaxbContext = JAXBContext.newInstance(Envio.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Envio envio = (Envio) unmarshaller.unmarshal(xmlResponse);
        connection.disconnect();
        return envio;
    }

    public void addEnvio(Envio envio) throws IOException, JAXBException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(Envio.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(envio, connection.getOutputStream());

        if (connection.getResponseCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }
        connection.disconnect();
    }

    public void updateEnvio(int id, Envio envio) throws IOException, JAXBException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(Envio.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(envio, connection.getOutputStream());

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }
        connection.disconnect();
    }

    public void deleteEnvio(int id) throws IOException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }
        connection.disconnect();
    }

    // Wrapper para manejar listas de envíos en XML
    public static class EnvioListWrapper {

        private List<Envio> envios;

        public List<Envio> getEnvios() {
            return envios;
        }

        public void setEnvios(List<Envio> envios) {
            this.envios = envios;
        }
    }

    // Filtrar por fecha
    public List<Envio> filterByDates(List<Envio> envios, String startDate, String endDate) {
        return envios.stream()
            .filter((Envio envio) -> envio.getFechaEnvio().toString().compareTo(startDate) >= 0 && envio.getFechaEntrega().toString().compareTo(endDate) <= 0)
            .collect(Collectors.toList());
    }

    // Filtrar por estado
    public List<Envio> filterEstado(List<Envio> envios, String estado) {
        return envios.stream()
            .filter(envio -> envio.getEstado().equals(estado))
            .collect(Collectors.toList());
    }

    // Filtrar por número de paquetes
    public List<Envio> filterNumPaquetes(List<Envio> envios, int numPaquetes) {
        return envios.stream()
            .filter(envio -> envio.getNumPaquetes() == numPaquetes)
            .collect(Collectors.toList());
    }
}
