package com.wfm.events;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class JobEvent extends AbstractEvent<JobEventType> {
    private String jobID;

    public JobEvent(JobEventType jobEventType, String jobID) {
        super(jobEventType);
        this.jobID = jobID;
    }

    public String getJobID() {
        return jobID;
    }
}
