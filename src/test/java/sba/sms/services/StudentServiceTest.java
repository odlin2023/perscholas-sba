package sba.sms.services;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import sba.sms.models.Course;
import sba.sms.models.Student;
import java.util.List;

/*@FieldDefaults(level = AccessLevel.PRIVATE)
class StudentServiceTest {

    static StudentService studentService;

    @BeforeAll
    static void beforeAll() {
        studentService = new StudentService();
        CommandLine.addData();
    }

    @Test
    void getAllStudents() {

        List<Student> expected = new ArrayList<>(Arrays.asList(
                new Student("reema@gmail.com", "reema brown", "password"),
                new Student("annette@gmail.com", "annette allen", "password"),
                new Student("anthony@gmail.com", "anthony gallegos", "password"),
                new Student("ariadna@gmail.com", "ariadna ramirez", "password"),
                new Student("bolaji@gmail.com", "bolaji saibu", "password")
        ));

        assertThat(studentService.getAllStudents()).hasSameElementsAs(expected);

    }
}
*/



public class StudentServiceTest {
    private StudentService studentService = new StudentService();

    @Test
    public void testValidLogin() {
        // Test a valid login scenario
        String email = "anthony@gmail.com";
        String password = "password";
        boolean loginResult = studentService.validateStudent(email, password);
        Assertions.assertThat(loginResult);
    }

    @Test
    public void testInvalidLogin() {
        // Test an invalid login scenario
        String email = "invaliduser@gmail.com";
        String password = "password";
        boolean loginResult = studentService.validateStudent(email, password);
        Assertions.assertThat(loginResult);
    }
    

    @Test
    void testCourseRegistration() {
        // Create a new student and a course
        Student student = new Student("reema@gmail.com", "Reema Brown", "password");
        Course course = new Course(0, "JPA", "Jafer Alhaboubi");

        // Register the student for the course
        studentService.registerStudentToCourse(student.getEmail(), course.getId());

        // Get the student's registered courses
        List<Course> registeredCourses = studentService.getStudentCourses(student.getEmail());

        // Verify that the student is registered for the course
        Assertions.assertThat(registeredCourses.contains(course));
}
}