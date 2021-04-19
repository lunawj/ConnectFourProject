/*************************************************************************
 * Course: CIS457-10
 * File:   Semester Project
 * Name:   Charlie Dorn, Kendra Haan, Wesley Luna, Justin Von Kulajta Winn
 * Date:   4/19/2021
 *
 * Description: The view for the Connect4 game. This class creates the frame
 * for the game board and displays it to the user.
 **************************************************************************/
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameView {
    /* Main method for client side. Sets up GUI for the 
       Connect4 game and creates an instance of the controller
       used to run the clients side */
    //public static void main(final String[] args) {
    public GameController controller;

    public boolean windowClosed;

    public boolean windowClosed() { return windowClosed; }


    public GameView(int player) {
        /* Create the JFrame, make it exit when close is pressed */
        windowClosed = false;
        JFrame frame = new JFrame("ConnectFour!");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowClosed = true;
            }
        });
        controller = new GameController(player);

        /* Add controller to JFrame created for program */
        frame.getContentPane().add(controller);
        frame.setResizable(true);

        /* Pack to good dimensions for game */
        frame.pack();

        /* Set the frame to visible so it can be interacted with */
        frame.setVisible(true);
    }
}
