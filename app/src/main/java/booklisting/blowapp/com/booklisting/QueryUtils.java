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
                String thumbnail = "http://www.francoisfabie.fr/wp-content/uploads/2017/06/cuisine-grise-laquee-16-cuisine-quip-e-plan-3d-1752-x-1107.jpg";
                String title = "title not available";
                String author = "by unknown";
                String date = "unknown date";
                String description = "no descriptions";
                String link = "";

                JSONObject currentItem, volumeInfo, imageLinks;
                JSONArray authors;
                currentItem = booksJsonArray.getJSONObject(i);
                volumeInfo = currentItem.getJSONObject("volumeInfo");

                if (volumeInfo.has("imageLinks")) {
                    imageLinks = volumeInfo.getJSONObject("imageLinks");
                    thumbnail = imageLinks.optString("smallThumbnail");
                }
                if (volumeInfo.has("authors")) {
                    authors = volumeInfo.getJSONArray("authors");
                    author = authors.optString(0);
                }
                if (volumeInfo.has("title"))
                    title = volumeInfo.optString("title");
                if (volumeInfo.has("publishedDate"))
                    date = volumeInfo.optString("publishedDate");
                if (volumeInfo.has("description"))
                    description = volumeInfo.optString("description");
                if (volumeInfo.has("previewLink"))
                    link = volumeInfo.optString("previewLink");

                Book book = new Book(thumbnail, title, author, date, description, link);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
