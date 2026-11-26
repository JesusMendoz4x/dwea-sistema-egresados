package CDI;
import logneg.MEvento;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aEvento")
@ViewScoped
public class AEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MEvento servicio;
    private Evento actual = new Evento();
    private List<Evento> registros = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/eventos.xhtml", false)) return;

        try { registros = servicio.consultar();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/comunicacion/eventos.xhtml", false)) return;
 actual = new Evento();
    }
    public void editar(Evento dato){
        if (!AccesoVista.permitir("/vistas/comunicacion/eventos.xhtml", false)) return;
actual=new Evento();actual.setIdEvento(dato.getIdEvento());
actual.setTitulo(dato.getTitulo());
actual.setTipo(dato.getTipo());
actual.setFecha(dato.getFecha());
actual.setSede(dato.getSede());
actual.setDescripcion(dato.getDescripcion());
}
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/eventos.xhtml", false)) return;

        try { servicio.guardar(actual); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Evento getActual() { return actual; }
    public List<Evento> getRegistros() { return registros; }
public void eliminar(Evento dato){
        if (!AccesoVista.permitir("/vistas/comunicacion/eventos.xhtml", false)) return;
try{servicio.eliminar(dato.getIdEvento());nuevo();cargar();FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Registro eliminado."));}catch(Exception ex){error(ex);}}
}
