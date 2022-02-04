<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.EventoExecucaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="EventoExecucaodt" scope="session" class= "br.gov.go.tj.projudi.dt.EventoExecucaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de EventoExecucao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(EventoExecucao, "O Campo EventoExecucao é obrigatório!")) return false;
				if (SeNulo(EventoExecucaoCodigo, "O Campo EventoExecucaoCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de EventoExecucao</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="EventoExecucao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="EventoExecucao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/EventoExecucaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados EventoExecucao </legend>
					<label class="formEdicaoLabel" for="Id_EventoExecucao">Id_EventoExecucao</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_EventoExecucao"  id="Id_EventoExecucao"  type="text"  readonly="true" value="<%=EventoExecucaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="EventoExecucao">*EventoExecucao</label><br> <input class="formEdicaoInput" name="EventoExecucao" id="EventoExecucao"  type="text" size="60" maxlength="60" value="<%=EventoExecucaodt.getEventoExecucao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="AlteraLocal">AlteraLocal</label><br> <input class="formEdicaoInput" name="AlteraLocal" id="AlteraLocal"  type="text" size="1" maxlength="1" value="<%=EventoExecucaodt.getAlteraLocal()%>" onkeyup=" autoTab(this,1)"><br />
					<label class="formEdicaoLabel" for="AlteraRegime">AlteraRegime</label><br> <input class="formEdicaoInput" name="AlteraRegime" id="AlteraRegime"  type="text" size="1" maxlength="1" value="<%=EventoExecucaodt.getAlteraRegime()%>" onkeyup=" autoTab(this,1)"><br />
					<label class="formEdicaoLabel" for="ValorNegativo">ValorNegativo</label><br> <input class="formEdicaoInput" name="ValorNegativo" id="ValorNegativo"  type="text" size="1" maxlength="1" value="<%=EventoExecucaodt.getValorNegativo()%>" onkeyup=" autoTab(this,1)"><br />
					<label class="formEdicaoLabel" for="EventoExecucaoCodigo">*EventoExecucaoCodigo</label><br> <input class="formEdicaoInput" name="EventoExecucaoCodigo" id="EventoExecucaoCodigo"  type="text" size="6" maxlength="6" value="<%=EventoExecucaodt.getEventoExecucaoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,6)"><br />
					<label class="formEdicaoLabel" for="Id_EventoExecucaoTipo">EventoExecucaoTipo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarEventoExecucaoTipo" name="imaLocalizarEventoExecucaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EventoExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparEventoExecucaoTipo" name="imaLimparEventoExecucaoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_EventoExecucaoTipo','EventoExecucaoTipo'); return false;" >
					</label><br>   <input  name='Id_EventoExecucaoTipo' id='Id_EventoExecucaoTipo' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="EventoExecucaoTipo" id="EventoExecucaoTipo" type="text" size="20" maxlength="20" value="<%=EventoExecucaodt.getEventoExecucaoTipo()%>"><br />
					<label class="formEdicaoLabel" for="Id_EventoExecucaoStatus">EventoExecucaoStatus
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarEventoExecucaoStatus" name="imaLocalizarEventoExecucaoStatus" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EventoExecucaoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparEventoExecucaoStatus" name="imaLimparEventoExecucaoStatus" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_EventoExecucaoStatus','EventoExecucaoStatus'); return false;" >
					</label><br>   <input  name='Id_EventoExecucaoStatus' id='Id_EventoExecucaoStatus' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="EventoExecucaoStatus" id="EventoExecucaoStatus" type="text" size="20" maxlength="20" value="<%=EventoExecucaodt.getEventoExecucaoStatus()%>"><br />
					<label class="formEdicaoLabel" for="Observacao">Observacao</label><br>  
					<input  class="formEdicaoInput"  name="Observacao" id="Observacao" type="text" size="120" maxlength="200" value="<%=EventoExecucaodt.getObservacao()%>"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
