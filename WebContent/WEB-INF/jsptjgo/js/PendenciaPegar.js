<script type="text/javascript"> 
	function buscarPai(idPendencia) {
		if ($("#pendencia_pai_" + idPendencia).get(0) != null ) {
			return;
		}
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=16&id_pendencia='+idPendencia;
	
		var corpoTabela = "";
		corpoTabela +='<tr class="primeiraLinha" id="pendencia_pai_' + idPendencia + '" name="pendencia_pai_' + idPendencia + '">';
		corpoTabela +='<td class="colunaMinima">' + 'JsoNid' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc1' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
		
		corpoTabela +='<td class="colunaMinima">';
		corpoTabela +='<a href=\"javascript:buscarArquivosPendenciaFinalizadaJSON(' + 'JsoNid' + ', \'PendenciaArquivo\', \'Id_PendenciaArquivo\', 6);\">';
		corpoTabela +='<img src=\"imagens\/22x22\/' + 'ico_arquivos.png' + '\" alt=\"Mostrar ou ocultar arquivos\" title=\"' + 'Mostrar ou ocultar arquivos' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
		
		corpoTabela +='<td class="colunaMinima">';
		corpoTabela +='<a href=\"javascript:buscarPai(' + 'JsoNid' + ');\">';
		corpoTabela +='<img src=\"imagens\/22x22\/' + 'ico_carregar_pai.png' + '\" alt=\"Carregar\" title=\"' + 'Carregar pend&ecirc;ncia que deu origem a esta' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
		corpoTabela +='</tr>';
		
		corpoTabela +='<tr id="linha_' + 'JsoNid' + '" style="display: none;">';
		corpoTabela +='<td colspan="5" id="pai_' + 'JsoNid' + '" class="Linha"></td>';
		corpoTabela +='</tr>';
		
		buscarPaiJSON(url, corpoTabela);
	}
	
	function buscarPaiJSON(url, corpoTabela) {
		var teste = false;
	    $.ajax({
	        url: url,
	        context: document.body,
	        timeout: 25000,
	        success: function(retorno){
	        	var tabela =  $('#TabelaArquivos');
	            var corpoTabelaJSON = "";
	            
	            $.each(retorno, function(i,item){
	    					teste = true;
	
	    					corpoTabelaJSON += corpoTabela;
	                		for (parametro in item) {
	                		    parametroSubstituir = 'JsoN'+parametro;
	                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
	                		}
	            });
	            tabela.append(corpoTabelaJSON);
	        },
	        beforeSend: function(data ){
				mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados ...');
	        },
	        error: function(request, status, error){
	        	mostrarMensagemErro("Projudi - Erro", request.responseText);
	        },
	        complete: function(data ){
	        	if(teste != true){
	        		mostrarMensagemErro('Projudi - Erro', 'Esta pend&ecirc;ncia n&atilde;o possui uma pend&ecirc;ncia anterior.');
	        	} else {
	        		$("#dialog").dialog('close');			
	        	}
				$("#formLocalizarBotao").show();
	        }
	    });	
	}
	
	function editarPendencia(id_pendenciaArquivo, hash, id_AquivoTipo, arquivoTipo, nomeArquivo){
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=19&id_pendenciaArquivo='+id_pendenciaArquivo+'&hash='+hash+'&id_ArquivoTipo='+document.getElementById('id_ArquivoTipo').value+'&arquivoTipo='+arquivoTipo;
		var boMostrar = false;
	    $.ajax({
	    	url: url,
	        context: document.body,
	        timeout: 25000,
	        success: function(retorno){
	        	var conteudo = '';
				conteudo = retorno.desc1;
				
				setTextoEditor(conteudo);
				
				var p1 = conteudo.indexOf('<!--Projudi');
	            var p2 = conteudo.indexOf('Projudi-->');
	            var texto2 = conteudo.substr(p1+11,p2-p1-11);
	            var partes = texto2.split(';')
	            for (i=0; i<partes.length;i++){
	                    var valor =partes[i].split(':');
	                   
	                    if (valor!=null && valor.length==2){
	                         AlterarValue(valor[0].trim(), valor[1].trim());
	                    }
	            }
				
			    $( "#abas" ).tabs( "option", "active", 1 );
				Mostrar("Editor");
				AlterarValue('id_ArquivoTipo', id_AquivoTipo);
				AlterarValue('arquivoTipo', arquivoTipo);
				AlterarValue('nomeArquivo', nomeArquivo);
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
</script>