package deploy;

import entity.Place;
import entity.Role;
import entity.User;
import facades.PlaceFacade;
import facades.UserFacade;
import rest.PlaceResource;
import security.Secret;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class DeploymentConfiguration implements ServletContextListener {

    public static String PU_NAME = "CA3";

    @Override
    @SuppressWarnings("empty-statement")
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("######################################################################################");
        System.out.println("############################ In ContextIntialized ####################################");
        System.out.println("######################################################################################");

        //Handling init-params from the properties file (secrets that should not be pushed to GIT)
        InputStream input = null;
        Properties prop = new Properties();
        try {
            input = getClass().getClassLoader().getResourceAsStream("/config.properties");
            ;
            if (input == null) {
                System.out.println("Could not load init-properties");
                return;
            }
            prop.load(input);
            Secret.SHARED_SECRET = prop.getProperty("tokenSecret").getBytes();
            PlaceResource.FILE_LOCATION = prop.getProperty("fileLocation");
            PlaceFacade.BASE_IMAGE_URL = prop.getProperty("baseImageUrl");
            input.close();

        } catch (IOException ex) {
            Logger.getLogger(DeploymentConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }

        ServletContext context = sce.getServletContext();

        boolean makeTestUser = context.getInitParameter("makeTestUser").toLowerCase().equals("true");
        if (makeTestUser) {
            EntityManager em = Persistence.createEntityManagerFactory(PU_NAME).createEntityManager();
            try {
                System.out.println("Creating TEST Users");
                if (em.find(User.class, "user") == null) {
                    em.getTransaction().begin();
                    Role userRole = new Role("UserResource");
                    Role adminRole = new Role("Admin");
                    User user = new User("user", "test");
                    user.addRole(userRole);
                    User admin = new User("admin", "test");
                    admin.addRole(adminRole);
                    User both = new User("user_admin", "test");
                    both.addRole(userRole);
                    both.addRole(adminRole);
                    em.persist(userRole);
                    em.persist(adminRole);
                    em.persist(user);
                    em.persist(admin);
                    em.persist(both);                   
                
               
                    Place place = new Place();
                    place.setAddress("Wall Street, 26");
                    place.setCity("New York");
                    place.setDescription("Where the money at!");
                    place.setRating(5);
                    place.setZip("6666");
                    place.setImageUrl("http://financeblvd.com/wp-content/uploads/2017/08/wallstreetfeature.jpg");
                    
                    Place place1 = new Place();
                    place1.setAddress("Kjeldsgårdsvej 27C, 3th.");
                    place1.setCity("Copenhagen");
                    place1.setDescription("Valby");
                    place1.setRating(3);
                    place1.setZip("2500");
                    place1.setImageUrl("http://rejsebox.dk/wp-content/uploads/2016/08/rejsebox-34673457_l-2015.jpg");
                    
                    Place place2 = new Place();
                    place2.setAddress("Tværager 63");
                    place2.setCity("Greve");
                    place2.setDescription("Parents");
                    place2.setRating(1);
                    place2.setZip("2670");
                    place2.setImageUrl("http://rejsebox.dk/wp-content/uploads/2016/08/rejsebox-34673457_l-2015.jpg");
                   
                    
                    Place place3 = new Place();
                    place3.setAddress("Kjeldsgårdsvej 27C, 3th.");
                    place3.setCity("Sweden");
                    place3.setDescription("Lalandia");
                    place3.setRating(4);
                    place3.setZip("2500");
                    place3.setImageUrl("http://rejsebox.dk/wp-content/uploads/2016/08/rejsebox-34673457_l-2015.jpg");
                    
                    Place place4 = new Place();
                    place4.setAddress("Kjeldsgårdsvej 27C, 3th.");
                    place4.setCity("Maribo");
                    place4.setDescription("Beauty of Denmark");
                    place4.setRating(2);
                    place4.setZip("2500");
                    place4.setImageUrl("http://rejsebox.dk/wp-content/uploads/2016/08/rejsebox-34673457_l-2015.jpg");
               
                    em.persist(place);
                    em.persist(place1);
                    em.persist(place2);
                    em.persist(place3);
                    em.persist(place4);
                 
                  
                    em.getTransaction().commit();
                }
                
            } catch (Exception ex) {
                Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
                em.getTransaction().rollback();
            } finally {
                em.close();
            }

        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

