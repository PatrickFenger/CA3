package facades;

import entity.place;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by adam on 01/11/2017.
 */
public class PlaceFacade {
    EntityManagerFactory emf;

    public PlaceFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<place> getAllPlaces() {
        EntityManager em = getEntityManager();
        List<place> places;
        try {
            em.getTransaction().begin();
            places = em.createQuery("SELECT u FROM Place u").getResultList();
            em.getTransaction().commit();
            return places;
        } finally {
            em.close();
        }
    }
}
