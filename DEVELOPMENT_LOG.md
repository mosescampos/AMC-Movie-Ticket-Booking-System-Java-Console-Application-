# Development Log: AMC Movie Ticket Booking System
**Course:** CST250 – Object-Oriented Programming with Java  
**Project:** AMC Movie Ticket Booking System (Console & Web Edition)  
**Season:** December 2026 Holiday Blockbusters  
**Current Version:** 2.1.0  
**Status:** Complete, Verified, and CI/CD Enabled  

---

## 1. Project Overview & Objectives

The goal of this project was to design, implement, test, and document a robust, object-oriented movie ticketing and theater management platform simulating AMC Theatres operations during the December 2026 holiday blockbuster season (*Avengers: Doomsday*, *Dune: Part Three*, *Avatar: Fire and Ash*, *Wicked: Part Two*).

### Key Architectural Goals
1. **Rigorous OOP Principles:** Encapsulation, inheritance hierarchies, polymorphic dynamic dispatch, and interface/abstract contracts.
2. **Intentional Data Structures:** Justified utilization of `ArrayList`, `HashMap`, `HashSet`, and `ArrayDeque` (FIFO Queue).
3. **Algorithmic Craftsmanship:** Scratch implementation of in-place Bubble Sort with early-exit optimization and linear multi-field substring search.
4. **Defensive Programming & Resilience:** Comprehensive input validation, boundary checking, and custom exception handling preventing crashes on malformed user input.
5. **Multi-Interface Usability:** Fully featured interactive Java console CLI paired with an Obsidian Cinema web interface (`stitch_ui`).

---

## 2. Chronological Engineering Milestones

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                             DEVELOPMENT TIMELINE                            │
│                                                                             │
│  Milestone 1: Domain Entities & Polymorphic Hierarchy                       │
│  ├── Ticket (abstract), AdultTicket, ChildTicket, SeniorTicket              │
│  └── Movie, Showtime, Customer, Booking                                     │
│                                                                             │
│  Milestone 2: Manager Layer & Data Structures                               │
│  ├── MovieManager: ArrayList + HashMap dual indexing                        │
│  ├── Custom Bubble Sort & Linear Search algorithms                          │
│  ├── Showtime: HashSet seat tracking (A1–E8) & ASCII map                    │
│  └── BookingManager: Atomic reservations & ArrayDeque FIFO queue            │
│                                                                             │
│  Milestone 3: Console CLI Experience (Main.java)                            │
│  ├── Robust input loops (resilient to NumberFormatException)                │
│  ├── Customer Portal & Employee Admin Portal workflows                      │
│  └── Rich ASCII receipts & ANSI formatting                                  │
│                                                                             │
│  Milestone 4: Persistence & Automated Test Harness                          │
│  ├── DataManager: State serialization & deserialization                     │
│  └── SystemTest: 11-step automated verification suite                       │
│                                                                             │
│  Milestone 5: Interactive Web UI (stitch_ui)                                │
│  ├── Modern Obsidian Cinema aesthetic (Tailwind + CSS)                      │
│  └── Responsive seat picker, passes wallet, and admin console               │
│                                                                             │
│  Milestone 6: Cloud & CI/CD Tooling                                         │
│  ├── .gitignore for Java development artifacts                              │
│  └── GitHub Actions CI pipeline (.github/workflows/run-tests.yml)           │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### Milestone 1: Domain Modeling & Object-Oriented Hierarchy
* **Abstract Ticket Base (`Ticket.java`):**
  * Established the abstract contract `calculatePrice()` ensuring all subclasses enforce business-specific pricing rules.
  * Encapsulated `ticketID`, `seatNumber`, `ticketPrice`, `ticketType`, and `showtime`.
