package facades;

import javax.persistence.Persistence;

/**
 * @author lam
 */
public class PlaceFacadeFactory {
    private static final PlaceFacade instance = new PlaceFacade(Persistence.createEntityManagerFactory("CA3"));

    public static PlaceFacade getInstance() {
        return instance;
    }
}