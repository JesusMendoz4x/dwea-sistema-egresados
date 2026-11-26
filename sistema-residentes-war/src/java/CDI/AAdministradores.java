package CDI;

import dao.AdministradorFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import logneg.MAdministrador;
import modelo.Administrador;

@Named(value = "aAdministradores")
@Dependent
public class AAdministradores {

    @EJB
    private MAdministrador madministrador;
    private List<Administrador> administradores;

    public List<Administrador> administradores() {
        administradores = madministrador.getAdministrador();
        return administradores;
    }

    public AAdministradores() {
    }
}
