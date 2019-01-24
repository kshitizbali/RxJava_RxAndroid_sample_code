package com.example.rxjavademo1.model;

public class Task {

    private int id;
    private String taskName;

    public Task(int i, String s) {
        this.id = i;
        this.taskName = s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
