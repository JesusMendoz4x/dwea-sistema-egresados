package logneg;
import dao.CatalogoFacade;
import modelo.Catalogo;
import java.util.*;
import javax.ejb.*;
@Stateless @LocalBean public class MCatalogo {
 @EJB private CatalogoFacade dao;
 public List<String> consultar(String categoria) {
  List<String> valores=new ArrayList<>();
  for(Catalogo c:dao.findAll()) if(categoria.equals(c.getCategoria())) valores.add(c.getValor());
  Collections.sort(valores);return valores;
 }
 public void validar(String categoria,String valor) {
  if(valor==null || !consultar(categoria).contains(valor)) throw new IllegalArgumentException("Selecciona un valor válido para "+categoria+".");
 }
}
