package players;

import hex.Board;
import hex.Cell;
import hex.Move;
import hex.Swap;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer extends AbstractPlayer
{
	private Random random=new Random();

	@Override
	public Move getMove(Board board)
	{
		ArrayList<Move> availables=new ArrayList<>();
		for (int i=0;i<board.getSize();i++) for (int j=0;j<board.getSize();j++) if (board.get(new Cell(i,j))==0)
			availables.add(new Move(new Cell(i,j)));
		if (board.isSwapAvailable()) availables.add(new Swap());
		return availables.get(random.nextInt(availables.size()));
	}
}
