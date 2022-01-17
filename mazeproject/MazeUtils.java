/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeproject;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author 15LHolland
 */

/* MazeUtils Class - This class will be an extension for the JPanel class
   and will act as a container for all the buttons inside the program.
*/
public class MazeUtils extends JPanel{
    public MazeUtils(int x, int y, int w, int h)
    {
        setBounds(x, y, w , h);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
