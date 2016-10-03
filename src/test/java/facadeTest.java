/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entity.Person;
import java.util.HashMap;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TimmosQuadros
 */
public class facadeTest {
    
    public facadeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        HashMap<String, Object> puproperties = new HashMap();
        puproperties.put("javax.persistence.sql-load-script-source", "scripts/ClearDB.sql");
        Persistence.generateSchema("mydb", puproperties);
        Persistence.generateSchema("mydb", null); 
//        fp.setEmf(Persistence.createEntityManagerFactory( "REST1PU_TEST"));
//        fp.addPerson(new Person("Dan", "Mark", 76543321));
//        fp.addPerson(new Person("Kaj", "Olsen", 34565432));
//        fp.addPerson(new Person("Jens", "Madsen", 85858882));
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
