package edu.grinnell.csc207.nspswr.utils;

import java.util.Scanner;
import java.io.PrintWriter;

/**
* A simple implementation of Calculators.
*
* @OriginalAuthor Schlager, Slough & Royle
* @author CSC207 2014F
* @version 1.0 of September 2014
* 
*/
public class Calculator
{
  /*
   * Class-wide variable for keeping track of storage elements r0...r7.
   */
  static Fraction[] r;

  /**
  * Constructor for calculator
  * Initializes fraction
  */
  public Calculator()
  {
    r = new Fraction[8];
  }

  
  /**
  * User interface for calculator, is controlled by REPL loop
  * displays error messages when user enters inappropriate input, but does not terminate
  * 
  * Preconditions- none
  * Postconditions - runs calculator with REPL loop.
  */
  public void runCalculator()
    throws Exception
  {
    PrintWriter out = new PrintWriter(System.out, true); //initialize printer and string
    String response;
    Scanner sc = new Scanner(System.in);
    out.println("Starting Calculator Simulator");
    out.print("> ");
    out.flush();
    while (true)
      {
        response = sc.nextLine(); //read response
        if (response.equals("exit")) //evaluate if exit
          {
            out.println("Program terminated.");
            sc.close();
            return; // exit program
          }
        else
          {
            try
            {
            	Fraction result = evaluate(response); //evaluate function
            	out.println("= " + result + "\n"); //print
            }
            catch (FormatException incorrectFormat)
            {
              out.println("Request was not handled: "+incorrectFormat.reason);
            } //catch format
          }
        out.print("> ");
        out.flush();
      }//while
  }//run calculator
  
  /**
   * Evaluate - the first Evaluate function. This determines if the expression
   * is an assignment or real expression
   * 
   * Preconditions- String must be valid for full execution
   * Postconditions - returns result of evaluating the expression
   */
  public Fraction evaluate(String expression)
    throws FormatException
  {
    isValid(expression); //evaluates expression for format
    boolean foundEquals = false; 
    String substring = "";
    for (int i = 0; i < expression.length(); i++)
      {
        if (expression.charAt(i) == '=') //if = found, chop left of equals
          {
            foundEquals = true;
            substring += expression.substring(i + 2);
          }//if
      }//for
    if (foundEquals)
      {
        int rIndex = expression.charAt(1) - '0';
        r[rIndex] = eval(substring); //eval to right of index
        return r[rIndex];
      }//if
    else
      {
        return eval(expression); //eval full function if not assignment
      }//else
  }//evaluate(string)
  
  /**
   * Eval- Acutally does the numerical calculations with the input expression
   * 
   * Preconditions- String must be valid for full execution (should have been filtered
   * out by this point though)
   * Postconditions - returns result of evaluating the expression for the above evaluate
   * function
   */
  public Fraction eval(String expression)
  {
    String[] fracsAndOps = expression.split(" "); //split on spaces
    Fraction[] fracs = new Fraction[fracsAndOps.length / 2 + 1]; //initialize containers for 
    char[] ops = new char[fracsAndOps.length / 2]; //nums and fracs
    int fracsIndex = 0;
    int opsIndex = 0;
    for (int i = 0; i < fracsAndOps.length; i++) //loops through and puts nums and ops in place
      {
        if (i % 2 == 0)
          {
            if (fracsAndOps[i].charAt(0) == 'r') //puts register values in place
              {
                int rIndex = fracsAndOps[i].charAt(1) - '0';
                fracs[fracsIndex++] = r[rIndex];
              }//if
            else
              {
                fracs[fracsIndex++] = new Fraction(fracsAndOps[i]); //puts others in place
              }//else
          }//if
        else
          {
            ops[opsIndex++] = fracsAndOps[i].charAt(0); //puts ops in place
          }//else
      }//for
    Fraction result = fracs[0];
    for (int i = 1; i < fracs.length; i++)
      {
        //evaluates operations in order
        switch (ops[i - 1]) //switch statement with operators
          {
            case '+':
              result = result.add(fracs[i]);
              break;
            case '-':
              result = result.subtract(fracs[i]);
              break;
            case '*':
              result = result.multiply(fracs[i]);
              break;
            case '/':
              result = result.divide(fracs[i]);
          }//switch
      }//for
    return result;
  }//eval(string)
  
  /**
   * isValid - determines if the input string is valid for use by calculator. We're confident
   * this filters out almost all cases of incorrect formatting. Heavily used is the match() 
   * method. This method uses regular expressions and is detailed in the java documentation
   * here: http://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html. Regex strings
   * were tested on here before full implementation: 
   * http://www.regexplanet.com/advanced/java/index.html
   * 
   * Preconditions- none
   * Postconditions - throws appropriate FormatExceptions when for each error, returns 
   * nothing if it goes through unabbated.
   */

