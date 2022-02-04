<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioAtrasoEntregaPostalDt"%>
<jsp:useBean id="RelatorioAtrasoEntregaPostalDt" scope="session" class="br.gov.go.tj.projudi.dt.relatorios.RelatorioAtrasoEntregaPostalDt"/>
<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
			@import url('./css/rastreamentoCorreios.css');
		</style>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='/js/jquery.js'></script>
		<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<script type="text/javascript" src="./js/rastreamentoCorreios.js" charset="utf-8"></script>		
		<script>
		  $( function() {
		    $( "#slider" ).slider({
		      value: <%=RelatorioAtrasoEntregaPostalDt.getDiasEspera()%>,
		      min: 0,
		      max: 100,
		      step: 1,
		      slide: function( event, ui ) {
		        $("#lblQtdeDias").html(ui.value + ' dias');
		        $("#DiasEspera").val(ui.value);
		      }
		    });
		    $("#lblQtdeDias").html($("#slider").slider("value") + ' dias');
		  } );
  		</script>
		
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Parâmetros de consulta</legend>															
						
						<div>
							<label class="formEdicaoLabel" for="Id_Serventia">Comarca
								<input id="Id_Comarca" name="Id_Comarca" type="hidden" value="<%=RelatorioAtrasoEntregaPostalDt.getIdComarca()%>"/>
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
								<input class="FormEdicaoimgLocalizar" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" title="Limpar Comarca">																	
							</label><br>
							<input class="formEdicaoInputSomenteLeitura" id="Comarca" name="Comarca" readonly type="text" size="60" maxlength="60" value="<%=RelatorioAtrasoEntregaPostalDt.getComarca()%>"/><br /> 
						</div>
												
						<div>
							<label class="formEdicaoLabel" for="Id_Serventia">Serventia
							<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%=RelatorioAtrasoEntregaPostalDt.getIdServentia()%>"/> 
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">					
							</label><br>
							<input  class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly type="text" size="60" maxlength="60" value="<%=RelatorioAtrasoEntregaPostalDt.getServentia()%>"/><br /> 
						</div>
																		
						<div>
							<p>
	  							<label for="amount">Esperando o retorno do Aviso de Recebimento(AR) à </label>
	  							<label id="lblQtdeDias" style="border:0; color:#000000; font-weight:bold;"></label>
	  							<input type="hidden" id="DiasEspera" name="DiasEspera" value="<%=RelatorioAtrasoEntregaPostalDt.getDiasEspera()%>" />
							</p>
	 						<div id="slider" style="width: 400px;"></div>
						</div>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
																					
					</fieldset>																										
				 </div>
				 
				 <!-- Resultados -->
				 <c:if test="${QtdeResultados >= 0 && PaginaAtual == 2}">
				 	<c:forEach var="entry" items="${ListaResultados}">
				 		<fieldset class="formEdicao">
					 	<legend>${entry.key}</legend>
					 	<div id="divTabela" class="divTabela" > 
					   		<table id="Tabela" class="Tabela" cellpadding="1" cellspacing="3">
					        	<thead>
					            	<tr class="TituloColuna">			                  	
					                  	<th>Número Processo</th>
					                  	<th>Parte</th>
					                  	<th>Tipo de Pendência</th>				                  	
					                  	<th>Rastreamento</th>
					                  	<th>Data Postagem</th>
					                  	<th>Data Entrega</th>
					                  	<th>Atraso</th>
					                  	<th>Mão Própria</th>			                  	          
					               	</tr>
					           	</thead>
					           	<tbody>
					           	<c:forEach var="item" items="${entry.value}">
					           		<tr>
					           			<td>${item.processoDt.processoNumeroCompleto}</td>
					           			<td>${item.nomeParte}</td>
					           			<td>${item.pendTipo}</td>
					           			<td><a href="RastreamentoCorreios?PaginaAtual=9&codigo=${item.codigoRastreamento}" onclick="verInfoRC(event);">${item.codigoRastreamento}</a></td>
					           			<td><center>${item.dataPostagem}</center></td>
					           			<td><center>${item.dataEntrega}</center></td>
					           			<td><center>${item.diasEspera}</center></td>
					           			<td><center>${item.maoPropria}</center></td>
					           		</tr>
					           	</c:forEach>						
					           </tbody>
					           <tfoot>
					           	<tr><td colspan="8">Total de registros: ${fn:length(entry.value)}</td></tr>
					           </tfoot>
					      	</table>      
					  	</div>	
					 </fieldset>
				 	</c:forEach>				 	
				 </c:if>
				 				 				 
				 <%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
					<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
				<%}%>
				
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>   
	</body>
</html>