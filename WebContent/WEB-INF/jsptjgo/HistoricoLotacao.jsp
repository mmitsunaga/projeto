<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.HistoricoLotacaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<jsp:useBean id="HistoricoLotacaodt" scope="session" class= "br.gov.go.tj.projudi.dt.HistoricoLotacaoDt"/>




<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%-----%>
<%--<jsp:setProperty name="objUsuarioServentia" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de HistoricoLotacao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
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
				if (SeNulo(UsuarioServentia, "O Campo UsuarioServentia é obrigatório!")) return false;
				if (SeNulo(DataInicio, "O Campo DataInicio é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de HistoricoLotacao</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="HistoricoLotacao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="HistoricoLotacao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/HistoricoLotacaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png"/> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados HistoricoLotacao </legend>
					<label class="formEdicaoLabel" for="Id_HistoricoLotacao">Id_HistoricoLotacao</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_HistoricoLotacao"  id="Id_HistoricoLotacao"  type="text"  readonly="true" value="<%=HistoricoLotacaodt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="UsuarioServentia">*UsuarioServentia</label><br> <input class="formEdicaoInput" name="UsuarioServentia" id="UsuarioServentia"  type="text" size="60" maxlength="60" value="<%=HistoricoLotacaodt.getUsuarioServentia()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="Id_UsuarioServentia">*UsuarioServentia
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuarioServentia" name="imaLocalizarUsuarioServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 				
					</label><br>  	<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="UsuarioServentia" id="UsuarioServentia" type="text" size="20" maxlength="20" value="<%=HistoricoLotacaodt.getUsuarioServentia()%>"/><br />
					<label class="formEdicaoLabel" for="DataInicio">*DataInicio</label><br> <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataInicio" id="DataInicio"  type="text" size="10" maxlength="10" value="<%=HistoricoLotacaodt.getDataInicio()%>"> <img id="calendarioDataInicio" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicio,'dd/mm/yyyy',this)"/><br />
					<label class="formEdicaoLabel" for="DataFim">DataFim</label><br> <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataFim" id="DataFim"  type="text" size="10" maxlength="10" value="<%=HistoricoLotacaodt.getDataFim()%>"> <img id="calendarioDataFim" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFim,'dd/mm/yyyy',this)"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
