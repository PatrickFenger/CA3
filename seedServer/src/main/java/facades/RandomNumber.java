/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author trez__000
 */
public class RandomNumber {

    private int number;

    public int getRndNumber() {
        number = (int) (Math.random() * 10 + 1);
        return number;
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
        UserFacade uf = new UserFacade(emf);
        System.out.println(uf.getUsers());
    }
}
