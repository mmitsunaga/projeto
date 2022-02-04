<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AudienciaAgendaDt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaAgendaDt" />

<html>
	<head>
		<title>Consultar Agendas Livres para Audiências</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type='text/javascript' src='./js/checks.js'></script>
   		<script type='text/javascript' >
   		
	   		$( document ).ready(function() {
	   			Ocultar('divConfirmarSalvar');
	   			Ocultar('divConfirmarLiberar');
	   			Ocultar('divConfirmarExcluir');
	   		});
	   		
	   		function buscaAudienciaJSON(posicaoPaginaAtual, tamanho){
	   			
	   			var timer;
	   			var tabela =  $('#tabListaAudiencia');
	   			var nomeBusca = "";
	   			for(i=1;i<=2;i++) {
	   				 nomeBusca += "&nomeBusca" + i + "=" + $("#nomeBusca"+ i).val();
	   			}
	   			if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
	   			tabela.html('');
	   			var boFecharDialog = false;
	   			$.ajax({
	   				url: encodeURI('AudienciaAgenda?AJAX=ajax&Passo=1&PaginaAtual=3' + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&Hash=' + new Date().getTime()),
	   				context: document.body,
	   				timeout: 300000, async: true,
	   				success: function(retorno){
	   					var inLinha=1;			
	   					var totalPaginas =0;
	   					var corpoTabela = "";
	   					$.each(retorno, function(i,item){
	   						if(item.id=="-50000"){						
	   							totalPaginas = item.desc1;
	   						}else if (item.id=="-60000"){
	   							posicaoPaginaAtual = item.desc1;
	   						}else {
		  	  					corpoTabela += '<tr class="MarcarLinha TabelaLinha"' + inLinha + '>';
		  	  					corpoTabela += '<td align="center"><input class="formEdicaoCheckBox" id="checkboxAudienciaLivre" name="checkboxAudienciaLivre" type="checkbox" value="' + item.id + '"/></td>';
		  	  					corpoTabela += '<td>' + item.desc1 + '</td>';
		  	  					corpoTabela += '<td>' + item.desc2 + '</td>';
		  	  					corpoTabela += '<td>' + item.desc3 + '</td>';
 	                      		if (item.desc4 == 1){
  	                      			corpoTabela += '<td>Sim</td></tr>';   
  	                      		}else{
  	                      			corpoTabela += '<td>Não</td></tr>';
  	                      		}
								if (inLinha==1) inLinha=2; else inLinha=1;
							}
	   					});
	   					tabela.append(corpoTabela);
	   					CriarPaginacaoAudienciaJSON(posicaoPaginaAtual,totalPaginas, tamanho); 
	   				},
	   				beforeSend: function(data ){
	   				//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
	   					timer = setTimeout(function() {
	   						mostrarMensagemConsultando('Projudi - Consultado', 'Aguarde, buscando os dados...');	
	   						boFecharDialog=true;
	   					}, 1500);
		   				$("#formLocalizarBotao").hide();			
	   				},
	   				error: function(request, status, error){	
	   					boFecharDialog=false;
	   					if (error=='timeout'){
	   						mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
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
	
	   		function CriarPaginacaoAudienciaJSON(posicaoPaginaAtual, totalPaginas, tamanho){
	   			var tempString;		
	   	        var loTotalPaginas = parseInt(totalPaginas);
	   			var loPaginaAtual = parseInt(posicaoPaginaAtual); 
	   			var loTamanho = parseInt(tamanho);
	   			
	   			var loTotal = Math.ceil((loTotalPaginas / loTamanho));  								
	   			
	   			var loPaginaSelecionada= (loPaginaAtual+1);
	   			
	   			var loPaginaInicial= loPaginaAtual - Math.floor((loTamanho / 2 ));
	   			if (loPaginaInicial<1) loPaginaInicial = 1;
	   			
	   			var loPaginaFinal = loPaginaInicial + loTamanho -1;
	   			
	   			if (loPaginaFinal > loTotal)
	   				loPaginaFinal = loTotal;
	   			
	   			if (loPaginaInicial > (loPaginaFinal - (loTamanho -1)))
	   				loPaginaInicial = loPaginaFinal - (loTamanho -1);
	   				
	   			if (loPaginaInicial<1) loPaginaInicial = 1;
	   			
	   			if(loTotal==1){
	   				$("#Paginacao").html( "");
	   				calcularTamanhoIframe();
	   				return
	   			}
	   			tempString = "<b>P&aacute;gina <\/b>\n";
	   			tempString +="<a href=\"javascript:buscaAudienciaJSON('0', '" + loTamanho + "')\"> Primeira </a>";

	   			loPaginaAtual = loPaginaInicial;
	   			while(loPaginaAtual<=loPaginaFinal){	
	   				if (loPaginaAtual==loPaginaSelecionada){
	   					tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
	   				} else {				
	   					tempString +="<a href=\"javascript:buscaAudienciaJSON('" +(loPaginaAtual-1) + "','"  + loTamanho + "')\"> " + (loPaginaAtual)  + " </a>";
	   				}		
	   				loPaginaAtual++;			
	   			}		
	   			
	   			tempString +="<a href=\"javascript:buscaAudienciaJSON('" +(loTotal-1) + "','"  + loTamanho + "')\"> Última </a>";
	   			
	   			tempString+="<input id=\"CaixaTextoPosicionar\" value=\"" + (loTotal) + "\" class=\"CaixaTextoPosicionar\" type=\"text\" size=\"5\" maxlength=\"10\" /><input class=\"BotaoIr\" type=\"button\" value=\"Ir\" onclick=\"buscaAudienciaJSON('0','" + loTamanho + "' ); return false;\" />";
	   			$("#Paginacao").html( tempString);
	   			calcularTamanhoIframe();
	   		}
   		</script>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo" />
	  		<div class="area"><h2>&raquo; Consulta de Agendas Livres para Audiências</h2></div>
			<form id="Formulario" name="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
					
					<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
					<input type="hidden"  id="PaginaAnterior" name="PaginaAnterior" value="<%=String.valueOf(Configuracao.Localizar)%>" />
					<input type="hidden" id="tempFluxo1" name="tempFluxo1"  value="<%=request.getAttribute("tempFluxo1")%>"/>
					<input type="hidden" name="TituloPagina" value="<%=request.getAttribute("tempTituloPagina")%>" />
					<input type="hidden"  name="id_Serventia" value="<%=request.getAttribute("Id_Serventia")%>" />
					<input type="hidden"  name="id_ServentiaCargo" value="<%=request.getAttribute("Id_ServentiaCargo")%>" />
					<input type="hidden"  name="id_AudienciaTipo" value="<%=request.getAttribute("Id_AudienciaTipo")%>" />
					
					<div class="divLocalizar" id=divLocalizar>
						<fieldset class="formLocalizar">
							<legend>Consulta de Agendas</legend>
							<label class="formLocalizarLabel">*Tipo da Audiência
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaTipo" name="imaLocalizarAudienciaTipo" readonly type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
							<input class="FormEdicaoimgLocalizar" id="imaLimparAudienciaTipo" name="imaLimparAudienciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AudienciaTipo','AudienciaTipo'); return false;" />  
							</label><br>
							
							<input class="formLocalizarInput" name="AudienciaTipo" id="AudienciaTipo" readonly type="text" size="87" maxlength="100" value="<%=AudienciaAgendaDt.getAudienciaTipo()%>" />
							<input type="hidden" id="Id_AudienciaTipo" name="Id_AudienciaTipo" value="" />
                            
							<br />
						
							<% if (request.getAttribute("podeSelecionarCargo") != null && (Funcoes.StringToBoolean(request.getAttribute("podeSelecionarCargo").toString()) == true)){ %>
							<label class="formLocalizarLabel">Cargo da Serventia
							 <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" readonly type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
							<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargo" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;"; />
							</label><br>
							  
							<input class="formLocalizarInput" name="ServentiaCargo" id="ServentiaCargo" readonly type="text" size="87" maxlength="100" value="<%=AudienciaAgendaDt.getAudienciaProcessoDt().getServentiaCargo()%>" />
							<input type="hidden" id="Id_ServentiaCargo" name="Id_ServentiaCargo" value="" />
                           
							<br />
							<% } %>						
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="BotaoConsultar" type="submit" value="Consultar" onclick="javascript:buscaAudienciaJSON('0', <%=Configuracao.TamanhoRetornoConsulta%>); return false;"/>
								<input name="BotaoVoltar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
							</div>
						</fieldset>
						<br />
						<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("AudienciaAgenda")) {%>
						<input value="Reservar" title="Reservar - Executa a reserva da(s) agenda(s) selecionada(s)" name="imaSalvar" type="submit" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');Mostrar('divConfirmarSalvar');AlterarValue('tempFluxo1','Reservar'); return false" />
						<input value="Liberar" title="Liberar - Libera a reserva da(s) agenda(s) selecionada(s)" name="imaSalvar" type="submit" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');Mostrar('divConfirmarLiberar');return false" />
						<input value="Excluir" title="Excluir - Exclui a(s) agendas(s) selecionada(s) " name="imaExcluir" type="submit" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Excluir)%>');Mostrar('divConfirmarExcluir');return false" />
					    <%}%>
						<div id="divTabela" class="divTabela"> 
					    	<table id="Tabela" class="Tabela">
					        	<thead>
					        		<tr class="TituloColuna1">
										<th colspan="6">Serventia: <%=AudienciaAgendaDt.getServentia()%></th>
									</tr>
					            	<tr class="TituloColuna">
					         		   	<th class="colunaMinina">
	            							<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" title="Alterar estados de todos os itens da lista" />
				    					</th>
					                    <th>Data da Audiência</th>
					                  	<th>Tipo da Audiência</th>
					                  	<th>Cargo da Serventia</th>
					                  	<th>Reservada?</th>
					               	</tr>
					           	</thead>
					           	<tbody id="tabListaAudiencia"></tbody>
							</table>
					   	</div>
					</div>
					<div id="Paginacao" class="Paginacao"></div>
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
			        	<input id='Submeter' class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="Ocultar('divConfirmarSalvar');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')"> <br />
			        	<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
			        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
			           	<% } else { %>
			           		<div class="divMensagemsalvar">Clique para confirmar a reserva</div>
			           	<% }%> 
			      	</div>
			      	<div id="divConfirmarLiberar" class="ConfirmarSalvar">
			        	<input id='Submeter' class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="Ocultar('divConfirmarLiberar');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')"> <br />
			        	<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
			        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
			           	<% } else { %>
			           		<div class="divMensagemsalvar">Clique para confirmar a liberação de reserva</div>
			           	<% }%> 
			      	</div>
			      	<div id="divConfirmarExcluir" class="ConfirmarSalvar">
			        	<input id='Submeter' class="imgexcluir" type="image" src="./imagens/imgExcluir.png" onclick="Ocultar('divConfirmarExcluir');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')"> <br />
			        	<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
			        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
			           	<% } else { %>
			           		<div class="divMensagemsalvar">Clique para confirmar a exclusão</div>
			           	<% }%> 
			        </div>
				</form>
		</div>
    	<%@ include file="/WEB-INF/jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>
