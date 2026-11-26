package CDI;
import logneg.MCredencial;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aCredencial")
@ViewScoped
public class ACredencial implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MCredencial servicio;
    private Credencial actual = new Credencial();
    private List<Credencial> registros = Collections.emptyList();
    private Integer egresado;
    private List<Egresado> opcionesEgresado=Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/credenciales/index.xhtml", false)) return;

        if(actual.getEstado()==null)actual.setEstado("Pendiente");
        try { registros = servicio.consultar(); opcionesEgresado=servicio.consultarEgresado();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/credenciales/index.xhtml", false)) return;
 actual = new Credencial(); actual.setEstado("Pendiente");egresado=null;
    }
    public void editar(Credencial dato){
        if (!AccesoVista.permitir("/vistas/credenciales/index.xhtml", false)) return;
actual=new Credencial();actual.setIdCredencial(dato.getIdCredencial());
actual.setEgresado(dato.getEgresado());
actual.setEstado(dato.getEstado());
actual.setObservaciones(dato.getObservaciones());
actual.setFechaSolicitud(dato.getFechaSolicitud());
actual.setFechaEmision(dato.getFechaEmision());
actual.setFolio(dato.getFolio());
egresado=dato.getEgresado().getIdEgresado();}
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/credenciales/index.xhtml", false)) return;

        try { servicio.guardar(actual,egresado); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Credencial getActual() { return actual; }
    public List<Credencial> getRegistros() { return registros; }
public Integer getEgresado(){return egresado;}public void setEgresado(Integer id){egresado=id;}
public List<Egresado> getOpcionesEgresado(){return opcionesEgresado;}
public String imprimir(Credencial dato){if(dato.getFolio()==null){FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Primero emite la credencial.",null));return null;}return "/vistas/credenciales/imprimir?faces-redirect=true&id="+dato.getIdCredencial();}
public void eliminar(Credencial dato){
        if (!AccesoVista.permitir("/vistas/credenciales/index.xhtml", false)) return;
try{servicio.eliminar(dato.getIdCredencial());nuevo();cargar();FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Registro eliminado."));}catch(Exception ex){error(ex);}}
}
