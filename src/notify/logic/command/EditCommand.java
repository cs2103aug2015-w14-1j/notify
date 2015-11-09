//@@author A0130319R

package notify.logic.command;

import java.util.ArrayList;
import java.util.Stack;

import notify.DateRange;
import notify.Task;
import notify.TaskType;
import notify.logic.TaskManager;

/**
 * The EditCommand class extends the abstract class ReversibleCommand.
 * 
 * The EditCommand class is responsible for creating the Result object, corresponding
 * to the EDIT command.
 * 
 * This class contains the undo and execute method called by Logic.
 *
 * @author sadhikabilla
 *
 */

public class EditCommand extends ReversibleCommand {
	
	//These are variables that are required to store the fields of each task
	private Task oldTask;
	private TaskType taskType;
	private String taskName;
	private DateRange dateRange;
	private String category;
	private int id;
	private TaskManager manager;
		
	public EditCommand(Action commandAction, Stack<ReversibleCommand> historyStack, TaskManager manager){ 
		super(commandAction, historyStack);
		this.manager = manager;
	}
	
	
	public void addValues(String taskName, DateRange dateRange, String category, int id, TaskType taskType){
		this.oldTask = manager.getTask(id);
		this.taskName = taskName;
		this.dateRange = dateRange;
		this.category = category;
		this.id = id;
		this.taskType = taskType;
	}
	
	/**
     * This method is responsible for creating the Result object corresponding to the EDIT action.
     * It also pushes the particular action onto a historyStack, to allow it to be undone in future. 
     * 
     * This method gets the Task object to be edited by calling the updateTask method
     * of TaskManger. It then adds the Task to an ArrayList to create the Result object which is used by
     * the Logic class. 
     * 
     * If the Task object is null, the method creates a result object corresponding to the INVALID action.
     * 
     * @return 'result' object corresponding to the EDIT/INVALID action.
     */
	
	@Override
	public Result execute(){
		Result result = null;
		ArrayList<Task> list = new ArrayList<Task>();
		
		if(oldTask == null){
			
			result = new Result(Action.INVALID, list);
		
		} else{
		
		checkNull();
		Task updatedTask = manager.updateTask(id, taskName, dateRange, category, taskType);
		list.add(updatedTask);
		result = new Result(Action.EDIT, list);
		pushToStack();
		
		}
		
		return result;
	}

	 /**
	  * This method is responsible for reverting the EDIT action (i.e to previous state of the task).
	  * 
	  * This method gets the pre-edited Task object by calling the updateTask method of TaskManager.
	  * It then adds the task to an ArrayList to create the Result object.
	  * 
	  * This method is called by the UndoCommand class {@see UndoCommand#execute()}
	  * 
	  * @return 'result' object corresponding to the UNDO action.  
	  */ 	
	
	@Override
	public Result undo(){
		
		Task beforeUpdatedTask = manager.updateTask(oldTask.getTaskId() , oldTask.getTaskName(), oldTask.getDateRange() , oldTask.getCategory(), oldTask.getTaskType());
		ArrayList<Task> list = new ArrayList<Task>();
		list.add(beforeUpdatedTask);
		Result result = new Result(Action.UNDO, list);
		return result;
	}
	
	public String getTaskName() {
		
		return this.taskName;
	}
	
	public TaskType getTaskType() { 
		
		return this.taskType;
	}
	
	public String getCategory() {
		
		return this.category;
	}
	
	public DateRange getDateRange() {
		
		return this.dateRange;
	}
	
	/**
	 * This method checks if any of the parameters of the updated task are null. If null, it replaces the
	 * corresponding parameters with the parameters of the pre-edited task. 
	 * 
	 */
	
	public void checkNull(){
		
		assert id != Constants.UNASSIGNED_TASK : "Task id cannot be null";
		
		if(taskName == null){
			this.taskName = oldTask.getTaskName();
		}
		if(dateRange == null){
			this.dateRange = oldTask.getDateRange();
		}
		if(category == null){
			this.category = oldTask.getCategory();
		}
		
		if(taskType == null){
			this.taskType = oldTask.getTaskType();
		}
	}
	
	

}
