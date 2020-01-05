package hex;


public class Move
{
	private boolean swap=false;
	private Cell newCell=null;

	public Move(Cell newCell)
	{
		this.newCell = newCell;
	}

	protected Move(boolean swap)
	{
		this.swap = swap;
	}

	public Cell getNewCell()
	{
		return newCell;
	}

	public boolean isSwap()
	{
		return swap;
	}
}
