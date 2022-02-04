// JavaScript Document

// Henrique Godinho
// Analista de Sistemas
// Goi�nia - Go - Brasil
// Janeiro de 2012
// Alterado em 12/07/2017 por M�rcio Gomes

var ctrlDown = false;
var ctrlKey = 17;
var cmdKey = 91;

document.addEventListener("keydown", function(e){
  if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = true;
}); 

document.addEventListener("keyup", function(e){
  if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = false;
}); 

function ValidarColar(event) {
	if (event) {
		if (event.clipboardData) {
			var textoColado = event.clipboardData.getData("text");	
			if (textoColado) {
				if (!ValidarNumeroProcesso(textoColado)) {
					alert("número inválido!");					
				}
			}
		}
	}
}

function ValidarNumeroProcesso(numeroPontoTraco) {
	var valido = true;
	var possuiPontoTraco = false;
	var strValidChars = "0123456789.- ";
	
	if (numeroPontoTraco) {
		if (numeroPontoTraco.length > 0) {
			for (i = 0; i < numeroPontoTraco.length && valido == true; i++) { 
				Char = numeroPontoTraco.charAt(i); 
				if (strValidChars.indexOf(Char) == -1) {
					valido = false;
				}
				if (Char == "." || Char == "-") {
					if (possuiPontoTraco) {
						valido = false;
					} else {
						possuiPontoTraco = true;
					}
				}
			}
		}
	}
	
	return valido;
}

function DigitarNumeroProcesso(campo, event) { 

	var BACKSPACE = 8;
	var PONTO = 46;
	var TRACO = 45;
	var NULL = 0;
	var CR = 13;
	var VKEY = 86;
	var vKEY = 118;
	var CKEY = 67;
	var cKEY = 99;
	var strNumeros = "0123456789";
	var strPonto = ".";
	
	var tecla = ( window.event ) ? event.keyCode : event.which;
	var texto = campo.value
    var key = String.fromCharCode(tecla);
	
    if (tecla == CR) return  false;
	
	if (tecla == NULL) return  true;
	
	if (tecla == BACKSPACE) return true; 
	
	if (strNumeros.indexOf(key) != -1) return true;
	
	if (tecla == PONTO || tecla == TRACO) {
		if (campo.value == "") {
			return false;
		}
		if (campo.value.endsWith(".") || campo.value.endsWith("-")) {
			return false;
		}
		return true;
	}
	
	if (ctrlDown && (tecla == VKEY || tecla == vKEY || tecla == CKEY || tecla == cKEY)) return true;
	
	return false;
}