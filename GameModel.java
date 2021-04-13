public class GameModel{
    enum BoardState {
        EMPTY,
        P1,
        P2
    }

    private static final int numRows = 6;
    private static final int numCols = 7;

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
        
        for(int i=numRows-1; i>=0; i--) {
            for(int j=numCols-1; j>=0; j--) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
        return retval;
    }

    public WinState checkWinCondition(int latestCol) {
        winState = WinState.NOWINNER;
        boolean winner = false;
        int numInARow = 0;
        BoardState currentTurn = BoardState.EMPTY;

        if(playerTurn == Turn.P1Turn) {
            currentTurn = BoardState.P1;
        } else if(playerTurn == Turn.P2Turn) {
            currentTurn = BoardState.P2;
        } else {
            System.out.println("ERROR: WHOS TURN???");
        }
 
        /* Check Vertical */
        for(int i=0; i<numRows; i++) {
            if(board[i][latestCol] == currentTurn) {
                numInARow++;
                if(numInARow >= 4) {
                    winner = true;
                }
            } else {
                numInARow = 0;
            }
        }

        /* Check Horizontal */
        if(winner != true) {
            for(int j=0; j<numCols; j++) {
                if(board[openSpaces[latestCol]-1][j] == currentTurn) {
                    numInARow++;
                    if(numInARow >= 4) {
                        winner = true;
                    }
                } else {
                    numInARow = 0;
                }
            }
        }

        /* Check upper left to lower right diagonal */
        if(winner != true) {
            /* in order to get the most left/up, we check to see how
               far we can go in both direction. the min is the val we
               will go. */
            int val = (latestCol < (numRows - (openSpaces[latestCol]-1)))
                       ? latestCol : (numRows - (openSpaces[latestCol]-1));
            
            int row = (openSpaces[latestCol]-1)+val;
            int col = latestCol - val;

            for(int i=col; (i<numCols) && (row >= 0); i++) {
                /* TODO: ERROR ON LINE 119 when you click the furthest right column */
                if(board[row][i] == currentTurn) {
                    numInARow++;
                    System.out.println("current score: " + numInARow);
                    if(numInARow >= 4) {
                        winner = true;
                    }
                } else {
                    numInARow = 0;
                }
                row--;
            }
        }

        /* TODO: Out of Bounds errors here... I have to get to class though
           and don't have time to investigate now */
        if(winner != true) {
            int val = ((numCols - latestCol) < (numRows - (openSpaces[latestCol]-1))) ?
                      (numCols - latestCol) : (numRows - (openSpaces[latestCol]-1));

            int row = (openSpaces[latestCol-1])+val;
            int col = latestCol + val;

            for(int i=col; (i>=0) && (row>=0); i--) {
                /* TODO: Error when you click the furthest left column */
                if(board[row][i] == currentTurn) {
                    numInARow++;
                    System.out.println("current score: " + numInARow);
                    if(numInARow >= 4) {
                        winner = true;
                    }
                } else {
                    numInARow = 0;
                }
                row--;
            }
        }
    
        if(winner == true) {
            if(playerTurn == Turn.P1Turn) {
                winState = WinState.P1Win;
                System.out.println("Player 1 wins!");
            } else if(playerTurn == Turn.P2Turn) {
                winState = WinState.P2Win;
                System.out.println("Player 2 wins!");
            } else {
                System.out.println("Someone won, but we don't know who!");
            }
        } else if(numPlaced == 42) {
            winState = WinState.TIE;
            System.out.println("It's a tie!");
        }
        return winState;
    }
}

