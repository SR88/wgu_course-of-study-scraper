package edu.wgu.cosscraper.EntityMangerTest;

import entity.EndPoint;
import entity.Link;
import entitymanagers.EntityManagement;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;

import java.util.List;

/**
 * Created By sethsneddon on Oct, 09 2018.
 */
public class Test_EntityManagement {

    EntityManagement em;

    public Test_EntityManagement(){
        em = new EntityManagement();
    }

//    @Before
//    public void build_TestCourseLink(){
//        em.testInsertLink();
//    }

    /**
     *  To return as success you must have an lrps link with no endpoint already in the table
     */
    @Test
    public void test_hql_SelectDistinctLRPSwNULLEndpoint(){
        List<String> links = em.cos_GetLrpsNoEndpoint();
        Assert.assertThat(links.isEmpty(),is(false));
        System.out.println("Number of links from query: " + links.size());
    }

    @Test
    public void test_create_endpoint(){
        EndPoint endPoint = new EndPoint("https://www.reddit.com/r/games", "reddit", "https://www.reddit.com/r/games", null, "https://www.reddit.com/r/games");
        em.create_Distinct_LRPS_EndPoint(endPoint, "https://www.reddit.com/r/games");
    }

    @Test
    public void test_hql_setLink_Endpoint(){
        em.set_Links_LRPS_Endpoint("https://www.reddit.com/" , 1);
    }

    @Test
    public void test_updateLink_withEndpoint(){
        em.test_insertNewLink();
        EndPoint endPoint = new EndPoint("https://www.reddit.com/r/games", "reddit", "https://www.reddit.com/r/games", null, "https://www.reddit.com/r/games");
        em.create_Distinct_LRPS_EndPoint(endPoint, "https://www.reddit.com/r/games");

    }

//    @AfterAll
//    public void tearDownTestCourse(){
//        em.testRemoveLink("https://www.reddit.com/");
//        em.testRemoveCourse("C123 - Test Course Of Study");
//    }

}
