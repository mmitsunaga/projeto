<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteSinalDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="ProcessoPartedt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoParteDt" />
<jsp:useBean id="Enderecodt" scope="session" class="br.gov.go.tj.projudi.dt.EnderecoDt" />

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<title>|<%=request.getAttribute("tempPrograma")%>| Lista parte</title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<link href="./css/Paginacao.css" type="text/css" rel="stylesheet" />
	
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_ProcessoParte")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaProcessoParte")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>

	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<%@ include file="./js/Paginacao.js"%>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Lista de Sentenciados </h2></div>
		<div id="divLocalizar" class="divLocalizar">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_ProcessoParte").toString()%>" name="<%=request.getAttribute("tempBuscaId_ProcessoParte").toString()%>" />
			<input type="hidden" id="<%=request.getAttribute("tempBuscaProcessoParte").toString()%>" name="<%=request.getAttribute("tempBuscaProcessoParte").toString()%>" />
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<%@ include file="DadosProcessoParteExecucao.jspf"%>
		</div>
			<%@ include file="./Padroes/Paginacao.jspf"%>
			<div id="divBotoesCentralizados" class="divBotoesCentralizados"><input name="imgInserir" type="submit" value="Inserir Nova Parte" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','16');"></div>
		</form>
	</div>
	
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
