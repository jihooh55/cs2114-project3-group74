package project;

/**
 * // -------------------------------------------------------------------------
 * /**
 * Methods for the Song class
 * 
 * @author
 * @version Nov 12, 2026
 */
public class Song {
    private final String title;
    private final String artist;
    private final String album;
    private final String genre;
    private final int durationInSec;
    private boolean like;

    /**
     * Constructor for a Song object
     * Sets everything null/0 except title
     * 
     * @param title
     *            The title of the Song object
     */
    public Song(String title) {
        this.title = title;
        this.artist = null;
        this.album = null;
        this.genre = null;
        this.durationInSec = 0;
        this.like = false;
    }


    /**
     * Constructor for a Song object
     * Initializes every field except like, which is initialized to false by
     * default
     * 
     * @param title
     *            The title of the Song object
     * @param artist
     *            The name of the artist of the Song object
     * @param album
     *            The name of the album of the Song object
     * @param genre
     *            The genre of the Song object
     * @param durationInSec
     *            The duration of the Song object
     */
    public Song(
        String title,
        String artist,
        String album,
        String genre,
        int durationInSec) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.durationInSec = durationInSec;
        this.like = false;
    }


    /**
     * Returns the song's title
     * 
     * @return
     *         This Song object's title field
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * Returns the song's artist name
     * 
     * @return
     *         This Song object's artist field
     */
    public String getArtist() {
        return this.artist;
    }


    /**
     * Returns the song's album name
     * 
     * @return
     *         This Song object's album field
     */
    public String getAlbum() {
        return this.album;
    }


    /**
     * Returns the song's genre
     * 
     * @return
     *         This Song object's genre field
     */
    public String getgenre() {
        return this.genre;
    }


    /**
     * Returns the song's duration
     * 
     * @return
     *         This Song object's durationInSec field
     */
    public int getDurationInSec() {
        return this.durationInSec;
    }


    /**
     * Sets this Song object's like value
     * 
     * @param value
     *            The updated like value (true or false)
     */
    public void setLike(boolean value) {
        this.like = value;
    }


    /**
     * Returns the song's like value
     * 
     * @return
     *         This Song object's like field
     */
    public boolean getLike() {
        return this.like;
    }
}
