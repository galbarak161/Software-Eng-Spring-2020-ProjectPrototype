package project.Prototype;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import project.Entities.Course;
import project.Entities.Study;

public class HibernateMain {
	static Session session;
	static Random random = new Random();
	static boolean hibernateSessionStatus = false;
	
	/*
	 * static final int NUMBER_OF_CARS = 10; static final int NUMBER_OF_PERSONS = 7;
	 * static final int NUMBER_OF_GARAGES = 2; static List<Car> cars; static
	 * List<Person> persons; static List<Garage> garages;
	 */

	/**
	 * The function create the session between the server and DB
	 * @return new session to DB
	 * @throws HibernateException
	 */
	private static SessionFactory getSessionFactory() throws HibernateException {
		// Turn off hibernate warning & info messages
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

		// Configure all objects annotations
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Study.class);
		configuration.addAnnotatedClass(Course.class);

		// Create new service and return it as session to DB
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	/**
	 * Get data collections from DB
	 * @param <T> - Type of entity
	 * @param entity
	 * @return List of entities from type <T>
	 * @throws Exception
	 */
	public static <T> List<T> getDataFromDB(Class<T> entity) throws Exception {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(entity);
		Root<T> rootEntry = criteriaQuery.from(entity);
		CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);
		TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
		return allQuery.getResultList();
	}

	/**
	 * Print object data using override toString() function
	 * @param <T> - Type of entity
	 * @param objectList
	 */
	public static <T> void printObjects(List<T> objectList) {
		for (int i = 0; i < objectList.size(); i++) {
			System.out.println(objectList.get(i));
		}
	}

	/**
	 * Initialize new object and set connections
	 * @throws Exception
	 */
	private static void initializeData() throws Exception {

		final int NUMBER_OF_STUDIES = 5;
		final int NUMBER_OF_COURSES = 10;
		// Generate fields of study
		Study[] studies = new Study[NUMBER_OF_STUDIES];
		String[] studiesName = new String[NUMBER_OF_STUDIES];
		studiesName[0] = "Arts";
		studiesName[1] = "Health and welfare";
		studiesName[2] = "Business Administration";
		studiesName[3] = "Natural sciences";
		studiesName[4] = "Social sciences";
		for (int i = 0; i < NUMBER_OF_STUDIES; i++) {
			Study s = new Study(studiesName[i]);
			studies[i] = s;
			session.save(s);
		}
		session.flush();

		// Generate courses
		Course[] courses = new Course[NUMBER_OF_COURSES];
		String[] coursesName = new String[NUMBER_OF_COURSES];
		coursesName[0] = "Music";
		coursesName[1] = "Theatre and Dance";
		coursesName[2] = "Health Sciences";
		coursesName[3] = "Pharmacy";
		coursesName[4] = "Low";
		coursesName[5] = "Business management";
		coursesName[6] = "Geography";
		coursesName[7] = "Computer Science ";
		coursesName[8] = "Economics";
		coursesName[9] = "Psychology";
		for (int i = 0, j = 0; i < NUMBER_OF_COURSES && j< NUMBER_OF_STUDIES; j++) {
			Course c1 = new Course(coursesName[i]);
			c1.addStudies(studies[j]);
			courses[i] = c1;
			i++;
			session.save(c1);
			Course c2 = new Course(coursesName[i]);
			c2.addStudies(studies[j]);
			courses[i] = c2;
			i++;	
			session.save(c2);
		}
		session.flush();
		
		
		// Note: Person may own more than one car and more than one garage
		// Note: Car may visit in more than one garage
		// Note: Garage my be own to more than one person and my serve more than one car

		// *** See Entities class for more info about entities connections ***
	}

	public static void main(String[] args) {
		try {
			System.out.println("Hibernate: Create new session using getSessionFactory...\n");
			SessionFactory sessionFactory = getSessionFactory();

			System.out.println("Hibernate: Open session...\n");
			session = sessionFactory.openSession();

			System.out.println("Hibernate: Begin Transaction...\n");
			session.beginTransaction();

			System.out.println("Hibernate: Generate and insert data to DB...\n");
			// Initialize Data - Insert query
			initializeData();

			System.out.println("Hibernate: Get data from DB...\n");
			// Get all data from DB - Select query
			/*
			 * cars = getDataFromDB(Car.class); persons = getDataFromDB(Person.class);
			 * garages = getDataFromDB(Garage.class);
			 */
			System.out.println("Hibernate: Committing all queries before closing connection...\n");
			session.getTransaction().commit();

		} catch (Exception exception) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			System.err.println("Hibernate: An error occured, changes have been rolled back.");
			exception.printStackTrace();

		} finally {
			session.close();
			System.out.println("Hibernate: Close session to DB!\n");
		}

		System.out.println("############## Print data ##############\n");
		// printObjects(cars);
		// printObjects(garages);
		System.out.println("############## Finish Print ##############\n");
	}

	public static boolean initHibernate() {
		boolean status = false;
		try {
			System.out.println("Hibernate: Create new session using getSessionFactory...\n");
			SessionFactory sessionFactory = getSessionFactory();

			System.out.println("Hibernate: Open session...\n");
			session = sessionFactory.openSession();

			System.out.println("Hibernate: Begin Transaction...\n");
			session.beginTransaction();

			System.out.println("Hibernate: Generate and insert data to DB...\n");
			initializeData();

			System.out.println("Hibernate: Committing all queries before closing connection...\n");
			session.getTransaction().commit();

			status = true;
			
		} catch (Exception exception) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			System.err.println("Hibernate: An error occured, changes have been rolled back.");
			status = false;
			exception.printStackTrace();
		}
		hibernateSessionStatus = status;
		return status;
	}
}