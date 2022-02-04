  function buscaDados(posicao, tamanho) {
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	url = 'PendenciaDwr?AJAX=ajax&PaginaAtual=3&fluxo=12&dataInicialInicio='+document.getElementById('dataInicialInicio').value+'&dataFinalInicio='+document.getElementById('dataFinalInicio').value+'&Id_PendenciaTipo='+document.getElementById('Id_PendenciaTipo').value+'&Id_PendenciaStatus='+document.getElementById('Id_PendenciaStatus').value+'&prioridade='+true+'&filtroTipo='+Formulario.filtroTipo.value+'&dataInicialFim='+document.getElementById('dataInicialFim').value +'&dataFinalFim='+document.getElementById('dataFinalFim').value + '&PosicaoPaginaAtual=' + posicao;
	
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
    				corpoTabela +='<td width="160" align="center">';
    				corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + item.desc8 + '">' + item.desc1 + '</a></td>';
    				corpoTabela +='<td>' + item.desc2 + '</td>';
    				corpoTabela +='<td>' + item.desc3 + '</td>';
    				corpoTabela +='<td class="lista_data">' + item.desc4 + '</td>';
    				corpoTabela +='<td class="lista_data">' + item.desc5 + '</td>';
    				corpoTabela +='<td class="lista_data">' + item.desc6 + '</td>';
    				corpoTabela +='<td>' + item.desc7 + '</td>';
    				corpoTabela +='<td>';
    				corpoTabela +='<a href=\"Pendencia?PaginaAtual=7&amp;pendencia=' + item.id + '&amp;CodigoPendencia='+ item.hash +'&amp;fluxo=3\">';
    				corpoTabela +='<img src=\"imagens/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
    				corpoTabela +='<\/a>';
    				corpoTabela +='</td>';
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