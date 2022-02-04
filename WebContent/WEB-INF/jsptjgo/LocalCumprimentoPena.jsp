<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="LocalCumprimentoPenadt" scope="session" class= "br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"/>
<jsp:useBean id="Enderecodt" scope="session" class= "br.gov.go.tj.projudi.dt.EnderecoDt"/>
<%@page import="br.gov.go.tj.projudi.dt.EnderecoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Local de Cumprimento de Pena  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(LocalCumprimentoPena, "O Campo LocalCumprimentoPena é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Local de Cumprimento de Pena</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="LocalCumprimentoPena" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="LocalCumprimentoPena" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/LocalCumprimentoPenaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Local de Cumprimento de Pena </legend>
					<label class="formEdicaoLabel" for="Id_LocalCumprimentoPena">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_LocalCumprimentoPena"  id="Id_LocalCumprimentoPena"  type="text"  readonly="true" value="<%=LocalCumprimentoPenadt.getId()%>"><br />
					<label class="formEdicaoLabel" for="LocalCumprimentoPena">*Nome</label><br> <input class="formEdicaoInput" name="LocalCumprimentoPena" id="LocalCumprimentoPena"  type="text" size="60" maxlength="60" value="<%=LocalCumprimentoPenadt.getLocalCumprimentoPena()%>" onkeyup=" autoTab(this,60)"><br />
					<fieldset> 
							<legend> Endereço </legend>
							<div class="col80">    				
							<label class="formEdicaoLabel" for="Logradouro">*Logradouro</label><br> 
							<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="105" maxlength="100" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getLogradouro()%>" onkeyup=" autoTab(this,60)">
							</div>
					    	<div class="col15"> 			
					    	<label class="formEdicaoLabel" for="Numero">Número</label><br> 
					    	<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="11" maxlength="11" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
								</div>
							<div class="col45 clear"> 
							<label class="formEdicaoLabel" for="Complemento">Complemento</label><br> 
							<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="59" maxlength="100" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getComplemento()%>" onkeyup=" autoTab(this,255)">
							</div>
							<div class="col60 clear"> 		
							<label class="formEdicaoLabel" for="Bairro">*Bairro 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							</label><br> 
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="80" maxlength="100" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getBairro()%>">
							</div>
							<div class="col45 clear"> 
							<label class="formEdicaoLabel" for="Cidade">Cidade</label><br>  
							<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="40" maxlength="60" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getCidade()%>">
							</div>
							<div class="col15"> 			
					   		<label class="formEdicaoLabel" for="Uf">UF</label><br> 
							<input class="formEdicaoInputSomenteLeitura" readonly name="Estado" id="Uf"  type="text" size="9" maxlength="10" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getUf()%>">
							</div>
							<div class="col20"> 
							<label class="formEdicaoLabel" for="Cep">*CEP</label><br> 
							<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="<%=LocalCumprimentoPenadt.getEnderecoLocal().getCep()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
							</div>
						</fieldset>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
