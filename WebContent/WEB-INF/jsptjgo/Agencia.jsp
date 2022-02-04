<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AgenciaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="Agenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.AgenciaDt"/>

<%@page import="br.gov.go.tj.projudi.dt.BancoDt"%>
<%-----%>
<%--<jsp:setProperty name="objBanco" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Agência  </title>
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
				if (SeNulo(Agencia, "O campo Nome é obrigatório!")) return false;
				if (SeNulo(AgenciaCodigo, "O campo Código é obrigatório!")) return false;
				if (SeNulo(Banco, "O campo Banco é obrigatório!")) return false;
				if (SeNulo(BancoCodigo, "O campo Banco é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Agência</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Agencia" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Agencia" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/AgenciaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao" id="Campos_Agencia" > 
					<legend class="formEdicaoLegenda">Edita os dados de Agência </legend>
					<label class="formEdicaoLabel" for="Id_Agencia">Identificador</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="Id_Agencia"  id="Id_Agencia"  type="text"  readonly="true" value="<%=Agenciadt.getId()%>" /><br />
					<label class="formEdicaoLabel" for="Agencia">*Nome</label><br> <input class="formEdicaoInput" name="Agencia" id="Agencia"  type="text" size="60" maxlength="60" value="<%=Agenciadt.getAgencia()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="AgenciaCodigo">*Código</label><br> <input class="formEdicaoInput" name="AgenciaCodigo" id="AgenciaCodigo"  type="text" size="11" maxlength="11" value="<%=Agenciadt.getAgenciaCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
					<label class="formEdicaoLabel" for="Id_Banco">*Banco
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarBanco" name="imaLocalizarBanco" type="image"  src="./imagens/imgLocalizarPequena.png"  					
					onclick="MostrarBuscaPadrao('Campos_Agencia','Agencia','Consulta de Banco', 'Digite o banco e clique em consultar.', 'Id_Banco', 'Banco', ['Banco'], [], '<%=(BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
					<input id="Id_Banco" name="Id_Banco" type="hidden" value="<%=Agenciadt.getId_Banco()%>" /> 					
					</label><br>   					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Banco" id="Banco" type="text" size="60" maxlength="60" value="<%=Agenciadt.getBanco()%>"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
