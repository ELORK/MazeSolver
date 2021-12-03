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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author luke
 */
public class ManPages extends JFrame
{
    //width and height of the window
    private int w;
    private int h;
    
    //the number of the currently selected page
    private int page_no;
    private int page_total;
    private JLabel page_no_label;
    
    //declaration of the left and right buttons
    private JButton page_left;
    private JButton page_right;
    
    //button and description containers
    private JPanel button_container;
    private JPanel desc_container;
    
    //page 0 components
    private JButton generate_maze_btn;
    private JButton solve_maze_btn;
    private JButton validate_soln;
    private JButton load_maze;
    
    private JLabel generate_maze_desc;
    private JLabel solve_maze_desc;
    private JLabel validate_soln_desc;
    private JLabel load_maze_desc;
    
    //page 1 components
    private JButton save_stats_btn;
    private JButton save_maze_btn;
    private JComboBox solve_select;
    private JComboBox maze_select;
    
    private JLabel save_stats_desc;
    private JLabel save_maze_desc;
    private JLabel solve_select_desc;
    private JLabel maze_select_desc;
    
    //page 2 components
    private JTextField user_soln;
    
    private JLabel user_soln_desc;
    private JPanel user_soln_background;
    
    //ManPages constructor
    public ManPages(String title, int w, int h) 
    {
        //set title of the window
        setTitle(title);
        //set the minimum size of the window
        Dimension minSize = new Dimension(w, h);
        setMinimumSize(minSize);
        setLayout(null);
        
        //set the width and height
        this.w = w;
        this.h = h;
        
        //set the background colour
        Color light_green = new Color(0x7a, 0xff, 0xa0);
        getContentPane().setBackground(light_green);
        
        //initialise the left and right buttons
        page_left = new JButton("<");
        page_right = new JButton(">");
        
        //align them to the bottom of the window, side-by-side
        page_left.setBounds((this.w-100)/2, this.h-100, 50, 50);
        page_right.setBounds((this.w)/2, this.h-100, 50, 50);
        
        page_left.addActionListener(e -> move_left());
        page_right.addActionListener(e -> move_right());
        
        //setup the maximum page number and the current page number
        page_no = 0;
        
        //not physical page total, used to create circular buffer
        page_total = 3;
        
        //set the text for the page label, centering the text
        page_no_label = new JLabel(String.valueOf(page_no) + "/" +
                String.valueOf(page_total-1), SwingConstants.CENTER);
        
        //set it above the left and right buttons
        page_no_label.setBounds((this.w-100)/2, this.h-170, 100, 100);
        
        //container setup
        button_container = new JPanel();
        desc_container = new JPanel();
        
        button_container.setLayout(new GridLayout(4, 1, 10, 10));
        desc_container.setLayout(new GridLayout(4, 1));
        
        button_container.setBounds(10, 10, 300, 500);
        desc_container.setBounds(this.w-310, 10, 300, 500);
        
        button_container.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        button_container.setBackground(light_green);
        desc_container.setBackground(light_green);
        
        //page 0 component setup 
        generate_maze_btn = new JButton("Generate Maze");
        solve_maze_btn = new JButton("Solve Maze");
        validate_soln = new JButton("Validate Solution");
        load_maze = new JButton("Load Maze");
        
        generate_maze_desc = new JLabel("<html>The 'Generate Maze' button is "
                + "used to generate a maze within the maze area.</html>");
        
        solve_maze_desc = new JLabel("<html>The 'Solve Maze' button is used"
                + " to solve the current maze within the maze area.</html>");
        
        validate_soln_desc = new JLabel("<html>The 'Validate Solution' button "
                + "is used to use the solution entered into the text area,"
                + " more about the validate solution text"
                + " area can be found on page 2.</html>");
        
        load_maze_desc = new JLabel("<html>The 'Load Maze' button is used to "
                + "load a maze from a file, these files can be obtained by "
                + "saving mazes using the 'Save Current Maze' button, more"
                + " on this button can be seen on page 1.</html>");
        
        //page 1 component setup
        save_stats_btn = new JButton("Save Current Stats");
        save_maze_btn = new JButton("Save Current Maze");
        solve_select = new JComboBox();
        maze_select = new JComboBox();
        
        save_stats_desc = new JLabel("<html>The 'Save Current Stats'"
                + " button is used to save the statistical data"
                + " present after a maze solve</html>");
        
        save_maze_desc = new JLabel("<html>The 'Save Current Maze'"
                + " button is used to save the current maze to a file"
                + " which can then be loaded with the 'Load Maze'"
                + " button seen on page 0</html>");
        
        solve_select_desc = new JLabel("<html>This allows you to "
                + "select which algorithm you want to use to solve"
                + " the maze</html>");
        
        maze_select_desc = new JLabel("<html>This allows you to "
                + "select which algorithm you want to use to "
                + "generate the maze</html>");
        
        solve_select.addItem("BFS");
        solve_select.addItem("recursive");
        
        maze_select.addItem("DFS");
        
        //set the fonts and font sizes
        solve_select.setFont(new Font("Sans-Serif", 24, 24));
        maze_select.setFont(new Font("Sans-Serif", 24, 24));
        
        //page 2 component setup
        user_soln = new JTextField();
        user_soln.setBounds((this.w-550)/2, 10, 550, 100);
        
        user_soln_desc = new JLabel("<html>This is the text area "
                + " where you can enter your solutions to the maze. "
                + "The solution syntax is defined as follows: <br><br>"
                + "A letter followed by a number, where the letter"
                + " can be 'L', 'U', 'D' or 'R' and the number can"
                + " be from 0-99. <br><br>"
                + "For example, L5 will move to the left by 5 cells<br><br>"
                + "A solution can be composed by multiple "
                + "combinations of these direction-magnitude components"
                + " which will form an entire solution."
                + " <br>For example:<br><br>"
                + "'R24D24' will solve the blank maze.</html>");
        
        //setup the background for the description
        user_soln_background = new JPanel();
        user_soln_background.setBounds((this.w-560)/2, 150, 560, 300);
        user_soln_background.setBackground(new Color(0xff,0xff,0xff));
        user_soln_background.setBorder(
                BorderFactory.createLineBorder(Color.BLACK));
        
        user_soln_desc.setBounds((this.w-550)/2, 50, 550, 500);
        
        page_0();
        
        //add them to the window
        add(page_left);
        add(page_right);
        add(page_no_label);
    }
    
