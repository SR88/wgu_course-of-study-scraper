package job;

import java.util.Collection;
import java.util.List;

/**
 * Created By sethsneddon on Sep, 17 2018.
 */
public class Job {
    private Boolean job_CourseList;
    private Boolean job_CoS;
    private Collection<List<Long>> partitionedJob;
    private Boolean overWriteAll;
    private Boolean insertOnly;

    public Job(){}

    @Override
    public String toString() {
        return "Job{" +
                "job_CourseList=" + job_CourseList +
                ", job_CoS=" + job_CoS +
                ", partitionedJob=" + partitionedJob +
                ", overWriteAll=" + overWriteAll +
                ", insertOnly=" + insertOnly +
                '}';
    }

    public Boolean getJob_CourseList() {
        return job_CourseList;
    }

    public void setJob_CourseList(Boolean job_CourseList) {
        this.job_CourseList = job_CourseList;
    }

    public Boolean getJob_CoS() {
        return job_CoS;
    }

    public void setJob_CoS(Boolean job_CoS) {
        this.job_CoS = job_CoS;
    }

    public Collection<List<Long>> getPartitionedJob() {
        return partitionedJob;
    }

    public void setPartitionedJob(Collection<List<Long>> partitionedJob) {
        this.partitionedJob = partitionedJob;
    }

    public Boolean getOverWriteAll() {
        return overWriteAll;
    }

    public void setOverWriteAll(Boolean overWriteAll) {
        this.overWriteAll = overWriteAll;
    }

    public Boolean getInsertOnly() {
        return insertOnly;
    }

    public void setInsertOnly(Boolean insertOnly) {
        this.insertOnly = insertOnly;
    }
}