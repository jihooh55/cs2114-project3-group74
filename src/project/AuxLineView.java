package project;
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
                case "3":
                    System.out.println("This option is not available yet.");
                    break;
                default:
                    System.out.println("Invalid choice. Enter 1 to add a song.");
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
        System.out.println("3. ");
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
    
    private static void displayNowPlaying() {
        
    }
    
    private static void displayErrorMessage() {
        
    }
    
    

    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
