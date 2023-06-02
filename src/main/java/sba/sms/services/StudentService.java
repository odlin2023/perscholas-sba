package sba.sms.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import org.hibernate.query.Query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import java.util.ArrayList;
import sba.sms.services.StudentService;




public class StudentService implements StudentI {
private Connection connection;

   private EntityManagerFactory emf;
   @PersistenceContext(unitName = "sbajpa")
   private EntityManager entityManager;
   


   
   
  public StudentService() {
       // Constructor for initializing the StudentService object
       /*establishConnection(); */
       emf = Persistence.createEntityManagerFactory("sbajpa"); 
   } 


   
   public StudentService(Student student) {
       emf = Persistence.createEntityManagerFactory("sbajpa"); 
   }
  

   
   
   
   private  void establishConnection() {
       try {
           // Load the JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           // Establish the connection
           String url = "jdbc:mysql://localhost:3306/sbajpa";
           String username = "root";
           String password = "Jessicadaniel1.";
           connection = DriverManager.getConnection(url, username, password);
       } catch (ClassNotFoundException | SQLException e) {
           e.printStackTrace();
           // Handle the exception appropriately
       }
   } 
    

   

   
   
   public void createStudent(Student student) {
       PreparedStatement statement=null;
      

       try {
           // Create a prepared statement with a parameterized query
       	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");
           String query = "INSERT INTO sbajpa.student (email,name, password) VALUES (?, ?,?)";
           statement = connection.prepareStatement(query);
           statement.setString(1, student.getEmail());
           statement.setString(2, student.getName());
           statement.setString(3, student.getPassword());

           // Execute the insertion
           statement.executeUpdate();

           // Commit the transaction
           connection.commit();

           System.out.println("Course created successfully.");
       } catch (SQLException e) {
           e.printStackTrace();
           // Handle the exception and perform rollback

           try {
               connection.rollback();
               System.out.println("Rollback performed.");
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
       } finally {
           // Close the statement
           try {
               if (statement != null) {
                   statement.close();
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }

    

    @Override



    public List<Student> getAllStudents() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Student> allStudents = new ArrayList<>();

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");

            // Prepare the SQL statement to select all students
            String sql = "SELECT * FROM sbajpa";
            statement = connection.prepareStatement(sql);

            // Execute the SQL statement and retrieve the result set
            resultSet = statement.executeQuery();

            // Iterate through the result set and create Student objects
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String password = resultSet.getString("Password");

                Student student = new Student(email, name, password);
                allStudents.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the result set, statement, and connection
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return allStudents;
    }

  
    
    
    
    public Student getStudentByEmail(String email) {
        Student student=new Student();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Create a prepared statement with a parameterized query
            String query = "SELECT * FROM sbajpa.student WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);

            // Execute the query and get the result set
            resultSet = statement.executeQuery();

            // Check if a student with the given email exists
            if (resultSet.next()) {
            	String Email = resultSet.getString("email");
                String Password = resultSet.getString("password");
                String Name = resultSet.getString("name");
      
                student = new Student(Name, Email, Password);

                // Commit the transaction
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception and perform rollback

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Close the result set, statement, and connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return student;
    }
    
    
    
    
    
    @Override
 
    public boolean validateStudent(String email, String password) {
        boolean isValid = false;
        PreparedStatement statement =null;
        ResultSet resultSet = null;
        establishConnection();
     

        try {
            // Create a prepared statement with a parameterized query
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");
            String query = ("SELECT * FROM sbajpa.student WHERE email = ? AND password = ?;");
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            connection.setAutoCommit(true);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isValid = true;
                // Commit the transaction
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception and perform rollback
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Close the result set, statement, and connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isValid;
    }
    
    
    
    

    
    
    public void registerStudentToCourse(String email, int courseId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Get the student and course entities
            Student student = em.find(Student.class, email);
            Course course = em.find(Course.class, courseId);

            if (student != null && course != null) {
                // Create the SQL insert statement
                String sql = "INSERT INTO student_courses (course_id, student_email) VALUES (?, ?)";
                
                // Execute the SQL statement
                jakarta.persistence.Query query = em.createNativeQuery(sql);
                query.setParameter(1, course.getId());
                query.setParameter(2, student.getEmail());
                query.executeUpdate();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    

    private boolean isStudentRegisteredForCourse(String email, int courseId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Create a prepared statement with a parameterized query
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");
            String query = "SELECT * FROM sbajpa.student WHERE email = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setInt(2, courseId);

            // Execute the query and get the result set
            resultSet = statement.executeQuery();

            // Check if a record with the given email and courseId exists
            return resultSet.next();
        } finally {
            // Close the result set and statement
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    @Override
    public List<Course> getStudentCourses(String email) {
        List<Course> courses = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Create a prepared statement with a native query
            String query = "SELECT * FROM sbajpa.course " +
                           "INNER JOIN student_courses sc ON c.course_id = sc.course_id " +
                           "WHERE sc.email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);

            // Execute the query and get the result set
            resultSet = statement.executeQuery();

            // Iterate over the result set and populate the courses list
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String Instructor = resultSet.getString("Instructors");

                Course course = new Course(courseId, courseName,Instructor);

                courses.add(course);
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception and perform rollback
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Close the result set, statement, and connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return courses;
    }
}
   