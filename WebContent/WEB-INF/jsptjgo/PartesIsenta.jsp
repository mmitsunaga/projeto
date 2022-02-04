<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PartesIsentaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="PartesIsentadt" scope="session" class= "br.gov.go.tj.projudi.dt.PartesIsentaDt"/>

<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de PartesIsenta  </title>
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
				if (SeNulo(Nome, "O Campo Nome é obrigatório!")) return false;
				if (SeNulo(NomeUsuarioCadastrador, "O Campo NomeUsuarioCadastrador é obrigatório!")) return false;
				if (SeNulo(ServentiaUsuarioCadastrador, "O Campo ServentiaUsuarioCadastrador é obrigatório!")) return false;
				if (SeNulo(DataCadastro, "O Campo DataCadastro é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de Partes Isentas</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="PartesIsenta" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="PartesIsenta" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/PartesIsentaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Partes Isentas </legend>
					
					<label class="formEdicaoLabel" for="Id_PartesIsentas">Identificador</label>
					 <input class="formEdicaoInputSomenteLeitura" name="Id_PartesIsentas"  id="Id_PartesIsentas"  type="text"  readonly="true" value="<%=PartesIsentadt.getId()%>"><br />
					
					<label class="formEdicaoLabel" for="Nome">*Nome</label> 
					<input <%if (PartesIsentadt.getDataBaixa() != null && PartesIsentadt.getDataBaixa().length()>0){%>  class="formEdicaoInputSomenteLeitura" readonly="true"<%} else {%> class="formEdicaoInput"  <%} %> name="Nome" id="Nome"  type="text" size="60" maxlength="60" value="<%=PartesIsentadt.getNome()%>" onkeyup=" autoTab(this,60)"><br />
					
					<label class="formEdicaoLabel" for="Cnpj">*CNPJ</label> 
					<input  <%if (PartesIsentadt.getDataBaixa() != null && PartesIsentadt.getDataBaixa().length()>0){%>  class="formEdicaoInputSomenteLeitura" readonly="true"<%} else {%> class="formEdicaoInput"  <%} %> name="Cnpj" id="Cnpj"  type="text" size="20" maxlength="14" value="<%=PartesIsentadt.getCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"><br />
					
					<label class="formEdicaoLabel" for="DataCadastro">Data Cadastro</label> 
					<input class="formEdicaoInputSomenteLeitura" name="DataCadastro" id="DataCadastro"   readonly="true"  type="text" size="20" maxlength="10" value="<%=PartesIsentadt.getDataCadastro()%>"><br />
					
					<label class="formEdicaoLabel" for="DataBaixa">Data Baixa</label> 
					<input class="formEdicaoInputSomenteLeitura" name="DataBaixa" id="DataBaixa" readonly="true"  type="text" size="20" maxlength="10" value="<%=PartesIsentadt.getDataBaixa()%>" ><br />
					
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
