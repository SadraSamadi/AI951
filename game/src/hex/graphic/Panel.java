package hex.graphic;

import hex.Board;
import hex.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;


public class Panel extends JPanel
{

	final static int size=20;
	final static int gap=10;
	private Board board;
	private Color [] colors={Color.WHITE,new Color(255,67,46),new Color(83,187,244)};

	public Panel()
	{
		super();
		setLayout(null);
		setOpaque(true);
	}

	private double[] rotate(double[] point,double [] center,double angle)
	{
		double [] res=new double[2];
		AffineTransform.getRotateInstance(Math.toRadians(angle), center[0], center[1])
				.transform(point,0,res,0,1);
		return res;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		paintBoarder((Graphics2D)g);
		for (int i=0;i<board.getSize();i++)
			for (int j=0;j<board.getSize();j++)
			{
				double [] coordinate=coordinate(i,j);
				paint((Graphics2D) g,coordinate[0],coordinate[1],size,colors[board.get(new Cell(board.getSize()-1-i,j))]);
			}
	}

	private void paintBoarder(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		ArrayList<double[]> points=new ArrayList<>();
		points.add(new double[]{-size*1.5,0});
		double [] temp=coordinate(0,board.getSize()-1);
		temp[1]=0;
		temp[0]+=1.5*size;
		points.add(temp);
		temp=coordinate(board.getSize()-1,board.getSize()-1);
		temp[0]+=size*Math.sqrt(3)/2+gap+size*1.5;
		temp[1]+=size+gap;
		points.add(temp);
		double bt=temp[1];
		temp=coordinate(board.getSize()-1,0);
		temp[1]=bt;
		temp[0]-=1.5*size;
		points.add(temp);
		for (int i=0;i<points.size();i++)
		{
			int nx=(i+1)%points.size();
			double [] mid=coordinate(board.getSize()/2,board.getSize()/2);
			int [] x={(int) Math.round(points.get(i)[0]), (int) Math.round(points.get(nx)[0]), (int) Math.round(mid[0])};
			int [] y={(int) Math.round(points.get(i)[1]), (int) Math.round(points.get(nx)[1]), (int) Math.round(mid[1])};
			g.setColor(colors[1+(i%2)]);
			g.fillPolygon(x,y,3);
			drawBoarder(g,x,y,3,0.5,Color.BLACK);

		}
	}

	public double [] coordinate(int x,int y)
	{
		double [] res=new double[2];
		res[0]=gap+(y+1./2)*Math.sqrt(3)*size;
		res[1]=gap+size+x*1.5*size;
		res[0]+=Math.sqrt(3)/2*x*size;
		return res;
	}
	private void paint(Graphics2D g, double cx, double cy, double l, Color color)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double [] now={cx,cy-l};
		int [] x=new int[6];
		int [] y=new int[6];

		for (int i=0;i<6;i++)
		{
			x[i]= (int) Math.round(now[0]);
			y[i]= (int) Math.round(now[1]);
			now=rotate(now, new double[]{cx, cy},180/3);
		}
		g.setColor(color);
		g.fillPolygon(x,y,6);
		drawBoarder(g, x, y, 6, 1, Color.BLACK);
	}

	private void drawBoarder(Graphics2D g, int[] x, int[] y,int n, double thickness,Color color)
	{
		Stroke oldStroke=g.getStroke();
		g.setStroke(new BasicStroke((float) thickness));
		g.setColor(color);
		g.drawPolygon(x,y,n);
		g.setStroke(oldStroke);
	}

	void setBoard(Board board)
	{
		this.board = board;
	}
}
