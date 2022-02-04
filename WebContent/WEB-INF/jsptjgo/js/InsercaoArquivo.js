<script type='text/javascript'>
	var lerSenha =  <%=!UsuarioSessao.isSenhaCertificado()%>;
	var adv_particular = <%=UsuarioSessao.isAdvogadoParticular()%>;
	var TIPO_ARQUIVO = "<%=UsuarioSessao.getTipoArquivoUpload()%>";

	var SEIS_MEGABYTES = 6291456;		
	var arquivoCache = { };		
	var opLimparArquivo = true;
	var opOcultarEditor = true;
	
	var arraynome=[];
	var arrayarquivo=[];
	var arraytipo=[];
	
	var totalDeArquivosInsercaoArquivoDwr;
   	var quantidadeAtualInsercaoArquivoDwr;
	var flagAnexando = false;	

	function ArquivoAssinado(nome, arquivo, id_tipo, tipo, chave, base64, assinado, gerar_assinatura, sernha_certificado, content_type, salvar_senha){
		this.nome = nome;
		this.arquivo = arquivo;
		this.id_tipo = id_tipo;
		this.tipo = tipo;
		this.chave = chave;
		this.base64 = base64;
		this.assinado = assinado;
		this.gerarAssinatura = gerar_assinatura;
		this.sernhaCertificado = sernha_certificado;
		this.contentType = content_type;
		this.salvarSenha = salvar_senha;			
	}
	
	
	function mostrarExt(){
		$( "#abasInsercaoArquivos" ).tabs( "option", "active", 0 );
		
	    $("#tipo_assinador").text("Upload de arquivo assinado");
        
        $("#gerarAssinatura").val('false');
        $("#assinado").val('true');
                                
	}
			
	function mostrarSer(){
		$( "#abasInsercaoArquivos" ).tabs( "option", "active", 1 );
		
		$("#tipo_assinador").text("Assinatura do lado do Servidor");            		            
        $("#gerarAssinatura").val('true');
        $("#assinado").val('false');
        
        //Mostrar('Editor');
        //alert('111111');
	}
	
	$(document).ready(function() {
		
	    //CKEDITOR.replace('Editor');
		$("#abasInsercaoArquivos").tabs();
		
		if (!lerSenha){
			$("#salvarSenhaCertificado").prop( "checked", true );
		}
		//salvar html vai ficar escondido para todos, somente o adv terá essa opção
		//$("#but_salvarHtml").hide();
		if (adv_particular){
			mostrarExt();				
			//$("#but_salvarHtml").show();
			//$("#but_assinar").hide();
		}else{ //ser
			mostrarSer();
		}
						      		  
		$("#botaoCancelar").click(function(){
			  $("#inc_confirma").fadeOut();
		});
		  
		$("#botaoAplicar").click(function(){
			$("#inc_confirma").fadeOut();
		});		  		 
		
		//atualizarArquivos();
	   
		//calcularTamanhoIframe();
		
	});	
	
	function iniciar() {
		//deve ser retira quando for retirada de todas as jsps
	}
	
	function gerarHTML() {
	  var text = CKEDITOR.instances.editor1.getData();
	  var a = document.createElement("a");
	  //var a = document.getElementById("a");
	  var file = new Blob([text], {type: 'text/html'});
	  url = URL.createObjectURL(file);
	  a.href = URL.createObjectURL(file);
	  var nomeArquivo = $("#nomeArquivo").val();  
	  if (nomeArquivo==undefined || nomeArquivo==""){
	  	nomeArquivo='online.html';
	  }else{
		  nomeArquivo+='.html';
	  }
	  a.download = nomeArquivo ;	
	  a.click();
	}
				
		  
	function incluirArquivoAssinatura(nome, arquivo){
  		arraynome.push(nome);
	  	arrayarquivo.push(arquivo);	  	
	}
	
	function getNomeArquivo(){
		return $("#nomeArquivo").val();
	}
	
//  		function getTextoAssinar(){
//  			var editor_data = CKEDITOR.instances.editor1.getData();  			
//  			return editor_data;
//  		}

	function temEditor(){
		var divEditor = $('#Editor');
		return (divEditor != null || divEditor != 'undefined') && divEditor.length > 0; 
	}
	
