<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt"%>

<jsp:useBean id="relatorioProcessoSegundoGrauDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="java.util.HashMap"%>
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
			<form action="RelatorioProcessoSegundoGrauDesembargadores" method="post" name="Formulario" id="Formulario">
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
					for(int i = Funcoes.StringToInt(relatorioProcessoSegundoGrauDt.getAnoConsulta().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioProcessoSegundoGrauDt.getAnoConsulta().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetServentia" > 
					<legend class="formEdicaoLegenda">Serventia</legend>
					<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" />
					</label><br> 
					<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%=relatorioProcessoSegundoGrauDt.getIdServentiaConsulta()%>"/>
					<input class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly="true" type="text" size="60" maxlength="60" value="<%=relatorioProcessoSegundoGrauDt.getServentiaConsulta()%>"/><br />
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