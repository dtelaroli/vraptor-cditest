package org.dtelaroli.cdi.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.dtelaroli.cdi.integration.Jpa;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
public class JpaTest {

	@Inject Jpa jpa;
	@Inject ContextController ctx;
	
	@Before
	public void setUp() {
		ctx.openRequest();
	}
	
	@After
	public void tearDown() {
		ctx.closeRequest();
	}
	
	@Test
	public void shouldInject() {
		assertThat(jpa, notNullValue());
	}
	
	@Test
	public void shouldBeOpenEntityManager() {
		assertThat(jpa.getEntityManager(), notNullValue());
		assertThat(jpa.getEntityManager().isOpen(), equalTo(true));
	}
	
	@Test
	public void shouldOpenTransaction() {
		jpa.begin();
		assertThat(jpa.getEntityManager().isJoinedToTransaction(), equalTo(true));
	}
	
	@Test
	public void shouldCommitTransaction() {
		jpa.begin();
		assertThat(jpa.getEntityManager().getTransaction().isActive(), equalTo(true));
		
		jpa.commit();		
		assertThat(jpa.getEntityManager().getTransaction().isActive(), equalTo(false));
	}
	
	@Test
	public void shouldRollbackTransaction() {
		jpa.begin();
		assertThat(jpa.getEntityManager().getTransaction().isActive(), equalTo(true));
		
		jpa.rollback();		
		assertThat(jpa.getEntityManager().getTransaction().isActive(), equalTo(false));
	}
	
	@Test
	public void shouldReturnTrueIfTransactionIsActive() {
		jpa.begin();
		
		assertThat(jpa.isActive(), equalTo(true));
	}
	
	@Test
	public void shouldReturnFalseIfTransactionIsActive() {
		jpa.begin();
		jpa.commit();
		
		assertThat(jpa.isActive(), equalTo(false));
	}
	
	@Test
	public void shouldReturnTrueIfConnectionOpen() {
		assertThat(jpa.isOpen(), equalTo(true));
	}
	
	@Test
	public void shouldReturnFalseIfConnectionOpen() {
		jpa.getEntityManager().close();
		
		assertThat(jpa.isOpen(), equalTo(false));
	}
	
}
