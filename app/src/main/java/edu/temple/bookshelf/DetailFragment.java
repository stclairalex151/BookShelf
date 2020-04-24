package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "book";//the state param for the book object

    private Book book;                  //the book passed in
    private TextView titleView;         //view object for the title
    private TextView authorView;        //view object for the author
    private ImageView coverPhoto;       //view object for the cover photo
    private Button playButton;          //play button
    private AudioPlayInterface parent;  //local copy of parent for implenting interface functions

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AudioPlayInterface){
            parent = (AudioPlayInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Implement interface!");
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
        coverPhoto = view.findViewById(R.id.imageView);
        playButton = view.findViewById(R.id.playButton);

        if(book != null) //if the book passed to this fragment has data, set it
            changeView(book);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.playAudio(book);
            }
        });

        return view;
    }

    void changeView(Book newBook) {
         book = newBook;

         titleView.setText(book.getTitle());
         authorView.setText(book.getAuthor());

         //set the cover photo
         Picasso.get().load(book.getCoverURL()).into(coverPhoto);
    }

    //This will be used to tell the activity that the audio book should begin,
    //  so that the activity can begin the service
    interface AudioPlayInterface{
        void playAudio(Book book);
    }
}