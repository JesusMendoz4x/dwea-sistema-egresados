package logneg;
import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MEmpresa {
    @EJB private EmpresaFacade dao;
    public List<Empresa> consultar() { return dao.findAll(); }
    public void guardar(Empresa dato) {
        if (dato.getNombre() == null || dato.getNombre().trim().isEmpty()) throw new IllegalArgumentException("Nombre es obligatorio.");
        if (dato.getIdEmpresa() == null) dao.create(dato);
        else { if (dao.find(dato.getIdEmpresa()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
    }
 public void eliminar(Integer id){Empresa existente=id==null?null:dao.find(id);if(existente==null)throw new IllegalArgumentException("Registro inexistente.");if(!existente.getVacanteCollection().isEmpty())throw new IllegalArgumentException("La empresa tiene vacantes; elimina o reasigna sus vacantes primero.");dao.remove(existente);}
}
