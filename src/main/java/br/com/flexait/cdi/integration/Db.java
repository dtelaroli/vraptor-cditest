package br.com.flexait.cdi.integration;

import java.io.FileInputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public class Db {

	private static final String RESOURCES_DATASETS = "src/test/resources/datasets/";

	private String dataSetName;
	@Inject private Jpa jpa;

	private IDataSet dataSet;

	private IDatabaseConnection connection;

	@SuppressWarnings("rawtypes")
	public void init(Class... clazz) throws Exception {
		for (Class c : clazz) {
			initOne(c);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initOne(Class c) throws Exception {
		dataSetName = c.getSimpleName();
		
		dataSet = getDataSet();
		connection = getConnection();
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
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

	public Jpa jpa() {
		return jpa;
	}

	public void clean() throws Exception {
		DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
	}

	public EntityManager em() {
		return jpa.em();
	}
	
}