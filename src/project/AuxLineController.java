package project;

import java.util.List;

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
        
        List<Song> likedSongs = model.getLikedSongs();
        for (int i = 0; i < likedSongs.size(); i ++) {
            if (song.equals(likedSongs.get(i))) {
                model.addToQueue(likedSongs.get(i));
                return;
            }
        }
        
        Song current = model.getCurrentSong();
        if (current != null && current.equals(song)) {
            model.addToQueue(current);
            return;
        }
        
        List<Song> songQueue = model.getQueuedSongs();
        for (int i = 0; i < songQueue.size(); i++) {
            if (songQueue.get(i).equals(song)) {
                model.addToQueue(songQueue.get(i));
                return;
            }
        }
        
        model.addToQueue(song);
    }
    
    public List<Song> getLikedSongs() {
        return model.getLikedSongs();
    }

    public Song playNext() {
        return model.playNext();
    }

    public Song getCurrentSong() {
        if (model.getCurrentSong() == null) {
            throw new NullPointerException("The queue is empty");
        }
        return model.getCurrentSong();
    }

    public List<Song> getQueuedSongs() {
        return model.getQueuedSongs();
    }
    
    public void likeSong(Song song, boolean like) {
        model.likeSong(song, like);
    }

}
