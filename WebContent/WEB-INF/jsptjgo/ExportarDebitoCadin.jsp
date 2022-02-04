<%@page import="br.gov.go.tj.utils.Configuracao"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<jsp:useBean id="ExportarDebitoCadindt" scope="session" class= "br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt"/>
	<head>	
		<title>Exportar Débitos para o Cadin</title>		
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');			
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>  
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   		<script type="text/javascript"  language="javascript"    >

		 	$(document).ready(function() {
		 		mostrarOpcao();	 		
			});
		 	
		 	function mostrarOpcao(){
		 		var varCheck = $("input[name=tipoExportacao]:checked");
		 		
		 		if (varCheck.val() == '<%=br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt.EnumTipoExportacao.REPROCESSAMENTO_POR_LOTE.ordinal()%>') {
		 			Ocultar('divReprocessamentoPorData');
		 			Mostrar('divReprocessamentoPorLote');
		 		} else if (varCheck.val() == '<%=br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt.EnumTipoExportacao.REPROCESSAMENTO_POR_DATA.ordinal()%>') {
		 			Ocultar('divReprocessamentoPorLote');
		 			Mostrar('divReprocessamentoPorData');
		 		} else {
		 			Ocultar('divReprocessamentoPorLote');
		 			Ocultar('divReprocessamentoPorData');
		 		}		
			}
	 	
	 	</script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
	  	<div class="area"><h2>&raquo; Exportar Débitos para o Cadin </h2></div>  
	  		<form action="ExportarDebitoCadin" method="post" name="Formulario" id="Formulario"> 				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
		  		<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
		  		<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
		  		<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		  						
			   	<div id="divEditar" class="divEditar">
			   		<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Exportar Débitos para o Cadin </legend>
					    
					    <label class="formEdicaoLabel" for="dividaSolidaria"> *Tipo de Exportação</label><br/>
						<input type="radio" name="tipoExportacao" value="<%=br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt.EnumTipoExportacao.TODOS_NAO_PROCESSADOS.ordinal()%>" id="tipoExportacaoTodos" <%=ExportarDebitoCadindt.isTipoTodosNaoProcessados() ? "checked" : ""%> onChange="mostrarOpcao()" />Todos não processados 
			       		<input type="radio" name="tipoExportacao" value="<%=br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt.EnumTipoExportacao.REPROCESSAMENTO_POR_LOTE.ordinal()%>" id="tipoExportacaoReprocessamentoLote" <%=ExportarDebitoCadindt.isReprocessamentoPorLote() ? "checked" : ""%> onChange="mostrarOpcao()" />Reprocessamento por lote					
			       		<input type="radio" name="tipoExportacao" value="<%=br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt.EnumTipoExportacao.REPROCESSAMENTO_POR_DATA.ordinal()%>" id="tipoExportacaoReprocessamentoData" <%=ExportarDebitoCadindt.isReprocessamentoPorData() ? "checked" : ""%> onChange="mostrarOpcao()" />Reprocessamento por data
						<br />
						
						<div id="divReprocessamentoPorLote" style="display: <%=ExportarDebitoCadindt.isReprocessamentoPorLote() ? "block" : "none"%>">
							<label class="formEdicaoLabel" for="numeroDoLote"> *Reprocessamento por Número do Lote </label>
							<br /> 	
							<input class="formEdicaoInput" name="numeroDoLote" id="numeroDoLote" type="text" size="10" maxlength="10" value="<%=ExportarDebitoCadindt.getNumeroDoLote()%>" onkeyup="mascara(this, '###############'); autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)">							
						</div>	
						
						<div id="divReprocessamentoPorData" style="display: <%=ExportarDebitoCadindt.isReprocessamentoPorData() ? "block" : "none"%>">
							<label class="formEdicaoLabel" for="dataExportacao"> *Reprocessamento pela Data de Exportação</label><br />
							<input class="formEdicaoInput" name="dataExportacao" id="dataExportacao"  type="text" size="10" maxlength="10" value="<%=ExportarDebitoCadindt.getDataExportacao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)">			    		 
				    		<img id="calendarioDataExportacao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataExportacao,'dd/mm/yyyy',this)">	
				    		<input class="FormEdicaoimgLocalizar" name="imaLimparDataExportacao" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('dataExportacao','dataExportacao'); return false;" title="Limpar Data de Exportação">				    		
			       		</div>
			       		<br />
					    
					    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<%if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%>
							<input name="imgInserir" type="submit" value="Exportar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
							<input name="imgCancelar" type="reset" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
							<% } else { %>
							<input name="imgCancelar" id="btnCancelar" type="submit" value="Cancelar/Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
							<% } %>							
					    </div>
					</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>