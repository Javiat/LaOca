package edu.uclm.esi.tysweb.laoca.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import org.json.JSONObject;

import javax.print.Doc;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;

import edu.uclm.esi.tysweb.laoca.dominio.EMailSenderService;
import edu.uclm.esi.tysweb.laoca.dominio.Usuario;
import edu.uclm.esi.tysweb.laoca.dominio.UsuarioRegistrado;
import edu.uclm.esi.tysweb.laoca.mongodb.MongoBroker;

public class DAOUsuario {

	public static boolean existe(String nombreJugador) throws Exception {
		MongoBroker broker=MongoBroker.get();
		BsonDocument criterio=new BsonDocument();
		criterio.append("email", new BsonString(nombreJugador));
		MongoClient conexion=broker.getConexionPrivilegiada();
		MongoDatabase db=conexion.getDatabase("LaOca2018");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument usuario=usuarios.find(criterio).first();
		return usuario!=null;
	}
	
	
	public static void insert(Usuario usuario, String pwd) throws Exception {
		BsonDocument bUsuario=new BsonDocument();
		bUsuario.append("email", new BsonString(usuario.getLogin()));
		bUsuario.put("pwd", new BsonString(pwd));
		bUsuario.put("victorias", new BsonInt32(0));
	
		MongoClient conexion=MongoBroker.get().getConexionPrivilegiada();
		MongoCollection<BsonDocument> usuarios = 
				conexion.getDatabase("LaOca2018").getCollection("usuarios", BsonDocument.class);
		try {
			if(existe(usuario.getLogin())==false) {
				usuarios.insertOne(bUsuario);
			}else {
				throw new Exception("Â¿No estarÃ¡s ya registrado, chaval/chavala?");
			}
		}
		catch (MongoWriteException e) {
			if (e.getCode()==11000)
			throw new Exception("Ha pasado algo muy malorrr");
		}
		
		conexion.close();

		
	}



	public static Usuario login(String email, String pwd) throws Exception {
			
			MongoClient conexion=MongoBroker.get().getConexionPrivilegiada();		
			BsonDocument bUsuario=new BsonDocument();
			bUsuario.append("email", new BsonString(email));
			bUsuario.append("pwd", new BsonString(pwd));
			MongoCollection<BsonDocument> usuarios=
					conexion.getDatabase("LaOca2018").getCollection("usuarios", BsonDocument.class);
			FindIterable<BsonDocument> resultadoUsuario = usuarios.find(bUsuario);
			 
			Usuario usuario=null;
			if (resultadoUsuario.first()!=null ) {
				usuario=new UsuarioRegistrado();
				usuario.setNombre(email);
			} else {
				throw new Exception("Error al hacer login");
			}
			conexion.close();
			return usuario;
		}
		

	

	public static boolean cambiarContrasena(String email, String pwdvieja, String pwd1) {
		boolean cambiado = false;
		MongoClient conexion=MongoBroker.get().getConexionPrivilegiada();
		BsonDocument criterioActualizacion=new BsonDocument();
		BsonDocument criterio=new BsonDocument();
		criterioActualizacion.append("email", new BsonString(email));
		criterioActualizacion.append("pwd", new BsonString(pwdvieja));
		criterio.append("pwd", new BsonString(pwd1));
		MongoCollection<BsonDocument> usuarios=
				conexion.getDatabase("LaOca2018").getCollection("usuarios", BsonDocument.class);
		FindIterable<BsonDocument> resultadoUsuario = usuarios.find(criterioActualizacion);
		try {
			if(resultadoUsuario.first()!=null) {
				usuarios.updateOne(criterioActualizacion, new BsonDocument("$set",criterio));
				cambiado=true;
			}else {
				throw new Exception("La contraseña vieja no coinciden");
				
			}
			
		} catch (Exception e) {
			System.out.println("La contraseña vieja no coinciden");
		}
		
		conexion.close();
		return cambiado;
	}
	


	public static void actualizarVictorias(String email) throws Exception {
		if(existe(email)) {
			MongoClient conexion=MongoBroker.get().getConexionPrivilegiada();
			
			BsonDocument criterioActualizacion=new BsonDocument();
			BsonDocument criterio=new BsonDocument();
			BsonDocument criterioBuscarVictorias=new BsonDocument();
			criterioBuscarVictorias.append("email", new BsonString(email));
			criterioActualizacion.append("email", new BsonString(email));
			MongoCollection<BsonDocument> usuarios=
					conexion.getDatabase("LaOca2018").getCollection("usuarios", BsonDocument.class);
			int vic=0;
			FindIterable<BsonDocument> buscarVic= usuarios.find(criterioBuscarVictorias);
			if(buscarVic!=null) {
				String victorias=buscarVic.first().get("victorias").toString();
				victorias=victorias.split("=")[1].split("}")[0];
				System.out.println(victorias);
				vic=Integer.parseInt(victorias);
				

			}

			criterio.append("victorias", new BsonInt32(vic+1));
			usuarios.updateOne(criterioActualizacion, new BsonDocument("$set",criterio));
			
			
			conexion.close();
			
		}
		
	}


	public static String ranking() {
		// TODO Auto-generated method stub
		JSONObject jso=new JSONObject();
		
		MongoClient conexion=MongoBroker.get().getConexionPrivilegiada();
		MongoCollection<BsonDocument> usuarios=
				conexion.getDatabase("LaOca2018").getCollection("usuarios", BsonDocument.class);
		int ki=0;
		try (MongoCursor <BsonDocument> cursor = usuarios.find().iterator()) {
		    while (cursor.hasNext()) {
		    	ki+=1;
		    	jso.put("user_"+Integer.toString(ki), cursor.next().toString());
		    }
		}	
		
		System.out.println(jso);
		return jso.toString();
	}
	

}