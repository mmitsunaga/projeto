
$(document).ready(
	function() {
		if (window.localStorage) {
			var b = new Date();
        	var c = b.getDate() +""+ b.getMonth() +""+ b.getFullYear();    			
			if(localStorage.getItem(c)){
				
				if(localStorage.getItem('Comarca1')){
					var obTemp = JSON.parse(localStorage.getItem('Comarca1'));
					atualizaTelaComarca(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarca(1); }, 14000);				
				}
				if(localStorage.getItem('Comarca7')){
					var obTemp = JSON.parse(localStorage.getItem('Comarca7'));
					atualizaTelaComarca(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarca(7); }, 17000);				
				}
				if(localStorage.getItem('Comarca30')){
					var obTemp = JSON.parse(localStorage.getItem('Comarca30'));
					atualizaTelaComarca(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarca(30); }, 20000);				
				}
				if(localStorage.getItem('Comarca182')){
					var obTemp = JSON.parse(localStorage.getItem('Comarca182'));
					atualizaTelaComarca(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarca(182); }, 23000);				
				}
				if(localStorage.getItem('Comarca365')){
					var obTemp = JSON.parse(localStorage.getItem('Comarca365'));
					atualizaTelaComarca(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarca(365); }, 26000);				
				}
				if(localStorage.getItem('ComarcaServentia1')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentia1'));	
					atualizaTelaComarcaServentia(obTemp );
				}else{
					gerarEstatisticaComarcaServentia(1);				
				}
				if(localStorage.getItem('ComarcaServentia7')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentia7'));
					atualizaTelaComarcaServentia(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentia(7); }, 2000);				
				}	
				if(localStorage.getItem('ComarcaServentia30')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentia30'));
					atualizaTelaComarcaServentia(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentia(30); }, 5000);				
				}
				if(localStorage.getItem('ComarcaServentia182')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentia182'));
					atualizaTelaComarcaServentia(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentia(182); }, 8000);				
				}
				if(localStorage.getItem('ComarcaServentia365')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentia365'));
					atualizaTelaComarcaServentia(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentia(365); }, 11000);				
				}
				if(localStorage.getItem('ComarcaServentiaCargo1')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentiaCargo1'));	
					atualizaTelaComarcaServentiaCargo(obTemp );
				}else{
					gerarEstatisticaComarcaServentiaCargo(1);				
				}
				if(localStorage.getItem('ComarcaServentiaCargo7')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentiaCargo7'));
					atualizaTelaComarcaServentiaCargo(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(7); }, 2000);				
				}	
				if(localStorage.getItem('ComarcaServentia30')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentiaCargo30'));
					atualizaTelaComarcaServentiaCargo(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(30); }, 5000);				
				}
				if(localStorage.getItem('ComarcaServentia182')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentiaCargo182'));
					atualizaTelaComarcaServentiaCargo(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(182); }, 8000);				
				}
				if(localStorage.getItem('ComarcaServentia365')){
					var obTemp = JSON.parse(localStorage.getItem('ComarcaServentiaCargo365'));
					atualizaTelaComarcaServentiaCargo(obTemp );	
				}else{
					setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(365); }, 11000);				
				}	
			}else{
				atualizarTudo();	
			}
		}else{
			//atualizar sempre
			atualizarTudo();
		}
    }
); 

function atualizarTudo(){
	if (window.localStorage) {
		localStorage.clear();
	}
	$('#divEstatistica').html("");
	setTimeout(function(){ gerarEstatisticaComarca(1); }, 100);
	setTimeout(function(){ gerarEstatisticaComarca(7); }, 2000);
	setTimeout(function(){ gerarEstatisticaComarca(30); },5000);
	setTimeout(function(){ gerarEstatisticaComarca(182); },8000);
	setTimeout(function(){ gerarEstatisticaComarca(365); },11000);
	setTimeout(function(){ gerarEstatisticaComarcaServentia(1)},14000);
	setTimeout(function(){ gerarEstatisticaComarcaServentia(7); }, 17000);
	setTimeout(function(){ gerarEstatisticaComarcaServentia(30); },20000);
	setTimeout(function(){ gerarEstatisticaComarcaServentia(182); },23000);
	setTimeout(function(){ gerarEstatisticaComarcaServentia(365); },26000);
	setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(1); },29000);
	setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(7); },32000);
	setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(30); },35000);
	setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(182); },38000);
	setTimeout(function(){ gerarEstatisticaComarcaServentiaCargo(365); },41000);
}

