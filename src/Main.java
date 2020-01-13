import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public static void main(String[] args) {
		createNewTable(); //Creating the table within the database file
		System.out.println("Before adding student:");
		displayAll(); //Displaying all the data within the table in a formatted fashion
		System.out.println(); //Break line between table outputs
		addStudent("Jeremiah J.", 16, 11, "Del Norte"); //Adds a student to the database - modify the values to add yourself!
		System.out.println("Student added:");
		displayAll(); 
		System.out.println();
		deleteStudent("Jeremiah J."); // Deleting the student from the database based off of their name
		System.out.println("After deleting student:");
		displayAll();
		System.out.println();
	}
	
	//Creates a table within the database if it doesn't already exist
	public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:Database.db"; //Setting up url which leads to the database file
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS students (\n"
        		+ "name text NOT NULL, \n"
        		+ "age integer NOT NULL, \n"
        		+ "grade integer NOT NULL, \n"
        		+ "school text NOT NULL, \n"
        		+ "id integer PRIMARY KEY AUTOINCREMENT\n"
        		+ ");";
        
        //Attempts to connect to the file you listed in the url, then creates a statement to prepare to execute the sql string
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	//Connects to the database
	public static Connection connect() {
		String url = "jdbc:sqlite:Database.db"; //url tells the driver manager where to locate the Database.db file
		Connection conn = null;
		
		//Attempts to connect to the database file
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn; //returns a connection object
	}
	
	//Displays all of the information that is stored in the table 
	public static void displayAll(){
		//Selecting the columns you'd like to get information from
        String sql = "SELECT Name, Age, Grade, School FROM students";
        
        //Connecting to the database then preparing the objects to receive the info
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set, outputting the information row by row
            while (rs.next()) {
                System.out.println(rs.getString("Name") +  "\t" + 
                                   rs.getInt("Age") + "\t" +
                                   rs.getInt("Grade") + "\t" +
                                   rs.getString("School"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	//Add a student to the students table within the database
	public static void addStudent(String name, int age, int grade, String school) {
		try {
			Connection conn = connect();
			//specifying the column names allows you to insert data into not all the columns through the prepared statement
			PreparedStatement prep = conn.prepareStatement("INSERT INTO students (name, age, grade, school) values(?, ?, ?, ?);");
			prep.setString(1, name); //load the name into the statement
			prep.setInt(2, age); //load the age into the statement
			prep.setInt(3, grade); //load the grade into the statement
			prep.setString(4, school); //load the school name into the statement
			prep.execute(); //write the information to the table
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Delete a student from the students table within the database file
	public static void deleteStudent(String name) {
        String sql = "DELETE FROM students WHERE name = ?"; //Deleting a student from the table based off of their name
 
        //try to connect to the database
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setString(1, name);
            // execute the delete statement
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
