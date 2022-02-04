<script type="text/javascript"> 
	
	var modoEdicaoPendencia = true;
	var possivelMarcar = false;
	var possivelCopiar = true;
	
	function criarArquivosPendenciaTabela(idPendencia){
		var definicaoTabela = "";
		definicaoTabela +='<fieldset id="arquivos_' + idPendencia + '">';
		definicaoTabela +='<table id="arquivos_' + idPendencia + '_tabela" class="Arquivos" border="0">';
		definicaoTabela +='<thead><tr><th>Tipo Arquivo</th><th>Assinador</th><th>Resposta</th><th>Visualiza&ccedil;&atilde;o</th><th>Impress&atilde;o</th><th>Recibo</th><th>Op&ccedil;&otilde;es</th></tr></thead>';
		definicaoTabela +='<tbody  id="TabelaPendencia' + idPendencia + '" class="Tabela">';
		definicaoTabela +='</tbody>';
		definicaoTabela +='</table>';
		definicaoTabela +='</fieldset>';
		
		var tdPai =  $(''+'#pai_'+idPendencia+'');
		var trLinha =  $(''+'#linha_'+idPendencia+'');
		tdPai.html(definicaoTabela);
		if($(trLinha).is(":visible")){
			trLinha.hide();
		}else{
			trLinha.show();
		}
	    
	}
	
	function buscarArquivosPendencia(id, link, campo, pa){
		var idTab = "arquivos_" + id;
		var tb = document.getElementById(idTab);
		
		var tabelaGeral = document.getElementById("pai_" + id);
	
		if (tb == null){	
			Pendencia.consultarArquivosPendencia(id, {
				callback:function (lista){
					if (lista != null){
						var tabelaArquivos = new TabelaArquivos(idTab, "pai_" + id, link, campo, pa, modoEdicaoPendencia, true, possivelMarcar, possivelCopiar);
						tabelaArquivos.criar(lista);
						trocarDisplay(document.getElementById("linha_" + id));
					} else {
						mostrarMensagemErro("Projudi - Erro","N&atilde;o h&Aacute; arquivos para esta pend&ecirc;ncia");
					}
				}
			});
		} else {
			trocarDisplay(document.getElementById("linha_" + id));
			trocarDisplay(tabelaGeral);
		}
	}
	
	function buscarArquivosPendenciaJSON(idPendencia){
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=18&id_pendencia='+idPendencia;
	
		criarArquivosPendenciaTabela(idPendencia);
		var corpoTabela = "";
		corpoTabela +='<td>' + 'JsoNdesc9' + '</td>';
		corpoTabela +='<td name="' + 'JsoNsubstitute11' + '">';
		corpoTabela +='JsoNdesc11' + '</td>';	
	
		corpoTabela +='<td name="' + 'JsoNsubstitute60'  + 'JsoNdesc6' + '">';
		corpoTabela += 'Sim' + '</td>';
	
		corpoTabela +='<td name="' + 'JsoNsubstitute200'  + '">';
		corpoTabela += '<a target=\"_blank\" title=\"Documento sem valor jur&iacute;dico, pois n&atilde;o possui c&oacute;digo nos termos do provimento 10/2013 da CGJ\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash'+'\">' + 'JsoNdesc2' + '</a></td>';
	
		corpoTabela +='<td class="Centralizado" name="' + 'JsoNsubstitute30'  + 'JsoNdesc3' + '">';
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
	}
	
	function buscarArquivosPendenciaSemEdicao(id, link, campo, pa){
		var idTab = "arquivos_" + id;
		var tb = document.getElementById(idTab);
		
		var tabelaGeral = document.getElementById("pai_" + id);
	
		if (tb == null){	
			Pendencia.consultarArquivosPendencia(id, {
				callback:function (lista){
					if (lista != null){
						modoEdicaoPendencia = false;
						var tabelaArquivos = new TabelaArquivos(idTab, "pai_" + id, link, campo, pa, modoEdicaoPendencia, true, possivelMarcar, possivelCopiar);
						tabelaArquivos.criar(lista);
						trocarDisplay(document.getElementById("linha_" + id));
					} else {
						mostrarMensagemErro("Projudi - Erro","N&atilde;o h&Aacute; arquivos para esta pend&ecirc;ncia");
					}
				}
			});
		} else {
			trocarDisplay(document.getElementById("linha_" + id));
			trocarDisplay(tabelaGeral);
		}
	}
	
	function buscarArquivosPendenciaSemEdicaoJSON(idPendencia){
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=18&id_pendencia='+idPendencia;
		
		criarArquivosPendenciaTabela(idPendencia);
		var corpoTabela = "";
		corpoTabela +='<td>' + 'JsoNdesc9' + '</td>';
		corpoTabela +='<td name="' + 'JsoNsubstitute11' + '">';
		corpoTabela +='JsoNdesc11' + '</td>';	
	
		corpoTabela +='<td name="' + 'JsoNsubstitute60'  + 'JsoNdesc6' + '">';
		corpoTabela += 'Sim' + '</td>';
	
		corpoTabela +='<td name="' + 'JsoNsubstitute200'  + '">';
		corpoTabela += '<a target=\"_blank\" title=\"Documento sem valor jur&iacute;dico, pois n&atilde;o possui c&oacute;digo nos termos do provimento 10/2013 da CGJ\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash'+'\">' + 'JsoNdesc2' + '</a></td>';
	
		corpoTabela +='<td class="Centralizado" name="' + 'JsoNsubstitute30'  + 'JsoNdesc3' + '">';
		corpoTabela +='<a target=\"_blank\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;CodigoVerificacao=true\">';
		corpoTabela +='<img src=\"imagens/22x22/' + 'btn_pdf.png' + '\" alt=\"Selecionar\" title=\"' + 'Documento com selo eletronico' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
	
		corpoTabela +='<td class="Centralizado" name="' + 'JsoNsubstitute30'  + 'JsoNdesc3' + '">';
		corpoTabela +='<a target=\"_blank\" href="PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;recibo=true">' + 'Recibo' + '</a></td>';
	
		corpoTabela +='<td align="center" name="' + 'JsoNsubstitute120'  + 'JsoNdesc12' + '">';
		corpoTabela +='---';
		corpoTabela +='</td>';
		
		buscarArquivosPendenciaJsonAjax(url, corpoTabela, idPendencia, buscarArquivosPendenciaJsonCallBack);
	}
	
	function buscarArquivosPendenciaJsonCallBack(){
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
	                		    parametroSubstituir = 'JsoN'+parametro+'(?![0-9])';
	                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
	                		}
	                		corpoTabelaJSON +='</tr>';
	                	}
	            	}
	            });
	            	tabela.append(corpoTabelaJSON);
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
	
	function buscarArquivosPendenciaFinalizada(id, link, campo, pa){
		var idTab = "arquivos_" + id;
		var tb = document.getElementById(idTab);
		
		var tabelaGeral = document.getElementById("pai_" + id);
	
		if (tb == null){	
			Pendencia.consultarArquivosPendenciaFinalizada(id, {
				callback:function (lista){
					if (lista != null){
						var tabelaArquivos = new TabelaArquivosFinalizados(idTab, "pai_" + id, link, campo, pa, modoEdicaoPendencia, true, possivelMarcar, possivelCopiar);
						tabelaArquivos.criar(lista);
						trocarDisplay(document.getElementById("linha_" + id));
					} else {
						mostrarMensagemErro("Projudi - Erro","N&atilde;o h&Aacute; arquivos para esta pend&ecirc;ncia");
					}
				}
			});
		} else {
			trocarDisplay(document.getElementById("linha_" + id));
			trocarDisplay(tabelaGeral);
		}
	}
	
	function buscarArquivosPendenciaFinalizadaJSON(idPendencia){
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=20&id_pendencia='+idPendencia;
	
		criarArquivosPendenciaTabela(idPendencia);
		var corpoTabela = "";
		
	//	ver modoEdicaoPendencia
	
		corpoTabela +='<td>' + 'JsoNdesc9' + '</td>';
		corpoTabela +='<td name="' + 'JsoNsubstitute11' + '">';
		corpoTabela += 'JsoNdesc11' + '</td>';
	
		corpoTabela +='<td name="' + 'JsoNsubstitute60'  + 'JsoNdesc6' + '">';
		corpoTabela += 'Sim' + '</td>';
	
		corpoTabela +='<td><a target=\"_blank\" title=\"Documento sem valor jur&iacute;dico, pois n&atilde;o possui c&oacute;digo nos termos do provimento 10/2013 da CGJ\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' + '&amp;finalizado=true'  +'\">' + 'JsoNdesc2' + '</a></td>';
	
		corpoTabela +='<td align="center">';
		corpoTabela +='<a target=\"_blank\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;CodigoVerificacao=true&amp;finalizado=true\">';
		corpoTabela +='<img src=\"imagens/22x22/' + 'btn_pdf.png' + '\" alt=\"Selecionar\" title=\"' + 'Documento com selo eletronico' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
	
		corpoTabela +='<td name="' + 'JsoNsubstitute30'  + 'JsoNdesc3' + '">';
		corpoTabela +='<a target=\"_blank\" href="PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;recibo=true&amp;finalizado=true">' + 'Recibo' + '</a></td>';
	
		corpoTabela +='<td align="center" name="' + 'JsoNsubstitute120'  + 'JsoNdesc12' + '">';
		corpoTabela +='<a href=\"#\" onclick=\"editarPendencia(' + 'JsoNid' + ', \''+ 'JsoNhash' +'\' , ' + 'JsoNdesc8' + ', \'' + 'JsoNdesc9' + '\', \'' + 'JsoNdesc2' + '\')\">';
		corpoTabela +='<img src=\"imagens/22x22/' + 'ico_editar.png' + '\" alt=\"Editar\" title=\"' + 'Editar o arquivo n&atilde;o assinado' + '\" \/>';
		corpoTabela +='Editar<\/a>';
		corpoTabela +='</td>';
	
		buscarArquivosPendenciaJsonAjax(url, corpoTabela, idPendencia, buscarArquivosPendenciaJsonCallBack);
	}
	
