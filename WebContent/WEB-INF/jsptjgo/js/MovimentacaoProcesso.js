<script type="text/javascript" charset="ISO-8859-1">

var dadosCache = { };
var viewed = -1;
var podeLimparAposIncluir = true;
	
function iniciar() {
	atualizarPendencias();
	podeLimparAposIncluir = true;
}

function iniciarExtratoAta(){
	atualizarPendencias();
	podeLimparAposIncluir = false;
}

function mostrarModeloIntimacaoCitacao(){
	var pendenciaTipoCodigo = $("#codPendenciaTipo").val();
	var expedicaoAutomatica = $("#expedicaoAutomatica").val();
	var a= $("#divOpcoesExpedicaoAutomatica");
	var html1 = "<label  id='labelExpedicaoAutomatica'> Modelo</label>";
	if (pendenciaTipoCodigo == 2){ // Intimação
		if($("#expedicaoAutomatica").is(":checked")){
			Mostrar('divOpcoesExpedicaoAutomatica');
			html1+='<select name="codExpedicaoAutomatica" id="codExpedicaoAutomatica" size="1">';
			html1+='<option value="-1">--Selecione o modelo do Tipo de Pend&ecirc;ncia-- </option>';
			//html1+='<option value="1">VARA C&Iacute;VEL-Audi&ecirc;ncia de Instrução e Julgamento</option>';
			html1+='<option value="2">VARA C&Iacute;VEL-Constituir Novo Advogado</option>';
			html1+='<option value="3">VARA C&Iacute;VEL-Cumprimento de Senten&ccedil;a</option>';
			html1+='<option value="4">VARA C&Iacute;VEL-Gen&eacute;rica</option>';
			html1+='<option value="5">VARA C&Iacute;VEL-Hora Certa</option>';
			//html1+='<option value="6">VARA C&Iacute;VEL-Junta M&eacute;dica</option>';
			html1+='<option value="7">VARA C&Iacute;VEL-Prosseguimento ao Feito</option>';
//			html1+='<option value="8">VARA C&Iacute;VEL-Testemunha</option>';
			html1+='<option value="9">VARA C&Iacute;VEL-Penhora</option>';
			html1+='<option value="17">FAM&Iacute;LIA-3 Dias</option>';
			html1+='<option value="18">FAM&Iacute;LIA-15 Dias</option>';
			html1+='<option value="29">FAM&Iacute;LIA-Comparecimento 1&ordf; Vara</option>';
			html1+='<option value="30">FAM&Iacute;LIA-Comparecimento 2&ordf; Vara</option>';
			html1+='<option value="31">FAM&Iacute;LIA-Comparecimento 3&ordf; Vara</option>';
			html1+='<option value="32">FAM&Iacute;LIA-Comparecimento 4&ordf; Vara</option>';
			html1+='<option value="33">FAM&Iacute;LIA-Comparecimento 5&ordf; Vara</option>';
			html1+='<option value="34">FAM&Iacute;LIA-Comparecimento 6&ordf; Vara</option>';
			html1+='<option value="35">FAM&Iacute;LIA-Senten&ccedil;a</option>';
			html1+='<option value="25">JUIZ FAZ PUB-Senten&ccedil;a do Promovente</option>';
			html1+='<option value="26">JUIZ FAZ PUB-Senten&ccedil;a do Promovido</option>';
			html1+='<option value="27">JUIZ FAZ PUB-Promovente</option>';
			html1+='<option value="28">JUIZ FAZ PUB-Promovido</option>';			
//			html1+='<option value="55">JUIZ C&Iacute;VEL-Audiência Instrução Reclamado</option>';
//			html1+='<option value="56">JUIZ C&Iacute;VEL-Audiência Instrução Reclamante</option>';
			html1+='<option value="57">JUIZ C&Iacute;VEL-Certid&atilde;o Decis&atilde;o Reclamado</option>';
			html1+='<option value="58">JUIZ C&Iacute;VEL-Certid&atilde;o Decis&atilde;o Reclamante</option>';
			html1+='<option value="59">JUIZ C&Iacute;VEL-Provar Pgto Custas Finais</option>';
//			html1+='<option value="60">JUIZ C&Iacute;VEL-Conciliação Art. 53 Reclamado</option>';
//			html1+='<option value="61">JUIZ C&Iacute;VEL-Conciliação Art. 53 Reclamante</option>';
//			html1+='<option value="62">JUIZ C&Iacute;VEL-Conciliação de Reclamado</option>';
//			html1+='<option value="63">JUIZ C&Iacute;VEL-Conciliação de Reclamante</option>';
			html1+='<option value="64">JUIZ C&Iacute;VEL-Cumprimento de Senten&ccedil;a</option>';
			html1+='<option value="65">JUIZ C&Iacute;VEL-Documentos Novos CPC 437 &sect;1&ordm;</option>';
			html1+='<option value="66">JUIZ C&Iacute;VEL-Senten&ccedil;a de Reclamante</option>';
//			html1+='<option value="67">JUIZ C&Iacute;VEL-Testemunha</option>';
			html1+='<option value="68">JUIZ C&Iacute;VEL-Gen&eacute;rica</option>';
			html1+='<option value="69">JUIZ C&Iacute;VEL-Manifestar Interesse Prosseguimento</option>';
			html1+='<option value="70">JUIZ C&Iacute;VEL-Manifestar Certid&atilde;o Oficial Justi&ccedil;a</option>';
			html1+='<option value="71">JUIZ C&Iacute;VEL-Manifestar Cita&ccedil;&atilde;o Frustrada</option>';
			html1+='<option value="72">JUIZ C&Iacute;VEL-Senten&ccedil;a de Reclamado</option>';
			html1+='<option value="73">JUIZ C&Iacute;VEL-Notifica&ccedil;&atilde;o Contradit&oacute;rio Pr&eacute;vio</option>';
			html1+='<option value="74">JUIZ C&Iacute;VEL-Notifica&ccedil;&atilde;o Tutela Provis&oacute;ria de Urg&ecirc;ncia</option>';
//			html1+='<option value="80">SUCESSÃO-Audiência Escrivania Testemunha</option>';
//			html1+='<option value="81">SUCESSÃO-Audiência Escrivania</option>';
			html1+='<option value="82">SUCESS&Atilde;O-Autor Inerte</option>';
			html1+='<option value="83">SUCESS&Atilde;O-Constituir Novo Advogado</option>';
			html1+='<option value="84">SUCESS&Atilde;O-Gen&eacute;rica</option>';
			html1+='<option value="85">SUCESS&Atilde;O-Inventariante Inerte</option>';
			html1+='<option value="36">VARA FAZ MUN-Notifica&ccedil;&atilde;o</option>';
			html1+='<option value="37">VARA FAZ PUB MUN-Apresentar Embargos</option>';
			html1+='<option value="38">VARA FAZ PUB MUN-Ci&ecirc;ncia de Nova CDA</option>';
			html1+='<option value="39">VARA FAZ PUB MUN-Contrarrazoar Recurso</option>';
			html1+='<option value="40">VARA FAZ PUB MUN-Manifestar Sobre Peti&ccedil;&atilde;o</option>';
			html1+='<option value="41">VARA FAZ PUB MUN-Oposi&ccedil;&atilde;o Embargos de Terceiro</option>';
			html1+='<option value="42">VARA FAZ PUB MUN-Pagamento Custas Processuais</option>';
			html1+='<option value="43">VARA FAZ PUB MUN-Pagamento do D&eacute;bito</option>';
			html1+='<option value="44">VARA FAZ PUB MUN-Penhora</option>';
			html1+='<option value="86">2&deg; GRAU C&Iacute;VEL-Agravado</option>';
			html1+='</select> <img title="Pr&eacute;-visualizar o modelo selecionado" src="./imagens/16x16/btn_endereco.png" onclick="preVisualizarModelo();"></img></br>';
			html1+= '<input class="formEdicaoInput" type="checkbox" name="maoPropria" id="maoPropria" value="true"/>M&atilde;o Pr&oacute;pria</br>';
			html1+= '<input class="formEdicaoInput" type="checkbox" name="ordemServico" id="ordemServico" value="true"/>Ordem de Servi&ccedil;o';
			$("#expedicaoAutomatica").val("true");
			Ocultar('divOpcoesIntimacao');
		}else{
			$("#expedicaoAutomatica").val("false");	
			Mostrar('divOpcoesIntimacao');
			html1="";
		}				
		a.html(html1);
	} else if (pendenciaTipoCodigo == 1){ // Carta de Citação
		if($("#expedicaoAutomatica").is(":checked")){
			Mostrar('divOpcoesExpedicaoAutomatica');
			html1+='<select name="codExpedicaoAutomatica" id="codExpedicaoAutomatica" size="1">';
			html1+='<option value="-1">--Selecione o modelo do Tipo de Pend&ecirc;ncia-- </option>';
			//html1+='<option value="10">VARA C&Iacute;VEL-Audi&ecirc;ncia CEJUSC</option>';
			html1+='<option value="11">VARA C&Iacute;VEL-Confinante</option>';
			html1+='<option value="12">VARA C&Iacute;VEL-Gen&eacute;rica</option>';
			html1+='<option value="13">VARA C&Iacute;VEL-Resposta ao Recurso</option>';
			html1+='<option value="14">VARA C&Iacute;VEL-Improced&ecirc;ncia Liminar do Pedido</option>';
			html1+='<option value="15">VARA C&Iacute;VEL-Monit&oacute;ria</option>';
			html1+='<option value="16">VARA C&Iacute;VEL-Execu&ccedil;&atilde;o</option>';
			html1+='<option value="19">FAM&Iacute;LIA-3 Dias</option>';
			html1+='<option value="20">FAM&Iacute;LIA-15 Dias</option>';
			html1+='<option value="21">EXECU&Ccedil;&Atilde;O FISCAL-Geral</option>';
			html1+='<option value="22">JUIZ FAZ PUB-Tutela Deferida</option>';
//			html1+='<option value="23">JUIZ FAZ PUB-Audi&ecirc;ncia Tutela Deferida</option>';
			html1+='<option value="24">JUIZ FAZ PUB-Gen&eacute;rica</option>';
			html1+='<option value="51">JUIZ C&Iacute;VEL-Execu&ccedil;&atilde;o T&iacute;tulo Extrajudicial</option>';
			html1+='<option value="52">JUIZ C&Iacute;VEL-Improced&ecirc;ncia Liminar do Pedido</option>';
			html1+='<option value="53">JUIZ C&Iacute;VEL-Incidente de Desconsidera&ccedil;&atilde;o da PJ</option>';
			html1+='<option value="54">JUIZ C&Iacute;VEL-Resposta ao Recurso</option>';
//			html1+='<option value="75">SUCESS&Atilde;O-Audiência</option>';
//			html1+='<option value="76">SUCESS&Atilde;O-Citação e Intimação Audiência</option>';
			html1+='<option value="77">SUCESS&Atilde;O-Gen&eacute;rica Sem Audi&ecirc;ncia</option>';
			html1+='<option value="78">SUCESS&Atilde;O-Gen&eacute;rica</option>';
			html1+='<option value="79">SUCESS&Atilde;O-Hora Certa</option>';
			html1+='<option value="45">VARA FAZ PUB MUN-Embargos de Terceiro</option>';
			html1+='<option value="46">VARA FAZ PUB MUN-Execu&ccedil;&atilde;o Fiscal</option>';
			html1+='<option value="47">VARA FAZ PUB MUN-Restaura&ccedil;&atilde;o de Autos</option>';
			html1+='</select> <img title="Pr&eacute;-visualizar o modelo selecionado" src="./imagens/16x16/btn_endereco.png" onclick="preVisualizarModelo();"></img></br>';
			html1+= '<input class="formEdicaoInput" type="checkbox" name="maoPropria" id="maoPropria" value="true"/>M&atilde;o Pr&oacute;pria</br>';
			html1+= '<input class="formEdicaoInput" type="checkbox" name="ordemServico" id="ordemServico" value="true"/>Ordem de Servi&ccedil;o';
			$("#expedicaoAutomatica").val("true");
			Ocultar('divOpcoesIntimacao');
		}else{			
			$("#expedicaoAutomatica").val("false");	
			Mostrar('divOpcoesIntimacao');
			html1="";
		}	
		a.html(html1);
	}
}

