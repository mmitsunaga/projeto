// JavaScript Document

// Jesus Rodrigo Corrê
// Analista de Sistemas
// Goiânia - Go - Brasil
// Dezembro de 2006

function MascaraValor(campo) {
	var vr= campo.value;
	var vr1="";
	var vr2="";
	
	if (vr.length==1) vr = "0,0" + vr;
		else if (vr.length>=3) { 
			//retiro todos as virgulas
			while(vr.indexOf(",",0)!=-1) vr = vr.replace( ",", "" );
			//retiro todos os pontos
			while(vr.indexOf(".",0)!=-1) vr = vr.replace( ".", "" );
			
			//alert("vr.substr(0,1) = " + vr.substr(0,1));			
			if (vr.substr(0,1)=="0") vr = vr.substr(1);
			if (vr.length==2) vr = "0," + vr;
				else {
					//alert("vr = " + vr);			
					vr1="," + vr.substr(vr.length-2,vr.length);
					vr = vr.substr(0,vr.length-2);
					//alert("vr = " + vr + " vr1 = " + vr1 );
					if (vr.length>=4) {
						for( i=vr.length; (i-3)>0; i-=3) {
	//						alert("vr = " + vr);			
							vr2="." + vr.substr(i-3,i) + vr2; 
							//alert("vr.substr(0,i-3) = " + vr.substr(0,i-3));
							vr = vr.substr(0,i-3);
						}
						vr2= vr + vr2;
						vr="";
					}
				}
	}
	
	campo.value = vr2 + vr + vr1;
}

