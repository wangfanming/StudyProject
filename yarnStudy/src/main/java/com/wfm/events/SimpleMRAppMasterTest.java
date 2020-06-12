package com.wfm.events;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

public class SimpleMRAppMasterTest {
    public static void main(String[] args) throws Exception {
        String jobID = "job_201912012208";
        SimpleMRAppMaster simpleMRAppMaster = new SimpleMRAppMaster("Simple MRAppMaster", jobID, 5);
        YarnConfiguration conf = new YarnConfiguration(new Configuration());
        simpleMRAppMaster.serviceInit(conf);
        simpleMRAppMaster.serviceStart();

        simpleMRAppMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_KILL, jobID));
        simpleMRAppMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_INIT, jobID));
    }
}
