package org.example;
import java.io.Serializable;

public class Artwork implements Serializable {
    public String title;
    public String author;
    public int year;
    public String type;

    public Artwork(String title, String author, int year, String type) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("[%s] '%s', Автор: %s, Год: %d", type, title, author, year);
    }
}