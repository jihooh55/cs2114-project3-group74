package project;
public class AuxLineController {
    AuxLineModel model;
    
    //~ Fields ................................................................
    public AuxLineController() {
        model = new AuxLineModel();
    }
    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

    public void addSong(String title, String artist, String album,
        String genre, int durationInSec) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Song title is required.");
        }
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist is required.");
        }
        if (durationInSec <= 0) {
            throw new IllegalArgumentException(
                "Duration must be a positive whole number of seconds.");
        }
        Song song = new Song(title.trim(), artist.trim(),
            album == null ? "" : album.trim(),
            genre == null ? "" : genre.trim(), durationInSec);
        model.addToQueue(song);
    }

    public Song playNext() {
        return model.playNext();
    }

    public Song getCurrentSong() {
        return model.getCurrentSong();
    }

}
