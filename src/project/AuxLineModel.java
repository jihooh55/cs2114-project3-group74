package project;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class AuxLineModel {
    Song currentSong;
    SongQueue<Song> songQueue;
    List<Song> likedSongs;
    
    public AuxLineModel() {
        currentSong = null;
        songQueue = new SongQueue<>();
        likedSongs = new ArrayList<>();
    }
    
    public boolean isEmpty() {
        return songQueue.size() == 0;
    }
    
    public void addToQueue(Song newSong) {
        songQueue.enqueue(newSong);
    }
    
    public Song viewFirst() {
        return songQueue.getFirst().getData();
    }
    
    public Song pop() {
        return songQueue.dequeue().getData();
    }

    /** Advances to the next queued song, preserving the current song if empty. */
    public Song playNext() {
        if (songQueue.size() == 0) {
            return null;
        }
        currentSong = pop();
        return currentSong;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    /** Returns a snapshot of the waiting songs in playback order. */
    public List<Song> getQueuedSongs() {
        List<Song> songs = new ArrayList<>();
        if (songQueue.size() == 0) {
            return songs;
        }
        Node<Song> node = songQueue.getFirst();
        while (node != null) {
            songs.add(node.getData());
            node = node.getNext();
        }
        return songs;
    }
    
    public void likeSong(Song song, boolean liked) {
        if (song.getLike() == liked) {
            if (liked == true) {
                throw new NoSuchElementException("Song is already liked");
            }
            else if (liked == false) {
                throw new NoSuchElementException("Song is already unliked");
            }
        }
        if (liked == true) {
            for (int i = 0; i < likedSongs.size(); i ++) {
                if (likedSongs.get(i).equals(song)) {
                    throw new NoSuchElementException("This song is already in the liked song list");
                }
            }
            song.setLike(true);
            likedSongs.add(song);
        }
        song.setLike(liked);
    }
    
    public List<Song> getLikedSongs() {
        return likedSongs;
    }
    
    
    
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
