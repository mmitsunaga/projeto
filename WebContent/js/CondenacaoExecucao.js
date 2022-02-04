function buscarListaCondenacoesProcessoExecucao(idEventoExecucao, idProcessoExecucao, posicao) {
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	
	var tabela =  $('#TabelaCondenacoes_'+idEventoExecucao);

	if (tabela.attr("consultado") == 'sim'){
		$('#linha_condenacoes_'+idEventoExecucao).toggle();
		return;
	}

	tabela.html('');
    url = 'CondenacaoExecucao?AJAX=ajax&PaginaAtual=3&fluxo=1&PosicaoPaginaAtual=' + posicao + '&nomeBusca=' + idProcessoExecucao;

	$.ajax({
        url:encodeURI(url),
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){
    		var crimeExecucao;
    		var tempoPenaEmAnos = "";
    		var reincidente;
    		var corpoTabela = "";
    		$.each(retorno, function(i,item){
            	if (i>-1){
            		if(item.id=="-50000"){						
    					totalPaginas = item.desc1;
    				}else if (item.id=="-60000"){
    					posicao = item.desc1;
    				}else {
        	 	 	if ((item.desc1 != null) && (item.desc1 != "")) {
                      crimeExecucao = "Lei: " + item.desc1;
    	    	 	}

    	 	    	if ((item.desc2 != null) && (item.desc2 != "")) {
                      crimeExecucao += " - Art: " + item.desc2;
    	 	 	    }

    	 	 	    if ((item.desc3 != null) && (item.desc3 != "")) {
                      crimeExecucao += " - Par&aacuteg: " + item.desc3;
    	 	 	    }

    	 	 	    if ((item.desc4 != null) && (item.desc4 != "")) {
                      crimeExecucao += " - Inc: " + item.desc4.length;
    	 	 	    }

    	 	 	    if (item.desc5 != "") {
    	 	 	      tempoPenaEmAnos = converterParaAnoMesDia(Number(item.desc5));
    	 	 	    }

	 	 	        reincidente = (Number(item.desc7) != 0?"Sim":"N&atildeo");
	 	 	    
	 	 	        corpoTabela += '<tr><td width="60%">' + crimeExecucao + '</td>' + 
	 	 	          '<td width="10%">' + tempoPenaEmAnos + '</td>' +
	 	 	          '<td width="10%">' + item.desc6 + '</td>' +
	 	 	          '<td width="10%">' + reincidente + '</td>' +
	 	 	          '<td width="10%">' + item.desc8 + '</td></tr>';
    				}
            	}
			});
 	 	    tabela.append(corpoTabela);
   	 	  tabela.attr("consultado","sim");
            $('#linha_condenacoes_'+idEventoExecucao).show();
		},
		error: function(request, status, error){		
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}
	       }	 
	}); // fim do .ajax*/				     
}