//	/**
//	 * Busca os arquivos assinados de uma pendencia e adiciona no grid de arquivos
//	 * @author Ronneesley Moura Teles
//	 * @since 21/01/2009 16:16
//	 * @param int id, id da pendencia
//	 * @param String link, link para o controle
//	 * @param String campo, campo para a associacao
//	 * @param int pa, pagina atual
//	 */
	function criarArquivosAssinadosPendenciaTabela(idPendencia){
		var definicaoTabela = "";
		definicaoTabela +='<fieldset id="arquivos_' + idPendencia + '">';
		definicaoTabela +='<table id="arquivos_' + idPendencia + '_tabela" class="Tabela" border="0">';
		definicaoTabela +='<thead><tr><th>Tipo Arquivo</th><th>Assinador</th><th>Visualiza&ccedil;&atilde;o</th><th>Impress&atilde;o</th><th>Recibo</th></tr></thead>';
		definicaoTabela +='<tbody  id="TabelaPendencia' + idPendencia + '" class="Tabela">';
		definicaoTabela +='</tbody>';
		definicaoTabela +='</table>';
		definicaoTabela +='</fieldset>';
		
		var tdPai =  $(''+'#pai_'+idPendencia+'');
		var trLinha =  $(''+'#linha_'+idPendencia+'');
		tdPai.html(definicaoTabela);
		if($(trLinha).is(":visible")){
			trLinha.hide();
		}else{
			trLinha.show();
		}   
	}
	
	function buscarArquivosAssinadosPendenciaJSON(idPendencia){
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=22&id_pendencia='+idPendencia;
		criarArquivosAssinadosPendenciaTabela(idPendencia);
		var corpoTabela = "";
		corpoTabela +='<td>' + 'JsoNdesc9' + '</td>';
		corpoTabela +='<td name="' + 'JsoNsubstitute11' + '">';
		corpoTabela +='JsoNdesc11' + '</td>';
	
		corpoTabela +='<td name="' + 'JsoNsubstitute200'  + '">';
		corpoTabela += '<a target=\"_blank\" title=\"Documento sem valor jur&iacute;dico, pois n&atilde;o possui c&oacute;digo nos termos do provimento 10/2013 da CGJ\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash'+'\">' + 'JsoNdesc2' + '</a></td>';
	
		corpoTabela +='<td align="center">';
		corpoTabela +='<a target=\"_blank\" href=\"PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;CodigoVerificacao=true\">';
		corpoTabela +='<img src=\"imagens/22x22/' + 'btn_pdf.png' + '\" alt=\"Selecionar\" title=\"' + 'Documento com selo eletronico' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
	
		corpoTabela +='<td class="Centralizado" name="' + 'JsoNsubstitute30'  + 'JsoNdesc3' + '">';
		corpoTabela +='<a target=\"_blank\" href="PendenciaArquivo?PaginaAtual=6&amp;Id_PendenciaArquivo=' + 'JsoNid' + '&amp;hash='+ 'JsoNhash' +'&amp;recibo=true">' + 'Recibo' + '</a></td>';
	
		buscarArquivosPendenciaJsonAjax(url, corpoTabela, idPendencia, buscarArquivosPendenciaJsonCallBack);
	}
	
	function buscarArquivosAssinadosPendencia(id, link, campo, pa){
		var idTab = "arquivos_" + id;
		var tb = document.getElementById(idTab);
		
		var tabelaGeral = document.getElementById("pai_" + id);
	
		if (tb == null){	
			Pendencia.consultarArquivosAssinadosPendencia(id, {
				callback:function (lista){
					if (lista != null){
						var tabelaArquivos = new TabelaArquivos(idTab, "pai_" + id, link, campo, pa, false, false, possivelMarcar, possivelCopiar);
						tabelaArquivos.criar(lista);
						trocarDisplay(document.getElementById("linha_" + id));
					} else {
						mostrarMensagemErro("Projudi - Erro","N�o h� arquivos para esta pend�ncia");
					}
				}
			});
		} else {
			trocarDisplay(document.getElementById("linha_" + id));
			trocarDisplay(tabelaGeral);
		}
	}
	
