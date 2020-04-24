# BookShelf

This application works as a digital bookshelf, with the added functionality of an audiobook service.
Each Book object contains a title, author, and image. When the user selects an item from the list, 
the detail fragment appears with the book's information. When the user presses play in the detail fragment, 
a signal is sent from the from the fragment to the activity, where it then starts a service for the audiobook. 
The track continues to play with pause, stop, and seekbar functionality, even when the user goes back to the list fragment 
or re-orients their screen. It is only by closing the app or pressing the stop button that the audiobook service will fully stop. 
Uses audio library from Karl Morris.