function preVisualizarModelo(){
	var codigo = $('#codExpedicaoAutomatica').val();
	if (codigo != 'undefined' && codigo != "-1"){
		var url = 'Modelo?AJAX=ajax&PaginaAtual=3&codigo=' + codigo;
		$.get(encodeURI(url), function(data){	  
		  var config = {modal: true, resizable: true, width:1024, height:820, title: data.modelo};
		  $("#dialog").html(data.texto).css('background-color', 'white').dialog(config).dialog('open');
		});
	}	
}

function preencherDestinatarios(){
	var codPendenciaTipo = $("#codPendenciaTipo").val();
	var url = 'MovimentacaoProcessoJson?AJAX=ajax&PaginaAtual=8&operacao=10004&codPendenciaTipo='+ codPendenciaTipo;
	$.ajax({				
		url: encodeURI(url),
		context: document.body,
		timeout: 36000,
		success: function(retorno){				
			
			$("#codDestinatario").html("");
			$('#labelDestinatario').html("Destinat&aacute;rio");
			
			ocultarDivs();
			
			var obj = retorno.tipo;
			
			if (obj == 'opcoesIntimacao'){
				Mostrar('divOpcoesIntimacao');
				Mostrar('divOpcoes');		
				Mostrar('divmostrarExpedicao');
				$( "#expedicaoAutomatica").prop( "checked", false );
			} else if (obj == 'opcoesIntimacaoTelefone'){
				Ocultar('divOpcoes');
				Mostrar('divOpcoesIntimacaoViaTelefone');
			} else if (obj == 'opcoes' && codPendenciaTipo != -1){
				Mostrar('divOpcoes');						
			} 
			
			if( obj == 'opcoesAlterarValorCausa' ) {
				Ocultar('divOpcoes');
				Mostrar('divAlterarValorCausa');
			}
			
			if( obj == 'opcoesAudiencia' ) {
				Ocultar('divOpcoes');
				Mostrar('divOpcoesAudiencia');
			}
			
			if( obj == 'opcoesEncaminharProcesso' ) {
				Ocultar('divOpcoes');
				Mostrar('divEncaminharProcesso');
			}
			
			if( obj == 'opcoesEncaminharProcessoGabinete' ) {
				Ocultar('divOpcoes');
				Mostrar('divEncaminharProcessoGabinete');
			}
			
			if( obj == 'opcoesArquivamento' ) {
				Ocultar('divOpcoes');
				Mostrar('divOpcoesArquivamento');
			}
			
			if( obj == 'opcoesAlterarClasseProcessual' ) {
				Ocultar('divOpcoes');
				Mostrar('divAlterarClasseProcessual');
			}
			
			//Tratando caso da opção de marcar sessão
			if (obj == 'sessao' || obj == 'sessaoClasse') {
				Mostrar('divSessao');
				preencherListaSessoes();
				$('#labelDestinatario').html(" Relator na Sess&atilde;o ");					
			}
			
			//Tratando caso da opção de marcar sessão com classe
			if (obj == 'sessaoClasse') {
				Mostrar('divSessaoClasse');
				preencherListaClasses();
			}
			
			if( obj == 'opcoesAguardandoDecursoPrazo' ) {
				Mostrar('divOpcoes');
				Mostrar('divOpcoesAguardandoDecursoPrazoProcessoFase');
				Mostrar('divOpcoesAguardandoDecursoPrazo');
			}					
			
			if (obj == 'magistrados') {
				$('#labelDestinatario').html(" Magistrado ");
			}
												
			if (retorno.destinatarios.length > 0){
				Mostrar('divDestinatario');					
				$.each(retorno.destinatarios, function(i,item){	
					if (item.selected) {
						if (item.selected == "true") {
							$('#codDestinatario').append($('<option>', { 
						        value: item.id,
						        text : item.texto,
						        selected: "selected"
						    }));	
						} else {
							$('#codDestinatario').append($('<option>', { 
						        value: item.id,
						        text : item.texto
						    }));
						}						
					} else {
						$('#codDestinatario').append($('<option>', { 
					        value: item.id,
					        text : item.texto
					    }));
					}						
				});
			}
					
		},
		beforeSend: function(data){},
		error: function(request, status, error){
			ocultarDivs();
			alert(request.responseText); 
	  	}
	});
}

