package project;
import java.util.List;
import java.util.Scanner;

public class AuxLineView {
    private static AuxLineController controller = new AuxLineController();
    private static boolean on = true;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        setUp();
        System.out.println("Hello, start by queueing a song");
        
        while (on == true) {
            displayMenu();
            String choice = readUserInput();
            if (choice == null) {
                break;
            }
            switch (choice.trim()) {
                case "1":
                    addSong();
                    break;
                case "2":
                    playNext();
                    break;
                case "3":
                    viewQueue();
                    break;
                case "4":
                    on = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println(
                        "Invalid choice. Enter 1, 2, 3, or 4.");
                    break;
            }
        }
    }
    
    private static void setUp() {
        controller = new AuxLineController();
        on = true;
        scanner = new Scanner(System.in);
    }
    
    private static void displayMenu() {
        System.out.println("-----OPTIONS-----");
        System.out.println("1. Add a song to queue");
        System.out.println("2. Play next song in queue");
        System.out.println("3. View queue");
        System.out.println("4. Exit");
    }
    
    private static String readUserInput() {
        if (!scanner.hasNextLine()) {
            on = false;
            return null;
        }
        return scanner.nextLine();
    }

    private static void addSong() {
        String title = prompt("Song title (required):");
        if (title == null) {
            return;
        }
        String artist = prompt("Artist (required):");
        if (artist == null) {
            return;
        }
        String album = prompt("Album (optional):");
        if (album == null) {
            return;
        }
        String genre = prompt("Genre (optional):");
        if (genre == null) {
            return;
        }
        String duration = prompt("Duration in seconds (required):");
        if (duration == null) {
            return;
        }

        try {
            int durationInSec = Integer.parseInt(duration.trim());
            controller.addSong(title, artist, album, genre, durationInSec);
            System.out.println("Song added to queue.");
        }
        catch (NumberFormatException e) {
            System.out.println(
                "Duration must be a positive whole number of seconds.");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String prompt(String message) {
        System.out.println(message);
        return readUserInput();
    }
    
    private static void playNext() {
        if (controller.playNext() == null) {
            System.out.println("Queue is empty. Add a song first.");
            return;
        }
        displayNowPlaying();
    }

    private static void displayNowPlaying() {
        Song song = controller.getCurrentSong();
        System.out.println("Now playing: " + song.getTitle() + " by "
            + song.getArtist() + " (" + song.getDurationInSec() + " seconds)");
    }

    private static void viewQueue() {
        List<Song> songs = controller.getQueuedSongs();
        if (songs.isEmpty()) {
            System.out.println("Queue is empty. Add a song first.");
            return;
        }
        System.out.println("Queued songs:");
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            System.out.println((i + 1) + ". " + song.getTitle() + " by "
                + song.getArtist() + " (" + song.getDurationInSec()
                + " seconds)");
        }
    }
    
    private static void displayErrorMessage() {
        
    }
    
    

    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
