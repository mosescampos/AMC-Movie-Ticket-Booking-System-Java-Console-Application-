import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an individual movie available in the theater for December 2026.
 * Demonstrates encapsulation with private fields and public methods.
 */
public class Movie {
    private int movieID;
    private String title;
    private String genre;
    private String rating;
    private LocalDate releaseDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    /**
     * Constructs a Movie with full validation.
     *
     * @param movieID     Unique positive identifier for the movie
     * @param title       Movie title (cannot be blank)
     * @param genre       Movie genre (cannot be blank)
     * @param rating      Motion picture rating (e.g., PG, PG-13, R)
     * @param releaseDate Release date in December 2026
     * @throws IllegalArgumentException if any field is invalid
     */
    public Movie(int movieID, String title, String genre, String rating, LocalDate releaseDate) {
        if (movieID <= 0) {
            throw new IllegalArgumentException("Movie ID must be a positive integer.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or blank.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie genre cannot be null or blank.");
        }
        if (rating == null || rating.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie rating cannot be null or blank.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be null.");
        }

        this.movieID = movieID;
        this.title = title.trim();
        this.genre = genre.trim();
        this.rating = rating.trim().toUpperCase();
        this.releaseDate = releaseDate;
    }

    // Getters
    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * Updates movie details with validation.
     */
    public void updateMovieDetails(String title, String genre, String rating, LocalDate releaseDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or blank.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie genre cannot be null or blank.");
        }
        if (rating == null || rating.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie rating cannot be null or blank.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be null.");
        }

        this.title = title.trim();
        this.genre = genre.trim();
        this.rating = rating.trim().toUpperCase();
        this.releaseDate = releaseDate;
    }

    /**
     * Formats complete movie details for display.
     */
    public String getMovieDetails() {
        return String.format(
            "Movie ID     : %d%n" +
            "Title        : %s%n" +
            "Genre        : %s%n" +
            "Rating       : %s%n" +
            "Release Date : %s",
            movieID, title, genre, rating, releaseDate.format(DATE_FORMATTER)
        );
    }

    @Override
    public String toString() {
        return String.format("[ID: %-3d] %-30s | %-16s | %-6s | Release: %s",
            movieID, title, genre, rating, releaseDate.format(DATE_FORMATTER));
    }
}
