vraptor-cditest
===============

Testing API for Vraptor4 with CDI

### Dependencies

This project is a wrap of [this project](http://jglue.org/cdi-unit-user-guide/). All features can be used.

#### Adding the CdiTest for your project

```
<dependency>
	<groupId>br.com.flexait</groupId>
	<artifactId>cdi-test</artifactId>
	<version>0.0.1</version>
</dependency>
```

### Basic Usage

Create your test unit and add the annotation

```
@RunWith(CdiRunner.class)
```

Now, your application it's able to use @Inject annotation.

[Example in this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/java/br/com/flexait/cdi/integration/DbTest.java)

## API

### Jpa class

The Jpa Class is a wrap of VRaptor JPA Plugin. 
It's injected in the Db Class and can be returned by the method Db#jpa() or the EntityManager directly by the Db#em();

#### Configuration

Create a persistence file like [this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/resources/META-INF/persistence.xml) on the src/test/resources/META-INF folder. The default persistence unit is called 'default'. 
Create a properties like [this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/resources/development.properties) on the src/test/resources folder to change the default name.

#### Usage

```
@RunWith(CdiRunner.class)
public class YourTest {

	@Inject Jpa jpa;
	
	public void yourAssert() {
		EntityManager em = jpa.em();
		...
	}
}

//Other methods
EntityManager Jpa#begin() //open a transaction

Jpa#commit()

Jpa#rollback()

boolean Jpa#isActive() //true if transaction is active

boolean Jpa#isOpen() //true if entityManager is open 

Connection Jpa#getConnection() //JDBC Connection

```

### Db class

The Db Class is a wrap of DbUnit. It has features to open a connection and prepare the data for your tests.

#### Configuration

Create the xml like [this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/resources/datasets/Model.xml) on the src/test/resouces/datasets folder.

Model: Entity Class Name
name: Name property in the Entity Model

Each Model xml node is a line inserted on the database.

```
@RunWith(CdiRunner.class)
public class YourTest {

	@Inject Db db;
	
	public void yourAssert() {
		db.init(YouModel.class, YouModel2.class);
		...
	}
}

//Other methods
Db#close() // erase the database

EntityManager Db#em()
```

### Logging

[Example](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/resources/log4j.xml)
