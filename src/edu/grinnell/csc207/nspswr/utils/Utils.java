package edu.grinnell.csc207.nspswr.utils;

import java.math.BigDecimal;


  /**
   * A Simple numeric calculation class with square root and exponentiation methods
   * @author schlager, slough, royle
   *
   */
public class Utils
{
  /**
   * Returns the square root of d to an accuracy specified by epsilon.
   * 
   * Preconditions: d >= 0, epsilon > 0
   * 
   * Postconditions: this method will return a BigDecimal (y). We will
   * have abs[y ^ 2 - d] < epsilon
   */
  public static BigDecimal sqrt(BigDecimal d, BigDecimal epsilon)
  {
    BigDecimal a = new BigDecimal("1.0");
    BigDecimal a1;
    /* We compare d/a - a to epsilon */
    while (d.divide(a, epsilon.scale(), BigDecimal.ROUND_HALF_UP).subtract(a)
            .abs().compareTo(epsilon) > 0)
      {
        a1 =
            (d.divide(a, epsilon.scale(), BigDecimal.ROUND_HALF_UP).subtract(a));
        a1 = a1.multiply(BigDecimal.valueOf(0.5));
        a = a1.add(a);
      }
    return a;
  }

  /**
   * Returns x raised to the power of p.
   * 
   * Preconditions: x ^ p must be less than or equal to integer.MAX_VALUE.
   * 
   * Postconditions: The value returned will be x raised to the power of p.
   */
  public static int expt(int x, int p)
  {
    int result = 1;
    while (p > 0)
      {
        /* If p is odd, multiply result by x which will then make p even.*/
        if (p % 2 == 1)
          {
            result *= x;
            p--;
          }
        /* If p is even, we simply square x and divide p by 2.*/
        else
          {
            x *= x;
            p /= 2;
          }
      }
    return result;
  }

  /**
   * Extra credit attempt
   * Returns x raised to the power of p.
   * 
   * Preconditions: x ^ p must be less than or equal to double.MAX_VALUE.
   * 
   * Postconditions: The value returned will be x raised to the power of p.
   */  
  public static double fastExpt(double x, double p)
  {
    double logX = Math.log(x); //e^(p*ln*x) = x^p
    return Math.exp(logX * p);
  }
}
