import controllers.IJob_Controller;
import controllers.Job_Controller;

/**
 * Created By sethsneddon on Aug, 17 2018.
 */
public class Application {

    public static void main(String[] args){

        IJob_Controller job_controller = new Job_Controller();
        job_controller.startUp_All();



//        /**
//         * cosa scrape example
//         */
//        Controller_CoSA controller_coSA = new Controller_CoSA();
//        controller_coSA.cosa_Scrape((long) 31);
//
//        /**
//         *  CoSB scrape example
//         */
//        Controller_CoSB controller_coSB = new Controller_CoSB();
//        controller_coSB.cosb_Scrape();


//        Course c = new Course("Test1", 99, "Test1", 99, "123456", "www.google.com");
//
//        EntityManagerCoS entityManagerCoS = new EntityManagerCoS();
//        entityManagerCoS.testBasicUsage(c);
//
//        List<Link> linksList;
//        linksList = entityManagerCoS.cos_GetLrpsNoEndpoint();

//        for (Link l : linksList){
//            System.out.println(l.toString());
//        }

    }
}
