<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.TipificacaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Tipificacaodt" scope="session" class= "br.gov.go.tj.projudi.dt.TipificacaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipificacao  </title>
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
				if (SeNulo(Tipificacao, "O Campo Tipificacao é obrigatório!")) return false;
				if (SeNulo(Lei, "O Campo Lei é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de Tipificacao</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Tipificacao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Tipificacao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/TipificacaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				</div>
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Tipificacao </legend>
					<label class="formEdicaoLabel" for="Id_Tipificacao">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_Tipificacao"  id="Id_Tipificacao"  type="text"  readonly="true" value="<%=Tipificacaodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Tipificacao">*Tipificacao</label> <input class="formEdicaoInput" name="Tipificacao" id="Tipificacao"  type="text" size="60" maxlength="60" value="<%=Tipificacaodt.getTipificacao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Lei">*Lei</label> <input class="formEdicaoInput" name="Lei" id="Lei"  type="text" size="60" maxlength="60" value="<%=Tipificacaodt.getLei()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Artigo">Artigo</label> <input class="formEdicaoInput" name="Artigo" id="Artigo"  type="text" size="20" maxlength="20" value="<%=Tipificacaodt.getArtigo()%>" onkeyup=" autoTab(this,20)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
