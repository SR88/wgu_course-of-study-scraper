package job;

import java.util.Collection;
import java.util.List;

/**
 * Created By sethsneddon on Sep, 17 2018.
 */
public class Job {
    private Boolean job_CourseList;
    private Boolean job_CoS;
    private Boolean job_EndPoint;
    private Collection<List<Long>> partitionedJob;
    private Boolean overWriteAll;
    private Boolean insertOnly;
    private Collection<List<String>> partitionedLRPSJob;

    public Job(){}

    @Override
    public String toString() {
        return "Job{" +
                "job_CourseList=" + job_CourseList +
                ", job_CoS=" + job_CoS +
                ", job_EndPoint=" + job_EndPoint +
                ", partitionedJob=" + partitionedJob +
                ", overWriteAll=" + overWriteAll +
                ", insertOnly=" + insertOnly +
                ", partitionedLRPSJob=" + partitionedLRPSJob +
                '}';
    }

    public Collection<List<String>> getPartitionedLRPSJob() {
        return partitionedLRPSJob;
    }

    public void setPartitionedLRPSJob(Collection<List<String>> partitionedLRPSJob) {
        this.partitionedLRPSJob = partitionedLRPSJob;
    }

    public Boolean getJob_EndPoint() {
        return job_EndPoint;
    }

    public void setJob_EndPoint(Boolean job_EndPoint) {
        this.job_EndPoint = job_EndPoint;
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