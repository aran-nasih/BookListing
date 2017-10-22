package booklisting.blowapp.com.booklisting;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    SearchView searchView;
    Button searchButton;
    TextView badResponseView;
    String searchResult;
    ProgressBar progressBar;

    private BookAdapter bookAdapter;
    private static int BOOK_LOADER_ID = 1;
    private String GOOGLE_BOOKS_API;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.searchButton = (Button) findViewById(R.id.search_button);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar_view);
        this.progressBar.setVisibility(View.GONE);
        this.badResponseView = (TextView) findViewById(R.id.bad_response_textview);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(this.badResponseView);
        this.bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(bookAdapter);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.searchView = (SearchView) findViewById(R.id.search_view);
                MainActivity.this.searchResult = MainActivity.this.searchView.getQuery().toString();
                progressBar.setVisibility(View.VISIBLE);
                badResponseView.setText("");
                bookAdapter.clear();
                dismissKeyboard(MainActivity.this);
                MainActivity.this.GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q="
                        + getRequestedUrl(searchResult) + "+terms&maxResults=6";
                if (isNetworkAvailable(MainActivity.this)) {
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(0, null, MainActivity.this);
                } else {
                    badResponseView.setText("No Internet Connection");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book book = bookAdapter.getItem(position);
                Uri urlIntent = Uri.parse(book.getmUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, urlIntent);
                startActivity(intent);
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookAsyncTaskLoader(this, GOOGLE_BOOKS_API);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> earthquakes) {
        progressBar.setVisibility(View.GONE);

        bookAdapter.clear();
        if (earthquakes == null || earthquakes.isEmpty()) {
            badResponseView.setText("No Books Found");
            return;
        }
        bookAdapter.addAll(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }

    private String getRequestedUrl(String key) {
        StringBuilder sb = new StringBuilder();
        key.trim();
        key.toLowerCase();
        String[] keys = key.split(" ");
        for (int i = 0; i < keys.length; i++) {
            sb.append(keys[i]);
            if (i != keys.length - 1) sb.append("%20");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}