* **Polymorphic Specializations (`AdultTicket.java`, `ChildTicket.java`, `SeniorTicket.java`):**
  * `AdultTicket`: Base rate \$16.50.
  * `ChildTicket`: Discounted rate \$12.00 (ages 2–12).
  * `SeniorTicket`: Discounted rate \$13.50 (ages 60+).
  * Eliminated brittle `switch`/`if-else` pricing logic across the codebase; `Booking.calculateTotal()` delegates directly to each ticket's overridden method.
* **Core Entities (`Movie.java`, `Showtime.java`, `Customer.java`, `Booking.java`):**
  * Implemented defensive getters, setters, and parameter validation.
  * Built formatted string summaries (`getMovieDetails()`, `getShowtimeDetails()`).

---

### Milestone 2: Data Structure Selection & Algorithms
* **Dual-Indexing in `MovieManager.java`:**
  * **`ArrayList<Movie>`:** Preserves insertion order, enables random index access, and provides the contiguous backing store required for in-place sorting.
  * **`HashMap<Integer, Movie>`:** Provides $O(1)$ constant-time lookup by `movieID` and rejects duplicate additions.
* **Custom Bubble Sort Implementation:**
  * Implemented `bubbleSortMoviesByTitle()` using title comparison (`compareToIgnoreCase`).
  * Incorporated a boolean `swapped` flag for $O(n)$ best-case runtime when the catalog is already sorted.
* **Auditorium Modeling & Collision Avoidance in `Showtime.java`:**
  * Modeled a 40-seat auditorium (Rows A through E, Columns 1 through 8).
  * Regex validation `^[A-E][1-8]$` prevents invalid seat entries (e.g., `Z9`, `A0`).
  * Utilized `HashSet<String> reservedSeats` for $O(1)$ duplicate seat reservation prevention.
  * Built dynamic ASCII seat map rendering `[   ]` for available and `[ X ]` for reserved seats with row/column headers.
* **Atomic Transactions & FIFO Queue in `BookingManager.java`:**
  * Implemented atomic seat reservation: validates that *all* requested seats are available before committing any seat reservation. If any seat is taken, the transaction rolls back cleanly.
  * Integrated `Queue<Booking> pendingBookings` backed by `ArrayDeque` demonstrating standard queue operations (`offer()`, `peek()`, `poll()`).

---

### Milestone 3: User Interface & Console Resilience (`Main.java`)
* **Crash-Resistant Input Processing:**
  * Wrapped all numeric prompts in `try-catch (NumberFormatException)` and `while` loops.
  * User entering strings, symbols, or out-of-range numbers is safely prompted to retry without terminating the session.
* **Customer Workflow:**
  * Browse December 2026 releases $\rightarrow$ Sort alphabetically $\rightarrow$ Search by title/genre $\rightarrow$ Select screening $\rightarrow$ Visual seat selection $\rightarrow$ Polymorphic tier selection $\rightarrow$ Formatted receipt generation.
* **Employee Workflow:**
  * Movie inventory CRUD (Add, Update, Remove with integrity checks).
  * Showtime scheduling with active booking protection (prevents deletion of screenings that contain confirmed reservations).
  * FIFO pending reservation queue management.

---

### Milestone 4: Persistence & Standalone Verification
* **Text File Persistence (`DataManager.java`):**
  * Engineered delimited text serialization for `Movie`, `Showtime`, `Customer`, `Booking`, and `reservedSeats`.
  * Allows saving complete system state to disk (`amc_system_data.txt`) and deserializing back on startup.
* **Automated Unit & Integration Test Suite (`SystemTest.java`):**
  * Created standalone automated test runner with 11 critical test assertions:
    * Test 1: Bubble sort boundary handling (empty and single list).
    * Test 2: Bubble sort alphabetical ordering accuracy.
    * Test 3: Title and genre case-insensitive substring search.
    * Test 4: Duplicate movie ID rejection via `HashMap`.
    * Test 5: Seat boundaries and regex format validation.
    * Test 6: Occupied seat reservation prevention via `HashSet`.
    * Test 7: Polymorphic pricing dynamic dispatch.
    * Test 8: Atomic booking creation and total calculation.
    * Test 9: Cancellation seat release and state transition.
    * Test 10: `ArrayDeque` FIFO queue operations (`offer`, `peek`, `poll`).
    * Test 11: Active reservation deletion guard in `BookingManager`.

