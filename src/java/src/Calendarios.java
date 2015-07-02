/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Eugenio
 */
@Entity
@Table(name = "CALENDARIOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendarios.findAll", query = "SELECT c FROM Calendarios c"),
    @NamedQuery(name = "Calendarios.findById", query = "SELECT c FROM Calendarios c WHERE c.id = :id"),
    @NamedQuery(name = "Calendarios.findByNombre", query = "SELECT c FROM Calendarios c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Calendarios.findByPublico", query = "SELECT c FROM Calendarios c WHERE c.publico = :publico")})
public class Calendarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "PUBLICO")
    private boolean publico;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcalendario")
    private Collection<Citas> citasCollection;
    @JoinColumn(name = "PROPIETARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuarios propietario;

    public Calendarios() {
    }

    public Calendarios(Integer id) {
        this.id = id;
    }

    public Calendarios(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    @XmlTransient
    public Collection<Citas> getCitasCollection() {
        return citasCollection;
    }

    public void setCitasCollection(Collection<Citas> citasCollection) {
        this.citasCollection = citasCollection;
    }

    public Usuarios getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuarios propietario) {
        this.propietario = propietario;
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
        if (!(object instanceof Calendarios)) {
            return false;
        }
        Calendarios other = (Calendarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String string = "ID del calendario: " + this.getId();
        string += ". Nombre del Calendario: " + this.getNombre();
        string += ". Propietario del Calendario: " + this.getPropietario();
        string += ". Calendario publico: " + this.getPublico();
        return string;
    }
    
}
