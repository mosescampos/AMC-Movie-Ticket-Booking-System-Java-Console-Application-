# AMC Movie Ticket Booking System (Console Edition)
**CST250 – Object-Oriented Programming with Java**  
**Season:** December 2026 Holiday Blockbusters  
**Platform:** Java 17+ / Java 21+ / Java 25 (Standard Java SE)  

---

## 1. Project Overview

The **AMC Movie Ticket Booking System** is an object-oriented Java console application simulating the customer ticketing and employee management operations of AMC Theatres. Built specifically for the December 2026 holiday movie season, the system features upcoming releases such as *Avengers: Doomsday*, *Dune: Part Three*, *Avatar: Fire and Ash*, and *Wicked: Part Two*.

The system provides:
* A **Customer Portal** to browse movies, search by title/genre, sort alphabetically using a custom Bubble Sort, view showtimes, inspect a visual ASCII theater seat map, select seats, choose tiered ticket categories (Adult, Child, Senior), calculate totals polymorphically, and view/cancel reservations.
* An **Employee Portal** to add/update/remove movie listings, schedule screenings, monitor seat occupancy, review master booking ledgers, process a FIFO pending booking queue, and save/load system state.
* An **Automated Diagnostics Suite** executing all 12 CST250 demo validation checks in real time.

---

## 2. CST250 Object-Oriented Concepts Demonstrated

| OOP / Java Concept | Implementation Details | Location in Code |
| :--- | :--- | :--- |
| **Encapsulation** | Private fields, validated constructors, defensive getters/setters, state encapsulation. | `Movie.java`, `Showtime.java`, `Customer.java`, `Booking.java` |
| **Inheritance** | Base abstract class `Ticket` extended by specific ticket types. | `Ticket.java`, `AdultTicket.java`, `ChildTicket.java`, `SeniorTicket.java` |
| **Polymorphism** | Overridden `calculatePrice()` in subclasses; dynamic method dispatch in `Booking.calculateTotal()`. | `AdultTicket.java`, `ChildTicket.java`, `SeniorTicket.java`, `Booking.java` |
| **Custom Bubble Sort** | Written from scratch using `compareToIgnoreCase()` with an early-exit flag `swapped`. | `MovieManager.java` (`bubbleSortMoviesByTitle`) |
| **Linear Search** | Case-insensitive substring searching across movie titles and genres. | `MovieManager.java` (`searchMoviesByTitle`, `searchMoviesByGenre`) |
| **HashSet (Set)** | Tracks occupied seats per screening, guaranteeing $O(1)$ duplicate seat prevention. | `Showtime.java` (`reservedSeats`) |
| **HashMap (Map)** | $O(1)$ movie lookup by ID and fast booking retrieval by confirmation number. | `MovieManager.java` (`movieLookup`), `BookingManager.java` (`completedBookings`) |
| **Queue (FIFO)** | `Queue<Booking>` implemented with `ArrayDeque` for high-demand pending reservations (`offer()`, `peek()`, `poll()`). | `BookingManager.java` (`pendingBookings`) |
| **Exception Handling** | Robust handling of `NumberFormatException`, `IllegalArgumentException`, `IllegalStateException`. | `Main.java`, `BookingManager.java`, `Showtime.java` |
| **File I/O Persistence** | Text-based serialization and deserialization of all system state (Feature F12). | `DataManager.java` |

---

## 3. Class Architecture

```
                    ┌─────────────────────────┐
                    │      Ticket (base)      │
                    │  abstract calculatePrice│
                    └────────────┬────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         ▼                       ▼                       ▼
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│   AdultTicket    │    │   ChildTicket    │    │   SeniorTicket   │
│     ($16.50)     │    │     ($12.00)     │    │     ($13.50)     │
└──────────────────┘    └──────────────────┘    └──────────────────┘

     ┌────────────┐               ┌──────────────┐
     │   Movie    │◄──────────────┤   Showtime   │ (contains HashSet<String>
     └─────▲──────┘               └──────▲───────┘  reservedSeats: A1-E8)
           │                             │
     ┌─────┴──────────────┐       ┌──────┴───────────────┐
     │    MovieManager    │       │    BookingManager    │
     │  ArrayList<Movie>  │       │  ArrayList<Showtime> │
     │  HashMap<ID,Movie> │       │  HashMap<ID,Booking> │
     └────────────────────┘       │  Queue<Booking>      │
                                  └──────────────────────┘
```