/**
 * Lista todas as movimentacoes e os arquivos de movimentacao JSON
 * @author JESUS RODRIGO
 * @since 26/11/2013 13:19   
 */
function gerarIndice(){
	
	var timer;
	var dadosIndice =  $('#dadosIndice');	
	dadosIndice.html('');
	var boFecharDialog = false;
	$.ajax({
		url: 'BuscaProcesso?PaginaAtual=9&PassoBusca=5&AJAX=AJAX',
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){
			var inLinha=1;			
			var totalPaginas =0;
			var corpoTabela = "";
			var ul = "<ul><li>Sum&Aacute;rio Processo</li>";
			
			$.each(retorno, function(i,item){
												
				var dataSelecoes="";
				var iColuna=2;
				
				ul += "<ul><li><b>" + (i+1) + "</b> - " +  item.titulo ;
				if (item.complemento!="null")
					ul += " - " + item.complemento;
											
				var li = "";
				if (item.arquivos!=null){
					li+="<ul>"
					$.each(item.arquivos, function(i,item2){											
						li += "<li> <a href=\"BuscaProcesso?PaginaAtual=6&Id_MovimentacaoArquivo=" + item2.id_movi_arq +"&hash=" + item2.hash +  "\" target='_blank' >" +  item2.nome.replace(".p7s","") + " </a></li>";	
					});
					li+="</ul>"
				}
				
				ul +=li + "</li></ul>";													         													
				
			});
			dadosIndice.append(ul + "</ul>");						 
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
				mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
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
			calcularTamanhoIframe();
			
			var dadosIndice =  $('#dadosIndice');												
			dadosIndice.attr("buscaDados",'N&atilde;o');
		  }
	});
}
	
