package facades;

import entity.Place;

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

    public List<Place> getAllPlaces() {
        EntityManager em = getEntityManager();
        List<Place> Places;
        try {
            em.getTransaction().begin();
            Places = em.createQuery("SELECT u FROM Place u").getResultList();
            em.getTransaction().commit();
            return Places;
        } finally {
            em.close();
        }
    }
}
