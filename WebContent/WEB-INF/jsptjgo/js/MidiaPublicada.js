 <script type="text/javascript">
	var TIPO_ARQUIVO = "<%=UsuarioSessao.getTipoArquivoMidiaPublicada()%>";
	var TIPO_ENVIO_JS = "<%=UsuarioSessao.getTipoEnvioMidiaUploadJavaScript()%>"; 
	var TAMANHO_MAXIMO_ARQUIVO = "<%=UsuarioSessao.getTamanhoMBArquivoMidiaPublicada()%>";
	var QUANTIDADE_MAXIMA_ARQUIVOS = "<%=UsuarioSessao.getQuantidadeMaximaArquivosMidiaPublicada()%>";
	var VERBO_ENVIO_PUT_JS = "<%=UsuarioSessao.getObjectStorageUploadVerboPUT()%>"; 
	var FORCE_IFRAME_TRANSPORT_JS = "<%=UsuarioSessao.getObjectStorageUploadForceIframeTransport()%>"; 
	var TAMANHO_PARTE_ARQUIVO = "<%=UsuarioSessao.getTamanhoMBParteArquivoMidiaPublicada()%>";
	
	var KILOBYTE = 1024;
	var MEGABYTE = KILOBYTE * 1024;
	var TAMANHO_MAXIMO_MEGABYTES = MEGABYTE * TAMANHO_MAXIMO_ARQUIVO;	
	var TAMANHO_PARTE_MEGABYTES = MEGABYTE * TAMANHO_PARTE_ARQUIVO;
	var filesArr = [];
	//var __response = null;
			
	function selecionaArquivo(arquivos){
		filesArr = Array.prototype.slice.call(arquivos);			
		var tipo_arquivos = TIPO_ARQUIVO.split(',');
				
		var filesizeTotal = 0; 
		for(var i=0;i<filesArr.length;i++){ 
			var arquivo = filesArr[i];
			var nome = arquivo.name;
			var filesize = arquivo.size;
			filesizeTotal += filesize;
		    if (filesize > (TAMANHO_MAXIMO_MEGABYTES)) { 
		    	alert('Arquivo '+nome+' maior que ' + TAMANHO_MAXIMO_ARQUIVO + 'MB.');
				return false;
		    }
		    if (filesize == 0) { 
		    	alert('Arquivo ' + nome + ' esta vazio.');
				return false;
		    }
		    var boArquivoNaoPermitido=true			    
		    for (var x=0;x<tipo_arquivos.length;x++){
		    	var sufixo = nome.substring(nome.length-tipo_arquivos[x].length, nome.length).toLowerCase();
		    	if(sufixo==tipo_arquivos[x]){			    		 
		    		boArquivoNaoPermitido=false;
		    		break;
		    	}
		    }
		    if (boArquivoNaoPermitido){ 
		    	alert('Arquivo ' + nome + ' invalido. Deve ser: ' + TIPO_ARQUIVO + '.');
				return false;
		    }		    
		}
		
		/*if (filesizeTotal > (TAMANHO_MAXIMO_MEGABYTES)) { 
			alert('A soma total dos arquivos deve ser ate ' + TAMANHO_MAXIMO_ARQUIVO + 'MB.');
			return false;
	    }*/
		
		calcularTamanhoIframe();
		
		return true;
	}
	
	function valideUploadArquivo(){
		if (filesArr == null) {
			alert('Nenhum arquivo foi selecionado.');
			return false;
		}
		
		if (filesArr.length == 0) {
			alert('Nenhum arquivo foi selecionado.');
			return false;
		}
		
		if (filesArr.length > 1) {
			alert('Somente um arquivo deve ser selecionado.');
			return false;
		}
		
		if ($('#DataRealizacao').val().length == 0) {	
			alert('A data deve ser informada.');
			return false;
		} else if (!verifica_data_string($('#DataRealizacao').val())) {
			alert('Data invalida! Formato correto: dd/MM/yyyy.');
			return false;
		}
		
		if ($('#HoraRealizacao').val().length == 0) {	
			alert('A hora deve ser informada.');
			return false;
		}
		
		var url = 'MidiaPublicada?AJAX=ajax&PaginaAtual=' + <%=Configuracao.Salvar%>;
		$.ajax({
			url: encodeURI(url),
			method: "POST",  				
			data: { 'DataRealizacao': $('#DataRealizacao').val(), 'HoraRealizacao': $('#HoraRealizacao').val(), 'MovimentacaoComplemento': $('#MovimentacaoComplemento').val() },
			context: document.body,
			timeout: 300000,
			success: function(retorno){	 
				if (retorno.sucesso) {
					AlterarValue('__Pedido__',retorno.__Pedido__);
					Mostrar('divConfirmarSalvar');						
				} else {
					alert(retorno.erro);
					Ocultar('divConfirmarSalvar');
				}				
			},
			beforeSend: function(data){},
			error: function(request, status, error){
				alert(request.responseText); 
				Ocultar('divConfirmarSalvar');
			},
			complete: function(data ){}
		});	
		
		return true;
	}	
</script>