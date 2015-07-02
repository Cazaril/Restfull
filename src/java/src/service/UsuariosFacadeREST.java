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
import src.Usuarios;

/**
 *
 * @author Eugenio
 */
@Stateless
@Path("usuarios")
public class UsuariosFacadeREST extends AbstractFacade<Usuarios> {
    @PersistenceContext(unitName = "RestfullPU")
    private EntityManager em;
final String URI = "http://localhost:8080/Restfull/webresources";

    public UsuariosFacadeREST() {
        super(Usuarios.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response createUser(Usuarios entity) {
        //SI EXISTE EL VALOR, NO LO CREA.
        Response resp;
        //System.out.println("POTORRO");
        //System.out.println(entity.getId());
        //System.out.println("POTORRO");
        super.create(entity);
        Query cuerito = em.createQuery("SELECT u FROM Usuarios u");
        entity = (Usuarios) cuerito.getResultList().get(cuerito.getResultList().size() - 1);

        resp = Response.status(Response.Status.CREATED).header("Location",
                URI + "/usuarios/" + entity.getId()).build();

        return resp;
    }

    @DELETE
    @Path("{UserID}")
    public Response remove(@PathParam("UserID") Integer UserID) {
        Response resp;
        if (super.find(UserID) != null) {
            resp = Response.status(Response.Status.OK).build();
            super.remove(super.find(UserID));
        }else{
            resp = Response.status(Response.Status.NOT_FOUND).build();
        }
        return resp;
    }

    @GET
    @Path("{UserID}")
    @Produces({"application/xml", "application/json"})
    public Usuarios find(@PathParam("UserID") Integer UserID) {
        try {
            //Usuario existe? Si no existe, 404
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.id = :id");
            Object o = query.setParameter("id", UserID).getSingleResult();
        } catch (NoResultException error) {
            throw new WebApplicationException(new Throwable("Usuario no encontrado"), 404);
        }
        return super.find(UserID);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Usuarios> findAllUsuarios() {
        List<Usuarios> potorro = super.findAll();
        return potorro;
    }

//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<Usuarios> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
