/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeproject;

import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author 15LHolland
 */
public class StatMaker {
    //stores the time taken to solve the maze
    public float stat_time;
    //stores the number of cells traversed before the maze was solved
    public int stat_cell_count;
    //stores the size of the smallest path
    public int stat_path_size;
    //stores the number of solutions to the maze
    public int stat_path_count;
    //stores the algorithms which was used to solve the maze
    public String stat_solve;
    //stores the algorithm which was used to generate the maze
    public String stat_generate;
    
    //constructor function which would be used to 
    //initialize all the attributes
    public StatMaker() 
    {
        stat_time = 0.f;
        stat_cell_count = 0;
        stat_path_size = 0;
        stat_path_count = 0;
        stat_solve = "None";
        stat_generate = "None";
    }
    
    //writes to the specified file, first creating the file on the user's
    //hard drive, to make sure the file exists, then writes to the file
    public String write_stats(String filename)
    {
        //try to open the specified file 
        try {
            if (!filename.contains(".csv")) 
                filename += ".csv";
            File filp = new File(filename);
            //if the file was newly created
            if (filp.createNewFile()) {
                //simple debug test to clarify new file
                System.out.println("Created new file: " + filp.getName());
            } else {
                //simple debug test to clarify not new file
                System.out.println("File already exists: " + filp.getName());
            }
            //if there was an error
        } catch (Exception e) {
            //return the error message
            return e.getMessage();
        }
        
        try {
            //open the file, anticipating that is exists
            FileWriter filp = new FileWriter(filename);
            //write data to the file in CSV format
            filp.write("Solve Algorithm, " + stat_solve + "\n");
            filp.write("Generation Algorithm, " + stat_generate + "\n");
            filp.write("Time, " + stat_time + "\n");
            filp.write("Cell Count, " + stat_cell_count + "\n");
            filp.write("Path Size, " + stat_path_size + "\n");
            filp.write("Path Count, " + stat_path_count + "\n");
            //close the file, flush the output to the file
            filp.close();
            //if there was an error
        } catch (Exception e) {
            //return the error message
            return e.getMessage();
        }
        //no error needs to be reported
        return null;
    }
}
