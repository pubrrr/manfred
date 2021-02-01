package manfred.game.controls;

@FunctionalInterface
public interface ControllerStateMapper<INITIAL_STATE extends ControllerInterface, NEW_STATE extends ControllerInterface> {

    NEW_STATE determineNewControllerState(INITIAL_STATE initialState);

    static <STATE extends ControllerInterface> STATE preserveState(STATE state) {
        return state;
    }
}
