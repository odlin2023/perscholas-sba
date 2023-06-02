package sba.sms.services;




import java.sql.Connection;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;



@Transactional
public class CourseService implements CourseI {
	private Connection connection;

    private EntityManagerFactory emf;
    
    @PersistenceContext(unitName = "sbajpa")
    private EntityManager entityManager;
    

    public CourseService() {
        emf = Persistence.createEntityManagerFactory("sbajpa");
        establishConnection();
    }
   
    

    private void establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/sbajpa";
            String username = "root";
            String password = "Jessicadaniel1.";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            ((Throwable) e).printStackTrace();
        }
    }

    
    public void createCourse(Course course) {
        PreparedStatement statement = null;
       

        try {
            // Create a prepared statement with a parameterized query
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbajpa", "root", "Jessicadaniel1.");
            String query = "INSERT INTO sbajpa.course (id,name, instructor) VALUES (?, ?,?)";
            statement = connection.prepareStatement(query);

            statement.setInt(1, course.getId());
            statement.setString(2, course.getName());
            statement.setString(3, course.getInstructor());

            statement.executeUpdate();

            // Commit the transaction
            connection.commit();

            System.out.println("Course created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();

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
   
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Create a prepared statement with a parameterized query
            String query = "SELECT * FROM sbajpa.course";
            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String instructor = resultSet.getString("instructor");

                Course course = new Course(id, name, instructor);
                courses.add(course);
            }

            // Commit the transaction
            connection.commit();

            System.out.println("All courses retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception and perform rollback

            rollbackConnection();

            System.out.println("Rollback performed.");
        } finally {
            // Close the result set, statement, and connection
            closeResultSet(resultSet);
            closeStatement(statement);
        }

        return courses;
    }

    private void rollbackConnection() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeStatement(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
   

    @Override
   
    public Course getCourseById(int courseId) {
        Course course = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM sbajpa.course WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, courseId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String courseName = resultSet.getString("name");
                String Instructor = resultSet.getString("instructor");
                course = new Course(courseId, courseName, Instructor);
            }

            connection.commit();
        } catch (SQLException e) {
            rollbackConnection();
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
        }

        return course;
    }
    
    public void close() {
        emf.close();
    }
}