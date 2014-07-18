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
	static final int NUM_ARGS = 6;
	static ArrayList<Course> courses;

	// read in a file line by line, [dept course section regular restricted] split by spaces
	// send url request and get html based on timer for array of courses
	// parse html to find seat numbers
	// call static email function 

	/**
	 * Course structure to contain course information.
	 */
	static class Course{
		String dept;
		String course;
		String section;
		String email;
		/* Whether or not the client wants to check restricted seats */
		boolean restricted;
		boolean ubcv;


		public Course(String dept, String course, String section, boolean restricted, boolean ubcv, String email){
			this.dept = dept;
			this.course = course;
			this.section = section;
			this.restricted = restricted;
			this.ubcv = ubcv;
			this.email = email;
		}

		public String toString(){
			return ("DEPT: " + dept + "\nCOURSE: " + course + "\nSECTION: " + section + "\nCheck Restricted: "
				 + restricted + "\nSCHOOL: " + ((ubcv) ? "UBCV" : "UBCO") +"\nEmail To: " + email);
		}
	}

	/**
	 * Read in from courses text file line by line and return an arraylist of courses
	 * @param filename name of textfile to read
	 */
	public static ArrayList<Course> readCourses(String filename){
		ArrayList<Course> result = new ArrayList<Course>();
		FileReader fr = null;
		try{
			// Create InputStreamReader and Buffered Reader to read lines of file.
			fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while((line = br.readLine()) != null){
				String[] tokens = line.split("\\s");
				if(tokens.length != NUM_ARGS){
					throw new Exception("Arguments wrong in courses.txt");
				}
				Course c = new Course(tokens[0], tokens[1], tokens[2], Boolean.valueOf(tokens[3]), Boolean.valueOf(tokens[4]), tokens[5]);
				result.add(c);
			}

			fr.close();
		}catch(Exception e){
			System.out.println("ERROR: " + e);
		}
		return result;
	}

	/**
	 * Given an ArrayList of courses, check if seats exist.
	 */
	public static void checkSeats(ArrayList<Course> courses){
		try{
			Iterator itr = courses.iterator();
			while(itr.hasNext()){
				Course c = (Course) itr.next();
				/* Form the url */
				String url = "https://courses.students.ubc.ca/cs/main?pname=subjarea&tname=subjareas" + 
					"&req=5" + 
					"&dept=" + c.dept +
					"&course=" + c.course +
					"&section=" + c.section +
					"&campuscd=" + ((c.ubcv) ? "UBC" : "UBCO");
				/* Check if course has opening */
				if(hasSeat(c, url)){
					System.out.println(c + "HAS SEAT!");
					itr.remove();
				}

			}
		}catch(Exception e){
			e.printStackTrace();
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

		/* Read in the Courses to be checked */
		courses = readCourses("courses.txt");
		for(Course c : courses){
			System.out.println(c);
		}

		/* Initialize Timer */
		Timer t = new Timer();
		TimerTask tt = new TimerTask(){
			@Override
			public void run(){
				// Check if courses still has courses not found.
				if(courses.size() > 0){
					checkSeats(courses);
				}
			}
		};

		/* Start the timer to check all of the courses every 60 seconds */
		t.schedule(tt, 0, 60000);

	}


}