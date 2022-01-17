package mazeproject;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 15LHolland
 */

/* Cell Class - used to fill up the maze matrix, contains a state attribute 
   which defines the state of which the cell is within the maze, such as
   a path state or wall state. 
*/
public class Cell extends JPanel{
    public int flags;
    
    //Cell constants used to represent specific cell states
    static final int CELL_PATH = 2;
    static final int CELL_WALL = 4;
    static final int CELL_START = 8;
    static final int CELL_END = 16;
    static final int CELL_VISITED = 32;
    static final int CELL_SEARCHED = 64;
    
    public Cell(int state, int w, int h)
    {
        //based on the current state, change the colour of the cell
        switch (state) {
            //if the cell is a path
            case Cell.CELL_PATH:
                setBackground(Color.WHITE);
                break;
            //if the cell is a wall
            case Cell.CELL_WALL:
                setBackground(Color.BLACK);
                break;
            //if the cell is the start node
            case Cell.CELL_START:
                setBackground(Color.RED);
                break;
            //if the cell is the end node
            case Cell.CELL_END:
                setBackground(Color.GREEN);
                break;
            //if the cell is a searched node
            case Cell.CELL_SEARCHED:
                setBackground(new Color(0xff, 0x00, 0xff));
                break;
        }
        //define a border for each cell, easily determine where the cells are 
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //define a fixed size of the cell within the maze.
        setSize(w, h);
        //set the current state of the cell
        flags = state;
    }
    
    public void set_state(int state)
    {
        flags = state;
        switch (state) {
            //if the cell is a path
            case Cell.CELL_PATH:
                setBackground(Color.WHITE);
                break;
            //if the cell is a wall
            case Cell.CELL_WALL:
                setBackground(Color.BLACK);
                break;
            //if the cell is the start node
            case Cell.CELL_START:
                setBackground(Color.RED);
                break;
            //if the cell is the end node
            case Cell.CELL_END:
                setBackground(Color.GREEN);
                break;
            //if the cell is a searched node
            case Cell.CELL_SEARCHED:
                //the pink colour, this is a volatile choice 
                setBackground(new Color(0xff, 0x00, 0xff));
                break;
        }
    }
    
    public void add_state(int state)
    {
        /* bitwise OR to set the bit of the state
        */
        flags |= state;
    }
    
    public void remove_state(int state)
    {
        /* flip all the bits of the state and bitwise AND with the current 
           state to remove the state
        */
        flags &= ~state;
    }
    
    public boolean check_state(int state)
    {
        /* bitwise AND to check the bit of the state
        */
        return ((flags & state) > 0);
    }
    
    public void increment_visit_state()
    {
        //set the background colour to a brigher colour, further and further
        //from white 
        setBackground(new Color(0xff, 0x00, 
                0xff));
    }
}
