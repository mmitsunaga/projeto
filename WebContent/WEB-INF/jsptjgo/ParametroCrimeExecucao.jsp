<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ParametroCrimeExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.CrimeExecucaoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Parâmetro dos Crimes - Execução Penal </title>
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
				if (SeNulo(CrimeExecucao, "O campo Crime é obrigatório!")) return false;
				if (SeNulo(Data, "O campo Data é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Parâmetro dos Crimes - Execução Penal </h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ParametroCrimeExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ParametroCrimeExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ParametroCrimeExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Parâmetros dos Crimes - Execução Penal </legend>
					<label class="formEdicaoLabel" for="Id_ParametroCrimeExecucao">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ParametroCrimeExecucao"  id="Id_ParametroCrimeExecucao"  type="text"  readonly="true" value="<%=ParametroCrimeExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Id_CrimeExecucao">*Crime  <input class="FormEdicaoimgLocalizar" id="imaLocalizarCrimeExecucao" name="imaLocalizarCrimeExecucao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CrimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		</label><br>			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="CrimeExecucao" id="CrimeExecucao" type="text" size="100" maxlength="60" value="<%=ParametroCrimeExecucaodt.getCrimeExecucao()%>" title="<%=ParametroCrimeExecucaodt.getCrimeExecucao()%>"><br />
					<label class="formEdicaoLabel" for="Data">*Data</label><br>
						<img id="calendarioData" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].Data,'dd/mm/yyyy',this)" />
						<input class="formEdicaoInput" name="Data" id="Data"  type="text" size="10" maxlength="10" value="<%=ParametroCrimeExecucaodt.getData()%>" onkeypress="return formataCampo(event, this, 10)"><br /> 
						<div class="clear">&nbsp;</div>
					<input name="chkParametro" id="chkParametro"  type="checkbox"  value="HediondoProgressao" <% if(ParametroCrimeExecucaodt.getHediondoProgressao().equalsIgnoreCase("true")){%>  checked<%}%>/><label style="margin-left: 5px" for="HediondoProgressao">Hediondo para Progressão de Regime</label><br> <br />
					<input name="chkParametro" id="chkParametro"  type="checkbox"  value="HediondoLivramCond" <% if(ParametroCrimeExecucaodt.getHediondoLivramCond().equalsIgnoreCase("true")){%>  checked<%}%>/><label style="margin-left: 5px" for="HediondoLivramCond">Hediondo para Livramento Condicional</label><br> <br />
					<input name="chkParametro" id="chkParametro"  type="checkbox"  value="EquiparaHediondoLivramCond" <% if(ParametroCrimeExecucaodt.getEquiparaHediondoLivramCond().equalsIgnoreCase("true")){%>  checked<%}%>/><label style="margin-left: 5px" for="EquiparaHediondoLivramCond">Crime comum - Fração equiparada com Hediondo para Livramento Condicional</label><br> <br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