//  		function getTextoAssinar(){
// 			var editor_data = CKEDITOR.instances.editor1.getData();
//  			var nomeArquivo =   $("#nomeArquivo").val();  			
//  			return editor_data;
//  		}
	
	function abreDialogoUploadArquivoAssinado() {
		if (podeAssinar() == true) {
			preparaUploadArquivoAssinado();
			$('#fileupload').click();
		}
	}
	
	function preparaUploadArquivoAssinado(){	   	   	  			
//		dwr.engine.beginBatch();
//		InsercaoArquivo.setaAbaJQuery();
//		dwr.engine.endBatch();
	}		  
	  
	function selecionaArquivoAssinado(arquivos){
		var id_arquivo_tipo = $('#id_ArquivoTipo').val();
		var nome_arquivo_tipo = $('#arquivoTipo').val();						
		var filesArr = Array.prototype.slice.call(arquivos);			
		var tipo_arquivos = TIPO_ARQUIVO.split(',');
		
		for(var i=0;i<filesArr.length;i++){ 
			var arquivo = filesArr[i];
			var nome = arquivo.name;
			var filesize = arquivo.size;
		    if (filesize > (SEIS_MEGABYTES)) { 
				alert('Arquivo '+nome+' e maior que 6MB.');
				return false;
		    }
		    if (filesize == 0) { 
				alert('Arquivo '+nome+' esta vazio.');
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
				alert('Arquivo '+nome+' nao e de um tipo valido (' + TIPO_ARQUIVO + ')');
				return false;
		    }
		}
		var totalArquivos = filesArr.length;						
		adicionarUploadComSemAssinartura(filesArr,id_arquivo_tipo,nome_arquivo_tipo,1,totalArquivos);				
		
		return true;
	}
	  
	function removeArquivoAssinado(arquivo){
	    var nome = arquivo;
	    arquivo = arquivo.substring(0,arquivo.length-TIPO_ARQUIVO.length);
		dwr.util.setValue("nomeArquivo", arquivo);
	   	dwr.util.setValue("id_ArquivoTipo", null);
	   	dwr.util.setValue("arquivoTipo", null);	
	   	var dados = { id:-1, id_ArquivoTipo:null, arquivoTipo:null, nomeArquivo:null, arquivo:null, assinado:true };
  		dwr.util.getValues(dados);	 
  		dwr.engine.beginBatch();
  		InsercaoArquivo.removeArquivoAssinado(dados,{
  			callback:function(){}, timeout:600000, errorHandler:function (error) { alert(error);}
  			});
  		dwr.engine.endBatch();
	  	return true;
	}
	  
