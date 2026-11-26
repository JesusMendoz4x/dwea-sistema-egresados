package modelo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
@Entity
@Table(name="Evento")
public class Evento implements Serializable {
 private static final long serialVersionUID=1L;
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer idEvento;
 @NotBlank @Size(max=255) private String titulo;
 @NotBlank @Size(max=255) private String tipo;
 @NotNull @Temporal(TemporalType.TIMESTAMP) private Date fecha;
 @NotBlank @Size(max=255) private String sede;
 @Size(max=1000) private String descripcion;
 public Integer getIdEvento(){return idEvento;}
 public void setIdEvento(Integer value){idEvento=value;}
 public String getTitulo(){return titulo;}
 public void setTitulo(String value){titulo=value;}
 public String getTipo(){return tipo;}
 public void setTipo(String value){tipo=value;}
 public Date getFecha(){return fecha;}
 public void setFecha(Date value){fecha=value;}
 public String getSede(){return sede;}
 public void setSede(String value){sede=value;}
 public String getDescripcion(){return descripcion;}
 public void setDescripcion(String value){descripcion=value;}
}
