import java.util.Random;

/** 
 * This class is an implementation of TaskGeneratorInterface.java.
 * This will generate tasks to be used in MyLifeInStardew.java.
 * 
 * @author eghere
 */
public class TaskGenerator implements TaskGeneratorInterface {
    private int currentEnergyStorage = DEFAULT_ENERGY;
    private double probability;
    Random rng = new Random();

    public TaskGenerator(double taskGenerationProbability, long seed) {
        rng = new Random(seed);
        this.probability = taskGenerationProbability;
    }

    public TaskGenerator(double taskGenerationProbability) {
        this.probability = taskGenerationProbability;
    }

    @Override
    public Task getNewTask(int hourCreated, TaskInterface.TaskType taskType, String taskDescription) {
        Task newTask = new Task(taskType, hourCreated, taskDescription);
        return newTask;
    }

    @Override
    public void decrementEnergyStorage(TaskInterface.TaskType taskType) {
        currentEnergyStorage -= taskType.getEnergyPerHour();
    }

    @Override
    public void resetCurrentEnergyStorage() {
        currentEnergyStorage = 200;
    }

    @Override
    public int getCurrentEnergyStorage() {
        return currentEnergyStorage;
    }

    @Override
    public void setCurrentEnergyStorage(int newEnergyNum) {
        currentEnergyStorage = newEnergyNum;
    }

    @Override
    public boolean generateTask() {
        if (probability > rng.nextDouble()) {
            return true;
        } 
        return false;
    }

    @Override
    public int getUnlucky(Task task, double unluckyProbability) {
        if (task.getTaskType().getPassingOutProbability() >= unluckyProbability) {
            if (task.getTaskType().getDyingProbabilityProbability() >= unluckyProbability && task.getTaskType() == TaskInterface.TaskType.MINING) {
                currentEnergyStorage /= 4;
                task.setPriority(0);
                return 2;
            } else {
                currentEnergyStorage /= 2;
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString(Task task, TaskInterface.TaskType taskType) {
        if (taskType == Task.TaskType.MINING) {
            return "     Mining " + task.getTaskDescription() + " at " + 
                currentEnergyStorage + " energy points (Priority: " + task.getPriority() + ")";
        }
        if (taskType == Task.TaskType.FISHING) {
            return "     Fishing " + task.getTaskDescription() + " at " + 
                currentEnergyStorage + " energy points (Priority: " + task.getPriority() + ")";
        }
        if (taskType == Task.TaskType.FARM_MAINTENANCE) {
            return "     Farm Maintenance " + task.getTaskDescription() + " at " + 
                currentEnergyStorage + " energy points (Priority: " + task.getPriority() + ")";
        }
        if (taskType == Task.TaskType.FORAGING) {
            return "     Foraging " + task.getTaskDescription() + " at " + 
                currentEnergyStorage + " energy points (Priority: " + task.getPriority() + ")";
        }
        if (taskType == Task.TaskType.FEEDING) {
            return "     Feeding " + task.getTaskDescription() + " at " + 
                currentEnergyStorage + " energy points (Priority: " + task.getPriority() + ")";
        }
        if (taskType == Task.TaskType.SOCIALIZING) {
            return "     Socializing " + task.getTaskDescription() + " at " + 
                currentEnergyStorage + " energy points (Priority: " + task.getPriority() + ")";
        }

        return "Nothing to see here...";
    }
    
}
