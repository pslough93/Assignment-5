package edu.grinnell.csc207.nspswr.utils;

import java.math.BigDecimal;

public class Utils
{
  public static BigDecimal sqrt(BigDecimal d, BigDecimal epsilon)
  {
    BigDecimal a = new BigDecimal("1.0");
    BigDecimal a1;
    while(a.multiply(a).subtract(d).abs().compareTo(epsilon) > 0){
      // (a + d/a)/2
      a1 = (d.divide(a, epsilon.scale() + 10, BigDecimal.ROUND_HALF_UP).subtract(a));
      a1 = a1.multiply(BigDecimal.valueOf(0.5));
      a = a1.add(a);
    }
    return a;
  }
  
  public static int expt(int x, int p){
    int result = 1;
    
    while(p > 0){
      if(p % 2 == 1){
        result *= x;
        p--;
      }
      else{
        x *= x;
        p /= 2;
      }
    }
    return result;
  }
  
  public static double fastExpt(double x, double p){
    double logX = Math.log(x);
		return Math.exp(logX*p);
  }
}
