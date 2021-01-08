package manfred.game.interact.person.textLineFactory;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

class FactoryRuleTest {

    @Test
    void orElse() {
        FactoryAction actionMock = mock(FactoryAction.class);

        FactoryRule first_notApplicable = outgoingEdges -> Optional.empty();
        FactoryRule second_applicable = outgoingEdges -> Optional.of(actionMock);

        Optional<FactoryAction> result = first_notApplicable.orElse(second_applicable)
            .applicableTo(List.of());

        assertFalse(result.isEmpty());
        assertThat(result.get(), equalToObject(actionMock));
    }

    @Test
    void orElse_else() {
        FactoryAction actionMock = mock(FactoryAction.class);

        FactoryRule first_notApplicable = outgoingEdges -> Optional.of(actionMock);
        FactoryRule second_applicable = outgoingEdges -> Optional.empty();

        Optional<FactoryAction> result = first_notApplicable.orElse(second_applicable)
            .applicableTo(List.of());

        assertFalse(result.isEmpty());
        assertThat(result.get(), equalToObject(actionMock));
    }
}