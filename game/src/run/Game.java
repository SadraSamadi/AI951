package run;


import hex.Board;
import hex.Move;
import hex.exceptions.BadMoveException;
import hex.graphic.Client;
import players.AbstractPlayer;

public class Game
{
	private AbstractPlayer [] players=new AbstractPlayer[2];
	private Client client;
	private Board board;
	public Game(AbstractPlayer red, AbstractPlayer blue)
	{
		players[0]=red;
		players[1]=blue;
		red.setColor(1);
		blue.setColor(2);

	}
	private int timeout=0;
	private boolean exception=false;
	public void start()
	{
		board=new Board();
		client=new Client(board);
		int turn=0;
		client.repaint();
		try
		{
			Thread.sleep(400);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		while(board.win() == 0)
		{
			Move m = getMove(players[turn], board);
			if (m==null)
			{
				timeout = 2 - turn;
				if (!exception) System.out.println("Player " + players[turn].getColor() + " has exceeded the time limit");
				break;
			}
			try
			{
				board.move(m,players[turn].getColor());
			} catch (BadMoveException e)
			{
				timeout=2-turn;
				e.printStackTrace();
				break;
			}
			client.repaint();
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			turn^=1;
		}
		int winner = (timeout==0?board.win():timeout);
		System.out.println((winner==1?"Red":"Blue")+" has won");
	}
	private Move m=null;
	private Move getMove( final AbstractPlayer p,  final Board board)
	{
		
		m=null;
		Thread t=new Thread()
		{
			public void run()
			{
				try
				{
					m = p.getMove(new Board(board));
					if (m==null) throw new BadMoveException();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					exception=true;
					m=null;
				}
			}
		};
		t.start();
		try
		{
			t.join(10000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return m;
	}
}
