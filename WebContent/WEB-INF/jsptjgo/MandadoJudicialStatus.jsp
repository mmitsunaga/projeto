<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="MandadoJudicialStatusdt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Status Mandado Judicial  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(MandadoJudicialStatus, "O Campo MandadoJudicialStatus é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;  Cadastro de Status de Mandado Judicial</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="MandadoJudicialStatus" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="MandadoJudicialStatus" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/MandadoJudicialStatusAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao" > 
					<legend class="formEdicaoLegenda">Edita os dados MandadoJudicialStatus </legend>
					<label class="formEdicaoLabel" for="Id_MandadoJudicialStatus">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_MandadoJudicialStatus" id="Id_MandadoJudicialStatus" type="text" readonly="true" value="<%=MandadoJudicialStatusdt.getId()%>">
					<br />
					
					<label class="formEdicaoLabel" for="MandadoJudicialStatusCodigo">Código</label><br>
					<input class="formEdicaoInput" name="MandadoJudicialStatusCodigo" id="MandadoJudicialStatusCodigo" type="text" size="11" maxlength="11" value="<%=MandadoJudicialStatusdt.getMandadoJudicialStatusCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
					<br />
					
					<label class="formEdicaoLabel" for="MandadoJudicialStatus">*Status Mandado Judicial</label><br>
					<input class="formEdicaoInput" name="MandadoJudicialStatus" id="MandadoJudicialStatus" type="text" size="60" maxlength="60" value="<%=MandadoJudicialStatusdt.getMandadoJudicialStatus()%>" onkeyup=" autoTab(this,60)">
					<br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
