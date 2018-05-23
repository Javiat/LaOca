package edu.uclm.esi.tysweb.laoca.dominio;

import java.sql.Date;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class UsuarioRegistrado extends Usuario {
	private int partidasGanadas;
	private int partidasPerdidas;
	
	public UsuarioRegistrado() {
		super();
		this.partidasGanadas=0;
	}
	public void actualizarVictorias() throws Exception {
		this.partidasGanadas++;
		DAOUsuario.actualizarVictorias(super.login);
	}

	public static Usuario login(String email, String pwd) throws Exception {
		return DAOUsuario.login(email, pwd);
	}

	public static boolean cambiarContrasena(String email,String pwdvieja, String pwdnueva) {
		return DAOUsuario.cambiarContrasena(email, pwdvieja,pwdnueva);
	}
	
	public static String ranking() {
		// TODO Auto-generated method stub
		return DAOUsuario.ranking();
	}
}
