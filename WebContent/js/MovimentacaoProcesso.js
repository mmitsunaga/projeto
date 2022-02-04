var podeLimparAposIncluir = true;

function iniciar() {
	atualizarPendencias();
	podeLimparAposIncluir = true;
}

function iniciarExtratoAta(){
	atualizarPendencias();
	podeLimparAposIncluir = false;
}

function mostrarOpcoesPendencia(){
	var codPendenciaTipo = $("#codPendenciaTipo").val();	
	var boErro = false;
	const MARCAR_SESSAO = 23;
	const REMARCAR_SESSAO = 26;
	const MARCAR_SESSAO_EXTRA_PAUTA = 83;
	const CARTA_NOTIFICACAO = 73;
	const MANDADO = 4;
	const CARTA_PRECATORIA = 7;
	const ALVARA = 3;
	const EDITAL = 5;
	const INTIMACAO = 2;
	const INTIMACAO_VIA_TELEFONE = 91;
	const CARTA_CITACAO = 1;
	const OFICIO = 6;
	const ALTERAR_VALOR_CAUSA = 101;
	const ALTERAR_CLASSE_PROCESSUAL = 102;
	const REVELIA = 51;
	const CONTUMACIA = 52;
	const PEDIDO_VISTA = 57;
	const CONCLUSO_GENERICO_SEGUNDO_GRAU = 78;
	const ENVIAR_INSTANCIA_SUPERIOR = 22;
	const RETORNAR_SERVENTIA_ORIGEM = 28;
	const ARQUIVAMENTO = 24;
	const DESARQUIVAMENTO = 25;
	const DESMARCAR_SESSAO = 27;
	const FINALIZAR_SUSPENSAO_PROCESSO = 49;
	const MARCAR_AUDIENCIA = 50;
	const MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC = 96;
	const MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT = 98;
	const MARCAR_AUDIENCIA_MEDIACAO_CEJUSC = 97;	
	const ARQUIVAMENTO_PROVISORIO = 54;
	const RELATORIO = 58;
	
	Ocultar('divOpcoesIntimacaoViaTelefone');
	Ocultar('divOpcoesIntimacao');
	Ocultar('divOpcoes');
	Ocultar('divAlterarValorCausa');
	Ocultar('divAlterarClasseProcessual');
	Ocultar('divSessaoClasse');
	Ocultar('divSessao');
	Ocultar('divDestinatario');
	Ocultar('divDesembargador');
	
	$.ajax({		
		url: 'Movimentacao?AJAX=ajax&PaginaAtual=8&operacao=' + codPendenciaTipo,
		context: document.body,
		timeout: 500000,
		success: function(retorno){
			switch(parseInt(codPendenciaTipo)) {
			case MARCAR_SESSAO: 
			case REMARCAR_SESSAO:
			case MARCAR_SESSAO_EXTRA_PAUTA:
				Mostrar('divSessao');
				Mostrar('divDesembargador');
				preencherListaSessoes(retorno);
				break;
			case CARTA_NOTIFICACAO:
			case MANDADO:
			case CARTA_PRECATORIA:
			case ALVARA:
			case EDITAL:
				Mostrar('divDestinatario');
				Mostrar('divOpcoes');
				preencherListaDestinatarios(retorno);
				break;
			case OFICIO:
				Mostrar('divDestinatario');
				Mostrar('divOpcoes');
				preencherListaDestinatarios(retorno);
				break;
			case CARTA_CITACAO:
			case INTIMACAO:
				Mostrar('divDestinatario');
				Mostrar('divOpcoesIntimacao');
				Mostrar('divOpcoes');
				preencherIntimacao(retorno);
				break;
			case INTIMACAO_VIA_TELEFONE:
				Mostrar('divDestinatario');
				Mostrar('divOpcoesIntimacaoViaTelefone');
				preencherIntimacao(retorno);
				break;
			case ALTERAR_VALOR_CAUSA:
				Mostrar('divAlterarValorCausa');
				break;
			case ALTERAR_CLASSE_PROCESSUAL:
				Mostrar('divAlterarClasseProcessual');
				break;
			case REVELIA:
				Mostrar('divDestinatario');
				preencherListaDestinatarios(retorno);
				break;
			case CONTUMACIA:
				Mostrar('divDestinatario');
				preencherListaDestinatarios(retorno);
				break;
			case PEDIDO_VISTA:
			case CONCLUSO_GENERICO_SEGUNDO_GRAU:
				Mostrar('divDestinatario');
				preencherListaDestinatarios(retorno);
				break;
			case ENVIAR_INSTANCIA_SUPERIOR:
			case RETORNAR_SERVENTIA_ORIGEM:
			case ARQUIVAMENTO:
			case DESARQUIVAMENTO:
			case DESMARCAR_SESSAO:
			case FINALIZAR_SUSPENSAO_PROCESSO:
			case MARCAR_AUDIENCIA:
			case MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC:
			case MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT:
			case MARCAR_AUDIENCIA_MEDIACAO_CEJUSC:	
			case ARQUIVAMENTO_PROVISORIO:
			case RELATORIO:
				break;
			default:
				verificarOpcoes(retorno);
				break;
			}
		},
		beforeSend: function(data ){},
		error: function(request, status, error){
			mostrarMensagemErro('Erro na Consulta', request.responseText);
			boErro=true;
		  }, 
		complete: function(data ){
			if (!boErro)
				$("#dialog").dialog('close');			
		  }
	});	  
	
}

