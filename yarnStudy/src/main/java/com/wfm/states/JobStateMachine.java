package com.wfm.states;

import com.wfm.events.JobEvent;
import com.wfm.events.JobEventType;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.*;

import java.util.EnumSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JobStateMachine implements EventHandler<JobEvent> {
    private final String jobID;
    private EventHandler eventHandler;
    private final Lock writeLock;
    private final Lock readLock;

    //定义状态机
    protected static final StateMachineFactory<JobStateMachine, JobStateInternal, JobEventType, JobEvent> stateMachineFactory
            = new StateMachineFactory<JobStateMachine, JobStateInternal, JobEventType, JobEvent>(JobStateInternal.NEW)
            .addTransition(JobStateInternal.NEW, JobStateInternal.INITED, JobEventType.JOB_INIT, new InitTransition())
            .addTransition(JobStateInternal.INITED, JobStateInternal.SETUP, JobEventType.JOB_START, new StartTransition())
            .addTransition(JobStateInternal.SETUP, JobStateInternal.RUNNING, JobEventType.JOB_SETUP_COMPLETED, new SetupCompletedTransition())
            .addTransition(JobStateInternal.RUNNING, EnumSet.of(JobStateInternal.KILLED, JobStateInternal.SUCCEEDED), JobEventType.JOB_COMPLETED, new JobTaskCompletedTransition())
            .installTopology();

    private final StateMachine<JobStateInternal, JobEventType, JobEvent> stateMachine;


    public JobStateMachine(String jobID, EventHandler eventHandler) {
        this.jobID = jobID;
        this.eventHandler = eventHandler;
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        this.eventHandler = eventHandler;
        stateMachine = stateMachineFactory.make(this);
    }

    public void handle(JobEvent event) {
        try {
            writeLock.lock();
            JobStateInternal oldState = getInternalState();
            try {
                getStateMachine().doTransition(event.getType(), event);
            } catch (InvalidStateTransitonException e) {
                System.out.println("Can't handle this event at current state");
                e.printStackTrace();
            }

            if (oldState != getInternalState()) {
                System.out.println("Job Transitioned from " + oldState + "to" + getStateMachine());
            }

        } finally {
            writeLock.unlock();
        }

    }

    public String getJobID() {
        return jobID;
    }

    public StateMachine<JobStateInternal, JobEventType, JobEvent> getStateMachine() {
        return stateMachine;
    }

    public JobStateInternal getInternalState() {
        readLock.lock();
        try {
            return getStateMachine().getCurrentState();
        } finally {
            readLock.unlock();
        }
    }

    public static class InitTransition implements SingleArcTransition<JobStateMachine, JobEvent> {

        public void transition(JobStateMachine jobStateMachine, JobEvent jobEvent) {
            System.out.println("Received event " + jobEvent);
            jobStateMachine.eventHandler.handle(new JobEvent(JobEventType.JOB_START, jobStateMachine.jobID));
        }
    }

    public static class StartTransition implements SingleArcTransition<JobStateMachine, JobEvent> {

        public void transition(JobStateMachine jobStateMachine, JobEvent jobEvent) {
            System.out.println("Received event " + jobEvent);
            jobStateMachine.eventHandler.handle(new JobEvent(JobEventType.JOB_SETUP_COMPLETED, jobStateMachine.jobID));
        }
    }

    public static class SetupCompletedTransition implements SingleArcTransition<JobStateMachine, JobEvent> {

        public void transition(JobStateMachine jobStateMachine, JobEvent jobEvent) {
            System.out.println("Received event " + jobEvent);
            jobStateMachine.eventHandler.handle(new JobEvent(JobEventType.JOB_COMPLETED, jobStateMachine.jobID));
        }
    }

    public static class JobTaskCompletedTransition implements MultipleArcTransition<JobStateMachine, JobEvent, JobStateInternal> {
        public JobStateInternal transition(JobStateMachine jobStateMachine, JobEvent jobEvent) {
            System.out.println("Received event " + jobEvent);
            return JobStateInternal.SUCCEEDED;
        }
    }

    public enum JobStateInternal { //作业内部状态
        NEW,
        SETUP,
        INITED,
        RUNNING,
        SUCCEEDED,
        KILLED
    }
}
