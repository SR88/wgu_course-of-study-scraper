package controllers;

import job.Job;

/**
 * Created By sethsneddon on Sep, 10 2018.
 */
public interface IJob_Controller {

    void startUp_All();

    void distribute_BatchJobs(Job job);

    void startBatches();


}
