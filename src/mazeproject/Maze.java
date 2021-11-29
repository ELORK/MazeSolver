/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeproject;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author 15LHolland
 */

/* Maze Class - this class will contain the maze, the matrix of cells 
   which are to be traversed, and contain all the subroutines which are 
   used to traverse those matrices.
*/
public class Maze extends JPanel{
    // this will store all the cells inside the maze.
    public ArrayList<Cell> matrix;
    
    //some constants used to define the result of a user solution
    public static final int MAZE_SOLVED = 2<<16;
    public static final int MAZE_HIT_WALL = 2<<17;
    public static final int MAZE_UNSOLVED = 2<<18;
    public static final int MAZE_OUT_OF_BOUNDS = 2<<19;
    
    public int matrix_w;
    public int matrix_h;
    public int start_node_pos;
    public int end_node_pos;
    public int cell_w;
    public int cell_h;
    
    //used to parse the solution
    private SolnParser parser;
    
    public Maze(int x, int y, int w, int h)
    {
        //initialize the maze structure
        matrix = new ArrayList<>();
        setBounds(x, y, w, h);
        setLayout(new GridLayout(w/20, h/20));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //this is the width and height of the nodes 
        cell_w = 20;
        cell_h = 20;
        
        //this is the number of nodes in each row and column of the maze
        matrix_w = w/cell_w;
        matrix_h = h/cell_h;
        
        start_node_pos = (1 + matrix_w * 1);
        end_node_pos = (matrix_w * (matrix_h - 1) - 2);
        
        //populate the matrix with cells, with state set to path.
        for (int i = 0; i < (matrix_w * matrix_h); ++i) {
            Cell tmp = new Cell(Cell.CELL_PATH, cell_w, cell_h);
            if (i == start_node_pos)
                tmp.set_state(Cell.CELL_START);
            else if (i == end_node_pos)
                tmp.set_state(Cell.CELL_END);
            matrix.add(tmp);
            add(tmp);
        }
        
        //initialise the parser
        parser = new SolnParser(null);
    }
    
    /* This iterates through the maze and sets all the nodes to walls
    */
    public void maze_set()
    {
        int i;
        
        //iterate through the maze, setting them all to walls
        for (i = 0; i < matrix.size(); ++i) {
            matrix.get(i).set_state(Cell.CELL_WALL);
        }
        
        //set the start node as the end node  
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        
        //set the end node as the end node 
        matrix.get(end_node_pos).set_state(Cell.CELL_END);
    }
    
    /* This iterates through the maze and sets all the nodes to paths
    */
    public void maze_reset()
    {
        int i;
        
        //iterate through the maze and set all the nodes to paths
        for (i = 0; i < matrix.size(); ++i) {
            matrix.get(i).set_state(Cell.CELL_PATH);
        }
        
        //set the start node as the start node
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        
        //set the end node as the end node 
        matrix.get(end_node_pos).set_state(Cell.CELL_END);
    }
    
    public void maze_refresh()
    {
        for (int i = 0; i < matrix.size(); i++) {
            matrix.get(i).remove_state(Cell.CELL_VISITED);
        }
    }
    
    //the Depth-First-Search Algorithm
    public void DFS(StatMaker statMaker)
    {   
        //set all the nodes in the maze to a wall
        maze_set();
        
        //initialize the stack
        Stack stack = new Stack();
        
        statMaker.stat_generate = "DFS";
        
        //initialize the random number generator
        Random rand = new Random();
        
        //set the current node to the end node, and begin search from here
        int index = end_node_pos;
        Cell curr = matrix.get(index);
        ArrayList<Integer> neighbours = null;
        //set the current node as visited.
        curr.add_state(Cell.CELL_VISITED);
        do {
            //get all adjacent cells of the current node 
            neighbours = get_neighbours(index, 2, Cell.CELL_VISITED);
            //if there are any non-visited neighbours
            if (!neighbours.isEmpty()) {
                //get the index of the randomly selected neighbour
                int tmp_index = neighbours.get(rand.nextInt(neighbours.size()));
                //get the cell of the randomly selected neighbour
                Cell tmp = matrix.get(tmp_index);
                //push the index of the current cell on the stack
                stack.push(index);
                
                //check how the index compares to find the adjacent node 
                //which needs to be also set.
                int d = index_compare(index, tmp_index);
                switch (d) {
                    //meaning that the index is to the right of tmp_index
                    case 0:
                        //set the left-adjacent cell to a path
                        matrix.get(index-1).set_state(Cell.CELL_PATH);
                        break;
                    //meaing that the index is to the left of tmp_index
                    case 1:
                        //set the right-adjacent cell to a path
                        matrix.get(index+1).set_state(Cell.CELL_PATH);
                        break;
                    //meaning that the index is below tmp_index
                    case 2:
                        //set the up-adjacent cell to a path
                        matrix.get(index-(matrix_w)).set_state(Cell.CELL_PATH);
                        break;
                    //meaning that the index is above tmp_index
                    case 3:
                        //set the down-adjacent cell to a path
                        matrix.get(index+(matrix_w)).set_state(Cell.CELL_PATH);
                        break;
                }
                
                //the new current index is the random neighbour selected
                index = tmp_index;
                //the current cell is the random neighbour selected
                curr = tmp;
                //set the state of the new current cell to a visited path
                curr.set_state(Cell.CELL_PATH);
                curr.add_state(Cell.CELL_VISITED);
             //if there are no neighbours for the current cell
            } else {
                //set the new current index to the index on top of the stack
                index = (int)stack.pop();
            }
        //end the generation when the stack is empty
        } while (!stack.isEmpty()); 
        //set the state of the start node cell back to the start node
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        
        //remove the visited state, so the algorithm works once applied again
        for (int i = 0; i < matrix.size(); ++i)
            matrix.get(i).remove_state(Cell.CELL_VISITED);
        
        //save the number of paths found into the statMaker object
        statMaker.stat_path_count = count_paths();
    }
    
