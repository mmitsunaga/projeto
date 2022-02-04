	<script type="text/javascript">
	
		var viewed = -1;
		
   		var quantidadeTotalAssinatura;
   		var quantidadeAtualAssinatura;
   		var quantidadeErrosAssinatura;
   		var mensagemAssinatura;
   		var ultimoErro;
   		var ultimoResponseText;
   		var lerSenha =  <%=!UsuarioSessao.isSenhaCertificado()%>;
   		
		//var arraynome = [];
		var arrayarquivo = [];	
		var totalDeArquivosAssinarPendenciaDwr;
	   	var quantidadeAtualAssinarPendenciaDwr;
	   	
	   	$(document).ready(function() {
			
			$("#abasInsercaoArquivos").tabs();
			
			if (!lerSenha){
				$("#salvarSenhaCertificado").prop( "checked", true );
			}
			
			if (tipoEnvio=='ass_ext'){
				mostrarExt();
			}else{ //ser
				mostrarSer();
			}
							      		  
			$("#botaoCancelar").click(function(){
				  $("#inc_confirma").fadeOut();
			});
			  
			$("#botaoAplicar").click(function(){
				$("#inc_confirma").fadeOut();
			});		  		 
		    
			calcularTamanhoIframe();
			
		});	
		
	   	
		function ArquivoAssinado(arquivo, nome){
			this.Conteudo=arquivo;
			this.Nome=nome;							  	
		}
						
		function getTextoAssinar(){		
 			return replaceAll($("#conteudoArquivos").attr('value'),"ASPAS_DUPLAS", "\"");	
  		}
  
  		function getNomeArquivo(){  
  			return replaceAll($("#nomeArquivos").attr('value'),"ASPAS_DUPLAS", "\"");  			
  		}
  
  		function podeAssinar(){		
  			return 'true';
  		}
  
  		function configuraAssinador(){
  			return "";
  		}
  
  		function iniciarAssinatura(){	   	   	  
			limparVariaveisLocais();
			AssinarPendencia.limparArquivosAssinados();   		   
  		}
  		
  		function limparVariaveisLocais(){
  			//arraynome=[];
			arrayarquivo=[];
			totalDeArquivosAssinarPendenciaDwr = 0;
	   		quantidadeAtualAssinarPendenciaDwr = 0;			
  		}
  			  
  		function incluirArquivoAssinatura(nome, arquivo){
			arrayarquivo.push(new ArquivoAssinado(arquivo,nome));
//			arraynome.push(nome);
//			arrayarquivo.push(arquivo);	  	
  		}  	
   	
  		function finalizarAssinatura(){	
  			if (totalDeArquivosAssinarPendenciaDwr == 0) {
  				totalDeArquivosAssinarPendenciaDwr = arrayarquivo.length;  				
  			}  			 
			mostrarMensagemConsultando("Inser��o de Arquivos", "Aguarde, os arquivos est�o sendo inseridos");
			//if (arraynome.length>=1){	   	 	   	 
			if (arrayarquivo.length>=1){
				// o pop() pega e retira a ultima posi��o do array							   	 
		   		//inserirArquivo(arraynome.shift(), arrayarquivo.shift());
		   		inserirArquivo(arrayarquivo.shift());					   		   
			}else{
				limparVariaveisLocais();				
				$("#dialog").dialog("close");		
		  		if ($("#assinaAssincrono").val() == 'true') 
				{
					executaAssinaturaAssincrona();			
				}
				else 
				{
					$("#divBotaoSalvar").css({'display':'block'});	
				}  			  			  			  		
			}		  
   		}
   		   		
   		function executaAssinaturaAssincrona() {
   			quantidadeErrosAssinatura = 0;
   			mostrarMensagemConsultando("A��o Assinar", "Aguarde, preparando para executar a��o");
   			preparaAssinaturaAssincrona();   			  			
   		}
   		
   		function recarregarPagina() {
   			AlterarValue('PaginaAtual', '-10');
   			var form = document.getElementById('Formulario');
   			form.action = 'Usuario';
			form.submit();
   		}
   		
   		function chamarImprimir(){
   			AlterarValue('PaginaAtual', '8');
   			var form = document.getElementById('Formulario');
   			form.submit();
   		}
   		
   		function assinaProximaConclusao(imprimir) {
   			if ((quantidadeTotalAssinatura == quantidadeAtualAssinatura) || (quantidadeErrosAssinatura >= 10)) 
   			{
   				if (quantidadeErrosAssinatura == 10) 
	   			{
	   				mostrarMensagemErroCallback("Projudi - Erro", "N�o foi poss�vel assinar ap�s 10 tentativas, favor repetir a opera��o mais tarde." + " Erro: " + ultimoErro + ". Texto: " + ultimoResponseText + ".", recarregarPagina);	   				
	   			} else if (mensagemAssinatura.length == 0) {
   					if (imprimir != null) {
   						mostrarMensagemOkCallback("A��o Assinar", "Assinatura efetuada com sucesso.", chamarImprimir);  
   					} else {
   						mostrarMensagemOkCallback("A��o Assinar", "Assinatura efetuada com sucesso.", recarregarPagina);  
   					}				
   				} else {
   					mostrarMensagemOkCallback("A��o Assinar", mensagemAssinatura, recarregarPagina);
   				}
   			} else {
   			
   				mostrarMensagemConsultando("A��o Assinar", "Processando " + (quantidadeAtualAssinatura + 1) + " de " + quantidadeTotalAssinatura + ".");
   				
   				var form = document.getElementById('Formulario');
				var action = form.action;
   				
   				url = action + '?AJAX=ajax&PaginaAtual=7';
   				
   				$.ajax({
					url: encodeURI(url),
					method: "POST",
					context: document.body,
					timeout: 300000, async: true,
					success: function(retorno)
					{
						quantidadeErrosAssinatura = 0;
						
						if (retorno.sucesso != "true") {							
							if (mensagemAssinatura.length == 0) {
								mensagemAssinatura = "N�o foi poss�vel executar a a��o dos seguintes processos:";
							}
							mensagemAssinatura = mensagemAssinatura + "\n" + retorno.mensagem;							
						}
								
						quantidadeAtualAssinatura = quantidadeAtualAssinatura + 1; //J� passou pelo processo, ent�o vamos para o proximo
						assinaProximaConclusao(retorno.imprimir);						
					},
					beforeSend: function(data ){ },
					error: function(request, status, error){					
						quantidadeErrosAssinatura = quantidadeErrosAssinatura + 1;
						ultimoErro = error;
						ultimoResponseText = request.responseText;
						assinaProximaConclusao();											
					},
					complete: function(data ) { }
				});
   			}	
   		}
   		
   		function preparaAssinaturaAssincrona() {
		   	var form = document.getElementById('Formulario');
			var action = form.action;
			url = action + '?AJAX=ajax&PaginaAtual=6';
   			
   			$.ajax({
				url: encodeURI(url),
				method: "POST",
				context: document.body,
				timeout: 300000, async: true,
				success: function(retorno)
				{
					if (retorno.sucesso == "true") {
						quantidadeTotalAssinatura = retorno.quantidade;
						quantidadeAtualAssinatura = 0;
						mensagemAssinatura = "";
						assinaProximaConclusao();
					} else {
						mostrarMensagemErroCallback('Projudi - Erro', retorno.mensagem);
					}
				},
				beforeSend: function(data ){ 	},
				error: function(request, status, error){
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErroCallback('Projudi - Erro','N�o foi poss�vel preparar assinatura ' + request.responseText);
				}
					
				},
				complete: function(data ) { }
			});
   		}
		
		//function inserirArquivo(nomeArquivo, conteudoArquivo) {
		function inserirArquivo(Arquivo) {
			//mostrarMensagemConsultando("Inser��o de Arquivos", "Aguarde, os arquivos est�o sendo inseridos. Processando " + (quantidadeAtualAssinarPendenciaDwr + 1) + " de " + totalDeArquivosAssinarPendenciaDwr + ".");			
			var dados = { id:viewed, id_ArquivoTipo:null, arquivoTipo:null, nomeArquivo:null, arquivo:null, assinado:true };
			dwr.util.getValues(dados);
			//dados.nomeArquivo = nomeArquivo;
			//dados.arquivo = conteudoArquivo;
			dados.nomeArquivo = Arquivo.Nome;
			dados.arquivo = Arquivo.Conteudo;				  		
			dwr.engine.beginBatch();
			AssinarPendencia.setArquivo(dados,{
										callback:function(){	  
											quantidadeAtualAssinarPendenciaDwr = quantidadeAtualAssinarPendenciaDwr + 1;				  	  								  					  			  			
											finalizarAssinatura();	  	  				  					  			
										}, 
										timeout:120000, errorHandler:function (error) { alert(error); finalizarAssinatura();}
				});
			dwr.engine.endBatch();
		}	
			
		/**
		 * Incluir arquivo selecionado na area de transferencia do servidor
		 * @author M�rcio Mendon�a Gomes
		 * @since 11/09/2012
		 */
		function incluirArquivosAreaTransferencia(){	
			mostrarMensagemConsultando("Inser��o de Arquivos", "Aguarde, os arquivos est�o sendo inseridos");					
			AssinarPendencia.incluirArquivosAreaTransferencia({
				callback:function (retorno){
					if (retorno != null && retorno == true) {
						finalizarAssinatura();
						alert("Arquivos inseridos com sucesso");
					} else {
						alert("N�o existem arquivos na area de transfer�ncia do servidor");
					}
				}, 
				timeout:120000, errorHandler:function (error) { alert(error); }
			});		
				
		}
		
		/**
		 * Remove todos os arquivos da area de transferencia do servidor
		 * @author M�rcio Mendon�a Gomes
		 * @since 13/09/2012
		 */
		function limparArquivosAreaTransferencia(){			
			AssinarPendencia.limparArquivosAreaTransferencia({
				callback:function (retorno){
					if (retorno != null && retorno == true) {						
						alert("Arquivos removidos da area de transfer�ncia com sucesso");
					} else {
						alert("N�o existem arquivos na area de transfer�ncia do servidor");
					}
				}, 
				timeout:120000, errorHandler:function (error) { alert(error); }
			});	
		}
		
		/**
		 * Remove Envia para o servidor pendencia por pendecia para serem assinadas e finalizadas
		 * @author jrcorrea
		 * @since 19/03/2020
		 */		
   		function assinarConclusoessGuardadas(total, posicao, qtderros) {
   			var senhaCertificado =  $('#senhaCertificado').val();
						
   			var quantidadeTotal = $( "input[name='pendencias']:checked" ).length;
   			var quatidadeErros = 0;
   			var index=1;
   			if(total!=undefined){
   				quantidadeTotal=total;
   				index = posicao+1;
   				quatidadeErros = qtderros;
   				
   			}
   						     			
			var id_pendencia = $( "input[name='pendencias']:checked:first" ).val();
			if (index<=quantidadeTotal){
				mostrarMensagemConsultando("A��o Assinar", "Processando " + index  + " de " + quantidadeTotal + ".\n " + quatidadeErros + " Erro(s)");
			}
			var salvarSenha=$("#salvarSenhaCertificado").is(':checked');		
			
			if (id_pendencia!=undefined && id_pendencia!="") {
				//removo o checked para n�o entrar em loop e para pegar o proximo selecionado
				$( "input[name='pendencias']:checked:first").prop('checked', false); 
				
   			   	var form = document.getElementById('Formulario');
   				var action = form.action;   				
   				url = action + '?AJAX=ajax&PaginaAtual=6';
   				
   				
   				$.ajax({
					url: encodeURI(url),
					method: "POST",
					context: document.body,
					timeout: 300000, 
					async: true,
					data: { 'id_pendencia': id_pendencia, 'senha': senhaCertificado, 'salvarSenha':salvarSenha},
					success: function(retorno) {
						assinarConclusoessGuardadas(quantidadeTotal,index,quatidadeErros);				
					},
					beforeSend: function(data ){ },
					error: function(request, status, error){
						if (error=='timeout'){
							mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
						} else {
							quatidadeErros++;  
							if (mensagemAssinatura.length == 0) {
								mensagemAssinatura = "N�o foi poss�vel executar a a��o dos seguintes processos:";
							}
							mensagemAssinatura = mensagemAssinatura + "\n" + request.responseText; 	
							assinarConclusoessGuardadas(quantidadeTotal,index,quatidadeErros);							
						}
					},
					complete: function(data ) { }
				});
			}else{   
				lerSenha=!salvarSenha;	
				if (quatidadeErros>0){
					mostrarMensagemErroCallback("A��o Assinar","Processandos " + posicao  + " de " + quantidadeTotal + ".\n " + quatidadeErros + " Erro(s) - Processo de Assinatura Finalizado. Mensagem: " + mensagemAssinatura + ".", ultimoPasso());
				}else{
   					mostrarMensagemOkCallback("A��o Assinar", "Assinatura efetuada com sucesso.", ultimoPasso);  
				}
	   			
			}
   		}
   		
   		function ultimoPasso(){
   		//colocar aqui para atualizar a pagina e pegar a lista de guardados para assinar novamente
   			AlterarValue('PaginaAtual', '7');
   			var form = document.getElementById('Formulario');
   			form.submit();
   		}
   		/**
		 * controle de leitura da senha
		 * @author Jesus Rodrigo Corr�a
		 * @since 19/03/2020
		 * � form esta dentro do mensagens.jspf
		 */		
		function senhaDigitadaAssinarPendencia(){				
			mensagemAssinatura = "";													
			var senhaCertificado =  $('#senhaCertificado').val();
			if(lerSenha==true && (senhaCertificado==undefined || senhaCertificado=="")){
				$("#divLerSenha").show();
				$( "#senhaCertificado" ).focus();
				$( "#but_senhaDigita" ).off('click').on('click',function() {senhaDigitadaAssinarPendencia(); });
				$( "#senhaCertificado").off('keypress').on('keypress', function( event ) { 
					if ( event.which == 13 ) {
						senhaDigitadaAssinarPendencia()(retornar);
					}
					});
			}else{				
				$("#divLerSenha").hide();
				assinarConclusoessGuardadas();
			}	
			return false;
		}		
			
  	</script>