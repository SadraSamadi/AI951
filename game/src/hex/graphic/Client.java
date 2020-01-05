package hex.graphic;

import hex.Board;

import javax.swing.*;
import java.awt.*;

public class Client
{
	private JFrame frame;
	private Panel panel;
	public Client(Board board)
	{
		panel=new Panel();
		panel.setBoard(board);
		double [] c=panel.coordinate(board.getSize()-1,board.getSize()-1);
		c[0]+= Panel.size *Math.sqrt(3)/2+ Panel.gap;
		c[1]+= Panel.size + Panel.gap;
		panel.setBounds(0,0,(int)Math.round(c[0]),(int)Math.round(c[1]));
		frame=new JFrame("Hex");
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension((int)Math.round(c[0]),(int)Math.round(c[1])));
		frame.pack();
		frame.setVisible(true);
		frame.add(panel);
		frame.setBounds(200, 200, 400, 400);
	}
	public void repaint()
	{
		panel.repaint();
	}

}
