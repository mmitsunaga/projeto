<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="certidaoNegativaPositivaPublicaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<title> |<%=request.getAttribute("tempPrograma")%>| Certid&atilde;o Nada Consta - Pessoa F&iacute;sica </title>
		<link rel="shortcut icon" href="./imagens/favicon.png">
					
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
			function executeAcaoTerritorio(valor) {
				if (valor == 'E') {			
					$("#divComarca").hide();		        		        	
		        }
		        else if (valor == 'C') {	
		        	$("#divComarca").show();
		        }
			}
			
			function inicializeTela() {
				$("#divComarca").hide();
				var valueAcaoTerritorio = '';
				<%if (certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E")) {%>
					valueAcaoTerritorio = 'E';
				<% } else if (certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("C")) {%>
					valueAcaoTerritorio = 'C';
				<% }%>				
				executeAcaoTerritorio(valueAcaoTerritorio);				
			}
						
			$(document).ready(function() {
				inicializeTela();				
			    $('input[type=radio][name=Territorio]').change(function() {
			    	executeAcaoTerritorio(this.value);			    	
			    });
			});			
		</script>
	</head>
	
	<body class="fundo">
	  
		 <%@ include file="/CabecalhoPublico.html" %> 
		 <div  id="divCorpo" class="divCorpo"> 
		 <div class="area"><h2>&raquo; Certid&atilde;o Nada Consta - <%=request.getAttribute("tempTituloPagina")%></h2></div>      
		 		
			
			<form action="CertidaoNegativaPositivaPublica" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="TipoArea" name="TipoArea" type="hidden" value="<%=certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim()%>">
				
				<div id="divEditar" class="divEditar">					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				    							
						<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
		    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="100" maxlength="255" value="<%=certidaoNegativaPositivaPublicaDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
		    			<br />
											
						<label class="formEdicaoLabel" for="Cpf">*CPF</label><br> 
		    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="11" value="<%=certidaoNegativaPositivaPublicaDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF do requerente."/>
		    			<div class="tooltip-container">
							<span style="font-size:18px; color:#999; vertical-align:middle">
								<a href="#" id="chamateclado"><i class="fa fa-question-circle"></i></a>
								<span class="tooltip">Digitar somente números, sem pontos ou hífen
								</span>
							</span>
						</div>
		    			
		    						
		    			<br />
		    			
						<label class="formEdicaoLabel" for="NomeMae">*Nome da M&atilde;e</label><br> 
		    			<input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="100" maxlength="255" value="<%=certidaoNegativaPositivaPublicaDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
		    			<br />	    			
		    			
		    			<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label><br> 
						<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento" mask="99/99/9999"  type="text" size="10" maxlength="10" value="<%=certidaoNegativaPositivaPublicaDt.getDataNascimento()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
						<img style="margin-top:6px" id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/>
		    			<br />	
		    			
		    			<label class="formEdicaoLabel" for="Territorio">*Território:</label>  <br />
						<input type="radio" name="Territorio" value="E" <%=(certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E")?" checked=checked ":"")%> /> Estadual 
			       		<input type="radio" name="Territorio" value="C" <%=(certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("C")?" checked=checked ":"")%> /> Comarca
			       		<br />
			       		<div id="divComarca">
				       		<label class="formEdicaoLabel" for="Comarca">*Comarca:</label>  <br />
				       		<input id="imaLocalizarComarca" name="imaLocalizarComarca" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
				       		<input type="hidden" name="Id_Comarca" id="Id_Comarca" value="<%=certidaoNegativaPositivaPublicaDt.getId_Comarca()%>" />
                   			<input class="formEdicaoInputSomenteLeitura" readonly name="Comarca" id="Comarca" type="text" size="56" maxlength="100" value="<%=certidaoNegativaPositivaPublicaDt.getComarca()%>"/>
                   			<br />
                 		</div>
                  		<br />	
		    			
		    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Gerar Certid&atilde;o" onclick="AlterarValue('PaginaAtual','3')">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','1');"> 
						</div>
					</fieldset>
					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda"><span style="font-size:14px;">Observa&ccedil;&otilde;es</span></legend>
							<p><h3 >
								Certid&atilde;o via Internet</h3></p>
							<p>
								<span >A emiss&atilde;o de certid&atilde;o de distribui&ccedil;&atilde;o (nada consta), via internet, ser&aacute; emitida somente para pessoa f&iacute;sica, para a emissão de pessoa jur&iacute;dica acesse o site <a href="https://projudi.tjgo.jus.br/CertidaoNegativaPositivaPublicaPJ?PaginaAtual=1"><u>https://projudi.tjgo.jus.br/CertidaoNegativaPositivaPublicaPJ?PaginaAtual=1</u></a>.</span></p>
							<p >
								<span >Esta certid&atilde;o refere-se ao período de 05/1996 (data da implanta&ccedil;&atilde;o do sistema informatizado neste Tribunal de Justiça) até a presente data.</span></p>
							<p>
								<span >Para emitir a certid&atilde;o, preencha os campos: Nome Completo (sem abreviaturas), CPF (utilizando somente n&uacute;meros, inclusive os zeros, mas sem caracteres de formata&ccedil;&atilde;o, como pontos, barras, tra&ccedil;os), Nome da M&atilde;e e Data de Nascimento. Ap&oacute;s clique no bot&atilde;o &quot;Gerar Certid&atilde;o&quot;.</span></p>
							<p>
								<span >O sistema n&atilde;o emitir&aacute; certid&otilde;es positivas. Para esses casos, o interessado dever&aacute; dirigir-se ao Cart&oacute;rio Distribuidor do F&oacute;rum local.</span></p>
							<p>
								<span>Em casos de d&uacute;vidas o usu&aacute;rio dever&aacute; dirigir-se ao F&oacute;rum mais pr&oacute;ximo a fim de dirimi-las.</span></p>
							<p>
								<span>Para garantir a autenticidade deste documento e evitar fraudes, ser&aacute; poss&iacute;vel verificar a sua validade a partir de um c&oacute;digo que corresponder&aacute; ao n&uacute;mero de cada certid&atilde;o no site: <a href="https://projudi.tjgo.jus.br/CertidaoPublica"><u>https://projudi.tjgo.jus.br/CertidaoPublica</u></a>.</span></p>
							<p">
								&nbsp;</p>
					</fieldset>				
				</div>			
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
			<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
	</body>
</html>