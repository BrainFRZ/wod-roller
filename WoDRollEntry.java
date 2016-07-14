/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Jul 12, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package wodroller;

public final class WoDRollEntry {
    public final int num;
    public final int rollAgain;
    public final int target;
    public final boolean rote;
    public final boolean chance;

    public WoDRollEntry(int num, int rollAgain, int target, boolean rote, boolean chance) {
        this.num       = num;
        this.rollAgain = rollAgain;
        this.target    = target;
        this.rote      = rote;
        this.chance    = chance;
    }
}
