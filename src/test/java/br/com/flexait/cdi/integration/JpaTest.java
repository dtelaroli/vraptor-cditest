package br.com.flexait.cdi.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.flexait.cdi.model.Model;

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
		assertThat(jpa.em(), notNullValue());
		assertThat(jpa.em().isOpen(), equalTo(true));
	}
	
	@Test
	public void shouldOpenTransaction() {
		jpa.beginTransaction();
		assertThat(jpa.em().isJoinedToTransaction(), equalTo(true));
	}
	
	@Test
	public void shouldReturnTrueIfConnectionOpen() {
		assertThat(jpa.isOpen(), equalTo(true));
	}
	
	@Test
	public void shouldReturnFalseIfConnectionOpen() {
		jpa.em().close();
		
		assertThat(jpa.isOpen(), equalTo(false));
	}
	
	@Test
	public void shouldReturnUnwrappedConnection() throws SQLException {
		Connection conn = jpa.getConnection();
		
		assertThat(conn.isClosed(), equalTo(false));
		conn.close();
	}
	
	@Test
	public void shouldCommitOnDestroy() {
		jpa.beginTransaction();
		assertThat(jpa.em().getTransaction().isActive(), equalTo(true));
		
		jpa.destroy();
			
		assertThat(jpa.em().getTransaction().isActive(), equalTo(false));
	}
	
	@Test
	public void shouldRollbackOnCommitErrorOnDestroy() {
		EntityTransaction tx = jpa.beginTransaction();
		assertThat(jpa.em().getTransaction().isActive(), equalTo(true));
		
		jpa.em().merge(new Model());
		tx.commit();
		jpa.destroy();
		
		assertThat(jpa.em().getTransaction().isActive(), equalTo(false));
	}
	
	@Test
	public void shouldReturnTransactionActive() {
		EntityTransaction tx = jpa.beginTransaction();
		assertThat(tx.isActive(), equalTo(true));
		tx.rollback();
	}
	
}
