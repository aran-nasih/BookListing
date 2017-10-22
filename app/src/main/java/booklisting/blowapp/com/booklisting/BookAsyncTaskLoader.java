package booklisting.blowapp.com.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Aran on 10/19/2017.
 */

public class BookAsyncTaskLoader extends AsyncTaskLoader<List<Book>> {
    String url;

    public BookAsyncTaskLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (url == null) return null;
        List<Book> result = QueryUtils.fetchData(this.url);
        return result;
    }
}
