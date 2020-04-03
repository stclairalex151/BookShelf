package edu.temple.bookshelf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class BookListAdapter extends BaseAdapter implements ListAdapter {

    private final Context c;    //context for this adapter
    private final ArrayList<Book> books; //list being adapted

    BookListAdapter(Context c, ArrayList<Book> books) {
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
        if(books == null )
            return 0;
        else
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

        View view = convertView;
        CompleteListHolder viewHolder;
        if(convertView == null){    //if nothing was in the view before
            view = LayoutInflater.from(c).inflate(R.layout.list_item, parent , false);
            viewHolder = new CompleteListHolder(view);
            view.setTag(viewHolder);
        }else{  // if this view is being re-used
            viewHolder = (CompleteListHolder) view.getTag();
        }

        viewHolder.textView1.setText(books.get(position).getTitle());
        viewHolder.textView2.setText(books.get(position).getAuthor());

        return view;
    }
}

class CompleteListHolder {
    TextView textView1;
    TextView textView2;

     CompleteListHolder(View base) {
        textView1 = base.findViewById(R.id.titleView);
        textView2 = base.findViewById(R.id.authorView);
    }
}


