package ca.sheridancollege.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.GameState;
import ca.sheridancollege.beans.QuestionCategory;
import ca.sheridancollege.beans.User;

public class DAO {
	SessionFactory sessionFactory = new Configuration().configure("ca/sheridancollege/config/hibernate.cfg.xml").buildSessionFactory();
	
	private static DAO INSTANCE = null;
	
	public static DAO getInstance(){
		if(INSTANCE == null){
			synchronized(DAO.class){
				if(INSTANCE == null){
					INSTANCE = new DAO();
				}
			}
		}
		return INSTANCE;
	}
	
	private DAO(){
		
	}
	
	public boolean save(User user, boolean update){
		boolean saved = true;
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		if(!exists(user.getUsername())){
			session.save(user);
		} else if(update){
			session.update(user);
		} else {
			saved = false;
		}
		
		session.getTransaction().commit();
		session.close();
		
		return saved;
	}

	public void save(QuestionCategory q){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(q);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void save(GameState g){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(g);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void update(GameState g){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.update(g);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void remove(GameState g){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.delete(g);
		
		session.getTransaction().commit();
		session.close();
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public User findByUsername(String username, boolean includeRoles){
		Session session = sessionFactory.openSession();
		
		List<User> users = session
			.createQuery("from User where username=:user")
			.setParameter("user", username)
			.list();
		
		User user = null;
		if(users.size() > 0) user = users.get(0);
		
		if(user != null && includeRoles){
			Hibernate.initialize(user.getUserRole());
		}
		
		session.close();
		
		return user;
	}
	
	private boolean exists(String user){
		return findByUsername(user, false) != null;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public GameState findGameByUser(User user){
		Session session = sessionFactory.openSession();
		
		List<GameState> games = session
				.createQuery("from GameState where user=:user")
				.setParameter("user", user)
				.list();
		
		GameState game = null;
		if(games.size() > 0) game = games.get(0);
		
		if(game != null){
			Hibernate.initialize(game.getQuestions());
		}
		
		session.close();
		
		return game;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<QuestionCategory> getAllQuestionCategories(){
		Session session = sessionFactory.openSession();
		
		List<QuestionCategory> categories = session
				.createQuery("from QuestionCategory")
				.list();
		
		for(QuestionCategory qc : categories){
			Hibernate.initialize(qc.getQuestions());
		}
		
		session.close();
		
		return categories;
	}
}
