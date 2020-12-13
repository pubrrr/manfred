package helpers;

import manfred.game.attack.CombinationElement;

import java.util.Arrays;
import java.util.Stack;

public class AttackCombinationHelper {

    public static Stack<CombinationElement> attackCombination(CombinationElement... elements) {
        Stack<CombinationElement> result = new Stack<>();
        Arrays.asList(elements).forEach(result::push);
        return result;
    }
}
