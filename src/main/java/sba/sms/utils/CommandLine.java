package sba.sms.utils;



import java.sql.SQLException;

import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.services.CourseService;
import sba.sms.services.StudentService;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.ArrayList;
import java.util.List;





public class CommandLine {
    private CommandLine() {
        // Utility classes should not have public constructors
    }

    public static void addData() {
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();

        String instructorPhillip = "Phillip Witkin";

        List<Student> dummyStudents = new ArrayList<>();
        dummyStudents.add(new Student("annette@gmail.com", "annette allen", "password"));
        dummyStudents.add(new Student("anthony@gmail.com", "anthony gallegos", "password"));
        dummyStudents.add(new Student("ariadna@gmail.com", "ariadna ramirez", "password"));
        dummyStudents.add(new Student("ariadna@gmail.com", "ariadna ramirez", "password"));
        dummyStudents.add(new Student("bolaji@gmail.com", "bolaji saibu", "password"));

        List<Course> dummyCourses = new ArrayList<>();
        dummyCourses.add(new Course(0, "Java", instructorPhillip));
        dummyCourses.add(new Course(0, "Frontend", "Kasper Kain"));
        dummyCourses.add(new Course(0, "JPA", "Jafer Alhaboubi"));
        dummyCourses.add(new Course(0, "Spring Framework", instructorPhillip));
        dummyCourses.add(new Course(0, "SQL", instructorPhillip));
        

        String url = "jdbc:mysql://localhost:3306/sbajpa";
        String username = "root";
        String password = "Jessicadaniel1.";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            for (Student student : dummyStudents) {
                studentService.createStudent(student);
            }

            for (Course course : dummyCourses) {
                courseService.createCourse(course);
            }

            connection.commit();

            System.out.println("Dummy data insertion completed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






/*public class CommandLine {
	private CommandLine() {
        // Utility classes should not have public constructors
    }

    //private static final String PASSWORD = "password";
 public static void addData() {
    	 StudentService studentService = new StudentService();
         CourseService courseService = new CourseService();
         
    	

        String instructorPhillip = "Phillip Witkin";
        
        studentService.createStudent(new Student("reema@gmail.com", "reema brown", "password"));
        studentService.createStudent(new Student("annette@gmail.com", "annette allen", "password"));
        studentService.createStudent(new Student("anthony@gmail.com", "anthony gallegos", "password"));
        studentService.createStudent(new Student("ariadna@gmail.com", "ariadna ramirez", "password"));
        studentService.createStudent(new Student("bolaji@gmail.com", "bolaji saibu", "password"));

        courseService.createCourse(new Course(0, "Java", instructorPhillip));
        courseService.createCourse(new Course(0, "Frontend", "Kasper Kain"));
        courseService.createCourse(new Course(0, "JPA", "Jafer Alhaboubi"));
        courseService.createCourse(new Course(0, "Spring Framework", instructorPhillip));
        courseService.createCourse(new Course(0, "SQL", instructorPhillip));
        


     /*
    
 }
 */

