package booklisting.blowapp.com.booklisting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aran on 10/19/2017.
 */

public class QueryUtils {
    public static List<Book> fetchData(String urlString) {
        URL url = generateUrl(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            System.out.println("FAILED MAKING HTTP REQUEST");
        }
        List<Book> books = parseJson(jsonResponse);
        return books;
    }

    public static URL generateUrl(String urlString) {
        if (urlString == null || urlString.length() < 1) return null;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.out.println("FAILED GENERATING URL");
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) return jsonResponse;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println("BAD RESPONSE CODE");
            }
        } catch (IOException e) {
            System.out.println("FAILED OPENING CONNECTION");
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Book> parseJson(String json) {
        List<Book> books = new ArrayList<>();
        JSONObject jsonBase;
        try {
            jsonBase = new JSONObject(json);
            JSONArray booksJsonArray = jsonBase.getJSONArray("items");
            for (int i = 0; i < booksJsonArray.length(); i++) {
                JSONObject currentItem = booksJsonArray.getJSONObject(i);
                JSONObject volumeInfo = currentItem.getJSONObject("volumeInfo");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
//                JSONObject previewLink = volumeInfo.getJSONObject("previewLink");

                String thumbnail = imageLinks.getString("smallThumbnail");
                String title = volumeInfo.getString("title");
                String author = authors.getString(0);
                String date = volumeInfo.getString("publishedDate");
                String description = volumeInfo.getString("description");
                String link = volumeInfo.getString("previewLink");

                Book book = new Book(thumbnail, title, author, date, description, link);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
