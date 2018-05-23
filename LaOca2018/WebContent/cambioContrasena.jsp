<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
		JSONObject respuesta=new JSONObject();
		String p=request.getParameter("p");
		JSONObject jso=new JSONObject(p);
		try {
			String email=jso.optString("email");
			String pwd1=jso.optString("pwd1");
			String pwd1Nueva=jso.optString("pwd1Nueva");
			String pwd2Nueva=jso.optString("pwd2Nueva");
			comprobarCredenciales(email, pwd1Nueva, pwd2Nueva);
			if(Manager.get().cambiarContrasena(email, pwd1,pwd1Nueva)==true){
				respuesta.put("result", "OK");
			}else{
				respuesta.put("result", "ERROR");
			}
			
		}
		catch (Exception e) {
			respuesta.put("result", "ERROR");
			respuesta.put("mensaje", e.getMessage());
		}
	
	out.println(respuesta.toString());
%>

<%!
private void comprobarCredenciales(String email, String pwd1Nueva, String pwd2Nueva) throws Exception {
	if (email.length()==0)
		throw new Exception("El email no puede ser vacío");
	
	if (!pwd1Nueva.equals(pwd2Nueva))
		throw new Exception("Las contraseñas no coinciden");
	if (pwd1Nueva.length()<4)
		throw new Exception("La contraseña tiene que tener 4 caracteres por lo menos");
}
%>