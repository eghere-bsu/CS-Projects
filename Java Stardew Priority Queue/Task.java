/**
 * The Task class is the object type that holds each
 * task a player may need to complete in MyLifeInStardew.
 * Each task has TaskType, a specific priority, the hour created,
 * and a description specified in its interface. 
 * 
 * @author eghere
 */
public class Task implements TaskInterface {

    private int priority, waitingTime, hourCreated;
    private String description;
    private TaskType taskType;

    public Task(TaskType taskType, int hourCreated, String description) {
        priority = 0;
        waitingTime = 0;
        this.hourCreated = hourCreated; 
        this.taskType = taskType;
        this.description = description;
    }

    public Task(int priority, int hourCreated){
        this.priority = priority;
        this.hourCreated = hourCreated; 
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int newPriority) {
        priority = newPriority;
    }

    @Override
    public TaskInterface.TaskType getTaskType() {
        return taskType;
    }

    @Override
    public void incrementWaitingTime() {
        this.waitingTime++;
    }

    @Override
    public void resetWaitingTime() {
        this.waitingTime = 0;
        
    }

    @Override
    public int getWaitingTime() {
        return waitingTime;
    }

    public int getHourCreated() {
        return hourCreated;
    }
    
	public String getTaskDescription() {
		return description;
	}

    public int compareTo(Task task) {
        int compare = 0;
        
        //Compare priority first; if the same, compare the hour created.
        if (this.getPriority() != task.getPriority()) {
            compare = (this.getPriority() > task.getPriority() ? 1 : -1);
        } else if (this.getHourCreated() != task.getHourCreated()) {
            compare = (this.getHourCreated() < task.getHourCreated() ? 1 : -1);
        }

        return compare;
    }

    @Override
    public String toString() {
        return taskType.name() + " " + description + " at Hour: " + hourCreated + ":00";
    }
}