function MensagemImagemBloqueio(valor ){
	var resposta =['',''];
	if (valor=='1'){
		resposta = ['./imagens/22x22/ico_publico.png','Arquivo P�blico'];
	}else if (valor=='2'){
		resposta = ['./imagens/22x22/ico_basico.png','Arquivo Normal'];
	}else if (valor=='3'){
		resposta = ['./imagens/22x22/ico_adv.png','Visível aos Advs, Delegacia, MP, Cartório e Magistrad'];
	}else if (valor=='4'){
		resposta = ['./imagens/22x22/ico_delegacia.png','Visível &agrave; Delegacia, MP, Cartório e Magistrado'];
	}else if (valor=='5'){
		resposta = ['./imagens/22x22/ico_mp.png','Visível ao MP, Cartório e Magistrado'];
	}else if (valor=='6'){
		resposta = ['./imagens/22x22/ico_cartorio.png','Visível ao Cartório e Magistrado'];
	}else if (valor=='7'){
		resposta = ['./imagens/22x22/ico_juiz.png','Visível somente ao Magistrado'];
	}else if (valor=='8'){
		resposta = ['./imagens/22x22/ico_bloqueio.png','Arquivo Bloqueado'];
	}else if (valor=='9'){
		resposta = ['./imagens/22x22/ico_bloqueio.png','Arquivo Bloqueado pro Vírus'];
	}else if (valor=='10'){
		resposta = ['./imagens/22x22/ico_bloqueio.png','Arquivo Bloqueado por Erro de Migra&ccedil;&atilde;o'];
	}
	return resposta;
}
	
