package manfred.manfreditor.controller.command;

@FunctionalInterface
public interface RollbackOperation {

    void rollback();

    default RollbackOperation andThen(RollbackOperation other) {
        return () -> {
            this.rollback();
            other.rollback();
        };
    }
}
