/**
 * Author: Kenneth Ho Chee Chong
 * Matric No: A0125364J
 * For CS2103T - Notify
 * 
 * Methods: getTasks() and it's corresponding overload.
 * 			getOverdueTasks()
 * 			getComingSoonTasks()
 * 
 * Author: Ye Kyaw Swa Aung Joshua
 * Matric No: A0124072U
 * For CS2103T - Notify
 * 
 * Constructor: TaskManager(Storage)
 * Methods: addTask(String, DateRange, String, TaskType)
 * 			deleteTask(int)
 * 			undeleteTask(int)
 * 			updateTask(int, String, DateRange, String, TaskType)
 * 			markTask(int, boolean)
 * 			searchTask(String)
 * 			exit
 * 			updatelatestId
 * 			
 */

package notify.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import notify.DateRange;
import notify.Task;
import notify.TaskType;
import notify.storage;

public class TaskManager {

	//@@author A0124072
	private int latest_id;
	private ArrayList<Task> taskList;
	private Storage storage;

	public TaskManager(Storage storage) {

		this.latest_id = 0;
		this.storage = storage;
		this.taskList = this.storage.loadTasks();
		
		updateLatestId();
		
	}

	public Task addTask(String name, DateRange timespan, String category, TaskType taskType) {

		// logger.log(Level.INFO, "ADDING TASK");

		Task task = new Task(latest_id, taskType, name, timespan, category, false);
		taskList.add(task);
		latest_id++;

		// logger.log(Level.WARNING, "TASK CANNOT BE ADDED");
		return task;
		
	}

	public Task deleteTask(int id) {

		Task task = getTask(id);

		if (task != null) {
			
			task.setDeleted(true);
			
			return task;
			
		} 

		return null;
				
	}

	public Task undeleteTask(int id) {

		Task task = getTask(id);

		if (task != null) {
			task.setDeleted(false);
			return task;
		} 

		return null;
		
	}

	public Task updateTask(int id, String newName, DateRange newDateRange, String category, TaskType newType) {

		// log.log(Level.INFO, "Updated task [{0}]", newName);

		Task task = getTask(id);

		if (task != null) {
			
			task.setTaskName(newName);
			task.setDateRange(newDateRange);
			task.setCategory(category);
			task.setTaskType(newType);

			return task;
			
		} 
		
		return null;
		
	}

	public Task markTask(int id, boolean isCompleted) {

		Task task = getTask(id);

		if (task != null) {
			
			task.setCompleted(isCompleted);
			
			return task;
			
		} else {
			
			return null;
			
		}
		
	}
	
	public ArrayList<Task> searchTask(String keyWord) {
		
		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for (Task task : taskList) {
			
			if (task.isSearchedTask(keyWord) && !task.isDeleted()) {
				
				tempList.add(task);
				
			}
			
		}
		
		Collections.sort(tempList);
		return tempList;
		
	}

	public void exit() {
		
		storage.saveTasks(taskList);
		
	}

	private void updateLatestId() {

		if (taskList == null) {

			taskList = new ArrayList<Task>();

		} else {

			if (!taskList.isEmpty()) {

				int lastTaskIndex = taskList.size() - 1;
				int lastTaskId = taskList.get(lastTaskIndex).getTaskId();
				latest_id = lastTaskId + 1;

			}

		}

	}

	public Task getTask(int taskId) {

		for (Task task: taskList) {
			
			if (task.getTaskId() == taskId) {
				
				return task;
				
			}
			
		}

		return null;
		
	}
	//@@author

	//@@author A0125364J
	/**
	 * Retrieve the task that is not deleted with the id specified or null if there are no task with the id specified.
	 * 
	 * @param taskId task id of the task to be retrieved
	 * @param isCompleted true to retrieve only completed tasks, false to retrieve only uncompleted task
	 * @return a task object of the task is found, otherwise return null
	 */
	public Task getTask(int taskId, boolean isCompleted) {

		for (Task task: taskList) {

			if (task.getTaskId() == taskId && !task.isDeleted() && task.isCompleted() == isCompleted) {

				return task;

			}

		}

		return null;

	}

	/**
	 * Retrieve all the tasks (including tasks that are deleted).
	 * 
	 * @return a list of all the tasks
	 */
	public ArrayList<Task> getTasks() {
		
		return taskList;

	}

	/**
	 * Retrieve all the tasks where its task type is equals to the task type specified.
	 * 
	 * @param taskType type of task to retrieve
	 * @param isCompleted true to retrieve only completed tasks, false to retrieve only uncompleted task
	 * @return a list of task where task type is equals to the task type specified.
	 */
	public ArrayList<Task> getTasks(TaskType taskType, boolean isCompleted) {

		ArrayList<Task> tempList = new ArrayList<Task>();

		for (Task task: taskList) {

			if (task.getTaskType() == taskType && !task.isDeleted() && task.isCompleted() == isCompleted) {

				tempList.add(task);

			}

		}

		Collections.sort(tempList);
		return tempList;

	}

	/**
	 * Retrieve all the tasks where its date falls on the date specified or the
	 * date specified is within its range.
	 * 
	 * @param date date of the task to retrieve
	 * @param isCompleted true to retrieve only completed tasks, false to retrieve only uncompleted task
	 * @return a list of task where its date falls on the date specified or the date specified is within its range.
	 */
	public ArrayList<Task> getTasks(Calendar date, boolean isCompleted) {

		ArrayList<Task> tempList = new ArrayList<Task>();

		for (Task task: taskList) {

			if (task.isOn(date) && !task.isDeleted() && task.isCompleted() == isCompleted) {

				tempList.add(task);

			}

		}

		Collections.sort(tempList);
		return tempList;

	}

	/**
	 * Retrieve all the tasks that are not deleted and its completed status
	 * matches the completed status specified.
	 * 
	 * @param isCompleted true to retrieve completed tasks. false to retrieve uncompleted tasks
	 * @return a list of completed task or uncompleted task depending on the value passed in.
	 */
	public ArrayList<Task> getTasks(boolean isCompleted) {

		ArrayList<Task> tempList = new ArrayList<Task>();

		for (Task task: taskList) {

			if (task.isCompleted() == isCompleted && !task.isDeleted()) {

				tempList.add(task);

			}

		}

		Collections.sort(tempList);
		return tempList;
	}

	/**
	 * Retrieve all the tasks that are overdue. 
	 * Overdue tasks are tasks that have its end date earlier than todays date.
	 * 
	 * @return a list of tasks that are overdue
	 */
	public ArrayList<Task> getOverdueTasks() {

		ArrayList<Task> tempList = new ArrayList<Task>();

		for (Task task: taskList) {

			if (task.isOverdue()) {

				assert task.isCompleted() == false;
				assert task.isDeleted() == false;

				tempList.add(task);

			}

		}

		Collections.sort(tempList);
		return tempList;

	}

	/**
	 * Retrieve all the tasks that are coming soon. Coming soon tasks are tasks
	 * that are not within the week (or today and up coming 6 days )
	 * 
	 * @return a list of tasks that are coming soon.
	 */
	public ArrayList<Task> getComingSoonTasks() {

		ArrayList<Task> tempList = new ArrayList<Task>();

		for (Task task: taskList) {

			if (task.isComingSoon()) {

				assert task.isCompleted() == false;
				assert task.isDeleted() == false;

				tempList.add(task);

			}

		}

		Collections.sort(tempList);
		return tempList;

	}
	//@@author
}
