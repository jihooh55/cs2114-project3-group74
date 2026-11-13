# AuxLine

**CS 2114 • Project 3**
Nolan Ayodeji · Oscar Chen · Jiho Oh · Enoch Shum

AuxLine is a shared music-request app for students hanging out or hosting small gatherings. Friends enter song requests on one computer, see what's coming up, and the host advances the lineup with **Play Next**. This version shows the current song but does not play audio or connect to Spotify.

Song requests wait in a custom singly-linked FIFO queue (`SongQueue<E>`) with head and tail references, so enqueue and dequeue are both O(1) and nobody can jump ahead of an earlier request.

---

## Running the program

> **TODO:** confirm the main class name and build steps.

```bash
# compile
javac -d bin src/*.java

# run the CLI
java -cp bin AuxLineMain

# run the tests (JUnit)
# e.g. from Eclipse: right-click the test folder → Run As → JUnit Test
```

### Menu (CLI)

> **TODO:** match these to the actual menu text in `AuxLineView.displayMenu()`.

```
1) Add song
2) Play next
3) Show queue
4) Clear queue
5) Quit
```

---

## Architecture (MVC)

| Class | Role | Responsibility |
|---|---|---|
| `Song` | Model | Immutable title, artist, duration (seconds), genre. Validates fields on construction. |
| `Node<E>` | Model | Generic singly-linked node: `data` + `next`. |
| `SongQueue<E>` | Model | Bounded FIFO linked queue (capacity 100): `enqueue`, `dequeue`, `peek`, `isEmpty`, `size`, `clear`. |
| `AuxLineModel` | Model | Holds the queue and current song; fires `PropertyChangeEvent`s (`"queueState"`, `"nowPlaying"`). |
| `AuxLineController` | Controller | Parses raw CLI input, validates it, calls the model, listens for model events and tells the view to redraw. |
| `AuxLineView` | View | Prints the menu, now-playing song, queue list and errors; reads input with `Scanner`. |

### Model / view boundary

- The view only knows `Song`'s getters and plain `List<Song>` snapshots. It never sees `SongQueue`, `Node`, or head/tail pointers.
- The model has no `System.out` or `Scanner` calls, so it can be tested headless.
- Change notification uses `java.beans.PropertyChangeListener`: the model fires an event, the controller (the registered listener) pulls a snapshot and asks the view to re-print.

### Where validation lives

| Bad input | Caught by | Result |
|---|---|---|
| Blank or >100-char title/artist/genre | `handleAddSongCommand` (backstop: `Song` constructor) | Error shown, nothing added |
| Duration not a whole number, or ≤ 0 | `handleAddSongCommand` (backstop: `Song` constructor) | Error shown, nothing added |
| Queue already holds 100 songs | `SongQueue.enqueue` throws `IllegalStateException`; controller catches it | Error shown, queue unchanged |
| `null` song passed to the model | `AuxLineModel.addSongToQueue`, `SongQueue.enqueue` | `IllegalArgumentException` |
| Play Next on an empty queue | `AuxLineModel.advanceQueue` | Current song kept, "Queue is empty: retaining current song" |
| `dequeue()` / `peek()` on an empty queue | `SongQueue` | `NoSuchElementException` |
| Unrecognized menu choice / input ends | `AuxLineView` | Error or placeholder, loop continues / exits cleanly |

---

## Reflection

> **TODO:** each answer below starts from what the spec said. Replace the bracketed parts with what you actually built.

### What changed from the specification?

- **Classes:** The spec had six classes (`Song`, `Node<E>`, `SongQueue<E>`, `AuxLineModel`, `AuxLineController`, `AuxLineView`) plus a main class. [Classes added, removed, merged, or renamed, and why.]
- **Method signatures:** [Any signature that changed. For example, did `handleAddSongCommand` still take four `String`s and return `boolean`? Did `advanceQueue()` still return the previous song?]
- **Where validation lives:** The spec put user-input checks in the controller, with `Song` and `SongQueue` throwing exceptions as a backstop. [Did that hold, or did some checks move?]
- **Test cases:** The spec planned normal and bad-input tests for `enqueue`, `dequeue`, `peek`, `isEmpty`, `size`, `addSongToQueue`, and `advanceQueue`. [Which were written as planned, which changed, which were added.]
- **Scope:** The Deliverable 1 MVP listed playback controls (Play/Pause, Skip, Seek, Like). [Say whether these were dropped, stubbed, or built, and why.]