function preencherListaSessoes(){
	var url = 'MovimentacaoProcessoJson?AJAX=ajax&PaginaAtual=8&operacao=10005';
	$.ajax({				
		url: encodeURI(url),
		context: document.body,
		timeout: 36000,
		success: function(retorno){				
			$("#id_Sessao").html("");				
			$.each(retorno, function(i,item){												
				$('#id_Sessao').append($('<option>', { 
			        value: item.id,
			        text : item.texto 
			    }));						
			});												
		},
		beforeSend: function(data){},
		error: function(request, status, error){				
			alert(request.responseText); 
	  	}
	});
}

function preencherListaClasses() {
	var url = 'MovimentacaoProcessoJson?AJAX=ajax&PaginaAtual=8&operacao=10006';
	$.ajax({				
		url: encodeURI(url),
		context: document.body,
		timeout: 36000,
		success: function(retorno){				
			$("#id_Classe").html("");				
			$.each(retorno, function(i,item){												
				$('#id_Classe').append($('<option>', { 
			        value: item.id,
			        text : item.texto 
			    }));						
			});												
		},
		beforeSend: function(data){},
		error: function(request, status, error){				
			alert(request.responseText); 
	  	}
	});	
}

function atualizarPendencias(){	
	var tabela = $('#corpoTabela');
	let url = 'MovimentacaoProcessoJson?AJAX=ajax&PaginaAtual=8&operacao=10001';
	$.ajax({				
		url: encodeURI(url),
		context: document.body,
		timeout: 36000,
		success: function(retorno){
			
			// Excluir as linhas e deixar apenas aquela com id "identificador"
			$('#corpoTabela > tr').not(document.getElementById("identificador")).remove();
			
			// Ordena a listagem do tipo de pendencia pelo tipo
			retorno.sort(function(p1, p2) { return p1.pendenciaTipo.localeCompare(p2.pendenciaTipo); });
							
			$.each(retorno, function(i,dado){
				
				var id = dado.id;
									
				// Cria uma nova tr igual ao tr "identificador"
				var tr = $('#identificador').clone().attr('id', 'identificador' + id);  
		  	
				// Para cada elemento de td, alterar o id do elemento com o sufixo id
			  	tr.find('td > span, td > input:checkbox, td > input:image').each(function(i, dom){    
					  $(this).attr('id', dom.id + id);
			  	});
				
			  	// Coloca a nova linha na tabela
				$('#corpoTabela').append(tr);
								
				var prefixo = "";
				if( dado.codPendenciaTipo == "101" ) {
					prefixo = "R$ " + dado.outros;
				}
				
				if( dado.codPendenciaTipo == "102" ) {
					prefixo = "Nova Classe: " + dado.processoTipo;
				}
				
				if( dado.codPendenciaTipo == "116" ) {
					if(dado.codTipoAudiencia == "2" ){
						prefixo = "Tipo: Conciliação";
					} else if (dado.codTipoAudiencia == "3"){
						prefixo = "Tipo: Interrogatório";
					} else if (dado.codTipoAudiencia == "4"){
						prefixo = "Tipo: Justificação";
					}  else if (dado.codTipoAudiencia == "8"){
						prefixo = "Tipo: Instrução e Julgamento";
					}  else if (dado.codTipoAudiencia == "17"){
						prefixo = "Tipo: Conciliação CEJUSC";
					}
				}
				
				if( dado.codPendenciaTipo == "32" ) {
					if(dado.codTipoProcessoFase == "9" ){
						prefixo = "Tipo: Recurso STJ";
					} else if (dado.codTipoProcessoFase == "10"){
						prefixo = "Tipo: Recurso STF";
					} else if (dado.codTipoProcessoFase == "11"){
						prefixo = "Tipo: Recurso STF e STJ";
					} 
				}
				
				
				if (dado.codPendenciaTipo == "24" && dado.procArquivamentoTipo != ""){
					prefixo = "Tipo: "+ dado.procArquivamentoTipo;
				}				  	
				if( dado.codPendenciaTipo == "1" || dado.codPendenciaTipo == 2) {
					if(dado.codExpedicaoAutomatica == "1" ){
						prefixo = "Modelo: Audiência de Instrução e Julgamento";
					} else if (dado.codExpedicaoAutomatica == "2"){
						prefixo = "Modelo: Constituir Novo Advogado";
					} else if (dado.codExpedicaoAutomatica == "3"){
						prefixo = "Modelo: Cumprimento de Sentença";
					}  else if (dado.codExpedicaoAutomatica == "4"){
						prefixo = "Modelo: Normal";
					}  else if (dado.codExpedicaoAutomatica == "5"){
						prefixo = "Modelo: Hora Certa";
					} else if (dado.codExpedicaoAutomatica == "6"){
						prefixo = "Modelo: Junta Médica";
					} else if (dado.codExpedicaoAutomatica == "7"){
						prefixo = "Modelo: Prosseguimento ao Feito";
					} else if (dado.codExpedicaoAutomatica == "8"){
						prefixo = "Modelo: Testemunha";
					} else if (dado.codExpedicaoAutomatica == "9"){
						prefixo = "Modelo: Penhora";
					} else if (dado.codExpedicaoAutomatica == "10"){
						prefixo = "Modelo: Audiência CEJUSC";
					} else if (dado.codExpedicaoAutomatica == "11"){
						prefixo = "Modelo: Confinante";
					} else if (dado.codExpedicaoAutomatica == "12"){
						prefixo = "Modelo: Normal";
					} else if (dado.codExpedicaoAutomatica == "13"){
						prefixo = "Modelo: Resposta ao Recurso";
					} else if (dado.codExpedicaoAutomatica == "14"){
						prefixo = "Modelo: Improcedência Liminar do Pedido";
					} else if (dado.codExpedicaoAutomatica == "15"){
						prefixo = "Modelo: Monitória";
					} else if (dado.codExpedicaoAutomatica == "16"){
						prefixo = "Modelo: Execução";
					} 
					if(dado.codExpedicaoAutomatica != '-1' && dado.codExpedicaoAutomatica !='') {
						if(dado.maoPropria == true) {
							prefixo += " / Mão Própria:Sim ";
						} else if(dado.maoPropria == false){
							prefixo += " / Mão Própria:Não ";
						}
						if(dado.ordemServico == true) {
							prefixo += " / Ordem Serviço: Sim ";
						} else if(dado.maoPropria == false){
							prefixo += " / Ordem Serviço: Não ";
						}
					}
				}
				
				// Altera a visibilidade da nova linha
				$("#identificador" + id).css("display", "table-row");
			    
				// Preenche os elementos de cada td com os valores do json
				$("#chk_idPendencia" + id).val(id);					
				$('#tableTipo'+ id).html(dado.pendenciaTipo); 
			    $("#tableDestinatario" + id).html(dado.destinatario);
			    $("#tablePrazo" + id).html(dado.prazo);
			    $("#tableAguardandoDecursoPrazo" + id).html(dado.dataLimite);
			    $("#tableUrgente" + id).html(dado.urgencia);
			    if ($("#tablePessoalAdvogados" + id).length > 0){
			    	$("#tablePessoalAdvogados" + id).html(dado.pessoalAdvogado);
			    }
			    if ($("#tableIntimacao" + id).length > 0){
			    	$("#tableIntimacao" + id).html(prefixo + dado.tipoIntimacao);
			    }
			    
			    // Coloca o id e o tipo de pendencia no botão de excluir
			    $("#button2" + id).attr('data-id', id);
			    $("#button2" + id).attr('data-desc', dado.pendenciaTipo);										
																			
			});	// fim do foreach	
			
		},
		beforeSend: function(data){},
		error: function(request, status, error){				
			alert(request.responseText); 
		}
	});	
}

