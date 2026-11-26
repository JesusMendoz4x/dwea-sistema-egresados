package CDI;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Usuario;

/** Restringe las operaciones de cada controlador a su página y rol. */
public final class AccesoVista {
    private AccesoVista() { }

    static boolean esPermitido(String vista, String ruta, Usuario usuario, boolean soloAdministrador) {
        if (usuario == null || !ruta.equals(vista)) return false;
        return "Administrador".equals(usuario.getRol())
                || (!soloAdministrador && "Vinculación".equals(usuario.getRol()));
    }

    public static boolean permitir(String ruta, boolean soloAdministrador) {
        FacesContext contexto = FacesContext.getCurrentInstance();
        if (contexto == null || contexto.getViewRoot() == null) return false;
        Usuario usuario = (Usuario) contexto.getExternalContext().getSessionMap().get("usuarioPanel");
        boolean permitido = esPermitido(contexto.getViewRoot().getViewId(), ruta, usuario, soloAdministrador);
        if (!permitido) {
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Esta operación no está disponible en esta página o para tu rol.", null));
        }
        return permitido;
    }
}
