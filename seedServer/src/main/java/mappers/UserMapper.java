/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappers;

import entity.User;
import java.util.List;

/**
 *
 * @author trez__000
 */
public class UserMapper {
    public String USER_NAME;
    public List<String> USER_ROLE;
    
    public UserMapper(User user){
        this.USER_NAME = user.getUserName();
        this.USER_ROLE = user.getRolesAsStrings();
    }
}
