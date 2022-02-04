function buscaDados(posicao, tamanho) {
	
	var timer;
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	url = 'Certificado?AJAX=ajax&PaginaAtual=3&fluxo=2'+'&PosicaoPaginaAtual=' + posicao;
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
            var stTempNome= "";
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
            		corpoTabela +='<td><div align="center"><font size="-1">-</font></div></td>';
            		dataExpiracao = new Date(item.desc5);
            		if (dataExpiracao > dataAtual) {
            		  corpoTabela +='<td class="azul">V&AacuteLIDO</td>';
            		  corpoTabela +='<td><input name="formLocalizarimgexcluir"  type="image" src="./imagens/imgExcluirPequena.png" title="Revogar Certificado"';
            		  corpoTabela +=' onclick="AlterarValue(\'PaginaAtual\',\'-3\'); AlterarValue(\'' + $("#tempBuscaId_Certificado").val() + '\',\'' + item.id + '\'); AlterarValue(\'' + $("tempBuscaUsuario_Certificado").val();
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