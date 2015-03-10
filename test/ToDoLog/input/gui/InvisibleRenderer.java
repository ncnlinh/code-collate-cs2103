package gui;

import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.*;

//@Author A0111513B
public class InvisibleRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component cellComponent = super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);

		if (((String) table.getValueAt(row, 4)).equalsIgnoreCase("done")) {
			colorCell(cellComponent,new Color(255, 255, 255, 0));
		} else {
			colorCell(cellComponent,new Color(255, 255, 255, 0));
		} 
		if ((Boolean) table.getValueAt(row, 6) == true) {
			highlightCell(cellComponent);
		}
		return cellComponent;
	}
	private void highlightCell(Component cellComponent) {
		//Color color = Color.YELLOW;
		JPanel highlightCell = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
		    {
		        g.setColor( getBackground() );
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		((JComponent) cellComponent).setBackground(new Color(255,255,0,0));
		((JComponent) cellComponent).setOpaque(true);
		((JComponent) cellComponent).add(highlightCell);
		
	}
	
	private void colorCell(Component cellComponent, Color color) {
		JPanel colorCell = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
		    {
		        g.setColor( getBackground() );
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		((JComponent) cellComponent).setBackground(color);
		((JComponent) cellComponent).setOpaque(true);
		((JComponent) cellComponent).add(colorCell);
	}
}
