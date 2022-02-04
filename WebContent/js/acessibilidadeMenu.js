// JavaScript Document
/**
Autor: Jesus Rodrigo Corr�a
Goi�nia - Goi�s - Brasil
20 de Setembro de 2010
Acessibilidade - Utiliza��o do Teclado para navegar no menu ul li
crie:
	 Uma DIV com id menu onde ser� aplicado a navega��o
	 No CSS as class .menuA, para o link, e .menuAHover, para quando estiver selecionado.
**/
	var objFocado;
	var objDesFocado;
	var objParaFoco;
	
	function filhos(obj, linha){
		$(obj).find(' > ul > li').each(function(coluna) {
			$(this).find('>a').attr('id', linha + 'm' + coluna);
			filhos(this,linha + 'm' + coluna);
		});
	}
	
	function criarMenu(obj, objFocus, classA, classAHover){
		objParaFoco=objFocus;
				
		//crio a marca��o do menu e sub-menus
		$('#'+obj+' > ul > li').each(function(index) {
			$(this).children('a').attr('id',index);
			filhos(this, index);			
		 });
		
		$('#'+obj+' li').mouseenter(function(evt) {
			$(this).removeClass(classA);
			$(this).addClass(classAHover);
			$(this).css('z-index',1500);
			//alert('Mouse Over');
			$(this).children('ul').fadeIn();
		});
		
		$('#'+obj+' li').mouseleave(function(evt) {
			$(this).removeClass(classAHover);
			$(this).addClass(classA);
			$(this).css('z-index',1000);
			//alert('Mouse Out');			
			$(this).children('ul').hide();
		});
		
		
		$('#'+obj+' a').focus(function(evt) {
			$(this).parent().children('ul').fadeIn();
		 	$(this).removeClass(classA);
			$(this).addClass(classAHover);
			objFocado=$(this).attr('id');

		  });
	  
		 $('#'+obj+' a').focusout(function(evt) {
			$(this).removeClass(classAHover);
		  	$(this).addClass(classA);
		  	var id=$(this).attr('id').split('m');
	        var tamanho = id.length;
		  	objDesFocado=$(this).attr('id');
		  	if (objFocado==objDesFocado){
		  			var objAnterior = $(this).parent().parent();					
					for(k=tamanho;k>=2;k--){
						objAnterior.hide();
						objAnterior = objAnterior.parent().parent();
					}	
		  	}
		  	
		 });
  	
		//---------------------- 	
	 	$('#'+obj+' a').bind("keypress", function(event){
			//pego o nome do menu, que significa a sua posição
			var id=$(this).attr('id').split('m');
	        var tamanho = id.length;
	
			var primeiroNivel = id[0];
			var ultimoNivel = id[tamanho-1];
			
			switch(event.keyCode){
				case 13:{					
					$('#' + objParaFoco ).focus();
					var objAnterior = $(this).parent().parent();					
					for(k=tamanho;k>=2;k--){
						objAnterior.hide();
						objAnterior = objAnterior.parent().parent();
					}					
					break;
				}
	        	case 39:{ //direita
					//no primeiro nivel
					if (tamanho==1)	{
						id[0]=parseInt(id[0])+1;
						var proximo = $("#"  + id.join('m'));	
						if (proximo[0]!=null){						
							$(this).parent().children('ul').fadeOut('slow', function() {   });
							proximo.focus();	
						}
					}else{ 
							//se tiver filho passa para o proximo menu
							if ($('#' + id.join('m') + 'm0')[0]!=null) {
	 						    $('#' + id.join('m') + 'm0').focus();
							}else {
								id[0]=parseInt(id[0])+1;
								//se não tiver filho passa para o proximo menu
								var proximo = $("#" + id.join('m'));	
								if (proximo[0]!=null && tamanho==2){						
									$(this).parent().parent().fadeOut('slow', function() {   });
									proximo.parent().parent().fadeIn('slow', function() {   });
									proximo.focus();						
								}
							}
						}
	            	break;
	            }
	           	case 37:{ //esquerda
					if (tamanho>=3) id.splice(tamanho-1); else 	id[0]=parseInt(id[0])-1;
					if (tamanho==1){
						var anterior = $("#"  + id.join('m'));
						if (anterior[0]!=null){
							$(this).parent().children('ul').fadeOut('slow', function() {   });
							anterior.focus();
						}
					}else{
						var anterior = $("#" + id.join('m'));
						if (anterior[0]!=null){
							$(this).parent().parent().fadeOut('slow', function() {   });
							anterior.parent().parent().fadeIn('slow', function() {   });
							anterior.focus();
						}
					}
	            	break;
	            }
	            case 38:{ // cima
					if (tamanho==1) break;
					if  (ultimoNivel==0) id.splice(tamanho-1); else id[tamanho-1]=parseInt(id[tamanho-1])-1;
							
					$(this).parent().children('ul').fadeOut('slow', function() {   });
					$("#" + id.join('m')).focus(); 
	
	            	break;
	            }
	           	case 40:{ // baixo
					if(tamanho==1) 	$("#"+ id.join('m') + 'm0').focus();            	
					else { 
							id[tamanho-1]=parseInt(id[tamanho-1])+1;			
							$(this).parent().children('ul').fadeOut('slow', function() {   });
							$("#" + id.join('m')).focus();            	
						}
	            	break;
	            }
			}		   
	 	});
	 	//-----------------------------------
 };