// JavaScript Document

// Jesus Rodrigo Correa
// Analista de Sistemas
// Goiania - Go - Brasil
// Dezembro de 2006
// g284155723@gmail.com
// Alterado em 17/04/2018 por Marcio Gomes

var ctrlDown = false;

var ctrlDown = false;
var ctrlKey = 17;
var cmdKey = 91;

document.addEventListener("keydown", function(e){
  if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = true;
}); 

document.addEventListener("keyup", function(e){
  if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = false;
}); 

function DigitarSoNumero(campo, event ) { 
  
	var BACKSPACE = 8;
	var PONTO = 46;
	var NULL = 0;
	var CR = 13;
	var VKEY = 86;
	var vKEY = 118;
	var CKEY = 67;
	var cKEY = 99;
	var strNumeros = "0123456789";
	
	var tecla = ( window.event ) ? event.keyCode : event.which;
	var texto = campo.value    
    var key = String.fromCharCode(tecla);
	
    if (tecla == CR) return  false;
	
	if (tecla == NULL) return  true;
	
	if (tecla == BACKSPACE) return true; 
	
	if (strNumeros.indexOf(key) != -1) return true;
	
	if (ctrlDown && (tecla == VKEY || tecla == vKEY || tecla == CKEY || tecla == cKEY)) return true;
	
	return false;			
} 
