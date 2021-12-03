/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.util.Timer;
import javax.swing.JToggleButton;

/**
 *
 * @author 15LHolland
 */


/* Window Class - the window class will be used to store all the components 
   which are to be presented to the user such as the maze and the buttons
*/

public class Window extends JFrame
{
    //store the current width and height of the maze
    private int w, h;
    //the maze component of the window which will be displayed and traversed.
    private Maze maze;
    private int maze_w;
    private int maze_h;
    private int maze_pad_y;
    
    //the Utils for for which buttons and combo boxes are placed inside
    private MazeUtils leftUtils;
    private MazeUtils rightUtils;
    
    //buttons for the left handside 
    
    //when clicked, the currently selected generation algorithm will be used
    private JButton createMazeBtn;
    //when clicked, the currently selected solve algorithm will be used
    private JButton solveMazeBtn;
    //when clicked, the currently entered user solution will be used
    private JButton validateSolnBtn;
    //when clicked, the user is prompted to select a file to be read from 
    private JButton loadMazeBtn;
    
    //buttons for the right handside
    
    //when clicked, data is saved to a file selected by the user
    private JButton saveStatsBtn;
    //when clicked, the maze is saved to a file selected by the user
    private JButton saveMazeBtn;
    //when clicked, a selection of algorithms can be viewed and selected
    private JComboBox selectSolveCombo;
    //when clicked, a selection of algorithms can be viewed and selected
    private JComboBox selectMazeCombo;

    /*a solution is entered here, this will be checked when the validateSolnBtn
      is pressed
    */
    private JTextField userSolnTxt;
    
    //the statMaker which saves statistical data to a file.
    private StatMaker statMaker;
    
    //the mazeMaker which saves the maze and corresponding solution to a file
    private MazeMaker mazeMaker;
    
    //additional window which contains instructions to use the software
    private ManPages manual;
    
    //button which opens the man pages when pressed
    private JButton manPageOpen;
    
    //toggle button to toggle the speed of the functions performed
    private JToggleButton toggleSpeed;
    
    /* This will initialize all the attributes of the Window
    */
    public Window(String title, int w, int h)
    {
        //simple window initialization
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension minSize = new Dimension(w, h);
        setMinimumSize(minSize);
        setLayout(null);
        this.w = getWidth();
        this.h = getHeight();
        
        /*Preferred background colours:
          =============================
          Light Green: #7affa0
          Light Blue:  #7ae7ff
          Light Pink:  #ff7af4
          =============================
        */
        
        Color light_green = new Color(0x7a, 0xff, 0xa0);
        
        getContentPane().setBackground(light_green);
        
        //Maze initialization
        maze_w = 540;
        maze_h = 540;
        maze_pad_y = 160;
        maze = new Maze((this.w-maze_w)/2, (this.h-maze_h-maze_pad_y)/2, 
                maze_w, maze_h);
        
        //initialize the statMaker
        statMaker = new StatMaker();
        
        //initialize the mazeMaker
        mazeMaker = new MazeMaker();
        
        
        //Buttons initialization - left
        createMazeBtn = new JButton("Generate Maze");
        createMazeBtn.addActionListener(e -> generate());
        solveMazeBtn  = new JButton("Solve Maze");
        solveMazeBtn.addActionListener(e -> solve());
        validateSolnBtn = new JButton("Validate Solution");
        loadMazeBtn = new JButton("Load Maze");
        
        createMazeBtn.setFont(new Font("Sans-Serif", 24, 24));
        solveMazeBtn.setFont(new Font("Sans-Serif", 24, 24));
        validateSolnBtn.setFont(new Font("Sans-Serif", 24, 24));
        loadMazeBtn.setFont(new Font("Sans-Serif", 24, 24));
        
        validateSolnBtn.addActionListener(e -> soln_validate());
        loadMazeBtn.addActionListener(e -> load_maze());
        
        //Buttons initialization - right
        saveStatsBtn = new JButton("Save Current Stats");
        saveMazeBtn = new JButton("Save Current Maze");
        
        saveStatsBtn.setFont(new Font("Sans-Serif", 24, 24));
        saveMazeBtn.setFont(new Font("Sans-Serif", 24, 24));
        
        saveStatsBtn.addActionListener(e -> save_stats());
        saveMazeBtn.addActionListener(e -> save_maze());
        
        //ComboBox Initialization - right
        selectSolveCombo = new JComboBox();
        selectMazeCombo = new JComboBox();
        
        selectSolveCombo.addItem("BFS");
        selectSolveCombo.addItem("Recursive");
        
        selectMazeCombo.addItem("DFS");
        selectMazeCombo.addItem("Prims");
        
        selectSolveCombo.setFont(new Font("Sans-Serif", 24, 24));
        selectMazeCombo.setFont(new Font("Sans-Serif", 24, 24));
        
        //JTextField initilization - bottom
        userSolnTxt = new JTextField();
        userSolnTxt.setBounds((this.w-550)/2, (this.h-200), 550, 100);
        userSolnTxt.setFont(new Font("SansSerif", 0, 24));
        
        //Utils initialization
        int utils_w = (int)(this.w * 0.2);
        int utils_h = (int)(this.h * 0.6);
        int utils_w_pad = 50;
        
        //LeftUtils initialization
        leftUtils = new MazeUtils(utils_w_pad, (this.h - utils_h)/2,
                utils_w, utils_h);
        leftUtils.setLayout(new GridLayout(4, 1, 10, 10));
        leftUtils.setBorder(null);
        leftUtils.add(createMazeBtn);
        leftUtils.add(solveMazeBtn);
        leftUtils.add(validateSolnBtn);
        leftUtils.add(loadMazeBtn);
        leftUtils.setBackground(light_green);
        
        //RightUtils initialization 
        rightUtils = new MazeUtils(this.w - utils_w - utils_w_pad, 
                (this.h - utils_h)/2, utils_w, utils_h);
        rightUtils.setLayout(new GridLayout(4, 1, 10, 10));
        rightUtils.setBorder(null);
        rightUtils.add(saveStatsBtn);
        rightUtils.add(saveMazeBtn);
        rightUtils.add(selectSolveCombo);
        rightUtils.add(selectMazeCombo);
        rightUtils.setBackground(light_green);
        
        //man pages button initialisation
        manPageOpen = new JButton("Manual");
        manPageOpen.setBounds(this.w-210, this.h-110, 180, 60);
        manPageOpen.addActionListener(e -> openManPage());
        
        //toggle button initialisation
        toggleSpeed = new JToggleButton();
        toggleSpeed.setBounds(10, this.h-110, 180, 60);
        toggleSpeed.setText("Slow (OFF)");
        toggleSpeed.addActionListener(e -> speedToggler());
        
        //adding everything to the window
        add(maze);
        add(leftUtils);
        add(rightUtils);
        add(userSolnTxt);
        add(manPageOpen);
        add(toggleSpeed);
    }
    
