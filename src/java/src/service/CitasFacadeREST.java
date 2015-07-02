/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import src.Calendarios;
import src.Citas;
import src.Usuarios;

/**
 *
 * @author Eugenio
 */
@Stateless
@Path("citas")
public class CitasFacadeREST extends AbstractFacade<Citas> {

    @PersistenceContext(unitName = "RestfullPU")
    private EntityManager em;
    final String URI = "http://localhost:8080/Restfullservice/webresources";

    public CitasFacadeREST() {
        super(Citas.class);
    }

    @POST
    //@Path("{UserID}/{CalID}/cita")
    @Consumes({"application/xml", "application/json"})
    public Response createCita(Citas entity) {
        Response resp;
        Query cuerito = em.createQuery("SELECT c FROM Citas c WHERE c.id = :idcita");
        cuerito.setParameter("idcita", entity.getId());
        if (!cuerito.getResultList().isEmpty()) {
            resp = Response.status(Response.Status.CONFLICT).header("Location",
                    URI + "/citas/" + entity.getIdcalendario().getPropietario().getId() + "/" + entity.getId()).build();
        } else {
            super.create(entity);
            cuerito = em.createQuery("SELECT c FROM Citas c");
            entity = (Citas) cuerito.getResultList().get(cuerito.getResultList().size() - 1);
            resp = Response.status(Response.Status.CREATED).header("Location",
                    URI + "/citas/" + entity.getIdcalendario().getPropietario().getId() + "/" + entity.getId()).build();
        }
        return resp;
    }

    @PUT
    @Path("{IDUsuario}/{IDCita}")
    @Consumes({"application/xml", "application/json"})
    public Response edit(Citas entity, @PathParam("IDUsuario") Integer IDUsuario, @PathParam("IDCita") Integer IDCita) {
        Response resp;
        Query cuerito = em.createQuery("SELECT c FROM Citas c JOIN c.idcalendario a WHERE c.id = :idcita AND a.propietario.id = :idpropietario");
        cuerito.setParameter("idcita", IDCita).setParameter("idpropietario", IDUsuario);
        try {
            cuerito.getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Cita no encontrada"), 404);
        }
        resp = Response.status(Response.Status.OK).build();
        super.edit(entity);
        return resp;
    }

    @DELETE
    @Path("{IDUsuario}/{IDCita}")
    public Response remove(@PathParam("IDUsuario") Integer IDUsuario, @PathParam("IDCita") Integer IDCita) {
        Response resp;

        Query cuerito = em.createQuery("SELECT c FROM Citas c JOIN c.idcalendario a WHERE c.id = :idcita AND a.propietario.id = :idpropietario");
        cuerito.setParameter("idcita", IDCita).setParameter("idpropietario", IDUsuario);
        try {
            cuerito.getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Cita no encontrada"), 404);
        }
        resp = Response.status(Response.Status.OK).build();
        super.remove(super.find(IDCita));
        return resp;
    }

    @GET
    @Path("{IDUsuario}/{IDCalendario}")
    @Produces({"application/xml", "application/json"})
    public List<Citas> listaCitasCalendario(@PathParam("IDUsuario") Integer IDUsuario, @PathParam("IDCalendario") Integer IDCalendario) {
        //Response resp;
        Query cuerito = em.createQuery("SELECT c FROM Calendarios c JOIN c.propietario s WHERE c.id = :id AND s.id = :propietario");
        cuerito.setParameter("id", IDCalendario).setParameter("propietario", IDUsuario);
        try {
            cuerito.getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("El calendario no pertenece al usuario indicado"), 404);
        }
        Query query = em.createQuery("SELECT u FROM Citas u WHERE u.idcalendario = :idcalendario");
        query.setParameter("idcalendario", new Calendarios(IDCalendario));
        List<Citas> listaCitas = query.getResultList();
        //resp = Response.ok(listaCitas).build();
        return listaCitas;
    }

    @GET
    @Path("{IDUsuario}")
    @Produces({"application/xml", "application/json"})
    public List<Citas> findAllCitasUsuario(@PathParam("IDUsuario") Integer IDUsuario,
            @QueryParam("IDCalendario") @DefaultValue("-1") Integer IDCalendario,
            @QueryParam("desde") @DefaultValue("") String desde,
            @QueryParam("hasta") @DefaultValue("") String hasta,
            @QueryParam("maxEntradas") @DefaultValue("-1") Integer maxEntradas) {

        Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
        try {
            //Usuario existe? Si no existe, 404
            Object o = query.setParameter("id", IDUsuario).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Usuario no encontrado"), 404);
        }
        //Y sí, sigo repitiendo código. Por qué, Podríais preguntaros. ¿Qué por qué? Muy sencillo: Porque puedo.
        // Bárcenas roba, los profesores de la FI, en muchos casos se tocan los cojones de una forma que avergonzaría
        // a un Dios griego en plena post-orgía....Y esta es mi forma de protesta. Algo ridícula e inocente...
        // Pero es mi código.

        Date fechaDesde = null;
        Date fechaHasta = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fechaDesde = df.parse(desde);
        } catch (ParseException e) {
        }
        try {
            fechaHasta = df.parse(hasta);
        } catch (ParseException e) {
        }

        System.out.println("POTORRO");
        System.out.println("POTORRO");
        System.out.println("POTORRO");
        System.out.println("Conversión: " + fechaDesde + ". Recibido:" + desde);
        System.out.println("Conversión: " + fechaHasta + ". Recibido:" + hasta);
        System.out.println("POTORRO");
        System.out.println("POTORRO");
        System.out.println("POTORRO");
        String textQuery = "SELECT c FROM Citas c JOIN c.idcalendario a WHERE a.propietario = :propietario";

        if (fechaDesde != null) {
            textQuery += " AND (c.fecha >= :desde)";
        }

        if (fechaHasta != null) {
            textQuery += " AND (c.fecha <= :hasta)";
        }
        if (IDCalendario > 0) {
            textQuery += " AND c.idcalendario = :idcalendario";
        }

        Usuarios usuario = (Usuarios) query.setParameter("id", IDUsuario).getSingleResult();
        Query cuerito = em.createQuery(textQuery).setParameter("propietario", usuario);
        if (IDCalendario > 0) {
            cuerito.setParameter("idcalendario", IDCalendario);
        }

        if (fechaDesde != null) {
            cuerito.setParameter("desde", fechaDesde);
        }

        if (fechaHasta != null) {
            cuerito.setParameter("hasta", fechaHasta);
        }

        if (maxEntradas > 0) {
            cuerito.setMaxResults(maxEntradas);
        }
        List<Citas> citas = cuerito.getResultList();

        return citas;
    }

//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<Citas> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Path("count")
//    @Produces("text/plain")
//    public String countREST() {
//        return String.valueOf(super.count());
//    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
