package com.urise.webapp.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ArrayStorageTest.class, SortedArrayStorageTest.class, 
				 ListStorageTest.class, 
				  MapStorageTest.class,
		   ObjectFileStorageTest.class,  ObjectPathStorageTest.class, 
			  XmlFileStorageTest.class, 	XmlPathStorageTest.class,
			 JsonFileStorageTest.class,    JsonPathStorageTest.class, 
			 DataFileStorageTest.class,    DataPathStorageTest.class,
		  JacksonFileStorageTest.class, JacksonPathStorageTest.class,
		  		  SqlStorageTest.class})
public class AllTests {

}
