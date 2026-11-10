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
    
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
