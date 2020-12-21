package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;
import static org.mockito.Mockito.mock;

class ChoicesFacadeTest {
    @Test
    void build() {
        GelaberEdge firstEdge = edge("first");
        GelaberEdge secondEdge = edge("second");
        GelaberEdge thirdEdge = edge("third");
        ChoicesFacade choicesFacade = ChoicesFacade.buildWith(mock(GameConfig.class))
            .from(List.of(firstEdge, secondEdge, thirdEdge));

        assertThat(choicesFacade.confirm().follow(), equalToObject(firstEdge.follow()));

        choicesFacade.up();
        assertThat(choicesFacade.confirm().follow(), equalToObject(thirdEdge.follow()));

        choicesFacade.up();
        assertThat(choicesFacade.confirm().follow(), equalToObject(secondEdge.follow()));

        choicesFacade.down();
        assertThat(choicesFacade.confirm().follow(), equalToObject(thirdEdge.follow()));
    }

    private GelaberEdge edge(String id) {
        return GelaberEdge.continuingWith(new GelaberNodeIdentifier(id), "edgeText");
    }
}