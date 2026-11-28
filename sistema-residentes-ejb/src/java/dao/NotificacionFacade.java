/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Notificacion;

/**
 *
 * @author jesusmn
 */
@Stateless
public class NotificacionFacade extends AbstractFacade<Notificacion> {

    @PersistenceContext(unitName = "sistema-residentes-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Notificacion> consultarDestinatario(Integer idUsuario) {
        return em.createQuery(
                "SELECT n FROM Notificacion n WHERE n.idUsuario.idUsuario = :idUsuario "
                + "ORDER BY n.fechaEmision DESC, n.idNotificacion DESC", Notificacion.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public NotificacionFacade() {
        super(Notificacion.class);
    }
    
}
