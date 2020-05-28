
import com.mavericks.ums.model.User;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nabin
 */

public class UserTest{
    private User user;
    
    public UserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    
    @Before
    public void setUp() {
        user = new User(1,"ramdh","password123","np03180184@heraldcollege.edu.np","Nabin","Chaulagain","admin",null,"9861780061");
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetId(){
        int id = user.getId();
        assertTrue(id == 1);
    }
    @Test
    public void testGetEmail(){
        String email = user.getEmail();
        assertEquals("np03180184@heraldcollege.edu.np", email);
    }

    @Test
    public void testGetUsername(){
        String username = user.getUsername();
        assertEquals("ramdh", username);
    }
    
    @Test
    public void testGetPhoneNum(){
        String phoneNum = user.getPhoneNum();
        assertEquals("9861780061", phoneNum);
    }
    
    @Test
    public void testGetFullName(){
        assertTrue("Nabin Chaulagain".equals(user.getFullName()));
    }
}