function ocultarDivs() {
	Ocultar('divOpcoes');
	Ocultar('divOpcoesAguardandoDecursoPrazo');
	Ocultar('divOpcoesAguardandoDecursoPrazoProcessoFase');
	Ocultar('divOpcoesIntimacao');
	Ocultar('divOpcoesIntimacaoViaTelefone');
	Ocultar('divAlterarValorCausa');
	Ocultar('divEncaminharProcesso');
	Ocultar('divEncaminharProcessoGabinete');
	Ocultar('divAlterarClasseProcessual');
	Ocultar('divSessao');
	Ocultar('divSessaoClasse');
	Ocultar('divDestinatario');	
	Ocultar('divOpcoesAudiencia');
	Ocultar('divOpcoesExpedicaoAutomatica');
	Ocultar('divmostrarExpedicao');
	Ocultar('divOpcoesArquivamento');
}

function excluir(eleid){		
  	if (confirm("Deseja retirar o item " + $('#' + eleid).attr('data-desc') + "?")) {
  		let url = 'MovimentacaoProcessoJson?AJAX=ajax&PaginaAtual=8&operacao=10002&id=' + $('#' + eleid).attr('data-id');
  		$.ajax({
			url: encodeURI(url),
			context: document.body,
			timeout: 500000,
			success: function(retorno){},
			beforeSend: function(data ){},
			error: function(request, status, error){
				mostrarMensagemErro('Erro na Consulta', request.responseText);
			}, 
			complete: function(data ){
				atualizarPendencias();											
			}
		});
		return true;
 	} return false;
}

