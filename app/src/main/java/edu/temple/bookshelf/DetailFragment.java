package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "book";

    private Book book;   //the book passed in
    private TextView titleView;             //view object for the title
    private TextView authorView;            //view object for the author

    // Required empty public constructor
    public DetailFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param b the hashmap object.
     * @return A new instance of fragment DetailFragment.
     */
     static DetailFragment newInstance(Book b) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();

        //but the book into the bundle
        args.putSerializable(ARG_PARAM1, b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if this object was created with a book in the bundle, store the book from it
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //the view being created by this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //declare the two views that exist in the detailfragment
        titleView = view.findViewById(R.id.titleView);
        authorView = view.findViewById(R.id.authorView);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());

        return view;
    }

    void changeView(Book book) {
        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
    }
}