    public void soln_validate()
    {
        //get the parser return code from the function
        int []codes = maze.validate(userSolnTxt.getText());
        
        //get the maze code
        int maze_code = codes[SolnParser.MAZE_CODE];
        
        //get the parser code
        int parser_code = codes[SolnParser.PARSER_CODE];
        
        //string where error messages are concatenated to.
        String error_msg = "";
        
        //interpret the maze code
        switch(maze_code) {
            //if the 'wall hit' error code is found
            case Maze.MAZE_HIT_WALL:
                //print error message
                error_msg += "Maze Error: Wall was hit";
                break;
            //if the 'out of bounds' error code is found
            case Maze.MAZE_OUT_OF_BOUNDS:
                //print error message
                error_msg += "Maze Error: Out of bounds";
                break;
            // if the 'maze unsolved' error is found
            case Maze.MAZE_UNSOLVED:
                //print error message
                error_msg += "Maze Error: Maze unsolved";
                break;
            //if the 'maze solved' code is found
            case Maze.MAZE_SOLVED:
                break;
        }
        
        //interpret the parser code
        switch (parser_code) {
            //if the 'invalid magnitude' error code is found
            case SolnParser.PARSER_INVALID_MAGNITUDE:
                //print error message
                error_msg += "\nParser Error: Invalid magnitude";
                break;
            //if the 'invalid move' error code is found
            case SolnParser.PARSER_INVALID_MOVE:
                //print error message
                error_msg += "\nParser Error: Invalid move";
                break;
            //if the 'parser success' code is found
            case SolnParser.PARSER_SUCCESS:
                //print success message
                break;
        }
        
        //if an error message needs to be reported, there is an error message
        //inside the error_msg variable
        if (error_msg.length() > 0)
            //display the error message
            JOptionPane.showMessageDialog(this, error_msg);
    }
    
    public void save_stats()
    {   
        //prompt the user to enter a file, store the return value in a string
        String filename = JOptionPane.showInputDialog("Enter the file's "
                + "name you want to save the data to: ");
        //pass the filename into the write procedure
        statMaker.write_stats(filename);
    }
    
    public void save_maze()
    {
        //get the maze in '1's and '0's format
        String maze_str = maze.maze_str();
        //get the shortest path string
        String maze_solution = maze.shortest_path_str();
        
        //prompt the user to enter a file, store the return value in a string
        String filename = JOptionPane.showInputDialog("Enter the file's "
                + "name you want to save the maze to: ");
        
        if (filename == null) {
            return;
        }
        
        //store the solution and maze into the mazeMaker
        mazeMaker.load_solution(maze_solution);
        mazeMaker.load_maze(maze_str);
        
        //write to the file specified
        String error = mazeMaker.write_maze(filename);
        
        //handle any errors by presenting them to the user
        if (error != null)
            JOptionPane.showMessageDialog(this, error);
    }
    
