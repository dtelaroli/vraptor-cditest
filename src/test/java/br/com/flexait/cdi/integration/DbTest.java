package br.com.flexait.cdi.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.flexait.cdi.model.Model;

@RunWith(CdiRunner.class)
public class DbTest {

	@Inject Db db;
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
	public void shouldInjectJpa() {
		assertThat(db.jpa(), instanceOf(Jpa.class));
	}
	
	@Test
	public void shouldInjectContextController() {
		assertThat(db.ctx(), instanceOf(ContextController.class));
	}
	
	@Test(expected = RuntimeException.class)
	public void shouldEvictOpenRequest() throws Exception {
		ctx.closeRequest();
		
		db.withoutImplicitRequest().init(Model.class);
		db.ctx().currentRequest();
	}
	
	@Test
	public void shouldOpenRequest() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		db.ctx().currentRequest();
	}
	
	@Test
	public void shouldGenerateDataByModelNameAndDataSource() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		
		Query query = jpa.em().createQuery("FROM Model");
		List<?> list = query.getResultList();
		assertThat(list.size(), equalTo(2));
	}
	
	@Test
	public void shouldCleanDataBase() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		
		EntityManager em = jpa.em();
		Query query = em.createQuery("FROM Model");
		List<?> list = query.getResultList();
		assertThat(list.size(), equalTo(2));
		
		db.clean();
		query = em.createQuery("FROM Model");
		list = query.getResultList();
		assertThat(list.size(), equalTo(0));
	}
	
	@Test
	public void shouldEscapeCleanIfIsRollbackOnly() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		
		EntityManager em = jpa.em();
		Query query = em.createQuery("FROM Model");
		List<?> list = query.getResultList();
		assertThat(list.size(), equalTo(2));
		
		jpa.beginTransaction().setRollbackOnly();
		
		db.clean();
		query = em.createQuery("FROM Model");
		list = query.getResultList();
		assertThat(list.size(), equalTo(2));
	}
	
}