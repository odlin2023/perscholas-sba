package sba.sms.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
       /*establishConnection();
       emf = Persistence.createEntityManagerFactory("sbajpa"); */
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
    

   
  /* public  void createStudent(Student student) {
	   establishConnection();
       PreparedStatement statement = null;

       try {
           connection.setAutoCommit(false);

           // Prepare the INSERT query
           String query = "INSERT INTO sbajpa.student (name, email, password) VALUES (?, ?, ?)";
           statement = connection.prepareStatement(query);

           // Set the parameter values
           statement.setString(0, student.getEmail());
           statement.setString(1, student.getName());
           statement.setString(2, student.getPassword());

           // Execute the INSERT query
           statement.executeUpdate();

           // Commit the transaction
           connection.commit();
           

           // Student added successfully

       } catch (SQLException e) {
           e.printStackTrace();
           
           // Rollback the transaction in case of exception
           if (connection != null) {
               try {
                   connection.rollback();
               } catch (SQLException ex) {
                   ex.printStackTrace();
               }
           }
       } finally {
           // Close the statement and connection
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
   }
*/
   
   
   
   
   @Override
  /* public void createStudent(Student student) {
       Connection connection = null;
       Savepoint savepoint = null;
       
       try {
           // Establish the database connection
           connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");
           
           // Disable auto-commit to handle transactions manually
           connection.setAutoCommit(false);
           
           // Create a savepoint before making any changes
           savepoint = connection.setSavepoint();
           
           // Code to persist the student to the database
           // Execute the necessary SQL statements or use prepared statements
           // Example:
           String insertQuery = "INSERT INTO sbajpa.student (name,email,password) VALUES (?, ?,?)";
           PreparedStatement statement = connection.prepareStatement(insertQuery);
           statement.setString(1, student.getName());
           statement.setString(2, student.getEmail());
           statement.setString(2, student.getPassword());
           statement.executeUpdate();
           
           // Commit the transaction
           connection.commit();
           
       } catch (SQLException e) {
           // Handle exceptions
           
           // Roll back to the savepoint if an exception occurs
           if (connection != null) {
               try {
                   connection.rollback(savepoint);
               } catch (SQLException rollbackException) {
                   // Handle rollback exception
               }
           }
           
           // Log or re-throw the exception as needed
       } 
           // Close the connection and release resources
           if (connection != null) {
               try {
                   connection.close();
               } catch (SQLException closeException) {
                   // Handle closing exception
               }
           }
       }
   
   */
   
   
   
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
    
    
    
    
    @Override
    
    public void registerStudentToCourse(String email, int courseId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Check if the student is already registered for the course
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");
            boolean isRegistered = isStudentRegisteredForCourse(email, courseId);
            if (isRegistered) {
                System.out.println("Student is already registered for the course.");
                return;
            }

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
   