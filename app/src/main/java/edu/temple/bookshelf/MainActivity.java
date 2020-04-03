package edu.temple.bookshelf;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements ListFragment.BookClickedInterface{

    boolean hasMultiPane;           //true if there's enough room for > 1 fragment
    DetailFragment detailFragment;  //copy of the detail fragment
    ListFragment listFragment;      //copy of the list fragment
    ArrayList<Book> books;          //list of books
    RequestQueue requestQueue;      //a queue for JSON object request
    EditText searchBox;             //the search box
    Button searchButton;            //the search button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        books = new ArrayList<>();
        listFragment = ListFragment.newInstance(books);

        searchBox = findViewById(R.id.searchBox);
        searchButton = findViewById(R.id.searchButton);

        //always draw the list fragment, regardless of the screen layout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.listContainer, listFragment)
                .addToBackStack(null)
                .commit();

        //if there's room, also draw the detail fragment
        hasMultiPane = findViewById(R.id.detailContainer) != null;
        if(hasMultiPane) {
            detailFragment = new DetailFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, detailFragment)
                    .commit();
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
                                                jsonObject.getString("cover_url")
                                        ); //declare a new book

                                        books.add(b);   //add the new book to the list
                                        listFragment.update(books);//update the fragment to the new list
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
    }

    @Override
    public void openDetails(int pos) {
        Book book = books.get(pos);  //book being selected

        if(hasMultiPane){
            //do not create a new detail fragment, just change values in existing fragment
            detailFragment.changeView(book);

        } else { //replace the listView with the detailView
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer, DetailFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit();
        }
    }
}