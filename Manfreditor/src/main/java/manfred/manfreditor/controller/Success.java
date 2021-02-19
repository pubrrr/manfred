package manfred.manfreditor.controller;

import java.util.function.Consumer;

public class Success implements CommandResult {

    @Override
    public void onFailure(Consumer<String> errorConsumer) {
        // do nothing
    }
}
