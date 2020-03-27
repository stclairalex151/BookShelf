package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
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
    private ArrayList<HashMap<String, String>> list;    //the "bookshelf"

    //we need an instance of the interface used to commun. with parent
    private BookClickedInterface parent;

    // Required empty public constructor
    public ListFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
     static ListFragment newInstance(ArrayList<HashMap<String, String>> param1) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();

        //put the arrayList into the bundle for this fragment
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {   //should be casted as arraylist<hashmap>
                list = (ArrayList<HashMap<String, String>>) getArguments().getSerializable(ARG_PARAM1);
            }catch (ClassCastException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //sets parent as context, but conforming to bookClickedInterface
        parent = (BookClickedInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        //TODO: find the list, create and assign an adapter to the list, and set an onClickListener
        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(new BookListAdapter(getContext(), list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: parent.getItemAtPosition(position) instead of list.get(position) for openDetails?

                try {   //should be casted as hashmap
                    HashMap<String, String> book = (HashMap<String, String>) list.get(position);
                    ListFragment.this.parent.openDetails(book);
                }catch (ClassCastException e) { e.printStackTrace(); }
            }
        });

        return view;
    }

    //This will be used to tell the activity which book has been pressed,
    //  so that the activity can build a fragment with the proper details
    interface BookClickedInterface{
        void openDetails(HashMap<String, String> book);
    }

}
