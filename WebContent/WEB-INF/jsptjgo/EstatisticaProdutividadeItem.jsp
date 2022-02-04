<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="EstatisticaProdutividadeItemdt" scope="session" class= "br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de EstatisticaProdutividadeItem  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de EstatisticaProdutividadeItem</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="EstatisticaProdutividadeItem" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="EstatisticaProdutividadeItem" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/EstatisticaProdutividadeItemAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados EstatisticaProdutividadeItem </legend>
					<label class="formEdicaoLabel" for="Id_EstatisticaProdutividadeItem">Id_EstatisticaProdutividadeItem</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_EstatisticaProdutividadeItem"  id="Id_EstatisticaProdutividadeItem"  type="text"  readonly="true" value="<%=EstatisticaProdutividadeItemdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="EstatisticaProdutividadeItem">*EstatisticaProdutividadeItem</label><br> <input class="formEdicaoInput" name="EstatisticaProdutividadeItem" id="EstatisticaProdutividadeItem"  type="text" size="60" maxlength="60" value="<%=EstatisticaProdutividadeItemdt.getEstatisticaProdutividadeItem()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="DadoCodigo">DadoCodigo</label><br> <input class="formEdicaoInput" name="DadoCodigo" id="DadoCodigo"  type="text" size="11" maxlength="11" value="<%=EstatisticaProdutividadeItemdt.getDadoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
