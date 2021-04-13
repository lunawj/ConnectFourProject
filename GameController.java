import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**********************************************************************
 * Controller class for the ConnectFour game. 
 *********************************************************************/
public class GameController extends JPanel {
    /** The board that contains the connect 4 pieces */
    private JButton[][] board;
    /** The label saying whos turn it is */
    private JLabel whosTurn;

    private GameModel model = new GameModel();

    Turn playerTurn;

    /** Constructor for GameController handles the starting of the game */
    public GameController() {
        ButtonListener listener = new ButtonListener();
        playerTurn = Turn.P1Turn;
        model.setPlayerTurn(playerTurn);

        /** panel is the overarching panel eveything is added to */
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
        whosTurn = new JLabel("Who's Turn?");
        panelArray[0].add(whosTurn);

        /* Create the board and buttons for each column */
        board = new JButton[7][7];
        for(int i=0; i<7; i++) {
            for(int j=0; j<7; j++) {
                if(i <6) {
                    board[i][j] = new JButton("EMPTY");
                    board[i][j].setEnabled(false);
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


/**********************************************************************
 * The button listener class is used to determine when the player
 * has pressed a button corresponding to the column they want their
 * piece in.
 *********************************************************************/
    private class ButtonListener implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
        
            for(int j=0; j<7; j++) {
                if(board[6][j] == e.getSource()) {

                    /* Get the next open space in the picked row */
                    int i = 5-model.getOpenSpaces(j);
                    /* Do the move, and make sure its valid before we do anything else */
                    boolean valid = model.placeChip(j);
                    if(valid) {
                        if(playerTurn == Turn.P1Turn) {
                            board[i][j].setText("P1");
                        } else {
                            board[i][j].setText("P2");
                        }
                        WinState winState = model.checkWinCondition();
                        if(winState == winState.TIE) {
                            System.out.println("Controller says TIE!");
                        } else if(winState == winState.P1Win) {
                            System.out.println("Controller says P1 WINS!");
                        } else if(winState == winState.P2Win) {
                            System.out.println("Controller says P2 WINS!");
                        } else {
                            if(playerTurn == Turn.P1Turn)
                                playerTurn = Turn.P2Turn;
                            else
                                playerTurn = Turn.P1Turn;
                            model.setPlayerTurn(playerTurn);
                        }
                    }
                }
            }
        }
    }
}
