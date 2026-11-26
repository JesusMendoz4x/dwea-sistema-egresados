package CDI;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import logneg.MAdministrador;
import logneg.ResumenDashboard;

/** Controlador de presentación: no ejecuta consultas SQL ni utiliza EntityManager. */
@Named("aDashboard")
@RequestScoped
public class ADashboard {

    private static final Logger LOGGER = Logger.getLogger(ADashboard.class.getName());

    @EJB
    private MAdministrador administrador;

    private ResumenDashboard resumen;
    private String error;

    @PostConstruct
    public void cargar() {
        resumen = null;
        error = null;
        try {
            resumen = administrador.consultarResumen();
        } catch (EJBException ex) {
            LOGGER.log(Level.SEVERE, "No se pudo consultar el dashboard mediante el EJB", ex);
            error = "No se pudo cargar el resumen. Intenta recargar la página; "
                    + "si el problema continúa, contacta al administrador del sistema.";
        }
    }

    public ResumenDashboard getResumen() { return resumen; }
    public String getError() { return error; }
    public boolean isDatosDisponibles() { return resumen != null; }
}