### Did the model really stand alone?

[Say whether the model tests run with no controller or view. Name anything you had to move across the model/view line to get it running, such as formatting strings, error messages, or a `Scanner`. Also say whether anything in the model still knows about the view.]

### What happened to our unknowns?

| Unknown (from Scope) | Status | Notes |
|---|---|---|
| How do we refresh the interface after queue changes without putting queue logic in the view? | [Solved / Open] | Planned answer: `PropertyChangeListener`. [Did it work as planned?] |
| How do we test transitions between empty, one-request, and multiple-request queues? | [Solved / Open] | [For example, the single-element dequeue that must reset `tail` to `null`.] |
| For save/restore, how do we keep request order and handle a missing or damaged file? | [Solved / Open] | Only matters if save/restore is attempted. |

### What doesn't work yet?

- [MVP features that are missing or partial]
- [Bad-input cases that aren't handled yet]
- [Tests that are missing or failing]

---

## Next Steps (Deliverable 4)

> **TODO:** confirm the order, the change feature, and who owns each item.

### What's left to finish the MVP (in order)

1. [Fix or finish …]
2. [Controller/view tests for the add-song bad-input paths]
3. [Polish the CLI output: queue list formatting, now-playing block]

### Change feature

**Feature:** [Pick one. Two candidates that fit the design: **Save and restore the session** (a stretch goal) or **Skip/requeue the current song**.]

**Predicted impact (to compare against what actually happens):**

| Class | Expected to change? | Why |
|---|---|---|
| `Song` | [No] | |
| `Node<E>` | [No] | |
| `SongQueue<E>` | [?] | |
| `AuxLineModel` | [Yes] | New state or logic lives here |
| `AuxLineController` | [Yes] | New command handler |
| `AuxLineView` | [Yes, small] | New menu option or output only |

### Stretch goals

| Stretch goal (from Scope) | Decision |
|---|---|
| Save and restore the current song and waiting requests | [Attempt / Drop] |
| Session history with request counts per person and repeat-song counts | [Attempt / Drop] |
| Rating system after each song | [Attempt / Drop] |
| Multi-user shared queue over a server | [Attempt / Drop] |

### New tests needed

- [Normal and bad-input tests for each new model method]
- [Save/restore: missing file, damaged file, order preserved (if attempted)]
- [Controller: new command with valid and invalid input]

### Who does what

| Item | Owner |
|---|---|
| [Item] | [Name] |
| [Item] | [Name] |
| [Item] | [Name] |
| [Item] | [Name] |

---

## Attribution

> **TODO:** these rows come from the planned division of work. Update them to match who actually wrote, revised, reviewed, or tested each file. Every member must contribute substantively to at least one class or test file.

| File | Member(s) | How (wrote / revised / reviewed / tested / GenAI-assisted) |
|---|---|---|
| `Song.java` | Oscar Chen, Enoch Shum | [ ] |
| `Node.java` | Oscar Chen, Enoch Shum | [ ] |
| `SongQueue.java` | Oscar Chen, Enoch Shum | [ ] |
| `AuxLineModel.java` | Oscar Chen, Enoch Shum | [ ] |
| `AuxLineController.java` | Jiho Oh | [ ] |
| `AuxLineView.java` | Nolan Ayodeji | [ ] |
| `AuxLineMain.java` | Nolan Ayodeji | [ ] |
| `SongTest.java` | [ ] | [ ] |
| `SongQueueTest.java` | Oscar Chen, Enoch Shum | [ ] |
| `AuxLineModelTest.java` | Oscar Chen, Enoch Shum | [ ] |
| `AuxLineControllerTest.java` | [ ] | [ ] |

### GenAI note

> **TODO:** describe how GenAI was used for *this* deliverable's code and tests, or say it wasn't used.

In earlier deliverables, the team used GenAI (ChatGPT) to brainstorm project ideas, edit wording, and review MVC boundaries. For example, one prompt asked how to keep the terminal view from knowing anything about the queue's data structure. For this deliverable: [for example, which files, what was asked, and how the output was checked or rewritten].
