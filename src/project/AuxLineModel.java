package project;

import java.util.ArrayList;
import java.util.List;

public class AuxLineModel {
    Song currentSong;
    SongQueue<Song> songQueue;
    
    public AuxLineModel() {
        currentSong = null;
        songQueue = new SongQueue<>();
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
    
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
