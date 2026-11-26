package CDI;
import java.util.*;import javax.ejb.EJB;import javax.inject.Named;import javax.enterprise.context.RequestScoped;import logneg.MCatalogo;
@Named("aCatalogos") @RequestScoped public class ACatalogos {
 @EJB private MCatalogo servicio;
 public List<String> getRoles(){return servicio.consultar("roles");}
 public List<String> getEstadosUsuario(){return servicio.consultar("estadosUsuario");}
 public List<String> getCarreras(){return servicio.consultar("carreras");}
 public List<String> getTiposEvento(){return servicio.consultar("tiposEvento");}
 public List<String> getEstadosCredencial(){return servicio.consultar("estadosCredencial");}
}
