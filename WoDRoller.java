package wodroller;

import java.util.ArrayList;
import java.util.Random;

public class WoDRoller {
    private static final Random random = new Random();

    public static final int EXCEPTIONAL_SUCCESS = 5;
    public static final int DICE_VALUE = 10;
    public static final Integer DEFAULT_CHANCE_DICE = 1, DEFAULT_NUM_DICE = 5, DEFAULT_TARGET = 8,
            DEFAULT_ROLL_AGAIN = 10;

    public static WoDRoll roll(WoDRollEntry rollset) {
        int successes, num;
        boolean exceptionalSuccess, dramaticFailure;

        num = DEFAULT_CHANCE_DICE;
        if (!rollset.chance) {
            num = rollset.num;
        }

        if (num <= 0) {
            throw new IllegalArgumentException("Enter a positive integer");
        }

        successes = 0;
        ArrayList<Integer> rollList = new ArrayList<>(num);
        for (int rollsLeft = num; rollsLeft > 0; rollsLeft--) {
            int roll = random.nextInt(DICE_VALUE) + 1;
            rollList.add(roll);

            if ((rollset.chance && roll == DICE_VALUE)
                    || (!rollset.chance && roll >= rollset.target))
            {
                successes++;
            }

            if (roll >= rollset.rollAgain || (roll == 1 && rollset.rote)) {
                rollsLeft++;
            }
        }

        exceptionalSuccess = (successes >= EXCEPTIONAL_SUCCESS);

        dramaticFailure = (successes == 0 && rollList.contains(1));

        return (new WoDRoll(successes, rollList, exceptionalSuccess, dramaticFailure));
    }
}
