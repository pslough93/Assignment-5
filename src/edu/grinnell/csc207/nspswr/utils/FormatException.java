package edu.grinnell.csc207.nspswr.utils;

  /**
   * A general exception class used to signify that a string doesn't meet a method's
   * input requirements
   * @author schlager, slough, royle
   *
   */

public class FormatException extends Exception
{
  String reason;
   /**
    * @param reason - reason for throwing exception.
    * The exception constructor also saves the reason to simplify handling by the
    * method which will handle the exception
    */
   public FormatException(String reason)
   {
     super(reason);
     this.reason=reason;
   }
  
}
