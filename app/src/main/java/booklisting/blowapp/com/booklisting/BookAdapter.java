package booklisting.blowapp.com.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Aran on 10/19/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, List<Book> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false);
        }

        Book book = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.book_image);
        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_title);
        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.book_author);
        TextView bookDate = (TextView) listItemView.findViewById(R.id.book_date);
        TextView bookDescription = (TextView) listItemView.findViewById(R.id.book_description);

        Picasso.with(getContext()).load(book.getmThumbnail()).into(imageView);
        bookTitle.setText(book.getmTitle());
        bookAuthor.setText(book.getmAuthor());
        bookDate.setText(book.getmDate());
        bookDescription.setText(book.getmDescription());

        return listItemView;
    }
}
