/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappers;

import entity.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author trez__000
 */
public class UsersMapper {
    public List<UserMapper> users = new ArrayList();
    
    public UsersMapper(List<User> users){
        for (User u: users){
            this.users.add(new UserMapper(u));
        }
    }
}
