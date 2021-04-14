public class GameModel{
    enum BoardState {
        EMPTY,
        P1,
        P2
    }

    private static final int numRows = 6;
    private static final int numCols = 7;
    private static final int In_a_Row = 4;

    private int numPlaced;
    private BoardState[][] board;
    private int[] openSpaces;
    private Turn playerTurn;
    private WinState winState;

    public GameModel() {
        numPlaced = 0;
        board = new BoardState[numRows][numCols];
        openSpaces = new int[numCols];
        playerTurn = Turn.P1Turn;
        winState = WinState.NOWINNER;

        for(int i=(numRows-1); i>=0; i--) {
            for(int j=(numCols-1); j>=0; j--) {
                board[i][j] = BoardState.EMPTY;
                openSpaces[j] = 0;
            }
        }
    }

    public void setPlayerTurn(Turn turn) {
        playerTurn = turn;
    }

    public int getOpenSpaces(int col) {
        return openSpaces[col];
    }

    public boolean placeChip(int col) {
        boolean retval = false;
        if(openSpaces[col] == numRows) {
            System.out.println("Invalid move");
        }
        else {
            if(board[openSpaces[col]][col] == BoardState.EMPTY) {
                if(playerTurn == Turn.P1Turn) {
                    board[openSpaces[col]][col] = BoardState.P1;
                } else {
                    board[openSpaces[col]][col] = BoardState.P2;
                }
                openSpaces[col]++;
                retval = true;
            }
        }
        
        return retval;
    }

    public WinState checkWinCondition() {
        //check if all spaces are taken
        int tie_count = 0;
        for (int i = 0; i < numCols; i++) {
            if (getOpenSpaces(i) == numRows)
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