function  gerarEstatisticaComarca(qtdDias) {
		var boFecharDialog = false;
		var vqtdDias = qtdDias;
		var id_serv = $('#Id_Serventia').val();
		var url="Estatistica?AJAX=ajax&PaginaAtual=2&fluxo=Comarca&id_serv="+id_serv+"&dias="+vqtdDias;
	    $.ajax({
	        url: encodeURI(url),
	        context: document.body,
	        timeout: 300000, async: true,
	        success: function(retorno){
	        	
        		objEstatisticaServentiaComarca = retorno;
        		//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
	        	if (window.localStorage) {
					localStorage.setItem('Comarca'+retorno.dias,JSON.stringify(retorno));
					var b = new Date();
					var c = b.getDate() +""+ b.getMonth() +""+ b.getFullYear();
					localStorage.setItem(c,'true');
	        	}
	        	atualizaTelaComarca(retorno);				
	            boFecharDialog=true;
	        },
	        
	        beforeSend: function(data ){
	        	//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
	        	timer = setTimeout(function() {
	        		mostrarMensagemConsultando('Projudi - Gerando ', 'Aguarde, buscando os dados...');		
	        		boFecharDialog=true;
	        	}, 1500);	        				
	        },
	        error: function(request, status, error){			
	        	boFecharDialog=false;
	        	if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}				
	        },
	        complete: function(data ){
	        	clearTimeout(timer);
	        	if (boFecharDialog) {
	        		$("#dialog").dialog('close');	
	        	}	        
	        }
	    });	
}

function  gerarEstatisticaComarcaServentia(qtdDias) {
		var boFecharDialog = false;
		var vqtdDias = qtdDias;
		var id_serv = $('#Id_Serventia').val();
		var url="Estatistica?AJAX=ajax&PaginaAtual=2&fluxo=ComarcaServentia&id_serv="+id_serv+"&dias="+vqtdDias;
	    $.ajax({
	        url: encodeURI(url),
	        context: document.body,
	        timeout: 300000, async: true,
	        success: function(retorno){
	        	
        		objEstatisticaServentiaComarca = retorno;
        		//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
	        	if (window.localStorage) {
					localStorage.setItem('ComarcaServentia'+retorno.dias,JSON.stringify(retorno));
					var b = new Date();
					var c = b.getDate() +""+ b.getMonth() +""+ b.getFullYear();
					localStorage.setItem(c,'true');
	        	}
	        	atualizaTelaComarcaServentia(retorno);				
	            boFecharDialog=true;
	        },
	        
	        beforeSend: function(data ){
	        	//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
	        	timer = setTimeout(function() {
	        		mostrarMensagemConsultando('Projudi - Gerando ', 'Aguarde, buscando os dados...');		
	        		boFecharDialog=true;
	        	}, 1500);	        				
	        },
	        error: function(request, status, error){			
	        	boFecharDialog=false;
	        	if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}				
	        },
	        complete: function(data ){
	        	clearTimeout(timer);
	        	if (boFecharDialog) {
	        		$("#dialog").dialog('close');	
	        	}	        
	        }
	    });	
}	

function  gerarEstatisticaComarcaServentiaCargo(qtdDias) {
		var boFecharDialog = false;
		var vqtdDias = qtdDias;
		var id_serv = $('#Id_Serventia').val();
		var url="Estatistica?AJAX=ajax&PaginaAtual=2&fluxo=ComarcaServentiaCargo&id_serv="+id_serv+"&dias="+vqtdDias;
	    $.ajax({
	        url: encodeURI(url),
	        context: document.body,
	        timeout: 300000, async: true,
	        success: function(retorno){
	        	
        		objEstatisticaServentiaComarca = retorno;
        		//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
	        	if (window.localStorage) {
					localStorage.setItem('ComarcaServentiaCargo'+retorno.dias,JSON.stringify(retorno));
					var b = new Date();
					var c = b.getDate() +""+ b.getMonth() +""+ b.getFullYear();
					localStorage.setItem(c,'true');
	        	}
	        	atualizaTelaComarcaServentiaCargo(retorno);				
	            boFecharDialog=true;
	        },
	        
	        beforeSend: function(data ){
	        	//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
	        	timer = setTimeout(function() {
	        		mostrarMensagemConsultando('Projudi - Gerando ', 'Aguarde, buscando os dados...');		
	        		boFecharDialog=true;
	        	}, 1500);	        				
	        },
	        error: function(request, status, error){			
	        	boFecharDialog=false;
	        	if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}				
	        },
	        complete: function(data ){
	        	clearTimeout(timer);
	        	if (boFecharDialog) {
	        		$("#dialog").dialog('close');	
	        	}	        
	        }
	    });	
}

