// JavaScript Document

// Jesus Rodrigo Corrêa
// Analista de Sistemas
// Goiânia - Go - Brasil
// Dezembro de 2006
// g284155723@gmail.com

function MascararHora(campo) {
	var vr= campo.value;
	var vr1="";

	if (vr.length>=2){
		while(vr.indexOf(":",0)!=-1) vr = vr.replace( ":", "" );		
		for( i=0; i<=vr.length;  i+=2) {
			//alert("vr = " + vr);			
			vr1+= vr.substr(0,2) + ":"; 
			vr = vr.substr(2);		
		}
		campo.value = vr1 + vr;
	}	

}

