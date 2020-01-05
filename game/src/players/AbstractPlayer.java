package players;

import hex.Board;
import hex.Move;


public abstract class AbstractPlayer
{
	private int color;
	abstract public Move getMove(Board board);

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}
}
