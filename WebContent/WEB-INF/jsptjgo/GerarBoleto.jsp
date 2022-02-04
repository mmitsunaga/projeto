<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="GuiaEmissaoDtBase" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>
<jsp:useBean id="GuiaEmissaoBoletoDt" scope="session" class="br.gov.go.tj.projudi.ne.boletos.BoletoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário para Geração de Boleto</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	
	<script type="text/javascript">
			function executeTipoPessoa(valor) {
				if (valor == 'F') {			
					$("#Cnpj").value = '';
					$("#RazaoSocial").value = '';
					$("#divPessoaJuridica").hide();
					$("#divPessoaFisica").show();
		        }
		        else if (valor == 'J') {	
					$("#Nome").value = '';
					$("#Cpf").value = '';
		        	$("#divPessoaFisica").hide();
					$("#divPessoaJuridica").show();
		        }
			}
			
			function inicializeTela() {
				var valueAcaoTipoPessoa = '';
				<%if (GuiaEmissaoBoletoDt.getPagador().getTipoPessoa().trim().equalsIgnoreCase("F")) {%>
					valueAcaoTipoPessoa = 'F';
				<% } else if (GuiaEmissaoBoletoDt.getPagador().getTipoPessoa().trim().equalsIgnoreCase("J")) {%>
					valueAcaoTipoPessoa = 'J';
				<% }%>				
				executeTipoPessoa(valueAcaoTipoPessoa);	
			}
						
			$(document).ready(function() {
				inicializeTela();
				$('input[type=radio][name=tipoPessoa]').change(function() {
			    	executeTipoPessoa(this.value);			    	
			    });
			});			
		</script>	
</head>
<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Geração de Boleto</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="GerarBoleto" id="GerarBoleto">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<div id="divEditar" class="divEditar">                
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> Dados da Guia </legend>
				    				    
					<div> Número da Guia </div>
					<span class="span1"><%= Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoDtBase.getNumeroGuiaCompleto())%></span>
					<br />	
					<br />					
					<%if (GuiaEmissaoDtBase.isGuiaEmitidaSPG() || GuiaEmissaoDtBase.isGuiaEmitidaSSG()) {%>
					<div> Valor da Causa </div>
					<span class="span1">R$ <%=Funcoes.FormatarDecimal(GuiaEmissaoDtBase.getValorAcao())%></span>
					<% } else { %>
					<div> Classe </div>
				    <span style="width: 500px;"><%=GuiaEmissaoDtBase.getProcessoTipo()%></span>
				    <br />	
				    <br />				    
				    <div> Valor da Causa </div>
					<span class="span1">R$ <%=Funcoes.FormatarDecimal(GuiaEmissaoDtBase.getNovoValorAcaoAtualizado())%></span>
					<% } %>	
					<br />	
				    <br />				    
				    <div> Valor da Guia </div>
					<span class="span1">R$ <%=Funcoes.FormatarDecimal(GuiaEmissaoDtBase.getValorTotalGuia())%></span>				
					<br />
					<br />	
				</fieldset>
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">
						* Dados Pagador Boleto
					</legend>
					
					<div id="divTipoPessoa"> * Tipo Pessoa</div>
					<span>
						<input type="radio" name="tipoPessoa" value="F" <%=(GuiaEmissaoBoletoDt.getPagador().getTipoPessoa().trim().equalsIgnoreCase("F")?" checked=checked ":"")%> /> Física 
				        <input type="radio" name="tipoPessoa" value="J" <%=(GuiaEmissaoBoletoDt.getPagador().getTipoPessoa().trim().equalsIgnoreCase("J")?" checked=checked ":"")%> /> Jurídica
					</span>					
				</fieldset>
				
				<div id="divPessoaJuridica">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">
							* Pessoa Jurídica
						</legend>
							<label class="formEdicaoLabel" for="RazaoSocial">*Razão Social (até 40 caracteres)</label><br> 
				    		<input class="formEdicaoInput" name="RazaoSocial" id="RazaoSocial" type="text" size="120" maxlength="40" value="<%=GuiaEmissaoBoletoDt.getPagador().getRazaoSocial()%>" onkeyup=" autoTab(this,40)"/>
			    			<br />
												
							<label class="formEdicaoLabel" for="Cnpj">*CNPJ (somente números)</label><br> 
			    			<input class="formEdicaoInput" name="Cnpj" id="Cnpj"  type="text" size="30" maxlength="14" value="<%=GuiaEmissaoBoletoDt.getPagador().getCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,14)" title="Digite o CNPJ"/>
			    			<div class="tooltip-container">
								<span style="font-size:18px; color:#999; vertical-align:middle">
									<a href="#" id="chamateclado"><i class="fa fa-question-circle"></i></a>
									<span class="tooltip">Digitar somente números, sem pontos ou hífen
									</span>
								</span>
							</div>				
					</fieldset>
				</div>
				
				<div id="divPessoaFisica">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">
							* Pessoa Física
						</legend>
							<label class="formEdicaoLabel" for="Nome">*Nome (até 40 caracteres)</label><br> 
			    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="120" maxlength="40" value="<%=GuiaEmissaoBoletoDt.getPagador().getNome()%>" onkeyup=" autoTab(this,40)"/>
			    			<br />
												
							<label class="formEdicaoLabel" for="Cpf">*CPF (somente números)</label>
			    			<br> 
			    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="11" value="<%=GuiaEmissaoBoletoDt.getPagador().getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF"/>
			    			<div class="tooltip-container">
								<span style="font-size:18px; color:#999; vertical-align:middle">
									<a href="#" id="chamateclado"><i class="fa fa-question-circle"></i></a>
									<span class="tooltip">Digitar somente números, sem pontos ou hífen
									</span>
								</span>
							</div>					
					</fieldset>
				</div>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgPreviaCalculo" value="Prévia do Cálculo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" >                    
                    	Atualizar
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >                    	
                    	Limpar
                    </button>
                </div>
                
			</div>
						
			<br/><br/>
			<%@ include file="Padroes/reCaptcha.jspf" %>
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>