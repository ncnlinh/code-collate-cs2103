package storage;

import java.io.IOException;
import java.util.LinkedList;

import common.Task;
/**
 * DBStorage objects implement Storage interface
 * stores Task in LinkedList and returns when load() is called
 * @param _task
 * @param _fileStorage
 */
//@author A0112156U
public class DBStorage implements Storage {
	private LinkedList<Task> _tasks;
	private FileStorage _fileStorage;
	public DBStorage() {
		_fileStorage = new FileStorage();
		init();
	}

	public DBStorage(String fileName) {
		_fileStorage = new FileStorage(fileName);
		init();
	}

	public LinkedList<Task> load() {
		return _tasks;
	}

	public FileStorage getFileStorage(){
		return _fileStorage;
	}
	@Override
	public void init() {
		_tasks = new LinkedList<Task>();
		_tasks = _fileStorage.load();
	}

	@Override
	public void store(LinkedList<Task> tasks) throws IOException {
		_tasks = tasks;
		_fileStorage.store(tasks);
	}
	

}
