package model;

public class Movie {
    private int id;
    private String movieTitle;
    private String type;
    private int yearOfProd;
    private String description;

    public Movie() {}

    public Movie(int id, String movieTitle, String type, int yearOfProd, String description) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.type = type;
        this.yearOfProd = yearOfProd;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getYearOfProd() { return yearOfProd; }
    public void setYearOfProd(int yearOfProd) { this.yearOfProd = yearOfProd; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 