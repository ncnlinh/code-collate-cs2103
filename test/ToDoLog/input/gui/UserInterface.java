
//for now a listener for the textfield where you input your command is
//enough

package gui;
import java.awt.AWTException;
//import java.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import logger.Log;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.JIntellitypeException;
//import com.sun.awt.AWTUtilities;



import common.Task;
import controller.Controller;

public class UserInterface extends JFrame { 
	private static final long serialVersionUID = 5L;
	
	private static final String MSG_CANNOT_ADD_TRAYICON = "TrayIcon could not be added.";
	private static final String MSG_CANNOT_SUPPORT_SYSTEMTRAY = "SystemTray is not supported\n";
	private static final String MSG_CANNOT_LOAD_HOTKEY = "Cannot load hotkey settings.\n";
	
	/* File paths for resources
	 */
	private static final String FILEPATH_ICON_40 = "resources/icons/icon-40x40.gif";
	private static final String FILEPATH_ICON_32 = "resources/icons/icon-32x32.gif";
	private static final String FILEPATH_ICON_16 = "resources/icons/icon-16x16.gif";
	private static final String FILEPATH_FONT_BPMONO = "resources/fonts/BPmono.ttf";
	private static final String FILEPATH_FONT_OPENSANS_REGULAR = "resources/fonts/OpenSans-Regular.ttf";
	private static final String FILEPATH_FONT_OPENSANS_SEMIBOLD = "resources/fonts/OpenSans-Semibold.ttf";
	private static final String FILEPATH_PHOTO_BACKGROUND_IMAGE = "resources/photos/seagull.jpg";
	
	private static final int TABLE_PAGE_SIZE = 16;
	private static final float TABLE_FONT_SIZE = 12f;
	private static final float ENTRY_TEXT_FIELD_FONT_SIZE = 20f;
	private static final float HELP_TEXT_FONT_SIZE = 13f;
	/* These are IDs for each components to set GridBagConstraints parameters
	 * ID_SCHEDULE_TABLE and ID_FLEXIBLE_TABLE are also used for setting focus
	 * tables
	 */
	private static final int ID_TOP_PANEL = 100;
	private static final int ID_ICON_PANEL = 101;
	private static final int ID_CLOCK_PANEL = 102;
	private static final int ID_TOP_RIGHT_BUTTONS_PANEL = 103;
	private static final int ID_TABLES_HOLDER = 200;
	private static final int ID_SCHEDULE_TABLE_LABEL = 201;
	private static final int ID_FLEXIBLE_TABLE_LABEL = 202;
	private static final int ID_SCHEDULE_TABLE = 203;
	private static final int ID_FLEXIBLE_TABLE = 204;
	private static final int ID_BOTTOM_PANEL = 300;
	private static final int ID_COMMAND_ENTRY_TEXT_FIELD = 301;
	private static final int ID_HELP_TEXT_AREA = 302;
	
	private TrayIcon trayIcon;
	private JLabel backgroundLabel;
	private BufferedImage backgroundImage;
	private JLayeredPane layeredPane;
	private JLabel iconPanel;
	private URL iconUrl;
	private DigitalClock clock;
	private JLabel closeButton;
	private JLabel minimizeButton;
	
	private JLabel scheduleTableLabel;
	private JLabel flexibleTableLabel;
	private JTable focusTable;
	private JTable scheduleTable;
	private JTable flexibleTable;
	private LinkedList<Task> scheduleTableItems;
	private LinkedList<Task> flexibleTableItems;
	private JScrollPane scheduleTableScrollPane;
	private JScrollPane flexibleTableScrollPane;
	private ToDoTasksListTableModel scheduleTableModel;
	private FloatingTasksListTableModel flexibleTableModel;
	
	private JTextField commandEntryTextField;
	private JTextArea helpText;
	
	/* This indicates true if the program is first minimized. 
	 */
	private boolean isFirstMinimized;
	private boolean isInvisible = false;
	
	private static UserInterface window;
	private static Point draggingOffsetPoint;
	
