package logneg;
import dao.*;
import modelo.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MVacante {
    public static final ZoneId ZONA_VIGENCIA = ZoneId.of("America/Mexico_City");
    public static final String MENSAJE_VIGENCIA = "La vigencia debe ser posterior a la fecha de hoy.";
    @EJB private VacanteFacade dao;
    @EJB private PublicacionVacanteFacade publicaciones;
    @EJB private EmpresaFacade daoEmpresa;
    @EJB private UsuarioFacade daoUsuario;
    public List<Vacante> consultar() { return dao.findAll(); }
    public List<Vacante> consultarVigentesPublicadas() {
        return dao.consultarVigentesPublicadas();
    }
    public List<Empresa> consultarEmpresa() { return daoEmpresa.findAll(); }
    public List<String> sugerirPuestos() { return dao.sugerirPuestos(); }
    public List<Usuario> consultarUsuario() { return daoUsuario.findAll(); }
    public void guardar(Vacante dato, Integer idEmpresa, Integer idUsuario) {
        validarTexto("Puesto", dato.getPuesto(), 255, true);
        validarTexto("Descripción", dato.getDescripcion(), 1000, true);
        validarTexto("Ubicación", dato.getUbicacion(), 255, true);
        validarTexto("Requisitos", dato.getRequisitos(), 255, dato.getIdVacante() == null);
        Empresa refidEmpresa = idEmpresa == null ? null : daoEmpresa.find(idEmpresa);
        if (refidEmpresa == null) throw new IllegalArgumentException("Selecciona un registro existente para empresa.");
        dato.setIdEmpresa(refidEmpresa);
        Usuario refidUsuario = idUsuario == null ? null : daoUsuario.find(idUsuario);
        if (refidUsuario == null) throw new IllegalArgumentException("Selecciona un registro existente para responsable.");
        dato.setIdUsuario(refidUsuario);
        if (dato.getVigencia() == null) throw new IllegalArgumentException("La vigencia es obligatoria.");
        if (!esVigenciaValida(dato.getVigencia())) throw new IllegalArgumentException(MENSAJE_VIGENCIA);
        if (dato.getIdVacante() == null) {
            dao.create(dato);
            PublicacionVacante publicacion = new PublicacionVacante();
            publicacion.setIdVacante(dato);
            publicacion.setFechaPublicacion(new Date());
            publicaciones.create(publicacion);
        }
        else { if (dao.find(dato.getIdVacante()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
    }
    public static LocalDate primeraVigenciaPermitida() {
        return LocalDate.now(ZONA_VIGENCIA).plusDays(1);
    }

    public static boolean esVigenciaValida(Date fecha) {
        if (fecha == null) {
            return false;
        }
        LocalDate dia = Instant.ofEpochMilli(fecha.getTime()).atZone(ZONA_VIGENCIA).toLocalDate();
        return !dia.isBefore(primeraVigenciaPermitida());
    }

    private void validarTexto(String campo, String texto, int maximo, boolean obligatorio) {
        if (obligatorio && (texto == null || texto.matches("(?U)\\s*"))) {
            throw new IllegalArgumentException("Completa el campo " + campo + ".");
        }
        if (texto != null && texto.length() > maximo) {
            throw new IllegalArgumentException(campo + " admite hasta " + maximo + " caracteres.");
        }
    }

    public void eliminar(Integer id) {
        Vacante existente = id == null ? null : dao.find(id);
        if (existente == null) throw new IllegalArgumentException("El registro ya no existe.");
        dao.remove(existente);
    }
}
