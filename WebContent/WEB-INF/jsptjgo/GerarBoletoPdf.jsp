<!DOCTYPE HTML>

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.ne.boletos.BoletoDt"%>

<jsp:useBean id="GuiaEmissaoBoletoDt" scope="session" class="br.gov.go.tj.projudi.ne.boletos.BoletoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Boleto CAIXA Online</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
</head>
<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
	<%@ include file="/CabecalhoPublico.html" %>
	<% } %>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Boleto CAIXA Online</h2></div>
		<form method="post" name="CarregarPdfBoleto" id="CarregarPdfBoleto">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<div id="divEditar" class="divEditar">                
                
                <fieldset id="VisualizaDados" class="VisualizaDados">
				    <legend> N�mero da Guia </legend>
					<span class="span1"><%= Funcoes.FormatarNumeroSerieGuia(GuiaEmissaoBoletoDt.getNumeroGuiaCompleto())%></span>
					<br />	
				</fieldset>
				
               	<span class="span1" style="text-align: center">
               		<br>
               		O boleto foi recebido COM SUCESSO pelo servi�o online da CAIXA, o download ser� iniciado automaticamente.<br><br>
               	
               		Caso seu Firefox n�o possua a cadeia de certificados ICP-BR instalada aparecer� a mensagem "Sua conex�o n�o � segura" no quadro abaixo.<br>
               		Clique <a href="https://www.iti.gov.br/navegadores/mozilla-firefox" target="_blank">aqui</a> e siga as instru��es de instala��o na p�gina do ITI (Instituto Nacional de Tecnologia da Informa��o) marcando todas as op��es
               		como confi�veis nas cadeias v2 e v5.<br>
               		Se estiver utilizando outro navegador clique <a href="https://www.iti.gov.br/navegadores" target="_blank">aqui</a>,
               		selecione seu navegador e siga as instru��es de instala��o.<br>
               		Ap�s a instala��o da cadeia de certificados clique em "Download do Boleto" para continuar a opera��o.<br><br>
               		
	               	Caso o servi�o de download da CAIXA esteja congestionado aparecer� a mensagem "Not Found" no quadro abaixo.<br>
	               	Enquanto a mensagem permanecer aguarde 30 segundos e clique em "Download do Boleto", em alguns casos o congestionamento pode durar alguns minutos.
	               	A CAIXA j� est� ciente da intermit�ncia neste servi�o e j� est� aumentando sua capacidade para evitar este inconveniente. Agradecemos a compreens�o.<br>
               	</span>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgAtualizar" value="Atualizar" onclick="recarrega();" >                    	
                    	Download do Boleto
                    </button>
                </div>
                
			</div>
					
			<div id="loadingMessage">Acessando servi�o de download de boletos da CAIXA...</div>	
			<br/>
			<iframe id="boleto" src="<%= GuiaEmissaoBoletoDt.getUrlPdf() %>" name ="boleto" style="width:100%"></iframe>
			<%@ include file="Padroes/reCaptcha.jspf" %>
	  	</form>   
	</div>
	<script> 
		$('#boleto').ready(function () {
		    $('#loadingMessage').css('display', 'none');
		});

		function recarrega() {
			var iframe = $('#boleto');
			iframe.src = iframe.src;
		}
	</script>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>