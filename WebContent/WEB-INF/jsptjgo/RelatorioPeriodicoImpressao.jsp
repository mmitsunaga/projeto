<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="RelatorioPeriodicodt" scope="session" class= "br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt"/>

<html>
<head>
	<title> |<%=request.getAttribute("tempPrograma")%>| Impressão de Relatório Periódico  </title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		
		
		<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		
		<script language="javascript" type="text/javascript"> </script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Impressão de Relatório Periódico</h2></div>
		<form action="RelatorioPeriodico" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')"> 
			</div/><br />
			<div id="divEditar" class="divEditar">
					
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Relatório Periódico</legend>
						<input id="Id_RelatorioPeriodico" name="Id_RelatorioPeriodico" type="hidden" value="<%=RelatorioPeriodicodt.getId()%>"/>
						
						<label class="formEdicaoLabel" for="RelatorioEstPro">Nome</label><br> 
						<input class="formEdicaoInput" name="RelatorioPeriodico" id="RelatorioPeriodico" readonly="readonly" type="text" size="116" maxlength="60" value="<%=RelatorioPeriodicodt.getRelatorioPeriodico()%>" onkeyup=" autoTab(this,60)"/>
						<br />
						
					</fieldset>

					<input id="ListaCampos" name="ListaCampos" type="hidden" value="<%=request.getAttribute("ListaCampos")%>" />
					<input id="CodigoSql" name="CodigoSql" type="hidden" value="<%=RelatorioPeriodicodt.getCodigoSql()%>" />
					
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Parâmetros de Consulta</legend>	
						
						<%if(request.getAttribute("ListaCampos")!=null){
								List liTemp = (List)request.getAttribute("ListaCampos");
								String[] objTemp;
								String stTempNome="";
								for(int i = 0 ; i < liTemp.size();i++) {
									objTemp = (String[])liTemp.get(i).toString().split(";");
									if (objTemp[3].equalsIgnoreCase("Texto")) { %>
										<label class="formEdicaoLabel" for="<%=objTemp[0]%>"><% if (objTemp[2].equals("true")){%>*<%}%><%=objTemp[1]%></label><br> 
										<input class="formEdicaoInput" name="<%=objTemp[0]%>" id="<%=objTemp[0]%>"  type="text" size="60" maxlength="60" value="" onkeyup=" autoTab(this,60)"/><br />
							 			<br />
							 		<%} else if (objTemp[3].equalsIgnoreCase("Data")) {%>
								 		<label class="formEdicaoLabel" for="<%=objTemp[0]%>"><% if (objTemp[2].equals("true")){%>*<%}%><%=objTemp[1]%></label><br> 
										<input class="formEdicaoInput" name="<%=objTemp[0]%>" id="<%=objTemp[0]%>"  type="text" size="10" maxlength="10" value="" onkeyup="mascara_data(this)" onblur="verifica_data(this)">
										<img id="calendario<%=objTemp[0]%>" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].<%=objTemp[0]%>,'dd/mm/yyyy',this)"/><br />
								 		<br />
							 		<%} else if (objTemp[3].equalsIgnoreCase("Número")) {%>
								 		<label class="formEdicaoLabel" for="<%=objTemp[0]%>"><% if (objTemp[2].equals("true")){%>*<%}%><%=objTemp[1]%></label><br> 
				    					<input class="formEdicaoInput" name="<%=objTemp[0]%>" id="<%=objTemp[0]%>"  type="text" size="20" maxlength="30" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
				    					<br />
							 		<%} else if (objTemp[3].equalsIgnoreCase("Boolean")) {%>
								 		<label class="formEdicaoLabel" for="<%=objTemp[0]%>"><% if (objTemp[2].equals("true")){%>*<%}%><%=objTemp[1]%></label><br>  
				       					<input type="radio" name="<%=objTemp[0]%>" value="1" />Verdadeiro/Ativo/Sim
										<input type="radio" name="<%=objTemp[0]%>" value="0" />Falso/Inativo/Não
								 		<br />
							 		<%}%>
							 	<%}%>
						<%}%>
						
						
						
						
					</fieldset>
					
					
					
					
				<%@ include file="Padroes/Mensagens.jspf"%>
			</div>
		</form>
	</div>
</body>
</html>
