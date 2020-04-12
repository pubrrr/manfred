package manfred.game.interact;

public class Person implements Interact{
    private String name;

    public Person(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