    //the Bredth-First-Search algorithm, might need to reimplement
    //seems wrong, does not work as intended
    public void BFS()
    {
        //set all the nodes in the maze to a wall
        maze_set();
        
        //initialize the queue
        Queue queue = new LinkedList<>();
        
        //initialize the random number generator
        Random rand = new Random();
        
        //set the current node to the end node, and begin search from here
        int index = end_node_pos;
        Cell curr = matrix.get(index);
        ArrayList<Integer> neighbours = null;
        //set the current node as visited.
        curr.add_state(Cell.CELL_VISITED);
        do {
            //get all adjacent cells of the current node 
            neighbours = get_neighbours(index, 2, Cell.CELL_VISITED);
            //if there are any non-visited neighbours
            if (!neighbours.isEmpty()) {
                //get the index of the randomly selected neighbour
                int tmp_index = neighbours.get(rand.nextInt(neighbours.size()));
                //get the cell of the randomly selected neighbour
                Cell tmp = matrix.get(tmp_index);
                //add the index of the current cell into the queue
                queue.add(index);
                
                //check how the index compares to find the adjacent node 
                //which needs to be also set.
                int d = index_compare(index, tmp_index);
                switch (d) {
                    //meaning that the index is to the right of tmp_index
                    case 0:
                        //set the left-adjacent cell to a path
                        matrix.get(index-1).set_state(Cell.CELL_PATH);
                        break;
                    //meaing that the index is to the left of tmp_index
                    case 1:
                        //set the right-adjacent cell to a path
                        matrix.get(index+1).set_state(Cell.CELL_PATH);
                        break;
                    //meaning that the index is below tmp_index
                    case 2:
                        //set the up-adjacent cell to a path
                        matrix.get(index-(matrix_w)).set_state(Cell.CELL_PATH);
                        break;
                    //meaning that the index is above tmp_index
                    case 3:
                        //set the down-adjacent cell to a path
                        matrix.get(index+(matrix_w)).set_state(Cell.CELL_PATH);
                        break;
                }
                
                //the new current index is the random neighbour selected
                index = tmp_index;
                //the current cell is the random neighbour selected
                curr = tmp;
                //set the state of the new current cell to a visited path
                curr.set_state(Cell.CELL_PATH);
                curr.add_state(Cell.CELL_VISITED);
            } else {
                //set the current cell index as the index at the start of the
                //queue
                index = (int)queue.remove();
            }
            //end maze generation if the queue is empty 
        } while (!queue.isEmpty());
        
        //set the state of the start node cell back to the start node
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        
        //remove the visited state so the algorithm works once applied again
        for (int i = 0; i < matrix.size(); ++i)
            matrix.get(i).remove_state(Cell.CELL_VISITED);
    }
   
    public void BFS_solve(StatMaker statMaker)
    {   
        //resets the visited state of each path
        maze_refresh();
        //instantiate the queue
        Queue queue = new LinkedList<>();
        //add the start node to the queue
        queue.add(start_node_pos);
        
        //set the stat_solve to 'BFS'
        statMaker.stat_solve = "BFS";
        
        //set the start node as visited
        matrix.get(start_node_pos).add_state(Cell.CELL_VISITED);
        
        //an arraylist which is used to store an array of neighbours
        ArrayList<Integer> neighbours = null;
        
        //an array which is used to store the indexes relative to their 
        //position in the shortest path
        int[] prev = new int[matrix.size()];
        
        //get the time before the algorithm runs
        float before = System.currentTimeMillis();
        
        while (!queue.isEmpty()) {
            //get the index at the front of the queue
            int index = (int)queue.remove();
            //get all the neighbours of the current node that is not visited
            //or a wall
            neighbours = get_neighbours(index, 
                    1, Cell.CELL_WALL | Cell.CELL_VISITED);
            
            //increment the cell count
            statMaker.stat_cell_count++;
            
            // loop through all the neighbours
            for (int i = 0; i < neighbours.size(); ++i) {
                //get the index of the neighbour
                int tmp_index = neighbours.get(i);
                //get the cell corresponding to that index
                Cell tmp = matrix.get(tmp_index);
                //add the neighbour's index to the queue
                queue.add(tmp_index);
                //set the state of the cell to visited
                tmp.add_state(Cell.CELL_VISITED);
                //the previous member of this current neighbour is the current
                //node being searched, store this in the prev array
                prev[tmp_index] = index;
            }
        }
        
        //record the time elapsed
        float total = System.currentTimeMillis() - before;
        
        //store the time elapsed into the stat_time variable
        statMaker.stat_time = total;
        
        //loop through the prev array to get the shortest path
        for (int i = end_node_pos; i != 0; i = prev[i]) {
            //get the cell 
            Cell tmp = matrix.get(i);
            //set the cell to a searched node, highlighting the path 
            tmp.set_state(Cell.CELL_SEARCHED);
            
            statMaker.stat_path_size++;
        }
        
        //set the start node and end node to their respective states 
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        matrix.get(end_node_pos).set_state(Cell.CELL_END);
    }
    
    
    