    public void load_maze()
    {
        //show GUI to let user select a file on their computer
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        
        //get the name of the file the user selected
        File filp = fc.getSelectedFile();
        
        //if a file was not selected, present error, return from the 
        //function to prevent further errors
        if (filp == null) {
            JOptionPane.showMessageDialog(this, "File not selected!");
            return;
        }
        
        //get the name of the file 
        String filename = filp.getName();
        
        //store the error returned from the open maze function
        String error = null;
        
        //if the file was selected
        error = mazeMaker.open_maze(filename, maze);
        
        //handle any errors by presenting them to the user
        if (error != null)
            JOptionPane.showMessageDialog(this, error);
    }
    
    public void solve()
    {
        //get the string from within the combo box 
        String solve_str = selectSolveCombo.getSelectedItem().toString();
        
        //if the algorithm requested is BFS
        if (solve_str == "BFS") {
            //call the BFS algorithm
            maze.BFS_solve(statMaker);
        //if the algorithm requested is the recursive algorithm
        } else if (solve_str == "Recursive") {
            //call the recursive algorithm
            maze.recursive_solve(statMaker);
        }
    }
    
    public void generate()
    {
        //get the string from within the combo box 
        String maze_str = selectMazeCombo.getSelectedItem().toString();
        
        //if the algorithm requested is DFS
        if (maze_str == "DFS") {
            //call the DFS algorithm
            //maze.DFS(statMaker);
            
            //if the button is currently pressed
            if (toggleSpeed.isSelected()) {
                for (int i = 0; i < maze.matrix.size(); i++) {
                    Cell curr = maze.matrix.get(i);
                    curr.set_state(Cell.CELL_WALL);
                }
                
                int start_node_index = 1 + maze.matrix_w;
                int end_node_index = (maze.matrix_w * (maze.matrix_w - 1) - 2);
                
                int []index_ref = new int[1];
                index_ref[0] = start_node_index;
                
                Stack stack = new Stack();
                
                stack.push(index_ref[0]);
                
                Timer timer = new Timer();
                
                TimerTask dfs_task = new TimerTask() {
                    public void run()
                    {
                        if (!stack.isEmpty()) {
                            maze.DFS_frame(statMaker, stack, index_ref);
                        } else {
                            timer.cancel();
                            //reset the start and end node states
                            maze.matrix.get(start_node_index).set_state(
                                    Cell.CELL_START);
                            maze.matrix.get(end_node_index).set_state(
                                    Cell.CELL_END);
                        }
                    }
                };
                
                timer.schedule(dfs_task, 60, 60);
            //if the button is not currently pressed
            } else {
                //run the algorithm normally, not fast
                maze.DFS(statMaker);
            }
        } else if (maze_str == "Prims") {
            //if the button is currently pressed
            if (toggleSpeed.isSelected()) {
                statMaker.stat_generate = "Prims";
                //set all the cells in the maze to walls
                for (int i = 0; i < maze.matrix.size(); i++) {
                    Cell curr = maze.matrix.get(i);
                    curr.set_state(Cell.CELL_WALL);
                }

                //global array list to maintain the storage of cells
                ArrayList<Pair> cells = new ArrayList<>();

                //the start and end node indexes
                int start_node_index = 1 + maze.matrix_w;
                int end_node_index =  (maze.matrix_w * (maze.matrix_h - 1) - 2);

                //store the initial pair, which is the starting position 
                //of the generation
                cells.add(new Pair((1 + maze.matrix_w), (1 + maze.matrix_w)));

                //timer variable used to schedule the procedure
                Timer timer = new Timer();
                
                //the task which is going to be scheduled
                TimerTask prims_task = new TimerTask() 
                {
                    public void run()
                    {        
                        //if there are still cells to process
                        if (!cells.isEmpty()) {
                            //show next frame
                            maze.prims_frame(statMaker, cells);
                        } else {
                            //cancel the timer
                            timer.cancel();
                            //reset the start and end node states
                            maze.matrix.get(start_node_index).set_state(
                                    Cell.CELL_START);
                            maze.matrix.get(end_node_index).set_state(
                                    Cell.CELL_END);
                        }
                    }
                };

                //schedule prims algorithm every second
                timer.schedule(prims_task, 60, 60);
            } else {
                //standard generation, no delay
                maze.prims(statMaker);
            }
        }
    }
    
    public void openManPage()
    {
        //instantiate the manual
        manual = new ManPages("Manual", 640, 640);
        
        //set the window to visible
        manual.setVisible(true);
    }
    
    public void speedToggler()
    {
        //if the button is pressed
        if (toggleSpeed.isSelected()) {
            //change the text to tell the user it is on
            toggleSpeed.setText("Slow (ON)");
        } else {
            //change the text to tell the user it is off
            toggleSpeed.setText("Slow (OFF)");
        }
    }
}