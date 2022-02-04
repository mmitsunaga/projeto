<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PrazoSuspensoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>

<jsp:useBean id="PrazoSuspensodt" scope="session" class= "br.gov.go.tj.projudi.dt.PrazoSuspensoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Prazo Suspenso  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(PrazoSuspensoTipo, "O campo Tipo é obrigatório!")) return false;
				if (SeNulo(Data, "O Campo Data é obrigatório!")) return false;
				if (SeNulo(PrazoSuspensoTipoCodigo, "O Campo PrazoSuspensoTipoCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Prazo Suspenso</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="PrazoSuspenso" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="PrazoSuspenso" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/PrazoSuspensoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Prazo Suspenso </legend>
					<label class="formEdicaoLabel" for="DataLancamento">Data de Lançamento</label><br> <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataLancamento" id="DataLancamento"  type="text" size="10" maxlength="10" value="<%=PrazoSuspensodt.getDataLancamento()%>"><br />
					<label class="formEdicaoLabel" for="Motivo">Motivo</label><br> <input class="formEdicaoInput" name="Motivo" id="Motivo"  type="text" size="60" maxlength="60" value="<%=PrazoSuspensodt.getMotivo()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="Id_PrazoSuspensoTipo">*Tipo <input class="FormEdicaoimgLocalizar" id="imaLocalizarPrazoSuspensoTipo" name="imaLocalizarPrazoSuspensoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(PrazoSuspensoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 		</label><br> 			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="PrazoSuspensoTipo" id="PrazoSuspensoTipo" type="text" size="60" maxlength="60" value="<%=PrazoSuspensodt.getPrazoSuspensoTipo()%>"/><br />
					<label class="formEdicaoLabel" for="Id_Comarca">Comarca  <input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparComarca" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" > </label><br><input  name='Id_Comarca' id='Id_Comarca' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Comarca" id="Comarca" type="text" size="60" maxlength="100" value="<%=PrazoSuspensodt.getComarca()%>"/><br />
					<label class="formEdicaoLabel" for="Id_Serventia">Serventia  <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" > </label><br><input  name='Id_Serventia' id='Id_Serventia' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=PrazoSuspensodt.getServentia()%>"/><br />
					<label class="formEdicaoLabel" for="Id_Cidade">Cidade  <input class="FormEdicaoimgLocalizar" id="imaLocalizarCidade" name="imaLocalizarCidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparCidade" name="imaLimparCidade" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Cidade','Cidade'); return false;" > </label><br><input  name='Id_Cidade' id='Id_Cidade' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Cidade" id="Cidade" type="text" size="60" maxlength="60" value="<%=PrazoSuspensodt.getCidade()%>"/><br />
					<label class="formEdicaoLabel" for="DataFeriado">*Data <img id="calendarioData" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFeriado,'dd/mm/yyyy',this)"/></label><br> <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataFeriado" id="DataFeriado"  type="text" size="10" maxlength="10" value="<%=PrazoSuspensodt.getData()%>"><br />
					<label class="formEdicaoLabel" for="Or"> Ou </label><br><br />
					<label class="formEdicaoLabel" style="float:left;" for="DataInicial">*Data Inicial <img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" /></label><br> 
					<input class="formEdicaoInputSomenteLeitura" style="float:left;" readonly="true" name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="<%=PrazoSuspensodt.getDataInicial()%>"><br>
					<label class="formEdicaoLabel"  style="float:left;" for="DataFinal">*Data Final <img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)"/></label><br> 
					<input class="formEdicaoInputSomenteLeitura" style="float:left;" readonly="true" name="DataFinal" id="DataFinal"  type="text" size="10" maxlength="10" value="<%=PrazoSuspensodt.getDataFinal()%>"> 
					<br />
					<br>		    		
		    		<div class="clear">&nbsp;&nbsp;
		    		<label for="PlantaoLiberado">Cadastro no Plantão aberto?</label>&nbsp;&nbsp;&nbsp;		    		
		    		<input type="radio" name="PlantaoLiberado" id="PlantaoLiberado" value="1" <%if(PrazoSuspensodt.getPlantaoLiberado().equalsIgnoreCase("1")){%> checked="true" <%}%>/>Sim 
				    <input type="radio" name="PlantaoLiberado" id="PlantaoLiberado" value="0" <%if(PrazoSuspensodt.getPlantaoLiberado().equalsIgnoreCase("0")){%> checked="true" <%}%>/>Não
			    	</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
