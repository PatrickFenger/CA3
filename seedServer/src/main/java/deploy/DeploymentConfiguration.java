package deploy;

import entity.Place;
import entity.Role;
import entity.User;
import facades.UserFacade;
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
            input = getClass().getClassLoader().getResourceAsStream("/config.properties");;
            if (input == null) {
                System.out.println("Could not load init-properties");
                return;
            }
            prop.load(input);
            Secret.SHARED_SECRET = prop.getProperty("tokenSecret").getBytes();
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
                if (em.find(User.class, "user") == null ) {
                    em.getTransaction().begin();
                    Role userRole = new Role("User");
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

                    System.out.println("Created TEST Users");

                    Place place = new Place();
                    place.setAddress("Wall Street, 26");
                    place.setCity("New York");
                    place.setDescription("Where the money at!");
                    place.setRating(5);
                    place.setZip("6666");
                    place.setImageUrl("http://financeblvd.com/wp-content/uploads/2017/08/wallstreetfeature.jpg");
                    
                    Place place2 = new Place();
                    place2.setAddress("Nuhavn 7");
                    place2.setCity("Kobenhavn");
                    place2.setDescription("WHere the blondes at");
                    place2.setImageUrl("http://cdn-image.travelandleisure.com/sites/default/files/styles/1600x1000/public/header-facebook-copenhagen-cph0416.jpg?itok=-2LcgY1H");
                    place2.setZip("1234");
                    place2.setRating(4);
                    
                    Place place3 = new Place();
                    place3.setAddress("Praca do Comercio");
                    place3.setCity("Lisbon");
                    place3.setDescription("Where the sun is");
                    place3.setImageUrl("https://media.cntraveler.com/photos/58f984542867946a9cbe1f11/master/pass/praca-do-comercio-lisbon-GettyImages-648812458.jpg");
                    place3.setZip("3000");
                    place3.setRating(5);
                    
                    em.persist(place);
                    em.persist(place2);
                    em.persist(place3);
                    System.out.println("Created Places");
                    
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
