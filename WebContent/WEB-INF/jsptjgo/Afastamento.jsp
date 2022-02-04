<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AfastamentoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="Afastamentodt" scope="session" class= "br.gov.go.tj.projudi.dt.AfastamentoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (Afastamento.value == "") {
					alert(" O campo Descrição é obrigatório!");
					Afastamento.focus();
					return false; 
				}
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Afastamento</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Afastamento" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Afastamento" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/AfastamentoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png"/> </a>
			</div>
			<%//--------------------------------------------------------------------------         %>
			<%//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais. %>
			<%//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:               %>
			<%//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra     %>
			<%//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf              %>
			<%//--------------------------------------------------------------------------         %>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Afastamento </legend>
					<label class="formEdicaoLabel" for="Id_Afastamento">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Afastamento"  id="Id_Afastamento"  type="text"  readonly="true" value="<%=Afastamentodt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="Afastamento">*Descrição</label><br> <input class="formEdicaoInput" name="Afastamento" id="Afastamento"  type="text" size="60" maxlength="60" value="<%=Afastamentodt.getAfastamento()%>" onkeyup=" autoTab(this,60)"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
