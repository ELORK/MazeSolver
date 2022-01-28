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

/* SolnParser: This class is used to parse the solution which is entered by
   the user, this will supply exit codes to the window and the window
   will respond accordingly to these codes and present the user with
   necesssary feedback
*/
public class SolnParser {
    //constants which define codes corresponding to the success of the parser
    public static final int PARSER_INVALID_MOVE = 2<<1;
    public static final int PARSER_INVALID_MAGNITUDE = 2<<2;
    public static final int PARSER_ERROR = 2<<3;
    public static final int PARSER_SUCCESS = 2<<4;
    
    //indexes for the codes array
    public static final int PARSER_CODE  = 0;
    public static final int MAZE_CODE = 1;
    
    //EOF string which represents the end of the user solution (Null Byte)
    public static final String PARSER_EOF = "\0";
    
    //string to store the user's solution
    private String soln;
    
    //an integer which holds the position of the next instruction between
    //calls to the parse function
    private int soln_ptr;
    
    //constructor for the SolnParser
    public SolnParser(String soln)
    {
        soln_ptr = 0;
        this.soln = soln;
    }
    
    //set the soln attribute to a new solution
    public void set_soln(String soln)
    {
        this.soln = soln;
        //reset the solution pointer
        soln_ptr = 0;
    }
    
    //get the soln attribute from the SolnParser
    public String get_soln()
    {
        return this.soln;
    }
    
    public String parse(int[] codes)
    {
        //check if pointer has reached the end of the solution
        if (soln_ptr >= soln.length() - 1) {
            //return end of 'stream' (EOF)
            soln_ptr = 0;
            return SolnParser.PARSER_EOF;
        }
        
        //a buffer which will hold the instructions 
        String buffer = "", copy = "";
        //flag to keep track of the number of instructions, and global i
        //which is used to set the pointer after loop terminates
        int flag = 0, i;
        
        //loop through all the characters inside the solution
        for (i = soln_ptr; i < soln.length(); i++) {
            //get the current character
            char curr = soln.charAt(i);
            //if the character is a direction
            if (curr == 'R' || curr == 'L' || curr == 'U' || curr == 'D')
                //increment the flag to define a new instruction
                flag++;
            //if there is 2 or more instructions being parsed
            if (flag >= 2)
                //break from the loop
                break;
            //assign the current character to the buffer
            copy = copy + curr;
        }
        //set the pointer to the start of the next instruction
        soln_ptr = i;
        
        //loop through the copy buffer
        for (int j = 0; j < copy.length(); j++) {
            //get the current character from the buffer
            char curr = copy.charAt(j);
            //if the character is not a space
            if (curr != ' ')
                //add it to the buffer
                buffer = buffer + curr;
        }
        
        //get the first character in the buffer, which should be a direction
        char first = buffer.charAt(0);
        //check to see if it is not a direction
        if (first != 'D' && first != 'U' && first != 'L' && first != 'R') {
            //reset the soln_ptr
            soln_ptr = 0;
            //set the parser code to INVALID_MAGNITUDE error
            codes[SolnParser.PARSER_CODE] = SolnParser.PARSER_INVALID_MOVE;
            //return the end of the 'stream' (EOF)
            return SolnParser.PARSER_EOF;
        }
        
        //check to see if the buffer's magnitude is greater than 2 digits
        if (buffer.length() > 3) {
            //reset the soln_ptr
            soln_ptr = 0;
            //set the parser code to INVALID_MAGNITUDE error
            codes[SolnParser.PARSER_CODE] = SolnParser.PARSER_INVALID_MAGNITUDE;
            //return the end of the 'stream' (EOF)
            return SolnParser.PARSER_EOF;
        }
        
        return buffer;
    }
}
