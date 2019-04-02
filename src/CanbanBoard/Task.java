package CanbanBoard;

import java.time.LocalDate;

public class Task {
    private String taskName;
    private LocalDate expDate;

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Priority priority;
    private String description;

    public Task(String taskName, LocalDate expDate, Priority priority, String description) {
        this.taskName = taskName;
        this.expDate = expDate;
        this.priority = priority;
        this.description = description;
    }

    public String toString(){
        return taskName + " - " + expDate;
    }

    public String getTaskDescription(){
        String desc = "Title: " + taskName + "\n" + "Exp. date: " + expDate + "\n" + "Priority: " + priority + "\n" + "Description: " + description;
        return desc;
    }

    public Priority getPriority(){
        return priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public String getDescription() {
        return description;
    }
}
