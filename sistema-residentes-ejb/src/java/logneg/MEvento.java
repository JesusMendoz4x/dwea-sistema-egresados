package logneg;

import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;

@Stateless
@LocalBean
public class MEvento {

    @EJB
    private EventoFacade dao;
    @EJB
    private MCatalogo catalogos;

    public List<Evento> consultar() {
        return dao.findAll();
    }

    public void guardar(Evento dato) {
        if (dato.getTitulo() == null || dato.getTitulo().trim().isEmpty() || dato.getFecha() == null || dato.getSede() == null || dato.getSede().trim().isEmpty()) {
            throw new IllegalArgumentException("Completa título, fecha y sede.");
        }
        catalogos.validar("tiposEvento", dato.getTipo());
        if (dato.getIdEvento() == null) {
            dao.create(dato);
        } else {
            if (dao.find(dato.getIdEvento()) == null) {
                throw new IllegalArgumentException("Registro inexistente.");
            }
            dao.edit(dato);
        }
    }

    public void eliminar(Integer id) {
        Evento dato = id == null ? null : dao.find(id);
        if (dato == null) {
            throw new IllegalArgumentException("Registro inexistente.");
        }
        dao.remove(dato);
    }
}
