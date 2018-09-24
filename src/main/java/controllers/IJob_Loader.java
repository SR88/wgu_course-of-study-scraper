package controllers;

import job.Job;

/**
 * Created By sethsneddon on Sep, 09 2018.
 */
public interface IJob_Loader {

    void declare_JobType();

    void load_Job();

    void balance_Job();

    void initialize_CoSJob();

    void initialize_ListJob();

    Job create_Job();
}
