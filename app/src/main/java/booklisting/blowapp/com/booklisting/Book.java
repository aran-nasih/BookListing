package booklisting.blowapp.com.booklisting;

/**
 * Created by Aran on 10/19/2017.
 */

public class Book {
    private String thumbnail;
    private String title;
    private String author;
    private String date;
    private String description;
    private String url;

    public Book(String thumbnail, String title, String author, String date, String description, String url) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.author = author;
        this.date = date;
        this.description = description;
        this.url = url;
    }

    public String getmThumbnail() {
        return thumbnail;
    }

    public String getmTitle() {
        return title;
    }

    public String getmAuthor() {
        return author;
    }

    public String getmDate() {
        return date;
    }

    public String getmDescription() {
        return description;
    }

    public String getmUrl() {
        return this.url;
    }
}
