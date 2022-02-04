<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.ne.CertidaoNe"%>
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
			function exibaMensagemInteressePessoal() {
				$('#mensagemInteressePessoal').empty();
				
				var mensagemAviso = "<p>Conforme Art. 5º, nº XXXIV, Letra 'B' da Constituição Federal e Provimento nº07/14 da Corregedoria Geral da Justiça, as certidões para fins de Concurso Público, Posse em Cargo Público, fins Militares e Eleitorais são isentas do recolhimento, mediante comprovação.</p>";
				
				$('#mensagemInteressePessoal').dialog({
				    autoOpen : false, modal : true, show : "blind", hide : "blind", position: "center top+25", closeOnEscape:true, show: { effect: "fade", duration: 200 }, hide: { effect: "fade", duration: 200 }
				 });
				
				$('#mensagemInteressePessoal').dialog({buttons: [{ text: "OK", click: function() { $( this ).dialog("close");}}]});
				$('#mensagemInteressePessoal').append(mensagemAviso);
				$('#mensagemInteressePessoal').css({'background-image':'url("imagens/32x32/ico_informacao.png")','background-repeat':'no-repeat'});
				$('#divCorpo').focus();
				$('#mensagemInteressePessoal').dialog('open');
				$('html, body',window.parent.document).animate({scrollTop:0}, 'slow');
			}
			
			function executeAcaoTipoCertidao(valor) {
				$("#divInteressePessoal").hide();
				$("#divGuiaCertidao").hide();
				$("#divParagrafoInteressePessoal").hide();
				if (valor == 'S') {			
		        	$("#divInteressePessoal").show();
		        	$("#divParagrafoInteressePessoal").show();		        			        	
		        }
		        else if (valor == 'N') {	
		        	$("#divGuiaCertidao").show();
		        }
			}
			
			function executeAcaoTerritorio(valor) {
				if (valor == 'E') {			
					$("#divComarca").hide();		        		        	
		        }
		        else if (valor == 'C') {	
		        	$("#divComarca").show();
		        }
			}
			
			function inicializeTela() {
				var valueAcaoTipoCertidao = '';
				<%if (certidaoNegativaPositivaPublicaDt.getInteressePessoal().trim().equalsIgnoreCase("S")) {%>
					valueAcaoTipoCertidao = 'S';
				<% } else if (certidaoNegativaPositivaPublicaDt.getInteressePessoal().trim().equalsIgnoreCase("N")) {%>
					valueAcaoTipoCertidao = 'N';
				<% }%>				
				executeAcaoTipoCertidao(valueAcaoTipoCertidao);
				$("#divComarca").hide();
				$("#divParagrafoCF").hide();
				if ($("#TipoArea").val() == "2") {
					$("#divTipoInteressePessoal").hide();
					$("#divTerritorioFinalidade").hide();	
				} else {
					$("#divParagrafoCF").show();
					$("#divTerritorioFinalidade").show();	
				}
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
			    $('input[type=radio][name=InteressePessoal]').change(function() {
			    	executeAcaoTipoCertidao(this.value);
			    	if (this.value == 'S') {	
			    		exibaMensagemInteressePessoal();	
			    	}			    	
			    });
			    $('input[type=radio][name=Territorio]').change(function() {
			    	executeAcaoTerritorio(this.value);			    	
			    });
			});			
		</script>
	</head>
	
	<body class="fundo">
	
		<div id="mensagemInteressePessoal" title="Aviso Importante" style="display:none">    	
		</div>
	  
		 <%@ include file="/CabecalhoPublico.html" %> 
		 <div  id="divCorpo" class="divCorpo"> 
		 <div class="area"><h2>&raquo; Certid&atilde;o Nada Consta - <%=request.getAttribute("tempTituloPagina")%></h2></div>      
		 	<form action="CertidaoNegativaPositivaPublica" method="post" name="Formulario" id="Formulario" enctype="multipart/form-data">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="TituloPagina" name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="TipoArea" name="TipoArea" type="hidden" value="<%=certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim()%>">
				
				<div id="divEditar" class="divEditar">	
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
				    	
				    	<div id="divTipoInteressePessoal">
					    	<label class="formEdicaoLabel" for="InteressePessoal">*Interesse pessoal (CF ART.5º, XXXIV, 'B'):</label>  <br />
							<input type="radio" name="InteressePessoal" value="S" <%=(certidaoNegativaPositivaPublicaDt.getInteressePessoal().trim().equalsIgnoreCase("S")?" checked=checked ":"")%> /> Sim 
				       		<input type="radio" name="InteressePessoal" value="N" <%=(certidaoNegativaPositivaPublicaDt.getInteressePessoal().trim().equalsIgnoreCase("N")?" checked=checked ":"")%> /> Não
				       		<br />
				    	</div>
				    	
				    	<div id="divGuiaCertidao">				    							
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
							<br />						
			    			
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgSubmeterGuia" type="submit" value="Gerar Certid&atilde;o" onclick="AlterarValue('PaginaAtual','4')">
								<input name="imgLimparGuia" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','1');"> 
							</div>
						</div>
						<div id="divInteressePessoal">
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
			    			
			    			<div id="divTerritorioFinalidade">
						    	<label class="formEdicaoLabel" for="Territorio">*Território:</label>  <br />
								<input type="radio" name="Territorio" value="E" <%=(certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E")?" checked=checked ":"")%> /> Estadual 
					       		<input type="radio" name="Territorio" value="C" <%=(certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("C")?" checked=checked ":"")%> /> Comarca
					       		<br />
					       		<div id="divComarca">
						       		<label class="formEdicaoLabel" for="Comarca">*Comarca:</label>  <br />
						       		<input id="imaLocalizarComarca" name="imaLocalizarComarca" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
						       		<input type="hidden" name="Id_Comarca" id="Id_Comarca" value="<%=certidaoNegativaPositivaPublicaDt.getId_Comarca()%>" />
	                    			<input class="formEdicaoInputSomenteLeitura" readonly name="Comarca" id="Comarca" type="text" size="30" maxlength="100" value="<%=certidaoNegativaPositivaPublicaDt.getComarca()%>"/>
	                    			<br />
                    			</div> 
                    			<label class="formEdicaoLabel" for="Finalidade">*Finalidade:</label>  <br />
                    			<select id="Finalidade" name="Finalidade">
	                    			<option value="">--Selecione a Finalidade--</option>
	                    			<option value="<%=CertidaoNe.CONCURSO_PUBLICO%>" <%=(certidaoNegativaPositivaPublicaDt.getFinalidade().trim().equalsIgnoreCase(String.valueOf(CertidaoNe.CONCURSO_PUBLICO))?"selected":"")%>>Concurso Público</option>
	                    			<option value="<%=CertidaoNe.CONTRATACAO_EMPREGO%>" <%=(certidaoNegativaPositivaPublicaDt.getFinalidade().trim().equalsIgnoreCase(String.valueOf(CertidaoNe.CONTRATACAO_EMPREGO))?"selected":"")%>>Contratação de Empregos</option>
	                    			<option value="<%=CertidaoNe.ELEITORAL%>" <%=(certidaoNegativaPositivaPublicaDt.getFinalidade().trim().equalsIgnoreCase(String.valueOf(CertidaoNe.ELEITORAL))?"selected":"")%>>Eleitoral</option>
	                    			<option value="<%=CertidaoNe.MILITAR%>" <%=(certidaoNegativaPositivaPublicaDt.getFinalidade().trim().equalsIgnoreCase(String.valueOf(CertidaoNe.MILITAR))?"selected":"")%>>Militar</option>
	                    			<option value="<%=CertidaoNe.OUTROS%>" <%=(certidaoNegativaPositivaPublicaDt.getFinalidade().trim().equalsIgnoreCase(String.valueOf(CertidaoNe.OUTROS))?"selected":"")%>>Outros</option>	                    				                			
		                		</select>								
					       		<br />                   			
					       		<label class="formEdicaoLabel" for="Documento">*Documento para Comprovação:</label><br />
					       		<input type="file" id="Documento" name="Documento" accept="application/pdf" />
					       		<div class="tooltip-container">
									<span style="font-size:18px; color:#999; vertical-align:middle">
										<a href="#" id="toolTipDocumento"><i class="fa fa-question-circle"></i></a>
										<span class="tooltip">Somente PDF com tamanho máximo de 2MB</span>
									</span>
								</div>
								<%if(certidaoNegativaPositivaPublicaDt.getNomeDocumento() != null && certidaoNegativaPositivaPublicaDt.getNomeDocumento().trim().length() > 0) {%>
									<br />
									<label class="formEdicaoLabel">Último Documento Selecionado: <%=certidaoNegativaPositivaPublicaDt.getNomeDocumento().trim()%></label>
								<% } %>	
								<br />							
				    		</div>	
			    			
			    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgSubmeterInteressePessoal" type="submit" value="Gerar Certid&atilde;o" onclick="AlterarValue('PaginaAtual','3')">
								<input name="imgLimparInteressePessoal" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','1');"> 
							</div>
						</div>
					</fieldset>					
				    
				    <fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda"><span style="font-size:14px;">Observa&ccedil;&otilde;es</span></legend>
							<p><h3>
								Certid&atilde;o via Internet</h3></p>
							<p>
								<span >A emiss&atilde;o de certid&atilde;o de distribui&ccedil;&atilde;o (nada consta), via internet, ser&aacute; emitida somente para pessoa f&iacute;sica, para a emissão de pessoa jur&iacute;dica acesse o site <a href="https://projudi.tjgo.jus.br/CertidaoNegativaPositivaPublicaPJ?PaginaAtual=1"><u>https://projudi.tjgo.jus.br/CertidaoNegativaPositivaPublicaPJ?PaginaAtual=1</u></a>.</span></p>
							<p >
								<span >Esta certid&atilde;o refere-se ao período de 05/1996 (data da implanta&ccedil;&atilde;o do sistema informatizado neste Tribunal de Justiça) até a presente data.</span></p>
							<div id="divParagrafoInteressePessoal">
								<p>
									<span >Para emitir a certid&atilde;o, preencha os campos: Nome Completo (sem abreviaturas), CPF (utilizando somente n&uacute;meros, inclusive os zeros, mas sem caracteres de formata&ccedil;&atilde;o, como pontos, barras, tra&ccedil;os), Nome da M&atilde;e e Data de Nascimento. Ap&oacute;s clique no bot&atilde;o &quot;Gerar Certid&atilde;o&quot;.</span></p>
							</div>
							<p>
								<span >O sistema n&atilde;o emitir&aacute; certid&otilde;es positivas. Para esses casos, o interessado dever&aacute; dirigir-se ao Cart&oacute;rio Distribuidor do F&oacute;rum local.</span></p>
							<p>
								<span>Em casos de d&uacute;vidas o usu&aacute;rio dever&aacute; dirigir-se ao F&oacute;rum mais pr&oacute;ximo a fim de dirimi-las.</span></p>
							<p>
								<span>Para garantir a autenticidade deste documento e evitar fraudes, ser&aacute; poss&iacute;vel verificar a sua validade a partir de um c&oacute;digo que corresponder&aacute; ao n&uacute;mero de cada certid&atilde;o no site: <a href="https://projudi.tjgo.jus.br/CertidaoPublica"><u>https://projudi.tjgo.jus.br/CertidaoPublica</u></a>.</span></p>
							<div id="divParagrafoCF">
								<p>
									<span><b>CONSTITUIÇÃO FEDERAL ART.5º, XXXIV</b> - são a todos assegurados, independentemente do pagamento de taxas:</span>
									<br />
									<span>b) a obtenção de certidões em repartições públicas, para <b>defesa de direitos</b> e <b>esclarecimento de situações de interesse pessoal</b>;																
								</p>
							</div>
							<p>&nbsp;</p>
					</fieldset>				
				</div>			
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
			<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
	</body>
</html>