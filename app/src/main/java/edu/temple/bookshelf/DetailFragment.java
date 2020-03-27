package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "book";

    private HashMap<String, String> book;   //the book passed in
    private View view;  //the view being created by this fragment


    // Required empty public constructor
    public DetailFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hm the hashmap object.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
     static DetailFragment newInstance(HashMap<String, String> hm) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, hm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            try {   //should be casted as hashmap
                this.book = (HashMap<String, String>) getArguments().getSerializable(ARG_PARAM1);
            }catch (ClassCastException e) { e.printStackTrace(); }
        }//TODO: why do you need this. here?
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        //we will need a set of keys to access the map
        String[] keys = getResources().getStringArray(R.array.keys);

        //declare the two views that exist in the detailfragment
        TextView titleView = view.findViewById(R.id.titleView);
        TextView authorView = view.findViewById(R.id.authorView);

        titleView.setText(book.get(keys[0]));
        authorView.setText(book.get(keys[1]));

        return view;
    }
}
