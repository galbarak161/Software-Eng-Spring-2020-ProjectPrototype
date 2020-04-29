package project.Prototype;

import java.util.Iterator;
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
import project.Entities.*;

public class HibernateMain {
	static Session session;
	static Random random = new Random();
	static boolean hibernateSessionStatus = false;

	/**
	 * The function create the session between the server and DB
	 * 
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
		configuration.addAnnotatedClass(Question.class);

		// Create new service and return it as session to DB
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	/**
	 * Get data collections from DB
	 * 
	 * @param <T>    - Type of entity
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


	public static List<Question> getDataQuestionsByCourseIdFromDB() {
		//String hql = "SELECT Q.questionText FROM Question AS Q WHERE Q.questionCode = 10000";
		String hql = "FROM question_course AS QC";
		List data = session.createQuery(hql).list();
		
        //for (Iterator iterator = data.iterator(); iterator.hasNext();){
        //	Question q = (Question) iterator.next(); 
        //    System.out.print("get Question Text: " + q.getQuestionText()); 
        // }
        //session.getTransaction().commit();
		return null;
	}
	
	/**
	 * Print object data using override toString() function
	 * 
	 * @param <T>        - Type of entity
	 * @param objectList
	 */
	public static <T> void printObjects(List<T> objectList) {
		for (int i = 0; i < objectList.size(); i++) {
			System.out.println(objectList.get(i));
		}
	}

	/**
	 * Initialize new object and set connections
	 * 
	 * @throws Exception
	 */
	private static void initializeData() throws Exception {

		final int NUMBER_OF_STUDIES = 5;
		final int NUMBER_OF_COURSES = 10;
		final int NUMBER_OF_QUESTIONS = 10;

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
		coursesName[7] = "Computer Science";
		coursesName[8] = "Economics";
		coursesName[9] = "Psychology";
		for (int i = 0, j = 0; i < NUMBER_OF_COURSES && j < NUMBER_OF_STUDIES; j++) {
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

		// Generate questions
		Question[] questions = new Question[NUMBER_OF_QUESTIONS];
		String[] questionsSubject = new String[NUMBER_OF_QUESTIONS];
		String questionsText = "What is the run time?";
		String[][] questionsAnswers = new String[NUMBER_OF_QUESTIONS][4];
		int[] correctAnswer = new int[NUMBER_OF_QUESTIONS];
		questionsSubject[0] = "DFS";
		questionsAnswers[0][0] = "O(n)";
		questionsAnswers[0][1] = "O(n^2)";
		questionsAnswers[0][2] = "O(n^3)";
		questionsAnswers[0][3] = "O(n^4)";
		correctAnswer[0] = 4;

		questionsSubject[1] = "BFS";
		questionsAnswers[1][0] = "O(n)";
		questionsAnswers[1][1] = "O(n^2)";
		questionsAnswers[1][2] = "O(n^3)";
		questionsAnswers[1][3] = "O(n^4)";
		correctAnswer[1] = 3;

		questionsSubject[2] = "Fibo Heap";
		questionsAnswers[2][0] = "O(n)";
		questionsAnswers[2][1] = "O(n^2)";
		questionsAnswers[2][2] = "O(n^3)";
		questionsAnswers[2][3] = "O(n^4)";
		correctAnswer[2] = 2;

		questionsSubject[3] = "Arrays";
		questionsAnswers[3][0] = "O(n)";
		questionsAnswers[3][1] = "O(n^2)";
		questionsAnswers[3][2] = "O(n^3)";
		questionsAnswers[3][3] = "O(n^4)";
		correctAnswer[3] = 1;

		questionsSubject[4] = "Sockets";
		questionsAnswers[4][0] = "O(n)";
		questionsAnswers[4][1] = "O(n^2)";
		questionsAnswers[4][2] = "O(n^3)";
		questionsAnswers[4][3] = "O(n^4)";
		correctAnswer[4] = 2;

		questionsSubject[5] = "Ferma's Little";
		questionsAnswers[5][0] = "O(n)";
		questionsAnswers[5][1] = "O(n^2)";
		questionsAnswers[5][2] = "O(n^3)";
		questionsAnswers[5][3] = "O(n^4)";
		correctAnswer[5] = 3;

		questionsSubject[6] = "Integrals";
		questionsAnswers[6][0] = "O(n)";
		questionsAnswers[6][1] = "O(n^2)";
		questionsAnswers[6][2] = "O(n^3)";
		questionsAnswers[6][3] = "O(n^4)";
		correctAnswer[6] = 3;

		questionsSubject[7] = "Graphics";
		questionsAnswers[7][0] = "O(n)";
		questionsAnswers[7][1] = "O(n^2)";
		questionsAnswers[7][2] = "O(n^3)";
		questionsAnswers[7][3] = "O(n^4)";
		correctAnswer[7] = 4;

		questionsSubject[8] = "RegEx";
		questionsAnswers[8][0] = "O(n)";
		questionsAnswers[8][1] = "O(n^2)";
		questionsAnswers[8][2] = "O(n^3)";
		questionsAnswers[8][3] = "O(n^4)";
		correctAnswer[8] = 2;

		questionsSubject[9] = "Binary Search";
		questionsAnswers[9][0] = "O(n)";
		questionsAnswers[9][1] = "O(n^2)";
		questionsAnswers[9][2] = "O(n^3)";
		questionsAnswers[9][3] = "O(n^4)";
		correctAnswer[9] = 3;

		for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
			questions[i] = new Question(questionsSubject[i], questionsText, questionsAnswers[i][0],
					questionsAnswers[i][1], questionsAnswers[i][2], questionsAnswers[i][3], correctAnswer[i]);
			questions[i].addCourses(courses[i]);
			questions[i].setQuestionCode(i * 10000);
			session.save(questions[i]);
		}
		session.flush();
		
		session.clear();
	}
	
	public static void main(String[] args) {
		try {
			initHibernate();
			getDataQuestionsByCourseIdFromDB();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeSession();
		}	
	}

	public static void closeSession() {
		session.close();
		System.out.println("Hibernate: Close session to DB!\n");
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