/**
 * Busca os arquivos de movimentacao JSON
 * @author JESUS RODRIGO
 * @since 28/11/2013 13:19  
 * @param int id, id da pendencia
 * @param String link, link para o controle
 * @param String campo, campo para a associacao
 * @param int pa, pagina atual
 * @param podeBloquear, define se Usu&Aacute;rio que est� consultando tem permiss�o para blouquear arquivos 
 */
function buscarArquivosMovimentacaoJSON(id, link, campo, paginaAtual, podeBloquear){
	if (podeBloquear=='true') podeBloquear = true;
	
	var linha = $("#linha_"+id);		
	
	if (linha.prop("carregado")=="sim"){
		if (linha.is(":visible")){ 
			linha.hide("slow"); 
			$('#MostrarArquivos_'+id).attr('src', 'imagens/22x22/go-bottom.png');
		} else{
			linha.show("slow");
			$('#MostrarArquivos_'+id).attr('src', 'imagens/22x22/go-top.png');
		}
		return;
	}
	var boFecharDialog = false;
	$.ajax({
		url: 'MovimentacaoArquivo?AJAX=ajax&PaginaAtual=8&Id_Movimentacao='+id,
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){			
			var corpoDiv = $("#pai_"+id);
			var id_proc = $("#id_proc");			
			var ul = "<ul class='lista_arquivos'>";
			var divs = "";
									
			$.each(retorno, function(i,item){
																
				var resposta = MensagemImagemBloqueio(item.AcessoArquivo );
				
				var imgBloqueio = resposta[0];
				var txtBloquio = resposta[1];
				
																														
				divs = "<li class='alinhada serrilhado'>";
				//Quando um arquivo estiver v�lido mostra links 
				//texto do title conforme ata de reuniao CGJ sobre provimento 10/2013
				//o Usu&Aacute;rio tem acesso
				if (item.AcessoUsuario>=item.AcessoArquivo){
										
					divs += "<div class='coluna20' title='Arquivo Tipo'    alt='Arquivo Tipo'>" + item.arquivo_tipo + "</div>";
					divs += "<div class='coluna60'>"
					divs += "   <div class='coluna60'><a target='_blank' title=\""+ item.nome_arquivo.replace('.p7s','') + " Documento sem valor jur&iacute;dico, pois N&atilde;o possui c&oacute;digo nos termos do provimento 10/2013 da CGJ\"  href=\"" + link + "?PaginaAtual=" + paginaAtual + "&" + campo + "=" + item.id + "&hash=" + item.hash + "&id_proc=" +id_proc.attr('value') + "\">" + limitaString(item.nome_arquivo.replace('.p7s',''),30)  + "</a></div> <br> ";
					divs += "   <div class='coluna60' title='Usu&Aacute;rio que Assinou' alt='Usu&Aacute;rio que Assinou'>" + limitaString(item.usuario_assinador,30) + "</div> ";					
					divs += "</div>"
						
					divs += " <div class='dropdown coluna5 dropArquivos' id_arquivo='"+item.id+"' nome_arquivo='"+item.nome_arquivo +"' bloqueavel='"+(item.AcessoArquivo==2)+"' img_bloqueio='"+imgBloqueio+"' txt_bloqueio='"+txtBloquio+"'>";
					divs += "		<img id='imgValidar' src='" + imgBloqueio + "' alt='"+ txtBloquio + "' title='"+ txtBloquio + "'  /> ";
					divs += "</div>";							
				  				
					//alert(item.codigo_temp);
					if (item.codigo_temp == 0) {
						if (item.recibo == 'true') {
							if (item.id_arquivo_tipo != 222) { // Aviso de Recebimento-Correios
								divs += "<div class='coluna5'><div title=\"Documento com selo digital\" align=\"center\"><a target='_blank' href=\"" + link + "?PaginaAtual=" + paginaAtual + "&" + campo + "=" + item.id + "&hash=" + item.hash + "&CodigoVerificacao=true\"><img src='imagens/22x22/btn_pdf.png'/></a></div></div>";		
							}
							divs += "<div class='coluna5' title='Recibo'  alt='Recibo'><a target='_blank' href=\"" + link + "?PaginaAtual=" + paginaAtual + "&" + campo + "=" + item.id + "&hash=" + item.hash + "&recibo=true\"><img src='imagens/22x22/contact-new.png'/></a></div>";
						}	
					}
					//Se for poss�vel copiar
					if (possivelCopiar == true){
						if (item.valido== 'true' && item.codigo_temp == 0){
							divs += "<div class='coluna5'><img id='imgCopiar' class='imgCopiar' src='imagens/22x22/edit-copy.png' alt='Copiar Arquivo para Area de transfer&ecirc;ncia' title='Copiar Arquivo para Area de transfer&ecirc;ncia'  onclick=\"javascript: incluirMovimentacaoArquivoAreaTransferencia('" + item.id + "','" + item.nome_arquivo + "','" + item.hash + "');\" /></div>";
							divs += "<div class='coluna5'><img id='imgLimpar' class='imgLimpar' src='imagens/22x22/edit-clear.png' alt='Limpar Arquivos da Area de transfer&ecirc;ncia' title='Limpar Arquivos da Area de transfer&ecirc;ncia'  onclick=\"javascript: limparArquivosAreaTransferencia();\" /></div>";
						} else {
							divs += "<div class='coluna5'></div>";
							divs += "<div class='coluna5'></div>";
						}
					}
				} else {
					//linha.push("<span class='bloqueado' title='Arquivo Bloqueado'>" + (obj.arquivoDt.nomeArquivoFormatado == ""?"Arquivo sem nome":obj.arquivoDt.nomeArquivoFormatado) + "</span>");
					divs += "<div class='coluna60'><span class='bloqueado' alt='"+ txtBloquio + "' title='"+ txtBloquio +"'>"+ txtBloquio + "</span></div>";
					divs += "  <div class='dropdown coluna5 dropArquivos' id_arquivo='"+item.id+"' nome_arquivo='"+item.nome_arquivo +"' bloqueavel='"+(item.AcessoArquivo==2)+"' img_bloqueio='"+imgBloqueio+"' txt_bloqueio='"+txtBloquio+"'>";
					divs += "		<img id='imgValidar' src='" + imgBloqueio + "' alt='"+ txtBloquio + "' title='"+ txtBloquio + "'  /> ";
					divs += "  </div>";								
				}
																
				divs += "</li>";
				ul += divs;
			});
			corpoDiv.append(ul + "</ul>" );								
		},
		beforeSend: function(data ){
			boMostrar=true;
			timer = setTimeout(function() {
				mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');	
				boFecharDialog=true;
			}, 1500);
			$("#formLocalizarBotao").hide();
		},
		error: function(request, status, error){	
			boFecharDialog=false;
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}								
		  }, 
		complete: function(data ){
					
			clearTimeout(timer);

			linha.show( "slow" );
			linha.prop("carregado",'sim');
			$('#MostrarArquivos_'+id).attr('src', 'imagens/22x22/go-top.png');
			MostrarOpcoes();
			
			if (boFecharDialog){
				$("#dialog").dialog('close');
			}
																					
		  }
	});
	
}
	
