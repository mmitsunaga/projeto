function getPendenciasJSON(idPendencia){
//	alert('getPendenciasJSON');
	url = 'PendenciaDwr?AJAX=ajax&PaginaAtual=3&fluxo=333&id_pendencia='+idPendencia;

	criarArquivosPendenciaTabela(idPendencia);
	var corpoTabela = "";
	
//	ver modoEdicaoPendencia

	corpoTabela +='<td>' + 'JsoNdesc9' + '</td>';
	corpoTabela +='<td name="' + 'JsoNsubstitute11' + '">';
	corpoTabela +='JsoNdesc11' + '</td>';	

	corpoTabela +='<td name="' + 'JsoNsubstitute60'  + 'JsoNdesc6' + '">';
	corpoTabela += 'Sim' + '</td>';

	corpoTabela +='<td name="' + 'JsoNsubstitute200'  + '">';
	corpoTabela += '<a target=\"_blank\" title=\"Documento sem valor jur&iacute;dico, pois n&atilde;o possui c&oacute;digo nos termos do provimento 10/2013 da CGJ\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash'+'\">' + 'JsoNdesc2' + '</a></td>';

	corpoTabela +='<td align="center">';
	corpoTabela +='<a target=\"_blank\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;CodigoVerificacao=true\">';
	corpoTabela +='<img src=\"imagens/22x22/' + 'btn_pdf.png' + '\" alt=\"Selecionar\" title=\"' + 'Documento com selo eletronico' + '\" \/>';
	corpoTabela +='<\/a>';
	corpoTabela +='</td>';

	corpoTabela +='<td class="Centralizado" name="' + 'JsoNsubstitute30'  + 'JsoNdesc3' + '">';
	corpoTabela +='<a target=\"_blank\" href="PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;recibo=true">' + 'Recibo' + '</a></td>';

	corpoTabela +='<td align="center" name="' + 'JsoNsubstitute120'  + 'JsoNdesc12' + '">';
	corpoTabela +='<a href=\"#\" onclick=\"editarPendencia(' + 'JsoNid' + ', \''+ 'JsoNhash' +'\' , ' + 'JsoNdesc8' + ', \'' + 'JsoNdesc9' + '\', \'' + 'JsoNdesc2' + '\')\">';
	corpoTabela +='<img src=\"imagens/22x22/' + 'ico_editar.png' + '\" alt=\"Editar\" title=\"' + 'Editar o arquivo n&atilde;o assinado' + '\" \/>';
	corpoTabela +='Editar<\/a>';
	corpoTabela +='</td>';
	
	buscarArquivosPendenciaJsonAjax(url, corpoTabela, idPendencia, buscarArquivosPendenciaJsonCallBack);
//	buscarArquivosPendenciaJsonAjax(url, corpoTabela, '#TabelaPendencia' + idPendencia, buscarArquivosPendenciaJsonCallBack);
}

function buscarArquivosPendenciaJsonCallBack(){
//	alert("penguin!");
	$("[name^='JsoNsubstitute300']").html("---");
	$("[name^='JsoNsubstitute600']").html("N&atilde;o");
	$("[name^='JsoNsubstitute1201']").html("");
}

function buscarArquivosPendenciaJsonAjax(url, corpoTabela, nomeTabela, jsonCallBack) {
	var boMostrar = false;
    $.ajax({
        url: url,
        context: document.body,
        timeout: 25000,
        success: function(retorno){
        	
//        	alert('fiibaa');
  
//        	var tabela =  $(nomeTabela);
        	var tabela =  $('#TabelaPendencia'+nomeTabela);
            var corpoTabelaJSON = "";
            
            $.each(retorno, function(i,item){
            	if (i > -1){
            		if(item.id=="-50000"){						
    				}else if (item.id=="-60000"){
    				}else {
    					corpoTabelaJSON +='<tr class="TabelaLinha'+(i%2 + 1)+'">';

                		corpoTabelaJSON += corpoTabela;
                		
                		for (parametro in item) {
//                		    parametroSubstituir = 'JsoN'+parametro;
                		    parametroSubstituir = 'JsoN'+parametro+'(?![0-9])';
                		    
//                		    alert("parametro: 'JsoN'"+parametro+" \nValor:"+item[parametro])
                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
//                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
                		}
                		corpoTabelaJSON +='</tr>';
                	}
            	}
            });
            	tabela.append(corpoTabelaJSON);
//            	alert('teturisu');
            	if(corpoTabelaJSON==''){
//            		alert(''+corpoTabelaJSON);
            		var tdPai =  $(''+'#pai_'+nomeTabela+'');
            		tdPai.html('');
            		mostrarMensagemErro("Projudi - Erro", 'N&atilde;o h&aacute; arquivos para esta pend&ecirc;ncia.');
            	}
            	
            	if(buscarArquivosPendenciaJsonCallBack != null){
            		buscarArquivosPendenciaJsonCallBack();
                }
        },
        
        beforeSend: function(data ){
        	//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
			timer = setTimeout(function() {
				mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');
				boMostrar=true;
			}, 1500);
			$("#formLocalizarBotao").hide();			
        },
        error: function(request, status, error){
        	mostrarMensagemErro("Projudi - Erro", request.responseText);
        },
        complete: function(data ){
        	//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
			clearTimeout(timer);
			if (boMostrar) $("#dialog").dialog('close');	
			$("#formLocalizarBotao").show();
        }
    });	
}
