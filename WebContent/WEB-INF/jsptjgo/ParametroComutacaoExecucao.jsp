<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ParametroComutacaoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ParametroComutacaoExecucao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
<link type="text/css" rel="stylesheet" href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(DataDecreto, "O Campo DataDecreto � obrigat�rio!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Par�metro das Comuta��es conforme Decreto Presidencial</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ParametroComutacaoExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ParametroComutacaoExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ParametroComutacaoExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados dos Par�metros das Comuta��es, conforme Decreto Presidencial </legend>
					<br />
					<label class="formEdicaoLabel" for="titulo"><b><small>Os dados abaixo ser�o utilizados no c�lculo de COMUTA��O PR�VIA</small></b></label><br> <br /><br />					
					<label class="formEdicaoLabel" for="Id_ParametroComutacaoExecucao">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ParametroComutacaoExecucao"  id="Id_ParametroComutacaoExecucao"  type="text"  readonly="true" value="<%=ParametroComutacaoExecucaodt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="DataDecreto">*Data do Decreto</label><br> 
						<img id="calendarioData" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calend�rio"  alt="Calend�rio" onclick="displayCalendar(document.forms[0].Data,'dd/mm/yyyy',this)" />
						<input class="formEdicaoInput" name="DataDecreto" id="DataDecreto"  type="text" size="10" maxlength="10" value="<%=ParametroComutacaoExecucaodt.getDataDecreto()%>"  onkeypress="return formataCampo(event, this, 10)"><br /> 
					<label class="formEdicaoLabel" for="FracaoHediondo">Fra��o p/ Crime Hediondo</label><br> <input class="formEdicaoInput" name="FracaoHediondo" id="FracaoHediondo"  type="text" size="3" maxlength="3" value="<%=ParametroComutacaoExecucaodt.getFracaoHediondo()%>" onkeyup=" autoTab(this,3)"/>
						<label class="formEdicaoLabel" for="descricao" ><small>Se o sentenciado n�o tiver direito ao c�lculo de comuta��o quando tiver crime hediodo, deixar este campo vazio!</small></label><br> <br />
					<label class="formEdicaoLabel" for="FracaoComum">*Fra��o p/ Crime Comum</label><br> <input class="formEdicaoInput" name="FracaoComum" id="FracaoComum"  type="text" size="3" maxlength="3" value="<%=ParametroComutacaoExecucaodt.getFracaoComum()%>" onkeyup=" autoTab(this,3)"/><br />
					<label class="formEdicaoLabel" for="FracaoComumReinc">*Fra��o p/ Crime Comum Reincidente</label><br> <input class="formEdicaoInput" name="FracaoComumReinc" id="FracaoComumReinc"  type="text" size="3" maxlength="3" value="<%=ParametroComutacaoExecucaodt.getFracaoComumReinc()%>" onkeyup=" autoTab(this,3)"/><br />
					<label class="formEdicaoLabel" for="FracaoComumFeminino">Fra��o p/ Crime Comum p/ Mulheres respons�veis por menor ou incapaz (A partir de 2017)</label><br> <input class="formEdicaoInput" name="FracaoComumFeminino" id="FracaoComumFeminino"  type="text" size="3" maxlength="3" value="<%=ParametroComutacaoExecucaodt.getFracaoComumFeminino()%>" onkeyup=" autoTab(this,3)"/><br />
					<label class="formEdicaoLabel" for="FracaoComumReincFeminino">Fra��o p/ Crime Comum Reincidente p/ Mulheres respons�veis por menor ou incapaz (A partir de 2017)</label><br> <input class="formEdicaoInput" name="FracaoComumReincFeminino" id="FracaoComumReincFeminino"  type="text" size="3" maxlength="3" value="<%=ParametroComutacaoExecucaodt.getFracaoComumReincFeminino()%>" onkeyup=" autoTab(this,3)"/><br />
					<label for="PenaUnificada">Considerar Pena Unificada</label> <input class="formEdicaoInput" name="PenaUnificada" id="PenaUnificada"  type="checkbox"  value="true" <% if(ParametroComutacaoExecucaodt.getPenaUnificada().equalsIgnoreCase("true")){%>  checked <%}%>/><br />
					<label for="BeneficioAcumulado">Beneficiar sentenciados j� beneficiados em decretos anteriores</label> <input class="formEdicaoInput" name="BeneficioAcumulado" id="BeneficioAcumulado"  type="checkbox"  value="true" <% if(ParametroComutacaoExecucaodt.getBeneficioAcumulado().equalsIgnoreCase("true")){%>  checked <%}%>/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
			<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
