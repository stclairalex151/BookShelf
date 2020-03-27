package edu.temple.bookshelf;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BookListAdapter extends BaseAdapter implements ListAdapter {

    private Context c;
    private ArrayList<HashMap<String, String>> books;

    BookListAdapter(Context c, ArrayList<HashMap<String, String>> books) {
        this.c = c;
        this.books = books;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return books.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //view will be a list_item layout with two textviews

        View view = LayoutInflater.from(c).inflate(R.layout.list_item, parent , false);
        //we need to get a copy of the keys used to access elements in the hashmap
        String[] keys = c.getResources().getStringArray(R.array.keys);

        //construct two textViews, one for title and one for author
        TextView titleView = view.findViewById(R.id.titleView);
        titleView.setText(books.get(position).get(keys[0]));//retrieves the title and sets the text
        titleView.setId(position);   //Give this item an ID or value so it can be retrieved later

        TextView authorView = view.findViewById(R.id.authorView);
        authorView.setText(books.get(position).get(keys[1]));//retrieves the author and sets the text
        authorView.setId(position);   //Give this item an ID or value so it can be retrieved later

        return view;
    }
}


