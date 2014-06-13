vraptor-cditest
===============

Projet of tests for Vraptor4 with CDI

### Dependencies

This project is an wrap of [this project](http://jglue.org/cdi-unit-user-guide/). All features can be used.

### Basic Usage

Create your test unit and add the annotation

```
@RunWith(CdiRunner.class)
```

Now, your application it's able to use @Inject annotation.

[Example in this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/java/br/com/flexait/cdi/integration/DbTest.java)

## API

### Jpa class

The Jpa Class is an wrap of VRaptor JPA Plugin. It's injected in the Db Class and can be returned by the method Db#jpa();

#### Configuration

Create a persistence file like [this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/resources/META-INF/persistence.xml) on the src/<your env [main or test]>/resources/META-INF folder. The default persistence unit name is 'default'. 
Create a properties like [this file](https://github.com/dtelaroli/vraptor-cditest/blob/master/src/test/resources/development.properties) on the src/<your env [main or test]>/resources folder to change the default name.

### Usage

```
@RunWith(CdiRunner.class)
public class YourTest {

	@Inject Jpa jpa;
	
	public void yourAssert() {
		EntityManager em = jpa.em();
		...
	}
}
```
### Db class

The Db Class is an wrap of DbUnit. It have features to open a connection and prepare data to your tests.

```

```