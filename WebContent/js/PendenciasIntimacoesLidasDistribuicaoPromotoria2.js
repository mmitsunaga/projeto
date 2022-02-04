function buscaDados(posicao, tamanho) {
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	url = 'PendenciaDwr?AJAX=ajax&PaginaAtual=3&fluxo=1&dataInicialInicio='+document.getElementById('dataInicialInicio').value+'&dataFinalInicio='+document.getElementById('dataFinalInicio').value + '&PosicaoPaginaAtual=' + posicao;
    
    $.ajax({
        url: url,
        context: document.body,
        timeout: 25000,
        success: function(retorno){
        	
        	var tabela =  $('#tabListaDados');
            tabela.html('');
            var corpoTabela = "";
            $.each(retorno, function(i,item){
            	if (i > -1){
            		if(item.id=="-50000"){						
    					totalPaginas = item.desc1;
    				}else if (item.id=="-60000"){
    					posicao = item.desc1;
    				}else {
            		corpoTabela +='<tr class="TabelaLinha'+(i%2 + 1)+'">';
            		corpoTabela +='<td   class="Centralizado" >' + ((i-1)) + '</td>';
                	corpoTabela +='<td class="colunaMinima lista_id">' + item.id + '</td>';
                	corpoTabela +='<td align="center">' + item.desc1 + '</td>';
    				corpoTabela +='<td width="130" align="center">';
    				corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + item.desc6 + '">' + item.desc2 + '</a></td>';
    				corpoTabela +='<td align="center">' + item.desc3 + '</td>';
    				corpoTabela +='<td align="center" width="150">' + item.desc4 + '</td>';
    				corpoTabela +='<td align="center" width="150">' + item.desc5 + '</td>';
    				corpoTabela +='<td class="colunaMinima"><a href="PendenciaResponsavel?PaginaAtual=4&amp;pendencia=' + item.id + '&amp;CodigoPendencia=' + item.hash + '"><img src="imagens/22x22/btn_encaminhar.png" alt="Distribuir" title="Efetuar troca de respons&Aacute;vel" /></a></td>';            	
                	corpoTabela +='</tr>';	
                	}
            	}
            	
            });
            tabela.append(corpoTabela);
            
            chamada = 'buscaDados(';
            CriarPaginacaoSimplesJSON(chamada, posicao,totalPaginas, tamanho);
        },
        
        beforeSend: function(data ){
        	var opt = {
			        autoOpen: false,
			        modal: true,			        
			        title: 'Projudi - Consultando'
			};
			$("#dialog").html("Aguarde, buscando os dados...");
		  	$('#dialog').css({'background-image':'url("imagens/spinner.gif")','background-repeat':'no-repeat'});
			$("#formLocalizarBotao").hide();
			$("#dialog").dialog(opt);
			$("#dialog").dialog('open');
        },
        error: function(request, status, error){
        	$('#dialog').dialog({title:"Projudi - Erro", buttons: [{ text: "OK", click: function() { $( this ).dialog("close");}}]});
			$("#dialog").html(request.responseText);
			$('#dialog').css({'background-image':'url("imagens/32x32/ico_erro.png")','background-repeat':'no-repeat'});
			$("#dialog").dialog('open');
        },
        complete: function(data ){
        	$("#dialog").dialog('close');			
			$("#formLocalizarBotao").show();
        }
    });	
}