  public static void isValid(String expression)
    throws FormatException
  {

    //detect double spaces in string
    for (int i = 1; i < expression.length(); i++)
      {
        if (expression.charAt(i) == ' ' && expression.charAt(i - 1) == ' ')
          {
            throw new FormatException("Double spaces present.");
          }//if
      }//for

    //beginning/end spacing
    if (expression.charAt(0) == ' '
        || expression.charAt(expression.length() - 1) == ' ')
      {
        throw new FormatException("Spaces at beginning and/or end of input.");
      }//if

    String[] segments = expression.split(" "); //splits at spaces
    int[] segmentTypes = new int[segments.length]; //makes type array
    //0 for ops, 1 for nums, 2 for r's, 3 for equals.  
    int opsCount = 0; //counts for each type
    int numsCount = 0;
    int rCount = 0;
    int equalsCount = 0;

    for (int k = 0; k < segments.length; k++)
      {
        int len = segments[k].length();
        //length one strings are either ops, 1 digit nums or equals. All others rejected
        if (len == 1)
          {
            if (segments[k].matches("[+-/*]"))
              {
                opsCount++;
                segmentTypes[k] = 0;
              }//if
            else if (segments[k].matches("="))
              {
                equalsCount++;
                segmentTypes[k] = 3;
              }//elseif
            else if (segments[k].matches("\\d{1}"))
              {
                numsCount++;
                segmentTypes[k] = 1;
              }//elseif
            else
              {
                throw new FormatException("Invalid character present: "
                                          + segments[k]);
              }//else
          }
        if (len == 2)
        	//length 2 strings must be registers, or 2 digit nums
          {
            if (segments[k].matches("r[0-7]")) 
              {
                if (r[segments[k].charAt(1) - '0'] == null)//if register is null
                  {
                        if (segments.length>=k+2)
                          {
                            if (segments[Math.min(k + 1, segments.length)].charAt(0) != '=')
                              {
                              throw new FormatException(segments[k]
                                  + " is currently undefined"); //declares register as undefined
                              }//if
                          }//if
                        else
                          {
                            throw new FormatException(segments[k]
                                + " is currently undefined");
                          }//else
                      }//if
                System.out.println("Passed!");
                rCount++;
                segmentTypes[k] = 2;
              }//if
            else if (segments[k].matches("r[89]"))
              {
                throw new FormatException("Invalid r assignment: "
                                          + segments[k]);
              }//else if
            else if (segments[k].matches("\\d{2}"))
              {
                numsCount++;
                segmentTypes[k] = 1;
              }//else if
            else
              {
                throw new FormatException("Invalid character sequence: "
                                          + segments[k]);
              }//else
          }
        if (len > 2)
        	//bigger than length 2 must be fraction. else invalid
          {
            if (segments[k].matches("\\d+(/\\d+)?"))
              {
                numsCount++;
                segmentTypes[k] = 1;
              }//if
            else
              {
                throw new FormatException("Invalid character sequence: "
                                          + segments[k]);
              }//else
          }//if
      }//for loop;

    //tests for more than one equals
    if (equalsCount > 1)
      {
        throw new FormatException("Too many equal signs.");
      }//iff
    //checks for unbalanced expression
    if ((numsCount + rCount) - (opsCount + equalsCount) != 1)
      {
        throw new FormatException("Unbalanced expression.");
      }//if

    //making sure equals and register is in right place
    if (segmentTypes.length > 1 && (segmentTypes[1] == 3 && segmentTypes[0] != 2))
      {
        throw new FormatException("Equals sign in wrong place.");
      }//if

    //test for equals only in position 1
    for (int h = 1; h < segmentTypes.length; h++)
      {
        if (h != 1 && segmentTypes[h] == 3)
          {
            throw new FormatException("Equals sign in wrong place.");
          }//if
      }//for

    //test for operators in odd positions
    for (int h = 1; h < segmentTypes.length; h += 2)
      {
        if (segmentTypes[h] != 0 && segmentTypes[h] != 3)
          {
            throw new FormatException("Incorrectly positioned operators.");
          }//if
      }//for

    //test for nums in even ones
    for (int h = 0; h < segmentTypes.length; h += 2)
      {
        if (segmentTypes[h] != 1 && segmentTypes[h] != 2)
          {
            throw new FormatException("Incorrectly positioned values.");
          }//if
      }//for
  }//isValid(string)

  public static void main(String[] args)
    throws Exception
  {
    Calculator c1 = new Calculator();
    c1.runCalculator();
  }
}
