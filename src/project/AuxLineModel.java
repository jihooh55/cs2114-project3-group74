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
    
    
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
