<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="certidaoNegativaPositivaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<title> |<%=request.getAttribute("tempPrograma")%>| Certid&atilde;o Nada Consta - Pessoa F&iacute;sica </title>
					
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
   		
   		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
   		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
   		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
   		<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
   		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
   				   
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		<style type="text/css"> #bkg_projudi{ display:none } </style>
		
		<script type="text/javascript">
			function setMascaraDataNascimento() {
				$("#DataNascimento").mask("99/99/9999");				
			}
		</script>
	</head>
	
	<body onload="setMascaraDataNascimento();">
	  <%@ include file="/CabecalhoPublico.html" %>    
	  <div  id="divCorpo" class="divCorpo">		     
		 <h1>Certid&atilde;o Nada Consta Segundo Grau</h1>		
			
			<form action="CertidaoSegundoGrauNegativaPositivaPublica" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
				<div id="divEditar" class="divEditar">					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				    							
						<label class="formEdicaoLabel" for="Nome">*Nome</label> 
		    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=certidaoNegativaPositivaDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
		    			<br />
											
						<label class="formEdicaoLabel" for="Cpf">*CPF</label> 
		    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="11" value="<%=certidaoNegativaPositivaDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF do requerente."/>
		    			<label for="Aviso" style="float:left;margin-left:25px;color:red">(digitar somente números, sem pontos ou hífen)</label>  			
		    			<br />
		    			
						<label class="formEdicaoLabel" for="NomeMae">*Nome da M&atilde;e</label> 
		    			<input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="60" maxlength="255" value="<%=certidaoNegativaPositivaDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
		    			<br />	    			
		    			
		    			<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label> 
						<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento" mask="99/99/9999"  type="text" size="10" maxlength="10" value="<%=certidaoNegativaPositivaDt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
						<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/>
		    			<br />		
		    			
		    			<label class="formEdicaoLabel" for="TipoArea">*Tipo de Área</label>  
						<input type="radio" name="TipoArea" value="1" <%=(certidaoNegativaPositivaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL))?" checked=checked ":"")%> /> Cível 
			       		<input type="radio" name="TipoArea" value="2" <%=(certidaoNegativaPositivaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))?" checked=checked ":"")%> /> Criminal
						<br />				
							
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Gerar Certid&atilde;o" onclick="AlterarValue('PaginaAtual','3')">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','1');"> 
						</div>
					</fieldset>
					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda"><span style="font-size:14px;">Observa&ccedil;&otilde;es</span></legend>
							<p style="text-align: center;">
								<strong><span style="font-size:18px;">Certid&atilde;o via Internet</span></strong></p>
							<p>
								&nbsp;</p>
							<p style="text-indent: 150px; text-align: justify;">
								<span style="font-size:14px;">A emiss&atilde;o de certid&atilde;o de distribui&ccedil;&atilde;o (nada consta), via internet, ser&aacute; emitida somente para pessoa f&iacute;sica, para a emissão de pessoa jur&iacute;dica acesse o site <a href="https://projudi.tjgo.jus.br/CertidaoSegundoGrauNegativaPositivaPublicaPJ?PaginaAtual=1"><u>https://projudi.tjgo.jus.br/CertidaoSegundoGrauNegativaPositivaPublicaPJ?PaginaAtual=1</u></a>.</span></p>
							<p style="text-indent: 150px; text-align: justify;">
								<span style="font-size:14px;">Para emitir a certid&atilde;o, preencha os campos: Nome Completo (sem abreviaturas), CPF (utilizando somente n&uacute;meros, inclusive os zeros, mas sem caracteres de formata&ccedil;&atilde;o, como pontos, barras, tra&ccedil;os), Nome da M&atilde;e, Data de Nascimento, e selecione o tipo de certid&atilde;o a ser emitida (C&iacute;vel ou Criminal). Ap&oacute;s clique no bot&atilde;o &quot;Gerar Certid&atilde;o&quot;.</span></p>
							<p style="text-indent: 150px; text-align: justify;">
								<span style="font-size:14px;">O sistema n&atilde;o emitir&aacute; certid&otilde;es positivas. Para esses casos, o interessado dever&aacute; dirigir-se a Divisão de Distribuição do Tribunal de Justiça de Goiás, sala 138, Térreo.</span></p>
							<p style="text-indent: 150px; text-align: justify;">
								<span style="font-size:14px;">Em caso de d&uacute;vidas o usu&aacute;rio dever&aacute; dirigir-se a Divisão de Distribuição do Tribunal de Justiça de Goiás a fim de dirimi-las.</span></p>
							<p style="text-indent: 150px; text-align: justify;">
								<span style="font-size:14px;">Para garantir a autenticidade deste documento e evitar fraudes, ser&aacute; poss&iacute;vel verificar a sua validade a partir de um c&oacute;digo que corresponder&aacute; ao n&uacute;mero de cada certid&atilde;o no site: <a href="https://projudi.tjgo.jus.br/CertidaoPublica"><u>https://projudi.tjgo.jus.br/CertidaoPublica</u></a>.</span></p>
							<p style="text-align: center; text-indent: 150px;">
								&nbsp;</p>
					</fieldset>				
				</div>
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
			<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
	</body>
</html>