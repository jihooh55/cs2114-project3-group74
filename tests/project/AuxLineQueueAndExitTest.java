package project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Standalone checks for queue viewing and exiting the console. */
public class AuxLineQueueAndExitTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        testQueueSnapshot();
        testConsoleQueue();
        testConsoleExit();
        System.out.println("Passed " + checks + " Queue and Exit checks.");
    }

    private static void testQueueSnapshot() {
        AuxLineController controller = new AuxLineController();
        check(controller.getQueuedSongs().isEmpty(), "Initial queue is empty");
        controller.addSong("First", "Artist A", "", "", 120);
        controller.addSong("Second", "Artist B", "", "", 180);
        List<Song> songs = controller.getQueuedSongs();
        check(songs.size() == 2, "Snapshot includes both songs");
        check("First".equals(songs.get(0).getTitle()), "First song comes first");
        check("Second".equals(songs.get(1).getTitle()), "Second song comes next");
        check(controller.getCurrentSong() == null, "Viewing does not start playback");
        songs.clear();
        check(controller.getQueuedSongs().size() == 2,
            "Changing snapshot membership does not affect the queue");
        Song first = controller.playNext();
        check("First".equals(first.getTitle()), "Viewing preserves playback order");
        check(controller.getQueuedSongs().size() == 1,
            "Playing removes one song from the waiting list");
        check("Second".equals(controller.getQueuedSongs().get(0).getTitle()),
            "Current song is excluded from the waiting list");
        check(controller.getCurrentSong() == first,
            "Viewing preserves the current song");
        check(songs.isEmpty(), "Snapshot does not change when playback advances");
        controller.playNext();
        check(controller.getQueuedSongs().isEmpty(), "Drained queue is empty");
        controller.addSong("New", "Artist", "", "", 60);
        check("New".equals(controller.getQueuedSongs().get(0).getTitle()),
            "Viewing works after refilling the queue");
    }

    private static void testConsoleQueue() throws Exception {
        String output = runConsole("3\n"
            + "1\nFirst\nArtist A\n\n\n120\n"
            + "1\nSecond\nArtist B\n\n\n180\n3\n3\n2\n3\n2\n3\n4\n");
        String both = "Queued songs:\n1. First by Artist A (120 seconds)\n"
            + "2. Second by Artist B (180 seconds)";
        check(output.contains("3. View queue"), "Menu offers queue viewing");
        check(count(output, "Queue is empty. Add a song first.") == 2,
            "Initially empty and drained queues show a helpful message");
        check(count(output, both) == 2, "Repeated viewing preserves queue order");
        check(output.contains("Now playing: First by Artist A (120 seconds)"),
            "Viewing does not remove the first song");
        check(output.contains("Queued songs:\n1. Second by Artist B (180 seconds)"),
            "Viewing after playback shows only the waiting song");
        check(output.contains("Now playing: Second by Artist B (180 seconds)"),
            "Viewing does not remove the second song");
    }

    private static void testConsoleExit() throws Exception {
        String output = runConsole(" 4 \n1\nIgnored\nArtist\n\n\n60\n");
        check(output.contains("4. Exit"), "Menu offers exit");
        check(count(output, "Goodbye!") == 1, "Exit says goodbye once");
        check(count(output, "-----OPTIONS-----") == 1,
            "Exit does not display another menu");
        check(!output.contains("Song title (required):"),
            "Exit stops reading further commands");
        check(runConsole("").contains("-----OPTIONS-----"),
            "Console can restart after exit and handle end of input");
        output = runConsole("wrong\n4\n");
        check(output.contains("Invalid choice. Enter 1, 2, 3, or 4."),
            "Invalid input explains all available options");
        check(output.contains("Goodbye!"), "Exit works after invalid input");
    }

    private static String runConsole(String input) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, "UTF-8"));
            AuxLineView.main(new String[0]);
        }
        finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return output.toString("UTF-8").replace("\r\n", "\n");
    }

    private static int count(String text, String target) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(target, index)) >= 0) {
            count++;
            index += target.length();
        }
        return count;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        checks++;
    }
}