---

## 4. Source Files Overview

| File | Purpose |
| :--- | :--- |
| `Movie.java` | Encapsulates movie properties: ID, title, genre, rating, December 2026 release date. |
| `Showtime.java` | Models an individual screening with date, time, 40-seat auditorium layout (A1–E8), ASCII seat map, and `HashSet<String>` reserved seats. |
| `Ticket.java` | Abstract base ticket class establishing the polymorphic `calculatePrice()` contract. |
| `AdultTicket.java` | Subclass implementing adult admission pricing (\$16.50). |
| `ChildTicket.java` | Subclass implementing child admission pricing (\$12.00, ages 2–12). |
| `SeniorTicket.java` | Subclass implementing senior admission pricing (\$13.50, ages 60+). |
| `Booking.java` | Represents a reservation containing customer information, `ArrayList<Ticket>`, total price, and status (`CONFIRMED`, `CANCELLED`, `PENDING`). |
| `Customer.java` | Stores customer identity and personal booking history. |
| `MovieManager.java` | Manages movie listings using `ArrayList` and `HashMap`; executes case-insensitive searches and custom Bubble Sort. |
| `BookingManager.java` | Handles atomic reservations, cancellation seat releases, active showtime integrity guards, and FIFO queue processing. |
| `DataManager.java` | Implements persistent Save and Load file I/O for Feature F12. |
| `Main.java` | Interactive console user interface with input-safe menu loops and sample December 2026 data. |
| `SystemTest.java` | Standalone automated unit & integration test suite verifying 11 requirements and edge cases. |
| `PRD.md` | Formal, detailed Product Requirements Document specification. |

---

## 5. Build and Execution Instructions

### Prerequisites
* Java Development Kit (JDK 17 or higher; tested and compatible with OpenJDK 25).

### 5.1 Compile All Classes
Open a command prompt / PowerShell window in the project directory:
```bash
javac *.java
```

### 5.2 Launch Interactive Console Application
```bash
java Main.Java
```

### 5.3 Run Standalone Automated Unit & Integration Tests
```bash
java SystemTest.Java
```

---

## 6. Live Demonstration Guide

Follow these steps during your live presentation or grading review to showcase all required features:

### Step 1: Menu Input Resilience (FR1)
* At the Main Menu prompt, enter `abc` or symbols.
* **Observe:** The system does not crash; it prints a friendly error message and safely re-prompts.

### Step 2: Browse December 2026 Blockbusters (FR2)
* Select `1` (Customer Portal), then select `1` (Browse All Movies).
* **Observe:** Tabular display of movies with ID, Title, Genre, Rating, and Release Date.

### Step 3: Custom Bubble Sort in Action (FR4)
* Select `4` (Sort Movie Catalog Alphabetically).
* **Observe:** Movies initially listed out of order (*Avengers*, *Dune*, *Avatar*, *Wicked*, *Jumanji*, *The Polar Express*) are sorted into strict alphabetical order.

### Step 4: Search by Title and Genre (FR3)
* Select `2` (Search by Title) and search `"avengers"`. Matches *Avengers: Doomsday*.
* Search for a non-existent title `"Matrix"`. Displays `"No movies found matching your search."`.
* Select `3` (Search by Genre) and search `"Sci-Fi"`. Displays all science-fiction screenings.

