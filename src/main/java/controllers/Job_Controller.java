package controllers;

import entity.Course;
import entitymanagers.EntityManagerCoS;
import job.Job;
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
        }
    }


    @Override
    public void distribute_BatchJobs(Job job) {
        Collection<List<Long>> batch = job.getPartitionedJob();

        for (List singleBatch : batch){
            Thread t = new Thread(() ->{

                // CoS controller instantiation
                Controller_CoSA controller_coSA = new Controller_CoSA();
                Controller_CoSB controller_coSB = new Controller_CoSB();

                driver_controller = new Driver_Controller();
                WebDrivers thread_WebDrivers = driver_controller.createWebDriver();
                login_control.login(thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());

                EntityManagerCoS entityManagerCoS = new EntityManagerCoS();
                List<Course> list_Course = entityManagerCoS.cos_GetBatchList((ArrayList<Long>) singleBatch);

                /*
                    determine the cos type and call the appropriate scraper class and pass all driver information
                    for each course in single batch
                */
                for(Course c : list_Course){
                    // CoS A Scrape
                    if(c.getCosType().toString().endsWith("a")){
                        controller_coSA.cosa_Scrape(c, thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());
                    }
                    // CoS B Scrape
                    if (c.getCosType().toString().endsWith("b")){
                        controller_coSB.cosb_Scrape(c, thread_WebDrivers.getDriver(), thread_WebDrivers.getWait());
                    }
                }
                thread_WebDrivers.getDriver().quit();
            });
            threads.add(t);
            // end thread
        }
        //end for loop for new threads
    }

    @Override
    public void startBatches() {
        for(Thread t : threads){
            t.start();
        }
    }

}
