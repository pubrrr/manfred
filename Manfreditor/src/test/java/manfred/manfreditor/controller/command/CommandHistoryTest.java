package manfred.manfreditor.controller.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CommandHistoryTest {

    private CommandHistory underTest;

    @BeforeEach
    void setUp() {
        underTest = new CommandHistory();
    }

    @Test
    void pushAndPop() {
        var rollbackOperationMock = mock(RollbackOperation.class);

        underTest.push(rollbackOperationMock);
        underTest.undoLast();

        verify(rollbackOperationMock).rollback();
    }

    @Test
    void pushAndPopTwice() {
        var firstRollbackOperationMock = mock(RollbackOperation.class);
        var secondRollbackOperationMock = mock(RollbackOperation.class);

        underTest.push(firstRollbackOperationMock);
        underTest.push(secondRollbackOperationMock);

        underTest.undoLast();
        verify(secondRollbackOperationMock).rollback();
        verify(firstRollbackOperationMock, never()).rollback();

        underTest.undoLast();
        verify(firstRollbackOperationMock).rollback();
    }

    @Test
    void popEmptyStackDoesNotThrowException() {
        underTest.undoLast();
    }
}