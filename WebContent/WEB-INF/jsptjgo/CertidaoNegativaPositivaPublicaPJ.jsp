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
<%@page import="br.gov.go.tj.projudi.dt.CertidaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="certidaoNegativaPositivaPublicaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<link rel="shortcut icon" href="./imagens/favicon.png">
		
		<title> |<%=request.getAttribute("tempPrograma")%>| Certid&atilde;o Nada Consta - Pessoa Jur&iacute;dica  </title>
					
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
		
			function executeAcaoTipoCertidao(valor) {
				$("#divGuiaCriminalCivelGratuita").hide();
				$("#divGuiaCivel").hide();
				$("#divParagrafoGratuita").hide();
				var emissaoGratuita = $("#isGratuitaEmissaoPrimeiroGrau").val();
				if (valor == '1' && emissaoGratuita == 'N') {			
		        	$("#divGuiaCivel").show();		        			        		        	
		        }
		        else if (valor == '2' || (valor == '1' && emissaoGratuita == 'S')) {	
		        	$("#divGuiaCriminalCivelGratuita").show();
		        	$("#divParagrafoGratuita").show();
		        }
			}
			
			function inicializeTela() {
				var valueAcaoTipoCertidao = '';
				<%if (certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().trim().equalsIgnoreCase("1")) {%>
					valueAcaoTipoCertidao = '1';
				<% } else if (certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase("2")) {%>
					valueAcaoTipoCertidao = '2';
				<% }%>				
				executeAcaoTipoCertidao(valueAcaoTipoCertidao);
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
				$('input[type=radio][name=TipoArea]').change(function() {
			    	executeAcaoTipoCertidao(this.value);			    	
			    });	
				$('input[type=radio][name=Territorio]').change(function() {
			    	executeAcaoTerritorio(this.value);			    	
			    });
			});			
		</script>
	</head>
	
	<body class="fundo">
	  <%@ include file="/CabecalhoPublico.html" %>   
	  <div  id="divCorpo" class="divCorpo">
		      
		 <div class="area"><h2>&raquo; Certid&atilde;o Nada Consta - Pessoa Jurídica</h2></div>		
			
			<form action="CertidaoNegativaPositivaPublicaPJ" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="isGratuitaEmissaoPrimeiroGrau" name="isGratuitaEmissaoPrimeiroGrau" type="hidden" value="<%=(CertidaoDt.isGratuitaEmissaoPrimeiroGrau?"S":"N")%>">
				
				<div id="divEditar" class="divEditar">					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				    	
				    	<label class="formEdicaoLabel" for="TipoArea">*Tipo de Área</label><br>  
						<input type="radio" name="TipoArea" value="1" <%=(certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL))?" checked=checked ":"")%> /> Cível 
			       		<input type="radio" name="TipoArea" value="2" <%=(certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))?" checked=checked ":"")%> /> Criminal
						<br />
				    	
				    	<div id="divGuiaCriminalCivelGratuita">
					    	<label class="formEdicaoLabel" for="RazaoSocial">*Razão Social</label><br> 
			    			<input class="formEdicaoInput" name="RazaoSocial" id="RazaoSocial" type="text" size="120" maxlength="255" value="<%=certidaoNegativaPositivaPublicaDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
			    			<br />
												
							<label class="formEdicaoLabel" for="Cnpj">*CNPJ</label><br> 
			    			<input class="formEdicaoInput" name="Cnpj" id="Cnpj"  type="text" size="30" maxlength="14" value="<%=certidaoNegativaPositivaPublicaDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,14)" title="Digite o CNPJ do requerente."/>
			    			<label for="Aviso" style="float:left;margin-left:25px;"><small>(digitar somente números, sem pontos ou hífen)</small></label><br>
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
				    	</div>				    							
				    	
				    	<div id="divGuiaCivel">
				    		<label class="formEdicaoLabel" for="NumeroDoRequerimento">*Número do Requerimento (Guia)</label><br> 
			    			<input class="formEdicaoInput" name="NumeroDoRequerimento" id="NumeroDoRequerimento"  type="text" size="30" maxlength="9" value="<%=certidaoNegativaPositivaPublicaDt.getNumeroGuiaCertidao()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,9)" title="Digite o Número do Requerimento."/>
			    			<div class="tooltip-container">
								<span style="font-size:18px; color:#999; vertical-align:middle">
									<a href="#" id="chamateclado"><i class="fa fa-question-circle"></i></a>
									<span class="tooltip">Digitar somente números, sem pontos, barra ou hífen. Exemplo guia NÚMERO: 18680149 - 1, favor digitar: 186801491.
									</span>
								</span>
							</div>
							<a href="http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-guia/forpspgi/EPG0201T" target="_blank"><u>Emitir Nova Guia</u></a>
				    	</div>
						  			
		    			<br />
		    			
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Gerar Certid&atilde;o" onclick="AlterarValue('PaginaAtual','3')">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','1');"> 
						</div>
					</fieldset>
					
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Observa&ccedil;&otilde;es</legend>
							<p>
								<h3>Certid&atilde;o via Internet</h3></p>
							<p>&nbsp;</p>
							<p>
								A emiss&atilde;o de certid&atilde;o de distribui&ccedil;&atilde;o (nada consta), via internet, ser&aacute; emitida somente para pessoa Jur&iacute;dica, para a emissão de pessoa f&iacute;sica acesse o site <a href="https://projudi.tjgo.jus.br/CertidaoNegativaPositivaPublica?PaginaAtual=1"><u>https://projudi.tjgo.jus.br/CertidaoNegativaPositivaPublica?PaginaAtual=1</u></a>.</p>
							<p>
								Esta certid&atilde;o refere-se ao período de 05/1996 (data da implanta&ccedil;&atilde;o do sistema informatizado neste Tribunal de Justiça) até a presente data.</p>
							<div id="divParagrafoGratuita">
								<p>
								Para emitir a certid&atilde;o, preencha os campos: Raz&atilde;o Social (sem abreviaturas), CNPJ (utilizando somente n&uacute;meros, inclusive os zeros, mas sem caracteres de formata&ccedil;&atilde;o, como pontos, barras, tra&ccedil;os). Ap&oacute;s clique no bot&atilde;o &quot;Gerar Certid&atilde;o&quot;.</p>
							</div>														
							<p>
								O sistema n&atilde;o emitir&aacute; certid&otilde;es positivas. Para esses casos, o interessado dever&aacute; dirigir-se ao Cart&oacute;rio Distribuidor do F&oacute;rum local.</p>
							<p>
								Em casos de d&uacute;vidas o usu&aacute;rio dever&aacute; dirigir-se ao F&oacute;rum mais pr&oacute;ximo a fim de dirimi-las.</p>
							<p>
								Para garantir a autenticidade deste documento e evitar fraudes, ser&aacute; poss&iacute;vel verificar a sua validade a partir de um c&oacute;digo que corresponder&aacute; ao n&uacute;mero de cada certid&atilde;o no site: <a href="https://projudi.tjgo.jus.br/CertidaoPublica"><u>https://projudi.tjgo.jus.br/CertidaoPublica</u></a>.</p>
							
					</fieldset>				
				</div>			
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
			<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
	</body>
</html>