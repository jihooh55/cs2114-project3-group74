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

### What changed from the specification & D3 Plan?
- **Feature Switch:** In D3, we planned to build "Save and restore the session." We decided to switch our Deliverable 4 change feature to **"Like a Song"** instead. This allowed us to focus on successfully managing concurrent lists (the main queue vs. the liked songs list) and interactive state changes before tackling file I/O.
- **Classes:** We kept the six main classes and put `main()` in `AuxLineView`. 
- **Method signatures:** The controller uses `addSong()`. The model uses `playNext()`, which returns the song that just became current or `null` if empty.
- **Where validation lives:** The view handles text input and asks again for blank fields or invalid durations. The controller also checks required fields before creating a song.

### How did your design absorb the change? (D3 Prediction vs. Reality)
To implement the "Like" feature, we touched `Song`, `AuxLineModel`, `AuxLineController`, and `AuxLineView`. 
- **What matched:** As predicted in our general architecture, the structural queue classes (`Node` and `SongQueue`) did not have to change at all to support this new feature. We successfully preserved their isolation. 
- **What surprised us:** We initially assumed we wouldn't need to change `Song`, but we ended up adding a `like` boolean field to it.

### Did the change cross the model/view line?
No, the boundary was respected. The `AuxLineView` learned how to display the "🤍/🖤" status, but it does not modify the data directly. It strictly calls `controller.likeSong()` and `controller.getLikedSongs()`. If a change rippled, it was purely routing the request safely down to the `AuxLineModel`.

### What design change would make the next feature cheaper?
Refactoring the `AuxLineView` scanner prompts into a centralized, robust `InputHandler` class would make adding new console features (like search or user profiles) much cheaper and completely eliminate edge-case input crashes across the board.

### What doesn't work yet?
- Play/Pause, Seek, and requester names are still missing.
- The 100-character field limit and the 100-request queue limit are still missing. 
- The three standalone test programs currently fail because they expect older prompts and controller behaviors.

---

## Next Steps

### What's left to finish the MVP (in order)
1. Fix the standalone console tests to match the new View prompts and Controller null-handling.
2. Add requester names and enforce the 100-character and 100-request limits.
3. Add simulated Play/Pause and Seek. Use Play Next as the skip action.

### Stretch goals
- **Committed to:** Save and restore the session (our original D3 idea, which we will now push to the final build).
- **Dropped:** Session history with request counts, Rating system, and Multi-user shared queue over a server.

### 3-Minute Demo Path
- **(0:00 - 0:45) Validation & Add:** Start the app. Attempt to add a song with a blank title and a negative duration to prove bad input survives. Successfully add 3 songs.
- **(0:45 - 1:15) Queue Order:** View the queue to show FIFO order is maintained.
- **(1:15 - 1:45) Playback:** Select Play Next twice to demonstrate the Model advancing the lineup. 
- **(1:45 - 2:45) New Change Feature (Like):** Like the current playing song. Attempt to like it a second time to show error handling. View the Liked menu to show the song was saved to the separate liked list. Unlike the song. 
- **(2:45 - 3:00):** Exit the app gracefully.

### Who does what
| Item | Proposed owner |
|---|---|
| Fix the standalone console tests and handle Now Playing before a song starts | Oscar Chen |
| Add requester names and enforce the input and queue limits | Jiho Oh |
| Add and test the simulated playback controls | Nolan Ayodeji |
| Add and test Save and Restore for the final build | Enoch Shum |

---

## Attribution

<!-- Fill in each row with who worked on the file and what they did. Use wording that matches the work, such as "Wrote the first version," "Revised the empty-queue logic," "Reviewed the queue behavior," "Tested invalid inputs," or "Used GenAI to brainstorm test cases and checked them against the code." Confirm the proposed owners above before submitting. -->

| File | Member(s) | How (wrote / revised / reviewed / tested / GenAI-assisted) |
|---|---|---|
| `src/project/Song.java` | Jiho Oh | Wrote and updated the class that stores song details. |
| `src/project/Node.java` | Jiho Oh | Wrote and updated the node class used by the linked queue. |
| `src/project/SongQueue.java` | Jiho Oh | Wrote and revised the linked queue methods for adding and removing songs. |
| `src/project/AuxLineModel.java` | Jiho Oh, Oscar Chen, Nolan Ayodeji | Jiho worked on the initial model. Oscar added Play Next and access to the waiting list. |
| `src/project/AuxLineController.java` | Jiho Oh, Oscar Chen | Jiho worked on the initial controller. Oscar added methods to handle song requests, advance playback, and retrieve the queue. |
| `src/project/AuxLineView.java` | Jiho Oh, Oscar Chen, Nolan Ayodeji | Jiho revised the Now Playing display. Oscar added and updated menu options for adding songs, playing the next song, and viewing the queue. |
| `src/project/SongQueueTest.java` | Jiho Oh, Enoch Shum | Wrote tests for enqueue, dequeue, and dequeuing from an empty queue. Enoch tested in a local environment and ensured all cases passed. |
| `tests/project/AuxLineAddSongTest.java` | Oscar Chen, Enoch Shum | Wrote checks for adding songs and rejecting invalid song details. |
| `tests/project/AuxLinePlayNextTest.java` | Oscar Chen, Nolan Ayodeji | Wrote checks for advancing through songs in order and handling an empty queue. |
| `tests/project/AuxLineQueueAndExitTest.java` | Oscar Chen, Enoch Shum | Wrote checks for viewing the waiting queue and exiting the console. |

### GenAI note

For Deliverable 3, We used ChatGPT to compare the prototype with the rubric, help draft the README template, and predict which classes Save and Restore would affect. The review included running the console flow and checking where the standalone tests failed. We also used Ai for difficult debugging issues when programming. 
