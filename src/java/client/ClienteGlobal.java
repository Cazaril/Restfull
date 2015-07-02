/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericType;




/**
 *
 * @author Eugenio
 */
public class ClienteGlobal {
    
    
    public static void main(String[] args) {
        Citas cita = new Citas();
        Usuarios usuario = new Usuarios();
        Calendarios calendario = new Calendarios();
        boolean fallado = false;
        
        UsuariosClient usuarioCliente = new UsuariosClient();
        usuario.setNombre("Perico de los Palotes");
        usuario.setPass(0147);
        Response respuesta = usuarioCliente.createUser_XML(usuario);
        System.out.println(respuesta.getStatus());
        if(respuesta.getStatus() == Response.Status.CREATED.getStatusCode()){
            System.out.println("Usuario creado con URI " + respuesta.getHeaders().getFirst("Location"));
            System.out.println("");
        }else{
            fallado = true;
            System.out.println("Error al crear el usuario");
        }
        usuarioCliente.close();
        if (fallado) {
            return;
        }
        
        CalendariosClient calendarioCliente =  new CalendariosClient();
        calendario.setNombre("CalendarioPrueba");
        calendario.setPropietario(new Usuarios(3));
        calendario.setPublico(true);
        Response respuesta1 = calendarioCliente.createCalendario_XML(calendario);
        System.out.println(respuesta1.getStatus());
        if (respuesta1.getStatus() == Response.Status.CREATED.getStatusCode()) {
            System.out.println("Calendario creado con URI " + respuesta1.getHeaders().getFirst("Location"));
            System.out.println("");
        } else {
            fallado = true;
            System.out.println("Error al crear el Calendario");
        }
        calendarioCliente.close();
        if (fallado) {
            return;
        }

        
        CitasClient citaCliente = new CitasClient();
        Date fecha = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fecha = df.parse("1959-07-17");
        } catch (ParseException ex) {
            Logger.getLogger(ClienteGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendarios cal = new Calendarios(4);
        cal.setPropietario(new Usuarios(3));
        cita.setIdcalendario(cal);
        cita.setFecha(fecha);
        cita.setDescripcion("Prueba de inserción de una cita");
        Response respuesta2 = citaCliente.createCita_XML(cita);
        System.out.println(respuesta2.getStatus());
        if (respuesta2.getStatus() == Response.Status.CREATED.getStatusCode()) {
            System.out.println("Cita creada con URI " + respuesta2.getHeaders().getFirst("Location"));
            System.out.println("");
        } else {
            System.out.println("Error al crear la Cita");
            fallado = true;
        }
        citaCliente.close();
        if (fallado) {
            return;
        }
        
        
        citaCliente = new CitasClient();
        try {
            fecha = df.parse("1957-12-10");
        } catch (ParseException ex) {
            Logger.getLogger(ClienteGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
        cita.setFecha(fecha);
        cita.setDescripcion("Prueba de modificacion de valores");
        Response respuesta3 = citaCliente.edit_XML(cita, "3", "5");
        System.out.println(respuesta3.getStatus());
        if (respuesta3.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Cita modificada correctamente");
            System.out.println("");
        } else {
            System.out.println("Error al modificar la cita");
            fallado = true;
        }
        citaCliente.close();
        if (fallado) {
            return;
        }
        
        
        citaCliente = new CitasClient();
        System.out.println("Prueba de listar todas las citas de un calendario dado");
        System.out.println("");
        GenericType<List<Citas>> gType = new GenericType<List<Citas>>() {};
                                                                     //IDUsuario, IDCalendario
        List<Citas> listaCitas = citaCliente.listaCitasCalendario_XML(gType, "2", "2");
        if (listaCitas.isEmpty()) {
            System.out.println("No existen citas en dicho calendario");
            System.out.println("");
        } else {
            System.out.println("Obtención de todas las citas de un calendario concreto");
            System.out.println("Dueño del calendario: ");
            System.out.println(listaCitas.get(0).getIdcalendario().getPropietario().getNombre());
            for (Citas cit : listaCitas) {
                System.out.println(cit.toString());
                System.out.println("");
            }
        }
        citaCliente.close();
        
        citaCliente = new CitasClient();
        System.out.println("Prueba de listar citas entre dos fechas dadas");
        System.out.println("");
        GenericType<List<Citas>> gtipo = new GenericType<List<Citas>>(){};
                                                       // IDUsuario, HASTA, DESDE, IDCalendario, MaxEntradas.
        listaCitas = citaCliente.findAllCitasUsuario_XML(gtipo, "1", "2015-07-07", "1991-06-27", null, null);
        if (listaCitas.isEmpty()) {
            System.out.println("No existen citas en dicho intervalo o con esos parámetros");
            System.out.println("");
        } else {
            System.out.println("Obtención de todas las citas entre dos fechas dadas");
            System.out.println("");
            System.out.println("Usuario propietario de dichas citas: " + listaCitas.get(0).getIdcalendario().getPropietario().getNombre());
            for (Citas citas : listaCitas) {
                System.out.println(citas.toString());
            }
        }
        citaCliente.close();
        
        System.out.println("Prueba de eliminar una cita");
        System.out.println("");
        citaCliente = new CitasClient();
                                   //IDUsuario, IDCita
        Response resp = citaCliente.remove("3", "5");
        if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Cita eliminada correctamente");
        }else{
            fallado = true;
            System.out.println("Error al eliminar la cita");
        }
        citaCliente.close();
        if (fallado) {
            return;
        }
        
        System.out.println("Prueba de eliminar un calendario");
        System.out.println("");
        calendarioCliente = new CalendariosClient();
        CitasClient citascliente = new CitasClient();
        GenericType<List<Citas>> gcit = new GenericType<List<Citas>>(){};
        List<Citas> listcit = citascliente.listaCitasCalendario_XML(gcit,"3", "4");
        citascliente.close();
        for (Citas cit : listcit) {
            citascliente = new CitasClient();
            citascliente.remove("3", "" + cit.getId());
            citascliente.close();
        }
                                    //IDUsuario, IDCalendario
        resp = calendarioCliente.remove("3", "4");
        if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Calendario eliminado correctamente");
            System.out.println("");
        }else{
            fallado = true;
            System.out.println("Error al eliminar el calendario");
        }
        calendarioCliente.close();
        if (fallado) {
            return;
        }
        
        System.out.println("Prueba de listar todos los usuarios");
        System.out.println("");
        usuarioCliente = new UsuariosClient();
        GenericType<List<Usuarios>> gtype = new GenericType<List<Usuarios>>(){};
        List<Usuarios> listaUsuarios = usuarioCliente.findAllUsuarios_XML(gtype);
        if (listaUsuarios.isEmpty()) {
            System.out.println("No existen usuarios en la Base de Datos");
        }else{
            for (Usuarios user : listaUsuarios) {
                System.out.println(user.toString());
                System.out.println("");
            }
        }
        usuarioCliente.close();
        
        
        calendarioCliente = new CalendariosClient();
        GenericType<List<Calendarios>> gcal = new GenericType<List<Calendarios>>(){};
        List<Calendarios> listaCalendarios = calendarioCliente.findAllCalendarios_XML(gcal, "1");
        if (listaCalendarios.isEmpty()) {
            System.out.println("El usuario indicado no posee calendarios");
        }else{
            calendarioCliente.close();
            citaCliente = new CitasClient();
            System.out.println("Citas pertenecientes a: " + listaCalendarios.get(0).getPropietario().getNombre());
            for (Calendarios calen : listaCalendarios) {
                System.out.println("");
                System.out.println("");
                listaCitas = citaCliente.listaCitasCalendario_XML(gType, "1","" + calen.getId());
                System.out.println("Citas pertenecientes al calendario: " + calen.getNombre());
                for (Citas cit : listaCitas) {
                    System.out.println(cit.toString());
                }
                System.out.println("");
                System.out.println("");
            }
        }
    }
}
