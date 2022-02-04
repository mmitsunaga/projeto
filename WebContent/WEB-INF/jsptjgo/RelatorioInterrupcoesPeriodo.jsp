<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.projudi.dt.ParametroRelatorioInterrupcaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="ParamRelIndisponibilidadeDt" scope="session" class= "br.gov.go.tj.projudi.dt.ParametroRelatorioInterrupcaoDt"/>

<html>
	<head>
		<title>Relatorio de Interrupções</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
    	
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	  	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	  	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>  	
		
	</head>
	<body  class="fundo">
  		
  			<% 
  			String nomeSistema = "Processo Judicial";
  			if (request.getAttribute("Sistema") != null && request.getAttribute("Sistema").equals("2")) {
  				nomeSistema= "PJe"; %>
  				<p>
	    			<img width="870" height="160" alt="bnr pje2" src="imagens/bnr_pje2.png"></img>
				</p>
				<p></p>
  			<% } else { %>
  				<style type="text/css"> #bkg_projudi{ display:none } </style>
  				<%@ include file="/CabecalhoPublico.html" %>
  			<% }  %>    
  			<div id="divCorpo" class="divCorpo" >    
  			<div class="area"><h2>&raquo; Relat&oacute;rio de Interrup&ccedil;&otilde;es - <%=nomeSistema%> </h2></div>
	  		<form action="RelatorioInterrupcoes" method="post" name="Formulario" id="Formulario">
	  		
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				<input id="Sistema" name="Sistema" type="hidden" value="<%=request.getAttribute("Sistema")%>">

				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda"> Relat&oacute;rio de Interrup&ccedil;&otilde;es - <%=nomeSistema%> </legend> 
					    
					    <fieldset>					    
					    	<legend class="formEdicaoLegenda">Período</legend> 					
							
							<div class="col15">
							<label for="DataInicial">Data Inicial</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" style="float:left;"  readonly name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="<%=ParamRelIndisponibilidadeDt.getPeriodoInicialUtilizado().getDataFormatadaddMMyyyy()%>"> <img id="calendarioDataInicial" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)">
			    			</div>
			    			
			    			<div class="col15">
			    				<label for="DataFinal">Data Final</label><br> 
			    				<input class="formEdicaoInputSomenteLeitura"  readonly name="DataFinal" id="DataFinal"  type="text" size="10" maxlength="10" value="<%=ParamRelIndisponibilidadeDt.getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy()%>"> <img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)"> 
							</div>
				
							<div class="clear"></div>
						</fieldset>
				
					<div id="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PassoBusca','<%=String.valueOf(Configuracao.Localizar)%>')">
						<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PassoBusca','<%=String.valueOf(Configuracao.Novo)%>');">
					</div>                        
					   
					</fieldset>		
				</div>
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>