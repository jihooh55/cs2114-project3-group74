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
        return scanner.nextLine();
    }
    
    private static void displayNowPlaying() {
        
    }
    
    private static void displayErrorMessage() {
        
    }
    
    

    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