//	function cancelaArquivoAssinado(){
//	  	dwr.engine.beginBatch();
//  		InsercaoArquivo.cancelaArquivoAssinado();
//  		dwr.engine.endBatch();
//  		$('#botaoAplicar').attr('style','display:none');
//  		$('#botaoCancelar').attr('style','display:none');
//  		$('#botaoUpload').attr('style','display:block');
//  		$('#nomeArquivo').attr('value',''); 
//	  	$('#arquivoAssinado').attr('value','');	 
//	}
	  
	  
//	function submeteArquivoAssinado(){
//	  	dwr.engine.beginBatch();
//  		InsercaoArquivo.submeteArquivoAssinado();
//		mostrarMensagemConsultando("Anexando Arquivos...", "Aguarde, os arquivos estão sendo anexados.");
//		flagAnexando = true;
//  		atualizarArquivos();
//  		dwr.engine.endBatch();
//  		$('#botaoAplicar').attr('style','display:none');
//  		$('#botaoCancelar').attr('style','display:none');
//  		$('#botaoUpload').attr('style','display:block');
//  		$('#nomeArquivo').attr('value','');
//	  	$('#id_ArquivoTipo').attr('value','');
//	  	$('#arquivoTipo').attr('value','');	 
//	}
	  
	function podeAssinar(){
	  	var pode = $('#id_ArquivoTipo').attr('value');
	  	if (pode != null && pode != 'null' && pode != '' ){
	  		return true;	  			 
  		} else {
  			alert("Escolha o Tipo de Arquivo antes de assinar");
  			return false;
  		}		  	
	}
	  
	function configuraAssinador(){
		return "Adicionar=true;Limpar=true";
	}
  		
	function iniciarAssinatura(){	   	   	  
		arraynome=[];
		arrayarquivo=[];
		quantidadeAtualInsercaoArquivoDwr = 0;	
		totalDeArquivosInsercaoArquivoDwr = 0;
	}	

	function inserirArquivoSession() {	
		var arquivoHtml = "";
		
		if ($('#id_arquivo_session').length) {				
			id_arquivo_session= $("#id_arquivo_session").val();
		}else {
			mostrarMensagemErro("Erro", 'Não foi possível definir o texto a ser assinado'); 
			return;
		}
		
		var id_arquivo_tipo = $('#id_ArquivoTipo').val();
		var nome_arquivo_tipo = $('#arquivoTipo').val();
		var senhaCertificado =  $('#senhaCertificado').val();
		var arquivoHtmlNome =  $('#nomeArquivo').val();
		
		var salvarSenha=$("#salvarSenhaCertificado").is(':checked');		
		lerSenha=!salvarSenha;			
		var dados = 
		{ 
				'id':-1, 
				'id_ArquivoTipo':id_arquivo_tipo, 
				'arquivoTipo':nome_arquivo_tipo, 
				'nomeArquivo': arquivoHtmlNome, 
				'arquivo':id_arquivo_session, 
				'assinado':false, 
				'gerarAssinatura':true, 
				'senhaCertificado':senhaCertificado, 
				"contentType":"text/html",
				'salvarSenha':salvarSenha 
		};
		
	    var url = 'InsercaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=11';
		$.ajax({
			url: encodeURI(url),
			method: "POST",  				
			data: dados,
			context: document.body,
			timeout: 300000,
			success: function(retorno){	  				
  				limparArquivo();
  		  		
  		  		atualizarArquivos();
  		  		alert("Opera\u00e7\u00e3o finalizada com Sucesso!");
			},
			beforeSend: function(data ){},
			error: function(request, status, error){
				mostrarMensagemErro("Erro", request.responseText); 
  				lerSenha=true;
			},
			complete: function(data ){}
		});	
		
  	//limpo a senha 
		$('#senhaCertificado').val("");	
	}	
	
	function inserirArquivo(gerarAssinatura) {	
		var arquivoHtml = "";
		if (CKEDITOR.instances.editor1!=undefined) {
			arquivoHtml = CKEDITOR.instances.editor1.getData();				
		}else {
			mostrarMensagemErro("Erro", 'Não foi possível definir o texto a ser assinado'); 
			return;
		}
		
		var id_arquivo_tipo = $('#id_ArquivoTipo').val();
		var nome_arquivo_tipo = $('#arquivoTipo').val();
		var senhaCertificado =  $('#senhaCertificado').val();
		var arquivoHtmlNome =  $('#nomeArquivo').val();
		
		var arquivoHtmlEmenta = "";
		var id_arquivo_tipo_ementa = "";
		var nome_arquivo_tipo_ementa = "";
		var arquivoHtmlNome_ementa =  "";
		
		if (CKEDITOR.instances.editorEmenta!=undefined) {
			arquivoHtmlEmenta = CKEDITOR.instances.editorEmenta.getData();	
			id_arquivo_tipo_ementa = $('#id_ArquivoTipoEmenta').val();
			nome_arquivo_tipo_ementa = $('#arquivoTipoEmenta').val();
			arquivoHtmlNome_ementa =  $('#nomeArquivoEmenta').val();
		}
		
		var _gerarAssinatura = true;
		if (gerarAssinatura!=undefined && gerarAssinatura!=''){
			_gerarAssinatura = gerarAssinatura;
		}
		var salvarSenha=$("#salvarSenhaCertificado").is(':checked');		
		lerSenha=!salvarSenha;			
		var dados = 
		{ 
			'id':-1, 
			'id_ArquivoTipo':id_arquivo_tipo, 
			'arquivoTipo':nome_arquivo_tipo, 
			'nomeArquivo': arquivoHtmlNome, 
			'arquivo':arquivoHtml, 
			'assinado':false, 
			'gerarAssinatura':_gerarAssinatura, 
			'senhaCertificado':senhaCertificado, 
			"contentType":"text/html",
			'salvarSenha':salvarSenha,
			'id_ArquivoTipoEmenta':id_arquivo_tipo_ementa, 
			'arquivoTipoEmenta':nome_arquivo_tipo_ementa, 
			'nomeArquivoEmenta': arquivoHtmlNome_ementa, 
			'arquivoEmenta':arquivoHtmlEmenta,
		};
		
	    var url = 'InsercaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=4';
		$.ajax({
			url: encodeURI(url),
			method: "POST",  				
			data: dados,
			context: document.body,
			timeout: 300000,
			success: function(retorno){	
  				CKEDITOR.instances.editor1.setData("");
  				if (CKEDITOR.instances.editorEmenta!=undefined) {
  					CKEDITOR.instances.editorEmenta.setData("");
  				}
  				limparArquivo();
  		  		
  		  		atualizarArquivos();
  		  		alert("Opera\u00e7\u00e3o finalizada com Sucesso!");
			},
			beforeSend: function(data ){},
			error: function(request, status, error){
				mostrarMensagemErro("Erro", request.responseText); 
  				lerSenha=true;
			},
			complete: function(data ){}
		});	
		
  	//limpo a senha 
		$('#senhaCertificado').val("");	
	}	