function atualizarPendencias(editavel) {
	const ATUALIZA_PENDENCIAS = 10001;
	var tabela = $('#corpoTabela');
	var boErro = false;
	$.ajax({		
		url: 'Movimentacao?AJAX=ajax&PaginaAtual=8&operacao=' + ATUALIZA_PENDENCIAS,
		context: document.body,
		timeout: 500000,
		success: function(retorno){
			tabela.html('');
			var inLinha=2;		
			var corpoTabela = "";
			$.each(retorno, function(i,item){
				var prefixo = "";
			  	if(item.codPendenciaTipo == "101") {
			  		prefixo = "R$ " + dado.outros;
			  		$('#"tableIntimacao"' + item.id).val(prefixo + item.tipoIntimacao);
			  	} else if(item.codPendenciaTipo == "102") {
			  		prefixo = "Nova Classe: " + dado.processoTipo;
			  		$('#"tableIntimacao"' + item.id).val(prefixo + item.tipoIntimacao);
			  	}

			  	corpoTabela +='<tr id="identificador' + item.id + '" style="display: table-row;" class="MarcarLinha TabelaLinha' + inLinha + '">';
			  	if (editavel == null){
			  		corpoTabela +='<td >' + '<input name="pendencias" id="chk_idPendencia' + item.id + '" value="' + item.id + '" type="checkbox"></td>';
			  	}
				corpoTabela +='<td >' + item.pendenciaTipo + '</td>';
				corpoTabela +='<td >' + item.destinatario + '</td>';
				corpoTabela +='<td >' + item.prazo + '</td>';
				corpoTabela +='<td >' + item.urgencia + '</td>';
				corpoTabela +='<td >' + item.outros + '</td>';
				if (editavel == null){
			  		corpoTabela +='<td >' + '<input name="button' + item.id + '" id="button' + item.id + '" title="Retirar esta linha" src="./imagens/imgExcluirPequena.png" onclick="excluir(\'' + item.id + '\',\'' + item.pendenciaTipo + '\'); return false;" type="image"></td>';
			  	}
     			corpoTabela +='</tr>';
     			
     			if (inLinha==1) inLinha=2; else inLinha=1;
			});
			tabela.append(corpoTabela);
		},
		beforeSend: function(data ){},
		error: function(request, status, error){
			mostrarMensagemErro('Erro na Consulta', request.responseText);
			boErro=true;
		}, 
		complete: function(data ){
			if (!boErro)
				$("#dialog").dialog('close');			
		}
	});	  
}

function excluir(indice, descricao) {
	const EXCLUIR_PENDENCIA = 10002;
	var boErro = false;
	if (descricao == null || confirm("Deseja retirar o item \"" + descricao + "\"?")) {
		$.ajax({		
			url: 'Movimentacao?AJAX=ajax&PaginaAtual=8&operacao=' + EXCLUIR_PENDENCIA + "&indice=" + indice,
			context: document.body,
			timeout: 500000,
			success: function(retorno){},
			beforeSend: function(data ){},
			error: function(request, status, error){
				mostrarMensagemErro('Erro na Consulta', request.responseText);
				boErro=true;
			}, 
			complete: function(data ){
				if (!boErro) {
					atualizarPendencias();
				}
				$("#dialog").dialog('close');			
			}
		});	  
		return true;
 	} return false;
}

