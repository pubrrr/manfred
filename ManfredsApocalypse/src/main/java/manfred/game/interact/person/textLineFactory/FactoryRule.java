package manfred.game.interact.person.textLineFactory;

import manfred.game.interact.person.GelaberEdge;

import java.util.List;
import java.util.Optional;

public interface FactoryRule {
    Optional<FactoryAction> applicableTo(List<GelaberEdge> outgoingEdges);

    default FactoryRule orElse(FactoryRule next) {
        return new ChainedRule(this, next);
    }

    class ChainedRule implements FactoryRule {
        private final FactoryRule wrapped;
        private final FactoryRule next;

        private ChainedRule(FactoryRule wrapped, FactoryRule next) {
            this.wrapped = wrapped;
            this.next = next;
        }

        @Override
        public Optional<FactoryAction> applicableTo(List<GelaberEdge> outgoingEdges) {
            return wrapped.applicableTo(outgoingEdges)
                .or(() -> next.applicableTo(outgoingEdges));
        }
    }
}
