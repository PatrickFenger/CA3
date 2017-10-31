package facades;

import entity.Role;
import security.IUserFacade;
import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade {

  EntityManagerFactory emf;

  public UserFacade(EntityManagerFactory emf) {
    this.emf = emf;   
  }

  private EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  @Override
  public IUser getUserByUserId(String id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(User.class, id);
    } finally {
      em.close();
    }
  }
  
  public List<User> getUsers(){
      EntityManager em = getEntityManager();
      List<User> users;
      try {
          em.getTransaction().begin();
          users = em.createQuery("SELECT u FROM SEED_USER u").getResultList();
          em.getTransaction().commit();
          return users;
      }
      finally{
          em.close();
      }
  }
  
  public void setUser(String username, String password) throws PasswordStorage.CannotPerformOperationException{
      EntityManager em = getEntityManager();
      try {
          em.getTransaction().begin();
          Role userRole = new Role("User");          
          User user = new User(username, password );
          user.addRole(userRole);   
          em.persist(user);         
          em.getTransaction().commit();
          System.out.println("Created New user");
      }
      finally {
          em.close();
      }
  }

  /*
  Return the Roles if users could be authenticated, otherwise null
   */
  @Override
  public List<String> authenticateUser(String userName, String password) {
    try {
      System.out.println("User Before:" + userName+", "+password);
      IUser user = getUserByUserId(userName);  
      System.out.println("User After:" + user.getUserName()+", "+user.getPasswordHash());
      return user != null && PasswordStorage.verifyPassword(password, user.getPasswordHash()) ? user.getRolesAsStrings() : null;
    } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex) {
      throw new NotAuthorizedException("Invalid username or password", Response.Status.FORBIDDEN);
    }
  }

}