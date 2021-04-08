import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class GameController extends JPanel {
    private JButton[][] board;
    private JLabel whosTurn;

    /** Constructor for GameController Handles the starting of the game */
    public GameController() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        JPanel[] panelArray = new JPanel[2];
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        panelArray[0] = new JPanel();
        panel.add(panelArray[0], c);

        c.gridx = 0;
        c.gridy = 1;
        panelArray[1] = new JPanel();
        panel.add(panelArray[1], c);

        panelArray[1].setLayout(new GridLayout(7,7));

        whosTurn = new JLabel("Who's Turn?");
        panelArray[0].add(whosTurn);

        board = new JButton[7][7];
        for(int i=0; i<7; i++) {
            for(int j=0; j<7; j++) {
                if(i < 6) {
                    board[i][j] = new JButton("EMPTY");
                    board[i][j].setEnabled(false);
                    panelArray[1].add(board[i][j]);
                }
                else {
                    board[i][j] = new JButton("PICK ME!");
                   panelArray[1].add(board[i][j]);
                }
            }
        }
        
        add(panel);
    }
}
