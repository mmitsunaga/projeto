<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="CondenacaoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CrimeExecucaoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de CondenacaoExecucao  </title>
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
				if (SeNulo(TempoPena, "O Campo TempoPena é obrigatório!")) return false;
				if (SeNulo(Reincidente, "O Campo Reincidente é obrigatório!")) return false;
				if (SeNulo(ProcessoNumero, "O Campo ProcessoNumero é obrigatório!")) return false;
				if (SeNulo(CrimeExecucao, "O Campo CrimeExecucao é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de CondenacaoExecucao</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="CondenacaoExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="CondenacaoExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/CondenacaoExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados CondenacaoExecucao </legend>
					<label class="formEdicaoLabel" for="Id_CondenacaoExecucao">Id_CondenacaoExecucao</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_CondenacaoExecucao"  id="Id_CondenacaoExecucao"  type="text"  readonly="true" value="<%=CondenacaoExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="CrimeExecucao">*CrimeExecucao</label><br> <input class="formEdicaoInput" name="CrimeExecucao" id="CrimeExecucao"  type="text" size="60" maxlength="60" value="<%=CondenacaoExecucaodt.getCrimeExecucao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="TempoPena">*TempoPena</label><br> <input class="formEdicaoInput" name="TempoPena" id="TempoPena"  type="text" size="11" maxlength="11" value="<%=CondenacaoExecucaodt.getTempoPena()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Reincidente">*Reincidente</label><br> <input class="formEdicaoInput" name="Reincidente" id="Reincidente"  type="checkbox"  value="true" <% if(CondenacaoExecucaodt.getReincidente().equalsIgnoreCase("true")){%>  checked<%}%>><br />
					<label class="formEdicaoLabel" for="DataFato">DataFato</label><br> <img id="calendarioDataFato" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFato,'dd/mm/yyyy',this)" /><br /> <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataFato" id="DataFato"  type="text" size="10" maxlength="10" value="<%=CondenacaoExecucaodt.getDataFato()%>">
					<label class="formEdicaoLabel" for="Id_ProcessoExecucao">*ProcessoExecucao
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoExecucao" name="imaLocalizarProcessoExecucao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 			
					</label><br>  		<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProcessoNumero" id="ProcessoNumero" type="text" size="20" maxlength="20" value="<%=CondenacaoExecucaodt.getProcessoNumero()%>"><br />
					<label class="formEdicaoLabel" for="Id_CrimeExecucao">*CrimeExecucao
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarCrimeExecucao" name="imaLocalizarCrimeExecucao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CrimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br>  <input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="CrimeExecucao" id="CrimeExecucao" type="text" size="60" maxlength="60" value="<%=CondenacaoExecucaodt.getCrimeExecucao()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