function atualizaTelaComarca(retorno){
	 	
 	var stRetonor="";
	var vrComarca = replaceAll(retorno.comarca," ","") ;
	
	if($("#"+vrComarca).length==0){
		$('#divEstatistica').append("<fieldset style='clear:both' id='"+vrComarca+"'> <legend> Comarca " + retorno.comarca + "</legend> </fieldset>");
	}
				 	
	stRetonor+="<fieldset class='col25'> <legend> " + retorno.coluna + "</legend>";
	var coluna =retorno[retorno.coluna];	
	var linhas =Object.keys(coluna);
	for (k=0;k<linhas.length;k++){
		if(retorno.dias==1){
			stRetonor+="<div class='divLinhaEstatistica'><span class='col60 AlinharDireita'  alt='Tipo' title='Tipo'>" + linhas[k] + "</span><span alt='Quantidade' title='Quantidade' class='col10 AlinharDireita'>" + $.number(coluna[linhas[k]].qtd,",",".") + "</span> </div>";				
		}else{
			stRetonor+="<div class='divLinhaEstatistica'><span class='col60 AlinharDireita' alt='Tipo' title='Tipo'>" + linhas[k] + "</span><span alt='Quantidade' title='Quantidade' class='col10 AlinharDireita'>" + $.number(coluna[linhas[k]].qtd,",",".") + "</span><span alt='M&eacute;dia' title='M&eacute;dia' class='col15 AlinharDireita'>" + $.number(coluna[linhas[k]].media,2,",",".") + "</span></div>";
		}
	}
	stRetonor+="</fieldset>";
					
	 $("#"+vrComarca).append(stRetonor);

}

function atualizaTelaComarcaServentia(retorno){	 	
 	var stRetonor="";	
	var vrServentia = replaceAll(retorno.serventia," ","");

	if($("#"+ vrServentia).length==0){
		$("#divEstatistica").append("<fieldset style='clear:both' id='"+vrServentia+"'> <legend> " + retorno.serventia + "</legend> </fieldset>");
	}
			 	
	stRetonor+="<fieldset class='col25'> <legend> " + retorno.coluna + "</legend>";
	var coluna =retorno[retorno.coluna];	
	var linhas =Object.keys(coluna);
	for (k=0;k<linhas.length;k++){
		if(retorno.dias==1){
			stRetonor+="<div class='divLinhaEstatistica'><span class='col60 AlinharDireita' alt='Tipo' title='Tipo' >" + linhas[k] + "</span><span alt='Quantidade' title='Quantidade' class='col10 AlinharDireita'>" + $.number(coluna[linhas[k]].qtd,",",".") + "</span> </div>";				
		}else{
			stRetonor+="<div class='divLinhaEstatistica'><span class='col60 AlinharDireita' alt='Tipo' title='Tipo'>" + linhas[k] + "</span><span alt='Quantidade' title='Quantidade' class='col10 AlinharDireita'>" + $.number(coluna[linhas[k]].qtd,",",".") + "</span><span alt='M&eacute;dia' title='M&eacute;dia' class='col15 AlinharDireita'>" + $.number(coluna[linhas[k]].media,2,",",".") + "</span> </div>";
		}
	}
	stRetonor+="</fieldset>";
					
	 $("#"+vrServentia).append(stRetonor);

}

function atualizaTelaComarcaServentiaCargo(retorno){
	 	
 	var stRetonor="";
		
	if($("#Cargos").length==0){
		$('#divEstatistica').append("<fieldset style='clear:both' id='Cargos'> <legend> Cargos da Serventia</legend> </fieldset>");
	}
			 	
	stRetonor+="<fieldset class='col25'> <legend> " + retorno.coluna + "</legend>";
	var coluna =retorno[retorno.coluna];	
	var linhas =Object.keys(coluna);
	for (k=0;k<linhas.length;k++){
		if(retorno.dias==1){
			stRetonor+="<div class='divLinhaEstatistica'> <span class='col60' alt='Tipo' title='Tipo'>" + coluna[linhas[k]].usuario + " - " + linhas[k] + "</span><span alt='Quantidade' title='Quantidade' class='col10'>" + $.number(coluna[linhas[k]].qtd,0,",",".") + "</span> </div>";				
		}else{
			stRetonor+="<div class='divLinhaEstatistica'> <span class='col60' alt='Tipo' title='Tipo'>" + coluna[linhas[k]].usuario + " - " + linhas[k] + "</span><span alt='Quantidade' title='Quantidade' class='col10'>" + $.number(coluna[linhas[k]].qtd,0,",",".") + "</span><span alt='M&eacute;dia' title='M&eacute;dia' class='col15 '>" + $.number(coluna[linhas[k]].media,2,",",".") + "</span> </div>";
		}
	}
	stRetonor+="</fieldset>";
					
	 $("#Cargos").append(stRetonor);

}
