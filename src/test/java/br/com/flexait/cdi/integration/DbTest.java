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
import org.jglue.cdiunit.InRequestScope;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.flexait.cdi.model.Model;

@RunWith(CdiRunner.class)
public class DbTest {

	@Inject Db db;
	@Inject Jpa jpa;
	
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
		db.withoutImplicitRequest().init(Model.class);
		db.ctx().currentRequest();
	}
	
	@Test @InRequestScope
	public void shouldOpenRequest() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		db.ctx().currentRequest();
	}
	
	@SuppressWarnings("rawtypes")
	@Test @InRequestScope
	public void shouldGenerateDataByModelNameAndDataSource() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		
		Query query = jpa.em().createQuery("FROM Model");
		List list = query.getResultList();
		assertThat(list.size(), equalTo(2));
	}
	
	@SuppressWarnings("rawtypes")
	@Test @InRequestScope
	public void shouldCleanDataBase() throws Exception {
		db.withoutImplicitRequest().init(Model.class);
		
		EntityManager em = jpa.em();
		Query query = em.createQuery("FROM Model");
		List list = query.getResultList();
		assertThat(list.size(), equalTo(2));
		
		db.clean();
		query = em.createQuery("FROM Model");
		list = query.getResultList();
		assertThat(list.size(), equalTo(0));
	}
	
}