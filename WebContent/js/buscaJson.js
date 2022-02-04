function buscaDadosJSON(posicao, tamanho, url, corpoTabela, jsonCallBack, nomeTabela) {
	var boFecharDialog = false;
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
        	
        	var tabela =  $('#tabListaDados');
        	if(nomeTabela != null){
        		tabela =  $(''+nomeTabela+'');
        	}
            tabela.html('');
        	
            var corpoTabelaJSON = "";
            
            $.each(retorno, function(i,item){
            	if (i > -1){
            		if(item.id=="-50000"){						
    					totalPaginas = item.desc1;
    				}else if (item.id=="-60000"){
    					posicao = item.desc1;
    				}else {
    					corpoTabelaJSON +='<tr class="TabelaLinha'+(i%2 + 1)+'">';
                		corpoTabelaJSON +='<td   class="Centralizado" >' + ((i-1)) + '</td>';            	

                		corpoTabelaJSON += corpoTabela;                		
                		for (parametro in item) {
                		    parametroSubstituir = 'JsoN'+parametro;
                			//alert("aqui: "+item.id+"\npar: "+parametro+"\nval: "+item[parametro]);
                		    
                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
                		}
                		corpoTabelaJSON +='</tr>';
                	}
            	}
            });
//            alert(corpoTabelaJSON);
            tabela.append(corpoTabelaJSON);
            
            chamada = 'buscaDados(';
            CriarPaginacaoSimplesJSON(chamada, posicao,totalPaginas, tamanho);
            if(jsonCallBack != null){
            	jsonCallBack();
            }
        },
        
        beforeSend: function(data ){
        	//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
        	timer = setTimeout(function() {
        		mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');		
        		boFecharDialog=true;
        	}, 1500);
        	$("#formLocalizarBotao").hide();			
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
        	//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
        	clearTimeout(timer);
        	if (boFecharDialog) {
        		$("#dialog").dialog('close');	
        	}
			$("#formLocalizarBotao").show();
        }
    });	
}