<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PresidioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.EnderecoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<jsp:useBean id="Presidiodt" scope="session" class= "br.gov.go.tj.projudi.dt.PresidioDt"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Presídio  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>
	<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Presidio, "O Campo Presídio é obrigatório!")) return false;
				submit();
			}
		}
		</script>
	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de Presídio</div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
					<form action="Presidio" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
					<form action="Presidio" method="post" name="Formulario" id="Formulario">
			<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>
					<a class="divPortaBotoesLink" href="Ajuda/PresidioAjuda.html" target="_blank"><img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /></a>
				</div>
				<fieldset class="formEdicao" id='Campos_Presidio' > 
					<legend class="formEdicaoLegenda">Edita os dados Presídio </legend>
					<input id='Id_Endereco' name='Id_Endereco' type='hidden' value="<%=Presidiodt.getId_Endereco()%>" />
					<label class="formEdicaoLabel" for="Id_Presidio">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_Presidio"  id="Id_Presidio"  type="text"  readonly="true" value="<%=Presidiodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Presidio">*Presídio</label> <input class="formEdicaoInput" name="Presidio" id="Presidio"  type="text" size="60" maxlength="60" value="<%=Presidiodt.getPresidio()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Logradouro">*Logradouro</label>
					<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="106" maxlength="100" value="<%=Presidiodt.getLogradouro()%>" onKeyUp=" autoTab(this,60)">  <br>
					
					<label class="formEdicaoLabel" for="Numero">Número</label> 
					<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="11" maxlength="10" value="<%=Presidiodt.getNumero()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)"> <br>
					 	
					<label class="formEdicaoLabel" for="Complemento">Complemento</label>  
					<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="59" maxlength="100" value="<%=Presidiodt.getComplemento()%>" onKeyUp=" autoTab(this,255)"> <br>
					
					<label class="formEdicaoLabel" for="Bairro">*Bairro </label>
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarFaccao" name="imaLocalizarFaccao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('Campos_Presidio','Presidio','Consulta de Bairro', 'Digite o Bairro e clique em consultar.', 'Id_Bairro', 'Bairro', ['Bairro','Cidade','UF'], ['Cidade', 'Uf'], '<%=(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
					<input id='Id_Bairro' name='Id_Bairro' type='hidden' value="<%=Presidiodt.getId_Bairro()%>" />
					<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="80" maxlength="100" value="<%=Presidiodt.getBairro()%>"> <br> 
					
					<label class="formEdicaoLabel" for="Cidade">Cidade</label>
					<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="40" maxlength="60" value="<%=Presidiodt.getCidade()%>"> <br>
					
					<label class="formEdicaoLabel" for="Uf">UF</label> 
					<input class="formEdicaoInputSomenteLeitura" readonly name="Uf" id="Uf"  type="text" size="10" maxlength="10" value="<%=Presidiodt.getUf()%>"> <br>
					
					<label class="formEdicaoLabel" for="Cep">*CEP</label> 
					<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="<%=Presidiodt.getCep()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)"> <br>
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>