function inserir() {
	const INCLUIR_LISTA_PENDENCIAS = 10000;
	var boErro = false;
	var dado = {
		id:$("#id").val(),  
		codPendenciaTipo:$("#codPendenciaTipo").val(), 
		pendenciaTipo:$("#codPendenciaTipo option:selected").text(), 
		codDestinatario:$("#codDestinatario").val(), 
		destinatario:$("#codDestinatario option:selected").text(), 
		destinatarioTipo:$("#destinatarioTipo").val(), 
		prazo:$("#prazo").val(), 
		urgencia:$("#urgencia").val(), 
		outros:$("#outros").val(), 
		idProcessoTipo:$("#idProcessoTipo").val(), 
		processoTipo:$("#processoTipo").val(), 
		onLine:$("#onLine").val(), 
		pessoalAdvogado:$("#pessoalAdvogado").val(), 
		intimacaoAudiencia: $("#intimacaoAudiencia").val(), 
		id_Sessao:$("#id_Sessao").val(),
		dataSessao:$("#id_Sessao option:selected").text(),
		pessoal:$("#pessoal").val(), 
		id_Classe:$("#id_Classe").val(), 
		classe:$("#id_Classe option:selected").text(),
		id_Desembargador:$("#id_Desembargador").val(), 
		desembargador:$("#id_Desembargador option:selected").text(),
		complemento:$("#complemento").val(), 
		};
	if (dado.codPendenciaTipo == -1) {
  		alert("Selecione o Tipo de Pendência");
  		return;
	}
	$.ajax({	
		type: 'POST',
		url: 'Movimentacao?AJAX=ajax&PaginaAtual=8&operacao=' + INCLUIR_LISTA_PENDENCIAS,
		data: dado,
		context: document.body,
		timeout: 500000,
		success: function(retorno){},
		beforeSend: function(data ){},
		error: function(request, status, error){
			mostrarMensagemErro('Erro na Consulta', request.responseText);
			boErro=true;
		}, 
		complete: function(data ){
			if (!boErro) {
				atualizarPendencias();
				$("#dialog").dialog('close');			
				if (podeLimparAposIncluir) {
					limpar();
				}
			}
		}
	});	  
}

function limpar() {
	$("#id").val("");  
	$("#codPendenciaTipo").val(""); 
	$("#pendenciaTipo").val(""); 
	$("#codDestinatario").empty();
	$("#destinatario").val("");
	$("#destinatarioTipo").val("");
	$("#prazo").val("");
	$("#urgencia").val("");
	$("#outros").val(""); 
	$("#idProcessoTipo").val(""); 
	$("#processoTipo").val(""); 
	$("#onLine").val(""); 
	$("#pessoalAdvogado").val(); 
	$("#intimacaoAudiencia").val(""); 
	$("#id_Sessao").empty();
	$("#sessao").empty();
	$("#pessoal").val("");
	$("#id_Classe").val("");
	$("#classe").val("");
	$("#id_Desembargador").empty();
	$("#desembargador").val("");
	$("#complemento").val(""); 

  	Ocultar('divOpcoesIntimacaoViaTelefone');
	Ocultar('divOpcoesIntimacao');
	Ocultar('divOpcoes');
	Ocultar('divAlterarValorCausa');
	Ocultar('divAlterarClasseProcessual');
	Ocultar('divSessaoClasse');
	Ocultar('divSessao');
	Ocultar('divDestinatario');
	Ocultar('divDesembargador');
}

function excluirSelecionados(tabela){
	if (!confirm("Deseja realmente excluir as pendências selecionadas?")) return false;
	var qtdExcluidos = 0;
	$("#" + tabela + " td > input:checked").each(function (i,a) {
		if (a.value != null && a.value != ""){
			if (excluir(a.value, null)) qtdExcluidos++;					
		}
	});
	
	alterarEstadoSelTodos(false);
	
	if (qtdExcluidos == 0) alert("Nenhuma pendência foi excluída");
}

function preencherListaSessoes(retorno) {
	
	var id_Sessao = $('#id_Sessao');
	var temp = "";
	
	id_Sessao.empty();
	$.each(retorno.SESSOES, function(i,item){
		id_Sessao.append($("<option>").attr('value',item.ID).text(item.DATA));
	})
	
	var id_Desembargador = $('#id_Desembargador');
	id_Desembargador.empty();
	$.each(retorno.RELATORES, function(i,item){
		id_Desembargador.append($("<option>").attr('value',item.ID).text(item.NOME));
	})
	
	if(retorno.TIPO == 2) {
		Mostrar('divSessaoClasse');
		var id_Classe = $('#id_Classe');
		id_Classe.empty();
		$.each(retorno.CLASSES, function(i,item){
			id_Classe.append($("<option>").attr('value',item.ID).text(item.PROC_TIPO));
		})
	}
}

function preencherListaDestinatarios(retorno) {
	
	var codDestinatario = $('#codDestinatario');
	var temp = "";
	
	codDestinatario.empty();
	$.each(retorno, function(i,item){
		codDestinatario.append($("<option>").attr('value',item.ID).text(item.DESTINATARIO));
	})
}

