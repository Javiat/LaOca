<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	JSONObject respuesta=new JSONObject();
	try {
		String email=jso.optString("email");
		String pwd=jso.optString("pwd1");
		
		Usuario usuario=Manager.get().login(email, pwd);
		session.setAttribute("usuario", usuario);
		respuesta.put("result", "OK");
		boolean existe=Manager.get().existe(usuario.getLogin());
		if(existe==true){
			respuesta.put("login","si");
		}else{
			respuesta.put("login","no");

		}
		
	}
	catch (Exception e) {
		
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>