//		function atualizarArquivos() {			
//	  		InsercaoArquivo.getTodosArquivos({
//	  		callback:function(Lista) {
//				dwr.util.removeAllRows("corpoTabelaArquivo", { filter:function(tr) { return (tr.id != "idArquivo");} });
//				var dados, id;
//				for (var i = 0; i < Lista.length; i++) {
//			  		dados = Lista[i];
//			  		id = dados.id;
//			  		dwr.util.cloneNode("idArquivo", { idSuffix:id });
//			  		//dwr.util.cloneNode("idArquivo", { idSuffix:id });
//			  		dwr.util.setValue("tableDescricao" + id, dados.arquivoTipo);
//			  		dwr.util.setValue("tableNome" + id, dados.nomeArquivo);
//			  		  
//			  		$("#idArquivo" + id).css("display", "table-row");
//			  		
//			  		$("#chk_idArquivo" + id).val(id);
//			  		
//			  		arquivoCache[id] = dados;
//				}
//				calcularTamanhoIframe();
//				if (flagAnexando){
//	  				$("#dialog").dialog("close");
//	  				flagAnexando = false;
//	  			}
//	  		}, timeout:600000, errorHandler:function (error) { flagAguarda = false;alert(error);	}
//	  		});
//		}

	function editarArquivo(eleid) {
  		var dados = arquivoCache[eleid.substring(4)];
//  		dwr.util.setValues(dados);
	}

//		/**
//		 * Exclui um determinado arquivo da lista
//		 *
//		 * Alteracoes quanto a confirmacao
//		 * @author Ronneesley Moura Teles
//		 * @since 21/10/2008 09:39
//		 */
//		function excluirArquivo(eleid, confirmar) {
//	  		var dados2 = arquivoCache[eleid.substring(7)];
//	  		
//	  		//Se for qualquer coisa diferente de falso, confirma a exclusao
//	  		if (confirmar != false)
//	  			if (!confirm("Deseja retirar o item: " + dados2.nomeArquivo + "?")) return false;
//	  	
//	  		var dados = { id:dados2.id, id_ArquivoTipo:dados2.id_ArquivoTipo, arquivoTipo:dados2.arquivoTipo, nomeArquivo:dados2.nomeArquivo, arquivo:dados2.arquivo, assinado:dados2.assinado };
//	  			  			
//			dwr.engine.beginBatch();
//			InsercaoArquivo.excluir(dados);	
//			atualizarArquivos();
//			dwr.engine.endBatch();
//			
//			return true;	  		
//		}


