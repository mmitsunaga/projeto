<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoFaixaValorDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="MandadoFaixaValordt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoFaixaValorDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoFaixaValor  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
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
		<div id="divTitulo" class="divTitulo"> Cadastro de MandadoFaixaValor</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="MandadoFaixaValor" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="MandadoFaixaValor" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/MandadoFaixaValorAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados MandadoFaixaValor </legend>
					<label class="formEdicaoLabel" for="Id_MandadoFaixaValor">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_MandadoFaixaValor"  id="Id_MandadoFaixaValor"  type="text"  readonly="true" value="<%=MandadoFaixaValordt.getId()%>"><br />
					<label class="formEdicaoLabel" for="FaixaInicio">*FaixaInicio</label> <input class="formEdicaoInput" name="FaixaInicio" id="FaixaInicio"  type="text" size="60" maxlength="60" value="<%=MandadoFaixaValordt.getFaixaInicio()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="FaixaFim">FaixaFim</label> <input class="formEdicaoInput" name="FaixaFim" id="FaixaFim"  type="text" size="22" maxlength="22" value="<%=MandadoFaixaValordt.getFaixaFim()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="DataVigenciaInicio">DataVigenciaInicio</label> <input class="formEdicaoInput" name="DataVigenciaInicio" id="DataVigenciaInicio"  type="text" size="7" maxlength="7" value="<%=MandadoFaixaValordt.getDataVigenciaInicio()%>" onkeypress="return DigitarHoraResumida(this, event)" onkeyup=" MascararHoraResumida(this); autoTab(this,7)"><br />
					<label class="formEdicaoLabel" for="DataVigenciaFim">DataVigenciaFim</label> <input class="formEdicaoInput" name="DataVigenciaFim" id="DataVigenciaFim"  type="text" size="7" maxlength="7" value="<%=MandadoFaixaValordt.getDataVigenciaFim()%>" onkeypress="return DigitarHoraResumida(this, event)" onkeyup=" MascararHoraResumida(this); autoTab(this,7)"><br />
					<label class="formEdicaoLabel" for="Valor">Valor</label> <input class="formEdicaoInput" name="Valor" id="Valor"  type="text" size="22" maxlength="22" value="<%=MandadoFaixaValordt.getValor()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
