package booklisting.blowapp.com.booklisting;

/**
 * Created by Aran on 10/19/2017.
 */

public class Book {
    private String mThumbnail;
    private String mTitle;
    private String mAuthor;
    private String mDate;
    private String mDescription;
    private String mUrl;

    public Book(String mThumbnail, String mTitle, String mAuthor, String mDate, String mDescription, String url) {
        this.mThumbnail = mThumbnail;
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mDate = mDate;
        this.mDescription = mDescription;
        this.mUrl = url;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmUrl() {
        return this.mUrl;
    }
}
