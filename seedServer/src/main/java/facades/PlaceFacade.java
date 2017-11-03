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
    public static String BASE_IMAGE_URL;

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

    public Place addPlace(String address, String city, String zip, String desctiption, String image) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Place place = new Place();
            place.setAddress(address);
            place.setCity(city);
            place.setZip(zip);
            place.setDescription(desctiption);
            place.setImageUrl(BASE_IMAGE_URL+image);
            em.persist(place);
            em.getTransaction().commit();
            return place;
        }
        finally {
            em.close();
        }
    }
}
