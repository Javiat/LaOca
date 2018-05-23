function registrar() {
	var request = new XMLHttpRequest();
	request.open("post", "registrar.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				
				window.location.href = "index.html";
			} else
				mensajeRegistro.innerHTML = respuesta.mensaje;
		}
	};
	var p = {
		email : email.value,pwd1 : pwd1.value,pwd2 : pwd2.value
	};
	request.send("p=" + JSON.stringify(p));
}
function cambiarPass() {
	var request = new XMLHttpRequest();
	request.open("post", "cambioContrasena.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				window.location.href = "panel.html";
			} else
				console.log(respuesta.mensaje);
				mensajePassword.innerHTML="No se ha podido cambiar la password";
		}
	};
	var p = {
		email : sessionStorage.getItem('email'),
		pwd1 : pwd1.value,
		pwd1Nueva : pwd1Nueva.value,
		pwd2Nueva : pwd2Nueva.value
	};
	request.send("p=" + JSON.stringify(p));
}


function login() {
	var request = new XMLHttpRequest();
	request.open("post", "login.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				if (respuesta.login == "si") {
					//El usuario esta registrado
					crearCookie();
					sessionStorage.setItem('email', email.value);
					window.location.href = "panel.html";
				} else {
					//Redirigo el usuario a una partida
					sessionStorage.setItem('email', email.value);
					window.location.href = "Tablero.html";

				}

			} else
				mensajeLogin.innerHTML = respuesta.mensaje;
		}
	};
	var p = {
		email : email.value,pwd1 : pwd1.value
	};
	request.send("p=" + JSON.stringify(p));
}



function ranking() {
	var request = new XMLHttpRequest();
	request.open("post", "ranking.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	var array;

	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			var numero = respuesta.numero;
			var i = 0;
			
			var table = document.getElementById("ranking")
					.getElementsByTagName('tbody')[0];
			for ( var i in respuesta) {

				var obj = respuesta[i];
				var re = JSON.parse(obj);

				var row = table.insertRow(table.rows.lenght);
				var celda1 = row.insertCell(0);
				var newText = document.createTextNode(re.email);
				celda1.appendChild(newText);
				var celda2 = row.insertCell(1);
				var newText2 = document.createTextNode(re.victorias);
				celda2.appendChild(newText2);
			}
		}
	};
	var p = {
		email : sessionStorage.getItem('email')
	};
	request.send("p=" + JSON.stringify(p));
}
function crearCookie() {
	var email = document.getElementById("email").value;
	var pwd = document.getElementById("pwd1").value;
	pwd = window.btoa(pwd);
	var diasexpiracion = 30;
	var d = new Date();
	d.setTime(d.getTime() + (diasexpiracion * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toUTCString();
	document.cookie = email + "=" + pwd + "; " + expires;
}
function getDatos() {
	var email = document.getElementById("email").value;
	document.getElementById("pwd1").value = obtenerCookie(email);
}
function obtenerCookie(clave) {
	var name = clave + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1);
		if (c.indexOf(name) == 0)
			return atob(c.substring(name.length, c.length));
	}
	return "";
}