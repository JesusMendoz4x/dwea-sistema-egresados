package CDI;
import javax.ejb.EJB;import javax.inject.Named;import javax.enterprise.context.RequestScoped;import modelo.Credencial;import logneg.MCredencial;
@Named("aImpresionCredencial") @RequestScoped public class AImpresionCredencial {
 @EJB private MCredencial servicio;private Integer id;private Credencial credencial;
 public void cargar(){credencial=servicio.buscar(id);if(credencial!=null&&credencial.getFolio()==null)credencial=null;}
 public Integer getId(){return id;}public void setId(Integer v){id=v;}public Credencial getCredencial(){return credencial;}
}
