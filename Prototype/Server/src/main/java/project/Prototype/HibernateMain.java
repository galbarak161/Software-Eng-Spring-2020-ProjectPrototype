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

	/**
	 * questionToUpdate(Question) update the question in DB
	 * 
	 * @param orignalQustion after updated fields
	 * @return 1 for success and -1 for failure
	 */
	public static int questionToUpdate(Question orignalQustion) {
		try {
			session.beginTransaction();
			session.evict(orignalQustion);
			session.update(orignalQustion);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
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
	 * Initialize new objects and set connections
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
		String[] questionsText =  new String[NUMBER_OF_QUESTIONS];
		String[][] questionsAnswers = new String[NUMBER_OF_QUESTIONS][4];
		int[] correctAnswer = new int[NUMBER_OF_QUESTIONS];
		
		
		questionsText[0]= "which machine has infinit possible states?";
		questionsSubject[0] = "turing machines";
		questionsAnswers[0][0] = "every machine has a finint number of states";
		questionsAnswers[0][1] = "deterministic finite automaton";
		questionsAnswers[0][2] = "Nondeterministic finite automaton";
		questionsAnswers[0][3] = "turing machine";
		correctAnswer[0] = 4;

		questionsText[1] = "What is the worst case for BFS?";
		questionsSubject[1] = "BFS";
		questionsAnswers[1][0] = "O(|V||E|)";
		questionsAnswers[1][1] = "O(|V|+|E|)^2";
		questionsAnswers[1][2] = "O(|V|+|E|)";
		questionsAnswers[1][3] = "O(|E|)";
		correctAnswer[1] = 3;

		questionsText[2] = "which state is the process in after it was running and got an interupt?";
		questionsSubject[2] = "Operating system concepts";
		questionsAnswers[2][0] = "waiting";
		questionsAnswers[2][1] = "running";
		questionsAnswers[2][2] = "terminated";
		questionsAnswers[2][3] = "new";
		correctAnswer[2] = 2;

		questionsText[3] = "what smallest component to self perserve an electric signal?";
		questionsSubject[3] = "Hardware?";
		questionsAnswers[3][0] = "Adder";
		questionsAnswers[3][1] = "MUX";
		questionsAnswers[3][2] = "flip-flop";
		questionsAnswers[3][3] = "ALU";
		correctAnswer[3] = 1;
		
		questionsText[4] = "which activity in AVL takes the most time complexcity wise?";
		questionsSubject[4] = "AVL";
		questionsAnswers[4][0] = "insert";
		questionsAnswers[4][1] = "delete";
		questionsAnswers[4][2] = "search";
		questionsAnswers[4][3] = "None of the above all take the same time";
		correctAnswer[4] = 2;

		questionsText[5] = "which string matches the regex (aab*|bc*)d";
		questionsSubject[5] = "RegEX";
		questionsAnswers[5][0] = "ad";
		questionsAnswers[5][1] = "bbcd";
		questionsAnswers[5][2] = "aabbbd";
		questionsAnswers[5][3] = "d";
		correctAnswer[5] = 3;

		questionsText[6] = "translation of the following statement,None of my friends are perfect";
		questionsSubject[6] = "First Order Logic";
		questionsAnswers[6][0] = "∃x(F(x)^P(x))";
		questionsAnswers[6][1] = "∃x(¬F(x)^¬P(x))";
		questionsAnswers[6][2] = "¬∃(F(x)^P(x))";
		questionsAnswers[6][3] = "∃x(¬F(x)^P(x))";
		correctAnswer[6] = 3;

		questionsText[7] = "How many registers are in in LC3?";
		questionsSubject[7] = "LC3 Assembely";
		questionsAnswers[7][0] = "32";
		questionsAnswers[7][1] = "4";
		questionsAnswers[7][2] = "8";
		questionsAnswers[7][3] = "16";
		correctAnswer[7] = 4;

		questionsText[8] = "what is theoretically the best encreption method that will never be broken?";
		questionsSubject[8] = "Encreption";
		questionsAnswers[8][0] = "AES";
		questionsAnswers[8][1] = "one time pad";
		questionsAnswers[8][2] = "RSA";
		questionsAnswers[8][3] = "Diffie-Hellman";
		correctAnswer[8] = 2;

		
		questionsText[9] = "who is the most handsome in this course?";
		questionsSubject[9] = "Life";
		questionsAnswers[9][0] = "everyone";
		questionsAnswers[9][1] = "no one";
		questionsAnswers[9][2] = "The professor";
		questionsAnswers[9][3] = "IDK";
		correctAnswer[9] = 3;

		for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
			questions[i] = new Question(questionsSubject[i], questionsText[i], questionsAnswers[i][0],
					questionsAnswers[i][1], questionsAnswers[i][2], questionsAnswers[i][3], correctAnswer[i],
					courses[i]);
			questions[i].GenerateQuestionCode();
			session.save(questions[i]);
		}
		session.flush();

		session.clear();
	}

	public static void main(String[] args) {
		try {
			initHibernate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
