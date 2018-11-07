package controllers;

import entity.Course;
import entitymanagers.EntityManagement;
import job.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import utility.Misc;
import webdrivers.WebDrivers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created By sethsneddon on Sep, 11 2018.
 */
public class Job_Controller implements IJob_Controller {

    private ILogin_Control login_control = new Login_Control();
    private IJob_Loader job_loader = new Job_Loader();
    private ArrayList<String> credentials;
    private ArrayList<Thread> threads = new ArrayList<>();
    private Job job;
    private IDriver_Controller driver_controller;
    public WebDrivers webDrivers;

    @Override
    public void startUp_All() {
        Misc.bootSig();
        credentials = login_control.get_credentials();
        job = (job_loader.create_Job());

        if (job.getJob_CourseList()){
            driver_controller = new Driver_Controller();
            webDrivers = driver_controller.createWebDriver();
            login_control.login(this.webDrivers.getDriver(), this.webDrivers.getWait());

            // todo updateall or insertnew

            Controller_CourseList controller_courseList = new Controller_CourseList();
            controller_courseList.courselist_ScrapeAll(this.webDrivers.getDriver(), this.webDrivers.getWait());

            this.webDrivers.getDriver().quit();

        } else if (job.getJob_CoS()) {
            //todo distribute job
            //todo updateall or insertnew

            distribute_BatchJobs(job);
            startBatches();

        } else if (job.getJob_EndPoint()){
            distribute_BatchJobs(job);
            startBatches();
        }
    }

    /**
     * takes a job object and creates threads if one of the boolean attributes is true
     * @param job
     */
    @Override
    public void distribute_BatchJobs(Job job) {

        /**
         *  distribute a cosa / b batch job among threads and run them
         */
        if (job.getJob_CoS()) {
            Collection<List<Long>> batch = job.getPartitionedJob();
            for (List singleBatch : batch) {
                Thread t = new Thread(() -> {

                    // CoS controller instantiation
                    Controller_CoSA controller_coSA = new Controller_CoSA();
                    Controller_CoSB controller_coSB = new Controller_CoSB();

                    driver_controller = new Driver_Controller();
                    WebDrivers thread_WebDrivers = driver_controller.createWebDriver();
                    login_control.login(thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());

                    // blow out the zoom level to help with the scraping
                    for (int i = 0; i<5; i++) {
                        thread_WebDrivers.getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.META, Keys.ADD);
                    }

                    EntityManagement entityManagement = new EntityManagement();
                    List<Course> list_Course = entityManagement.cos_GetBatchList((ArrayList<Long>) singleBatch);


                /*
                    determine the cos type and call the appropriate scraper class and pass all driver information
                    for each course in single batch
                */
                    for (Course c : list_Course) {
                        // CoS A Scrape
                        if (c.getCosType().toString().endsWith("a")) {
                            controller_coSA.cosa_Scrape(c, thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());
                        }
                        // CoS B Scrape
                        if (c.getCosType().toString().endsWith("b")) {
                            controller_coSB.cosb_Scrape(c, thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());
                        }
                    }
                    thread_WebDrivers.getDriver().quit();
                });
                threads.add(t);
            } // end threads loop
        } // end if cos scrape job

        /**
         *  distribute unregistered distinct links jobs across threads
         */
         if (job.getJob_EndPoint()){
            Collection<List<String>> batch = job.getPartitionedLRPSJob();
            for(List singleBatch : batch){
                List<String> listURLs = singleBatch;
                Thread t = new Thread(() ->{

                    for (String distinctStringUrl : listURLs){
                        Controller_LRPS_EndPoint controller_lrps_endPoint = new Controller_LRPS_EndPoint();
                        driver_controller = new Driver_Controller();
                        WebDrivers thread_WebDrivers = driver_controller.createWebDriver();
                        login_control.login(thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());

                        controller_lrps_endPoint.endPoint_Scrape(thread_WebDrivers.getDriver(), thread_WebDrivers.getWait(), distinctStringUrl);

                        thread_WebDrivers.getDriver().quit();

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    Controller_LRPS_EndPoint controller_lrps_endPoint = new Controller_LRPS_EndPoint();
//                    driver_controller = new Driver_Controller();
//                    WebDrivers thread_WebDrivers = driver_controller.createWebDriver();
//                    login_control.login(thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());
//
//                    for(String distinctStringUrl : listURLs) {
//                        controller_lrps_endPoint.endPoint_Scrape(thread_WebDrivers.getDriver(), thread_WebDrivers.getWait(), distinctStringUrl);
//                    }
//                    thread_WebDrivers.getDriver().quit();
                });
                threads.add(t);
            }  // end threads loop
        } // end if endpoint job
    }

    @Override
    public void startBatches() {
        for(Thread t : threads){
            t.start();
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
