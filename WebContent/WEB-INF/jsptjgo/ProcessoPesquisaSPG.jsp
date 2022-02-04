<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
	<head>
		<title>Busca de Processo no SPG</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="BuscaProcessoSPG" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos no SPG </legend>
                        <p />
                        
					    <label class="formEdicaoLabel" for="ProcessoNumeroSPG">Número do Processo</label><br> 
					    <input class="formEdicaoInput" name="ProcessoNumeroSPG" id="ProcessoNumeroSPG"  type="text" size="21" maxlength="16" value="" onkeyup=" autoTab(this,16)" onkeypress="return DigitarNumeroProcesso(this, event)"/>
					    <br />
					    <br />
					    
					    <label class="formEdicaoLabel" for="ProcessoNumeroCNJ">Número do Processo CNJ</label><br> 
					    <input class="formEdicaoInput" name="ProcessoNumeroCNJ" id="ProcessoNumeroCNJ"  type="text" size="8" maxlength="7" value="" onkeyup=" autoTab(this,7)" onkeypress="return DigitarNumeroProcesso(this, event)"/>
					    <label class="formEdicaoLabel" for="ProcessoNumeroCNJDigito">.</label><br>
					    <input class="formEdicaoInput" name="ProcessoNumeroCNJDigito" id="ProcessoNumeroCNJDigito"  type="text" size="1" maxlength="2" value="" onkeyup=" autoTab(this,2)" onkeypress="return DigitarNumeroProcesso(this, event)"/>
					    <label class="formEdicaoLabel" for="ProcessoNumeroCNJAno">.</label><br>
					    <input class="formEdicaoInput" name="ProcessoNumeroCNJAno" id="ProcessoNumeroCNJAno"  type="text" size="3" maxlength="4" value="" onkeyup=" autoTab(this,4)" onkeypress="return DigitarNumeroProcesso(this, event)"/>
					    <label class="formEdicaoLabel" for="ProcessoNumeroCNJCodigoOrigem">.8.09.</label><br>
					    <input class="formEdicaoInput" name="ProcessoNumeroCNJCodigoOrigem" id="ProcessoNumeroCNJCodigoOrigem"  type="text" size="3" maxlength="4" value="" onkeyup=" autoTab(this,4)" onkeypress="return DigitarNumeroProcesso(this, event)"/>
					    <br /><br />
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>