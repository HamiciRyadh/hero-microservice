package me.escoffier.workshop.supes;

public class Villain  {

    public Long id;
    public String name;
    public String otherName;
    public int level;
    public String picture;
    public String powers;

    @Override
    public String toString() {
        return "Villain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", otherName='" + otherName + '\'' +
                ", level=" + level +
                ", picture='" + picture + '\'' +
                ", powers='" + powers + '\'' +
                '}';
    }
}
