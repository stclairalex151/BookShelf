package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass that shows a list of items
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private static final String ARG_PARAM1 = "list";    //bundle key for fragment
    private ArrayList<Book> list;                       //the "bookshelf"
    private BookClickedInterface parent;                //instance of the interface used to commun. with parent
    private BookListAdapter bookListAdapter;            //copy of the adapter object


    // Required empty public constructor
    public ListFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
     static ListFragment newInstance(ArrayList<Book> param1) {
        ListFragment fragment = new ListFragment(); //the fragment being created
        Bundle args = new Bundle(); //bundle to accompany the new fragment

        //put the arrayList into the bundle for this fragment
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if the object was created with a list in the bundle, store it locally
        if (getArguments() != null) {
            list = (ArrayList<Book>) getArguments().getSerializable(ARG_PARAM1);
        }

        bookListAdapter = new BookListAdapter(getContext(), list);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //sets parent as context, but conforming to bookClickedInterface
        if(context instanceof BookClickedInterface)
            parent = (BookClickedInterface) context;
        else
            throw new RuntimeException(context.toString() + "*** THIS NEEDS TO IMPLEMENT THE INTERFACE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        //get the list and set the adapter to it
        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(bookListAdapter);

        //set a listener on the list that opens a detail fragment with that item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = list.get(position);
                ListFragment.this.parent.openDetails(position);
            }
        });
        return view;
    }

    /**
     * Reset the book list to the new one passed in
     * @param books the new list of books to be used by the fragment
     */
    void update(ArrayList<Book> books) {
         list = books;
         bookListAdapter.notifyDataSetChanged();
    }

    //This will be used to tell the activity which book has been pressed,
    //  so that the activity can build a fragment with the proper details
    interface BookClickedInterface{
        void openDetails(int pos);
    }
}
