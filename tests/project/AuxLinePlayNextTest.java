package project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/** Standalone checks for advancing playback through the console and model. */
public class AuxLinePlayNextTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        testModelPlayback();
        testControllerPlayback();
        testConsolePlayback();
        testConsoleEmptyQueueRecovery();
        System.out.println("Passed " + checks + " Play Next checks.");
    }

    private static void testModelPlayback() {
        AuxLineModel model = new AuxLineModel();
        check(model.getCurrentSong() == null, "No song is playing initially");
        check(model.playNext() == null, "An empty queue has no next song");
        check(model.getCurrentSong() == null, "Empty playback leaves no current song");
        Song first = new Song("First", "Artist", "", "", 120);
        Song second = new Song("Second", "Artist", "", "", 180);
        model.addToQueue(first);
        model.addToQueue(second);
        check(model.playNext() == first, "Playback starts with the first song");
        check(model.getCurrentSong() == first, "The model records the current song");
        check(model.songQueue.size() == 1, "Playback removes exactly one song");
        check(model.viewFirst() == second, "The second song remains queued");
        check(model.playNext() == second, "Playback advances in insertion order");
        check(model.getCurrentSong() == second, "The next song becomes current");
        check(model.songQueue.size() == 0, "The last queued song is removed");
        check(model.playNext() == null, "An exhausted queue has no next song");
        check(model.getCurrentSong() == second,
            "An empty request preserves the current song");
        model.addToQueue(first);
        check(model.playNext() == first, "Playback works after refilling the queue");
    }

    private static void testControllerPlayback() {
        AuxLineController controller = new AuxLineController();
        check(controller.playNext() == null, "Controller handles an empty queue");
        controller.addSong("First", "Artist", "", "", 120);
        controller.addSong("Second", "Artist", "", "", 180);
        Song first = controller.playNext();
        check("First".equals(first.getTitle()), "Controller advances the model");
        check(controller.getCurrentSong() == first, "Controller exposes current song");
        check(controller.model.songQueue.size() == 1, "Controller removes one song");
        check("Second".equals(controller.playNext().getTitle()),
            "Controller advances to the next song");
        check(controller.playNext() == null, "Controller handles an exhausted queue");
    }

    private static void testConsolePlayback() throws Exception {
        String output = runConsole("1\nFirst\nArtist A\n\n\n120\n"
            + "1\nSecond\nArtist B\n\n\n180\n2\n2\n2\n");
        String first = "Now playing: First by Artist A (120 seconds)";
        String second = "Now playing: Second by Artist B (180 seconds)";
        check(output.contains(first), "Console displays the first song details");
        check(output.contains(second), "Console displays the second song details");
        check(output.indexOf(first) < output.indexOf(second),
            "Console plays songs in insertion order");
        check(output.contains("Queue is empty. Add a song first."),
            "Console explains when no next song is available");
        check(consoleModel().songQueue.size() == 0, "Console drains the queue");
        check("Second".equals(consoleModel().getCurrentSong().getTitle()),
            "Empty console request preserves the current song");
    }

    private static void testConsoleEmptyQueueRecovery() throws Exception {
        String output = runConsole("2\n1\nNew song\nArtist\n\n\n60\n2\n");
        check(output.contains("Queue is empty. Add a song first."),
            "Playing an initially empty queue shows a message");
        check(output.contains("Now playing: New song by Artist (60 seconds)"),
            "User can add and play a song after an empty request");
        check(consoleModel().songQueue.size() == 0, "Recovered playback dequeues song");
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
        return output.toString("UTF-8");
    }

    private static AuxLineModel consoleModel() throws Exception {
        Field field = AuxLineView.class.getDeclaredField("controller");
        field.setAccessible(true);
        return ((AuxLineController)field.get(null)).model;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        checks++;
    }
}
