package manfred.manfreditor.controller.command;

@FunctionalInterface
public interface RollbackOperation {
    void rollback();
}
