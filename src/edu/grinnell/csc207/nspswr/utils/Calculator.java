package edu.grinnell.csc207.nspswr.utils;

import java.util.Scanner;
import java.io.PrintWriter;

public class Calculator
{
  Fraction[] r;

  public Calculator()
  {
    r = new Fraction[8];
  }

  public void runCalculator()
    throws Exception
  {
    PrintWriter out = new PrintWriter(System.out, true);
    String response;
    Scanner sc = new Scanner(System.in);
    out.println("Starting Calculator Simulator");
    out.println("Please enter a command:");
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
            Fraction result = evaluate(response);
            out.println("= " + result + "\n");
          }
        out.println("Please enter another command:");
      }
  }

  public Fraction evaluate(String expression)
  {
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

  public void testString(String expression) 
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
    
  }
  
  public static void main(String[] args) 
      throws Exception
  {

    Calculator c1 = new Calculator();
    c1.runCalculator();
  }
}
