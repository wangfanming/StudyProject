package com.wfm.states;

import com.wfm.events.JobEventType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;

public class SimpleMRAppMaster1 extends CompositeService {
    private Dispatcher dispatcher; // 中央异步调度器
    private String jobID;

    public SimpleMRAppMaster1(String name, String jobID) {
        super(name);
        this.jobID = jobID;
    }

    @Override
    protected void serviceInit(final Configuration conf) throws Exception {
        dispatcher = new AsyncDispatcher(); //定义一个中央异步调度器
        // 注册状态机处理器
        dispatcher.register(JobEventType.class, new JobStateMachine(jobID, dispatcher.getEventHandler()));
        addService((Service) dispatcher);
        super.serviceInit(conf);
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
