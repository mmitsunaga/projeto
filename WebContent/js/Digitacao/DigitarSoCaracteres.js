// JavaScript Document

// Jesus Rodrigo Corr�a
// Analista de Sistemas
// Goi�nia - Go - Brasil
// Dezembro de 2006
// g284155723@gmail.com

function DigitarSoCaracteres(campo, event ) { 
  
	var BACKSPACE= 8; 
	var tecla; 
	var key;
	//caracteres v�lidos
	var strValidos = "qwertyuiopasdfghjkl�zxcvbnmQWERTYUIOPASDFGHJKL�ZXCVBNM��������������������������������������������";
	
	if(navigator.userAgent.indexOf('IE 5')>0 ||	navigator.userAgent.indexOf('IE 6')>0) 
		tecla= event.keyCode;
	 else tecla= event.which;      
	
	key = String.fromCharCode( tecla); 
	
  	alert( 'key: ' + tecla + ' -> campo: ' + campo.value); 

	if ( tecla == 13 )  return  false; 
		else if ( tecla == 0 )  return  true; 
			else if ( tecla == BACKSPACE ) return true; 
						 else if( strValidos.indexOf(key) != -1) {
								 return  true ;							 
							} else return  false ;							 
	  
} 
