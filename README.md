# AuxLine

**CS 2114 • Project 3**  
Nolan Ayodeji · Oscar Chen · Jiho Oh · Enoch Shum

AuxLine is a shared music-request app for students hanging out or hosting small gatherings. Friends enter song requests on one computer, see what's coming up, and the host advances the lineup with **Play Next**. This version shows the current song but does not play audio or connect to Spotify.

Song requests wait in a custom singly-linked FIFO queue (`SongQueue<E>`) with head and tail references, so enqueue and dequeue are both O(1) and nobody can jump ahead of an earlier request.

---

## Running the program

Run these commands from the repo folder:

```bash
javac -d bin -sourcepath src src/project/AuxLineView.java
java -cp bin project.AuxLineView
```

In Eclipse, open `AuxLineView.java` and select **Run As → Java Application**. To run the queue tests, open `SongQueueTest.java` and select **Run As → JUnit Test** with the course libraries loaded.

To try the model on its own, run `jshell --class-path bin` after compiling and enter:

```java
import project.*;
var model = new AuxLineModel();
model.addToQueue(new Song("Peaches"));
model.addToQueue(new Song("Stitches"));
model.playNext().getTitle();       // Peaches
model.playNext().getTitle();       // Stitches
model.playNext() == null;          // true
model.getCurrentSong().getTitle(); // Stitches
/exit
```

### Menu (CLI)

```text
1. View the currently playing song
2. Add a song to queue
3. Play next song in queue
4. View queue
5. Exit
```

---

## Architecture (MVC)

| Class | Role | Responsibility |
|---|---|---|
| `Song` | Model | Stores the title, artist, album, genre, duration in seconds, and like status. |
| `Node<E>` | Model | Stores an entry and a link to the next node. |
| `SongQueue<E>` | Model | Stores waiting songs in FIFO order. Its main operations are `enqueue`, `dequeue`, `getFirst`, and `size`. |
| `AuxLineModel` | Model | Holds the waiting queue and current song, advances the lineup, and returns a list of waiting songs. |
| `AuxLineController` | Controller | Checks song details, creates `Song` objects, and passes requests between the view and model. |
| `AuxLineView` | View | Reads input with `Scanner` and prints the menu, current song, waiting queue, and messages. |

### Model / view boundary

- The view uses `Song` getters and a `List<Song>` from the controller to show the lineup. Queue nodes stay inside the model and queue code.
- The model can run without the console or controller. Input and printing happen in the view.
- The view asks the controller for the latest state when the user selects Now Playing or View Queue. It also displays the current song after a successful Play Next.

### Where validation lives

| Bad input | Caught by | Result |
|---|---|---|
| Blank title or artist | `AuxLineView.addSong()` and `AuxLineController.addSong(...)` | The console asks for the field again. The controller also rejects blank fields passed directly to it. |
| Duration that is not a positive whole number | `AuxLineView.addSong()` and `AuxLineController.addSong(...)` | The console asks again. The controller rejects durations of zero or less. |
| Play Next on an empty queue | `AuxLineModel.playNext()` and `AuxLineView.playNext()` | The model keeps the current song and the view prints an empty-queue message. |
| `dequeue()` or `getFirst()` on an empty queue | `SongQueue` | Throws `NoSuchElementException`. |
| Unrecognized menu choice | `AuxLineView` | Prints a message and shows the menu again. |

The 100-character field limit and the 100-request queue limit are still pending. Ending input at the main menu exits the app, but ending input during Add Song can cause a crash.

---

## Reflection

### What changed from the specification?

- **Classes:** We kept the six main classes and put `main()` in `AuxLineView`. That gave us a simple way to start the console without adding a separate launcher.
- **Method signatures:** The controller uses `addSong(String title, String artist, String album, String genre, int durationInSec)`. The model uses `playNext()`, which returns the song that just became current. If the queue is empty, it returns `null` so the view can show a message while the model keeps the current song.
- **Where validation lives:** The view handles text input and asks again for blank required fields or an invalid duration. The controller checks the required fields and duration before creating a song. This lets the console explain the problem while also giving the controller its own checks.
- **Test cases:** `SongQueueTest` checks enqueue, dequeue, and what happens when you dequeue from an empty queue. We also added standalone tests for Add Song, Play Next, viewing the queue, and exiting so we could check the console flow.
- **Scope:** We built Play Next first because it shows how the queue works. Play/Pause, Seek, and Like are still open. The prototype asks for optional album and genre fields, and we still need to add requester names.

### Did the model really stand alone?

Yes. We can create an `AuxLineModel`, add songs, and call `playNext()` without opening the console or creating a controller. The model keeps track of the waiting queue and the current song. The view handles prompts and messages.

### What happened to our unknowns?

| Unknown (from Scope) | Status | Notes |
|---|---|---|
| How do we refresh the interface after queue changes without putting queue logic in the view? | Solved for the console | The view asks the controller for the current song or waiting list when it needs to display them. |
| How do we test transitions between empty, one-request, and multiple-request queues? | Tested manually | We added two songs, played both in order, and then hit Play Next again. The last song stayed current. |
| For save/restore, how do we keep request order and handle a missing or damaged file? | Open | We plan to save the waiting songs in FIFO order and check the whole file before replacing the active session. |