	/**
	 * Launch the application.
	 */
	//@author A0111513B
	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
		commandEntryTextField.requestFocusInWindow();
	}

	//@author A0111513B
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {  
				try {
					window = new UserInterface();
					window.dispose();
					window.setVisible(true);
					window.addMouseListener(window.new ScreenDraggingMouseListener());
					window.addMouseMotionListener(window.new ScreenDraggingMouseMotionListener());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		});
	}
	
	//@author A0111513B
	public UserInterface() {
		initialize(this); 
		fillUpTheJFrame(this);
		useJIntellitype();
		makeTrayIcon(this);
		startReminderTimer();
	}
	
	//@author A0112156U
	private void useJIntellitype() {
		try {
			JIntellitype.getInstance();
			
			//register the hotkeys "Alt-B" and "Alt-N"
			JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_ALT, (int) 'B');
			JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT, (int) 'N');			
			JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
	            @Override
	            public void onHotKey(int combination) {
	            	
	            	//if "Alt-B" is pressed, then minimize or maximize the window
	                if (combination == 1)
						showOrHideWindow();
	           
                    //if "Alt-N" is pressed, then make everything except the
                    //commandTextField invisible
                    if(combination == 2){
	                	if (window.isVisible()) {
	                		hideWindowExceptCommandEntry();
	                	}
	                }
	            }       
			});
		} catch (JIntellitypeException jie) {
			Log.warn("Cannot load JIntellitype for hotkeys",jie);
			helpText.append(MSG_CANNOT_LOAD_HOTKEY);
		}
	}
	
	//@author A0112156U
	private void makeTrayIcon(JFrame userInterface) {
		//Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
        	helpText.append(MSG_CANNOT_SUPPORT_SYSTEMTRAY);
            return;
        }
        URL url = this.getClass().getClassLoader().getResource(FILEPATH_ICON_16);
        ImageIcon img = new ImageIcon(url);
        PopupMenu popup = new PopupMenu();
        trayIcon =
                new TrayIcon(img.getImage());
        SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem helpItem = new MenuItem("Help");
        MenuItem showItem = new MenuItem("Show/Hide");
        MenuItem exitItem = new MenuItem("Exit");
        helpItem.addActionListener(new HelpPopupItemActionListener());
        showItem.addActionListener(new ShowPopupItemActionListener());
        exitItem.addActionListener(new ExitPopupItemActionListener());
        
        //Add components to pop-up menu
    
        popup.add(helpItem);
        popup.add(showItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
        	Log.warn("Cannot add tray icon in system tray",e);
            helpText.append(MSG_CANNOT_ADD_TRAYICON);
        }
        isFirstMinimized = false;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	
	//@author A0111513B
	//this initialize method sets up the main frame for ToDoLog
	private void initialize(JFrame UserInterface) { 
		Controller.init();
		setIconsForApplication(UserInterface);
		setWindowParameters(UserInterface);	
	}
	
	//@author A0111513B
	private void setWindowParameters(JFrame UserInterface) {
		UserInterface.setTitle("ToDoLog");
		UserInterface.setResizable(false);
		UserInterface.setBounds(325,140,700, 610);					
		UserInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInterface.setUndecorated(true);
		UserInterface.setBackground(new Color(255,255,255,255));
		UserInterface.addWindowListener(new WindowStatusWindowListener());
		Log.info("Draw the window frame");
	}
	
	//@author A0112156U
	private void setIconsForApplication(JFrame UserInterface) {
		ArrayList<Image> images = new ArrayList<Image>();
		URL url = this.getClass().getClassLoader().getResource(FILEPATH_ICON_16);
		Image image = Toolkit.getDefaultToolkit().getImage(url);
		images.add(image);
		url = this.getClass().getClassLoader().getResource(FILEPATH_ICON_32);
		image = Toolkit.getDefaultToolkit().getImage(url);
		images.add(image);
		
		UserInterface.setIconImages(images);
		Log.info("Load icon images to UI");
	}
	
	//@author A0111513B
	//this method consists of setting the different sections within the frame of ToDoLog
	private void fillUpTheJFrame(JFrame UserInterface){
		Container contentPane = UserInterface.getContentPane();
		layeredPane = new JLayeredPane();
		contentPane.add(layeredPane);
		addBackgroundLabel(layeredPane);
		addMainPanel(layeredPane);
		Log.info("Draw all components on frame");
	}
	
	//@author A0112156U
	private void addBackgroundLabel(Container layeredPane) {
		backgroundLabel= loadBackgroundImage(FILEPATH_PHOTO_BACKGROUND_IMAGE);
		layeredPane.add(backgroundLabel,new Integer(0));
	}
	
	//@author A0112156U
	private JLabel loadBackgroundImage(String filePath) {
		try {
			URL url = this.getClass().getClassLoader().getResource(filePath);
			backgroundImage = ImageIO.read(url);
			backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
			backgroundLabel.setBounds(0,0,700, 610);
			Log.info("Loaded background image");
			return backgroundLabel;
		} catch (IOException e) {
			Log.error("Cannot load background image");
			return new JLabel();
		}
	}
	
	//@author A0111513B
	private void addMainPanel(Container layeredPane) {
		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 700, 590);
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setOpaque(false);
		createTopPanel(mainPanel);
		createTablesHolder(mainPanel);
		createBottomPanel(mainPanel); 
		layeredPane.add(mainPanel,new Integer(2));
		Log.info("Drawed main panel");
	}	
	
	//@author A0112156U
	private void createTopPanel(Container mainPanel){
		JPanel topPanel = new JPanel(new GridBagLayout());
		
		//give settings to the top panel
		topPanel.setPreferredSize(new Dimension(650,50));
		topPanel.setOpaque(false);
		
		//determine the position of top panel in main panel
		GridBagConstraints parameters;
		parameters = setParameters(ID_TOP_PANEL);
		
		//fill the top panel with the icon, clock and button
		createIcon(topPanel);
		createClockPanel(topPanel);
		createButtonPanel(topPanel);
		
		mainPanel.add(topPanel, parameters);
		Log.info("Drawed top panel");
	}
	
	//@author A0112156U
	private void createIcon(JPanel topPanel) {
		iconPanel = new JLabel();
		
		iconUrl = this.getClass().getClassLoader().getResource(FILEPATH_ICON_40);
		ImageIcon icon = new ImageIcon(iconUrl);
		iconPanel.setIcon(icon);
		
		//add iconPanel to topPanel
		GridBagConstraints parameters;
		parameters = setParameters(ID_ICON_PANEL);
		topPanel.add(iconPanel,parameters);
		Log.info("Drawed icon panel");
		
	}
	
	//@author A0111513B
	private void createClockPanel(Container topPanel){
		
		 clock = new DigitalClock();
		 GridBagConstraints clockPanelParameters = setParameters(ID_CLOCK_PANEL);
		 topPanel.add(clock.getTime(),clockPanelParameters); //clock.getTime() refers to the JLabel within DigitalClock class
		 clock.start();
		 Log.info("Drawed clock");
	}
	
	//@author A0112156U
	private void createButtonPanel(Container topPanel) {
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		//set up the close button
		closeButton = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				protected void paintComponent(Graphics g){
			        g.setColor( getBackground() );
			        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			        g.fillOval(0, 0, getWidth(), getHeight());
			        super.paintComponent(g);
			    }
				//paintComponent is used to give the button a circled-shape
			};
			
		closeButton.setBackground(new Color(242, 38, 19, 255));
		closeButton.setOpaque(false);
		closeButton.setPreferredSize(new Dimension(17,17));
		closeButton.addMouseListener(new CloseButtonMouseListener());
		
		//set up the minimize button
		minimizeButton = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
		    {
		        g.setColor( getBackground() );
		        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g.fillOval(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		
		minimizeButton.setBackground(new Color(241, 196, 15, 255));
		minimizeButton.setOpaque(false);
		minimizeButton.setPreferredSize(new Dimension(17,17));
		minimizeButton.addMouseListener(new MinimizeButtonMouseListener());
		
		
		GridBagConstraints padParameters =
				new GridBagConstraints(0,0,3,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		GridBagConstraints minimizeButtonParameters = 
				new GridBagConstraints(3,0,1,1,0.0,0.0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,10,7),0,0);
		GridBagConstraints closeButtonParameters =
				new GridBagConstraints(4,0,1,1,0.0,0.0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,10,10),0,0);
		
		//add the buttons to a button panel
		buttonPanel.add(Box.createGlue(),padParameters);
		buttonPanel.add(minimizeButton,minimizeButtonParameters);
		buttonPanel.add(closeButton,closeButtonParameters);
		buttonPanel.setOpaque(false);
		
		//add the button panel to the top panel
		GridBagConstraints ButtonPanelParameters = setParameters(ID_TOP_RIGHT_BUTTONS_PANEL);
		topPanel.add(buttonPanel,ButtonPanelParameters);
		Log.info("Drawed button panel");
	}
	
	//@author A0111513B
	private void createTablesHolder(Container mainPanel){
		GridBagConstraints panelParameters;   
		panelParameters = setParameters(ID_TABLES_HOLDER); 
		//panelParameters are values for how the toDoListHolder panel will fit into the main 
		//frame of ToDoLog
		
		JPanel toDoListHolder = new JPanel(new GridBagLayout());
		toDoListHolder.setPreferredSize(new Dimension(650, 310));
		
		//add scheduleTable and flexibleTable into the toDoListHolder
		createScheduleTableLabel(toDoListHolder);
		createScheduleTable(toDoListHolder);
		createFlexibleTableLabel(toDoListHolder);
		createFlexibleTable(toDoListHolder);
		
		//add the toDoListHolder to the mainPanel
		mainPanel.add(toDoListHolder, panelParameters);
		toDoListHolder.setOpaque(false);
		Log.info("Drawed all tables");
		
	}
	
	//@author A0112156U
	private void createFlexibleTableLabel(JPanel toDoListHolder) {
		flexibleTableLabel = new JLabel("Flexible tasks:") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			protected void paintComponent(Graphics g)
		    {	
				int arc = 5;
		        g.setColor( getBackground() );
		        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        super.paintComponent(g);
		    }
		};
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILEPATH_FONT_OPENSANS_SEMIBOLD);
		try {
			Font font;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			Font sizedFont = font.deriveFont(HELP_TEXT_FONT_SIZE);
			flexibleTableLabel.setFont(sizedFont);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Border paddingBorder = BorderFactory.createEmptyBorder(4,4,4,4);
		Border border = BorderFactory.createMatteBorder(3, 3, 0, 3, Color.GRAY);
		Border compoundBorder = BorderFactory.createCompoundBorder(border, paddingBorder);
		flexibleTableLabel.setBorder(compoundBorder);
		flexibleTableLabel.setPreferredSize(new Dimension(40,25));
		flexibleTableLabel.setOpaque(false);
		flexibleTableLabel.setBackground(new Color(255,255,255,220));
		
		//add the flexibleTableLabel to the toDoListHolder
		GridBagConstraints floatingTaskListLabelParameters;
		floatingTaskListLabelParameters = setParameters(ID_FLEXIBLE_TABLE_LABEL);
		toDoListHolder.add(flexibleTableLabel,floatingTaskListLabelParameters);
		
	}

	//@author A0112156U
	private void createScheduleTableLabel(JPanel toDoListHolder) {
		scheduleTableLabel = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			protected void paintComponent(Graphics g)
		    {	
				int arc = 5;
		        g.setColor( getBackground() );
		        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        super.paintComponent(g);
		    }
		};
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILEPATH_FONT_OPENSANS_SEMIBOLD);
		try {
			Font font;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			Font sizedFont = font.deriveFont(HELP_TEXT_FONT_SIZE);
			scheduleTableLabel.setFont(sizedFont);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Border paddingBorder = BorderFactory.createEmptyBorder(4,4,4,4);
		Border border = BorderFactory.createMatteBorder(3, 3, 0, 3, new Color(247,223,124, 255));
		Border compoundBorder = BorderFactory.createCompoundBorder(border, paddingBorder);
		scheduleTableLabel.setBorder(compoundBorder);
		scheduleTableLabel.setPreferredSize(new Dimension(40,25));
		scheduleTableLabel.setOpaque(false);
		scheduleTableLabel.setBackground(new Color(255,255,255,220));
		
		//add the toDoTaskLabel to the toDoListHolder
		GridBagConstraints toDoListLabelParameters;
		toDoListLabelParameters = setParameters(ID_SCHEDULE_TABLE_LABEL);
		toDoListHolder.add(scheduleTableLabel,toDoListLabelParameters);
		
	}
	
	//@author A0111513B
	/* here is where the table displaying the user's tasks is implemented
	 * the table is formed in reference to a table model, and then placed into a scroll pane 
	 * which is then added to the toDoListHolder
	 */
	private void createScheduleTable(Container toDoListHolder) {
		//GridBagConstraints to position the scrollPane within toDoListHolder
		GridBagConstraints scrollPaneParameters; 
		scrollPaneParameters = setParameters(ID_SCHEDULE_TABLE);
		
		/* a Table Model is used to adjust the table information for ToDoLog
		 */
		scheduleTableModel = new ToDoTasksListTableModel(scheduleTableItems);
		scheduleTable = new JTable(scheduleTableModel);
		scheduleTable.setPreferredSize(new Dimension(450,280));
		//fix the width of the columns of the table and color the rows
		//according to the different due dates of tasks
		adjustToDoTaskTableColumns(scheduleTable);
		
		scheduleTable.getTableHeader().setResizingAllowed(false);
		scheduleTable.getTableHeader().setBackground(new Color(0,0,0,0));
		scheduleTable.getTableHeader().setReorderingAllowed(false);
        
        //get rid of the usual grid in the JTable
		scheduleTable.setShowGrid(false);
		scheduleTable.setIntercellSpacing(new Dimension(0, 0));
		scheduleTable.setOpaque(false);
		scheduleTable.setEnabled(false);
		
		focusTable = scheduleTable;
		((DefaultTableCellRenderer)scheduleTable.getDefaultRenderer(Object.class)).setOpaque(false);
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILEPATH_FONT_OPENSANS_REGULAR);
		try {
			Font font;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			Font sizedFont = font.deriveFont(TABLE_FONT_SIZE);
			scheduleTable.setFont(sizedFont);
			scheduleTable.getTableHeader().setFont(sizedFont);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        scheduleTableScrollPane = new JScrollPane(scheduleTable)

		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			protected void paintComponent(Graphics g)
		    {	
				int arc = 20;
		        g.setColor( getBackground() );
		        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        super.paintComponent(g);
		    }
		};
        //settings of the scroll pane
		scheduleTableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(247,223,124),3));
		scheduleTableScrollPane.setPreferredSize(new Dimension(450,280));
        scheduleTableScrollPane.setOpaque(false);
		scheduleTableScrollPane.setBackground(new Color(255,255,255,220));
		scheduleTableScrollPane.getViewport().setOpaque(false);
		scheduleTableScrollPane.getViewport().setBackground(new Color(255,255,255,220));
		scheduleTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		//to disable borders at the table headers and also to shift the  headings to the left
        scheduleTable.getTableHeader().setDefaultRenderer(new DefaultTableCellHeaderRenderer() {            
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(
                                                           JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                DefaultTableCellHeaderRenderer rendererComponent = (DefaultTableCellHeaderRenderer)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                rendererComponent.setBorder(null);
                rendererComponent.setHorizontalAlignment(LEFT);
                
                return rendererComponent;
            }     
        });
        scheduleTableItems = Controller.getAllTasksInViewList();
		scheduleTableModel = new ToDoTasksListTableModel(scheduleTableItems);
		scheduleTableLabel.setText(Controller.getViewOrSearchType());
		scheduleTable.setModel(scheduleTableModel);
		adjustToDoTaskTableColumns(scheduleTable);
		changeToDoTableColors(scheduleTable, new CustomRenderer());
		focusTable = scheduleTable;
		
		toDoListHolder.add(scheduleTableScrollPane,scrollPaneParameters);
	}
	
	//@author A0111513B
	//implementation is about the same as toDoTaskTable
	private void createFlexibleTable(JPanel toDoListHolder) {
		
		//set position of folatingTaskTable in toDoListHolder
		GridBagConstraints floatingTaskListParameters; 
		floatingTaskListParameters = setParameters(ID_FLEXIBLE_TABLE);
		
		//use a table model to set the table
		flexibleTableModel = new FloatingTasksListTableModel(flexibleTableItems);
		flexibleTable = new JTable(flexibleTableModel);
		flexibleTable.setPreferredSize(new Dimension(180,270));
		//adjust the columns and rows
		adjustFloatingTaskTableColumns(flexibleTable);
		
		flexibleTable.getTableHeader().setResizingAllowed(false);
		flexibleTable.getTableHeader().setBackground(new Color(0,0,0,0));
		flexibleTable.getTableHeader().setReorderingAllowed(false);
		flexibleTable.getTableHeader().setDefaultRenderer(new DefaultTableCellHeaderRenderer() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
            		boolean isSelected, boolean hasFocus, int row, int column) {
                DefaultTableCellHeaderRenderer rendererComponent = 
                		(DefaultTableCellHeaderRenderer)super.getTableCellRendererComponent(table, value, 
                				isSelected, hasFocus, row, column);
                rendererComponent.setBorder(null);
                rendererComponent.setHorizontalAlignment(LEFT);
                
                return rendererComponent;
            }
            
        });
		
		//get rids of the normal grid that tables have
		flexibleTable.setOpaque(false);
		flexibleTable.setEnabled(false);
		flexibleTable.setShowGrid(false);
		flexibleTable.setIntercellSpacing(new Dimension(0, 0));
		((DefaultTableCellRenderer)flexibleTable.getDefaultRenderer(Object.class)).setOpaque(false);
		
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILEPATH_FONT_OPENSANS_REGULAR);
		try {
			Font font;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			Font sizedFont = font.deriveFont(TABLE_FONT_SIZE);
			flexibleTable.setFont(sizedFont);
			flexibleTable.getTableHeader().setFont(sizedFont);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		flexibleTableScrollPane = new JScrollPane(flexibleTable)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g)
		    {	
				int arc = 20;
		        g.setColor( getBackground() );
		        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        super.paintComponent(g);
		    }
		};
		
		flexibleTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		flexibleTableScrollPane.setPreferredSize(new Dimension(180,270));
		flexibleTableScrollPane.setOpaque(false);
		flexibleTableScrollPane.setBackground(new Color(255,255,255,220));
		flexibleTableScrollPane.getViewport().setOpaque(false);
		flexibleTableScrollPane.getViewport().setBackground(new Color(255,255,255,220));
		flexibleTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		flexibleTableItems = Controller.getFlexibleList();
		flexibleTableModel = new FloatingTasksListTableModel(flexibleTableItems);
		flexibleTable.setModel(flexibleTableModel);
		adjustFloatingTaskTableColumns(flexibleTable);
		changeFloatingTableColors(flexibleTable, new CustomRenderer());
		toDoListHolder.add(flexibleTableScrollPane,floatingTaskListParameters);
		
	}

	//@author A0111513B
	/* this method creates the bottom section of ToDoLog which consists of the command entry
	 *	line and the dynamic help text area
	 */
	private void createBottomPanel(Container mainPanel){
		
		//settings of the bottom panel
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		bottomPanel.setBackground(new Color(255,255,255,0));
		bottomPanel.setPreferredSize(new Dimension(650,170));
		bottomPanel.setOpaque(true);
		
		//position of the bottom panel within the main panel
		GridBagConstraints parameters;
		parameters = setParameters(ID_BOTTOM_PANEL);
		
		createCommandEntryTextField(bottomPanel);
		createHelpTextArea(bottomPanel);

		mainPanel.add(bottomPanel, parameters);
		Log.info("Drawed bottom panel");
	}
	
	//@author A0111513B
	private void createCommandEntryTextField(JPanel bottomPanel) {
		
		//position of the commandEntryTextField within bottom panel
		GridBagConstraints bottomPanelParameters;
		bottomPanelParameters = setParameters(ID_COMMAND_ENTRY_TEXT_FIELD);
		
		commandEntryTextField = new JTextField(20){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g){
				int arc = 10;
		        g.setColor( getBackground() );
		        ((Graphics2D) g).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        super.paintComponent(g);
			}
		};
		bottomPanel.add(commandEntryTextField,bottomPanelParameters);
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILEPATH_FONT_BPMONO);
		
		try {
			Font font;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			Font sizedFont = font.deriveFont(ENTRY_TEXT_FIELD_FONT_SIZE);
			commandEntryTextField.setFont(sizedFont);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		commandEntryTextField.setBorder(new LineBorder(new Color(34,167,247),2,false));
		commandEntryTextField.setFocusTraversalKeysEnabled(false);
		
		//actionListener to take in the user's input and keylistener to enable features
		//such as flipping through pages of the user's tasks, getting the previous user 
		//input, and changing focus between the toDoTaskTable and floatingTaskTable
		commandEntryTextField.addActionListener(new CommandEntryTextFieldActionListener());
		commandEntryTextField.addKeyListener(new CommandEntryTextFieldKeyListener());
		commandEntryTextField.getDocument().addDocumentListener(new CommandEntryTextFieldDocumentListener());
		commandEntryTextField.getDocument().putProperty("name", "Text Field");
		Log.info("Drawed entry text field");
	}
	
	//@author A0111513B
	private void createHelpTextArea(JPanel bottomPanel){
		
		//position of the dynamicHelpText within the bottomPanel
		GridBagConstraints helpTextParameters;
		helpTextParameters = setParameters(ID_HELP_TEXT_AREA);
		
		//characterize the text area box into the bottom panel
		helpText = new JTextArea(5,33);
		helpText.setMaximumSize(helpText.getSize());
		helpText.setBorder(new LineBorder(Color.GRAY));
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(false);
		helpText.setEditable(false);
		helpText.setFocusable(false);
		helpText.setHighlighter(null);
		helpText.append(Controller.getFeedback());
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILEPATH_FONT_OPENSANS_REGULAR);
		try {
			Font font;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			Font sizedFont = font.deriveFont(HELP_TEXT_FONT_SIZE);
			helpText.setFont(sizedFont);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//put the dynamic area into a scroll pane
		bottomPanel.add(helpText,helpTextParameters);
		Log.info("Drawed help text area");
	
	}	
	
	/*we use JIntellitype to enable the use of hotkeys to enable features like minimizing
	 * and maximizing, and to hide the application and leave the commandTextField visible
	 */
	
	//@author A0111513B
	// remember to write unit test as you code
	private class CommandEntryTextFieldActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			
			String commandString = commandEntryTextField.getText();
			//for the case where user exits ToDoLog
			if (commandString.equalsIgnoreCase("exit")) {
				exitProgram();
			}
			
			//this is where ToDoLog takes in the user's value
			Controller.acceptUserCommand(commandString);
			commandEntryTextField.setText("");
			
			//feedback is sent to the dynamicHelpText to assist the user in using ToDoLog
			helpText.setText(Controller.getFeedback());	
			scheduleTableItems = Controller.getAllTasksInViewList();
			flexibleTableItems = Controller.getFlexibleList();
			
			//to inform the user which sets of tasks ToDoLog will be displaying
			scheduleTableLabel.setText(Controller.getViewOrSearchType());
			
			//constantly changes the table model and updates the changes
			scheduleTableModel.setTableData(scheduleTableItems);
			scheduleTableModel.fireTableDataChanged();
			flexibleTableModel.setTableData(flexibleTableItems);
			flexibleTableModel.fireTableDataChanged();
			//I want the toDoListItems to show the previous screen if the next screen has no more items to display
			flipPages();
		}
	}
	
	//@author A0111513B
	private void flipPages() {
		if (focusTable == scheduleTable) {
			if (Controller.getFocusTask() == null) {
				((ToDoLogTableModel) focusTable.getModel()).goToPage(0);
				return;
			}
			Task focusTask = Controller.getFocusTask();
			//boolean found = false;
			for (int index = 0; index < scheduleTableItems.size(); index ++){
				Task task = scheduleTableItems.get(index);
				if (task == focusTask) {
					
					((ToDoLogTableModel) focusTable.getModel()).goToPage((index)/TABLE_PAGE_SIZE);
				//	found = true;
					
				}
			}
		} else {
			if (Controller.getFocusTask() == null) {
				((ToDoLogTableModel) focusTable.getModel()).goToPage(0);
				return;
			}
			Task focusTask = Controller.getFocusTask();
			//boolean found = false;
			for (int index = 0; index < flexibleTableItems.size(); index ++){
				Task task = flexibleTableItems.get(index);
				if (task == focusTask) {
					
					((ToDoLogTableModel) focusTable.getModel()).goToPage((index)/TABLE_PAGE_SIZE);
				//	found = true;

				}
			}
		}
		Log.debug("Change the pages and focus table to where the focus task is");
	}
	
	//@author A0112156U
	//this method highlights the table which is in focus by the user. the table focused on
	// has a yellow border surrounding it
	private void setFocusTable(int tableIndex) {
		Border paddingBorder = BorderFactory.createEmptyBorder(4,4,4,4);
		Border grayBorder = BorderFactory.createMatteBorder(3, 3, 0 ,3, Color.GRAY);
		Border yellowBorder = BorderFactory.createMatteBorder(3, 3, 0 ,3, new Color(247,223,124,255));
		Border compoundGrayBorder = BorderFactory.createCompoundBorder(grayBorder, paddingBorder);
		Border compoundYellowBorder = BorderFactory.createCompoundBorder(yellowBorder, paddingBorder);
		
		switch (tableIndex) {
			case ID_SCHEDULE_TABLE: 
				if (!isInvisible) {
					flexibleTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, false));
					flexibleTableLabel.setBorder(compoundGrayBorder);

					scheduleTableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(247,223,124,255), 3, false));
					scheduleTableLabel.setBorder(compoundYellowBorder);
				}
				focusTable = scheduleTable;
				break;
			case ID_FLEXIBLE_TABLE: 

				if (!isInvisible) {
					scheduleTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, false));
					scheduleTableLabel.setBorder(compoundGrayBorder);

					flexibleTableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(247,223,124,255), 3, false));
					flexibleTableLabel.setBorder(compoundYellowBorder);
				}
				focusTable = flexibleTable;
				break;
			default: break;
		}
	}
	
	//@author A0112156U
	private void toggleFocusTable() {
		if (focusTable == scheduleTable) {
			setFocusTable(ID_FLEXIBLE_TABLE);
			Log.info("Focus table toggled to flexi");
		} else {
			setFocusTable(ID_SCHEDULE_TABLE);
			Log.info("Focus table toggled to schedule");
		}
	}
	
	//@author A0112156U
	public class ExitPopupItemActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			exitProgram();
		}
	}
	
	//@author A0112156U
	public class ShowPopupItemActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			showOrHideWindow();
		}

	}

	//@author A0112156U
	public class HelpPopupItemActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Controller.acceptUserCommand("help");
			Log.info("Open help screen");
		}

	}
	
	//@author A0112156U
	private class CommandEntryTextFieldDocumentListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			String helperText = readKeyForHelperFeedback();
			helpText.setText(helperText);
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			String helperText = readKeyForHelperFeedback();
			helpText.setText(helperText);
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			
		}
		
		private String readKeyForHelperFeedback() {
			String commandString = commandEntryTextField.getText();
			LinkedList<String> entryHelper = Controller.getCommandEntryHelperDetailsFromInput(commandString);
			String helperText = helpText.getText();
			if (!entryHelper.isEmpty()) {
				helperText += "\n";
				helperText = UIFeedbackHelper.createCmdHelpText(entryHelper);
				switch (UIFeedbackHelper.getProcessingTaskType()) {
					case FLOATING:
						setFocusTable(ID_FLEXIBLE_TABLE);
						break;
					case DEADLINE:
					case TIMED:
						setFocusTable(ID_SCHEDULE_TABLE);
						break;
					default:
						break;		
				}
				flipPages();
			} 
			Log.debug("Text change in command entry text");
			return helperText;
		}
	}
	
	//@author A0111513B
	private class CommandEntryTextFieldKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			//press the pageup and pagedown buttons to look through the pages of
			//the user's tasks
			if ((keyCode == KeyEvent.VK_PAGE_UP) || (keyCode == KeyEvent.VK_F9)){
				((ToDoLogTableModel) focusTable.getModel()).pageUp();
				Log.info("Page up pressed");
			}
			if ((keyCode == KeyEvent.VK_PAGE_DOWN) || (keyCode == KeyEvent.VK_F10)){
				((ToDoLogTableModel) focusTable.getModel()).pageDown();
				Log.info("Page down pressed");
			}
			
			//press the arrow keys to look through past user inputs
			if (keyCode == KeyEvent.VK_UP){
				try {
					commandEntryTextField.setText(Controller.getInput().getBackwards());
					Log.info("Up pressed");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			if (keyCode == KeyEvent.VK_DOWN){
				try {
					commandEntryTextField.setText(Controller.getInput().getForwards());
					Log.info("Down pressed");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			//press tab to switch focus between the two tables
			if ((keyCode == KeyEvent.VK_TAB)) {
				toggleFocusTable();
				Log.info("Tab pressed");
			}
		}
	
		@Override
		public void keyReleased(KeyEvent e) {
			
		}
			
		@Override
		public void keyTyped(KeyEvent e){

		}
	}
	
	//@author A0111513B
	/*the two mouseListener classes below enable the user to drag ToDoLog around 
	 *the computer screen
	 */
	private class ScreenDraggingMouseListener implements MouseListener {
	
			@Override
			public void mousePressed(final MouseEvent e) {
				
				draggingOffsetPoint = new Point();
				draggingOffsetPoint.setLocation(e.getPoint());
				Log.info("Start dragging");
			}
			
			@Override
			public void mouseReleased(final MouseEvent e){
				
			}
			
			@Override
			public void mouseClicked(final MouseEvent e){
				
			}
			
			@Override
			public void mouseEntered(final MouseEvent e){
				
			}
			
			@Override
			public void mouseExited(final MouseEvent e){
				
			}
	}
	
	//@author A0111513B
	private class ScreenDraggingMouseMotionListener implements MouseMotionListener{
        @Override
        public void mouseDragged(final MouseEvent e) {
            window.setLocation(e.getXOnScreen()-draggingOffsetPoint.x, e.getYOnScreen()-draggingOffsetPoint.y);
        }
		
        @Override
        public void mouseMoved(final MouseEvent e){
        	
        }
	}
	
	//@author A0112156U
	/*listeners added to the minimize button and the close button to enable
	 *functions such as minimize and exit respectively, also the button will change
	 *its color when the mouse goes over it
	 */
	private class MinimizeButtonMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// do nothing
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			minimizeButton.setBackground(minimizeButton.getBackground().darker());
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			minimizeButton.setBackground(new Color(241, 196, 15, 255));
			repaint();
			
			window.setState(JFrame.ICONIFIED);
			Log.info("Window minimized");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			minimizeButton.setBackground(minimizeButton.getBackground().brighter());
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			minimizeButton.setBackground(new  Color(241, 196, 15, 255));
			repaint();
		}
	}
	
	//@author A0112156U
	private class CloseButtonMouseListener implements MouseListener {
	
		@Override
		public void mouseClicked(MouseEvent e) {
			// do nothing
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			closeButton.setBackground(closeButton.getBackground().darker());
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			closeButton.setBackground(new Color(242, 38, 19, 255));
			repaint();
			showOrHideWindow();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			closeButton.setBackground(closeButton.getBackground().brighter());
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			closeButton.setBackground(new Color(242, 38, 19, 255));
			repaint();
		}
	}
	
	//@author A0112156U
	// a pop-up to tell the user that ToDoLog has been minimized
	private class WindowStatusWindowListener implements WindowListener{
		public void windowActivated(WindowEvent e) {
			commandEntryTextField.requestFocusInWindow();
			Log.info("Window activated");
		}

		@Override
		public void windowClosed(WindowEvent e) {
			Log.info("Window closed");
		}

		@Override
		public void windowClosing(WindowEvent e) {
			Log.info("Window closing");
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			Log.info("Window deactivated");
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			Log.info("Window maximized");
		}

		@Override
		public void windowIconified(WindowEvent e) {
			Log.info("Window minimized");
			if (!isFirstMinimized) {
				trayIcon.displayMessage("ToDoLog", 
					"ToDoLog is minimized. To open use combination ALT+B", TrayIcon.MessageType.INFO);
				isFirstMinimized = true;
				Log.info("Tray message for first minimized shown");
			}
			
		}
		
		@Override
		public void windowOpened(WindowEvent e) {
			Log.info("Window opened");
		}
	}
	
	//@author A0111513B
	/*this method is to set up the parameters of the gridbagconstraints
	to put the different panels into the right positions on the JFrame
	here we use the constructor GridBagConstraints(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,insets,ipadx,ipady)
	*/
	private GridBagConstraints setParameters(int panelParameters){
		GridBagConstraints parameters;
		Insets topPanelInsets = new Insets(0,0,0,0);
		Insets bottomPanelInsets = new Insets(0,0,0,0);
		Insets buttonPanelInsets = new Insets(0,0,0,0);
		Insets iconInsets = new Insets(10,30,0,0);
		Insets clockInsets = new Insets(15,0,0,53);
		Insets toDoListHolderInsets = new Insets(10,0,0,0);
		Insets toDoTableInsets = new Insets(0,0,0,0);
		Insets floatingTasksTableInsets = new Insets(0,10,0,0);
		Insets commandEntryTextFieldInsets = new Insets(10,25,5,25);
		Insets dynamicHelpTextInsets = new Insets(10,25,10,20);
		Insets toDoTableLabelInsets= new Insets(0,0,0,205);
		Insets floatingTasksTableLabelInsets= new Insets(0,10,0,60);
		if(panelParameters == ID_CLOCK_PANEL){
			parameters = new GridBagConstraints(1,0,1,1,0.1,0.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,clockInsets,0,0);
			return parameters;
		}
		else if(panelParameters == ID_TABLES_HOLDER){
			parameters = new GridBagConstraints(0,1,3,4,0.1,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,toDoListHolderInsets,0,0);
			return parameters;
		}
		else if(panelParameters == ID_TOP_PANEL){
			parameters = new GridBagConstraints(0,0,1,1,0.1,0.0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,topPanelInsets,0,0);
			return parameters;
		}
		else if(panelParameters == ID_TOP_RIGHT_BUTTONS_PANEL){
			parameters = new GridBagConstraints(2,0,1,1,0.2,0.0,GridBagConstraints.NORTHEAST,GridBagConstraints.BOTH,buttonPanelInsets,0,0);
			return parameters;
		}
		else if(panelParameters == ID_ICON_PANEL){
			parameters = new GridBagConstraints(0,0,1,1,0.2,0.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,iconInsets,0,0);
			return parameters;
		}
		else if(panelParameters == ID_BOTTOM_PANEL){
			parameters = new GridBagConstraints(0,5,3,3,0.0,0.3,GridBagConstraints.CENTER,GridBagConstraints.BOTH,bottomPanelInsets,0,0);
			return parameters;
		}
		
		
		else if(panelParameters == ID_COMMAND_ENTRY_TEXT_FIELD){
			parameters = new GridBagConstraints(0,0,3,1,0.1,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,commandEntryTextFieldInsets,0,0);
			
			return parameters;
		}
		
		else if(panelParameters == ID_HELP_TEXT_AREA){
			parameters = new GridBagConstraints(0,1,3,1,0.0,0.1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,dynamicHelpTextInsets,0,0);
			return parameters;
		}
		
		else if(panelParameters == ID_SCHEDULE_TABLE){
			parameters = new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,toDoTableInsets,0,0);
			return parameters;
		} else if (panelParameters == ID_FLEXIBLE_TABLE) {
			parameters = new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.BOTH,floatingTasksTableInsets,0,0);
			return parameters;
		} else if (panelParameters == ID_SCHEDULE_TABLE_LABEL) {
			
			parameters = new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,toDoTableLabelInsets,0,0);
			return parameters;
		} else if (panelParameters == ID_FLEXIBLE_TABLE_LABEL) {
			parameters = new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.BOTH,floatingTasksTableLabelInsets,0,0);
			return parameters;
		}
		return null;
	} 
	
	//@author A0111513B
	/*	adjust and fix the width of the columns of the toDoTaskTable
	 */	
	private void adjustToDoTaskTableColumns(JTable toDoListTable){
		TableColumn tableColumn = null;
		
		for(int columnHeaders = 0; columnHeaders < 7;columnHeaders++){
			tableColumn = toDoListTable.getColumnModel().getColumn(columnHeaders);
			
			switch(columnHeaders){
			case 0:
				tableColumn.setPreferredWidth(30);
				break;
			case 1:
				tableColumn.setPreferredWidth(130);
				break;
			case 2:
				tableColumn.setPreferredWidth(150);
				break;
			case 3:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				break;
			case 4:
				tableColumn.setPreferredWidth(90);
				break;
			case 5:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				break;
			case 6:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				tableColumn.setWidth(0);
				break;
			}
		}
	}
	
	//@author A0111513B
	//adjust the width of the floatingTaskTable's columns
	private void adjustFloatingTaskTableColumns(JTable floatingListTable){
		TableColumn tableColumn = null;
		
		for(int columnHeaders = 0; columnHeaders < 7;columnHeaders++){
			tableColumn = floatingListTable.getColumnModel().getColumn(columnHeaders);
			
			switch(columnHeaders){
			case 0:
				tableColumn.setPreferredWidth(35);
				break;
			case 1:
				tableColumn.setPreferredWidth(145);
				break;
			case 2:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				tableColumn.setWidth(0);
				break;
			case 3:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				tableColumn.setWidth(0);
				break;
			case 4:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				tableColumn.setWidth(0);
				break;
			case 5:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				tableColumn.setWidth(0);
				break;
			case 6:
				tableColumn.setPreferredWidth(0);
				tableColumn.setMinWidth(0);
				tableColumn.setMaxWidth(0);
				tableColumn.setWidth(0);
				break;
			}
		}
	}
	
	//@author A0111513B
	//a renderer to change the color of the rows of the table to show different priorities
	private void changeToDoTableColors(JTable toDoListTable, DefaultTableCellRenderer renderer){
		toDoListTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		toDoListTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		toDoListTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
		toDoListTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
		toDoListTable.getColumnModel().getColumn(4).setCellRenderer(renderer);	
	}
	
	//@author A0111513B
	private void changeFloatingTableColors(JTable toDoListTable, DefaultTableCellRenderer renderer){
		toDoListTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		toDoListTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
	}
	
	//@author A0111513B
	private void hideWindowExceptCommandEntry() {
		if(isInvisible == false){
			commandEntryTextField.setBorder(new LineBorder(new Color(34,167,240),4,true));
			backgroundLabel.setIcon(null);
			iconPanel.setIcon(null);
			minimizeButton.setVisible(false);
			closeButton.setVisible(false);
			window.setBackground(new Color(255,255,255,20));

			//hide dynamicHelpText
			
			helpText.setBackground(new Color(255,255,255,0));
			helpText.setBorder(null);
			helpText.setForeground(new Color(255,255,255,0));
			
			scheduleTableLabel.setBackground(new Color(255,255,255,20));
			scheduleTableLabel.setForeground(new Color(255,255,255,20));
			scheduleTableLabel.setBorder(null);
			
			flexibleTableLabel.setBackground(new Color(255,255,255,20));
			flexibleTableLabel.setForeground(new Color(255,255,255,20));
			flexibleTableLabel.setBorder(null);
			
			scheduleTableScrollPane.setBackground(new Color(255,255,255,20));
			scheduleTableScrollPane.setBorder(null);
			scheduleTable.setForeground(new Color(255,255,255,20));
			scheduleTable.getTableHeader().setForeground(new Color(255,255,255,20));
			changeToDoTableColors(scheduleTable,new InvisibleRenderer());
			flexibleTableScrollPane.setBackground(new Color(255,255,255,20));
			flexibleTableScrollPane.setBorder(null);
			flexibleTable.setForeground(new Color(255,255,255,20));
			flexibleTable.getTableHeader().setForeground(new Color(255,255,255,20));
			changeFloatingTableColors(flexibleTable,new InvisibleRenderer());
			clock.getTime().setForeground(new Color(255,255,255,20));
			
			isInvisible = true;
			Log.info("Set into stealth mode");
		}else{
                //make everything reappear with the same hotkey
			commandEntryTextField.setBorder(new LineBorder(new Color(34,167,240),2,true));
			backgroundLabel.setIcon(new ImageIcon(backgroundImage));
			iconPanel.setIcon(new ImageIcon(iconUrl));
			minimizeButton.setVisible(true);
			closeButton.setVisible(true);
			window.setBackground(new Color(255,255,255,255));

			//hide dynamicHelpText
			helpText.setBackground(new Color(255,255,255,255));
			helpText.setBorder(new LineBorder(Color.GRAY));
			helpText.setForeground(new Color(0,0,0,255));
			
			scheduleTableScrollPane.setBackground(new Color(255,255,255,220));
			
			
			scheduleTableLabel.setBackground(new Color(255,255,255,220));
			scheduleTableLabel.setForeground(new Color(0,0,0,255));
			
			flexibleTableLabel.setBackground(new Color(255,255,255,220));
			flexibleTableLabel.setForeground(new Color(0,0,0,255));
          
			scheduleTable.setForeground(Color.BLACK);
			scheduleTable.getTableHeader().setForeground(Color.BLACK);
			flexibleTableScrollPane.setBackground(new Color(255,255,255,220));
			flexibleTable.setForeground(Color.BLACK);
			flexibleTable.getTableHeader().setForeground(Color.BLACK);
			clock.getTime().setForeground(Color.WHITE);
			changeToDoTableColors(scheduleTable,new CustomRenderer());
			changeFloatingTableColors(flexibleTable,new CustomRenderer());
			isInvisible = false;
			toggleFocusTable();toggleFocusTable();
			Log.info("Exit stealth mode");
		}
	}

	//@author A0111513B
	private void showOrHideWindow() {
		if (window.getState() == JFrame.ICONIFIED) {
			window.setState(JFrame.NORMAL);
			window.setVisible(true);
			
		} else {
			if (isInvisible) {
				hideWindowExceptCommandEntry();
			}
			window.setState(JFrame.ICONIFIED);
			window.dispose();
			
		}
	}

	//@author A0118899E
	private void startReminderTimer(){
		final Timer timer = new Timer( 60000 ,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						ReminderLogic reminder = new ReminderLogic();
						reminder.execute();
					}
				});
		timer.setInitialDelay(0);
		timer.start();
		
	}

	//@author A0112156U
	private void exitProgram() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
		Log.info("Exit program");
	}
}
