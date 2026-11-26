/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Mensaje;

/**
 *
 * @author jesusmn
 */
@Stateless
public class MensajeFacade extends AbstractFacade<Mensaje> {

    @PersistenceContext(unitName = "sistema-residentes-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MensajeFacade() {
        super(Mensaje.class);
    }
    
    public java.util.List<Mensaje> consultarPorConversacion(Integer conversacion) {
        return em.createQuery("SELECT m FROM Mensaje m JOIN FETCH m.idUsuario "
                + "WHERE m.idConversacion.idConversacion = :conversacion "
                + "ORDER BY m.fechaHoraEnvio ASC, m.idMensaje ASC", Mensaje.class)
                .setParameter("conversacion", conversacion).getResultList();
    }
}
