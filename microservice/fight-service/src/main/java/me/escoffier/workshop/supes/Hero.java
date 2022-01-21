package me.escoffier.workshop.supes;

public class Hero {

    public Long id;
    public String name;
    public String otherName;
    public int level;
    public String picture;
    public String powers;


    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", otherName='" + otherName + '\'' +
                ", level=" + level +
                ", picture='" + picture + '\'' +
                ", powers='" + powers + '\'' +
                '}';
    }
}
