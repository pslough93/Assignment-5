package edu.grinnell.csc207.nspswr.utils;

import java.math.BigInteger;

/**
* A simple implementation of Fractions.
*
* @OriginalAuthor Samuel A. Rebelsky
* @EditedBy Schlager, Royle, Slough
* @author CSC152 2005S
* @version 1.0 of February 2005
* 
* Taken from Assignment-4.
*/
public class Fraction
{
  public static void main(String[] args)
  {
    Fraction testFraction = new Fraction("7/-7");
    //testFraction.simplifyFraction();
    System.out.println(testFraction.num.toString() + ", "
                       + testFraction.denom.toString());
  }

  // +------------------+---------------------------------------------
  // | Design Decisions |
  // +------------------+
  /*
  * (1) Denominators are always positive. Therefore, negative fractions are
  * represented with a negative numerator. Similarly, if a fraction has a
  * negative numerator, it is negative.
  *
  * (2) Fractions are not necessarily stored in simplified form. To obtain a
  * fraction in simplified form, one must call the simplify method.
  */
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+
  /** The numerator of the fraction. Can be positive, zero or negative. */
  BigInteger num;
  /** The denominator of the fraction. Must be non-negative. */
  BigInteger denom;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+
  /**
  * Build a new fraction with numerator num and denominator denom.
  *
  * 
  * Preconditions: Fraction must be a string composed of numbers consisting of no more than one backslash.
  * Postconditions: Creates a fraction object, returns nothing (constructor).
  */
  public Fraction(String fraction)
  {
    String fractionParts[] = fraction.split("/");
    //We deal with case in which backslash or not
    if (fractionParts.length == 1)
      {
        num = new BigInteger(fractionParts[0]);
        denom = new BigInteger("1");
      }
    else
      {
        num = new BigInteger(fractionParts[0]);
        denom = new BigInteger(fractionParts[1]);
      }
    simplifyFraction();
  } // Fraction(String)

  public Fraction(BigInteger num, BigInteger denom)
  {
    this.num = num;
    this.denom = denom;
    simplifyFraction();
  } // Fraction(BigInteger, BigInteger)

  /**
  * Build a new fraction with numerator num and denominator denom.
  *
  * Warning! Not yet stable.
  */
  public Fraction(int num, int denom)
  {
    this.num = BigInteger.valueOf(num);
    this.denom = BigInteger.valueOf(denom);
    simplifyFraction();
  } // Fraction(int, int)
  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
  * Express this fraction as a double.
  */
  public double doubleValue()
  {
    return num.doubleValue() / denom.doubleValue();
  } // doubleValue()

  /**
  * Add the fraction other to this fraction.
  */
  public Fraction add(Fraction addMe)
  {
    BigInteger resultNumerator;
    BigInteger resultDenominator;
    // The denominator of the result is the
    // product of this object's denominator
    // and addMe's denominator
    resultDenominator = denom.multiply(addMe.denom);
    // The numerator is more complicated
    resultNumerator =
        (num.multiply(addMe.denom)).add(addMe.num.multiply(denom));
    // Return the computed value
    return new Fraction(resultNumerator, resultDenominator);
  }// add(Fraction)

  /**
  * Convert this fraction to a string for ease of printing.
  */
  public String toString()
  {
    // Special case: It's zero
    if (num.equals(BigInteger.ZERO))
      {
        return "0";
      } // if it's zero
      if (denom.equals(1))
        {
          return num.toString();
        }
        // Lump together the string represention of the numerator,
    // a slash, and the string representation of the denominator
    // return this.num.toString().concat("/").concat(this.denom.toString());
    return num + "/" + denom;
  } // toString()

  /**
  * Multiplies the existing fraction by negative 1, returns it
  * Preconditions: [none]
  * Postconditions: returns fraction multiplied by -1 as new fraction object.
  */
  public Fraction negate()
  {
    return new Fraction(num.multiply(new BigInteger("-1")), denom);
  } //negate()

  /**
  * Returns the existing fraction multiplied by multiplicand
  * Preconditions: the denominator of multiplicand must be non-zero
  * Postconditions: returns fraction multiplied by multiplicand as new fraction object.
  */
  public Fraction multiply(Fraction multiplicand)
  {
    //Multiply the numerators and denominators separately
    BigInteger numerator = num.multiply(multiplicand.num);
    BigInteger denominator = denom.multiply(multiplicand.denom);
    return new Fraction(numerator, denominator);
  } //multiply(Fraction)

  /**
  * Returns the existing fraction with subtrahend subtracted
  * Preconditions: the denominator of subtrahend must be non-zero
  * Postconditions: returns the existing fraction with subtrahend subtracted as new fraction object.
  */
  public Fraction subtract(Fraction subtrahend)
  {
    //compute ad and bc separately, then subtract then divide by denominator
    BigInteger numeratorP1 = num.multiply(subtrahend.denom);
    BigInteger numeratorP2 = denom.multiply(subtrahend.num);
    BigInteger numerator = numeratorP1.subtract(numeratorP2);
    BigInteger denominator = denom.multiply(subtrahend.denom);
    return new Fraction(numerator, denominator);
  } //subtract(Fraction)

  /**
  * Returns the existing fraction divided by divisor
  * Preconditions: the denominator of divisor must be non-zero
  * Postconditions: returns the existing fraction divided by divisor as new fraction object.
  */
  public Fraction divide(Fraction divisor)
  {
    //multiply this by inverse of divisor
    return multiply(new Fraction(divisor.denom, divisor.num));
  } //divide(Fraction)

  /**
  * Returns the existing fraction raised to the power of expt
  * Preconditions: [none]
  * Postconditions: returns the existing fraction raised to the power of expt as new fraction object.
  */
  public Fraction pow(int expt)
  {
    //We raise both denominator and numerator to power of expt
    BigInteger numerator = num.pow(expt);
    BigInteger denominator = denom.pow(expt);
    return new Fraction(numerator, denominator);
  } //pow(int)

  
  /**
  * Takes the existing fraction and simplifies it
  * Preconditions: [none]
  * Postconditions: modifies the existing fraction so that the denominator of the fraction is positive one 
  * and so that the value of the fraction (num/denom) is unchanged
  */
  public void simplifyFraction()
  {
    //We change the sign of denominator and numerator if the sign of the denominator is negative
    if (denom.compareTo(BigInteger.valueOf(0)) == -1)
      {
        num = num.multiply(new BigInteger("-1"));
        denom = denom.multiply(new BigInteger("-1"));
      }
    //We also set the denominator to 1 if the numerator is zero
    if (num.compareTo(BigInteger.valueOf(0)) == 0)
      {
        denom = BigInteger.valueOf(1);
      }
    BigInteger gcd = num.gcd(denom);
    num = num.divide(gcd);
    denom = denom.divide(gcd);
  } //simplifyFraction()

} // class Fraction
