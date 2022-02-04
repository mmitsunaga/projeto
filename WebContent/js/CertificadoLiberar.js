function buscaDados(posicao, tamanho) {
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	
	var timer;
	var nomeBusca = $('#NomeBusca').val();
	
	url = "Certificado?AJAX=ajax&PaginaAtual=3&fluxo=1&PosicaoPaginaAtual=" + posicao + "&nomeBusca=" + nomeBusca;
	var boFecharDialog = false;
	
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
        	
        	var tabela =  $('#tabListaDados');
            tabela.html('');
            var corpoTabela = "";
            var dataAtual = new Date();
            var dataExpiracao;
            $.each(retorno, function(i,item){
            	if (i>-1){
            		if(item.id=="-50000"){						
    					totalPaginas = item.desc1;
    				}else if (item.id=="-60000"){
    					posicao = item.desc1;
    				}else {
            		corpoTabela +='<tr class="TabelaLinha'+(i%2 + 1)+'">';
            		corpoTabela +='<td > '+(i - 1)+'</td>';
            		corpoTabela +='<td>'+ item.id + '</td>';
            		corpoTabela +='<td>'+ item.desc1 + '</td>';
            		corpoTabela +='<td>'+ item.desc2 + '</td>';
            		corpoTabela +='<td>'+ item.desc3;
            		corpoTabela +='<td>'+ item.desc4;
            		dataExpiracao = new Date(item.desc5);
            		if ((dataExpiracao > dataAtual) && (item.desc6 == "")) {
            		  corpoTabela +='<td class="azul">V&AacuteLIDO</td>';
            		  corpoTabela +='<td align="center"><input name="formLocalizarimgsalvar"  type="image" src="./imagens/22x22/ico_liberar.png" title="Liberar Certificado"';
            		  corpoTabela +=' onclick="AlterarValue(\'PaginaAtual\',\'-5\'); AlterarValue(\'' + $("#tempBuscaId_Certificado").val() + '\',\'' + item.id + '\'); AlterarValue(\'' + $("tempBuscaUsuario_Certificado").val();
            		  corpoTabela +='\',\'' + item.desc1 + '\')"></td>';
            		} else {
            			corpoTabela +='<td colspan="2" class="vermelho">INV&AacuteLIDO</td>';
            		}
            		
            		corpoTabela +='</tr>';
    				}
            	}
            	
            	
            });
            tabela.append(corpoTabela);
            chamada = 'buscaDados(';
            CriarPaginacaoSimplesJSON(chamada, posicao,totalPaginas, tamanho);
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
				mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}        	
        },
        complete: function(data ){
        	//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
        	clearTimeout(timer);
        	if (boFecharDialog){
        		$("#dialog").dialog('close');	
        	}
			$("#formLocalizarBotao").show();
        }
    });	
}