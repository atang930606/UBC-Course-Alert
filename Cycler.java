import java.util.*;
import java.net.*;
import java.io.*;

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
		boolean restricted;


		public Couse(String dept, String course, String section, boolean restricted, String email){
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
				if(tokens.length != )
			}

		}catch(Exception e){

		}finally{
			if(fr != null){
				fr.close();
			}
		}

	}


	public static void main(String[] args) {
		
	}


}