function excluirSelecionados(tabela){
	if (!confirm("Deseja realmente excluir as pendências selecionadas?")) return false;
	var qtdExcluidos = 0;	
	$("#" + tabela + " td > input:checked").each(function (i,a) {		
		if (a.value != null && a.value != ""){			
			if (excluir("button2" + a.value)) qtdExcluidos++;					
		}
	});	
	alterarEstadoSelTodos(false);	
	if (qtdExcluidos == 0) alert("Nenhuma pendência foi exclu&Iacute;da");
}

function limpar() {
	$("#id").val("-1");  
	$("#codPendenciaTipo").val("-1"); 
	$("#pendenciaTipo").val(""); 
	$("#codDestinatario").empty();
	$("#destinatario").val("");
	$("#destinatarioTipo").val("");
	$("#prazo").val("");
	$("#dataLimite").val("");
	$("#urgencia").val("");	
	$("#outros").val(""); 
	$("#idProcessoTipo").val(""); 
	$("#processoTipo").val(""); 
	$("#onLine").val(""); 
	$("#pessoalAdvogado").val(); 
	$("#pessoalAdvogado").prop('checked', false);
	$("#intimacaoAudiencia").val("");
	$("#intimacaoAudiencia").prop('checked', false);
	$("#id_Sessao").empty();
	$("#id_Classe").empty();
	$("#dataSessao").empty();
	$("#pessoal").val("");
	$("#pessoal").prop('checked', false);
	$("#codTipoAudiencia").val("-1");
	$("#codTipoProcessoFase").val("-1");
	$("#id_ProcArquivamentoTipo").val("");
	$("#procArquivamentoTipo").val("");
	$('#idServentiaDestino').val("");
	$('#idAreaDistribuicaoDestino').val("");
	
	if (typeof($("#maoPropria").val()) != "undefined"){
  		$("#expedicaoAutomatica").val("");
  		$("#codExpedicaoAutomatica").val("");
  		$("#maoPropria").val("");
  		$("#ordemServico").val("");
  	}
	
	Ocultar('divDestinatario');
	Ocultar('divOpcoesIntimacao');
	Ocultar('divSessao');
	Ocultar('divSessaoClasse');
	Ocultar('divOpcoesAguardandoDecursoPrazo');
	Ocultar('divOpcoesAguardandoDecursoPrazoProcessoFase');
	Ocultar('divOpcoes');
	Ocultar('divOpcoesAudiencia');
	Ocultar('divOpcoesExpedicaoAutomatica');
	Ocultar('divmostrarExpedicao');
	Ocultar('divOpcoesArquivamento');
}

