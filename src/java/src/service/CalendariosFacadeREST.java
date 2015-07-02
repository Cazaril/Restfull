/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import src.Calendarios;
import src.Usuarios;

/**
 *
 * @author Eugenio
 */
@Stateless
@Path("calendarios")
public class CalendariosFacadeREST extends AbstractFacade<Calendarios> {
    @PersistenceContext(unitName = "RestfullPU")
    private EntityManager em;
final String URI = "http://localhost:8080/Restfullservice/webresources";

    public CalendariosFacadeREST() {
        super(Calendarios.class);
    }

    @POST
    //@Path("{UserID}")
    @Consumes({"application/xml", "application/json"})
    public Response createCalendario(/*@PathParam("UserID") int UserID,*/Calendarios entity) {
        Response resp;
        try {
            //Usuario existe? Si no existe, 404
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
            Object u = query.setParameter("id", entity.getPropietario().getId()).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Usuario no encontrado"), 404);
        }
        //Revisamos si el usuario X ya tenía un calendario Y, no aceptamos 2 calendarios iguales para el mismo propietario
        Query cuerito = em.createQuery("SELECT c FROM Calendarios c WHERE c.nombre = :nombre AND c.propietario = :propietario");
        cuerito.setParameter("nombre", entity.getNombre());
        cuerito.setParameter("propietario", entity.getPropietario());
        if (!cuerito.getResultList().isEmpty()) {
            resp = Response.status(Response.Status.CONFLICT).header("Location",
                    URI + "/calendarios/" + entity.getPropietario().getId() + "/" + entity.getId()).build();
        } else {
            /*
             Para los calendarios queda algo así como
             BASE/calendarios/IDDueñoDelCalendario/IDCalendario
             De forma que los calendarios están organizados por usuarios.
             */
            super.create(entity);
            
            cuerito = em.createQuery("SELECT c FROM Calendarios c");
            entity = (Calendarios) cuerito.getResultList().get(cuerito.getResultList().size() - 1);
                    
            resp = Response.status(Response.Status.CREATED).header("Location",
                    URI + "/calendarios/" + entity.getPropietario().getId() + "/" + entity.getId()).build();

        }
        return resp;
    }

    @PUT
    @Path("{UserID}/{CalID}")
    @Consumes({"application/xml", "application/json"})
    public Response edit(@PathParam("CalID") Integer CalID, Calendarios entity,@PathParam("UserID") int UserID) {
        Response resp;
        try {
            //Usuario existe? Si no existe, 404
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
            Object o = query.setParameter("id", UserID).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Usuario no encontrado"), 404);
        }
        
        //El calendario no existe? Si no existe, 404
        Calendarios cal = super.find(CalID);
        if (cal == null) {
            throw new WebApplicationException(new Throwable("Calendario no encontrado"), 404);
        }
        if (cal.getPropietario().getId() != UserID) {
            throw new WebApplicationException(new Throwable("Calendario no pertenece al usuario indicado"), 409);
        }
        //A estas alturas del codigo me estoy planteando seriamente hacer un metodo o dos
        //que haga todas estas comprobaciones y no tener que estar repitiendo tanto código
        // Pero me da mucha pereza, si alguien ve este código... Perdóname, es mi primerito día :D
        super.edit(entity);
        resp = Response.status(Response.Status.ACCEPTED).build();
        return resp;
    }

    @DELETE
    @Path("{UserID}/{CalID}")
    public Response remove(@PathParam("UserID") Integer UserID, @PathParam("CalID") Integer CalID) {
        Response resp;
        Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
        try {
            //Usuario existe? Si no existe, 404
            Object o = query.setParameter("id", UserID).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("usuario no encontrado"), 404);
        }
        Usuarios propietario = (Usuarios) query.setParameter("id", UserID).getSingleResult();
        
        try {
            //Calendario existe? Si no existe, 404
            Query cuerito = em.createQuery("SELECT c FROM Calendarios c WHERE c.id = :idcalendario AND c.propietario = :propietario");
            Object o = cuerito.setParameter("idcalendario", CalID).setParameter("propietario", propietario).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Calendario no encontrado"), 404);
        }
        resp = Response.status(Response.Status.OK).build();
        super.remove(super.find(UserID));
        return resp;
    }

    @GET
    @Path("{UserID}/{CalID}")
    @Produces({"application/xml", "application/json"})
    public Calendarios find(@PathParam("CalID") Integer CalID, @PathParam("UserID") int UserID) {
        Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
        try {
            //Usuario existe? Si no existe, 404
            Object o = query.setParameter("id", UserID).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("usuario no encontrado"), 404);
        }
        Usuarios propietario = (Usuarios) query.setParameter("id", UserID).getSingleResult();
        
        try {
            //Usuario existe? Si no existe, 404
            Query cuerito = em.createQuery("SELECT c FROM Calendarios c WHERE c.id = :idcalendario AND c.propietario = :propietario");
            Object o = cuerito.setParameter("idcalendario", CalID).setParameter("propietario", propietario).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Calendario no encontrado"), 404);
        }
        return super.find(CalID);
    }

    @GET
    @Path("{UserID}")
    @Produces({"application/xml", "application/json"})
    public List<Calendarios> findAllCalendarios(@PathParam("UserID") int UserID) {
        Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
        try {
            //Usuario existe? Si no existe, 404
            Object o = query.setParameter("id", UserID).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("usuario no encontrado"), 404);
        }
        Usuarios propietario = (Usuarios) query.setParameter("id", UserID).getSingleResult();
        Query cuerito = em.createQuery("SELECT c FROM Calendarios c WHERE c.propietario = :propietario AND c.publico = :publico");
        cuerito.setParameter("propietario", propietario);
        cuerito.setParameter("publico", true);
        List<Calendarios> potorro = cuerito.getResultList();
        return potorro;
    }

//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<Calendarios> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
