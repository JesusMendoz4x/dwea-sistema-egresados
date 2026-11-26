package CDI;
import logneg.MEmpresa;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aEmpresa")
@ViewScoped
public class AEmpresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MEmpresa servicio;
    private Empresa actual = new Empresa();
    private List<Empresa> registros = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/administracion/empresas.xhtml", true)) return;

        try { registros = servicio.consultar();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/administracion/empresas.xhtml", true)) return;
 actual = new Empresa();
    }
    public void editar(Empresa dato) {
        if (!AccesoVista.permitir("/vistas/administracion/empresas.xhtml", true)) return;
 actual = new Empresa();
        actual.setIdEmpresa(dato.getIdEmpresa());
        actual.setNombre(dato.getNombre());

    }
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/administracion/empresas.xhtml", true)) return;

        try { servicio.guardar(actual); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Empresa getActual() { return actual; }
    public List<Empresa> getRegistros() { return registros; }
public void eliminar(Empresa dato){
        if (!AccesoVista.permitir("/vistas/administracion/empresas.xhtml", true)) return;
try{servicio.eliminar(dato.getIdEmpresa());nuevo();cargar();FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Registro eliminado."));}catch(Exception ex){error(ex);}}
}
