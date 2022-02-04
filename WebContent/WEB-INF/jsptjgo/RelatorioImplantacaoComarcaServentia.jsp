<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>

<jsp:useBean id="RelatorioImplantacaodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioImplantacaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioImplantacao" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
				<div id="divPortaBotoes" class="divPortaBotoes">
					<input id="imgNovo" class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')">
					<input id="imgImprimir" class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" src="./imagens/imgImprimir.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
				</div><br/>
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Dados da Consulta</legend>
							<label class="formEdicaoLabel" for="Id_Comarca">Comarca
								<input class="FormEdicaoimgLocalizar" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
								<input class="FormEdicaoimgLocalizar" id="imaLimparComarca" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" />
								</label><br>
								<input id="Id_Comarca" name="Id_Comarca" type="hidden" value="<%=RelatorioImplantacaodt.getId_Comarca()%>"/>
								<input  class="formEdicaoInputSomenteLeitura" id="Comarca" name="Comarca" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioImplantacaodt.getComarca()%>"/><br />
							<label class="formEdicaoLabel" for="Id_ServentiaTipo">ServentiaTipo
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaTipo" name="imaLocalizarServentiaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
								<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaTipo" name="imaLimparServentiaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaTipo','ServentiaTipo'); return false;" >
								
								</label><br>
								<input  name="Id_ServentiaTipo" id="Id_ServentiaTipo" type="hidden"  value="<%=RelatorioImplantacaodt.getId_ServentiaTipo()%>">
								<input  class="formEdicaoInputSomenteLeitura" id="ServentiaTipo" name="ServentiaTipo" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioImplantacaodt.getServentiaTipo()%>"/><br />
								
					</fieldset>

				</div>
			</form>
		</div>
		<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
			<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
		<%}%>
		
		<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
			<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().trim()%></div>
		<%}%>
	</body>
</html>