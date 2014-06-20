package br.com.flexait.cdi.integration;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jglue.cdiunit.AdditionalClasses;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.jpa.EntityManagerCreator;
import br.com.caelum.vraptor.jpa.EntityManagerFactoryCreator;

@ApplicationScoped
@AdditionalClasses({EntityManagerCreator.class, EntityManagerFactoryCreator.class, ServletBasedEnvironment.class})
public class Jpa {

	@Inject private EntityManager em;
	private EntityTransaction tx;
	private Connection conn;
	
	@Deprecated //cdi only
	public Jpa() {
	}

	public EntityManager em() {
		return em;
	}

	public EntityTransaction beginTransaction() {
		if(tx == null) {
			tx = em().getTransaction();
		}
		tx.begin();
		return tx;
	}

	public boolean isOpen() {
		return em().isOpen();
	}

	public Connection getConnection() throws SQLException {
		doWork((Session) em().getDelegate());
		return conn;
	}

	private void doWork(Session session) {
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				conn = connection;				
			}
		});
	}

	@PreDestroy
	public void destroy() {
		if(tx != null && tx.isActive()) {
			tx.rollback();
		}
	}

}
