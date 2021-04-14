/************************************************************************
 * This program creates the logic of the game: Cuatro that is based off
 * the iconic board game, Connect Four. The program interacts with a GUI
 * and connects two clients and puts them in their own server thread.
 ************************************************************************/
public class GameModel{

    /** these are the states that a square of the board can hold  */
    enum BoardState {
        EMPTY,
        P1,
        P2
    }

    /** This is the number of rows in the game */
    private static final int numRows = 6;

    /** This is the number of columns in the game */
    private static final int numCols = 7;

    /** This is the number of connections a player needs to win the game */
    private static final int In_a_Row = 4;

    /** This is the double array that holds the board */
    private BoardState[][] board;

    /** This is the array that holds how many spaces are taken in each column */
    private int[] takenSpaces;

    /** This tells the program who's turn it is */
    private Turn playerTurn;

    /** This tells the program the current win state */
    private WinState winState;

    /************************************************************************
     * This is the basic constructor. It sets the board to it's
     * default settings
     ************************************************************************/
    public GameModel() {
        board = new BoardState[numRows][numCols];
        takenSpaces = new int[numCols];
        playerTurn = Turn.P1Turn;
        winState = WinState.NOWINNER;

        for(int i=(numRows-1); i>=0; i--) {
            for(int j=(numCols-1); j>=0; j--) {
                board[i][j] = BoardState.EMPTY;
                takenSpaces[j] = 0;
            }
        }
    }

    /************************************************************************
     * This function sets the player's turn to the input.
     * @param turn is the what the variable 'PlayerTurn' will be set to
     ************************************************************************/
    public void setPlayerTurn(Turn turn) {
        playerTurn = turn;
    }

    /************************************************************************
     * This function returns the number of open spaces in a given column
     * @param col is the column wanted
     * @return the number of open spaces taken in the given column
     ************************************************************************/
    public int getTakenSpaces(int col) {
        return takenSpaces[col];
    }

    /************************************************************************
     * This function tries to place a chip in a column. If a column is full,
     * it will let the user know
     * @param col holds the column that the user is trying to drop their chip in
     * @return if the chip has been placed in a valid space or not
     ************************************************************************/
    public boolean placeChip(int col) {
        boolean retval = false;
        if(takenSpaces[col] == numRows) {
            System.out.println("Invalid move");
        }
        else {
            if(board[takenSpaces[col]][col] == BoardState.EMPTY) {
                if(playerTurn == Turn.P1Turn) {
                    board[takenSpaces[col]][col] = BoardState.P1;
                } else {
                    board[takenSpaces[col]][col] = BoardState.P2;
                }
                takenSpaces[col]++;
                retval = true;
            }
        }

        return retval;
    }

    /************************************************************************
     * This function checks if a winner exists on the board currently
     * @return the win state of the board(P1WIn, P2Win, TIE, NOWINNER)
     ************************************************************************/
    public WinState checkWinCondition() {
        //check if all spaces are taken
        int tie_count = 0;
        for (int i = 0; i < numCols; i++) {
            if (getTakenSpaces(i) == numRows)
                tie_count++;//checks if each column is full
        }
        if (tie_count == numCols)
            return winState.TIE;

        int p1Count = 0;//counter for Player 1
        int p2Count = 0;//counter for Player 2
        for (int r = 0; r < numRows; r++)//goes through each row and column combination
            for (int c = 0; c < numCols; c++) {
                //checks for a vertical win
                if (r <= numRows - In_a_Row) {//checks if the board able to calculate a win at this height
                    p1Count = 0;
                    p2Count = 0;

                    //this loop goes from the bottom row and up, checking for a win condition for either player
                    for (int i = 0; i < In_a_Row; i++) {
                        if (board[r + i][c] == BoardState.P1)
                            p1Count++;
                        if (board[r + i][c] == BoardState.P2)
                            p2Count++;
                    }

                    if (p1Count == In_a_Row)//If the 4 of P1 chips are in a row
                        return winState.P1Win;
                    else if (p2Count == In_a_Row)//If the 4 of P2 chips are in a row
                        return winState.P2Win;
                }
                //checks for a horizontal win
                if (c <= numCols - In_a_Row) {
                    p1Count = 0;
                    p2Count = 0;

                    //This loop checks from a win in columns, going for the right to the left
                    for (int i = 0; i < In_a_Row; i++) {
                        if (board[r][c + i] == BoardState.P1)
                            p1Count++;
                        if (board[r][c + i] == BoardState.P2)
                            p2Count++;
                    }

                    if (p1Count == In_a_Row)
                        return winState.P1Win;
                    else if (p2Count == In_a_Row)
                        return winState.P2Win;
                }
                //checks for a secondary diagonal(upper right to lower left)
                if (r >= In_a_Row - 1 && c <= numCols - In_a_Row) {//checks if win check is necessary or lead to a array out of bounds error
                    p1Count = 0;
                    p2Count = 0;
                    //this loop checks each location for a win condition in every position
                    for (int i = 0; i < In_a_Row; i++) {
                        if (board[r - i][c + i] == BoardState.P1)
                            p1Count++;
                        if (board[r - i][c + i] == BoardState.P2)
                            p2Count++;
                    }

                    if (p1Count == In_a_Row)
                        return winState.P1Win;
                    else if (p2Count == In_a_Row)
                        return winState.P2Win;
                }
                //checks for a primary diagonal win(upper left to lower right)
                if (r <= numRows - In_a_Row && c <= numCols - In_a_Row) {
                    p1Count = 0;
                    p2Count = 0;

                    for (int i = 0; i < In_a_Row; i++) {
                        if (board[r + i][c + i] == BoardState.P1)
                            p1Count++;
                        if (board[r + i][c + i] == BoardState.P2)
                            p2Count++;
                    }

                    if (p1Count == In_a_Row)
                        return winState.P1Win;
                    else if (p2Count == In_a_Row)
                        return winState.P2Win;
                }
            }

        return winState.NOWINNER;
    }
}
