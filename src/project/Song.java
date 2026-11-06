package project;

public class Song {
    private final String title;
    private final String artist;
    private final String album;
    private final String genre;
    private final int durationInSec;
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
    
    public int getDurationInSec() {
        return this.durationInSec;
    }
    
    public void setLike(boolean value) {
        this.like = value;
    }
    
    public boolean getLike() {
        return this.like;
    }
}
