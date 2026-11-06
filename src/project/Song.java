package project;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int durationInSec;
    private boolean like;
    
    public Song(String title, String artist, String album, String genre, int durationInSec) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.durationInSec = durationInSec;
        this.like = false;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getArtist() {
        return this.artist;
    }
    
    public String getAlbum() {
        return this.album;
    }
    
    public String getgenre() {
        return this.genre;
    }
    
    public String getdurationInSec() {
        return this.du;
    }
    
    
    
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
