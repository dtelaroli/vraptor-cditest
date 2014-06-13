package org.dtelaroli.cdi.integration;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jglue.cdiunit.AdditionalClasses;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.jpa.EntityManagerCreator;
import br.com.caelum.vraptor.jpa.EntityManagerFactoryCreator;

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

	public EntityManager begin() {
		if(tx == null) {
			tx = em.getTransaction();
		}
		tx.begin();
		return em;
	}

	public void commit() {
		tx.commit();	
	}

	public void rollback() {
		tx.rollback();
	}

	public boolean isActive() {
		return tx.isActive();
	}

	public boolean isOpen() {
		return em.isOpen();
	}

	public Connection getConnection() throws SQLException {
		doWork((Session) em.getDelegate());
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

}
