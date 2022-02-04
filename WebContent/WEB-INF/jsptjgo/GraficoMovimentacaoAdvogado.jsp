<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt"%>

<jsp:useBean id="graficoMovimentacaoAdvogadoDt" scope="session" class= "br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt"/>

<html>
	<head>
		<title>gráfico de Movimentações de Advogados</title>	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>		
	</head>
	<body>
	<div id="divCorpo" class="divCorpo">
  		<div class="area"><h2>&raquo; Relat&oacute;rio de Movimenta&ccedil;&otilde;es de Advogados </h2></div>
	  		<form action="GraficoMovimentacaoAdvogado" method="post" name="Formulario" id="Formulario">
	  		
	  			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />				
				
				<div id="divPortaBotoes" class="divPortaBotoes">					
					<input id="imgImprimir"  class="imgImprimir" title="Gerar Gráfico" name="imgImprimir" type="image" 
							src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
				</div><br />

				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda"> Relat&oacute;rio de Movimenta&ccedil;&otilde;es de Advogados </legend> 
					    
					    <fieldset>					    
					    	<legend class="formEdicaoLegenda">Parâmetros</legend> 					
							<div class="col25">
							<label class="formEdicaoLabel" for="DataAnalise" style="float:left;">Data para Análise</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" style="float:left;"  readonly name="DataAnalise" id="DataAnalise"  type="text" size="10" maxlength="10" value="<%=graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy()%>"> <img id="calendarioDataAnalise" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataAnalise,'dd/mm/yyyy',this)">
			    			</div>
			    			<div class="col25">
			    			<label class="formEdicaoLabel" for="DataComparacao">Data para Comparação</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura"  readonly name="DataComparacao" id="DataComparacao"  type="text" size="10" maxlength="10" value="<%=graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy()%>"> <img id="calendarioDataComparacao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataComparacao,'dd/mm/yyyy',this)"> 
				</div>
						</fieldset>
						
						<fieldset>					    
					    	<legend class="formEdicaoLegenda">Datas Adicionais</legend> 					
							<div class="col25">
							<label class="formEdicaoLabel" for="DataAdicional001" style="float:left;">Data Adicional 001</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" style="float:left;"  readonly name="DataAdicional001" id="DataAdicional001"  type="text" size="10" maxlength="10" value="<%=graficoMovimentacaoAdvogadoDt.getDataAdicional001FormatadaddMMyyyy()%>"> <img id="calendarioDataAdicional001" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataAdicional001,'dd/mm/yyyy',this)">		    			
			    			</div>
			    			<div class="col25">
			    			<label class="formEdicaoLabel" for="DataAdicional002" style="float:left;">Data Adicional 002</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" style="float:left;"  readonly name="DataAdicional002" id="DataAdicional002"  type="text" size="10" maxlength="10" value="<%=graficoMovimentacaoAdvogadoDt.getDataAdicional003FormatadaddMMyyyy()%>"> <img id="calendarioDataAdicional002" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataAdicional002,'dd/mm/yyyy',this)">
			    			</div>
			    			<div class="col25">
			    			<label class="formEdicaoLabel" for="DataAdicional003" style="float:left;">Data Adicional 003</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" style="float:left;"  readonly name="DataAdicional003" id="DataAdicional003"  type="text" size="10" maxlength="10" value="<%=graficoMovimentacaoAdvogadoDt.getDataAdicional003FormatadaddMMyyyy()%>"> <img id="calendarioDataAdicional003" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataAdicional003,'dd/mm/yyyy',this)">
			    			</div>
						</fieldset>
				
					<div id="divBotoesCentralizados">						
						<input name="imgSubmeter" type="submit" value="Gerar Gráfico" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">						
					</div>                        
					   
					</fieldset>		
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>