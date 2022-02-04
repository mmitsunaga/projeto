var tecladoAlfanumerico = new tecladoVirtual('./imagens/TecladoVirtual.png');

var input = null;
var focusedField = 'Senha';
var restrictedFields = new Array();
var minLeft,maxLeft,minTop,maxTop;
var isDebugging = true;
var ie=document.all
var ns6=document.getElementById&&!document.all
var ns4=document.layers;
var capsLockOn = false;
var isShifted = false;
var retiraShift = false;
			
	function mostraTecladoVirtual(){
		restrictedFields[0] = document.getElementsByName('Senha')[0];
		preparaInputs();
		reloadPage(true);
		mostrarTecladoVirtual();

	}

	tecladoAlfanumerico.tecla = new Array();
	
	tecladoAlfanumerico.tecla[1] = new Tecla('"','"', '9,9,26,27');
	tecladoAlfanumerico.tecla[2] = new Tecla('!','!', '29,9,46,27');
	tecladoAlfanumerico.tecla[3] = new Tecla('@','@', '49,9,66,27');	
	tecladoAlfanumerico.tecla[4] = new Tecla('#','#', '69,9,86,27');	
	tecladoAlfanumerico.tecla[5] = new Tecla('$','$', '89,9,106,27');	
	tecladoAlfanumerico.tecla[6] = new Tecla('%','%', '109,9,126,27');	
	tecladoAlfanumerico.tecla[7] = new Tecla('&','&', '129,9,146,27');	
	tecladoAlfanumerico.tecla[8] = new Tecla('*','*', '149,9,166,27');		
	tecladoAlfanumerico.tecla[9] = new Tecla('(','(', '169,9,186,27');		
	tecladoAlfanumerico.tecla[10] = new Tecla(')',')', '189,9,206,27');		
	
	tecladoAlfanumerico.tecla[11] = new Tecla('1','1', '9,28,26,46');
	tecladoAlfanumerico.tecla[12] = new Tecla('2','2', '29,28,46,46');
	tecladoAlfanumerico.tecla[13] = new Tecla('3','3', '49,28,66,46');
	tecladoAlfanumerico.tecla[14] = new Tecla('4','4', '69,28,86,46');
	tecladoAlfanumerico.tecla[15] = new Tecla('5','5', '89,28,106,46');
	tecladoAlfanumerico.tecla[16] = new Tecla('6','6', '109,28,126,46');
	tecladoAlfanumerico.tecla[17] = new Tecla('7','7', '129,28,146,46');
	tecladoAlfanumerico.tecla[18] = new Tecla('8','8', '149,28,166,46');
	tecladoAlfanumerico.tecla[19] = new Tecla('9','9', '169,28,186,46');
	tecladoAlfanumerico.tecla[20] = new Tecla('0','0', '189,28,206,46');
	
	tecladoAlfanumerico.tecla[21] = new Tecla('a','a', '9,47,26,65');
	tecladoAlfanumerico.tecla[22] = new Tecla('b','b', '29,47,46,65');
	tecladoAlfanumerico.tecla[23] = new Tecla('c','c', '49,47,66,65');
	tecladoAlfanumerico.tecla[24] = new Tecla('d','d', '69,47,86,65');
	tecladoAlfanumerico.tecla[25] = new Tecla('e','e', '89,47,106,65');
	tecladoAlfanumerico.tecla[26] = new Tecla('f','f', '109,47,126,65');
	tecladoAlfanumerico.tecla[27] = new Tecla('g','g', '129,47,146,65');
	tecladoAlfanumerico.tecla[28] = new Tecla('h','h', '149,47,166,65');
	tecladoAlfanumerico.tecla[29] = new Tecla('i','i', '169,47,186,65');
	tecladoAlfanumerico.tecla[30] = new Tecla('j','j', '189,47,206,65');

	tecladoAlfanumerico.tecla[31] = new Tecla('k','k', '9,66,26,84');	
	tecladoAlfanumerico.tecla[32] = new Tecla('l','l', '29,66,46,84');
	tecladoAlfanumerico.tecla[33] = new Tecla('m','m', '49,66,66,84');
	tecladoAlfanumerico.tecla[34] = new Tecla('n','n', '69,66,86,84');
	tecladoAlfanumerico.tecla[35] = new Tecla('o','o', '89,66,106,84');
	tecladoAlfanumerico.tecla[36] = new Tecla('p','p', '109,66,126,84');
	tecladoAlfanumerico.tecla[37] = new Tecla('q','q', '129,66,146,84');
	tecladoAlfanumerico.tecla[38] = new Tecla('r','r', '149,66,166,84');
	tecladoAlfanumerico.tecla[39] = new Tecla('s','s', '169,66,186,84');
	tecladoAlfanumerico.tecla[40] = new Tecla('t','t', '189,66,206,84');

	tecladoAlfanumerico.tecla[41] = new Tecla('u','u', '9,85,26,103');
	tecladoAlfanumerico.tecla[42] = new Tecla('v','v', '29,85,46,103');
	tecladoAlfanumerico.tecla[43] = new Tecla('w','w', '49,85,66,103');
	tecladoAlfanumerico.tecla[44] = new Tecla('x','x', '69,85,86,103');
	tecladoAlfanumerico.tecla[45] = new Tecla('y','y', '89,85,106,103');
	tecladoAlfanumerico.tecla[46] = new Tecla('z','z', '109,85,126,103');
	tecladoAlfanumerico.tecla[47] = new Tecla('capsLock','null', '129,85,166,103');
	tecladoAlfanumerico.tecla[48] = new Tecla('shift','null', '169,85,186,103');	
	tecladoAlfanumerico.tecla[49] = new Tecla('backSpace','null', '189,85,206,103');
	

	function tecladoVirtual(pImg){
		this.pathImg = pImg;
		this.getPathImagem = function(){return pathImg;};
		this.setPathImagem = function(x){pathImg = x; document.getElementById("imgTecladoVirtual").src=pathImg;};
	}

	function Tecla(i, v, c){
		this.id_Tecla = i;
		this.getId = function(){return this.id_Tecla;};
		this.valor_Tecla = new String(v);
		this.getValor = function(){return this.valor_Tecla;};
		this.setValor = function(x){this.valor_Tecla = x;};
		this.coordenada_Tecla = c;
		this.getCoordenada = function(){return this.coordenada_Tecla;};
		this.setCoordenada = function(x){this.coordenada_Tecla = x;};
	}

	function setInputElement(elemento){
		if(focusedField == ''){
			input = elemento;
		}
	}

	function getInputElement(){
		return input;
	}

	function preparaInputs(){
		if(focusedField != ''){
			input = document.getElementsByName(focusedField)[0];
		}else{
			for(i = 0; i < document.forms.length; i++){
				for(j = 0; j < document.forms[i].elements.length; j++){
					if(document.forms[i].elements[j].type == 'text' || document.forms[i].elements[j].type == 'password'){
						document.forms[i].elements[j]._onfocus_ = document.forms[i].elements[j].onfocus;
						document.forms[i].elements[j].onfocus = function(){
							input = this;
							if(this._onfocus_)
								this._onfocus_();
						}
					}
				}
			}
		}
		for(i = 0; i < restrictedFields.length; i++){
		    if (typeof(restrictedFields[i]) == "object") {
			    restrictedFields[i].oldValue = '';
	        }
		}
		for(i = 0; i < document.forms.length; i++){
			formulario = document.forms[i];
			formulario._onreset_ = formulario.onreset;
			formulario.onreset = function(){
				for(i = 0; i < restrictedFields.length; i++){
					restrictedFields[i].oldValue = '';
				}
				this._onreset_();
			}
		}
	}

	function reloadPage(init) {
	    if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
		    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight;
		    onresize=MM_reloadPage; }}
		else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH)
		    location.reload();
	}

	function tecladoVirtualAlfaBranco_MM_findObj(n, d) { //v4.01
	    var p,i,x; if(!d) d=document;
	    if((p=n.indexOf("?"))>0&&parent.frames.length) {
	    	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
	    }
	    if(!(x=d[n])&&d.all) x=d.all[n];
	    	for (i=0;!x&&i<d.forms.length;i++)
			    x=d.forms[i][n];
	    for(i=0;!x&&d.layers&&i<d.layers.length;i++)
	        x=tecladoVirtualAlfaBranco_MM_findObj(n,d.layers[i].document);
	    if(!x && d.getElementById)
	    	x=d.getElementById(n);
	   	return x;
	}

	function debug(l){
		if(isDebugging){
			divDebug = document.getElementById('debug');
			if(divDebug != null)
				divDebug.innerHTML += l + ' - ';
		}
	
	}

	function Adiciona(valor) {
		if(input != null){
			var valorCampo = input.value;
			if(valorCampo.length < input.maxLength || input.maxLength <= 0){
				input.value = valorCampo + valor;
				debug('add '+ input.oldValue + ' ' + valor);
			}
		}
		Focaliza();
	}

	function Atualiza(){
		if(input != null)
			input.oldValue = input.value;
	}
	
	function Focaliza(){
		if(input != null){
		    window.setTimeout("input.focus();",100);
		}else{
		}
	}

	function tecladoVirtualAlfaBranco_Limpar() {
	    input.value = '';
	    Focaliza();
	}
	
	function tecladoVirtualAlfaBranco_VerificaTecla() {
		var key;
		if(ie)
			key = event.keyCode
		else if (ns6)
			key = e.which
		else if (ns4) {
			key = e.which
			if(key == prevKey) {
				key = 0;
				prevKey=-100;
			}
			else {
				prevKey=key;
			}
		}
	}

	function GuardaTecla(tecla) {
	    document.form.tecla.value = tecla;
	}
	
	function LimpaTecla() {
	    document.form.tecla.value = "";
	}
	
	function tecladoVirtualAlfaBranco_hiddenTeclado() {
		var divRef = tecladoVirtualAlfaBranco_getRefDiv();
		divRef.style.visibility = 'hidden';
	}

	function tecladoVirtualAlfaBranco_hiddenTabela(idTabela, oper) {
	    var el;
	    var i;
	    var d = document;
	
	    if(d.getElementById) {
	        // Netscape > 6:
	        el = d.getElementById(idTabela);
	        el.style.visibility = (oper) ? "visible" : "hidden";
	    } else {
	        if (d.layers) {
	            // Netscape < 6:
	            for (i=0; i<d.layers.length; i++) {
	                if (d.layers[i].id == idTabela) {
	                    d.layers[i].visibility = (oper) ? "show" : "hide";
	                }
	            }
	        } else {
	            // IE:
	            eval(idTabela + ".style.visibility='" + ((oper) ? "visible" : "hidden") + "';");
	        }
	    }
	}

	function BeforeClick(tecla){
		if (tecla.getId() == "shift") {
		    if (isShifted) {
		        tecladoAlfanumerico.setPathImagem('./imagens/TecladoVirtual.jpg');
		    } else {
				tecladoAlfanumerico.setPathImagem('./imagens/TecladoVirtualMaiusculo.png');
			}
			isShifted = !isShifted;
		} else if(tecla.getId() == "capsLock"){
			capsLockOn = !capsLockOn;
			tecladoMudaCase_tecladoBranco();
			if(capsLockOn){
				tecladoAlfanumerico.setPathImagem('./imagens/TecladoVirtualMaiusculo.png');
			}else{
				tecladoAlfanumerico.setPathImagem('./imagens/TecladoVirtual.png');
			}
		} else if(tecla.getId() == "backSpace"){
			getInputElement().value = getInputElement().value.substring(0,getInputElement().value.length-1);
			getInputElement().focus();
		} else if (isShifted) {
			if (capsLockOn) {
				tecla.setValor(tecla.getValor().toLowerCase());
			} else {
				tecla.setValor(tecla.getValor().toUpperCase());
	            tecladoAlfanumerico.setPathImagem('./imagens/TecladoVirtual.png');
		    }
			isShifted = false;
			retiraShift = true;
		}
	}

	function tecladoMudaCase_tecladoBranco(upper){
		for(var i = 1; i < tecladoAlfanumerico.tecla.length; i++){
			var tecla = tecladoAlfanumerico.tecla[i];
			if(tecla.getValor() != null){
				if(capsLockOn)
					tecla.setValor(tecla.getValor().toUpperCase());
				else
					tecla.setValor(tecla.getValor().toLowerCase());
			}
		}
	}
	//evento apos onclick
	function AfterClick(tecla){
		if(retiraShift){
			if(capsLockOn)
				tecla.setValor(tecla.getValor().toUpperCase());
			else
				tecla.setValor(tecla.getValor().toLowerCase());
			retiraShift = false;
		}
		var id = tecla.getId();
		if(id != 'shift' && id != 'capsLock' && id != 'backSpace'){
			pulaCampo(getInputElement());
		}
	}

	function pulaCampo(elemento){
		if(elemento.maxLength == elemento.value.length){
			var form = elemento.form;
			for(i=0;i<form.elements.length;i++){
				var child = form.elements[i];
				var next = form.elements[i+1];
				if(next && child == elemento && next.type && (next.type == 'password' || next.type == 'text')){
					setInputElement(next);
					break;
				}
			}
		}
	}
				
	function tecladoVirtualAlfaBranco_getRefDiv() {
		var divRef;
		if(ie) {
			divRef=eval("document.all.divTecladoVirtual");
		}
		else if (ns6) {
			divRef=eval("document.getElementById('divTecladoVirtual')");
		}
		else if (ns4) {
			divRef=eval("document.layers['divTecladoVirtual']");
		}
		return(divRef);
	}

	function mostrarTecladoVirtual(){
			
			$("#divTecladoVirtual").css('display','none');
/*			
			var divRef = tecladoVirtualAlfaBranco_getRefDiv();
			
			if((ie || ns6))
				divRef.style.visibility = 'visible';
			else if((ns4) && divRef)
				divRef.visibility = 'show';
*/				
	}
	
	//criado por Jesus Rodrigo
	
		function tcvDefinirOpacity(){
			/*$("#imgTecladoVirtual").css('opacity',0.4);*/
/*			
			var obj = document.getElementById("imgTecladoVirtual");
			obj.style.opacity = 0.4;
			*/
		}
		//--------------------------------------------------------------
		function tcvAumentar(){
			$("#imgTecladoVirtual").css('opacity',-0.5);
/*			
			var obj = document.getElementById("imgTecladoVirtual");
			obj.style.opacity  -= -0.05;
*/			
		}
		//--------------------------------------------------------------
		function tcvDiminuir(){
			$("#imgTecladoVirtual").css('opacity',0.5);
/*			
			var obj = document.getElementById("imgTecladoVirtual");
			obj.style.opacity -=0.05;
*/			
		}
		
	//Fim criado por Jesus Rodrigo