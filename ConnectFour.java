import java.util.Random;
import java.util.Scanner;

public class ConnectFour{
    enum BoardState {
        EMPTY,
        P1,
        P2,
        EDGE
    }
    
    enum Turn {
        P1Turn,
        P2Turn,
    }
    
    enum WinState {
    	P1Win,
        P2Win,
        TIE,
        NOWINNER
    }
    
    enum HowToHandleTurns {
        P1_ALWAYS_FIRST,
        P2_ALWAYS_FIRST,
        RANDOM_FIRST,
        WINNER_FIRST,
        LOSER_FIRST,
        ALTERNATE_TURNS
    }
    
    enum Directions {
        UP,
        UPPER_LEFT,
        UPPER_RIGHT,
        RIGHT,
        LEFT,
        DOWN,
        LOWER_LEFT,
        LOWER_RIGHT
    }
    
    public static void main(String []args){
        int numRows = 6;
        int numCols = 7;
        int latestColPicked = -1;
        boolean keepRunning = true;
        int numPlaced = 0; //increments per successful chip placement, 42 max
        BoardState[][] board = new BoardState[numRows][numCols];
        int[] openSpaces = new int[numCols];
        Turn playerTurn;
        WinState winState = WinState.NOWINNER;
        HowToHandleTurns turnHandler;
        
        System.out.println("Hello World");
        
        /*
        Set player turn
        Rand?
        Let player choose?
        Loser goes first? Winner?
        Player 1 goes first by default?
        Alternate turns?
        */
        
        Random rand = new Random();
        int randNum = rand.nextInt(1);
        initializeBoard(board, numRows, numCols, openSpaces);
        /*
        while(keepRunning)
        {
            switch
            {
                case GameState.P1Turn:
                    break;
                    
                case GameState.P2Turn:
                    break;
                    
                case GameState.P1Win:
                    break;
                    
                case GameState.P2Win:
                    break;
                    
                case GameState.TIE:
                    break;
                    
                case GameState.RESET:
                    break;
                    
                case GameState.MENU:
                    break;
                
                default:
                    break;
            }
        }
        */
        //Check if board was initialized correctly
        System.out.println("IN MAIN");
        printBoard(board, numRows, numCols);
        
        //test
        playerTurn = Turn.P1Turn;
        while(true)
        {
        	latestColPicked = placeChip(board, numPlaced, numRows, numCols, playerTurn, openSpaces);
        	if(latestColPicked != -1)
        	{
        		numPlaced++;
        		//check win condition
        		winState = checkWinCondition(board, numPlaced, numRows, numCols, playerTurn, latestColPicked, openSpaces);
        		if(winState != WinState.NOWINNER)
        		{
        			break;
        		}
			if(playerTurn == playerTurn.P1Turn)
	    		{
	    			playerTurn = playerTurn.P2Turn;
	    		}else if(playerTurn == playerTurn.P2Turn)
	    		{
	    			playerTurn = playerTurn.P1Turn;
	    		}else
	    		{
	    			//error
	    		}
    		}else
    		{
    			//error
    		}
    		System.out.println("\n\n\n");
        	printBoard(board, numRows, numCols);
        }
        System.out.println("Total number of chips placed: " + numPlaced);
        printOpenSpaces(openSpaces, numCols);
        System.out.println("\n\n\n");
	printBoard(board, numRows, numCols);
    }
     
    public static void initializeBoard(BoardState[][] board, int numRows, int numCols, int[] openSpaces)
    {
        for( int i = numRows - 1; i >= 0; i--)
        {
            for(int j = numCols - 1; j >= 0; j--)
            {
                board[i][j] = BoardState.EMPTY;
                openSpaces[j] = 0;
            }
        }
    }
    
