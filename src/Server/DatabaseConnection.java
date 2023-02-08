package Server;

import Entidades.*;

import java.sql.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

public class DatabaseConnection {
    String user = "postgres";
    String password = "1409";
    String url = "jdbc:postgresql://localhost:5432/MyDatabase";


    public Hashtable<Long, Movie> getMovies() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to de db  established");
            String query = """
                    Select\s
                    movies.user_id,
                    movies.id,
                    movies.name,
                    movies.creation_date,
                    movies.oscars_count,
                    movies.total_box_office,
                    movies.budget,
                    movies.mpaa_rating,
                    coordinate.x,
                    coordinate.y,
                    person.name person_name,
                    person.height person_height,
                    person.passport_id person_passport,
                    person.eye_color person_eye,
                    location.x location_x,
                    location.y location_y,
                    location.z location_z,
                    location.name location_name
                    from movies\s
                    left join coordinates on movie.id = coordinates.movie_id\s
                    left join persons on movie.id = persons.movie_id\s
                    left join location on persons.movie_id = location.person_id""";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                Hashtable<Long, Movie> movieHashtable = new Hashtable<>();
                while (rs.next()) {
                    Integer userId = rs.getInt("user_id");
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String creationDate = rs.getString("creation_date");
                    Long oscarsCount = rs.getLong("oscars_count");
                    Integer budget = rs.getInt("budget");
                    Integer totalBoxOffice = rs.getInt("total_box_office");
                    String mpaaRating = rs.getString("mpaa_rating");
                    Double x = rs.getDouble("x");
                    Float y = rs.getFloat("y");
                    String personName = rs.getString("person_name");
                    Long height = rs.getLong("person_height");
                    String passportID = rs.getString("person_passport");
                    String eyeColor = rs.getString("person_eye");
                    Long xLocation = rs.getLong("location_x");
                    Float yLocation = rs.getFloat("location_y");
                    Float zLocation = rs.getFloat("location_z");
                    String nameLocation = rs.getString("location_name");

                    Movie movie = new Movie();
                    movie.setUserId(userId);
                    movie.setId(id);
                    movie.setName(name);
                    movie.setCreationDate(Date.valueOf(creationDate));
                    movie.setOscarsCount(oscarsCount);
                    movie.setBudget(budget);
                    movie.setTotalBoxOffice(totalBoxOffice);
                    movie.setMpaaRating(MpaaRating.valueOf(mpaaRating));
                    movie.setCoordinates(new Coordinates(x, y));
                    movie.setOperator(new Person(personName, height, passportID,Color.valueOf(eyeColor) , new Location(xLocation, yLocation, zLocation, nameLocation)));
                    movieHashtable.put(id, movie);
                }
                return movieHashtable;
            } catch (SQLException e) {
                throw new Error("Problem", e);
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public boolean IsValidUser(User userToValidate) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to de db  established");
            Statement stmt;
            String query = "Select password from public.users where name = '"
                    + userToValidate.getName() + "';";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return false;

            byte[] dataToValidate = rs.getBytes("password");
            return Arrays.equals(dataToValidate, new EncriptionHelper().encrypt(userToValidate.getPassword()));
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void CreateAdmin() {
        System.out.println("Write the name of the User:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        System.out.println("Write a password for the User:");
        String user_password = scanner.nextLine();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            byte[] encryptedData = new EncriptionHelper().encrypt(user_password);
            String query = "Insert into public.users (name, password) values (?, ?);";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1,name);
            pst.setBytes(2, encryptedData);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public Hashtable<Long, Movie> saveMovies(Hashtable<Long, Movie> movieHashtable, Integer userId) {
        // I delete all dragons that doesn't belong to the  current user
        movieHashtable.entrySet().removeIf(entry -> !entry.getValue().getUserId().equals(userId));

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to de db  established");
            String query = "Delete from public.movies where user_id = " + userId;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(query);
                Statement stmtInsert = conn.createStatement();
                movieHashtable.forEach((k, v) -> {
                    String insert = v.toInsert();
                    try {
                        stmtInsert.execute(insert);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                return this.getMovies();

            } catch (SQLException e) {
                throw new Error("Problem", e);
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}