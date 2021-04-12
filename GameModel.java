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
        if(openSpaces[col] == numRows) {
            return false;
        }
        else {
            if(playerTurn == Turn.P1Turn) {
                board[openSpaces[col]][col] = BoardState.P1;
            } else {
                board[openSpaces[col]][col] = BoardState.P2;
            }
            openSpaces[col]++;
            return true;
        }
    }

    public WinState checkWinCondition(int latestCol) {
        winState = WinState.NOWINNER;
        boolean winner = false;
        int numInARow = 0;
        BoardState currentTurn;

        if(playerTurn == Turn.P1Turn) {
            currentTurn = BoardState.P1;
        } else if(playerTurn == Turn.P2Turn) {
            currentTurn = BoardState.P2;
        } else {
            System.out.println("Turn var is not valid. No action taken.");
            return winState;
        }

        /* check up */
        for(int i=openSpaces[latestCol]-1; i<numRows && i>=0; i++) {
            if(board[i][latestCol] == currentTurn) {
                numInARow++;
            }
            else {
                break;
            }

            if(numInARow == 4) {
                System.out.println("Winner set while checking up."); //debug
                winner = true;
                break;
            }
        }

        /* prevents double counting the latest picked space */
        numInARow--;

        /* check down */
        for(int i=openSpaces[latestCol]-1; i<numRows && i>=0; i++) {
            if(board[i][latestCol] == currentTurn) {
                numInARow++;
            } else {
                break;
            }
            
            if(numInARow == 4) {
                System.out.println("Winner set while checking down."); //debug
                winner = true;
                break;
            }
        }
        
        /* reset */
        numInARow = 0;
        /* Check horizontal... only if no winner */
        if(winner == false) {
            /* Check left */
            for(int i=latestCol; i<numCols && i>= 0; i++) {
                if(board[openSpaces[latestCol]-1][i] == currentTurn) {
                    numInARow++;
                } else {
                    break;
                }

                if(numInARow == 4) {
                    System.out.println("Winner set while checking left."); //debug
                    winner = true;
                    break;
                }
            }

            /* prevents double counting the latest picked space */
            numInARow--;
            /* Check right */
            for(int i=latestCol; i<numCols && i>=0; i--) {
                if(board[openSpaces[latestCol]-1][i] == currentTurn) {
                    numInARow++;
                } else {
                    break;
                }

                if(numInARow == 4) {
                    System.out.println("Winner set while checking right."); //debug
                    winner = true;
                    break;
                }
            }
        }

        /* reset */
        numInARow = 0;
        /* Check upper left diagonal */
        int j = 0;
        if(winner == false) {
            /* Check upper left */
            for(int i=openSpaces[latestCol]-1; 
                (i<numRows && i>=0) && (((latestCol+j)<numCols) && ((latestCol+j)>=0)); i++) {
                if(board[i][latestCol+j] == currentTurn) {
                    numInARow++;
                } else {
                    break;
                }
 
                if(numInARow == 4) {
                    System.out.println("Winner set while checking upper left."); // debug
                    winner = true;
                    break;
                }
                j++;
            }

            /* prevents double counting the latest picked space */
            numInARow--;
            j = 0;
            /* Check lower right diagonal */
            for(int i=openSpaces[latestCol]-1;
                (i<numRows && i>=0) && (((latestCol-j)<numCols) && ((latestCol-j)>=0)); i--) {
                if(board[i][latestCol-j] == currentTurn) {
                    numInARow++;
                } else {
                    break;
                }  

                if(numInARow == 4) {
                    System.out.println("Winner set while checking lower right."); //debug
                    winner = true;
                    break;
                }
                j++;
            }
        }

        /* reset */
        numInARow = 0;
        j = 0;
        /* Check upper right diagonal */
        if(winner == false) {
            /* Check upper right */
            for(int i=openSpaces[latestCol]=1;
                (i<numRows && i>=0) && (((latestCol-j)<numCols) && ((latestCol-j)>=0)); i++) {
                if(board[i][latestCol-j] == currentTurn) {
                    numInARow++;
                } else {
                    break;
                }
 
                if(numInARow == 4) {
                    System.out.println("Winner set while checking upper right."); //debug
                    winner = true;
                    break;
                }
                j++;
            }

            /* prevents double counting the latest picked space */
            numInARow--;
            j = 0;
            /* Check lower left */
            for(int i=openSpaces[latestCol]-1;
                (i<numRows && i>=0) && (((latestCol+j)<numCols) && ((latestCol+j)>=0)); i--) {
                if(board[i][latestCol+j] == currentTurn) {
                    numInARow++;
                } else {
                    break;
                }

                if(numInARow == 4) {
                    System.out.println("Winner set while checking lower left."); //debug
                    winner = true;
                    break;
                }
                j++;
            }
        }
    
        /* reset */
        numInARow = 0;

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