//	/**
//	 * Busca os arquivos de movimentacao
//	 * @author Ronneesley Moura Teles
//	 * @since 24/09/2008 14:07  
//	 * @param int id, id da pendencia
//	 * @param String link, link para o controle
//	 * @param String campo, campo para a associacao
//	 * @param int pa, pagina atual
//	 * @param podeBloquear, define se usu�rio que est� consultando tem permiss�o para blouquear arquivos 
//	 */
	function buscarArquivosMovimentacao(id, link, campo, pa, podeBloquear){
		alert("function buscarArquivosMovimentacao");
		if (podeBloquear=='true') podeBloquear = true;
		
		var idTab = "arquivos_" + id;
		var tb = document.getElementById(idTab);
		
		var tabelaGeral = document.getElementById("pai_" + id);
	
		if (tb == null){	
			MovimentacaoArquivo.consultarArquivosMovimentacao(id, {
				callback:function (lista){
					var tabelaArquivos = new TabelaArquivos(idTab, "pai_" + id, link, campo, pa, false, false, false, podeBloquear, possivelCopiar);
					tabelaArquivos.criar(lista);
					trocarDisplay(document.getElementById("linha_" + id));
				}
			});
		} else {
			trocarDisplay(document.getElementById("linha_" + id));
			trocarDisplay(tabelaGeral);
		}
	}
	
	
	function buscarArquivosMovimentacaoEventoExecucaoJSON(idEventoExecucao, id, movimentacaoDataRealizacao, link, campo, pa, podeBloquear){
		alert("buscarArquivosMovimentacaoEventoExecucaoJSON");
		
		url = 'MovimentacaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=3&id_MovimentacaoArquivo='+id_Movimentacao;
		
		var boMostrar = false;
	    $.ajax({
	        url: url,
	        context: document.body,
	        timeout: 25000,
	        success: function(retorno){
	//        	alert('OK -'+url);
	        },        
	        error: function(request, status, error){
	        	mostrarMensagemErro("Projudi - Erro", request.responseText);
	        },
	    });
	}
	
