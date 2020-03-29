package edu.temple.bookshelf;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ListFragment.BookClickedInterface{

    boolean hasMultiPane;   //true if theres enough room for >1 fragment
    DetailFragment detailFragment;  //copy of the detail fragment
    ArrayList<HashMap<String, String>> books;   //list of books

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildShelf();   //sets up the arraylist of books

        //always draw the list fragment, regardless of the screen layout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.listContainer, ListFragment.newInstance(books))
                .addToBackStack(null)
                .commit();

        //if there's room, also draw the detail fragment
        hasMultiPane = findViewById(R.id.detailContainer) != null;
        if(hasMultiPane) {
            detailFragment = DetailFragment.newInstance(books.get(0));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, detailFragment)
                    .commit();
        }
    }

    @Override
    public void openDetails(int pos) {
        //TODO: should either of these be add instead of replace?
        HashMap<String, String> book = books.get(pos);  //book being selected

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

    /**
     * Worker function that gets values from resource files
     * and uses them to build an ArrayList of book (Hashmap) objects
     * (MODIFIES THE LOCAL VARIABLE BOOKS)
     */
    private void buildShelf(){
        Resources resources = getResources();   //copy of resources
        String[] keys = resources.getStringArray(R.array.keys);
        String[] bookTitles = resources.getStringArray(R.array.bookTitles); //gets the array of book titles
        String[] bookAuthors = resources.getStringArray(R.array.bookAuthors);//gets the array of book authors

        books = new ArrayList<>();//list of hashmaps

        //puts 10 hashmaps in the arraylist (all called temp) which have title and author
        for(int i = 0; i < bookTitles.length; i++){
            HashMap<String, String> temp = new HashMap<>();
            temp.put(keys[0], bookTitles[i]);
            temp.put(keys[1], bookAuthors[i]);
            books.add(temp);
        }
    }
}
