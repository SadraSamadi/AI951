package run;


import hex.exceptions.BadMoveException;
import players.AIPlayer;
import players.AbstractPlayer;
import players.RandomPlayer;

public class Main
{
	public static void main(String[] args) throws BadMoveException, InterruptedException
	{
		AbstractPlayer p1 = new RandomPlayer();
//		AbstractPlayer p1 = new AIPlayer();
//		AbstractPlayer p2 = new RandomPlayer();
		AbstractPlayer p2 = new AIPlayer();
		Game g = new Game(p1, p2);
		g.start();
	}
}
