<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ContaUsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<jsp:useBean id="ContaUsuariodt" scope="session" class= "br.gov.go.tj.projudi.dt.ContaUsuarioDt"/>




<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%-----%>
<%--<jsp:setProperty name="objUsuario" property="*"/>--%>

<%@page import="br.gov.go.tj.projudi.dt.AgenciaDt"%>
<%-----%>
<%--<jsp:setProperty name="objAgencia" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Conta de Usuário  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
				with(document.Formulario) {
					if (SeNulo(ContaUsuario, "O campo Número é obrigatório!")) return false;
					if (SeNulo(Usuario, "O campo Usuário é obrigatório!")) return false;
					if (SeNulo(Agencia, "O campo Agência é obrigatório!")) return false;
					if (SeNulo(Ativa, "O campo Ativa é obrigatório!")) return false;
					submit();
				}
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Conta de Usuário</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ContaUsuario" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ContaUsuario" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				
				<a class="divPortaBotoesLink" href="Ajuda/ContaUsuarioAjuda.html" target="_blank">  
				<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png"/> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Conta de Usuário </legend>
				
					<label class="formEdicaoLabel" for="Id_ContaUsuario">Identificador</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="Id_ContaUsuario"  id="Id_ContaUsuario"  size = "5" type="text"  readonly="true" value="<%=ContaUsuariodt.getId()%>"/><br />
										
					<label class="formEdicaoLabel" for="OperacaoContaUsuario">Operação</label><br> 
					<input class="formEdicaoInput" name="OperacaoContaUsuario" id="OperacaoContaUsuario"  type="text" size="10" maxlength="3" value="<%=ContaUsuariodt.getOperacaoContaUsuario()%>" onkeyup=" autoTab(this,3)"/><br />
									
					<label class="formEdicaoLabel" for="ContaUsuario">*Número</label><br> 
					<input class="formEdicaoInput" name="ContaUsuario" id="ContaUsuario"  type="text" size="10" maxlength="6" value="<%=ContaUsuariodt.getContaUsuario()%>" onkeyup=" autoTab(this,60)"/><br />
										
					<label class="formEdicaoLabel" for="DvContaUsuario">Dígito</label><br> 
					<input class="formEdicaoInput" name="DvContaUsuario" id="DvContaUsuario"  type="text" size="10" maxlength="1" value="<%=ContaUsuariodt.getDvContaUsuario()%>" onkeyup=" autoTab(this,1)"/><br />
								
					<label class="formEdicaoLabel" for="Id_Usuario">*Usuário 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario" name="imaLocalizarUsuario" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br> 
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Usuario" id="Usuario" type="text" size="60" maxlength="30" value="<%=ContaUsuariodt.getUsuario()%>"/><br />
				
				
					<label class="formEdicaoLabel" for="Id_Agencia">*Agência 
				
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarAgencia" name="imaLocalizarAgencia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AgenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					
					</label><br> <input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Agencia" id="Agencia" type="text" size="60" maxlength="60" value="<%=ContaUsuariodt.getAgencia()%>"/><br />
				
				
				
					<label for="Ativa">*Ativa</label> <input class="formEdicaoInput" name="Ativa" id="Ativa"  type="checkbox"  value="true" <% if(ContaUsuariodt.getAtiva().equalsIgnoreCase("true")){%>  checked<%}%>/><br />
				</fieldset>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
