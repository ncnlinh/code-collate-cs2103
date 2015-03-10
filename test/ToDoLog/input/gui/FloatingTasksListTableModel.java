package gui;


/*import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;*/
import javax.swing.table.*;

import common.Task;

import controller.Controller;

import java.util.*;

//@Author A0112156U
public class FloatingTasksListTableModel extends AbstractTableModel implements ToDoLogTableModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4665613032022419075L;
	/**
	 * 
	 */
	
	private final static String[] COLUMNS = {"  No.","Name","Time","Person / Venue","Done","<hidden>","<hidden>"};
	private LinkedList<Task> tableData;
	private final static int PAGE_SIZE = 16;
	private static final int NOT_DEADLINE = Integer.MIN_VALUE;
	private int pageOffSet = 0;
	
	public FloatingTasksListTableModel(LinkedList<Task> floatingTaskListItems){
		tableData = floatingTaskListItems;
	}
	
	public void setTableData(LinkedList<Task> floatingTaskListItems){
		tableData = floatingTaskListItems;
	}
	public int getColumnCount(){
		return COLUMNS.length;
	}
	
	
	public int getRowCount(){
		//return tableData.size();
		if(pageOffSet == getPageCount() -1){  
			if(tableData.size() % PAGE_SIZE == 0){
				return PAGE_SIZE;
			}
			
			else{
				return tableData.size() % PAGE_SIZE;
			}
		}
		return Math.min(PAGE_SIZE, tableData.size());
	}
	
	public int getActualRowCount(){
		return tableData.size();
	}
	
	public int getPageSize(){
		return PAGE_SIZE;
	}
	public String getColumnName(int col){
		return COLUMNS[col];
	}
	public Object getValueAt(int row,int col){
		int actualRow =  row + (pageOffSet * PAGE_SIZE);
		Task task = tableData.get(actualRow);
		
		if(task == null){
			return null;
		}
		
		switch(col){
		
		case 0:
			return " "+(actualRow+1+Controller.getNumberOfScheduledTasks());
		
		case 1: 
			return task.getTaskName();
			
		case 3: {
			String col4 = "";
			if (!task.getTaskPerson().isEmpty()) {
				col4 = col4.concat("with ").concat(task.getTaskPerson().concat(" "));
			}
			if (!task.getTaskVenue().isEmpty()) {
				col4 = col4.concat("@ ").concat(task.getTaskVenue());
			}
			return col4;
		}
		case 4:
			if(task.getTaskStatus() == true){
				return "Done";
			}
			else{
				int duePeriod = task.duePeriod();
				if (duePeriod == NOT_DEADLINE) {
					return "";
				}
				if (duePeriod < 7) {
					if (duePeriod == 0) {
						return "Due today";
					} else if (duePeriod == 1) {
						return "Due tomorrow";
					} else {
						return String.format("Due in %d days",duePeriod);
					}
				}
				return "";
				
			} 
		case 5:
			return task.duePeriod();
		case 6:
			if (task == Controller.getFocusTask()) {
				return true;
			}
			else {
				return false;
			}
		default:
			return null;
			
		}
	}
	
	public int getPageOffSet(){
		return pageOffSet;
	}
	
	public int getPageCount() {
	    return (int) Math.ceil((double) tableData.size() / PAGE_SIZE);
	}
	
	 public void pageDown() {
		    if (pageOffSet < getPageCount() - 1) {
		      pageOffSet++;
		      fireTableDataChanged();
		    }
		  }

	 public void pageUp() {
		    if (pageOffSet > 0) {
		      pageOffSet--;
		      fireTableDataChanged();
		    }
		  }

	public void goToPage(int page) {
		pageOffSet = page;
		fireTableDataChanged();
		
	}

	
}