---

### Milestone 5: Web UI Modernization (`stitch_ui`)
* **Interactive Web Application (`stitch_ui/index.html`):**
  * Built using Tailwind CSS, Google Material Symbols, and Plus Jakarta Sans.
  * Interactive 40-seat visual seat picker with real-time seat status toggling.
  * Dynamic pricing calculator reflecting Adult, Child, and Senior rates.
  * "My Passes" wallet with realistic barcode rendering and 1-click booking cancellation.
  * Admin / Staff dashboard with FIFO queue inspection, occupancy heatmaps, and test suite runner.

---

### Milestone 6: CI/CD & GitHub Integration
* **Repository Sanitation (`.gitignore`):**
  * Excluded Java bytecode (`*.class`), log files, archives, and runtime persistence artifacts.
* **GitHub Actions Workflow (`.github/workflows/run-tests.yml`):**
  * Automates checkout, JDK 21 setup, compilation (`javac *.java`), and test execution (`java SystemTest`) on every push and pull request.
* **Cloud Execution Support:**
  * Fully documented execution pathways for **GitHub Codespaces** (in-browser interactive terminal) and **GitHub Pages** (web UI hosting).

---

## 3. Engineering Decisions & Architectural Trade-offs

| Decision | Alternative Considered | Rationale & Outcome |
| :--- | :--- | :--- |
| **`HashSet<String>` for Seats** | 2D `boolean[][]` array | `HashSet` provides immediate $O(1)$ lookup and dynamic seat codes (`"A1"`) directly matching user input without manual index offset math (`row - 'A'`). |
| **Dual Collections in `MovieManager`** | Single `ArrayList` or single `HashMap` | `ArrayList` allows in-place bubble sorting and indexed browsing; `HashMap` guarantees $O(1)$ uniqueness and instant ID lookups. Best of both worlds. |
| **Custom Bubble Sort** | `Collections.sort()` | Built from scratch with an early-exit `swapped` flag to fulfill CST250 algorithmic requirements and demonstrate sorting mechanics. |
| **Polymorphic Dynamic Dispatch** | `switch(ticketType)` statement | Follows the Open-Closed Principle (OCP). New ticket categories (e.g., `MatineeTicket`, `MilitaryTicket`) can be added without modifying existing calculation code. |
| **Atomic Multi-Seat Rollback** | Immediate reservation on input | Prevents partial or corrupted bookings if a user enters two valid seats followed by one already-reserved seat. |

---

## 4. Test Matrix & Verification Status

```
═══════════════════════════════════════════════════════════════════════
  AMC THEATRES SYSTEM INTEGRATION & VERIFICATION REPORT
═══════════════════════════════════════════════════════════════════════
  [PASS] Test 1:  Bubble Sort on empty & single-element collections
  [PASS] Test 2:  Alphabetical sorting accuracy on multi-element catalog
  [PASS] Test 3:  Case-insensitive substring search (Title & Genre)
  [PASS] Test 4:  Duplicate Movie ID rejection via HashMap lookup
  [PASS] Test 5:  Auditorium seat format & boundary validation (A1-E8)
  [PASS] Test 6:  Duplicate seat reservation blocked via HashSet
  [PASS] Test 7:  Polymorphic pricing dispatch ($16.50 / $12.00 / $13.50)
  [PASS] Test 8:  Atomic multi-seat booking transaction & subtotaling
  [PASS] Test 9:  Booking cancellation and automatic seat release
  [PASS] Test 10: ArrayDeque FIFO queue operations (offer, peek, poll)
  [PASS] Test 11: Showtime deletion blocked when active bookings exist
═══════════════════════════════════════════════════════════════════════
  OVERALL STATUS: 11 / 11 PASSED (100% SUCCESS RATE)
═══════════════════════════════════════════════════════════════════════
```

