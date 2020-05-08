package manfred.game.interact.gelaber;

import org.springframework.lang.Nullable;

public class GelaberNextResponse {
    private boolean continueTalking;
    @Nullable private AbstractGelaberText nextGelaber;

    public GelaberNextResponse(boolean continueTalking) {
        this.continueTalking = continueTalking;
    }

    public GelaberNextResponse(boolean continueTalking, @Nullable AbstractGelaberText nextGelaber) {
        this.continueTalking = continueTalking;
        this.nextGelaber = nextGelaber;
    }

    public boolean continueTalking() {
        return continueTalking;
    }

    @Nullable
    public AbstractGelaberText getNextGelaber() {
        return nextGelaber;
    }
}
