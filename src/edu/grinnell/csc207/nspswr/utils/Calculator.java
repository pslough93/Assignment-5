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
  * Initializes fraction
  */
  public void runCalculator()
    throws Exception
  {
    PrintWriter out = new PrintWriter(System.out, true);
    String response;
    Scanner sc = new Scanner(System.in);
    out.println("Starting Calculator Simulator");
    out.print("> ");
    out.flush();
    while (true)
      {
        response = sc.nextLine();
        if (response.equals("exit"))
          {
            out.println("Program terminated.");
            sc.close();
            return;
          }
        else
          {
            try
            {
            Fraction result = evaluate(response);
            out.println("= " + result + "\n");
            }
            catch (FormatException incorrectFormat)
            {
              out.println("Request was not handled: "+incorrectFormat.reason);
            }
          }
        out.print("> ");
        out.flush();
      }
  }

  public Fraction evaluate(String expression)
    throws FormatException
  {
    isValid(expression);
    boolean foundEquals = false;
    String substring = "";
    for (int i = 0; i < expression.length(); i++)
      {
        if (expression.charAt(i) == '=')
          {
            foundEquals = true;
            substring += expression.substring(i + 2);
          }
      }
    if (foundEquals)
      {
        int rIndex = expression.charAt(1) - '0';
        r[rIndex] = eval(substring);
        return r[rIndex];
      }
    else
      {
        return eval(expression);
      }
  }

  public Fraction eval(String expression)
  {
    String[] fracsAndOps = expression.split(" ");
    Fraction[] fracs = new Fraction[fracsAndOps.length / 2 + 1];
    char[] ops = new char[fracsAndOps.length / 2];
    int fracsIndex = 0;
    int opsIndex = 0;
    for (int i = 0; i < fracsAndOps.length; i++)
      {
        if (i % 2 == 0)
          {
            if (fracsAndOps[i].charAt(0) == 'r')
              {
                int rIndex = fracsAndOps[i].charAt(1) - '0';
                fracs[fracsIndex++] = r[rIndex];
              }
            else
              {
                fracs[fracsIndex++] = new Fraction(fracsAndOps[i]);
              }
          }
        else
          {
            ops[opsIndex++] = fracsAndOps[i].charAt(0);
          }
      }
    Fraction result = fracs[0];
    for (int i = 1; i < fracs.length; i++)
      {
        //evaluates operations in order
        switch (ops[i - 1])
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
          }
      }
    return result;
  }

  public static boolean isValid(String expression)
    throws FormatException
  {

    //double spacing
    for (int i = 1; i < expression.length(); i++)
      {
        if (expression.charAt(i) == ' ' && expression.charAt(i - 1) == ' ')
          {
            throw new FormatException("Double spaces present.");
          }
      }

    //beginning/end spacing
    if (expression.charAt(0) == ' '
        || expression.charAt(expression.length() - 1) == ' ')
      {
        throw new FormatException("Spaces at beginning and/or end of input.");
      }

    String[] segments = expression.split(" ");
    int[] segmentTypes = new int[segments.length];
    //0 for ops, 1 for nums, 2 for r's, 3 for equals.  
    //for(int j = 0; j < segments.length; j++){
    //System.out.println(segments[j]);
    //}
    // formats
    int opsCount = 0;
    int numsCount = 0;
    int rCount = 0;
    int equalsCount = 0;

    for (int k = 0; k < segments.length; k++)
      {
        int len = segments[k].length();
        if (len == 1)
          {
            if (segments[k].matches("[+-/*]"))
              {
                opsCount++;
                segmentTypes[k] = 0;
              }
            else if (segments[k].matches("="))
              {
                equalsCount++;
                segmentTypes[k] = 3;
              }
            else if (segments[k].matches("\\d{1}"))
              {
                numsCount++;
                segmentTypes[k] = 1;
              }
            else
              {
                throw new FormatException("Invalid character present: "
                                          + segments[k]);
              }
          }
        if (len == 2)
          {
            if (segments[k].matches("r[0-7]"))
              {
                if (r[segments[k].charAt(1) - '0'] == null)
                  {
                        if (segments.length>=k+2)
                          {
                            if (segments[Math.min(k + 1, segments.length)].charAt(0) != '=')
                              {
                              throw new FormatException(segments[k]
                                  + " is currently undefined");
                              }
                              
                          }
                        else
                          {
                            throw new FormatException(segments[k]
                                + " is currently undefined");
                          }

                      }
                System.out.println("Passed!");
                rCount++;
                segmentTypes[k] = 2;
              }
            else if (segments[k].matches("r[89]"))
              {
                throw new FormatException("Invalid r assignment: "
                                          + segments[k]);
              }
            else if (segments[k].matches("\\d{2}"))
              {
                numsCount++;
                segmentTypes[k] = 1;
              }
            else
              {
                throw new FormatException("Invalid character sequence: "
                                          + segments[k]);
              }
          }
        if (len > 2)
          {
            if (segments[k].matches("\\d+(/\\d+)?"))
              {
                numsCount++;
                segmentTypes[k] = 1;
              }
            else
              {
                throw new FormatException("Invalid character sequence: "
                                          + segments[k]);
              }
          }
      }//for loop;

    //more than one equals
    if (equalsCount > 1)
      {
        throw new FormatException("Too many equal signs.");
      }
    //unbalanced expression
    if ((numsCount + rCount) - (opsCount + equalsCount) != 1)
      {
        throw new FormatException("Unbalanced expression.");
      }

    //making sure everythings in the right place
    if (segmentTypes.length > 1 && (segmentTypes[1] == 3 && segmentTypes[0] != 2))
      {
        throw new FormatException("Equals sign in wrong place.");
      }

    //equals only in position 1
    for (int h = 1; h < segmentTypes.length; h++)
      {
        if (h != 1 && segmentTypes[h] == 3)
          {
            throw new FormatException("Equals sign in wrong place.");
          }
      }

    //operators in odd positions
    for (int h = 1; h < segmentTypes.length; h += 2)
      {
        if (segmentTypes[h] != 0 && segmentTypes[h] != 3)
          {
            throw new FormatException("Incorrectly positioned operators.");
          }
      }

    //nums in even ones
    for (int h = 0; h < segmentTypes.length; h += 2)
      {
        if (segmentTypes[h] != 1 && segmentTypes[h] != 2)
          {
            throw new FormatException("Incorrectly positioned values.");
          }
      }

    return true;
  }

  /*public void testString(String expression) 
      throws FormatException
  {
    boolean wasSpace = false;
    String ops = "+-/*";
    // Beginning and ending in spaces
    if (expression.charAt(0) == ' ' || expression.charAt(expression.length() - 1) == ' ')
      {
      throw new FormatException("Spaces at beginning and end of instruction not allowed");
      }
    // Double spaces
    for (int i = 0; i < expression.length(); i++)
      {
        if (wasSpace && expression.charAt(i) == ' ')
          {
            throw new FormatException("Too many spaces");
          }
        if (expression.charAt(i) == ' ')
          {
            wasSpace = true;
          }
      }
   
    String[] split = expression.split(" ");
    for (String i : split)
      {
        if (containsChar(i,ops)&&i.length()==1)
          {
            i="operation";
          }
        else
          {
            System.out.println("We blow up");
          }
      }
    
  }
  
  public static boolean containsChar(String container, String contents)
  {
    for (int increment=0;increment<contents.length();increment++)
      {
        if (container.indexOf(contents.charAt(increment))>0)
          {
            return true;
          }
      }
    return false;
    
  }*/

  public static void main(String[] args)
    throws Exception
  {
    Calculator c1 = new Calculator();
    c1.runCalculator();
  }
}
