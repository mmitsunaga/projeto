<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="audienciaDRSDt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaDRSDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
  	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
    <script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>	   
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Vincular Audiência do DRS de Outro Processo </h2></div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
			   		<legend class="formEdicaoLegenda"> Vincular Audiência do DRS de Outro Processo  </legend>			   		
			   		<label class="formEdicaoLabel"> Número do Processo </label>
			   		<br />
					<span class="destaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
	    			<br />
	    			<label class="formEdicaoLabel"> Serventia </label><br>
	    			<span class="destaque"><%=processoDt.getServentia()%></span/>
	    			<br />
    				<label class="formEdicaoLabel" for="ProcessoNumeroAudiencia" >Processo da Audiência DRS Publicada</label><br>	
				    <input class="formEdicaoInput" name="ProcessoNumeroAudiencia" id="ProcessoNumeroAudiencia" type="text" size="40" maxlength="25" onkeypress="return DigitarNumeroProcesso(this, event)" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" value="<%=(audienciaDRSDt.getProcessoNumero() != null ? audienciaDRSDt.getProcessoNumero() : "")%>">
		    		<em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em>
	    			<br />
	    			<label class="formEdicaoLabel" for="DataAudiencia">Data da Realização </label><br>
					<input class="destaque" id="DataAudiencia" name="DataAudiencia" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=(audienciaDRSDt.getDataHoraDaAudiencia() != null ? audienciaDRSDt.getDataHoraDaAudiencia().getDataFormatadaddMMyyyy() : "")%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" />
					<img id="calData" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataAudiencia,'dd/mm/yyyy',this)" />
					<br />
					<label class="formEdicaoLabel" for="HoraAudiencia">Hora da Realização</label>
					<br />
					<input class="destaque" name="HoraAudiencia" index="0" maxlength="5" size="5" onkeypress="return DigitarHoraResumida(this, event)" onkeyup="MascararHoraResumida(this); autoTab(this,5)" value="<%=(audienciaDRSDt.getDataHoraDaAudiencia() != null ? audienciaDRSDt.getDataHoraDaAudiencia().getHoraFormatadaHHmm() : "")%>" />
	    			<% if(request.getAttribute("ocultarSalvar") == null) {%>
		    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Vincular Audiência DRS" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
						</div>
					<% } %>				
					
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
