package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

//@Author A0118899E
public class HelpFrame {

	String _text;
	
	public static final String FRAME_HEADER = "HELPER";
	public HelpFrame (String text) {
		_text = text;
	}
	
	public void execute() {
		JTextArea textArea = new JTextArea();
		textArea.setTabSize(2);
        DefaultCaret caret1 = (DefaultCaret) textArea.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
	    textArea.setLineWrap( true );
	    textArea.setWrapStyleWord( true );
	    JScrollPane scroll = new JScrollPane(textArea);
	    scroll.setBackground(Color.BLACK);
	    textArea.append(_text);
	    textArea.setForeground(Color.WHITE);
		JFrame frame = new JFrame(FRAME_HEADER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dimension = new Dimension(700,500);
        frame.setPreferredSize(dimension);
        frame.pack();
        frame.getContentPane().add(scroll, java.awt.BorderLayout.CENTER);
        textArea.setBackground(Color.DARK_GRAY);
        frame.setResizable( false );
        frame.setVisible( true );
	}
}
