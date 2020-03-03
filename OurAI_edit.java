// code is based on  p166 Figure5.3 and  p171 evaluation function
import java.util.HashMap;

public class OurAI_edit extends AIModule
{
	private int depth = 0;

	private HashMap<int [][], Double> evalMap = new HashMap<int [][], Double>();


	// }
		// function to get next move
	public void getNextMove(final GameStateModule game)
	{
    // initialize 
		chosenMove = 3;
		int width = game.getWidth();
    // to store the values of every possible step, in order to choose the largest one
		double[] values = new double[width];
		int p = game.getActivePlayer();
    // choose the action with max minValue, that is what the first function do in Figure5.3
		for(int i = 0; i < width; ++i) 
			values[i] = -Integer.MAX_VALUE;
		//for(int i = 0; i < width; ++i) {
		int []arr = {3,2,4,1,5,0,6};
        for(int k = 0; k < width; ++k) 
        {
        	if (terminate)
        		break;
        	int i = arr[k];
			System.out.printf("number i is: %d\n", i);
			depth = 0;
			if(game.canMakeMove(i)) {
				game.makeMove(i);
				values[i] = minValue(game, ++depth);
				game.unMakeMove();
				if (values[i] > values[chosenMove])
					chosenMove = i;
			}
		}
		System.out.printf("final i is: %d\n", chosenMove);	
	}


	/**
  * @return board score, higher values indicates a more favorable board to win
	*/
	private double eval(final GameStateModule state)
	{
	  int score = 0;
		final int width = state.getWidth();
	  final int player = state.getActivePlayer();

	 // currently counts multiple times for the same row
	 // ex: row of 3 has a weight of > 81
	 // breaks hight loop if 0; skips
		for (int x = 0; x < width; ++x) {
			int height = state.getHeightAt(x);
			for (int y = 0 ; y < height ; ++y) {
				if (state.getAt(x, y) == 0)
					break;

				// checks for vertical alignments of 3; weights currently ^3
				else if ((y + 2 < state.getHeightAt(x)) && (state.getAt(x,y + 2) == player)
								 && (state.getAt(x,y + 1) == player) && (state.getAt(x,y) == player))
								 score += (state.getAt(x, y) == player ? 81 : -81);

				// verticals of 2
				else if ((y + 1 < state.getHeightAt(x)) && (state.getAt(x,y + 1) == player))
							score += (state.getAt(x,y) == player ? 27: -27);

				// horizontal alignments of 3
				else if ((x + 3 < width) && (state.getAt(x + 3, y) == player)
							&& (state.getAt(x + 2, y) == player) && (state.getAt(x,y) == player))
								score += (state.getAt(x, y) == player ? 81 : -81);

				// h-alignments of 2
				else if ((x + 2 < width) && (state.getAt(x + 2,y) == player)
							&& (state.getAt(x + 1, y) == player) && (state.getAt(x,y) == player))
								score += (state.getAt(x, y) == player ? 27 : -27);

				//ascending diagonal of 3
				else if ((x + 2 < width) && (state.getAt(x + 2,y + 3) == player)
							 && (state.getAt(x + 1, y + 2) == player && (state.getAt(x,y) == player)))
							 	score += (state.getAt(x, y) == player ? 81 : -81);

				//ascending diagonal of 2
				else if ((x + 1 < width) && (state.getAt(x + 1,y + 2) == player)
							 && (state.getAt(x, y) == player))
							 	score += (state.getAt(x, y) == player ? 81 : -81);

				// descending diagonal of 3
				else if (x >= 2 && (state.getAt(x - 2,y + 3) == player) && (state.getAt(x - 1, y + 2) == player)
							&& (state.getAt(x,y) == player))
								score += (state.getAt(x, y) == player ? 81 : -81);

				// descending diagonal of 2
				else if (x >= 1 && (state.getAt(x - 1,y + 2) == player)
							 && (state.getAt(x, y) == player))
								score += (state.getAt(x, y) == player ? 27 : -27);

				// individual cases get weight of 1 or -1
				else
					score += (state.getAt(x, y) == player ? 1: -1);
			}
		} // end of outer loop

  //  System.out.println("Score: " + score);
		return score;
}
	/**
  * Can current player wins by playing a given column?
  * never call on a non-playable column.
  * @param  : col playable column.
  * @return :  true if current player can get connect-4 with current move
  */
	private boolean isWinningMove(final GameStateModule state)
	  {
			final int player = state.getActivePlayer();
			System.out.println("TODO: Implement isWinningMove");
			return true;
		}

	private boolean cutoffTest(final GameStateModule game, int d) 
	{
		if (terminate)
			return true;
		else if (game.isGameOver())
			return true;
		else if (d == 7)
			return true;
		else
			return false;
	}



	private double maxValue(final GameStateModule game, int d) 
	{
		if (cutoffTest(game, d))
			return eval(game);
		double v = -Integer.MAX_VALUE;
		for (int i = 0; i< game.getWidth(); ++i) {
			depth = d;
			if (game.canMakeMove(i)) {
				game.makeMove(i);
				double temp  = minValue(game, ++depth);
				if (temp > v)
					v = temp;
				game.unMakeMove();
			}
		}
		return v;
	}


//  that is what the third function do in Figure5.3
	private double minValue(final GameStateModule game, int d) 
	{
		if (cutoffTest(game, d))
			return eval(game);
		double v = Integer.MAX_VALUE;
		for (int i = 0; i< game.getWidth(); ++i) {
			depth = d;
			if (game.canMakeMove(i)) {
				game.makeMove(i);
				double temp  = maxValue(game, ++depth);
				if (temp < v)
					v = temp;
				game.unMakeMove();
			}
		}
		return v;

	}
}

