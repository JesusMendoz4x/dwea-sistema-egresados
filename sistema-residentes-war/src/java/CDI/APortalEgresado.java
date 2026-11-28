package CDI;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import logneg.MVacante;
import modelo.Vacante;

@Named("aPortalEgresado")
@RequestScoped
public class APortalEgresado {
    @EJB
    private MVacante servicio;
    private List<Vacante> vacantes = Collections.emptyList();
    private boolean error;

    @PostConstruct
    public void cargar() {
        try {
            vacantes = servicio.consultarVigentesPublicadas();
        } catch (RuntimeException ex) {
            error = true;
            Logger.getLogger(getClass().getName()).log(Level.WARNING,
                    "No se pudieron consultar las vacantes vigentes", ex);
        }
    }

    public List<Vacante> getVacantes() {
        return vacantes;
    }

    public TimeZone getZonaHoraria() {
        return TimeZone.getDefault();
    }

    public boolean isError() {
        return error;
    }
}
