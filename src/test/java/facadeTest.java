/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entity.Cityinfo;
import entity.Person;
import java.util.HashMap;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import facade.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author TimmosQuadros
 */
public class facadeTest {

    Facade fp;

    public facadeTest() {

        fp = new Facade(Persistence.createEntityManagerFactory("TESTPU"));
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        HashMap<String, Object> puproperties = new HashMap();
        //puproperties.put("TESTPU", "scripts/ClearDB.sql");
        //Persistence.generateSchema("mydb", puproperties);
        //Persistence.generateSchema("mydb", null);
//        fp.setEmf(Persistence.createEntityManagerFactory( "REST1PU_TEST"));
//        fp.createPerson(new Person(1, "Abe", "Abesen", null));
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
     public void hello() {
     
         Person p = fp.getPerson(1);
         Person p_test = new Person(1, "Abe", "Abesen", null);
         assertEquals(p.getFirstName(), p_test.getFirstName());
     }
}
