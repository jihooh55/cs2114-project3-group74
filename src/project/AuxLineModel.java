package project;
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
    
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
