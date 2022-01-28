/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author 15LHolland
 */
public class MazeMaker {
    private String soln;
    private String maze;
    
    public MazeMaker()
    {
        soln = "";
        maze = "";
    }
    
    public void load_solution(String soln)
    {
        this.soln = soln;
    }
    
    public void load_maze(String maze)
    {
        this.maze = maze;
    }
    
    public String write_maze(String filename)
    {
        try {
            //check if .maze is not already added by the user
            if (!filename.contains(".maze")) {
                //add it to the filename
                filename += ".maze";
            }
            
            //instantiate a file object
            File filp = new File(filename);
            
            //create the file,  returns 0 if already created
            if (filp.createNewFile()) {
                System.out.println("Created new File: " + filp.getName());
            } else {
                System.out.println("File already exists: " + filp.getName());
            }
        //if there were errors, return the error message
        } catch (Exception e) {
            return e.getMessage();
        }
        
        try {
            //instantiate a FileWriter object
            FileWriter filp = new FileWriter(filename);
            //write the maze and solution to the file 
            filp.write(this.maze);
            filp.write(this.soln + '\n');
            //close the file 
            filp.close();
        //report any errors
        } catch (Exception e) {
            return e.getMessage();
        }
        
        //no errors
        return null;
    }
    
    public String open_maze(String filename, Maze maze)
    {
        //if the file selected has the correct extension 
        if (filename.contains(".maze")){
            try {
                //open the file 
                File file = new File(filename);
                //check if the file doesn't exist
                if (!file.exists())
                    return "File does not exist!";
            //report any errors
            } catch (Exception e) {
                return e.getMessage();
            }
        //let the user know that the file selected is invalid
        } else {
            return "File is not in the valid format!";
        }
        
        try {
            //copy buffer to hold the lines from the bufferedReader
            String copy = "";
            
            //stores the maze
            String maze_str = "";
            
            //stores the solution
            String maze_solution = "";
            
            //instantiate a FileReader object
            FileReader reader = new FileReader(filename);
            //open a BufferedReader object
            BufferedReader buffer = new BufferedReader(reader);
            
            //read from the buffer to get the first 27 lines for the maze
            for (int i = 0; i < 27 && (copy = buffer.readLine()) != null; i++) {
                //copy the binary format of the line into the maze_str buffer
                maze_str += maze.hex_to_bin(copy);
            }
            
            //get the next line and store that in the maze_solution buffer
            maze_solution = buffer.readLine();
            
            //call the maze_load function on the maze_str to load the maze
            //into the physical maze 
            maze.maze_load(maze_str);
            
            //close the reader and buffer
            reader.close();
            buffer.close();
        
        //report any errors
        } catch (Exception e) {
            return e.getMessage();
        }
        
        //no errors 
        return null;
    }
}