function verificarOpcoes(retorno) {
	$.each(retorno, function(i,item){
		if(item.CONCLUSAO != 'CONCLUSAO') 
			Mostrar('divOpcoes');
	})
}

function preencherIntimacao(retorno) {
	
	var codDestinatario = $('#codDestinatario');
	var temp = "";
	
	codDestinatario.empty();
	$.each(retorno, function(i,item){
		codDestinatario.append($("<option>").attr('value',item.ID).text(item.DESTINATARIO));
	})
}

function atualizarPendenciasJSON() {
//	alert('atualizarPendenciasJSON');
	url = 'MovimentacaoDwr?AJAX=ajax&PaginaAtual=3&fluxo=1';

	var corpoTabela = "";
	corpoTabela +='<tr id="identificador' + 'JsoNid' + '" >';
	corpoTabela +='<td>' + 'JsoNdesc1' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc6' + 'JsoNdesc7' + 'JsoNdesc8' + '</td>';
	
	atualizarPendenciasJsonAjax(url, '#corpoTabela', corpoTabela);
}

function atualizarPendenciasJsonAjax(url, nomeTabela, corpoTabela, jsonCallBack) {
	var boMostrar = false;
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 25000,
        success: function(retorno){        	
        	var tabela =  '';
        	if(nomeTabela != null){
        		tabela =  $(''+nomeTabela+'');
//        		alert('atualizarPendenciasJsonAjax2');
        	}
//            tabela.html('');
        	
            var corpoTabelaJSON = "";            
            $.each(retorno, function(i,item){
            	if (i > -1){
                		corpoTabelaJSON += corpoTabela;                		
                		for (parametro in item) {
                		    parametroSubstituir = 'JsoN'+parametro;
                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
                		}
                		corpoTabelaJSON +='</tr>';
            	}
            });
            alert(corpoTabelaJSON);
            tabela.append(corpoTabelaJSON);
            
            if(jsonCallBack != null){
            	jsonCallBack();
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
        	if (boMostrar) {
        		$("#dialog").dialog('close');	
        	}
			$("#formLocalizarBotao").show();
        }
    });	
}

function inserirJSON() {
//	alert('atualizarPendenciasJSON');
	url = 'MovimentacaoDwr?AJAX=ajax&PaginaAtual=3&fluxo=2';

	var corpoTabela = "";
	corpoTabela +='<tr id="identificador' + 'JsoNid' + '" >';
	corpoTabela +='<td>' + 'JsoNdesc1' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc6' + 'JsoNdesc7' + 'JsoNdesc8' + '</td>';
	
	atualizarPendenciasJsonAjax(url, '#corpoTabela', corpoTabela);
}

//function placeholder(posicao, tamanho) {
//	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
//	url = 'PendenciaDwr?AJAX=ajax&PaginaAtual=3&fluxo=17&dataInicialFim='+document.getElementById('dataInicialFim').value+'&dataFinalFim='+document.getElementById('dataFinalFim').value +'&nomeBusca='+document.getElementById('nomeBusca').value+ '&PosicaoPaginaAtual=' + posicao;
//
//	var corpoTabela = "";
//	corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
//	corpoTabela +='<td width="130" align="center">';
//	corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc1' + '">' + 'JsoNdesc2' + '</a></td>';
//	corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
//	corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
//	corpoTabela +='<td class="lista_data">' + 'JsoNdesc5' + '</td>';
//	corpoTabela +='<td class="lista_data">' + 'JsoNdesc6' + '</td>';
//	
//	corpoTabela +='<td align="center">';
//	corpoTabela +='<a href=\"DescartarPendenciaProcesso?PaginaAtual=7&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;finalizada='+ 'JsoNdesc9' +'\">';
//	corpoTabela +='<img src=\"imagens\/22x22\/' + 'btn_movimentar.png' + '\" alt=\"Marcar Aguardando Parecer/Peticionamento\" title=\"' + 'Marcar Aguardando Parecer/Peticionamento' + '\" \/>';
//	corpoTabela +='<\/a>';
//	corpoTabela +='</td>';
//
//	corpoTabela +='<td align="center">';
//	corpoTabela +='<a href=\"Pendencia?PaginaAtual=-1&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;NovaPesquisa=true&amp;finalizada='+ 'JsoNdesc9' +'\">';
//	corpoTabela +='<img src=\"imagens\/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
//	corpoTabela +='<\/a>';
//	corpoTabela +='</td>';
//	
//	buscaDadosJSON(posicao, tamanho, url, corpoTabela);
//
//}