### What doesn't work yet?

- Play/Pause, Seek, Like, and requester names are still missing.
- The field and queue limits are still missing. Closing input in the middle of Add Song can crash the console.
- The three standalone test programs currently fail. One expects an older prompt, one expects the controller to return `null` before a song starts, and one hits the Add Song input crash while using the older menu flow.

---

## Next Steps

### What's left to finish the MVP (in order)

1. Fix the Add Song crash when input ends, handle Now Playing before a song starts, and update the console tests.
2. Add requester names and enforce the 100-character and 100-request limits.
3. Add simulated Play/Pause, Seek, and Like. Use Play Next as the skip action.

### Change feature

**Save and restore the session.** The host can save the current song and waiting requests, close AuxLine, and load the same lineup later. If the file is missing or damaged, the app should show an error and keep the active lineup.

| Class | Expected to change? | Why |
|---|---|---|
| `Song` | No | We expect to save and restore the song fields available when the feature is added. |
| `Node<E>` | No | A node still stores an entry and a link to the next node. |
| `SongQueue<E>` | No | We can rebuild the waiting list by enqueuing the saved songs in order. |
| `AuxLineModel` | Yes | Add `snapshotSession()` and `restoreSession(...)` to capture and restore the current song and waiting list. |
| `AuxLineController` | Yes | Add `saveSession(...)` and `loadSession(...)` to handle the file and call the model. |
| `AuxLineView` | Yes | Update `displayMenu()` and `main()` to handle Save and Load and show the result. |

### Stretch goals

| Stretch goal (from Scope) | Decision |
|---|---|
| Save and restore the current song and waiting requests | Attempt as our change feature |
| Session history with request counts per person and repeat-song counts | Drop for Deliverable 4 |
| Rating system after each song | Drop for Deliverable 4 |
| Multi-user shared queue over a server | Drop for Deliverable 4 |

### New tests needed

- Try blank and oversized fields, invalid durations, and closing input during Add Song. Verify that request 100 is accepted and request 101 leaves the queue unchanged.
- Test Play/Pause, Seek, and Like with and without a current song. Try seeking within the duration and past the end of the song.
- Test `snapshotSession()` and `restoreSession(...)` with an empty session and with a current song and multiple waiting songs. Verify that Play Next follows the original request order after loading.
- Test Save and Load through the console with a valid file. Try an invalid save path and a missing or damaged load file. Check that the app shows an error and keeps the active lineup.

### Who does what

| Item | Proposed owner |
|---|---|
| Fix the Add Song and Now Playing edge cases and update the console tests | Oscar Chen |
| Add requester names and enforce the input and queue limits | Jiho Oh |
| Add and test the simulated playback controls | Nolan Ayodeji |
| Add and test Save and Restore | Enoch Shum |

---

## Attribution

<!-- Fill in each row with who worked on the file and what they did. Use wording that matches the work, such as "Wrote the first version," "Revised the empty-queue logic," "Reviewed the queue behavior," "Tested invalid inputs," or "Used GenAI to brainstorm test cases and checked them against the code." Confirm the proposed owners above before submitting. -->

| File | Member(s) | How (wrote / revised / reviewed / tested / GenAI-assisted) |
|---|---|---|
| `src/project/Song.java` | Jiho Oh | Wrote and updated the class that stores song details. |
| `src/project/Node.java` | Jiho Oh | Wrote and updated the node class used by the linked queue. |
| `src/project/SongQueue.java` | Jiho Oh | Wrote and revised the linked queue methods for adding and removing songs. |
| `src/project/AuxLineModel.java` | Jiho Oh, Oscar Chen | Jiho worked on the initial model. Oscar added Play Next and access to the waiting list. |
| `src/project/AuxLineController.java` | Jiho Oh, Oscar Chen | Jiho worked on the initial controller. Oscar added methods to handle song requests, advance playback, and retrieve the queue. |
| `src/project/AuxLineView.java` | Jiho Oh, Oscar Chen | Jiho revised the Now Playing display. Oscar added and updated menu options for adding songs, playing the next song, and viewing the queue. |
| `src/project/SongQueueTest.java` | Jiho Oh | Wrote tests for enqueue, dequeue, and dequeuing from an empty queue. |
| `tests/project/AuxLineAddSongTest.java` | Oscar Chen | Wrote checks for adding songs and rejecting invalid song details. |
| `tests/project/AuxLinePlayNextTest.java` | Oscar Chen | Wrote checks for advancing through songs in order and handling an empty queue. |
| `tests/project/AuxLineQueueAndExitTest.java` | Oscar Chen | Wrote checks for viewing the waiting queue and exiting the console. |

### GenAI note

For Deliverable 3, We used ChatGPT to compare the prototype with the rubric, help draft the README template, and predict which classes Save and Restore would affect. The review included running the console flow and checking where the standalone tests failed. We also used Ai for difficult debugging issues when programming. 
