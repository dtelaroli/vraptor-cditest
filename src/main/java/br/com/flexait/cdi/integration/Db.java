package br.com.flexait.cdi.integration;

import java.io.FileInputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jglue.cdiunit.ContextController;

@ApplicationScoped
public class Db {

	private static final String RESOURCES_DATASETS = "src/test/resources/datasets/";

	private String dataSetName;
	@Inject private Jpa jpa;
	@Inject ContextController ctx;

	@Deprecated //cdi only
	public Db() {
	}
	
	private IDataSet dataSet;

	private IDatabaseConnection connection;

	private boolean implicitRequest = true;

	@SuppressWarnings("rawtypes")
	public void init(Class... clazz) throws Exception {
		if(implicitRequest) {
			ctx.openRequest();
		}
		
		for (Class c : clazz) {
			initOne(c);
		}
	}
	
	public void clean() throws Exception {
		DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
		
		if(implicitRequest) {
			ctx.closeRequest();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initOne(Class c) throws Exception {
		dataSetName = c.getSimpleName();
		
		dataSet = getDataSet();
		connection = getConnection();
		DatabaseOperation.INSERT.execute(connection, dataSet);
	}

	protected IDataSet getDataSet() throws Exception {
		if(dataSet == null) {
			FileInputStream fileInputStream = new FileInputStream(getDataSetName());
			dataSet = new FlatXmlDataSet(fileInputStream);
		}
		
		return dataSet;
	}
	
	protected String getDataSetName() {
		return RESOURCES_DATASETS + dataSetName + ".xml";
	}
	
	protected IDatabaseConnection getConnection() throws Exception {
		if(connection == null) {
			connection = new DatabaseConnection(jpa.getConnection());
		}
		
		return connection;
	}


	protected Jpa jpa() {
		return jpa;
	}

	public ContextController ctx() {
		return ctx;
	}

	public Db withoutImplicitRequest() {
		implicitRequest = false;
		return this;
	}

}