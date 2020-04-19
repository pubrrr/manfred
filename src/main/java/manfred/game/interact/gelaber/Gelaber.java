package manfred.game.interact.gelaber;

import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;
import java.util.LinkedList;

public class Gelaber implements Paintable {
    public final static int TEXT_DISTANCE_TO_BOX = 50;
    public final static int TEXT_POINT_SIZE = 30;

    private final static int TEXT_BOX_DISTANCE_TO_BORDER = 100;
    public final static int TEXT_BOX_WIDTH = GamePanel.WIDTH - 2 * TEXT_BOX_DISTANCE_TO_BORDER;
    public final int TEXT_BOX_HEIGHT;

    private final int TEXT_BOX_POSITION_X;
    private final int TEXT_BOX_POSITION_Y;

    public final int DISTANCE_BETWEEN_LINES;
    private final int NUMBER_OF_TEXT_LINES;

    private AbstractGelaberText[] texts;
    private LinkedList<String[]> lineGroupsToPaintQueue;
    private String[] lineGroupsToPaint;

    public Gelaber(AbstractGelaberText[] texts) {
        this.texts = texts;

        DISTANCE_BETWEEN_LINES = 20;

        TEXT_BOX_HEIGHT = GamePanel.HEIGHT / 3 - TEXT_BOX_DISTANCE_TO_BORDER;
        TEXT_BOX_POSITION_X = TEXT_BOX_DISTANCE_TO_BORDER;
        TEXT_BOX_POSITION_Y = GamePanel.HEIGHT * 2 / 3;
        NUMBER_OF_TEXT_LINES = (TEXT_BOX_HEIGHT - 2 * TEXT_DISTANCE_TO_BOX) / (TEXT_POINT_SIZE + DISTANCE_BETWEEN_LINES);

        lineGroupsToPaintQueue = setLinesToPaintQueue(texts[0]);
        lineGroupsToPaint = lineGroupsToPaintQueue.poll();
    }

    public AbstractGelaberText[] getTexts() {
        return texts;
    }

    private LinkedList<String[]> setLinesToPaintQueue(AbstractGelaberText text) {
        String[] lines = text.getLines();
        int numberOfLineGroups = lines.length % NUMBER_OF_TEXT_LINES == 0
                ? lines.length / NUMBER_OF_TEXT_LINES
                : (lines.length / NUMBER_OF_TEXT_LINES) + 1;

        LinkedList<String[]> result = new LinkedList<>();
        for (int groupQueueIdx = 0; groupQueueIdx < numberOfLineGroups; groupQueueIdx++) {
            String[] lineGroup = new String[NUMBER_OF_TEXT_LINES];
            for (int indexInGroup = 0; indexInGroup < NUMBER_OF_TEXT_LINES; indexInGroup++) {
                lineGroup[indexInGroup] = groupQueueIdx * NUMBER_OF_TEXT_LINES + indexInGroup < lines.length
                        ? lines[groupQueueIdx * NUMBER_OF_TEXT_LINES + indexInGroup]
                        : "";
            }
            result.add(lineGroup);
        }

        return result;
    }

    public boolean next() {
        if (lineGroupsToPaintQueue.isEmpty()) {
            return false;
        }
        lineGroupsToPaint = lineGroupsToPaintQueue.poll();
        return true;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(TEXT_BOX_POSITION_X, TEXT_BOX_POSITION_Y, TEXT_BOX_WIDTH, TEXT_BOX_HEIGHT);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, TEXT_POINT_SIZE));

        for (int idx = 0; idx < NUMBER_OF_TEXT_LINES; idx++) {
            g.drawString(
                    lineGroupsToPaint[idx],
                    TEXT_BOX_POSITION_X + TEXT_DISTANCE_TO_BOX,
                    TEXT_BOX_POSITION_Y + TEXT_DISTANCE_TO_BOX + idx * (TEXT_POINT_SIZE + DISTANCE_BETWEEN_LINES) + TEXT_POINT_SIZE / 2
            );
        }
    }
}
