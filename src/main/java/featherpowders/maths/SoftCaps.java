package featherpowders.maths;

/**
 * Soft caps functions.<br>
 * <br>
 * Soft caps functions are functions that caps your value under 1.00 (or 100%). I personally
 * use this for capping critical hit chance (Eg: 100 critical chance is not equals to 100% chance)
 * or player's defense.
 * @author nahkd
 *
 */
public class SoftCaps {
    
    /** Linear clamp. The return value will stuck at 1.0 if the input is larger than 1.0 */
    public static double linearClamp(double x) { return x < 1.0? x : 1.0; }
    
    /** Returns {@link Math#tanh(double)} */
    public static double tanh(double x) { return Math.tanh(x); }
    
    /** Returns (1.0 / (-1.0 - x)) + 1.0 */
    public static double invX(double x) { return (1.0 / (-1.0 - x)) + 1.0; }
    
}
