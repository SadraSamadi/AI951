package hex;


public class Cell
{
	private int r, c;

	public Cell(int r, int c)
	{
		this.r = r;
		this.c = c;
	}

	public Cell()
	{
	}

	public int getR()
	{
		return r;
	}

	public void setR(int r)
	{
		this.r = r;
	}

	public int getC()
	{
		return c;
	}

	public void setC(int c)
	{
		this.c = c;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Cell cell = (Cell) o;

		if (r != cell.r) return false;
		return c == cell.c;

	}

	@Override
	public int hashCode()
	{
		int result = r;
		result = 31 * result + c;
		return result;
	}
}
