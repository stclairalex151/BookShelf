package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements ListFragment.BookClickedInterface,
        DetailFragment.AudioPlayInterface{

    private static final String STATE_PARAM1 = "listFragment";
    private static final String STATE_PARAM2 = "newList";
    private static final String STATE_PARAM3 = "newBook";

    boolean hasMultiPane;           //true if there's enough room for > 1 fragment
    DetailFragment detailFragment;  //copy of the detail fragment
    ListFragment listFragment;      //copy of the list fragment
    ArrayList<Book> books;          //list of books
    RequestQueue requestQueue;      //a queue for JSON object request
    EditText searchBox;             //the search box
    Button searchButton;            //the search button
    Book book;                      //the book that has been chosen
    TextView nowPlaying;
    SeekBar seekBar;
    Button pauseButton;
    Button stopButton;
    Intent audioServiceIntent;
    boolean serviceConnected;
    AudiobookService.MediaControlBinder mediaBinder;
    Message bookProgressMessage;
    AudiobookService.BookProgress bookProgress;
    boolean paused;


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceConnected = true;
            mediaBinder = (AudiobookService.MediaControlBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceConnected = false;
            mediaBinder = null;
        }
    };

    Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            bookProgress = (AudiobookService.BookProgress) msg.obj;
            if(!paused) {
                seekBar.setProgress(bookProgress.getProgress());
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        searchBox = findViewById(R.id.searchBox);
        searchButton = findViewById(R.id.searchButton);
        hasMultiPane = findViewById(R.id.detailContainer) != null;
        nowPlaying = findViewById(R.id.nowPlaying);
        seekBar = findViewById(R.id.seekBar);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);

        audioServiceIntent = new Intent(MainActivity.this, edu.temple.audiobookplayer.AudiobookService.class);
        bindService(audioServiceIntent, serviceConnection, BIND_AUTO_CREATE);

        if(savedInstanceState != null){     //if a fragment's state is saved
            books = (ArrayList<Book>) savedInstanceState.getSerializable(STATE_PARAM2);
            listFragment = (ListFragment) getSupportFragmentManager().getFragment(savedInstanceState, STATE_PARAM1);
            book = (Book) savedInstanceState.getSerializable(STATE_PARAM3);
            detailFragment = new DetailFragment();

            //show the list fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer, listFragment)
                    .addToBackStack(null)
                    .commit();

            if(book != null && !hasMultiPane){    //book was chosen, in portrait
                detailFragment = DetailFragment.newInstance(book);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.listContainer, detailFragment)
                        .addToBackStack(null)
                        .commit();

            }else{  //book was chosen, in landscape view
                detailFragment = DetailFragment.newInstance(book);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.listContainer, listFragment)
                        .replace(R.id.detailContainer, detailFragment)
                        .commit();
            }
        }else{
            books = new ArrayList<>();
            book = null;
            listFragment = ListFragment.newInstance(books);
            detailFragment = new DetailFragment();

            //always draw the list fragment, regardless of the screen layout
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer, listFragment)
                    .addToBackStack(null)
                    .commit();

            //if there's room, also draw the detail fragment
            if (hasMultiPane) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailContainer, detailFragment)
                        .commit();
            }
        }

        //set a listener to the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                books.clear();

                String url = "https://kamorris.com/lab/abp/booksearch.php?search=" + searchBox.getText().toString();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONObject jsonObject; //each individual json object
                                for(int i =0; i < response.length(); i++){
                                    try{
                                        jsonObject = response.getJSONObject(i);
                                        Book b = new Book(
                                                jsonObject.getInt("book_id"),
                                                jsonObject.getString("title"),
                                                jsonObject.getString("author"),
                                                jsonObject.getString("cover_url"),
                                                jsonObject.getInt("duration")
                                        ); //declare a new book

                                        books.add(b);               //add the new book to the list
                                        listFragment.update(books); //update the fragment to the new list
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceConnected && mediaBinder.isPlaying())
                    paused = true;
                else
                    paused = false;

                mediaBinder.pause();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceConnected){
                    mediaBinder.stop();
                    seekBar.setProgress(0);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mediaBinder.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(STATE_PARAM2, books);

        getSupportFragmentManager().putFragment(outState, STATE_PARAM1, listFragment);
        //getSupportFragmentManager().putFragment(outState, STATE_PARAM3, detailFragment);
        outState.putSerializable(STATE_PARAM3, book);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void openBookDetails(int pos) {
        book = books.get(pos);  //book being selected

        if(hasMultiPane){   //do not create a new detail fragment, just change values in existing fragment
            detailFragment.changeView(book);
        } else { //replace the listView with the detailView
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer, DetailFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void playAudio(Book book) {
        if(serviceConnected){

            startService(audioServiceIntent);
            mediaBinder.setProgressHandler(progressHandler);
            mediaBinder.play(book.getId());
            seekBar.setProgress(0);
            seekBar.setMax(book.getDuration());
            nowPlaying.setText("Now playing: " + book.getTitle() + " - " + book.getAuthor());
            paused = false;
        }
    }
}