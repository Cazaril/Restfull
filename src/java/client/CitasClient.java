/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:CitasFacadeREST [citas]<br>
 * USAGE:
 * <pre>
 *        CitasClient client = new CitasClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Eugenio
 */
public class CitasClient {
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Restfull/webresources";

    public CitasClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("citas");
    }

    public <T> T findAllCitasUsuario_XML(GenericType<T> responseType, String IDUsuario, String hasta, String desde, String IDCalendario, String maxEntradas) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (hasta != null) {
            resource = resource.queryParam("hasta", hasta);
        }
        if (desde != null) {
            resource = resource.queryParam("desde", desde);
        }
        if (IDCalendario != null) {
            resource = resource.queryParam("IDCalendario", IDCalendario);
        }
        if (maxEntradas != null) {
            resource = resource.queryParam("maxEntradas", maxEntradas);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{IDUsuario}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAllCitasUsuario_JSON(GenericType<T> responseType, String IDUsuario, String hasta, String desde, String IDCalendario, String maxEntradas) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (hasta != null) {
            resource = resource.queryParam("hasta", hasta);
        }
        if (desde != null) {
            resource = resource.queryParam("desde", desde);
        }
        if (IDCalendario != null) {
            resource = resource.queryParam("IDCalendario", IDCalendario);
        }
        if (maxEntradas != null) {
            resource = resource.queryParam("maxEntradas", maxEntradas);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{IDUsuario}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response edit_XML(Object requestEntity, String IDUsuario, String IDCita) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{IDUsuario, IDCita})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Response.class);
    }

    public Response edit_JSON(Object requestEntity, String IDUsuario, String IDCita) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{IDUsuario, IDCita})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public Response createCita_XML(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Response.class);
    }

    public Response createCita_JSON(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public <T> T listaCitasCalendario_XML(GenericType<T> responseType, String IDUsuario, String IDCalendario) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{IDUsuario, IDCalendario}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T listaCitasCalendario_JSON(GenericType<T> responseType, String IDUsuario, String IDCalendario) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{IDUsuario, IDCalendario}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response remove(String IDUsuario, String IDCita) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{IDUsuario, IDCita})).request().delete(Response.class);
    }

    public void close() {
        client.close();
    }
    
}
