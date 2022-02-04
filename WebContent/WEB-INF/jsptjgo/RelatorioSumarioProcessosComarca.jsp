<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosComarcaDt"%>

<jsp:useBean id="RelatorioSumarioProcessosComarcadt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosComarcaDt"/>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
	  	<div id="divLocalizar" class="divLocalizar" > 
			<form action="RelatorioSumarioProcessosComarca" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
			</div><br />
			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Parâmetros de consulta</legend>
					
					<label for="Data">*Quantidade</label><br> 
					<select id="LimiteConsulta" name="LimiteConsulta" class="formEdicaoCombo">
						<option value="10" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("10")){%>selected="true"<%}%>>10</option>
						<option value="20" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("20")){%>selected="true"<%}%>>20</option>
						<option value="30" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("30")){%>selected="true"<%}%>>30</option>
						<option value="40" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("40")){%>selected="true"<%}%>>40</option>
						<option value="50" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("50")){%>selected="true"<%}%>>50</option>
						<option value="60" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("60")){%>selected="true"<%}%>>60</option>
						<option value="70" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("70")){%>selected="true"<%}%>>70</option>
						<option value="80" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("80")){%>selected="true"<%}%>>80</option>
						<option value="90" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("90")){%>selected="true"<%}%>>90</option>
						<option value="100" <%if(RelatorioSumarioProcessosComarcadt.getLimiteConsulta() != null && RelatorioSumarioProcessosComarcadt.getLimiteConsulta().equals("100")){%>selected="true"<%}%>>100</option>
					</select>
					<br/>
					<label class="formEdicaoLabel" for="TipoParte">Tipo de Processo </label><br>  
					<input type="radio" name="TipoProcesso" value="<%=RelatorioSumarioProcessosComarcadt.ATIVO%>" checked="true"/>Ativo 
				    <input type="radio" name="TipoProcesso" value="<%=RelatorioSumarioProcessosComarcadt.DISTRIBUIDO%>"/>Distribuído
				    <input type="radio" name="TipoProcesso" value="<%=RelatorioSumarioProcessosComarcadt.ARQUIVADO%>"/>Arquivado
			    </fieldset>
			</div>
			</form>
			</div>
				<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>