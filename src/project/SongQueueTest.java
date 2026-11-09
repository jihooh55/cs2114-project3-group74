package project;


import java.util.NoSuchElementException;
import student.TestCase;

public class SongQueueTest extends TestCase {
    SongQueue<Song> songQueue = new SongQueue<>();
    Song song1 = new Song("Peaches");
    Song song2 = new Song("Stitches");
    
    public void setUp() {
        songQueue.enqueue(song1);
    }
    
    public void testEnqueue() {
        assertEquals(1, songQueue.size());
        songQueue.enqueue(song2);
        assertEquals(2, songQueue.size());
    }
    
    public void testDequeue() {
        assertEquals(1, songQueue.size());
        assertNotNull(songQueue.getFirst());
        assertEquals(song1, songQueue.dequeue().getData());
        assertEquals(0, songQueue.size());
        Exception exception = null;
        try {
            songQueue.dequeue();
        }
        catch (NoSuchElementException e){
            exception = e;
        }
        assertNotNull(exception);
    }
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
