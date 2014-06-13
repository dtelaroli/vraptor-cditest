package org.dtelaroli.cdi.model;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hamcrest.Matchers;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.jpa.EntityManagerCreator;
import br.com.caelum.vraptor.jpa.EntityManagerFactoryCreator;

@RunWith(CdiRunner.class)
@AdditionalClasses({EntityManagerCreator.class, EntityManagerFactoryCreator.class, ServletBasedEnvironment.class})
public class ModelTest {

	@Inject EntityManager em;
	@Inject ContextController contextController;
	private EntityTransaction tx;
	
	@Before
	public void setUp() {
		contextController.openRequest();
		tx = em.getTransaction();
		tx.begin();
	}
	
	@After
	public void tearDown() {
		tx.commit();
		contextController.closeRequest();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		Model model = new Model();
		model.name = "foo";
		
		model = em.merge(model);
		
		Assert.assertThat(model.id, Matchers.notNullValue());
		
		Query query = em.createQuery("FROM Model");
		List<Model> models = (List<Model>) query.getResultList();
		Assert.assertThat(models.size(), Matchers.equalTo(1));		
	}

}