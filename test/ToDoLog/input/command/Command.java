package command;
//@author A0112156U
public interface Command {
	public String execute() ;
	public String undo();
	public boolean isUndoable();
}
