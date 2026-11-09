import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages movie listings, searches, and Bubble Sort ordering.
 * Uses an ArrayList<Movie> for ordered display and sorting, and a
 * HashMap<Integer, Movie> for O(1) ID lookups and duplicate prevention.
 */
public class MovieManager {
    private ArrayList<Movie> movies;
    private HashMap<Integer, Movie> movieLookup;

    /**
     * Initializes empty MovieManager.
     */
    public MovieManager() {
        this.movies = new ArrayList<>();
        this.movieLookup = new HashMap<>();
    }

    /**
     * Adds a movie to the catalog. Rejects duplicate movie IDs.
     *
     * @param movie Movie object to add
     * @return true if successfully added, false if duplicate ID
     * @throws IllegalArgumentException if movie is null
     */
    public boolean addMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null.");
        }
        if (movieLookup.containsKey(movie.getMovieID())) {
            return false; // Duplicate movie ID
        }

        movies.add(movie);
        movieLookup.put(movie.getMovieID(), movie);
        return true;
    }

    /**
     * Removes a movie from the catalog by its unique ID.
     *
     * @param movieID ID of movie to remove
     * @return true if removed, false if not found
     */
    public boolean removeMovie(int movieID) {
        Movie movie = movieLookup.remove(movieID);
        if (movie != null) {
            movies.remove(movie);
            return true;
        }
        return false;
    }

    /**
     * Fast O(1) lookup using HashMap.
     *
     * @param movieID Movie ID to find
     * @return Movie object or null if not found
     */
    public Movie findMovieByID(int movieID) {
        return movieLookup.get(movieID);
    }

    /**
     * Case-insensitive linear search for movies matching title substring.
     *
     * @param title Title or keyword to search
     * @return List of matching Movie objects
     */
    public ArrayList<Movie> searchMoviesByTitle(String title) {
        ArrayList<Movie> matches = new ArrayList<>();
        if (title == null || title.trim().isEmpty()) {
            return matches;
        }
        String query = title.trim().toLowerCase();
        for (Movie m : movies) {
            if (m.getTitle().toLowerCase().contains(query)) {
                matches.add(m);
            }
        }
        return matches;
    }

    /**
     * Case-insensitive linear search for movies matching genre keyword.
     *
     * @param genre Genre keyword to search
     * @return List of matching Movie objects
     */
    public ArrayList<Movie> searchMoviesByGenre(String genre) {
        ArrayList<Movie> matches = new ArrayList<>();
        if (genre == null || genre.trim().isEmpty()) {
            return matches;
        }
        String query = genre.trim().toLowerCase();
        for (Movie m : movies) {
            if (m.getGenre().toLowerCase().contains(query)) {
                matches.add(m);
            }
        }
        return matches;
    }

    /**
     * Custom Bubble Sort algorithm arranging movies alphabetically by title.
     * Implements early-termination optimization if no swaps occur during a pass.
     * Handles empty lists and single-element lists without error.
     */
    public void bubbleSortMoviesByTitle() {
        int n = movies.size();
        if (n <= 1) {
            return; // Empty or single-element list is already sorted
        }

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                // Alphabetical title comparison using compareToIgnoreCase
                if (movies.get(j).getTitle().compareToIgnoreCase(movies.get(j + 1).getTitle()) > 0) {
                    // Swap adjacent Movie objects
                    Movie temp = movies.get(j);
                    movies.set(j, movies.get(j + 1));
                    movies.set(j + 1, temp);
                    swapped = true;
                }
            }
            // Optimization: if no two elements were swapped, list is sorted
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Nicely formats and displays all movies in tabular layout.
     */
    public void displayMovies() {
        System.out.println("==========================================================================================");
        System.out.println("                             DECEMBER 2026 MOVIE CATALOG                                  ");
        System.out.println("==========================================================================================");
        System.out.printf("%-8s %-32s %-20s %-8s %-14s%n", "ID", "TITLE", "GENRE", "RATING", "RELEASE DATE");
        System.out.println("------------------------------------------------------------------------------------------");
        if (movies.isEmpty()) {
            System.out.println("No movies currently available in the catalog.");
        } else {
            for (Movie m : movies) {
                System.out.printf("[%-4d]  %-32s %-20s %-8s %-14s%n",
                    m.getMovieID(),
                    truncate(m.getTitle(), 30),
                    truncate(m.getGenre(), 18),
                    m.getRating(),
                    m.getReleaseDate());
            }
        }
        System.out.println("==========================================================================================");
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public int getMovieCount() {
        return movies.size();
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 2) + "..";
    }
}