    public void page_0()
    {
        //remove the page 2 components
        remove(user_soln_desc);
        remove(user_soln);
        remove(user_soln_background);
        
        //remove the page 1 components
        button_container.removeAll();
        desc_container.removeAll();
        
        //add all the components for page 0
        button_container.add(generate_maze_btn);
        button_container.add(solve_maze_btn);
        button_container.add(validate_soln);
        button_container.add(load_maze);
        
        desc_container.add(generate_maze_desc);
        desc_container.add(solve_maze_desc);
        desc_container.add(validate_soln_desc);
        desc_container.add(load_maze_desc);
        
        //add the containers which now contain page 0 components
        add(button_container);
        add(desc_container);
        
        this.repaint();
    }
    
    public void page_1()
    {
        //remove page 2 components
        remove(user_soln_desc);
        remove(user_soln);
        remove(user_soln_background);
        
        //remove page 0 components
        button_container.removeAll();
        desc_container.removeAll();
        
        //add page 1 components
        button_container.add(save_stats_btn);
        button_container.add(save_maze_btn);
        button_container.add(solve_select);
        button_container.add(maze_select);
        
        desc_container.add(save_stats_desc);
        desc_container.add(save_maze_desc);
        desc_container.add(solve_select_desc);
        desc_container.add(maze_select_desc);
        
        //add the containers which now contain page 1 components
        add(button_container);
        add(desc_container);
       
        this.repaint();
    }
    
    public void page_2()
    {   
        //remove the containers for page 0 and page 1 components
        remove(button_container);
        remove(desc_container);
        
        //add the page 2 components
        add(user_soln);
        add(user_soln_desc);
        add(user_soln_background);
        
        this.repaint();
    }
    
    public void move_left()
    {
        //decrement the page number
        page_no--;
        
        //if it is less than 0 
        if (page_no < 0) {
            //add the size for the circular buffer
            page_no += page_total;
        }
        
        //check if out of bounds for the circular buffer 
        page_no %= page_total;
        
        //set the page number for the page label
        page_no_label.setText(String.valueOf(page_no) + "/" + 
                String.valueOf(page_total-1));
        
        //produce the following page based on the page number
        switch(page_no) {
            case 0:
                page_0();
                break;
            case 1:
                page_1();
                break;
            case 2:
                page_2();
                break;
        }
    }
    
    public void move_right() 
    {
        //increment the page number
        page_no++;
        
        //circular array bounding check
        page_no %= page_total;
        
        //set the page number for the page label
        page_no_label.setText(String.valueOf(page_no) + "/" + 
                String.valueOf(page_total-1));
        
        //produce the page based on the page number
        switch(page_no) {
            case 0:
                page_0();
                break;
            case 1:
                page_1();
                break;
            case 2:
                page_2();
                break;
        }
    }
}
