package edu.temple.bookshelf;

import java.io.Serializable;

class Book implements Serializable {

    private int id;         //the id of the current book
    private String title;   //the title of the book
    private String author;  //the author of the book
    private String coverURL;//the URL to the image showing the book cover

    //constructor
    public Book(int id, String title, String author, String coverURL) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
    }

    /**
     * Getter for the ID
     * @return the unique ID of this object
     */
    int getId(){ return id; }

    /**
     * Getter for the title
     * @return the title of the given book
     */
    String getTitle(){ return title; }

    /**
     * Getter for the author
     * @return the author of the given book
     */
    String getAuthor(){ return author; }

    /**
     * Getter for the image URL
     * @return the URL to this books cover image
     */
    String getCoverURL(){ return coverURL;}

    /**
     * Setter for the title
     * @param t the new title
     */
    void setTitle(String t){ title = t; }

    /**
     * Setter for the author
     * @param a the new author
     */
    void setAuthor(String a){ author = a; }

    /**
     * Setter for the cover URL
     * @param url the URL of the new image
     */
    void setCoverURL(String url){ coverURL = url; }

    /**
     * Setter for the book Id
     * @param id the ID of the new book
     */
    public void setId(int id) { this.id = id; }
}
