package command;

import java.util.LinkedList;

import common.Task;

import controller.Controller;
import storage.DBStorage;

/**
 *  CommandSearch class is called by the controller in order to execute the search command
 *  @param _storage - stores the DBstorage to get the storageList
 *	@param _storageList - stores the complete list of tasks
 *	@param _returnList - stores the list which has all the tasks that contain the search key
 *	@param _searchKey - has the search element that has to be searched.
 */

public class CommandSearch implements Command {
	private static String _searchKey;
	private static LinkedList<Task> _returnList;
	private static DBStorage _storage;
	private static LinkedList<Task> _storageList;
	
	private static final String FEEDBACK_VALID_SEARCH = "Searching for \"%1$s\" is completed";
	private static final String FEEDBACK_INVALID_UNDO = "Search cannot be undone";
 	
	//@Author A0118899E
	public CommandSearch(String searchKey) {
		_searchKey = searchKey;
		_storage = Controller.getDBStorage();
		_storageList = _storage.load();
 	}
	
	//@Author A0118899E
	public String getSearchKey() {
		return _searchKey;
	}
	
	//@Author A0118899E
	private void setReturnList(LinkedList <Task> list) {
		_returnList = list;
	}
	
	//@Author A0118899E
	public LinkedList<Task> getReturnList() {
		return _returnList;
	}
	
	//@Author A0118899E
	@Override
	public String execute() {
		String feedback;
		Controller.setFocusTask( null ); // set focus task to change UI's page
		searchName(_searchKey);
		feedback = String.format(FEEDBACK_VALID_SEARCH, _searchKey);
		return feedback;
	}
	
	//@Author A0118899E
	public void searchName(String searchKey) {
		LinkedList<Task> searchList = new LinkedList<Task>();
		for (int i = 0; i < _storageList.size(); i++ ) {
			if (_storageList.get(i).getTaskName().toUpperCase().contains(searchKey.toUpperCase())) {
				searchList.add(_storageList.get(i));
			}
		}
		setReturnList(searchList);
	}
	
	//@Author A0118899E
	@Override
	public String undo() {
		return FEEDBACK_INVALID_UNDO;
	}
	
	//@Author A0118899E
	@Override
	public boolean isUndoable() {
		return false;
	}
}
