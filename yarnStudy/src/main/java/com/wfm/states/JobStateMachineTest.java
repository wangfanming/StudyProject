package com.wfm.states;

import com.wfm.events.JobEvent;
import com.wfm.events.JobEventType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

public class JobStateMachineTest {
    public static void main(String[] args) throws Exception {
        String jobID = "job_201912020108";
        SimpleMRAppMaster1 appMaster = new SimpleMRAppMaster1("JobStateMachineTest", jobID);
        YarnConfiguration configuration = new YarnConfiguration(new Configuration());
        appMaster.serviceInit(configuration);
        appMaster.init(configuration);
        appMaster.start();

        appMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_INIT, jobID));
    }
}