//		/**
//		 * Excluir arquivos selecionados na lista
//		 * @author Ronneesley Moura Teles
//		 * @since 21/10/2008 09:30
//		 */
//		function excluirSelecionados(tabela){
//			if (!confirm("Deseja realmente excluir os arquivos selecionados?")) return false;			
//						
//			var qtdExcluidos = 0;
//			
//			$("#" + tabela + " td > input:checked").each(function (i,a) {											
//				if (a.value != null && a.value != ""){
//					//Concatenacao com a palavra button2 deivod ao legado
//					if (excluirArquivo("button2" + a.value, false)) qtdExcluidos++;					
//				}							
//			});						
//			
//			alterarEstadoSelTodos(false);
//			
//			if (qtdExcluidos == 0) alert("Nenhum arquivo foi excluido");
//		}
	
	/**
	 * Incluir arquivo selecionado na area de transferencia do servidor
	 * @author Márcio Mendonça Gomes
	 * @since 11/09/2012
	 */
	function incluirArquivosAreaTransferencia(){					
		InsercaoArquivo.incluirArquivosAreaTransferencia({
			callback:function (retorno){
				if (retorno != null && retorno == true) {
					atualizarArquivos();
					alert("Arquivos inseridos com sucesso");
				} else {
					alert("Não existem arquivos na area de transferência do servidor");
				}
			}, 
			timeout:600000, errorHandler:function (error) { alert(error); }
		});		
			
	}
	
	/**
	 * Remove todos os arquivos da area de transferencia do servidor
	 * @author Márcio Mendonça Gomes
	 * @since 13/09/2012
	 */
	function limparArquivosAreaTransferencia(){			
		InsercaoArquivo.limparArquivosAreaTransferencia({
			callback:function (retorno){
				if (retorno != null && retorno == true) {						
					alert("Arquivos removidos da area de transferência com sucesso");
				} else {
					alert("Não existem arquivos na area de transferência do servidor");
				}
			}, 
			timeout:600000, errorHandler:function (error) { alert(error); }
		});	
	}	
	/**
	 * Enviar o arquivo do editor para o servidor para ser assinado e adicionado na lista de arquivo
	 * @author Jesus Rodrigo Corrêa
	 * @since 12/03/2020
	 */		
	function digitarSenhaCertificado(retornar){
		if (podeAssinar()==true){
			if (adv_particular){
				iniciarAssinaturaA3();
			}else {
				var senhaCertificado =  $('#senhaCertificado').val();			
				if(lerSenha==true && (senhaCertificado==undefined || senhaCertificado=="")){
					$("#divLerSenha").show();
					$( "#senhaCertificado" ).focus();
					$( "#but_senhaDigita").off('click').on('click',function() {digitarSenhaCertificado(retornar); });
					$( "#senhaCertificado").off('keypress').on('keypress', function( event ) { 
																		if ( event.which == 13 ) {
																			digitarSenhaCertificado(retornar);
																		}
																		});	
				}else{
					$("#divLerSenha").hide();
					retornar();
				}
			}
		}	
		return false;
	}

			
	/**
	 * Enviar o upload para o servidor para ser assinado e adicionado na lista de arquivo
	 * @author Jesus Rodrigo Corrêa
	 * @since 18/03/2020
	 */		
	function adicionarUploadComSemAssinartura(ArquivosUpload, id_arquivo_tipo, nome_arquivo_tipo, atual, quantidadeTotal ){
		var salvarSenha=$("#salvarSenhaCertificado").is(':checked');		
		if(ArquivosUpload.length>=1){
			//retiro o arquivo do array
			arquivo=ArquivosUpload.shift();
			var boAssinado = false;
			if(arquivo.name.includes('.p7s')){
				boAssinado=true;
			}
			var senhaCertificado =  $('#senhaCertificado').val();			
			if(!boAssinado && lerSenha==true && (senhaCertificado==undefined || senhaCertificado=="")){
				//incluo o arquivo novamente no array
				ArquivosUpload.push(arquivo);
				digitarSenhaCertificado(function(){ adicionarUploadComSemAssinartura(ArquivosUpload, id_arquivo_tipo, nome_arquivo_tipo, atual, quantidadeTotal); });					
			}else{
				mostrarMensagemConsultando("Envio de Arquivos", "Processando " + atual  + " de " + quantidadeTotal );//				    
				var reader = new FileReader();					
				reader.onloadend = function () {
					var contentType = arquivo.type;
					if (contentType==undefined || contentType==null || contentType==""){
						if (boAssinado){
							contentType='application/pkcs7-signature'; 
						}else if (arquivo.name.includes('.pdf')){
							contentType='application/pdf';
						}else if (arquivo.name.includes('.mp3')){
							contentType='audio/mpeg';
						}
					}					    	
				    var dados = { 'id':-1, 'id_ArquivoTipo':id_arquivo_tipo, 'arquivoTipo':nome_arquivo_tipo, 'nomeArquivo': arquivo.name, 'arquivo':reader.result, 'assinado':boAssinado, 'gerarAssinatura':!boAssinado, 'senhaCertificado':senhaCertificado, "contentType":contentType ,'salvarSenha':salvarSenha };
				    var url = 'InsercaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=4';
					$.ajax({
						url: encodeURI(url),
						method: "POST",  				
						data: dados,
						context: document.body,
						timeout: 300000,
						success: function(retorno){	
						adicionarUploadComSemAssinartura(ArquivosUpload, id_arquivo_tipo, nome_arquivo_tipo, (atual+1), quantidadeTotal);
						},
						beforeSend: function(data ){},
						error: function(request, status, error){
							mostrarMensagemErro("Erro", request.responseText);
				    		lerSenha=true;
				    		atualizarArquivos();
						},
						complete: function(data ){}
					});												   
				  }
				reader.readAsDataURL(arquivo);
			}
		}else{				
			lerSenha=!salvarSenha;
			$("#dialog").dialog('close');
			//dwr.util.setValues({ id:-1, 'id_ArquivoTipo':'null', 'arquivoTipo':'', 'nomeArquivo':'', 'arquivo':'null', 'id_Modelo':'null', 'modelo':'' });
			atualizarArquivos();
			//limpo a senha 
			$('#senhaCertificado').val("");	
		}
	}
			
	function atualizarArquivos(editavel) {
		console.log('InsercaoArquivo.js::atualizarArquivos()');
		var url = 'InsercaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=9';
		$.ajax({
			url: encodeURI(url),		
			context: document.body,
			timeout: 300000, 
			async: true,
			success: function(retorno){		
				var tabela =  $('#corpoTabelaArquivo');
				tabela.html('');
				var corpoTabela = "";
				$.each(retorno, function(i,item){
						corpoTabela +='<tr  id="idArquivo' +  i  + '" style="display: table-row;">';	     			
						if (editavel == null){
							corpoTabela +='<td  class="colunaMinima" ><input id="chk_idArquivo' + i  + '" name="arquivos" value="' +  (item.id == "" ? i : item.id)  + '" type="checkbox"></td>';
						}					
					corpoTabela +='<td><span id="tableDescricao' + i + '">' + item.arquivo_tipo + '</span></td>';
					corpoTabela +='<td><span id="tableNome' + i + '">' + item.arquivo_nome + '</span></td>';   					
					if (editavel == null){
						corpoTabela +='<td class="colunaMinima"><input id="button2' + i  + '" name="button2" title="Retirar este arquivo da lista" src="./imagens/imgExcluirPequena.png" onclick="excluirArquivo(\'' + item.eleid + '\',\''+ item.arquivo_nome +'\',\'true\'); return false;" type="image"></td>';
					}					
					corpoTabela +='</tr>';						
				});
				tabela.append(corpoTabela);				
			},
			beforeSend: function(data){},
			error: function(request, status, error){
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
			complete: function(data ){
				calcularTamanhoIframe();
			}
		});
	}


	function iniciarAssinaturaA3(){
		
		console.log('InsercaoArquivo.js::iniciarAssinaturaA3()');
		
		var arquivosLote = [];
		var botaoSalvar = false;
		var pode = $('#id_ArquivoTipo');
		
		arrayarquivo=[];
		inMsg = 0;
		
		if(!parent.isWebSocketConnect()) {
			mostrarMensagemErro('Erro ao conectar', 'N&atilde;o foi poss&iacute;vel encontrar o sistema de assinatura. Habilite-o e tente novamente.');
			return;
		}
		
//		if (parent.websocket.readyState == WEBSOCKET_CLOSE_STATE){
//		   
//		}
		
		if (pode.length >0){
		   if (pode.attr('value') == null || pode.attr('value') == 'null' || pode.attr('value') == '' ){
			   mostrarMensagemErro('Erro ao iniciar a assinatura', 'Escolha o Tipo de Arquivo antes de tentar assinar');
			   return;
		   }
		}
	   
		parent.websocket.onmessage = function (sadiMsg) {	
			
			if (typeof sadiMsg == "undefined" ) {return;}
			
			var resposta = sadiMsg.data.split(';_@-;');
			
			if (resposta!= null && resposta.length>0){
				if (resposta[0]=='iniciar_assinatura'){
					console.log("<- " + resposta[0]);				
					arquivosLote = montarArquivosParaAssinar();
					botaoSalvar = arquivosLote.length > 2;
					if (arquivosLote.length > 0){
						var tempArq = arquivosLote.shift();
						enviarMensagemSADI('enviar_texto', tempArq.arquivo, tempArq.nome, tempArq.id_tipo, tempArq.tipo, tempArq.chave, tempArq.base64);
					} else {
						//TODO: ou abre o assinador ou mostra mensagem de aviso que não tem texto no editor
						//se não tiver texto no editor mas o usuario pode fazer upload de arquivos
						enviarMensagemSADI('abrir_assinador'); 					
						//mostrarMensagemErro('Assinar Arquivo','Não foi possível encontrar arquivo para enviar para o assinador');
						Mostrar('divBotoesCentralizadosAssinador');
					}				
				}else if (resposta[0]=='enviar_texto'){
					console.log("<- " + resposta[0]);
					var tempArq = arquivosLote.shift();
					if(tempArq != null) {//(comando, arquivo, nome, id_tipo, tipo, chave, base64)
						enviarMensagemSADI('enviar_texto',tempArq.arquivo, tempArq.nome, tempArq.id_tipo, tempArq.tipo, tempArq.chave, tempArq.base64);
					} else {
						enviarMensagemSADI('abrir_assinador');
					}
				}else if (resposta[0]=='abrir_assinador'){					
					console.log("<- " + resposta[0]);
				}else if (resposta[0]=='arquivo_assinado'){//(nome, arquivo, id_tipo, tipo, chave, base64, assinado)
					mostrarMensagemConsultando("Anexando Arquivos...", "Aguarde, os arquivos estão sendo anexados.");
					arrayarquivo.push(new ArquivoAssinado(resposta[1], resposta[2], $('#id_ArquivoTipo').val(), $('#arquivoTipo').val(), resposta[4],    '',   'true'));
					enviarMensagemSADI('proximo_arquivo');
					console.log("<- " + resposta[0]);				
				}else if (resposta[0]=='assinatura_cancelada'){
					$("#dialog").dialog("close");
					console.log("<- " + resposta[0]);
					Mostrar('divBotoesCentralizadosAssinador');
				}else if (resposta[0]=='concluido'){
					finalizarAssinaturaA3();
					console.log("<- " + resposta[0]);
					Ocultar("Editor");
					Mostrar('divBotoesCentralizadosAssinador');
					if(botaoSalvar) {
						Ocultar("divBotoesCentralizados");
						Mostrar("divBotaoSalvar");
						botaoSalvar = false;
					}
				} else if (resposta[0]=='erro'){
					$("#dialog").dialog("close");
					alert('erro');
				}
			}else{		
				log(" erro " + data.data);				
			}
		};
	  	
		enviarMensagemSADI('iniciar_assinatura');		
		Ocultar('divBotoesCentralizadosAssinador');
	}
	
	function finalizarAssinaturaA3(){
		console.log('InsercaoArquivo.js::finalizarAssinatura()');
		if (arrayarquivo.length>=1){
			inserirArquivoA3(arrayarquivo.shift());
		} else {
		  	var editor = document.getElementById('editor1');
		  	if (editor != null) limparEditor();
			if (opLimparArquivo) limparArquivo();
			atualizarArquivos();
			$("#dialog").dialog("close");
		}
	}
	
	function enviarMensagemSADI(comando, arquivo, nome, id_tipo, tipo, chave, base64) {
		if (parent.websocket == undefined || parent.websocket== null) {
			mostrarMensagemErro("Erro ao acessar o SADI","Não Conectado ao Serviço de Assinatura Digital. Tente Conectar Primeiro");
			return;
		}
		if (comando=='iniciar_assinatura'){
			parent.websocket.send('iniciar_assinatura;_@-;adicionar;limpar');
			arquivos=[];
			nomeArquivos=[];
			$("#mostraArquivos").html("");			
			console.log("->Enviar mensagem ('iniciar_assinatura') " + inMsg++);
		} else if (comando=='enviar_texto'){		
			parent.websocket.send('enviar_texto;_@-;' + arquivo + ';_@-;' + nome + ';_@-;' + tipo + ';_@-;' + chave + ';_@-;' + id_tipo + ';_@-;' + base64);
			console.log("->enviar_texto " + inMsg++ );						
		} else if (comando=='abrir_assinador'){
			parent.websocket.send("abrir_assinador");
			console.log("->abrir_assinador " + inMsg++ );						
		} else if (comando=='proximo_arquivo'){
			parent.websocket.send("proximo_arquivo");
			console.log("->proximo_arquivo " + inMsg++ );						
		}
	}
	
	function inserirArquivoA3(arquivo){
		console.log('InsercaoArquivo.js::inserirArquivo()');	
//			var dados = {
//					id:-1, 
//					id_ArquivoTipo: arquivo.id_tipo, 
//					arquivoTipo:arquivo.tipo, 
//					nomeArquivo:arquivo.nome, 
//					arquivo:arquivo.arquivo, 
//					assinado: true, 
//					chaveMap:arquivo.chave
//			};		  	
		
		var contentType='application/pkcs7-signature'; 
						
		var dados = { 'id':-1, 'id_ArquivoTipo':arquivo.id_tipo, 'arquivoTipo':arquivo.tipo, 'nomeArquivo': arquivo.nome, 'arquivo':arquivo.arquivo, 'assinado':true, 'gerarAssinatura':false, 'senhaCertificado':'', 'contentType':contentType ,'salvarSenha':''};
		
		var url = 'InsercaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=4';
		$.ajax({
			url: encodeURI(url),
			method: "POST",  				
			data: dados,
			context: document.body,
			timeout: 300000,
			success: function(retorno){	
				finalizarAssinaturaA3();
			},
			beforeSend: function(data ){},
			error: function(request, status, error){
				mostrarMensagemErro("Erro", request.responseText);
			},
			complete: function(data ){}
		});	
	}

	function limparArquivo(){
		$('#id').val(-1);
		$('#id_ArquivoTipo').val('null');
		$('#arquivoTipo').val('');
		$('#arquivo').val('null');
		$('#nomeArquivo').val('')
		$('#id_Modelo').val('null');
		$('#modelo').val('');
		
		if (CKEDITOR.instances.editorEmenta!=undefined) {
			$('#id_ArquivoTipoEmenta').val('null');
			$('#arquivoTipoEmenta').val('');
			$('#arquivoEmenta').val('null');
			$('#nomeArquivoEmenta').val('')
			$('#id_ModeloEmenta').val('null');
			$('#modeloEmenta').val('');
		}
	}

	function limparEditor(){
		CKEDITOR.instances.editor1.setData("");
		Ocultar("Editor");
	}
	
	function montarArquivosParaAssinar(){
		var arquivosLote = [];
		var idTipo = $('#id_ArquivoTipo').val();
		var tipo = $('#arquivoTipo').val();	
		if (temEditor()){		
			var texto = CKEDITOR.instances.editor1.getData();
			if (texto != ""){
				var nomeArquivo = $("#nomeArquivo").val() != null || $("#nomeArquivo").val() != "" ? $("#nomeArquivo").val() : "online.html";			
				arquivosLote.push(new ArquivoAssinado(nomeArquivo, texto, idTipo, tipo, '', 'false', false));
			}
		}
		return arquivosLote;
	}

	function excluirArquivo(eleid, nomeArquivo, confirmar) {
		console.log('InsercaoArquivo.js::excluirArquivo()');
		if (confirmar == "true"){
			if (!confirm("Deseja retirar o item: " +nomeArquivo + "?")) return false;
		}
		var ok = true;
		var url = 'InsercaoArquivo?AJAX=ajax&PaginaAtual=3&fluxo=8&id_list='+eleid;
		$.ajax({
			url: encodeURI(url),		  						
			context: document.body,
			timeout: 300000, 
			async: true,		
			error: function(request, status, error){
				ok = false;
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");				
				}else{
					mostrarMensagemErro('Projudi - Erro','Não foi possível excluir o arquivo ' + request.responseText);				
				}			
			},
			complete: function(data ){
				atualizarArquivos();			
			}
		});	
		return ok;
	}	

	function excluirSelecionados(tabela){
		console.log('InsercaoArquivo.js::excluirSelecionados()');
		if (!confirm("Deseja realmente excluir os arquivos selecionados?")) return false;				
		var qtdExcluidos = 0;
		$("#" + tabela + " td > input:checked").each(function (i,a) {											
			if (a.value != null && a.value != ""){			
				if (excluirArquivo("button2" + a.value, false)) qtdExcluidos++;					
			}
		});							
		alterarEstadoSelTodos(false);	
		if (qtdExcluidos == 0) alert("Nenhum arquivo foi excluido");
	}
		
</script>