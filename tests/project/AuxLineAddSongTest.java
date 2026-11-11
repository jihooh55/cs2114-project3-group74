package project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/** Standalone Add Song checks; no external test library is required. */
public class AuxLineAddSongTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        testControllerAddsSongs();
        testControllerRejectsInvalidDetails();
        testConsoleAddsSong();
        testConsoleRejectsInvalidDetails();
        testConsoleRecoversAndHandlesEndOfInput();
        System.out.println("Passed " + checks + " Add Song checks.");
    }

    private static void testControllerAddsSongs() {
        AuxLineController controller = new AuxLineController();
        controller.addSong(" First ", " Artist ", " Album ", " Pop ", 180);
        controller.addSong("Second", "Another artist", null, null, 60);
        check(controller.model.songQueue.size() == 2, "Both songs are queued");
        Song first = controller.model.pop();
        check("First".equals(first.getTitle()), "Title is stored and trimmed");
        check("Artist".equals(first.getArtist()), "Artist is stored and trimmed");
        check("Album".equals(first.getAlbum()), "Album is stored and trimmed");
        check("Pop".equals(first.getgenre()), "Genre is stored and trimmed");
        check(first.getDurationInSec() == 180, "Duration is stored");
        Song second = controller.model.pop();
        check("Second".equals(second.getTitle()), "Songs retain insertion order");
        check("".equals(second.getAlbum()), "Album is optional");
        check("".equals(second.getgenre()), "Genre is optional");
    }

    private static void testControllerRejectsInvalidDetails() {
        AuxLineController controller = new AuxLineController();
        for (String blank : new String[] { null, "", " \t " }) {
            expectRejected(controller, blank, "Artist", 180, "title");
            expectRejected(controller, "Title", blank, 180, "Artist");
        }
        expectRejected(controller, "Title", "Artist", 0, "Duration");
        expectRejected(controller, "Title", "Artist", -1, "Duration");
    }

    private static void expectRejected(AuxLineController controller,
        String title, String artist, int duration, String expectedMessage) {
        try {
            controller.addSong(title, artist, "", "", duration);
            throw new AssertionError("Invalid song should be rejected");
        }
        catch (IllegalArgumentException e) {
            check(e.getMessage().contains(expectedMessage),
                "Validation identifies the invalid field");
        }
        check(controller.model.songQueue.size() == 0,
            "Rejected requests leave the queue empty");
    }

    private static void testConsoleAddsSong() throws Exception {
        String output = runConsole("1\nTitle\nArtist\n\n\n180\n");
        check(output.contains("Song title (required):"), "Title is prompted");
        check(output.contains("Artist (required):"), "Artist is prompted");
        check(output.contains("Album (optional):"), "Album is prompted");
        check(output.contains("Genre (optional):"), "Genre is prompted");
        check(output.contains("Duration in seconds (required):"),
            "Duration is prompted");
        check(output.contains("Song added to queue."), "Success is reported");
        AuxLineModel model = consoleModel();
        check(model.songQueue.size() == 1, "Console request reaches the queue");
        check("Title".equals(model.viewFirst().getTitle()),
            "Console title reaches the model");
        check("Artist".equals(model.viewFirst().getArtist()),
            "Console artist reaches the model");
        check(model.viewFirst().getDurationInSec() == 180,
            "Console duration reaches the model");
    }

    private static void testConsoleRejectsInvalidDetails() throws Exception {
        String[][] cases = {
            { "1\n \nArtist\n\n\n180\n", "Song title is required." },
            { "1\nTitle\n \n\n\n180\n", "Artist is required." },
            { "1\nTitle\nArtist\n\n\n\n", "Duration must be" },
            { "1\nTitle\nArtist\n\n\nabc\n", "Duration must be" },
            { "1\nTitle\nArtist\n\n\n1.5\n", "Duration must be" },
            { "1\nTitle\nArtist\n\n\n0\n", "Duration must be" },
            { "1\nTitle\nArtist\n\n\n-1\n", "Duration must be" },
            { "1\nTitle\nArtist\n\n\n2147483648\n", "Duration must be" }
        };
        for (String[] test : cases) {
            String output = runConsole(test[0]);
            check(output.contains(test[1]), "Console explains invalid input");
            check(!output.contains("Song added to queue."),
                "Invalid input never reports success");
            check(consoleModel().songQueue.size() == 0,
                "Invalid console input leaves the queue empty");
        }
    }

    private static void testConsoleRecoversAndHandlesEndOfInput()
        throws Exception {
        String output = runConsole("wrong\n2\n3\n"
            + "1\n \nArtist\n\n\n180\n"
            + "1\nValid\nArtist\nAlbum\nRock\n120\n");
        check(output.contains("Invalid choice."), "Invalid menu choice is handled");
        check(output.contains("Queue is empty. Add a song first."),
            "Viewing an empty queue is handled");
        check(consoleModel().songQueue.size() == 1,
            "A valid request succeeds after rejected input");
        check("Valid".equals(consoleModel().viewFirst().getTitle()),
            "Only the valid request is queued");
        for (String input : new String[] { "", "1\n", "1\nTitle\n",
            "1\nTitle\nArtist\n", "1\nTitle\nArtist\nAlbum\n",
            "1\nTitle\nArtist\nAlbum\nGenre\n" }) {
            runConsole(input);
            check(consoleModel().songQueue.size() == 0,
                "End of input does not enqueue an incomplete request");
        }
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
