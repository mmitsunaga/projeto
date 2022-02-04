<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt"%>

<jsp:useBean id="relatorioProcessoSegundoGrauDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt"/>

<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="java.util.HashMap"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%><html>
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
			<form action="RelatorioProcessoSegundoGrauArea" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relat�rio" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div>
			<br />
			<div id="divEditar" class="divEditar">
				
				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Per�odo</legend>
					<label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Ano de consulta</label><br>
					<select id="AnoConsulta" name ="AnoConsulta" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(relatorioProcessoSegundoGrauDt.getAnoConsulta().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioProcessoSegundoGrauDt.getAnoConsulta().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
										
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetAreaDistribuicao" > 
					<legend class="formEdicaoLegenda">�rea de Distribui��o</legend>
					<label class="formEdicaoLabel" for="Id_AreaDistribuicao">*�rea de Distribui��o 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AreaDistribuicao','AreaDistribuicao'); return false;" />
					</label><br> 
					<input id="Id_AreaDistribuicao" name="Id_AreaDistribuicao" type="hidden" value="<%=relatorioProcessoSegundoGrauDt.getIdAreaDistribuicao()%>"/>
					<input class="formEdicaoInputSomenteLeitura" id="AreaDistribuicao" name="AreaDistribuicao" readonly="true" type="text" size="60" maxlength="60" value="<%=relatorioProcessoSegundoGrauDt.getAreaDistribuicao()%>"/><br />
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