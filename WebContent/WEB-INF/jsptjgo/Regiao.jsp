<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.RegiaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="regiaoDt" scope="session" class= "br.gov.go.tj.projudi.dt.RegiaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Região  </title>
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
			if ($("#PaginaAtual").val() == '<%= String.valueOf(Configuracao.SalvarResultado) %>') {
				with(document.Formulario) {
					if (SeNulo(Regiao, "O campo Região é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;						
					}
					if (SeNulo(RegiaoCodigo, "O campo Código é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(RegiaoCodigo, "O campo Comarca é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					submit();
				}	
			}			
		}
		</script>
	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Região</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Regiao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Regiao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/RegiaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Região </legend>
					<label class="formEdicaoLabel" for="Id_Regiao">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Regiao"  id="Id_Regiao"  type="text"  readonly="true" value="<%=regiaoDt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Regiao">*Região</label><br> <input class="formEdicaoInput" name="Regiao" id="Regiao"  type="text" size="60" maxlength="60" value="<%=regiaoDt.getRegiao()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="RegiaoCodigo">*Código</label><br> <input class="formEdicaoInput" name="RegiaoCodigo" id="RegiaoCodigo"  type="text" size="11" maxlength="11" value="<%=regiaoDt.getRegiaoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					
					<label class="formEdicaoLabel" for="label_Comarca">*Comarca 
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
		    		</label> <br>
		    		<input  name='Id_Comarca' id='Id_Comarca' type='hidden'  value='<%=regiaoDt.getId_Comarca()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="Comarca" id="Comarca" type="text" size="60" maxlength="60" value="<%=regiaoDt.getComarca()%>"/>			
				
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
