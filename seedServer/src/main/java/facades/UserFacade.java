package facades;

import entity.Role;
import security.IUserFacade;
import entity.User;
import security.IUser;
import security.PasswordStorage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.List;

public class UserFacade implements IUserFacade {

    EntityManagerFactory emf;

    public UserFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public User getUserByUserId(String id) {
        EntityManager em = getEntityManager();
        return em.find(User.class, id);
    }

    @Override
    public void registerUser(String username, String password) throws PasswordStorage.CannotPerformOperationException{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Role userRole = new Role("UserResource");
            User user = new User(username, password );
            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    @Override
    public User deleteUser(String id) {
        EntityManager em = getEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("UserResource " + id + " not found!");
            }
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

    @Override
    public User editUserRoles(List<Role> roles, String username) {
        EntityManager em = getEntityManager();
        try {
            User editUser = em.find(User.class, username);
            if (editUser == null) {
                throw new EntityNotFoundException("UserResource " + username + " not found!");
            }
            editUser.setRoles(roles);
            em.getTransaction().begin();
            em.merge(editUser);
            em.getTransaction().commit();
            return editUser;
        } finally {
            em.close();
        }
    }


    public List<User> getUsers() {
        EntityManager em = getEntityManager();
        List<User> users;
        try {
            em.getTransaction().begin();
            users = em.createQuery("SELECT u FROM User u").getResultList();
            em.getTransaction().commit();
            return users;
        } finally {
            em.close();
        }
    }

    /**
     * @return Roles if users could be authenticated, otherwise null
     */
    @Override
    public List<String> authenticateUser(String userName, String password) {
        try {
            IUser user = getUserByUserId(userName);
            return user != null && PasswordStorage.verifyPassword(password, user.getPasswordHash()) ? user.getRolesAsStrings() : null;
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex) {
            throw new NotAuthorizedException("Invalid username or password", Response.Status.FORBIDDEN);
        }
    }
}