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
		coursesName[4] = "Law";
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
		
		
		questionsText[0]= "who is best-selling music artist?";
		questionsSubject[0] = "Music";
		questionsAnswers[0][0] = "Eyal Golan";
		questionsAnswers[0][1] = "Michael Jackson";
		questionsAnswers[0][2] = "Madonna";
		questionsAnswers[0][3] = "The Beatles";
		correctAnswer[0] = 4;

		questionsText[1] = "Rudolf Nureyev is known for what type of dance??";
		questionsSubject[1] = "Theatre and Dance";
		questionsAnswers[1][0] = "Contemporary";
		questionsAnswers[1][1] = "Hip Hop";
		questionsAnswers[1][2] = "Ballet";
		questionsAnswers[1][3] = "ModernModern";
		correctAnswer[1] = 3;

		questionsText[2] = "what is a bad things to do for your body?";
		questionsSubject[2] = "Health Sciences?";
		questionsAnswers[2][0] = "bing Netflix";
		questionsAnswers[2][1] = "Exercise regularly";
		questionsAnswers[2][2] = "Eat more fruits and vegetables";
		questionsAnswers[2][3] = "Get a good night's sleep";
		correctAnswer[2] = 1;
		
		questionsText[3] = "what type of drug is Heroin classfied as?";
		questionsSubject[3] = "Pharmacy";
		questionsAnswers[3][0] = "Depressants";
		questionsAnswers[3][1] = "Opioids";
		questionsAnswers[3][2] = "Dissociatives";
		questionsAnswers[3][3] = "Inhalants";
		correctAnswer[3] = 2;
		
		questionsText[4] = "In US law, a malicious act to 'intentionally' cause damage to property is called what?";
		questionsSubject[4] = "Law";
		questionsAnswers[4][0] = "Malicious mischief";
		questionsAnswers[4][1] = "Vandalism";
		questionsAnswers[4][2] = "Mandamus";
		questionsAnswers[4][3] = "Intestacy";
		correctAnswer[4] = 2;

		questionsText[5] = "when looking at management levelsis a hierarchical view where is Supervisors located?";
		questionsSubject[5] = "Business management";
		questionsAnswers[5][0] = "Top-level";
		questionsAnswers[5][1] = "Middle-level";
		questionsAnswers[5][2] = "Low-level";
		questionsAnswers[5][3] = "Center-level";
		correctAnswer[5] = 3;

		questionsText[6] = "how many continents in the world?";
		questionsSubject[6] = "Geography";
		questionsAnswers[6][0] = "5";
		questionsAnswers[6][1] = "6";
		questionsAnswers[6][2] = "7";
		questionsAnswers[6][3] = "4";
		correctAnswer[6] = 3;

		questionsText[7] = "How many registers are in in LC3?";
		questionsSubject[7] = "LC3 Assembely";
		questionsAnswers[7][0] = "32";
		questionsAnswers[7][1] = "4";
		questionsAnswers[7][2] = "8";
		questionsAnswers[7][3] = "16";
		correctAnswer[7] = 4;

		questionsText[8] = "when was the stock market crash?";
		questionsSubject[8] = "Economics";
		questionsAnswers[8][0] = "2008";
		questionsAnswers[8][1] = "1929";
		questionsAnswers[8][2] = "2020";
		questionsAnswers[8][3] = "2010";
		correctAnswer[8] = 2;

		
		questionsText[9] = "Who is regarded as the father of psychology?";
		questionsSubject[9] = "Psychology";
		questionsAnswers[9][0] = "Mary Whiton Calkins";
		questionsAnswers[9][1] = "Sigmund Freud";
		questionsAnswers[9][2] = "Wilhelm Wundt";
		questionsAnswers[9][3] = "Kurt Lewin ";
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