---

## 5. File Inventory

| Path | Category | Description |
| :--- | :--- | :--- |
| [`Main.java`](file:///c:/Users/2nice/Downloads/java/Main.java) | Application | Main entry point; interactive console loop with role routing. |
| [`Movie.java`](file:///c:/Users/2nice/Downloads/java/Movie.java) | Entity | Encapsulates title, genre, rating, and release date. |
| [`Showtime.java`](file:///c:/Users/2nice/Downloads/java/Showtime.java) | Entity | Models screening schedule, seat map, and `HashSet` reservations. |
| [`Ticket.java`](file:///c:/Users/2nice/Downloads/java/Ticket.java) | Abstract Base | Contract defining polymorphic `calculatePrice()`. |
| [`AdultTicket.java`](file:///c:/Users/2nice/Downloads/java/AdultTicket.java) | Subclass | Standard adult admission ticket (\$16.50). |
| [`ChildTicket.java`](file:///c:/Users/2nice/Downloads/java/ChildTicket.java) | Subclass | Discounted child admission ticket (\$12.00). |
| [`SeniorTicket.java`](file:///c:/Users/2nice/Downloads/java/SeniorTicket.java) | Subclass | Discounted senior admission ticket (\$13.50). |
| [`Booking.java`](file:///c:/Users/2nice/Downloads/java/Booking.java) | Entity | Customer transaction record with heterogeneous `Ticket` list. |
| [`Customer.java`](file:///c:/Users/2nice/Downloads/java/Customer.java) | Entity | Customer account tracking personal booking history. |
| [`MovieManager.java`](file:///c:/Users/2nice/Downloads/java/MovieManager.java) | Controller | Catalog management, linear search, and custom Bubble Sort. |
| [`BookingManager.java`](file:///c:/Users/2nice/Downloads/java/BookingManager.java) | Controller | Transaction coordination, active safeguards, and FIFO queue. |
| [`DataManager.java`](file:///c:/Users/2nice/Downloads/java/DataManager.java) | Persistence | File I/O serialization and deserialization engine. |
| [`SystemTest.java`](file:///c:/Users/2nice/Downloads/java/SystemTest.java) | Quality Assurance | Automated test harness executing 11 integration tests. |
| [`stitch_ui/index.html`](file:///c:/Users/2nice/Downloads/java/stitch_ui/index.html) | Web Frontend | Standalone interactive HTML5/Tailwind cinema web application. |
| [`.github/workflows/run-tests.yml`](file:///c:/Users/2nice/Downloads/java/.github/workflows/run-tests.yml) | DevOps | GitHub Actions CI workflow for cloud compilation and testing. |
| [`.gitignore`](file:///c:/Users/2nice/Downloads/java/.gitignore) | Tooling | Git exclusion configuration for build artifacts. |
| [`PRD.md`](file:///c:/Users/2nice/Downloads/java/PRD.md) | Documentation | Product Requirements Document and specifications. |
| [`README.md`](file:///c:/Users/2nice/Downloads/java/README.md) | Documentation | Comprehensive user guide, architecture, and demo script. |
| [`DEVELOPMENT_LOG.md`](file:///c:/Users/2nice/Downloads/java/DEVELOPMENT_LOG.md) | Documentation | Engineering chronology, decisions, and testing log. |

---

## 6. Future Expansion Roadmap

1. **Database Integration:** Transition from flat text file persistence (`DataManager.java`) to SQLite or PostgreSQL using JDBC and connection pooling.
2. **REST API Gateway:** Wrap `MovieManager` and `BookingManager` in Spring Boot or lightweight HTTP microservices to connect the web UI directly to the Java runtime.
3. **Loyalty & Rewards Program:** Add an `AMCEstubsTier` interface with tiered point accumulation and discount perks for registered customers.
4. **Enhanced Concessions Module:** Model popcorn, drinks, and merchandise combos using the Decorator or Composite design pattern.