    public int []validate(String soln)
    {
        //the codes which represent the success of the parser and 
        //the solution 
        int []codes = new int[2];
        
        //the first index represents the parser code
        codes[SolnParser.PARSER_CODE] = SolnParser.PARSER_SUCCESS;
        //second index represents the solution/maze code
        codes[SolnParser.MAZE_CODE] = Maze.MAZE_UNSOLVED;
        
        //set the current solution to the newly entered solution
        parser.set_soln(soln);
        
       
        //reset the paths to paths and reset the start and end node positions
        for (int i = 0; i < matrix.size(); i++) {
            Cell tmp = matrix.get(i);
            if (tmp.check_state(Cell.CELL_VISITED)) {
                tmp.set_state(Cell.CELL_PATH);
            }
        }
        
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        matrix.get(end_node_pos).set_state(Cell.CELL_END);
        
        //a buffer which will store the current instruction
        String buffer = "";
        
        //the start coordinates which represent the start of the maze
        //the solution is used to manipulate these coordinates
        int x = start_node_pos % matrix_w;
        int y = (int)(start_node_pos / matrix_w);
        
        //get the next instruction until the end of the solution is reached
        while ((buffer = parser.parse(codes)) != SolnParser.PARSER_EOF) {
            
            //initialize the direction and magnitude global to all the 
            //scopes within the while loop
            char dir = '\0';
            int mag = 0;
            
            //get the direction
            dir = buffer.charAt(0);
            
            //if the buffer is greater than 1, meaning there is a magnitude
            if (buffer.length() > 1) {
                //get the magnitude
                mag = Integer.parseInt(buffer.substring(1, buffer.length()));
            } else {
                //set the magnitude to 1 (Default)
                mag = 1;
            }
            
            //check which direction the instruction moves
            switch (dir) {
                //if Down
                case 'D':
                    //loop through all the cells from y to y + mag
                    for (int i = 0; i < mag; i++) {
                        y = y + 1;
                        
                        //check if out of bounds
                        if (y > matrix_h) {
                            //set the error code to out of bounds error
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_OUT_OF_BOUNDS;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                    
                        //get the index
                        int index = (x + y * matrix_w);
                        //get a reference to the cell in the matrix
                        Cell tmp = matrix.get(index);
                    
                        //check if the state of that cell is not a wall
                        if(tmp.check_state(Cell.CELL_WALL)) {
                            //set the maze error code to a wall collision
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_HIT_WALL;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                        
                        //check if the end of the maze has been reached
                        if (tmp.check_state(Cell.CELL_END)) {
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_SOLVED;
                            //return, the maze is solved, no more of the 
                            //solution needs to be parsed.
                            return codes;
                        }
                        
                        //set the currently selected cell to a selected cell
                        tmp.set_state(Cell.CELL_SEARCHED);
                    }
                    break;
                case 'U':
                    //loop through all the cells from y to y - mag
                    for (int i = 0; i < mag; i++) {
                        y = y - 1;
                        
                        //check if out of bounds
                        if (y < 0) {
                            //set the maze error to out of bounds error
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_OUT_OF_BOUNDS;
                            return codes;
                        }
                    
                        //get the index
                        int index = (x + y * matrix_w);
                        //get a reference to the cell in the maze
                        Cell tmp = matrix.get(index);
                    
                        //check if the cell is a wall
                        if(tmp.check_state(Cell.CELL_WALL)) {
                            //set the maze error code to a wall collision
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_HIT_WALL;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                        
                        //check if the end of the maze has been reached
                        if (tmp.check_state(Cell.CELL_END)) {
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_SOLVED;
                            //return, the maze is solved, no more of the 
                            //solution needs to be parsed.
                            return codes;
                        }
                        
                        //set the currently selected cell to a selected cell
                        tmp.set_state(Cell.CELL_SEARCHED);
                    }
                    break;
                case 'L':
                    //loop through all the cells from x to x - mag
                    for (int i = 0; i < mag; i++) {
                        x = x - 1;
                        
                        //check if the cell is out of bounds
                        if (x < 0) {
                            //set the maze error code to out of bounds error
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_OUT_OF_BOUNDS;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                    
                        //get the index
                        int index = (x + y * matrix_w);
                        //get a reference to the cell in the maze
                        Cell tmp = matrix.get(index);
                    
                        //check if the current cell is a wall
                        if(tmp.check_state(Cell.CELL_WALL)) {
                            //set the maze error code to a wall collision
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_HIT_WALL;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                        
                        //check if the end of the maze has been reached
                        if (tmp.check_state(Cell.CELL_END)) {
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_SOLVED;
                            //return, the maze is solved, no more of the 
                            //solution needs to be parsed.
                            return codes;
                        }
                        
                        //set the currently selected cell to a selected cell
                        tmp.set_state(Cell.CELL_SEARCHED);
                    }
                    break;
                case 'R':
                    //loop through all cells from x to x + mag
                    for (int i = 0; i < mag; i++) {
                        x = x + 1;
                        
                        //check if out of bounds
                        if (x > matrix_w) {
                            //set the maze error code to out of bounds error
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_OUT_OF_BOUNDS;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                    
                        //get the index
                        int index = (x + y * matrix_w);
                        //get a reference to the cell in the maze
                        Cell tmp = matrix.get(index);
                    
                        //check if the cell is a wall
                        if(tmp.check_state(Cell.CELL_WALL)) {
                            //set the maze error code to a wall collision
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_HIT_WALL;
                            //return, since there was an error, can't continue
                            return codes;
                        }
                        
                        //check if the end of the maze has been reached
                        if (tmp.check_state(Cell.CELL_END)) {
                            codes[SolnParser.MAZE_CODE] = Maze.MAZE_SOLVED;
                            //return, the maze is solved, no more of the 
                            //solution needs to be parsed.
                            return codes;
                        }
                        
                        //set the currently selected cell to a selected cell
                        tmp.set_state(Cell.CELL_SEARCHED);
                    }
                    break;
            }
        }
        return codes;
    }
    
    public void recursive_solve(StatMaker statMaker) 
    {
        //refresh the maze, remove all visited states
        maze_refresh();
        
        //record the statistics for the maze 
        statMaker.stat_cell_count = 0;
        statMaker.stat_time = 0;
        statMaker.stat_path_size = 0;
        statMaker.stat_solve = "recursive";
        
        //time how long to solve the maze
        float start = System.currentTimeMillis();
        
        recursive_solve_helper(start_node_pos, statMaker);
        
        //record the time taken
        statMaker.stat_time = System.currentTimeMillis() - start;
        
        //reset the start node and end node
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        matrix.get(end_node_pos).set_state(Cell.CELL_END);
    }
    
    public boolean recursive_solve_helper(int index, StatMaker statMaker)
    {
        //get the x and y coordinates for the current index
        int x = index % matrix_w;
        int y = (int) (index / matrix_w);
        
        //get a reference to the cell in the maze
        Cell cell = matrix.get(index);
        
        //check if the index is a wall or it is visited
        if (cell.check_state(Cell.CELL_WALL | Cell.CELL_VISITED))
            //return from the function, so the recursive tree does not 
            //proceed down this path of indexes
            return false;
        
        //set the state of the cell to visited
        cell.add_state(Cell.CELL_VISITED);
        
        //increment the cell count, for each cell traversed
        statMaker.stat_cell_count += 1;
        
        //if we are at the end of the maze, return
        if (index == end_node_pos)
            return true;
        
        //if not out of the maze bounds
        if (x > 0) {
            //check to see if the cell to the left is an unvisited path
            if (recursive_solve_helper(index-1, statMaker)) {
                //increment the path size
                statMaker.stat_path_size += 1;
                //set is as a searched cell
                matrix.get(index-1).set_state(Cell.CELL_SEARCHED);
                return true;
            }
        }
        
        //if not out of the maze bounds
        if (x < matrix_w - 1) {
            //check to see if the cell to the right is an unvisited path
            if (recursive_solve_helper(index+1, statMaker)) {
                //increment the path size
                statMaker.stat_path_size += 1;
                //set it as a searched cell 
                matrix.get(index+1).set_state(Cell.CELL_SEARCHED);
                return true;
            }
        }
        
        //if not out of the maze bounds
        if (y > 0) {
            //check to see if the cell above is an unvisited path
            if (recursive_solve_helper(index-matrix_w, statMaker)) {
                //increment the path size
                statMaker.stat_path_size += 1;
                //set it as a searched cell
                matrix.get(index-matrix_w).set_state(Cell.CELL_SEARCHED);
                return true;
            }
        }
        
        //if not out of the maze bounds
        if (y < matrix_h - 1) {
            //check to see if the cell below is an unvisited path
            if (recursive_solve_helper(index+matrix_w, statMaker)) {
                //increment the path size
                statMaker.stat_path_size += 1;
                //set uit as a searched cell
                matrix.get(index+matrix_w).set_state(Cell.CELL_SEARCHED);
                return true;
            }
        }
        
        //index is out of bounds
        return false;
    }
    
    public void prims(StatMaker statMaker)
    {
        //set all the cells to walls
        maze_set();
        
        //set the starting index to be the index of the start cell 
        int index = start_node_pos;
        
        //instantiate a random number generator
        Random rand = new Random();
        
        //array to contain all the walls 
        ArrayList<Integer> walls = new ArrayList<>();
        
        //set the start node as visited
        matrix.get(index).set_state(Cell.CELL_START);
        matrix.get(index).add_state(Cell.CELL_VISITED);
        
        //get all the adjacent walls for that index
        insert_walls(index, walls);
        
        //while there are still walls to process
        while (!walls.isEmpty()) {
            //get a random index into the walls array
            int rand_index = rand.nextInt(walls.size());
            
            //get the index of the wall for the matrix
            int wall_index = walls.get(rand_index);
            
            //get a reference to the cell at that index
            Cell wall = matrix.get(wall_index);
            
            //if there is 1 visited cell adjacent to the wall
            if (check_cells(wall_index) == 1) {
                //set the cell to a path and visited
                wall.set_state(Cell.CELL_PATH);
                wall.add_state(Cell.CELL_VISITED);
                
                //if the wall is to the left or right of the current index
                if (wall_index % matrix_w == index % matrix_w) {
                    //check if it is above or below in the index
                    if ((int) (wall_index / matrix_w) > 
                            (int) (index / matrix_w)) {
                        Cell cell = matrix.get(wall_index+matrix_w);
                        cell.set_state(Cell.CELL_PATH);
                        cell.add_state(Cell.CELL_VISITED);
                        insert_walls(wall_index+matrix_w, walls);
                     //must be above the index
                    } else {
                        Cell cell = matrix.get(wall_index-matrix_w);
                        cell.set_state(Cell.CELL_PATH);
                        cell.add_state(Cell.CELL_VISITED);
                        insert_walls(wall_index-matrix_w, walls);
                    }
                    //else, must be to the left or right of the index
                } else {
                    //check if it is to the left 
                    if (wall_index % matrix_w > index % matrix_w) {
                        Cell cell = matrix.get(wall_index+1);
                        cell.set_state(Cell.CELL_PATH);
                        cell.add_state(Cell.CELL_VISITED);
                        insert_walls(wall_index+1, walls);
                    //must be to the right
                    } else {
                        Cell cell = matrix.get(wall_index-1);
                        cell.set_state(Cell.CELL_PATH);
                        cell.add_state(Cell.CELL_VISITED);
                        insert_walls(wall_index-1, walls);
                    }
                }
            }
            
            //remove the wall from the walls list 
            walls.remove(rand_index);
        }
    }
    
    public void insert_walls(int index, ArrayList<Integer> walls)
    {
        //get the x and y coordinates
        int x = index % matrix_w;
        int y = (int) (index / matrix_w);
        
        //check if the left adjacent index can be out of bounds
        if (x > 1) {
            //get a reference to the left-adjacent cell 
            Cell cell1 = matrix.get(index-1);
            //check if the cell is wall and not already visited
            if (cell1.check_state(Cell.CELL_WALL)
                    && !cell1.check_state(Cell.CELL_VISITED)) {
                //add the index to the walls list 
                walls.add(index-1);
            }
        }
        
        //check if the right adjacent index will be out of bounds
        if (x < matrix_w - 2) {
            //get a reference to the left-adjacent cell 
            Cell cell1 = matrix.get(index+1);
            //check if the cell is wall and not already visited
            if (cell1.check_state(Cell.CELL_WALL)
                    && !cell1.check_state(Cell.CELL_VISITED)) {
                //add the index to the walls list 
                walls.add(index+1);
            }
        }
        
        //check if the top adjacent index will be out of bounds
        if (y > 1) {
            //get a reference to the left-adjacent cell 
            Cell cell1 = matrix.get(index-matrix_w);
            //check if the cell is wall and not already visited
            if (cell1.check_state(Cell.CELL_WALL)
                    && !cell1.check_state(Cell.CELL_VISITED)) {
                //add the index to the walls list 
                walls.add(index-matrix_w);
            }
        }
        
        //check if the bottom adjacent index will be out of bounds
        if (y < matrix_h - 2) {
            //get a reference to the left-adjacent cell 
            Cell cell1 = matrix.get(index+matrix_w);
            //check if the cell is wall and not already visited
            if (cell1.check_state(Cell.CELL_WALL)
                    && !cell1.check_state(Cell.CELL_VISITED)) {
                //add the index to the walls list 
                walls.add(index+matrix_w);
            }
        }
    }
    
    public int check_cells(int wall_index)
    {
        //count variable, stores the number of visited cells
        int count = 0;
        
        //get the x and y coordinates 
        int x = wall_index % matrix_w;
        int y = (int) (wall_index / matrix_w);
        
        //check if the left adjacent cell is not out of bounds
        if (x > 0) {
            //get a reference to the left adjacent cell 
            Cell cell = matrix.get(wall_index-1);
            //check if the cell is visited
            if (cell.check_state(Cell.CELL_VISITED))
                //increment the counter 
                count++;
        }
        
        //check if the right adjacent cell is not out of bounds
        if (x < matrix_w - 1) {
            //get a reference to the right adjacent cell 
            Cell cell = matrix.get(wall_index+1);
            //check if the cell is visited
            if (cell.check_state(Cell.CELL_VISITED))
                //increment the counter
                count++;
        }
        
        //check if the top adjacent cell is not out of bounds
        if (y > 0) {
            //get a reference to the top adjacent cell 
            Cell cell = matrix.get(wall_index-matrix_w);
            //check if the cell is visited
            if (cell.check_state(Cell.CELL_VISITED))
                //increment the counter
                count++;
        }
        
        //check if the bottom adjacent cell is not out of bounds
        if (y < matrix_h - 1) {
            //get a reference to the bottom adjacent cell 
            Cell cell = matrix.get(wall_index+matrix_w);
            //check if the cell is visited
            if (cell.check_state(Cell.CELL_VISITED))
                //increment the counter
                count++;
        }
        
        return count;
    }
    
    //gets the neighbours of the currently selected cell
    public ArrayList<Integer> get_neighbours(int index, int offset, int state)
    {
        //initialize the array which will store the neighbours
        ArrayList<Integer> neighbours = new ArrayList<>();
        //formula: (x + y * width) 
        
        //based on the formula, the x-coordinate is the remainder of the 
        //division by the width of the maze 'index MOD matrix_w'
        int xcoord = index % matrix_w;
        
        //based on the formula, the y-coordinate is the integer division
        //by the width of the maze 'index DIV matrix_w'
        int ycoord = (int)index/matrix_w;
        
        //find the positions of the left, right, up and down nodes
        //using the formula stated above 
        int left = (xcoord - offset + ycoord * matrix_w);
        int right = (xcoord + offset + ycoord * matrix_w);
        int up = (xcoord + (ycoord - offset) * matrix_w);
        int down = (xcoord + (ycoord + offset) * matrix_w);
        
        //check to see if the x-coordinate is within the maze for the left
        //node and not visited.
        if (xcoord - offset >= 0 
                && !matrix.get(left).check_state(state)) {
            //if valid node, inside the maze, add to the neighbours array
            neighbours.add(left);
        //check to see if the x-coordinate is within the maze for the right
        //node and not visited.
        } if (xcoord + offset < matrix_w 
                && !matrix.get(right).check_state(state)) {
            neighbours.add(right);
        //check to see if the y-coordinate is within the maze for the up
        //node and not visited.
        } if (ycoord - offset >= 0 
                && !matrix.get(up).check_state(state)) {
            neighbours.add(up);
        //check to see if the y-coordinate is within the maze for the down
        //node and not visited.
        } if (ycoord + offset < matrix_h
                && !matrix.get(down).check_state(state)) {
            neighbours.add(down);
        }
        
        return neighbours;
    }
    
    public int index_compare(int index1, int index2)
    {
        //formula: index = (x + y * width)
        
        //get the x-coordinates and y-coordinates of the indexes
        int xcoord1 = index1 % matrix_w;
        int ycoord1 = (int)index1/matrix_w;
        int xcoord2 = index2 % matrix_w;
        int ycoord2 = (int)index2/matrix_w;
        
        //if index1's x-coordinate is greater than index2's x-coordinate
        if (xcoord1 > xcoord2) {
            //return 0, representing index1 is to the right of index2
            return 0;
        //if index1's x-coordinate is less than index2's x-coordinate
        } else if (xcoord1 < xcoord2) {
            //return 1, representing index1 is to the left of index2
            return 1;
        //if index1's y-coordinate is below index2's y-coordinate
        } else if (ycoord1 > ycoord2) {
            //return 2, representing index1 is below index2
            return 2;
        //if index1's y-coordinate is above index2's y-coordinate
        } else if (ycoord1 < ycoord2) {
            //return 3, representing index1 is above index2
            return 3;
        }
        
        //invalid input
        return -1;
    }
    
    public int count_paths()
    {
        //store the number of paths
        int count = 0;
        
        //queue to store all the indexes
        Queue<Integer> queue = new LinkedList<>();
        
        //add the start node to the queue
        queue.add(start_node_pos);
        
        //continue the search until no more cells to search
        while (!queue.isEmpty()) {
            //get the index at the front of the queue
            int index = (int)queue.remove();
            
            //get all the available neighbours which haven't been visited
            ArrayList<Integer> neighbours = get_neighbours(index, 1,
                    Cell.CELL_WALL | Cell.CELL_SEARCHED);
            
            //loop through all of these neighbours
            for (int i = 0; i < neighbours.size(); i++) {
                //get the index of the current neighbour
                int neighbour_index = neighbours.get(i);
                //get the corresponding reference to a cell in the matrix
                Cell neighbour = matrix.get(neighbour_index);
                
                
                //check if the cell is the end cell, then increment path count
                if (neighbour.check_state(Cell.CELL_END)) {
                    count++;
                //else, add the neighbour to the queue
                } else {
                    queue.add(neighbour_index);
                }
                //make the state of the cell a searched cell, to prevent
                //an infinite loop of infinitely many solutions to any maze
                neighbour.add_state(Cell.CELL_SEARCHED);
            }
        }
        
        return count;
    }
    
    public String maze_str()
    {
        //store the format of the maze
        String str = "";
        
        //each line, containing the binary number on for each row
        String line = "";
       
        //loop through the cells in the maze
        for (int i = 0; i < matrix.size(); i++) {
            //get a reference to the cell at that location
            Cell tmp = matrix.get(i);
            //if a newline in the matrix has been reached
            if ((i % matrix_w) == 0 && i != 0) {
                //add a newline to the maze_str
                str += bin_to_hex(line) + '\n';
                line = "";
            }
            
            //if the cell is a wall
            if (tmp.check_state(Cell.CELL_WALL)) {
                //add a 0
                line += '0';
            //else if, cell is a path
            } else if (tmp.check_state(Cell.CELL_PATH | Cell.CELL_END 
                    | Cell.CELL_START)) {
                //add a 1
                line += '1';
            }
        }
        //convert final line to hex 
        str += bin_to_hex(line) + '\n';
        return str;
    }
    
    public String bin_to_hex(String bin)
    {
        //the string which stores the final hex string
        String hex_str = "";
        
        /* - ptr is a pointer to hold the start position of each nibble
           - ext is how much the pointer has extended over the binary string
             buffer, and prevents and buffer overflow from occuring when
             using substring
        */
        int ptr = 0, ext = 0;
        
        //while there are still substrings to process
        while (ptr < bin.length()) {
            
            //check if ptr does not extend over the buffer
            if (ext < 0)
                //reset as pointer is still smaller than buffer
                ext = 0;
            
            //get the nibble starting at the index ptr
            String buffer = bin.substring(ptr, ptr+4-ext);
            
            //convert the nibble into it's decimal equivalent
            //and convert into it's hex value from 0 to F
            switch(bin_to_dec(buffer)) {
                case 0:
                    hex_str += '0';
                    break;
                case 1:
                    hex_str += '1';
                    break;
                case 2:
                    hex_str += '2';
                    break;
                case 3:
                    hex_str += '3';
                    break;
                case 4:
                    hex_str += '4';
                    break;
                case 5:
                    hex_str += '5';
                    break;
                case 6:
                    hex_str += '6';
                    break;
                case 7:
                    hex_str += '7';
                    break;
                case 8:
                    hex_str += '8';
                    break;
                case 9:
                    hex_str += '9';
                    break;
                case 10:
                    hex_str += 'A';
                    break;
                case 11:
                    hex_str += 'B';
                    break;
                case 12:
                    hex_str += 'C';
                    break;
                case 13:
                    hex_str += 'D';
                    break;
                case 14:
                    hex_str += 'E';
                    break;
                case 15:
                    hex_str += 'F';
                    break;
            }
            
            //increment the pointer with the width of a nibble
            ptr += 4;
            
            //check to see if the pointer extends over the buffer and
            //remove any possible width to the substring final index
            ext = ptr + 4 - bin.length();
        }
        return hex_str;
    }
    
    public int bin_to_dec(String bin)
    {
        //used to store the decimal number
        int dec = 0;
        
        //loop through the binary string from end to the start
        for (int i = bin.length() - 1, j = 0; i >= 0; i--, j++) {
            //if the bit at that position is set
            if (bin.charAt(i) == '1')
                //add to the number the corresponding power of 2
                dec += (1<<j);
        }
        return dec;
    }
    
    public String hex_to_bin(String hex)
    {
        //string to store the binary string
        String bin_str = "";
        
        //loop through the hex string 
        for (int i = 0; i < hex.length(); i++) {
            //get the current character
            char c = hex.charAt(i);
            
            //convert each character into binary equivalent
            //checking the required with for the given maze
            //this is fixed, since I know the maze width is not going 
            //to exceed 27, and since 27 MOD 4 = 3 the width of the final
            //binary string needs to have a width of 3
            if (c == '0') {
                if (i == hex.length() - 1)
                    bin_str += "000";
                else
                    bin_str += "0000";
            } else if (c == '1') {
                if (i == hex.length() - 1)
                    bin_str += "001";
                else
                    bin_str += "0001";
            } else if (c == '2') {
                if (i == hex.length() - 1)
                    bin_str += "010";
                else
                    bin_str += "0010";
            } else if (c == '3') {
                if (i == hex.length() - 1)
                    bin_str += "011";
                else
                    bin_str += "0011";
            } else if (c == '4') {
                if (i == hex.length() - 1)
                    bin_str += "100";
                else
                    bin_str += "0100";
            } else if (c == '5') {
                if (i == hex.length() - 1)
                    bin_str += "101";
                else
                    bin_str += "0101";
            } else if (c == '6') {
                if (i == hex.length() - 1)
                    bin_str += "110";
                else
                    bin_str += "0110";
            } else if (c == '7') {
                if (i == hex.length() - 1)
                    bin_str += "111";
                else
                    bin_str += "0111";
            } else if (c == '8') {
                bin_str += "1000";
            } else if (c == '9') {
                bin_str += "1001";
            } else if (c == 'A') {
                bin_str += "1010";
            } else if (c == 'B') {
                bin_str += "1011";
            } else if (c == 'C') {
                bin_str += "1100";
            } else if (c == 'D') {
                bin_str += "1101";
            } else if (c == 'E') {
                bin_str += "1110";
            } else if (c == 'F') {
                bin_str += "1111";
            }
        }
        return bin_str;
    }
    
    public String shortest_path_str()
    {
        //stores the solution
        String maze_solution = "";
        
        //copy buffer, used to store the reversed string
        String copy = "";
        
        //instantiate a queue to store the indexes 
        Queue queue = new LinkedList<>();
        
        //add the start node to the queue
        queue.add(start_node_pos);
        
        //set the start node as visited
        matrix.get(start_node_pos).add_state(Cell.CELL_VISITED);
        
        //prev array to store the a link to previously visited cells for 
        //each index, contains the shortest path
        int prev[] = new int[matrix.size()];
        
        //while there are still more cells to visit
        while (!queue.isEmpty()) {
            
            //get the index at the front of the queue
            int index = (int)queue.remove();
            //get all the neighbours of the current cell
            ArrayList<Integer> neighbours = get_neighbours(index, 1,
                    Cell.CELL_WALL | Cell.CELL_VISITED);
            //loop through all the neighbours
            for (int i = 0; i < neighbours.size(); i++) {
                //get the index of the current neighbour
                int neighbour_index = neighbours.get(i);
                //get a reference to the cell in the maze
                Cell neighbour_cell = matrix.get(neighbour_index);
                //add the neighbour to the queue
                queue.add(neighbour_index);
                //add the visited state to the neighbour
                neighbour_cell.add_state(Cell.CELL_VISITED);
                //add it to the prev array
                prev[neighbour_index] = index;
            }
        }
        
        //loop through the smallest path
        int prev_cell = end_node_pos;
        for (int i = prev_cell; i != start_node_pos; i = prev[i]) {
            //get the next cell in the path
            int curr_cell = prev[i];
            //get the direction based on the 2 indexes, add it to the solution
            maze_solution += get_dir_prev(curr_cell, i);
        }
        
        //simplify the solution
        maze_solution = soln_simplify(maze_solution);
        
        //reverse the solution and store in the copy buffer
        for (int i = maze_solution.length() - 1; i >= 0; i--) {
            copy += maze_solution.charAt(i);
        }
        return copy;
    }
    
    //used to help with the shortest_path_str function 
    public char get_dir_prev(int curr, int prev)
    {
        //get the x and y coordinates for each index
        int curr_x = curr % matrix_w;
        int curr_y = (int)(curr / matrix_w);
        int prev_x = prev % matrix_w;
        int prev_y = (int)(prev / matrix_w);
        
        //if you need to move right to get to curr from prev
        if (curr_x > prev_x)
            //instead move left
            return 'L';
        //if you need to move left to get to curr from prev
        else if (curr_x < prev_x)
            //instead move right
            return 'R';
        //if you need to move down to get to curr from prev
        else if (curr_y > prev_y)
            //instead move up
            return 'U';
        else
            //else must need to move down
            return 'D';
    }
    
    public String soln_simplify(String soln)
    {
        //buffer where the characters are copied to 
        String copy = "";
        
        //pointer to contain the start of the next series of moves
        int ptr = 0;
       
        //loop until the pointer exceeds length of the solution
        while (ptr < soln.length()) {
            //get the current direction in the solution
            char curr_dir = soln.charAt(ptr);
            
            //magnitude defaults to 1, keep global i to set ptr once loop 
            //ends
            int mag = 1, i;
            //loop through the solution, if next characters are equal to the
            //current character, then increase the magnitude
            for (i = ptr + 1; i < soln. length(); i++) {
                if (soln.charAt(i) == curr_dir)
                    mag += 1;
                else
                    break;
            }
            
            //if the magnitude is greater than 10, need to process each digit
            copy += int_to_str(mag);
            //add the direction to the copy buffer
            copy += curr_dir;
            //change the pointer to point to the start of the next series of 
            //moves
            ptr = i;
        }
        return copy;
    }
    
    public String int_to_str(int magnitude)
    {
        //stores the number
        String str = "";
        
        //while there are still digits to process
        while (magnitude > 0) {
            //add the units digit to the str buffer
            str += magnitude % 10;
            //move to the next digit
            magnitude /= 10;
        }
        
        return str;
    }
    
    
    public void maze_load(String maze_str)
    {
        //loop through the maze
        for (int i = 0; i < matrix.size(); i++) {
            //get a reference to the cell at that location
            Cell cell = matrix.get(i);
            //if the character is a 0 in the maze_str
            if (maze_str.charAt(i) == '0')
            //set the cell to a wall
                cell.set_state(Cell.CELL_WALL);
            //else, set the cell to a path
            else
                cell.set_state(Cell.CELL_PATH);
        }
        
        //reset the start node cell
        matrix.get(start_node_pos).set_state(Cell.CELL_START);
        
        //reset the end node cell
        matrix.get(end_node_pos).set_state(Cell.CELL_END);
    }
}