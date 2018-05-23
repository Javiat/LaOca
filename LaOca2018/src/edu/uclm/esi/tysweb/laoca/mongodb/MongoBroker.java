package edu.uclm.esi.tysweb.laoca.mongodb;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoBroker {
	private ConcurrentLinkedQueue<MongoClient> usadas, libres;
	private MongoClient conexionPrivilegiada;
	
	private MongoBroker() {
		ServerAddress address=new ServerAddress("localhost");
		this.conexionPrivilegiada=new MongoClient(address);
		
		this.usadas=new ConcurrentLinkedQueue<>();
		this.libres=new ConcurrentLinkedQueue<>();
		
		for (int i=0; i<10; i++) {
			MongoClient conexion=new MongoClient(address);
			this.libres.add(conexion);
		}
	}
	
	
	private static class MongoBrokerHolder {
		static MongoBroker singleton=new MongoBroker();
	}
	
	public static MongoBroker get() {
		return MongoBrokerHolder.singleton;
	}

	public MongoDatabase getDatabase(String databaseName) {
		return conexionPrivilegiada.getDatabase(databaseName);
	}

	public void close() {
		this.conexionPrivilegiada.close();
	}

	public MongoClient getDatabase(String databaseName, String email, String pwd) throws Exception {
		MongoCredential credenciales=MongoCredential.createCredential(email, databaseName, pwd.toCharArray());
		ServerAddress address=new ServerAddress("localhost");
		List<MongoCredential> lista=Arrays.asList(credenciales);
		return new MongoClient(address, lista);
	}
	
	public MongoClient getConexionPrivilegiada() {
		
        ServerAddress address = new ServerAddress("localhost");
        
        return new MongoClient(address);
	}
}







