/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Vacante;

/**
 *
 * @author jesusmn
 */
@Stateless
public class VacanteFacade extends AbstractFacade<Vacante> {

    @PersistenceContext(unitName = "sistema-residentes-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Vacante> consultarVigentesPublicadas() {
        return em.createQuery(
                "SELECT v FROM Vacante v JOIN FETCH v.idEmpresa "
                + "WHERE v.vigencia >= CURRENT_DATE AND EXISTS "
                + "(SELECT p.idPublicacionVacante FROM PublicacionVacante p "
                + "WHERE p.idVacante = v AND p.fechaPublicacion <= CURRENT_DATE) "
                + "ORDER BY v.vigencia, v.puesto, v.idVacante", Vacante.class)
                .getResultList();
    }

    public List<String> sugerirPuestos() {
        return em.createQuery("SELECT DISTINCT v.puesto FROM Vacante v WHERE v.puesto IS NOT NULL ORDER BY v.puesto",
                String.class).getResultList();
    }

    public List<String> sugerirUbicaciones() {
        return em.createQuery("SELECT DISTINCT v.ubicacion FROM Vacante v WHERE v.ubicacion IS NOT NULL ORDER BY v.ubicacion",
                String.class).getResultList();
    }

    public VacanteFacade() {
        super(Vacante.class);
    }
    
}