function inserir() {
	
	const INSERIR_PENDENCIA = 10000;
	var boErro = false;
	
	var expedicaoAutomatica = '';
	var codExpedicaoAutomatica = '';
	var maoPropria = 'false';
	var ordemServico = 'false';
	
	if (typeof($("#maoPropria").val()) != "undefined") {
		expedicaoAutomatica = $("#expedicaoAutomatica").val() == 'undefined' ? '' : $("#expedicaoAutomatica").val();
		codExpedicaoAutomatica = $("#codExpedicaoAutomatica").val() == 'undefined' ? '' : $("#codExpedicaoAutomatica").val();		
		if($("#maoPropria").is(":checked")) maoPropria= 'true';
		if($("#ordemServico").is(":checked")) ordemServico = 'true';
	}
		
	var urgencia = 'Não';
	if ($("#divOpcoes").css('display') != 'none'){		
		urgencia = $('#divOpcoes > select[name="urgencia"]').val();
	} 
	if ($("#divOpcoesIntimacaoViaTelefone").css('display') != 'none'){
		urgencia = $('#divOpcoesIntimacaoViaTelefone > select[name="urgencia"]').val();
	}
	
	var dado = {
		id: "-1",  
		codPendenciaTipo:$("#codPendenciaTipo").val(), 
		pendenciaTipo:$("#codPendenciaTipo option:selected").text(), 
		codDestinatario:$("#codDestinatario").val(), 
		destinatario:$("#codDestinatario option:selected").text(), 
		destinatarioTipo:$("#destinatarioTipo").val(), 
		prazo:$("#prazo").val(), 
		dataLimite: $('#dataLimite').val(),
		urgencia: urgencia,		
		outros:$("#outros").val(), 
		idProcessoTipo:$("#idProcessoTipo").val(), 
		processoTipo:$("#processoTipo").val(), 
		onLine:$("#onLine").val(), 
		pessoalAdvogado: $("#pessoalAdvogado").is(':checked') ? 'Sim' : 'Não', 
		intimacaoAudiencia: $("#intimacaoAudiencia").is(':checked') ? 'Sim' : 'Não', 
		id_Sessao:$("#id_Sessao").val(),
		dataSessao:$("#id_Sessao option:selected").text(),
		pessoal:$("#pessoal").is(':checked') ? 'Sim' : 'Não', 
		id_Classe:$("#id_Classe").val(), 
		classe:$("#id_Classe option:selected").text(),
		idServentiaDestino: $('#idServentiaDestino').val(),
		idServentiaCargo: $('#Id_ServentiaCargo').val(),
		idAreaDistribuicaoDestino: $('#idAreaDistribuicaoDestino').val(),
		codTipoAudiencia:$("#codTipoAudiencia").val(),
		codTipoProcessoFase:$("#codTipoProcessoFase").val(),
		id_ProcArquivamentoTipo:$("#id_ProcArquivamentoTipo").val(),
		procArquivamentoTipo:$("#procArquivamentoTipo").val(),
		expedicaoAutomatica: expedicaoAutomatica,
		codExpedicaoAutomatica: codExpedicaoAutomatica,
		maoPropria: maoPropria,
		ordemServico: ordemServico		
	};
	
	if (dado.codPendenciaTipo == -1) {
  		alert("Selecione o Tipo de Pendência");
  		return;
	}
	
	let url = 'MovimentacaoProcessoJson?AJAX=ajax&PaginaAtual=8&operacao=' + INSERIR_PENDENCIA;
	$.ajax({	
		type: 'POST',
		url: encodeURI(url),
		data: dado,
		context: document.body,
		timeout: 500000,
		success: function(retorno){},
		beforeSend: function(data ){},
		error: function(request, status, error){			
			alert(request.responseText.replace('Erro n.  -', ''));
			boErro=true;
		}, 
		complete: function(data ){
			if (!boErro) {
				atualizarPendencias();						
				if (podeLimparAposIncluir) {
					limpar();
				}
			}
		}
	});
	
}

function consultarMovimentacoesProcessoAjax(posicao, tamanho) {
	
}

</script>
