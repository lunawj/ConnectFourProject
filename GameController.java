import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**********************************************************************
 * Controller class for the ConnectFour game. This class manages the
 * GUI that is used for each client in the application
 *********************************************************************/
public class GameController extends JPanel {
    
    /** The board that contains the connect 4 pieces */
    private JButton[][] board;

    /** The label saying who's turn it is */
    private JLabel whosTurn;

    /** The local GameModel that calls the GameModel class */
    private GameModel model = new GameModel();

    /** This holds the value of who's turn it is. */
    private Turn playerTurn;

    /** This holds the column of the most recently placed chip. */
    private int lastMove;

    /** This boolean indicates whether a move has been read by the game client. */
    private volatile boolean newMove;

    /** This indicates whether the player of this game is P1 or P2. */
    private int activePlayer;

    /************************************************************************
     * This is the basic constructor. It sets the board to a empty board,
     * always having player 1 go first. It sets up the GUI so that the players
     * can interact with each other
     ************************************************************************/
    public GameController(int player) {
        ButtonListener listener = new ButtonListener();
        playerTurn = Turn.P1Turn;
        model.setPlayerTurn(playerTurn);
        newMove = false;
        activePlayer = player;

        /* panel is the overarching panel eveything is added to */
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        /* panelArray contains two smaller panels that will be added to
           the overarching panel */
        JPanel[] panelArray = new JPanel[2];
        GridBagConstraints c = new GridBagConstraints();
        /* We add the first panel at the top */
        c.gridx = 0;
        c.gridy = 0;
        panelArray[0] = new JPanel();
        panel.add(panelArray[0], c);

        /* We add the second panel below the first */
        c.gridx = 0;
        c.gridy = 1;
        panelArray[1] = new JPanel();
        panel.add(panelArray[1], c);

        /* Within the second panel we create a 7x7 grid. The top 6 rows
           are for the board, the bottom row is buttons that allow the
           player to select which column they want */
        panelArray[1].setLayout(new GridLayout(7,7));

        /* create the turn label, and add it to the first panel */
        whosTurn = new JLabel("Player 1's Turn");
        panelArray[0].add(whosTurn);

        /* Create the board and buttons for each column */
        board = new JButton[7][7];
        for(int i=0; i<7; i++) {
            for(int j=0; j<7; j++) {
                if(i <6) {
                    board[i][j] = new JButton("EMPTY");
                    board[i][j].setBackground(Color.lightGray);
                    board[i][j].setOpaque(true);
                    board[i][j].setEnabled(false);
                    board[i][j].setFont(new Font("Dialog", Font.PLAIN, 12));
                    panelArray[1].add(board[i][j]);
                }
                else {
                    board[i][j] = new JButton("PICK ME!");
                    panelArray[1].add(board[i][j]);
                    board[i][j].addActionListener(listener);
                }
            }
        }
        /* add panel to the GUI so the user can see it */
        add(panel);
    }

    /************************************************************************
     * This function returns the current turn as an integer value.
     ************************************************************************/
    public int getCurrentTurn() {
        if(playerTurn == Turn.P1Turn)
            return 1;
        else
            return 2;
    }
    /************************************************************************
     * This function returns the value of the most recent move.
     ************************************************************************/
    public int getMove() { return lastMove; }

    /************************************************************************
     * This function returns the value of the new move flag.
     ************************************************************************/
    public boolean newMove() { return newMove; }

    /************************************************************************
     * This function relinquishes the current turn to the other player.
     ************************************************************************/
    public void switchTurn(int player) {
        if(player == 1)
            playerTurn = Turn.P2Turn;
        else
            playerTurn = Turn.P1Turn;
        model.setPlayerTurn(playerTurn);
    }

    /************************************************************************
     * This function checks whether it is currently the active player's turn.
     ************************************************************************/
    public boolean myTurn() {
        if(playerTurn == Turn.P1Turn && activePlayer == 1)
            return true;
        else if(playerTurn == Turn.P2Turn && activePlayer == 2)
            return true;
        else
            return false;
    }

