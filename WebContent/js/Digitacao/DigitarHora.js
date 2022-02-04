// JavaScript Document

// Jesus Rodrigo Corrêa
// Analista de Sistemas
// Goiânia - Go - Brasil
// Dezembro de 2006
// g284155723@gmail.com

function DigitarHora(campo, event ) { 
  
	var BACKSPACE= 8; 
	var tecla; 
	var loTamanho;
	var key;
	var horas="012";
	var horas2="0123";	
	var minutos ="012345";
	var todos="0123456789";

	if(navigator.userAgent.indexOf('IE 5')>0 ||	navigator.userAgent.indexOf('IE 6')>0) 
		tecla= event.keyCode;
	 else tecla= event.which;      
	
	key = String.fromCharCode( tecla); 
	
  	//alert( 'key: ' + tecla + ' -> campo: ' + campo.value); 

	if ( tecla == 13 )  return  false; 
		else if ( tecla == 0 )  return  true; 
			else if ( tecla == BACKSPACE ) return true; 			
						 else  if(todos.indexOf(key)!=-1){
									if( campo.value.length==0) {
										if (horas.indexOf(key)!=-1) return true; else return false;
									}else if(campo.value.length==1) {
												if (campo.value=="2"){														
													if (horas2.indexOf(key)!=-1) return true; else return false;
												}else  return true; 
											} else if ((campo.value.length==4) || (campo.value.length==7)) return true;
											        else if (minutos.indexOf(key)!=-1) return true; else return false;
								} else return false;																		 
} 
