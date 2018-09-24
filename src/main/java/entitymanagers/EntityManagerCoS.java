package entitymanagers;

import entity.Course;
import entity.Link;
import javax.persistence.*;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By sethsneddon on Aug, 22 2018.
 */
public class EntityManagerCoS {

    private EntityManagerFactory entityManagerFactory;
    SimpleDateFormat sdf;
    Date date;

    protected void setup() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("edu.wgu.cosscrapper");
        System.out.println("Entity Manager Setup");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();
    }

    protected void tearDown() throws Exception{
        entityManagerFactory.close();
        System.out.println("Entity Manger Shutdown");
    }

    public void testBasicUsage(){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        String timestamp = sdf.format(date);

        //entityManager.persist(new Course("COS-a", 1, "Seth's TEST COS", 2, "1234567890", "https://www.google.com", timestamp));


        entityManager.getTransaction().commit();
        entityManager.close();





        // now lets pull events from the database and list them
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Course> result = entityManager.createQuery("from Course", Course.class).getResultList();
        for (Course course : result){
            System.out.println(course.toString());
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void testInsertLink(){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Course course = new Course("COS-a", 1, "C123 - Test Course Of Study", 1, "1234567890", "https://my.wgu.edu/1234567890");
        entityManager.persist(course);

        Link testLink = new Link();
        testLink.setLinkText("This is some context");
        testLink.setUrl("https://www.reddit.com/");
        testLink.setCourse(course);
        entityManager.persist(testLink);

        entityManager.getTransaction().commit();

        entityManager.close();
    }

    public void uploadCourses(List<Course> courseList){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        for (Course course : courseList){
            entityManager.persist(course);
        }

        entityManager.getTransaction().commit();
        entityManager.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void link_Insert(Long courseId, List<Link> links){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Course tempCourse = entityManager.getReference(Course.class, courseId);

        for (Link link : links){
            link.setCourse(tempCourse);
            entityManager.persist(link);
        }

        entityManager.getTransaction().commit();

        entityManager.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String cos_GetURL(Long record){
        String cos_Url = "";

        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Course course = em.find(Course.class, record);
        cos_Url = course.getUrl();

        em.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cos_Url;
    }

    public List<Course> cos_GetBatchList(ArrayList<Long> list_SingleBatch){
        List<Course> courses;

        try{
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();


        Query query = em.createQuery("from Course where id in (:id)");
        query.setParameter("id", list_SingleBatch);
        courses = query.getResultList();

        em.close();

        try{
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

}
