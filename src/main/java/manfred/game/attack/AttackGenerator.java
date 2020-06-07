package manfred.game.attack;

public class AttackGenerator {
    private final int x;
    private final int y;
    private final int sizeX;
    private final int sizeY;

    public AttackGenerator(int x, int y, int sizeX, int sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Attack generate() {
        return new Attack(this.x, this.y, this.sizeX, this.sizeY);
    }
}
