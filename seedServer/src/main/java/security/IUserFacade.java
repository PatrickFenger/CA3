package security;

import entity.Role;
import entity.User;

import java.util.List;

/**
 *
 * @author lam
 */
public interface IUserFacade {

    /*
    Return the Roles if users could be authenticated, otherwise null
     */
    List<String> authenticateUser(String userName, String password);
    User getUserByUserId(String id);
    List<User> getUsers();
    User deleteUser(String id);
    User editUserRoles(List<Role> roles, String username);

    
    void registerUser(String username, String password) throws PasswordStorage.CannotPerformOperationException;
    
    
    
}
