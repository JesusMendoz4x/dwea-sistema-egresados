package logneg;

import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;

@Stateless
@LocalBean
public class MEgresado {

    @EJB
    private MCatalogo catalogos;
    @EJB
    private EgresadoFacade dao;
    @EJB
    private UsuarioFacade daoUsuario;

    public List<Egresado> consultar() {
        return dao.findAll();
    }

    public List<Usuario> consultarUsuario() {
        return daoUsuario.findAll();
    }

    public void guardar(Egresado dato, Integer idUsuario) {
        catalogos.validar("carreras", dato.getCarrera());
        if (dato.getCarrera() == null || dato.getCarrera().trim().isEmpty()) {
            throw new IllegalArgumentException("Carrera es obligatorio.");
        }
        Usuario refidUsuario = idUsuario == null ? null : daoUsuario.find(idUsuario);
        if (refidUsuario == null) {
            throw new IllegalArgumentException("Selecciona un registro existente para usuario.");
        }
        dato.setIdUsuario(refidUsuario);
        if (dato.getAñoEgreso() == null || dato.getAñoEgreso() < 1900 || dato.getAñoEgreso() > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Año de egreso inválido.");
        }
        for (Egresado existente : dao.findAll()) {
            if (existente.getIdUsuario().equals(dato.getIdUsuario()) && !Objects.equals(existente.getIdEgresado(), dato.getIdEgresado())) {
                throw new IllegalArgumentException("El usuario ya tiene registro de egresado.");
            }
        }
        if (dato.getIdEgresado() == null) {
            dao.create(dato);
        } else {
            if (dao.find(dato.getIdEgresado()) == null) {
                throw new IllegalArgumentException("El registro ya no existe.");
            }
            dao.edit(dato);
        }
    }

    public void eliminar(Integer id) {
        Egresado existente = id == null ? null : dao.find(id);
        if (existente == null) {
            throw new IllegalArgumentException("El registro ya no existe.");
        }
        dao.remove(existente);
    }
}
