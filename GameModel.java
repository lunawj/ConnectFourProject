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

    public boolean checkWinCondition() {
        /* Not implemented yet */
        return false;
    }
}


