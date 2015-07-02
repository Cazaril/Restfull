/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eugenio
 */
@Entity
@Table(name = "CITAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citas.findAll", query = "SELECT c FROM Citas c"),
    @NamedQuery(name = "Citas.findById", query = "SELECT c FROM Citas c WHERE c.id = :id"),
    @NamedQuery(name = "Citas.findByDescripcion", query = "SELECT c FROM Citas c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Citas.findByFecha", query = "SELECT c FROM Citas c WHERE c.fecha = :fecha")})
public class Citas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "IDCALENDARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Calendarios idcalendario;

    public Citas() {
    }

    public Citas(Integer id) {
        this.id = id;
    }

    public Citas(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Calendarios getIdcalendario() {
        return idcalendario;
    }

    public void setIdcalendario(Calendarios idcalendario) {
        this.idcalendario = idcalendario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Citas)) {
            return false;
        }
        Citas other = (Citas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String string = "ID de la cita: " + this.getId();
        string += ". Descripcion de la cita: " + this.getDescripcion();
        string += ". Calendario al cual pertenece la cita: " + this.getIdcalendario().getNombre();
        string += ". Fecha de la cita: " + this.getFecha();
        return string;
    }
    
}
