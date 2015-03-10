package gui;


import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.util.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

//@Author A0111513B
public class DigitalClock {
	private final JLabel time = new JLabel();
    private final SimpleDateFormat sdf  = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    private int   currentSecond;
    private Calendar calendar;
    
    

    public JLabel getTime(){
    	time.setPreferredSize(new Dimension(700,20));
    	return time;
    }
    private void reset(){
        calendar = Calendar.getInstance();
        currentSecond = calendar.get(Calendar.SECOND);
    }
    public void start(){
    	time.setForeground(Color.WHITE);
        reset();
        Timer timer = new Timer(1000, new ActionListener(){
            public void actionPerformed( ActionEvent e ) {
                    if( currentSecond == 60 ) {
                        reset();
                    }
                    time.setText( String.format("%s:%02d", sdf.format(calendar.getTime()), currentSecond ));
                    time.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    Font font = new Font("SansSerif", Font.BOLD,18);
                    time.setFont(font);
                    currentSecond++;
                }
            });
            timer.start();
        }
}

