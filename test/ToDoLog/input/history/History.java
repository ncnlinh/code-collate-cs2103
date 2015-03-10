package history;

import java.util.LinkedList;

import command.Command;

//@Author A0112156U
public class History {
	private LinkedList<Command> _commands;
	private int _position;
	public History() {
		_commands = new LinkedList<Command>();
		_position = -1;
	}
	public boolean addCommand(Command command) {
		
		for (int i = _commands.size()-1-_position; i>0; i--) {
			_commands.removeLast();
		}
		_position ++;
		return _commands.add(command);
	}
	public Command getBackwards() throws Exception {
		if (_position >= 0) {
			return _commands.get(_position);
		} else {
			return null;
		}
	}
	public void goBackwards() throws Exception {
		if (_position >= 0) {
			_position--;
		} else {
			throw new Exception("No commands to undo!");
		}
	}
	public Command getForwards() throws Exception {
		try{
			if (_position < _commands.size()) {			
				Command toBeRedone = _commands.get(_position+1);
				return toBeRedone;
			} else {
				_position--;
				return null;
			}
		} catch (IndexOutOfBoundsException ioobe) {
			return null;
		}
	}
	public void goForwards() throws Exception {
		_position++;
		if (_position >= _commands.size()) {
			_position--;
			throw new Exception("No commands to redo!");
		}
	}
}