    public static void printBoard(BoardState[][] board, int numRows, int numCols)
    {
        for( int i = numRows - 1; i >= 0; i--)
        {
            //System.out.print("Row = " + i + " ");
            for(int j = numCols - 1; j >= 0; j--)
            {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    public static void setPlayerTurn()
    {
        
    }
    
    public static WinState checkWinCondition(BoardState[][] board, int numPlaced, int numRows, int numCols, Turn playerTurn, int latestColPicked, int[] openSpaces)
    {
    	WinState winState = WinState.NOWINNER;
    	boolean winner = false;
    	int numInARow = 0;
    	int i;
    	int j;
    	BoardState currentTurn;
    	if(playerTurn == Turn.P1Turn)
    	{
    		currentTurn = BoardState.P1;
    	}else if(playerTurn == Turn.P2Turn)
    	{
    		currentTurn = BoardState.P2;
    	}else
    	{
    		System.out.println("Turn variable is not a players turn. No action taken.");
    		return winState;
    	}
    	//board[openSpaces[latestColPicked-1]][colPicked];
    	//check vertical
    	//Checks up
    	for(i = openSpaces[latestColPicked]-1; i < numRows && i >= 0; i++)
    	{
    		if(board[i][latestColPicked] == currentTurn)
    		{
    			numInARow++;
    		}else
    		{
    			break;
    		}
    		if(numInARow == 4)
    		{
    			System.out.println("Winner set while checking up."); //debug
    			winner = true;
    			break;
    		}
    	}
    	//prevents double counting the latest picked space
    	numInARow--;
    	//Check down
    	for(i = openSpaces[latestColPicked] - 1; i < numRows && i >= 0; i--)
    	{
    		if(board[i][latestColPicked] == currentTurn)
    		{
    			numInARow++;
    		}else
    		{
    			break;
    		}
    		if(numInARow == 4)
    		{
    			System.out.println("Winner set while checking down."); //debug
    			winner = true;
    			break;
    		}
    	}
    	//reset
    	numInARow = 0;
    	//checks horizontal
    	if(winner == false)
    	{
	    	//Checks left
	    	for(i = latestColPicked; i < numCols && i >= 0; i++)
	    	{
	    		if(board[openSpaces[latestColPicked]-1][i] == currentTurn)
	    		{
	    			numInARow++;
	    		}else
	    		{
	    			break;
	    		}
	    		if(numInARow == 4)
	    		{
	    			System.out.println("Winner set while checking left."); //debug
	    			winner = true;
	    			break;
	    		}
	    	}
	    	//prevents double counting the latest picked space
	    	numInARow--;
	    	//Check right
	    	for(i = latestColPicked; i < numCols && i >= 0; i--)
	    	{
	    		if(board[openSpaces[latestColPicked]-1][i] == currentTurn)
	    		{
	    			numInARow++;
	    		}else
	    		{
	    			break;
	    		}
	    		if(numInARow == 4)
	    		{
	    			System.out.println("Winner set while checking right."); //debug
	    			winner = true;
	    			break;
	    		}
	    	}
    	}
    	//reset
    	numInARow = 0;
    	
    	//check upper left diagnal
    	j = 0;
    	if(winner == false)
    	{
	    	//Checks upper left
	    	for(i = openSpaces[latestColPicked]-1; (i < numRows && i >= 0) && ((latestColPicked + j) < numCols && (latestColPicked + j) >= 0); i++)
	    	{
	    		if(board[i][latestColPicked + j] == currentTurn)
	    		{
	    			numInARow++;
	    		}else
	    		{
	    			break;
	    		}
	    		if(numInARow == 4)
	    		{
	    			System.out.println("Winner set while checking upper left."); //debug
	    			winner = true;
	    			break;
	    		}
	    		j++;
	    	}
	    	//prevents double counting the latest picked space
	    	numInARow--;
	    	j = 0;
	    	//Check lower right
	    	for(i = openSpaces[latestColPicked] -1; (i < numRows && i >= 0) && ((latestColPicked - j) < numCols && (latestColPicked - j) >= 0); i--)
	    	{
	    		if(board[i][latestColPicked - j] == currentTurn)
	    		{
	    			numInARow++;
	    		}else
	    		{
	    			break;
	    		}
	    		if(numInARow == 4)
	    		{
	    			System.out.println("Winner set while checking lower right."); //debug
	    			winner = true;
	    			break;
	    		}
	    		j++;
	    	}
    	}
    	//reset
    	numInARow = 0;
    	j = 0;
    	//check upper right diagnal
    	if(winner == false)
    	{
	    	//Checks upper right
	    	for(i = openSpaces[latestColPicked]-1; (i < numRows && i >= 0) && ((latestColPicked - j) < numCols && (latestColPicked - j) >= 0); i++)
	    	{
	    		if(board[i][latestColPicked - j] == currentTurn)
	    		{
	    			numInARow++;
	    		}else
	    		{
	    			break;
	    		}
	    		if(numInARow == 4)
	    		{
	    			System.out.println("Winner set while checking upper right."); //debug
	    			winner = true;
	    			break;
	    		}
	    		j++;
	    	}
	    	//prevents double counting the latest picked space
	    	numInARow--;
	    	j = 0;
	    	//Checks lower left
	    	for(i = openSpaces[latestColPicked]-1; (i < numRows && i >= 0) && ((latestColPicked + j) < numCols && (latestColPicked + j) >= 0); i--)
	    	{
	    		if(board[i][latestColPicked + j] == currentTurn)
	    		{
	    			numInARow++;
	    		}else
	    		{
	    			break;
	    		}
	    		if(numInARow == 4)
	    		{
	    			System.out.println("Winner set while checking lower left."); //debug
	    			winner = true;
	    			break;
	    		}
	    		j++;
	    	}
    	}
    	//reset
    	numInARow = 0;
    	
    	
        if(winner == true)
        {
        	if(playerTurn == Turn.P1Turn)
	    	{
	    		winState = WinState.P1Win;
        		System.out.println("Player 1 wins!");
	    	}else if(playerTurn == Turn.P2Turn)
	    	{
	    		winState = WinState.P2Win;
        		System.out.println("Player 2 wins!");
	    	}else
	    	{
	    	//Error
	    	}
        }else if(numPlaced == 42) //don't hard code
        {
        	winState = WinState.TIE;
        	System.out.println("It's a tie!");
        	return winState;
        }
	return winState;
    }
    
    public static int placeChip(BoardState[][] board, int numPlaced, int numRows, int numCols, Turn playerTurn, int[] openSpaces)
    {
    	BoardState currentTurn;
    	int colPicked;
    	Scanner in = new Scanner(System.in);
        
    	if(playerTurn == Turn.P1Turn)
    	{
    		currentTurn = BoardState.P1;
    	}else if(playerTurn == Turn.P2Turn)
    	{
    		currentTurn = BoardState.P2;
    	}else
    	{
    		System.out.println("Turn variable is not a players turn. No action taken.");
    		return -1;
    	}
    	
    	while(true)
    	{
	    	System.out.println("\nWhere would you like to place your chip? Enter a number between 0 (right-most column) and 6. (left-most column)");
	    	colPicked = in.nextInt();
	    	if(colPicked < 0 || colPicked >= numCols)
	    	{
	    		System.out.println("Invalid range. Please try again.");
	    	}else if(openSpaces[colPicked] ==  numRows)
	    	{
	    		System.out.println("That Column is full. Please try again.");
	    	}else if(openSpaces[colPicked] <  numRows)
	    	{
	    		if(board[openSpaces[colPicked]][colPicked] == BoardState.EMPTY)
	    		{
	    			board[openSpaces[colPicked]][colPicked] = currentTurn;
	    			openSpaces[colPicked]++;
	    			break;
	    		}
	    	}
		printBoard(board, numRows, numCols);
        }
        return colPicked;
    }
    
    public static void aiTurn()
    {
        
    }
    
    public static void menuScreen()
    {
        
    }
    
    public static void printOpenSpaces(int[] openSpaces, int numCols)
    {
    	System.out.println("The open spaces are: ");
    	for(int i = 0; i < numCols; i++)
    	{
    		if(i == numCols - 1)
    		{
    			System.out.println("(" + i + ", " + openSpaces[i] + ").");
    		}else
    		{
    			System.out.print("(" + i + ", " + openSpaces[i] + "), ");
    		}
   	}
    }
    
}