//	/**
//	 * Busca os arquivos de movimentacao referente aos eventos execu��o
//	 * @author Karla Riccioppo
//	 * @since 08/04/2010 
//	 * @param int idEventoExecucao, id do evento da execu��o
//	 * @param int id, id da movimenta��o
//	 * @param String link, link para o controle
//	 * @param String campo, campo para a associacao
//	 * @param int pa, pagina atual
//	 * @param podeBloquear, define se usu�rio que est� consultando tem permiss�o para blouquear arquivos
//	 *  
//	 */
	function buscarArquivosMovimentacaoEventoExecucao(idEventoExecucao, id, movimentacaoDataRealizacao, link, campo, pa, podeBloquear){
	//	alert("buscarArquivosMovimentacaoEventoExecucao");
	//	if (podeBloquear=='true') podeBloquear = true;
	//	 
	//	var idTab = "arquivos_" + idEventoExecucao;
	//	var tb = document.getElementById(idTab);
	//	
	//	var tabelaGeral = document.getElementById("pai_" + idEventoExecucao);
	//	if (tb == null){	
	//		MovimentacaoArquivo.consultarArquivosMovimentacao(id, {
	//			callback:function (lista){
	//				for (var i = 0; i < lista.length; i++){
	//			 	 	var obj = lista[i]; //Objeto do laco
	//
	//			 	 	obj.movimentacaoTipo = movimentacaoDataRealizacao + " - " + obj.movimentacaoTipo;
	//				}
	//				var tabelaArquivos = new TabelaArquivosEventoExecucao(idTab, "pai_" + idEventoExecucao, link, campo, pa, false, false, false, podeBloquear);
	//				tabelaArquivos.criar(lista);
	//				trocarDisplay(document.getElementById("linha_" + idEventoExecucao));
	//			}
	//		});
	//	} else {
	//		trocarDisplay(document.getElementById("linha_" + idEventoExecucao));
	//		trocarDisplay(tabelaGeral);
	//	}
		buscarArquivosMovimentacaoEventoExecucaoJSON(idEventoExecucao, id, movimentacaoDataRealizacao, link, campo, pa, podeBloquear);
	}
	
	function incluirMovimentacaoArquivoAreaTransferenciaJSON(id_MovimentacaoArquivo, descricao, hash){
		alert("incluirMovimentacaoArquivoAreaTransferenciaJSON:"+id_MovimentacaoArquivo+";"+descricao+";"+hash);
		
		url = 'MovimentacaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=1&id_MovimentacaoArquivo='+id_MovimentacaoArquivo+'&hash='+hash;
		
		
		var boMostrar = false;
	    $.ajax({
	        url: url,
	        context: document.body,
	        timeout: 25000,
	        success: function(retorno){
	//        	alert('OK -'+url);
	        },        
	        error: function(request, status, error){
	        	mostrarMensagemErro("Projudi - Erro", request.responseText);
	        },
	    });
		
	
		
	//	buscarArquivosPendenciaJsonAjax(url, corpoTabela, idPendencia, buscarArquivosPendenciaJsonCallBack);
		
		
	}
	
	function incluirMovimentacaoArquivoAreaTransferencia(id_MovimentacaoArquivo, descricao, hash){
	//	alert("function incluirMovimentacaoArquivoAreaTransferencia");
	//	if (id_MovimentacaoArquivo != null & id_MovimentacaoArquivo > 0){
	//		MovimentacaoArquivo.incluirMovimentacaoArquivoAreaTransferencia(id_MovimentacaoArquivo, hash, {
	//			callback:function (valor){
	//				if (valor != null && valor == true) {
	//					alert("Arquivo " + descricao + " copiado com sucesso para a area de transfer?ncia no servidor");
	//				} else {
	//					alert("Arquivo " + descricao + " n?o copiado para a area de transf?ncia, favor tentar novamente");
	//				}
	//			}, 
	//			timeout:120000, errorHandler:function (error) { alert(error); }
	//		});			
	//	} else {
	//		alert("N?o h? arquivo selecionado");
	//	}
		
		incluirMovimentacaoArquivoAreaTransferenciaJSON(id_MovimentacaoArquivo, descricao, hash);
		
	}
	
	function limparArquivosAreaTransferenciaJSON(){
	//	alert("limparArquivosAreaTransferenciaJSON:");
		
		url = 'MovimentacaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=2';
			
		var boMostrar = false;
	    $.ajax({
	        url: url,
	        context: document.body,
	        timeout: 25000,
	        success: function(retorno){
	//        	alert('OK -'+url);
	        },        
	        error: function(request, status, error){
	        	mostrarMensagemErro("Projudi - Erro", request.responseText);
	        },
	    });
	}
	
	
	function limparArquivosAreaTransferencia(){
	//	alert("function limparArquivosAreaTransferencia");
	//	MovimentacaoArquivo.limparArquivosAreaTransferencia({
	//		callback:function (retorno){
	//			if (retorno != null && retorno == true) {				
	//				alert("Arquivos removidos da area de transfer?ncia com sucesso");
	//			} else {
	//				alert("N?o existem arquivos na area de transfer?ncia do servidor");
	//			}
	//		}, 
	//		timeout:120000, errorHandler:function (error) { alert(error); }
	//	});	
		
		limparArquivosAreaTransferenciaJSON();
	}	
	
	
