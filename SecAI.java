// code is based on  p166 Figure5.3 and  p171 evaluation function 
import java.util.HashMap;
import java.awt.Point;

public class SecAI extends AIModule 
{
	// the depth that the algorithrm search 
	 private int depth = 0;
	 private int p;
	 private final long color[] = new long[2];
	 private final static int WIDTH = 7;
	 private final static int HEIGHT = 6;
	 private final static int H1 = HEIGHT + 1;
	 private final static int H2 = HEIGHT + 2;


	// function to get next move
	public void getNextMove(final GameStateModule game)
	{
    // initialize 
		chosenMove = 3;
		int width = game.getWidth();
    // to store the values of every possible step, in order to choose the largest one
		double[] values = new double[width];
		color[0] = color[1] = 0L;
		p = game.getActivePlayer();
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
				System.out.printf("value is: %f\n", values[i]);
				game.unMakeMove();
				if (values[i] > values[chosenMove])
					chosenMove = i;
			}
		}
		System.out.printf("final i is: %d\n", chosenMove);	
	}

	private int computeFour(final long board)
	{
		int count = 0;
		long temp = board & (board >> HEIGHT);
		count = count+ countOne(temp & (temp >> 2 * HEIGHT));// check diagonal \
		temp = board & (board >> H1);
		count = count+ countOne(temp & (temp >> 2 * H1)); // check horizontal -		
		temp = board & (board >> H2); // check diagonal /
		count = count + countOne(temp & (temp >> 2 * H2));
		temp = board & (board >> 1); // check vertical |
		count = count + countOne(temp & (temp >> 2));
		return count;
	}

	private int computeThree(final long board)
	{
		int count = 0;
		long temp = board & (board >> HEIGHT);
		count = count+ countOne(temp & (temp >> HEIGHT));// check diagonal \
		temp = board & (board >> H1);
		count = count+ countOne(temp & (temp >> H1)); // check horizontal -		
		temp = board & (board >> H2); // check diagonal /
		count = count + countOne(temp & (temp >> H2));
		temp = board & (board >> 1); // check vertical |
		count = count + countOne(temp & (temp >> 1));
		return count;
	}

	private int computeTwo(final long board)
	{
		int count = 0;
		count = count+ countOne(board & (board >> HEIGHT));// check diagonal \
		count = count+ countOne(board & (board >> H1)); // check horizontal -		
		count = count + countOne(board & (board >> H2));	// check diagonal /
		count = count + countOne(board & (board >> 1)); //check vertical |
		return count;
	}

	private int countOne(long n)
	{
   	    int c =0 ;
    	while (n!=0)
        {
        	c++;
        	n &= (n -1) ; 
        }
        return c ;
    }


	// return the evaluation function of current game
	private double eval(final GameStateModule game) 
	{
		color[0] = color[1] = 0L;
		for (int i = 0; i<WIDTH; i++) 
			for (int j = 0; j<game.getHeightAt(i); j++) 
				if (game.getAt(i,j) == 1)
					color[0] |= 1L << (i*7 + j);

		for (int i = 0; i<WIDTH; i++) 
			for (int j = 0; j<game.getHeightAt(i); j++) 
				if (game.getAt(i,j) == 2)
					color[1] |= 1L << (i*7 + j);

		if (p ==1){
			double result = 0;
			result = 64 * computeFour(color[0]) + 27 * computeThree(color[0]) + 8 * computeTwo(color[0]);
			result = result - 64 * computeFour(color[1]) - 27 * computeThree(color[1]) - 8 * computeTwo(color[1]);
			return result;
		}

        else{
			double result = 0;
			result = 64 * computeFour(color[1]) + 27 * computeThree(color[1]) + 8 * computeTwo(color[1]);
			result = result - 64 * computeFour(color[0]) - 27 * computeThree(color[0]) - 8 * computeTwo(color[0]);
			return result;		
		}
	}

	// cutoff test to decide if we need to applt eval()
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

  // that is what the second function do in Figure5.3 
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


