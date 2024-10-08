package CAMs_App.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import CAMs_App.data.AuthData;
import CAMs_App.data.Database;
import CAMs_App.entity.*;
import java.util.Scanner;
/**
 * The {@link HelperService} class provides utility methods in various tasks related to the application
 * 
 *  @author Liang Meng
 *  @version 1.0
 *  @since 2023-10-25
*/
public class HelperService {
    static Scanner sc = new Scanner(System.in);

    /**
     * Method to clear the screen of the terminal for user experience and neat
     * interface.
    */
    public static void clearScreen() {
        try {
          new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception err) {}
    } 

    /**
     * Method to print a decorative overlay for presentation purposes
     * @param route : Present the route/menu to be displayed
    */
    public static void printRoute(String route) {
      int totalWidth = 105; // Adjust this based on the total width of your console or window
  
      int spacesNeeded = Math.max(0, totalWidth - route.length());
      int leftSpaces = spacesNeeded / 2;
      int rightSpaces = spacesNeeded - leftSpaces + 1;
  
      String leftPadding = String.format("%" + leftSpaces + "s", "");
      String rightPadding = String.format("%" + rightSpaces + "s", "");
  
      ColouredTextPrinter.printPurple(
              "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
              ColouredTextPrinter.printPurple("║" + leftPadding + route + rightPadding + "║");
      ColouredTextPrinter.printPurple(
              "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
  }
    /**
     * Method to print the student profile details
    */
    public static void printStudentProfile(){
      Student student = (Student)AuthData.getCurrentUser();
      ColouredTextPrinter.printCyan("\n-------------------------");
      ColouredTextPrinter.printCyan("  Your student profile");
      ColouredTextPrinter.printCyan("Name: " + student.getName());
      ColouredTextPrinter.printCyan("UserID: " + student.getUserID());
      ColouredTextPrinter.printCyan("Faculty: " + student.getFaculty());
      if(student.getIsComittee()){
      ColouredTextPrinter.printCyan("You are a committee member of " + student.getComitteeCamp().getCampName());
      ColouredTextPrinter.printCyan("Position held: " + student.getCampComMem().getPosition());
      ColouredTextPrinter.printCyan("Points accumulated: " + student.getPoints());
    }
      ColouredTextPrinter.printCyan("-------------------------\n");
    }
    /**
     * Method to read user's integer inputs and handles input mismatch exceptions 
     * @return the integer input from the user
    */
    public static int readInt(){
      while (true) {
        try {
          int userInput = -1;
          userInput = sc.nextInt();
          sc.nextLine(); // Consume newline left-over
          return userInput;
        } catch (InputMismatchException e) {
          sc.nextLine();
          ColouredTextPrinter.printRed("Invalid Input, enter an integer!");
        }
      }
    }
    /**
     * Method to read the integer input between a specified range and exception handling
     * @param min : Minimum allowed value
     * @param max : Maximum allowed value
     * @param message : Prints the message if the input is out of range
     * @return the valid integer user input
    */
    public static int readInt(int min, int max , String message) {
      while (true) {
          try{
              int userInput = -1;
              userInput = sc.nextInt();
              sc.nextLine(); // Consume newline left-over
              if (userInput < min || userInput > max) {
                throw new OutOfRange();
              } else {
                return userInput;
              }
          } catch (InputMismatchException e) {
              sc.nextLine();
              ColouredTextPrinter.printRed("\nInvalid Input, Enter an integer!");
            } catch (OutOfRange e) {
              System.out.println(message);
              }
        }
    }
    /**
       * Method to create a delay for a specified duration
       * @param sec : Number of seconds to wait
      */
      //make some delay
    public static void wait(int sec){
      int milli = sec*1000;
      try {
          Thread.sleep(milli);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
    /**
     * Method to prompt the user to press to continue.
    */
    public static void pressAnyKeyToContinue() {
      ColouredTextPrinter.printGreen("Press <Enter> key to continue...");
      try {
        System.in.read();
      } catch (Exception e) {
      }
    }
    /**
     * Method to view the current camp information
     * @param camp : Camp to be shown
    */
    public static void viewCamp(Camp camp){
        System.out.println("Camp Name: " + camp.getCampName());
        System.out.println("Camp Date: " + camp.getCampDate());
        System.out.println("Camp Registration Closing Date: " + camp.getRegCloseDate());
        System.out.println("Camp open to faculty: " + camp.getUserGroup().toString());   
        System.out.println("Camp location: " + camp.getLocation());
        System.out.println("Camp total slots: " + camp.getTotalSlots());
        System.out.println("Camp descriptions: " + camp.getDescription());
        System.out.println("Camp staffInCharge: " + camp.getStaffInCharge());
        System.out.println("Number of days: " + camp.getNumberOfCampDays());
        System.out.println("Remaining attendee slot: " + camp.getRemainingSlot());
        System.out.println("Remaining committee slot: " + camp.getCampCommitteeRemainingSlots());
        System.out.println("\n");
    }
    /**
     * Method to print the registered camp of a student
     * @param camp : The registered camp
     * @param student : Student who registered for the camp
    */
    public static void printRegisteredCamp(Camp camp, Student student){
        System.out.println("Camp Name: " + camp.getCampName());
        System.out.println("Camp Date: " + camp.getCampDate()); 
        System.out.println("Camp location: " + camp.getLocation());
        System.out.println("Camp descriptions: " + camp.getDescription());
        System.out.println("Camp staffInCharge: " + camp.getStaffInCharge());
        System.out.println("Number of days: " + camp.getNumberOfCampDays());
        if(student.getIsComittee() && camp.getCampName().equals(student.getComitteeCamp().getCampName())){
          System.out.println("You have signed up as a camp committee for this camp.");
        }
        else{
          System.out.println("You have signed up as an attendee for this camp.");
        }
        System.out.println("\n");
    }


    /**
     * Method to filter camp by date, location, faculty or alphabetical order(default)
     * @return list of sorted camps
    */
    public static List<Camp> filter(){
        System.out.println("View camps by :");
        System.out.println("(1) Dates");
        System.out.println("(2) Location");
        System.out.println("(3) Faculty");
        System.out.println("Alphabetical order by default (Any number)");
        Map<String, Camp> camp = Database.getCampData();

        int choice = HelperService.readInt();

        switch (choice) {
            case 1:    //date
                List<Camp> sortedDate = sortCampsByDates(camp);
                return sortedDate;
                
            
            case 2:    //location
                List<Camp> sortedLocation = sortCampsByLocation(camp);    
                return sortedLocation;

            case 3:    //Own faculty
                List<Camp> sortedFaculty = sortCampsByFaculty(camp);
                return sortedFaculty;
              
          
            default:   //alphabetical order
            List<Camp> sortedName = sortCampsByName(camp);
                return sortedName;
                
        }
    }
    /**
     * Method to sort camp by their name, to be used in {@link HelperService#filter()}
     * @param campMap : The map containing camps
     * @return list of camp sorted by name
    */
    public static List<Camp> sortCampsByName (Map<String,Camp>campMap) {
        List<Camp> campList = new ArrayList<>(campMap.values());
        Collections.sort(campList, Comparator.comparing(Camp::getCampName));
        return campList;
    }
    /**
     * Method to sort camp by thier name, used in {@link StudentCampService#viewRegisteredCamp()}
     * @param campArr : Arraylist of camps
     * @returns sorted list of camps by name
    */
    public static List<Camp> sortCampListByName (ArrayList<String>campArr) {
        List<Camp> campList = new ArrayList<>();
        for(String campName:campArr){
          campList.add(DatabaseService.getCamp(campName));
        }
        
        Collections.sort(campList, Comparator.comparing(Camp::getCampName));
        return campList;
    }
    /**
     * Method to sort camps by dates
     * @param campMap : Map containing camps
     * @return list of sorted camps by date
    */
    public static List<Camp> sortCampsByDates (Map<String,Camp>campMap) {
        List<Camp> campList = new ArrayList<>(campMap.values());
        Collections.sort(campList, Comparator.comparing(Camp::getCampDate));
        return campList;
    }
    /**
     * Method to sort camp by location
     * @param campMap : Map containing camps
     * @return sorted list of camp by location
    */
    public static List<Camp> sortCampsByLocation (Map<String,Camp>campMap) {
        List<Camp> campList = new ArrayList<>(campMap.values());
        Collections.sort(campList, Comparator.comparing(Camp::getLocation));
        return campList;
    }
    /**
     * Method to sort camp by faculty
     * @param campMap : Map containing camps
     * @return sorted list of camp by faculty
    */
    public static List<Camp> sortCampsByFaculty (Map<String,Camp>campMap) {
        List<Camp> campList = new ArrayList<>(campMap.values());
        Collections.sort(campList, Comparator.comparing(Camp::getUserGroup));
        return campList;
    }
    /**
     * Method to sort student by filtering name,faculty
     * @return sorted list of student either by name or faculty
    */
    public static List<Student> sortFilter(){
        System.out.println("Sort student by :");
        System.out.println("(1) Name");
        System.out.println("(2) Faculty");
        System.out.println("Alphabetical order by default (Any number)");
        Map<String, Student> student = Database.getStudentsData();

        int choice = HelperService.readInt();

        switch (choice) {
            case 2:    //faculty
                List<Student> sortedFaculty = sortStudentByFaculty(student);
                return sortedFaculty;
              
          
            default:   //alphabetical order
            List<Student> sortedName = sortStudentByName(student);
                return sortedName;
                
        }
    }
    /**
     * Method to sort student sorted by name
     * @param studentMap : Map containing students
     * @return list of students sorted by name
    */
    public static List<Student> sortStudentByName (Map<String,Student>studentMap) {
        List<Student> studentList = new ArrayList<>(studentMap.values());
        Collections.sort(studentList, Comparator.comparing(Student::getName));
        return studentList;
    }
    /**
     * Method to sort student by faculty
     * @param studentMap : Map containing students
     * @return list of students sorted by faculty 
    */
    public static List<Student> sortStudentByFaculty (Map<String,Student>studentMap) {
        List<Student> studentList = new ArrayList<>(studentMap.values());
        Collections.sort(studentList, Comparator.comparing(Student::getFaculty));
        return studentList;
    }
   

}
/**
 * Additonal class to handle out of range exceptions
*/
class OutOfRange extends Exception {

    /**
     * Constructor that initialises the error message
     */
    public OutOfRange() {
      super("Input is out of allowed range");
    }
  
    /**
     * Overrided constructor that initialises the error message with the specified
     * message
     * 
     * @param message error message to be displayed
     */
    public OutOfRange(String message) {
      super(message);
    }
}