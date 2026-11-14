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
                    displayNowPlaying();
                    break;
                case "2":
                    addSong();
                    break;
                case "3":
                    playNext();
                    break;
                case "4":
                    viewQueue();
                    break;
                case "5":
                    on = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println(
                        "Invalid choice. Enter 1,2,3,4,5.");
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
        System.out.println("1. View the currently playing song");
        System.out.println("2. Add a song to queue");
        System.out.println("3. Play next song in queue");
        System.out.println("4. View queue");
        System.out.println("5. Exit");
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
        while (title != null && title.trim().isEmpty()) {
            System.out.println("Song title cannot be empty");
            title = prompt("Song title (required):");
        }
        String artist = prompt("Artist (required):");
        while (artist.isEmpty()) {
            System.out.println("Artist name cannot be empty");
            artist = prompt("Artist (required):");
        }
        String album = prompt("Album (optional):");
        
        String genre = prompt("Genre (optional):");       
      
        int durationInSec = 0;
        boolean canProceed = false;
        
        String duration = prompt("Duration in seconds (required):");
        
        while (canProceed == false) {
            canProceed = true;
            if (duration.isEmpty()) {
                canProceed = false;
                System.out.println("Duration cannot be empty");
            }
            else {
                try {
                    durationInSec = Integer.parseInt(duration.trim());
                }
                catch (NumberFormatException e) {
                    canProceed = false;
                    System.out.println(
                        "Duration must be a positive whole number of seconds. Please enter a valid duration.");
                }
                if (durationInSec <= 0) {
                    canProceed = false;
                    System.out.println(
                        "Duration must be a positive whole number of seconds. Please enter a valid duration.");
                }
            }
            if (canProceed == false) {
                duration = prompt("Duration in seconds (required):");
            }
            else {

                durationInSec = Integer.parseInt(duration.trim());
            }
        }
        
        controller.addSong(title, artist, album, genre, durationInSec);
        System.out.println("Song added to queue successfully.");
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
        Song song;
        try {
            song = controller.getCurrentSong();
        }
        catch (NullPointerException e) {
            System.out.println("No song is being played currently.");
            return;
        }
        song = controller.getCurrentSong();
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

}
