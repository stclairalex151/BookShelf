package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources resources = getResources();

        //TODO: put this in a worker function
        String[] bookTitles = resources.getStringArray(R.array.bookTitles); //gets the array of book titles
        String[] bookAuthors = resources.getStringArray(R.array.bookAuthors);//gets the array of book authors

        ArrayList<HashMap<String, String>> books = new ArrayList<>();//list of hashmaps

        //puts 10 hashmaps in the arraylist (all called temp) which have title and author
        for(int i = 0; i < bookTitles.length; i++){
            HashMap<String, String> temp = new HashMap<>();
            temp.put(bookTitles[i], bookAuthors[i]);
            books.add(temp);
        }


    }
}
