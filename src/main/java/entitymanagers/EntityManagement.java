package entitymanagers;

import entity.Course;
import entity.EndPoint;
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
public class EntityManagement {

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

    public void testBasicUsage(Course c){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        javax.persistence.EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        //entityManager.persist(new Course("COS-a", 1, "Seth's TEST COS", 2, "1234567890", "https://www.google.com", timestamp));

        entityManager.persist(c);

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
        javax.persistence.EntityManager entityManager = entityManagerFactory.createEntityManager();
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

        try{
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test_insertNewLink(){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Link testLink = new Link();
        testLink.setUrl("https://www.reddit.com/r/games");
        testLink.setLinkText("Reddit - Games");
        em.persist(testLink);

        em.getTransaction().commit();

        em.close();

        try{
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testRemoveLink(String linkUrl){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        javax.persistence.EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        String hqlDelete = "delete Link link where link.url = :linkUrl";
        em.createQuery(hqlDelete)
                .setParameter("linkUrl", linkUrl)
                .executeUpdate();

        em.getTransaction().commit();

        em.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testRemoveCourse(String cosName){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        javax.persistence.EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        String hqlDelete = "delete Course course where course.cosName = :cosName";
        em.createQuery(hqlDelete)
                .setParameter("cosName", cosName)
                .executeUpdate();

        em.getTransaction().commit();

        em.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadCourses(List<Course> courseList){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.persistence.EntityManager entityManager = entityManagerFactory.createEntityManager();
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
        javax.persistence.EntityManager entityManager = entityManagerFactory.createEntityManager();
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

        javax.persistence.EntityManager em = entityManagerFactory.createEntityManager();
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

        javax.persistence.EntityManager em = entityManagerFactory.createEntityManager();
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

    public List<String> cos_GetLrpsNoEndpoint(){
        List<String> links;

        try{
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.persistence.EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        /**
         *      Select distinct urls that have lrps address and have nulls for endpoint ids
         */
        Query q1 = em.createQuery("select distinct link.url from Link link where link.url like '%lrps%' and endPoint is null");

        links = q1.getResultList();

        em.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }

    public Long create_Distinct_LRPS_EndPoint(EndPoint distinctEndPoint, String distinctLRPSLink){
        try{
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.persistence.EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(distinctEndPoint);
        entityManager.getTransaction().commit();

        entityManager.close();

        System.out.println("DISTINCT EndPoint: " + distinctEndPoint.toString() + " has been inserted.");


        EntityManager em = entityManagerFactory.createEntityManager();
        Query query = em.createQuery("from EndPoint where lrps in (:lrps)");
        query.setParameter("lrps", distinctLRPSLink);
        List<EndPoint> ep = query.getResultList();
        Long endPointRecordNumber = ep.get(0).getId();

        // didn't work
        //EndPoint ep = em.find(EndPoint.class, distinctLRPSLink);

        try{
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return endPointRecordNumber;
    }

    public void set_Links_LRPS_Endpoint(String distinctLRPSLink, long endPointID){
        try{
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.persistence.EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        String hqlUpdate = "update Link link set link.endPoint = :endPointID where link.url = :distinctLRPSLink";
        em.createQuery(hqlUpdate)
                .setParameter("endPointID", endPointID)
                .setParameter("distinctLRPSLink", distinctLRPSLink)
                .executeUpdate();

        em.getTransaction().commit();

        em.close();

        try{
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void links_SetEndpoint(String distinctLRPSLink){
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.persistence.EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List results = entityManager.createQuery("from EndPoint endpoint where endpoint.lrps = :lrps")
                .setParameter("lrps", distinctLRPSLink)
                .getResultList();

        EndPoint ep = (EndPoint) results.get(0);

        String hqlUpdate = "update Link link set link.endPoint = :endPoint where link.url = :url";
        entityManager.createQuery(hqlUpdate)
                .setParameter("endPoint", ep)
                .setParameter("url", distinctLRPSLink)
                .executeUpdate();

        entityManager.getTransaction().commit();

        entityManager.close();

        try {
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public int get_EndPointID_ByLRPS(String address){
//        int i;
//
//        try {
//            setup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//
//        String hqlQuery = ("from EndPoint where lrps = :lrpsString");
//        List<EndPoint> epList = em.createQuery(hqlQuery)
//                .setParameter("lrpsString", address)
//                .getResultList();
//
//        if (epList.size() > 0){
//            EndPoint endPoint = epList.get(0);
//            i = Math.toIntExact(endPoint.getId());
//        } else {
//            System.out.println("No EndPoint with that LRPS URL.");
//        }
//
//        return i;
//    }


}
