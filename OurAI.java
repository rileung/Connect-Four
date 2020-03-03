// code is based on  p166 Figure5.3 and  p171 evaluation function
import java.util.HashMap;

public class OurAI extends AIModule
{
	private int depth = 0;

	private HashMap<int [][], Double> evalMap = new HashMap<int [][], Double>();

	public void getNextMove(final GameStateModule game)
	{
    // initialize
		chosenMove = 0;
		//int p = game.getActivePlayer();
    // to store the values of every possible step, in order to choose the largest one
		double[] values = new double[game.getWidth()];
    // choose the action with max minValue, that is what the first function do in Figure5.3
		for(int i = 0; i < values.length; ++i) {
			if (terminate)
				break;
			depth = 0;
			if(game.canMakeMove(i)) {
				game.makeMove(i);
				values[i] = minValue(game, depth++);
				game.unMakeMove();
				if (values[i] > values[chosenMove])
					chosenMove = i;
			}
		}


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

	// cutoff test to decide if we need to applt eval()
	private boolean cutoffTest(final GameStateModule state, int d)
	{
		if (terminate)
			return true;
		if (state.isGameOver())
			return true;
		if (depth == 8)
			return true;
		return false;
	}


  // that is what the second function do in Figure5.3
	private double maxValue(final GameStateModule state, int d)
	{
		if (cutoffTest(state, depth))
			return eval(state);
		double v = -Integer.MAX_VALUE;
		for (int i = 0; i< state.getWidth(); ++i) {
			depth = d;
			if (state.canMakeMove(i)) {
				state.makeMove(i);
				double temp  = minValue(state, depth++);
				if (temp > v)
					v = temp;
				state.unMakeMove();
			}
		}
		return v;
	}

  //  that is what the third function do in Figure5.3
	private double minValue(final GameStateModule state, int d)
	{
		if (cutoffTest(state, depth))
			return eval(state);
		double v = Integer.MAX_VALUE;
		for (int i = 0; i< state.getWidth(); ++i) {
			depth = d;
			if (state.canMakeMove(i)) {
				state.makeMove(i);
				double temp  = maxValue(state, depth++);
				if (temp < v)
					v = temp;
				state.unMakeMove();
			}
		}
		return v;

	}
}


	// private double eval(final GameStateModule state, int p)
	// {
	// 	double e = 0;
	// 	for(int x = 0; x < state.getWidth(); ++x) {
	// 		int h = state.getHeightAt(x);
	// 		for (int y = 0; y<h; ++y) {
	// 			if (state.getAt(x, y) ==p) {
	// 				if (x+1<state.getWidth() && y< state.getHeightAt(x+1))
	// 					e +=(state.getAt(x+1,y) ==p ? 1: -0.5);
	// 				else if (y+1< state.getHeightAt(x))
	// 					e +=(state.getAt(x,y+1) ==p ? 1: -0.5);
	// 				else if (x+1<state.getWidth() && y+1< state.getHeightAt(x+1))
	// 					e +=(state.getAt(x+1,y+1) ==p ? 1: -0.5);

	// 			}
	// 		}
	// 	}


	// }
