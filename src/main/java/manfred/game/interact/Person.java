package manfred.game.interact;

import manfred.game.interact.gelaber.Gelaber;

public class Person implements Interact{
    private String name;
    private Gelaber gelaber;

    public Person(String name, Gelaber gelaber){
        this.name = name;
        this.gelaber = gelaber;
    }

    public String getName() {
        return this.name;
    }

    public Gelaber getGelaber() {
        return this.gelaber;
    }

    @Override
    public void interact() {
        System.out.println("Hallo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
