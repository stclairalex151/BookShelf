package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ListFragment.BookClickedInterface{

    boolean hasMultiPane;   //true if theres enough room for >1 fragment
    DetailFragment detailFragment;  //copy of the detail fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();   //copy of resources

        //TODO: put this in a worker function
        String[] keys = resources.getStringArray(R.array.keys);
        String[] bookTitles = resources.getStringArray(R.array.bookTitles); //gets the array of book titles
        String[] bookAuthors = resources.getStringArray(R.array.bookAuthors);//gets the array of book authors

        ArrayList<HashMap<String, String>> books = new ArrayList<>();//list of hashmaps

        //puts 10 hashmaps in the arraylist (all called temp) which have title and author
        for(int i = 0; i < bookTitles.length; i++){
            HashMap<String, String> temp = new HashMap<>();
            temp.put(keys[0], bookTitles[i]);
            temp.put(keys[1], bookAuthors[i]);
            books.add(temp);
        }

        //always draw the list fragment, regardless of the screen layout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.listContainer, ListFragment.newInstance(books))
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
    }

    @Override
    public void openDetails(HashMap<String, String> book) {
        //TODO: should either of these be add instead of replace?
        if(hasMultiPane){
            //replace detailContainer with a new instance given book
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, DetailFragment.newInstance(book))
                    .commit();
        } else { //replace the listView with the detailView
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer, DetailFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
