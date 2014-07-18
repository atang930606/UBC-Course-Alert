import java.util.*;
import java.net.*;
import java.io.*;

/* brandon's imports */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
import java.io.IOException;

public class Cycler{
	final String NUM_ARGS = 5;

	// read in a file line by line, [dept course section regular restricted] split by spaces
	// send url request and get html based on timer for array of courses
	// parse html to find seat numbers
	// call static email function 

	static class Course{
		String dept;
		String course;
		String section;
		String email;
		/* Whether or not the client wants to check restricted seats */
		boolean restricted;


		public Course(String dept, String course, String section, boolean restricted, String email){
			this.dept = dept;
			this.course = course;
			this.section = section;
			this.restricted = restricted;
		}

		public String toString(){
			System.out.println(dept + " " + course + " " + section + "\nCheck Restricted: "
				 + restricted + "\nEmail To: " + email);
		}
	}

	/**
	 * Read in from courses text file line by line and return an arraylist of courses
	 * @param filename name of textfile to read
	 */
	public ArrayList<Course> readCourses(String filename){
		ArrayList<Course> result = new ArrayList<Course>();
		try{
			// Create InputStreamReader
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while((line = br.readLine()) != null){
				String[] tokens = line.split("\\s");
				if(tokens.length != NUM_ARGS){
					throw new Exception("Arguments wrong in courses.txt");
				}
				Course c = new Course(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);

			}

		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}finally{
			if(fr != null){
				fr.close();
			}
		}

	}

	/** 
	 * Given the html to the page, return true if available seat exists
	 * Check course.restricted to see if you need to also check for restricted.
	 */
	public static boolean hasSeat(Course c, String url){

		try{
			Document doc = Jsoup.connect(url).get();
			Elements elements = doc.getElementsByClass("table-nonfluid");
			Element table = elements.get(1);	
			String generalSeats = table.child(1).child(2).child(1).child(0).ownText();
			String restrictedSeats = table.child(1).child(3).child(1).child(0).ownText();
			
			if(Integer.parseInt(generalSeats) > 0){
				return true;
			}
			else if(c.restricted && Integer.parseInt(restrictedSeats) > 0){
				return true;
			}
			return false;


		}
		catch(IOException e){
			e.printStackTrace();
		}

		return false;

	}

	public static void main(String[] args) {
		
	}


}