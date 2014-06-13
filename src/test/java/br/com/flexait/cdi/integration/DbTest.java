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

import br.com.flexait.cdi.integration.Db;
import br.com.flexait.cdi.integration.Jpa;
import br.com.flexait.cdi.model.Model;

@RunWith(CdiRunner.class)
public class DbTest {

	@Inject ContextController ctx;
	@Inject Db db;
	
	@Before
	public void setUp() throws Exception {
		ctx.openRequest();
	}

	@After
	public void tearDown() throws Exception {
		ctx.closeRequest();
	}
	
	@Test
	public void shouldInjectJpa() {
		assertThat(db.jpa(), instanceOf(Jpa.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void shouldGenerateDataByModelNameAndDataSource() throws Exception {
		db.init(Model.class);
		
		Query query = db.jpa().em().createQuery("FROM Model");
		List list = query.getResultList();
		assertThat(list.size(), equalTo(2));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void shouldCleanDataBase() throws Exception {
		db.init(Model.class);
		
		EntityManager em = db.jpa().em();
		Query query = em.createQuery("FROM Model");
		List list = query.getResultList();
		assertThat(list.size(), equalTo(2));
		
		db.clean();
		query = em.createQuery("FROM Model");
		list = query.getResultList();
		assertThat(list.size(), equalTo(0));
	}
}