package com.wfm.events;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;

public class SimpleMRAppMaster extends CompositeService {
    private Dispatcher dispatcher; // 中央异步调度器
    private String jobID;
    private int taskNumber;
    private String[] taskIDs;

    public SimpleMRAppMaster(String name, String jobID, int taskNumber) {
        super(name);
        this.jobID = jobID;
        this.taskNumber = taskNumber;
        taskIDs = new String[taskNumber];
        for (int i = 0; i < taskNumber; i++) {
            taskIDs[i] = new String(jobID + "_task_" + i);
        }
    }

    @Override
    protected void serviceInit(final Configuration conf) throws Exception {
        dispatcher = new AsyncDispatcher(); //定义一个中央异步调度器
        // 分别注册Job和Task事件调度器
        dispatcher.register(JobEventType.class, new JobEventDispatcher());
        dispatcher.register(TaskEventType.class, new TaskEventDispatcher());
        addService((Service) dispatcher);
        super.serviceInit(conf);
    }

    @Override
    public void serviceStart() throws Exception {
        super.serviceStart();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    private class JobEventDispatcher implements EventHandler<JobEvent> {

        public void handle(JobEvent event) {
            if (event.getType() == JobEventType.JOB_KILL) {
                System.out.println("Received JOB_KILL event");
                for (int i = 0; i < taskNumber; i++) {
                    dispatcher.getEventHandler().handle(new TaskEvent(TaskEventType.T_KILL, taskIDs[i]));
                }
            } else if (event.getType() == JobEventType.JOB_INIT) {
                System.out.println("Received JOB_INIT event");
                for (int i = 0; i < taskNumber; i++) {
                    dispatcher.getEventHandler().handle(new TaskEvent(TaskEventType.T_SCHEDULE, taskIDs[i]));
                }
            }
        }
    }

    private class TaskEventDispatcher implements EventHandler<TaskEvent> {

        public void handle(TaskEvent event) {
            if (event.getType() == TaskEventType.T_KILL) {
                System.out.println("Received T_KILL event of task " + event.getTaskID());
            } else if (event.getType() == TaskEventType.T_SCHEDULE) {
                System.out.println("Received T_SCHEDULE event of task " + event.getTaskID());
            }
        }
    }

}

