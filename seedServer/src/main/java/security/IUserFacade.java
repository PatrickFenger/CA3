package security;

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
    User deleteUser(String id);
    User editUser(User user);

}
