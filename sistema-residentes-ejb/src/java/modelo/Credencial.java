package modelo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
@Entity
@Table(name="Credencial")
public class Credencial implements Serializable {
 private static final long serialVersionUID=1L;
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer idCredencial;
 @NotNull @ManyToOne(optional=false) @JoinColumn(name="idEgresado",nullable=false,unique=true) private Egresado egresado;
 @NotNull @Size(max=60) private String estado;
 @Temporal(TemporalType.DATE) private Date fechaSolicitud;
 @Temporal(TemporalType.DATE) private Date fechaEmision;
 @Column(unique=true,length=60) private String folio;
 @Size(max=1000) private String observaciones;
 public Integer getIdCredencial(){return idCredencial;}
 public void setIdCredencial(Integer value){idCredencial=value;}
 public Egresado getEgresado(){return egresado;}
 public void setEgresado(Egresado value){egresado=value;}
 public String getEstado(){return estado;}
 public void setEstado(String value){estado=value;}
 public Date getFechaSolicitud(){return fechaSolicitud;}
 public void setFechaSolicitud(Date value){fechaSolicitud=value;}
 public Date getFechaEmision(){return fechaEmision;}
 public void setFechaEmision(Date value){fechaEmision=value;}
 public String getFolio(){return folio;}
 public void setFolio(String value){folio=value;}
 public String getObservaciones(){return observaciones;}
 public void setObservaciones(String value){observaciones=value;}
}
