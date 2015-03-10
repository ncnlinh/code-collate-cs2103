package history;

import java.util.LinkedList;

//@Author A0111608R
public class InputHistory {
	private LinkedList<String> _strings;
	private int _position;
	public InputHistory() {
		_strings = new LinkedList<String>();
		_position = -1;
	}
	
	public boolean addInput (String string) {
		_position = _strings.size();
		return _strings.add(string);
	}
	public String getBackwards() throws Exception {
		if (_position >= 0) {
			String toBeReShown = _strings.get(_position);
			_position--;
			return toBeReShown;
		} else {
			if (_strings.size() == 0) {
				return "";
			} else {
				return _strings.get(0);
			}
		}
	}
	public void goBackwards() throws Exception {
		if (_position >= 0) {
			_position--;
		} else {
			throw new Exception("No commands to undo!");
		}
	}
	public String getForwards() throws Exception {
		if (_position < _strings.size()-1) {
			String toBeReShown = _strings.get(_position+1);
			_position++;
			return toBeReShown;
		} else {
			return null;
		}
	}
	public void goForwards() throws Exception {
		_position++;
		if (_position >= _strings.size()) {
			_position--;
			throw new Exception("No commands to redo!");
		}
	}
	
}
