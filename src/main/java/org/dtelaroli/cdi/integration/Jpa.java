package org.dtelaroli.cdi.integration;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jglue.cdiunit.AdditionalClasses;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.jpa.EntityManagerCreator;
import br.com.caelum.vraptor.jpa.EntityManagerFactoryCreator;

@AdditionalClasses({EntityManagerCreator.class, EntityManagerFactoryCreator.class, ServletBasedEnvironment.class})
public class Jpa {

	@Inject private EntityManager em;
	private EntityTransaction tx;
	
	@Deprecated //cdi only
	public Jpa() {
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void begin() {
		if(tx == null) {
			tx = em.getTransaction();
		}
		tx.begin();
	}

	public void commit() {
		tx.commit();	
	}

	@PreDestroy
	public void rollback() {
		tx.rollback();
	}

	public boolean isActive() {
		return tx.isActive();
	}

	public boolean isOpen() {
		return em.isOpen();
	}

}
