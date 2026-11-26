package CDI;
import logneg.MNotificacion;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aNotificacion")
@ViewScoped
public class ANotificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MNotificacion servicio;
    private Notificacion actual = new Notificacion();
    private List<Notificacion> registros = Collections.emptyList();
    private Integer idUsuario;
    private List<Usuario> opcionesUsuario = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/avisos.xhtml", false)) return;

        try { registros = servicio.consultar();
            opcionesUsuario = servicio.consultarUsuario();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/comunicacion/avisos.xhtml", false)) return;
 actual = new Notificacion();
        idUsuario = null;
    }
    public void editar(Notificacion dato) {
        if (!AccesoVista.permitir("/vistas/comunicacion/avisos.xhtml", false)) return;
 actual = new Notificacion();
        actual.setIdNotificacion(dato.getIdNotificacion());
        actual.setTitulo(dato.getTitulo());
        actual.setTipo(dato.getTipo());
        actual.setCuerpo(dato.getCuerpo());
        actual.setEnlaceDirecto(dato.getEnlaceDirecto());
        actual.setFechaEmision(dato.getFechaEmision());
        actual.setIdUsuario(dato.getIdUsuario());

        idUsuario = dato.getIdUsuario() == null ? null : dato.getIdUsuario().getIdUsuario();
    }
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/avisos.xhtml", false)) return;

        try { servicio.guardar(actual, idUsuario); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Notificacion getActual() { return actual; }
    public List<Notificacion> getRegistros() { return registros; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer value) { idUsuario = value; }
    public List<Usuario> getOpcionesUsuario() { return opcionesUsuario; }
    public void eliminar(Notificacion dato) {
        if (!AccesoVista.permitir("/vistas/comunicacion/avisos.xhtml", false)) return;

        try { servicio.eliminar(dato.getIdNotificacion()); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro eliminado."));
        } catch (Exception ex) { error(ex); }
    }
}
