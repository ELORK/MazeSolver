/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeproject;

/**
 *
 * @author 15LHolland
 */

/* MazeProject Class - This is where all the components of the project are to 
   be stored and instantiated. It will contain the window of the maze and will
   be the entry point into the main program.
*/
public class MazeProject {
    // This is the maze object
    public Window window;
    public static void main(String[] args) {
        //instantiate a maze object
        MazeProject mp = new MazeProject("Maze Solver", 1400, 800);
    }
    
    /*  constructor which initializes the window, once window is initialized
        it will set it to visible, so the user can see the window.
    */
    public MazeProject(String title, int w, int h)
    {
        //setup all the components of the MazeProject class
        window = new Window(title, w, h);
        window.setVisible(true);
    }
}