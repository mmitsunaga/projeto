// JavaScript Document

// Henrique Godinho
// Analista de Sistemas
// Goiânia - Go - Brasil
// Novembro de 2013
// henrique.godinho@gmail.com

function DigitarNomeParteProcesso(campo, event ) { 

	var BACKSPACE= 8; 
	var tecla; 
	var key;
	//caracteres inválidos
	var strInvalidos = "\""; 
	
	if(navigator.userAgent.indexOf('IE 5')>0 ||	navigator.userAgent.indexOf('IE 6')>0) 
		tecla= event.keyCode;
	 else tecla= event.which;      
	
	key = String.fromCharCode(tecla); 
	
	if ( tecla == 13 )  return  false; 
		else if ( tecla == 0 )  return  true; 
			else if ( tecla == BACKSPACE ) return true; 
						 else if( strInvalidos.indexOf(key) != -1) {
								 return  false ;							 
							} else return true ;							 
	  
} 
