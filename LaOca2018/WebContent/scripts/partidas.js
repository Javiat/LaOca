function crearPartida() {
	var request = new XMLHttpRequest();	
	request.open("post", "crearPartida.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK") {
				console.log("Creación de partida (" + respuesta.mensaje + ") solicitada");
				conectarWebSocket();
			} else {
				console.log("Error: " + respuesta.mensaje);
			}				
		}
	};
	var p = {
		nombre : document.getElementById("nombre").innerHTML,
		numeroDeJugadores : document.getElementById("numero").value
	};
	request.send("p=" + JSON.stringify(p));
	
}

function unirse() {
	var request = new XMLHttpRequest();	
	request.open("post", "llegarASalaDeEspera.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=request.responseText;
			console.log(respuesta);
			conectarWebSocket();
		}else{
			console.log("Error");
		}
	};
	var p = {
		nombre : document.getElementById("nombre").innerHTML
	};
	request.send("p=" + JSON.stringify(p));
}


var ws;
function conectarWebSocket() {
	ws=new WebSocket("ws://localhost:8080/LaOca2017/servidorDePartidas");
	
	ws.onopen = function() {
	}
	
	ws.onmessage = function(datos) {
		var mensaje=datos.data;
		mensaje=JSON.parse(mensaje);
		if (mensaje.tipo=="DIFUSION") {
            var mensaje=document.getElementById("partida");
            mensaje.value+="Has entrado a una partida, espera que lleguen los jugadores.\n";
            var btnUnirse = document.getElementById("btnUnirse");
 			var btnCrear = document.getElementById("btnCrear");
 			btnUnirse.disabled=true;
 			btnCrear.disabled=true;   
		} 
		else if(mensaje.tipo=="COMIENZO"){
			comenzar(mensaje);
            
		}
		else if(mensaje.tipo=="TIRADA"){
			tirada(mensaje);
		}
		
	}
	ws.onclose=function(){
		addMensaje("Websocket cerrado");
	}
	
}
var idPartida;
function comenzar(mensaje){
	
    var mensaje2=document.getElementById("partida");
    jugadorTurno=mensaje.jugadorConElTurno;
    mensaje2.value+="Es el turno de: "+jugadorTurno+"\n";
	
	if(sessionStorage.email==jugadorTurno){
		
		var botonEnviar = document.getElementById("lanzarDado");
		botonEnviar.disabled=false;
	}
	 var numerojugadores=mensaje.numerojugadores;
	idPartida=mensaje.idPartida; //meter en el areatext de partida
	
}
function tirada(mensaje){
	
	var mensaje3=document.getElementById("partida");
	var casillaOrigen=mensaje.casillaOrigen;
	var mensajeAdicional=mensaje.mensajeAdicional;
	var numeroDelDado=mensaje.dado;
	var destinoFinal=mensaje.destinoFinal;
	var destinoInicial=mensaje.destinoInicial;
	var jugadorQueMueve=mensaje.jugador;
	var botonDado = document.getElementById("lanzarDado");
	if(mensaje.jugadorConElTurno!=jugadorQueMueve){
		botonDado.disabled=true;
		jugadorTurno=mensaje.jugadorConElTurno;
	}
	if(sessionStorage.email==jugadorTurno){
		var botonDado = document.getElementById("lanzarDado");
		botonDado.disabled=false;
	}
	
	mensaje3.value+="Destino inicial: "+destinoInicial+" DestinoFinal: "+destinoFinal+".\n\n";

	if(mensaje.mensaje!=null){
		mensaje3.value+=mensaje.mensaje+"\n";
		/*if(mensaje3.value == jugadorQueMueve+ " cae en la muerte"){
			document.getElementById(jugadorQueMueve).remove();
		}*/
	}
	if(mensajeAdicional!=null){
		mensaje3.value+=mensajeAdicional+"\n\n";
	}
	if(jugadorTurno!=null){
		mensaje3.value+="Es el turno del usuario: "+ jugadorTurno+".\n\n";
	}
	
	if(mensaje.ganador!=null){
		mensaje3.value+="El ganador es: "+mensaje.ganador+"\n\n";
		botonDado.disabled=true;

	}
}

function addMensaje(texto) {
	console.log(texto);
}

function lanzarDado(){
	var p = {
	        tipo: "dadoselanza",	
	        nombreJugador: document.getElementById("nombre").innerHTML,
	        puntos: getRandomInt(1, 6),
	        idPartida: idPartida
	    };
	   ws.send(JSON.stringify(p));
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
