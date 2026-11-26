package CDI;
import logneg.MEgresado;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aEgresado")
@ViewScoped
public class AEgresado implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MEgresado servicio;
    private Egresado actual = new Egresado();
    private List<Egresado> registros = Collections.emptyList();
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
        if (!AccesoVista.permitir("/vistas/egresados/gestionEgresados.xhtml", true)) return;

        try { registros = servicio.consultar();
            opcionesUsuario = servicio.consultarUsuario();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/egresados/gestionEgresados.xhtml", true)) return;
 actual = new Egresado();
        idUsuario = null;
    }
    public void editar(Egresado dato) {
        if (!AccesoVista.permitir("/vistas/egresados/gestionEgresados.xhtml", true)) return;
 actual = new Egresado();
        actual.setIdEgresado(dato.getIdEgresado());
        actual.setCarrera(dato.getCarrera());
        actual.setAñoEgreso(dato.getAñoEgreso());
        actual.setIdUsuario(dato.getIdUsuario());

        idUsuario = dato.getIdUsuario() == null ? null : dato.getIdUsuario().getIdUsuario();
    }
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/egresados/gestionEgresados.xhtml", true)) return;

        try { servicio.guardar(actual, idUsuario); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Egresado getActual() { return actual; }
    public List<Egresado> getRegistros() { return registros; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer value) { idUsuario = value; }
    public List<Usuario> getOpcionesUsuario() { return opcionesUsuario; }
    public void eliminar(Egresado dato) {
        if (!AccesoVista.permitir("/vistas/egresados/gestionEgresados.xhtml", true)) return;

        try { servicio.eliminar(dato.getIdEgresado()); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro eliminado."));
        } catch (Exception ex) { error(ex); }
    }
}
