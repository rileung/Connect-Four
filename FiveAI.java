// code is based on  p166 Figure5.3 and  p171 evaluation function 
import java.util.HashMap;
import java.awt.Point;

public class FiveAI extends AIModule 
{
	// the depth that the algorithrm search 
	 private int depth = 0;
	 private int p;
	 private final long color[] = new long[2];
	 private final static int WIDTH = 7;
	 private final static int HEIGHT = 6;
	 private final static int H1 = HEIGHT + 1;
	 private final static int H2 = HEIGHT + 2;
	 private final static long TOP = 0x1020408102040L;
	 private final static long RIGHT = 0xfe000000000000L;
	 private int wfour = 1000;
	 private int wthree2space = 500;
	 private int wthree1space = 100;
	 private int wtwo2space = 50;
	 private int wtwo1space = 10;


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
				//System.out.printf("value is: %f\n", values[i]);
				game.unMakeMove();
				if (values[i] > values[chosenMove])
					chosenMove = i;
			}
			System.out.printf("depth is %d\n", depth);
		}
		//System.out.printf("final i is: %d\n", chosenMove);	
	}

	private int computeFour(final long board)
	{
		int sum = 0;
		long temp = board & (board >> HEIGHT);
		sum = sum+ wfour * countOne(temp & (temp >> 2 * HEIGHT));// check diagonal \
		temp = board & (board >> H1);
		sum = sum+ wfour * countOne(temp & (temp >> 2 * H1)); // check horizontal -		
		temp = board & (board >> H2); // check diagonal /
		sum = sum + wfour * countOne(temp & (temp >> 2 * H2));
		temp = board & (board >> 1); // check vertical |
		sum = sum + wfour * countOne(temp & (temp >> 2));
		return sum;
	}

	private int computeThree(final long board, final long enemy)
	{
		int sum = 0;
		long temp = board & (board >> HEIGHT);// check diagonal \
		long temp2 = temp & (temp >> HEIGHT);
		long temp3 = temp2 >> HEIGHT;
		long temp4 = temp3 & (temp3 ^ enemy) & (temp3 ^ TOP);
		sum = sum + wthree1space * countOne(temp4);
		long temp5 = temp2 << 3 * HEIGHT;
		sum = sum + wthree1space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ TOP)  & (temp5 ^ RIGHT) );
		long temp6 = temp4 << 4 * HEIGHT;
		sum = sum + wthree2space * countOne( temp6 & (temp6 ^ enemy) & (temp6 ^ TOP)  & (temp5 ^ RIGHT) );
		
		temp = board & (board >> H1);//check horizontal
		temp2 = temp & (temp >> H1);
		temp3 = temp2 >> H1;
		temp4 = temp3 & (temp3 ^ enemy);
		sum = sum + wthree1space * countOne(temp4);
		temp5 = temp2 << 3 * H1;
		sum = sum + wthree1space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ RIGHT) );
		temp6 = temp4 << 4 * H1;
		sum = sum + wthree2space * countOne( temp6 & (temp6 ^ enemy) & (temp6 ^ RIGHT) );


		temp = board & (board >> H2); // check diagonal /
		temp2 = temp & (temp >> H2);
		temp3 = temp2 >> H2;
		temp4 = temp3 & (temp3 ^ enemy) & (temp3 ^ TOP);
		sum = sum + wthree1space * countOne(temp4);
		temp5 = temp2 << 3 * H2;
		sum = sum + wthree1space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ TOP)  & (temp5 ^ RIGHT));
		temp6 = temp4 << 4 * H2;
		sum = sum + wthree2space * countOne( temp6 & (temp6 ^ enemy) & (temp6 ^ TOP)  & (temp5 ^ RIGHT));

		temp = board & (board >> 1); // check vertical |
		temp2 = temp & (temp >> 1);
		temp3 = temp2 >> 1;
		temp4 = temp3 & (temp3 ^ enemy) & (temp3 ^ TOP);
		sum = sum + wthree1space * countOne(temp4);
		temp5 = temp2 << 3;
		sum = sum + wthree1space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ TOP) );
		temp6 = temp4 << 4;
		sum = sum + wthree2space * countOne( temp6 & (temp6 ^ enemy) & (temp6 ^ TOP) );
		return sum;
	}

	private int computeTwo(final long board, final long enemy)
	{
		int sum = 0;
		// check diagonal \
		long temp = board & (board >> HEIGHT); 
		long temp2 = temp >> HEIGHT;
		long temp3 = temp2 & (temp2 ^ enemy) & (temp2 ^ TOP);
		sum = sum + wtwo1space * countOne(temp3);
		long temp4 = temp << 2 * HEIGHT;
		sum = sum + wtwo1space * countOne( temp4 & (temp4 ^ enemy) & (temp4 ^ TOP) & (temp4 ^ RIGHT));
		long temp5 = temp3 << 3 * HEIGHT;
		sum = sum + wtwo2space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ TOP) & (temp5 ^ RIGHT));

		
		// check horizontal -		
		temp = board & (board >> H1); 
		temp2 = temp >> H1;
		temp3 = temp2 & (temp2 ^ enemy);
		sum = sum + wtwo1space * countOne(temp3);
		temp4 = temp << 2 * H1;
		sum = sum + wtwo1space * countOne( temp4 & (temp4 ^ enemy) &  (temp4 ^ RIGHT));
		temp5 = temp3 << 3 * H1;
		sum = sum + wtwo2space * countOne( temp5 & (temp5 ^ enemy) &  (temp5 ^ RIGHT));



		// check diagonal /
		temp = board & (board >> H2); 
		temp2 = temp >> H2;
		temp3 = temp2 & (temp2 ^ enemy) & (temp2 ^ TOP);
		sum = sum + wtwo1space * countOne(temp3);
		temp4 = temp << 2 * H2;
		sum = sum + wtwo1space * countOne( temp4 & (temp4 ^ enemy) & (temp4 ^ TOP) & (temp4 ^ RIGHT));
		temp5 = temp3 << 3 * H2;
		sum = sum + wtwo2space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ TOP) & (temp5 ^ RIGHT));

		
		//check vertical |
		temp = board & (board >> 1); 
		temp2 = temp >> 1;
		temp3 = temp2 & (temp2 ^ enemy) & (temp2 ^ TOP);
		sum = sum + wtwo1space * countOne(temp3);
		temp4 = temp << 2;
		sum = sum + wtwo1space * countOne( temp4 & (temp4 ^ enemy) & (temp4 ^ TOP) );
		temp5 = temp3 << 3;
		sum = sum + wtwo2space * countOne( temp5 & (temp5 ^ enemy) & (temp5 ^ TOP) );
		return sum;
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
			result = computeFour(color[0]) + computeThree(color[0], color[1]) + computeTwo(color[0], color[1]);
			result = result - computeFour(color[1]) - computeThree(color[1], color[0]) - computeTwo(color[1], color[0]);
			return result;
		}

        else{
			double result = 0;
			result = computeFour(color[1]) + computeThree(color[1], color[0]) + computeTwo(color[1], color[0]);
			result = result - computeFour(color[0]) - computeThree(color[0], color[1]) -  computeTwo(color[0], color[1]);
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