    /************************************************************************
     * This function clears the new move flag when the move is read by the
     * game client.
     ************************************************************************/
    public void clearNewMove() { newMove = false; }

    public boolean setChipFromServer(int col) {
        boolean result = false;

        /* Get the next open space in the picked row */
        int row = 5-model.getTakenSpaces(col);
        /* Do the move, and make sure its valid before we do anything else */
        boolean valid = model.placeChip(col);
        if(valid) {
            if(playerTurn == Turn.P1Turn) {
                board[row][col].setText("P1");
                board[row][col].setBackground(Color.BLUE);
                board[row][col].setOpaque(true);
                board[row][col].setFont(new Font("Dialog", Font.BOLD, 12));
            } else {
                board[row][col].setText("P2");
                board[row][col].setBackground(Color.red);
                board[row][col].setOpaque(true);
                board[row][col].setFont(new Font("Dialog", Font.BOLD, 12));
            }
            WinState winState = model.checkWinCondition();
            if(winState == winState.TIE) {
                System.out.println("Controller says TIE!");
                JOptionPane.showMessageDialog(null, "The game has ended in a tie",
                        "No moves remaining!", JOptionPane.INFORMATION_MESSAGE);
            } else if(winState == winState.P1Win) {
                System.out.println("Controller says P1 WINS!");
                JOptionPane.showMessageDialog(null, "Player 1 has won",
                        "4 in a Row Found!", JOptionPane.INFORMATION_MESSAGE);
            } else if(winState == winState.P2Win) {
                System.out.println("Controller says P2 WINS!");
                JOptionPane.showMessageDialog(null, "Player 2 has won",
                        "4 in a Row Found!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if(playerTurn == Turn.P1Turn) {
                    playerTurn = Turn.P2Turn;
                    whosTurn.setText("Player 2's Turn");
                } else {
                    playerTurn = Turn.P1Turn;
                    whosTurn.setText("Player 1's Turn");
                }
                model.setPlayerTurn(playerTurn);
            }
            result = true;
        }
        return result;
    }



/**********************************************************************
 * The button listener class is used to determine when the player
 * has pressed a button corresponding to the column they want their
 * piece in.
 *********************************************************************/
    private class ButtonListener implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            boolean valid = false;
            for(int j=0; j<7; j++) {
                if(board[6][j] == e.getSource()) {

                    /* Get the next open space in the picked row */
                    int i = 5-model.getTakenSpaces(j);
                    /* Do the move, and make sure its valid before we do anything else */
                    if(myTurn())
                        valid = model.placeChip(j);
                    if(valid) {
                        lastMove = j;
                        newMove = true;
                        if(playerTurn == Turn.P1Turn) {
                            board[i][j].setText("P1");
                            board[i][j].setBackground(Color.BLUE);
                        } else {
                            board[i][j].setText("P2");
                            board[i][j].setBackground(Color.red);
                        }
                        board[i][j].setOpaque(true);
                        board[i][j].setFont(new Font("Dialog", Font.BOLD, 12));
                        WinState winState = model.checkWinCondition();
                        if(winState == winState.TIE) {
                            System.out.println("Controller says TIE!");
                            JOptionPane.showMessageDialog(null, "The game has ended in a tie",
                                    "No moves remaining!", JOptionPane.INFORMATION_MESSAGE);
                        } else if(winState == winState.P1Win) {
                            System.out.println("Controller says P1 WINS!");
                            JOptionPane.showMessageDialog(null, "Player 1 has won",
                                    "4 in a Row Found!", JOptionPane.INFORMATION_MESSAGE);
                        } else if(winState == winState.P2Win) {
                            System.out.println("Controller says P2 WINS!");
                            JOptionPane.showMessageDialog(null, "Player 2 has won",
                                    "4 in a Row Found!", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            if(playerTurn == Turn.P1Turn) {
                                //playerTurn = Turn.P2Turn;
                                whosTurn.setText("Player 2's Turn");
                            } else {
                                //playerTurn = Turn.P1Turn;
                                whosTurn.setText("Player 1's Turn");
                            }
                        }
                    }
                }
            }
        }
    }
}
