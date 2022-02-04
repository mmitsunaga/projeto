<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoEventoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ProcessoEventoExecucao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>

<link type="text/css" rel="stylesheet" href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(DataInicio, "O Campo DtInicio é obrigatório!")) return false;
				if (SeNulo(MovimentacaoTipo, "O Campo MovimentacaoTipo é obrigatório!")) return false;
				if (SeNulo(EventoExecucao, "O Campo EventoExecucao é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de ProcessoEventoExecucao</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ProcessoEventoExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ProcessoEventoExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ProcessoEventoExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados ProcessoEventoExecucao </legend>
					<label class="formEdicaoLabel" for="Id_ProcessoEventoExecucao">Id_ProcessoEventoExecucao</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ProcessoEventoExecucao"  id="Id_ProcessoEventoExecucao"  type="text"  readonly="true" value="<%=ProcessoEventoExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="EventoExecucao">*EventoExecucao</label><br> <input class="formEdicaoInput" name="EventoExecucao" id="EventoExecucao"  type="text" size="60" maxlength="60" value="<%=ProcessoEventoExecucaodt.getEventoExecucao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="DtInicio">*DtInicio</label><br> <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DtInicio" id="DtInicio"  type="text" size="10" maxlength="10" value="<%=ProcessoEventoExecucaodt.getDataInicio()%>"> <img id="calendarioDtInicio" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DtInicio,'dd/mm/yyyy',this)" /><br />
					<label class="formEdicaoLabel" for="Quantidade">Quantidade</label><br> <input class="formEdicaoInput" name="Quantidade" id="Quantidade"  type="text" size="11" maxlength="11" value="<%=ProcessoEventoExecucaodt.getQuantidade()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Id_Movimentacao">*Movimentacao <input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacao" name="imaLocalizarMovimentacao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		</label><br> 			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="MovimentacaoTipo" id="MovimentacaoTipo" type="text" size="60" maxlength="255" value="<%=ProcessoEventoExecucaodt.getMovimentacaoTipo()%>"><br />
					<label class="formEdicaoLabel" for="Id_EventoExecucao">*EventoExecucao <input class="FormEdicaoimgLocalizar" id="imaLocalizarEventoExecucao" name="imaLocalizarEventoExecucao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EventoExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		</label><br> 			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="EventoExecucao" id="EventoExecucao" type="text" size="20" maxlength="20" value="<%=ProcessoEventoExecucaodt.getEventoExecucao()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