### Step 5: Visual Seat Map & Seat Selection (FR5, FR6)
* Select `6` (Book Tickets).
* Choose Movie ID `101` (*Avengers: Doomsday*), then Showtime `201`.
* **Observe:** The visual ASCII seat map shows the 5x8 auditorium. Seats `[A1]` and `[A2]` display as `[ X ]` (reserved from Alice's pre-existing booking).
* Attempt to choose seat `A1` $\rightarrow$ Rejected as already reserved.
* Attempt to choose seat `Z9` $\rightarrow$ Rejected as out-of-bounds seat format.
* Choose available seats `B1` and `B2`.

### Step 6: Ticket Tier Selection & Polymorphism (FR7, FR8)
* Assign Seat `B1` as Adult (\$16.50) and `B2` as Child (\$12.00).
* Review the Booking Summary showing subtotal \$28.50.
* Confirm purchase with `Y`.
* **Observe:** Formatted AMC confirmation receipt generated with confirmation number `AMC-001002`, and seats `B1` and `B2` are now marked `[ X ]` on the updated seat map.

### Step 7: Booking Cancellation & Seat Release (FR9, FR10)
* Select `7` (View My Booking History) to inspect active bookings.
* Select `8` (Cancel a Booking) and enter Booking ID `1001` (Alice's initial booking).
* **Observe:** Booking status transitions to `[CANCELLED]`.
* Re-inspect the seat map for Showtime `201`; seats `A1` and `A2` are immediately released and available again.

### Step 8: Employee Administration & Safeguards (FR11, FR12)
* Return to the Main Menu and enter `2` (Employee Portal).
* Select `1` (Add New Movie) and enter ID `101`. The system rejects duplicate IDs.
* Select `6` (Remove Showtime) and try removing Showtime `201` while an active booking exists $\rightarrow$ Blocked with integrity message.
* Select `9` to demonstrate the FIFO pending queue (`offer`, `peek`, `poll`).
* Select `10` to save system state to `amc_system_data.txt` (Feature F12).

---

## 7. Sample December 2026 Dataset

```
Movies:
  [101] Avengers: Doomsday                   | Action / Sci-Fi     | PG-13  | Dec 18, 2026
  [102] Dune: Part Three                     | Sci-Fi / Adventure  | PG-13  | Dec 18, 2026
  [103] Avatar: Fire and Ash                 | Sci-Fi / Fantasy    | PG-13  | Dec 25, 2026
  [104] Wicked: Part Two                     | Musical / Fantasy   | PG     | Dec 25, 2026
  [105] Jumanji: Next Level 2                | Action / Comedy     | PG-13  | Dec 11, 2026
  [106] The Polar Express (Holiday Special)  | Animation / Family  | G      | Dec 04, 2026

Default Profiles:
  * Customer 1: Alice Johnson (alice.j@example.com) — Has initial booking on Showtime 201 (A1, A2)
  * Customer 2: Bob Smith (bob.smith@example.com)
  * Customer 3: Charlie Davis (charlie.d@example.com)
```

---

## 8. CST250 Requirements Checklist

- [x] **F1:** Main Menu with Customer and Employee routing, input-resilient validation.
- [x] **F2:** December 2026 movie catalog browsing with full metadata.
- [x] **F3:** Case-insensitive search by title and genre with clear no-results messaging.
- [x] **F4:** Custom Bubble Sort algorithm arranging movies alphabetically by title.
- [x] **F5:** Showtime discovery with screening IDs, dates, times, and movie associations.
- [x] **F6:** Visual auditorium seat map with `HashSet` duplicate seat prevention.
- [x] **F7:** Tiered ticket classes (`AdultTicket`, `ChildTicket`, `SeniorTicket`) overriding `calculatePrice()`.
- [x] **F8:** Atomic booking transactions, summary verification, and confirmation receipts.
- [x] **F9:** Customer booking history inspection and cancellation with automatic seat release.
- [x] **F10:** Employee movie inventory management (Add, Update, Remove with integrity checks).
- [x] **F11:** Employee showtime scheduling, seat inspection, and active reservation protection.
- [x] **F12:** Persistent file storage (Save/Load) and FIFO queue processing (`ArrayDeque`).
