package com.wfm.events;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class TaskEvent extends AbstractEvent<TaskEventType> {
    private String taskID;

    public TaskEvent(TaskEventType taskEventType, String taskID) {
        super(taskEventType);
        this.taskID = taskID;
    }

    public String getTaskID() {
        return taskID;
    }
}
