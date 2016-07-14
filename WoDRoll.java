/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Jul 12, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package wodroller;

import java.util.ArrayList;


public final class WoDRoll {
    final public int successes;
    final public ArrayList<Integer> rollList;
    final public boolean exceptionalSuccess, dramaticFailure;

    public WoDRoll(int successes, ArrayList<Integer> rollList, boolean success, boolean failure) {
        if (success && failure) {
            throw new IllegalArgumentException("**ERROR**: Can't be a dramatic failure and "
                    + "exceptional success!");
        }

        this.successes = successes;
        this.rollList  = rollList;
        this.exceptionalSuccess = success;
        this.dramaticFailure    = failure;
    }
}