/**
 * Gera menu de navega��o dos arquivos JSON
 * @author JESUS RODRIGO
 * @since 18/03/2015 13:49   
 */
function gerarMenuNavegacao(){
	
	var timer;
	dadosIndice =  $('#menuNavegacao');
	
	//variavel para limitar a busca pelos dados da navega��o
	//se for diferente de null, mosta a navega��o
	//caso contrario faz a busca			
	if (DadosNavegacao!=null){
		var iframe =  $('#iframe');
        iframe.html('<iframe width="100%" height="100%" frameborder="0" scrolling="auto" id="iframe"  id="arquivo" name="arquivo"> </iframe>');
        dadosIndice.append(DadosNavegacao);
        return;
	}
	
	var ul = "<ul id='indice'><li>Sum&aacute;rio Movimenta&ccedil;&atilde;o</li>";
	var boFecharDialog = false;
	
	$.ajax({
		url: 'BuscaProcesso?PaginaAtual=9&PassoBusca=5&AJAX=AJAX',
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){
			var inLinha=1;			
			var totalPaginas =0;
			var corpoTabela = "";			
			
			$.each(retorno, function(i,item){
												
				var dataSelecoes="";
				var iColuna=2;
				
				ul += "<ul><li><b>" + (i+1) + "</b> - " +  item.titulo ;
				if (item.complemento!="null")
					ul += " - " + item.complemento;
											
				var li = "";
				if (item.arquivos!=null){
					li+="<ul>"
					$.each(item.arquivos, function(i,item2){											
						li += "<li> <a href=\"BuscaProcesso?PaginaAtual=6&Id_MovimentacaoArquivo=" + item2.id_movi_arq +"&hash=" + item2.hash +  "\" target='arquivo' >" +  item2.nome.replace(".p7s","") + " </a></li>";	
					});
					li+="</ul>"
				}
				
				ul +=li + "</li></ul>";													         													
				
			});
			DadosNavegacao = ul + "</ul>";						 
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
			dadosIndice.append(DadosNavegacao);			
									
		  }
	});
}	

</script>