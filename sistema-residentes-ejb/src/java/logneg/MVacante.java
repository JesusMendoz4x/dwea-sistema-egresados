package logneg;
import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MVacante {
    @EJB private VacanteFacade dao;
    @EJB private PublicacionVacanteFacade publicaciones;
    @EJB private EmpresaFacade daoEmpresa;
    @EJB private UsuarioFacade daoUsuario;
    public List<Vacante> consultar() { return dao.findAll(); }
    public List<Empresa> consultarEmpresa() { return daoEmpresa.findAll(); }
    public List<Usuario> consultarUsuario() { return daoUsuario.findAll(); }
    public void guardar(Vacante dato, Integer idEmpresa, Integer idUsuario) {
        if (dato.getPuesto() == null || dato.getPuesto().trim().isEmpty()) throw new IllegalArgumentException("Puesto es obligatorio.");
        if (dato.getDescripcion() == null || dato.getDescripcion().trim().isEmpty()) throw new IllegalArgumentException("Descripción es obligatorio.");
        if (dato.getUbicacion() == null || dato.getUbicacion().trim().isEmpty()) throw new IllegalArgumentException("Ubicación es obligatorio.");
        Empresa refidEmpresa = idEmpresa == null ? null : daoEmpresa.find(idEmpresa);
        if (refidEmpresa == null) throw new IllegalArgumentException("Selecciona un registro existente para empresa.");
        dato.setIdEmpresa(refidEmpresa);
        Usuario refidUsuario = idUsuario == null ? null : daoUsuario.find(idUsuario);
        if (refidUsuario == null) throw new IllegalArgumentException("Selecciona un registro existente para responsable.");
        dato.setIdUsuario(refidUsuario);
        if (dato.getVigencia() == null) throw new IllegalArgumentException("La vigencia es obligatoria.");
        if (dato.getIdVacante() == null) {
            dao.create(dato);
            PublicacionVacante publicacion = new PublicacionVacante();
            publicacion.setIdVacante(dato);
            publicacion.setFechaPublicacion(new Date());
            publicaciones.create(publicacion);
        }
        else { if (dao.find(dato.getIdVacante()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
    }
    public void eliminar(Integer id) {
        Vacante existente = id == null ? null : dao.find(id);
        if (existente == null) throw new IllegalArgumentException("El registro ya no existe.");
        dao.remove(existente);
    }
}
