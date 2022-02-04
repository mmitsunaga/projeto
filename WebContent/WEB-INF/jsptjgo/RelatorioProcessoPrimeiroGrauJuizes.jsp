<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoPrimeiroGrauDt"%>

<jsp:useBean id="relatorioProcessoPrimeiroGrauDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoPrimeiroGrauDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioProcessoPrimeiroGrauJuizes" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
			</div>
			<br />
			<div id="divEditar" class="divEditar">
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Período</legend>
					<label class="formEdicaoLabel" for="AnoConsulta">*Ano de consulta</label><br>
					<select id="AnoConsulta" name ="AnoConsulta" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(relatorioProcessoPrimeiroGrauDt.getAnoConsulta().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioProcessoPrimeiroGrauDt.getAnoConsulta().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetServentia" > 
					<legend class="formEdicaoLegenda">Serventia</legend>
					<label style="float:left;margin-left:25px;color:black" ><%=relatorioProcessoPrimeiroGrauDt.getServentiaConsulta()%></label><br>
				</fieldset> 
				
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>