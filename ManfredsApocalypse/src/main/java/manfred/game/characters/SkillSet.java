package manfred.game.characters;

import manfred.game.attack.AttackGenerator;
import manfred.game.attack.CombinationElement;

import java.util.HashMap;
import java.util.Optional;
import java.util.Stack;

public class SkillSet {
    private final HashMap<Stack<CombinationElement>, AttackGenerator> combinationToAttackMap = new HashMap<>();

    public void put(Stack<CombinationElement> key, AttackGenerator attackGenerator) {
        this.combinationToAttackMap.put(key, attackGenerator);
    }

    public Optional<AttackGenerator> get(Stack<CombinationElement> key) {
        return Optional.ofNullable(this.combinationToAttackMap.get(key));
    }
}
