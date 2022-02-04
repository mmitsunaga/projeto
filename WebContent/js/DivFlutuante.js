// JavaScript Document

	var posXi=0;
	var posYi=0;
	var mover=false;

	function DivFlutuanteMover(event, id){
		var obj= document.getElementById(id);
		var x =	event.pageX;
		var y = event.pageY;
		var xAgora = new String(obj.style.left);
		var yAgora = new String(obj.style.top);
		var xNovo = 0;
		var yNovo = 0;
		xAgora = xAgora.replace("px","");
		yAgora = yAgora.replace("px","");
		//	alert(xAgora);
		//	alert(yAgora);
		//	alert(parseInt(xAgora) + parseInt(yAgora));
		if (mover==true){
				obj.style.top = (parseInt(yAgora) + (parseInt(y) - parseInt(posYi) )) + "px" ;
				obj.style.left = (parseInt(xAgora) + ( parseInt(x) - parseInt(posXi) ))  + "px";
				posXi = x;
				posYi = y;
		}

	}
	
	function DivFlutuanteDown( event , objeto){
		var obj= document.getElementById(objeto);
		mover = true;
		posXi =	event.pageX;
		posYi = event.pageY;
		obj.style.cursor="move";
	}
	
	function DivFlutuanteUp(objeto){
		var obj= document.getElementById(objeto);
		mover = false;
		obj.style.cursor="default";
	}
	
	function DivFlutuanteValoresIniciais(titulo, mensagem, altura, largura, top, left, objeto){
		var obj= document.getElementById("Informe");
		obj.style.height = altura + "px";
		obj.style.width = largura + "px";
		
		if (objeto != '') {
			obj.style.top = objeto.y + "px";
			obj.style.left = (objeto.x - largura) + "px";
		} else {
			obj.style.top = top + "px";
			if (left != ''){
				obj.style.left = left + "px";
			} else {
				obj.style.left = ((document.width / 2) - 180) + "px";
			}
		}
		
		var objTitulo = document.getElementById("Titulo");
		objTitulo.innerHTML = titulo;
		var objCorpo = document.getElementById("Corpo");
		objCorpo.innerHTML = mensagem;
		if ((mensagem!='') && (mensagem!='null')) obj.style.display="block";
	}
	
	function DivFlutuanteFechar(objeto){
		var obj= document.getElementById(objeto);
//		var obj1= document.getElementById("Corpo");
		obj.style.display="none";
//		obj1.style.display="none";
	}
	
	function DivFlutuanteEsconder(objeto, objeto1){
		var obj= document.getElementById(objeto);
		var obj1= document.getElementById(objeto1);
		if (obj.style.height == "200px"){
			obj.style.height = "22px";
			obj1.style.display="none";
//			document.getElementById("Titulo").style.color="#F8F8F8";
		}else{
				obj.style.height = "200px";
				obj1.style.display="block";
//				document.getElementById("Titulo").style.color="#333333";
			}
	}