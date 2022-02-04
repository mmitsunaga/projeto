<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="Classificadordt" scope="session" class= "br.gov.go.tj.projudi.dt.ClassificadorDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Classificador  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Classificador, "O campo Descrição é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Cadastro de Classificador</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Classificador" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		<form action="Classificador" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ClassificadorAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png"/> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao" id='Campos_Classificador'> 
					<legend class="formEdicaoLegenda">Cadastro de Classificador </legend>
					<label class="formEdicaoLabel" for="Id_Classificador">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Classificador"  id="Id_Classificador"  type="text"  readonly="true" value="<%=Classificadordt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="Classificador">*Descrição</label><br> <input class="formEdicaoInput" name="Classificador" id="Classificador"  type="text" size="60" maxlength="60" value="<%=Classificadordt.getClassificador()%>" onkeyup=" autoTab(this,60)"/><br />
					
					<% if (request.getAttribute("podeSelecionarServentia") != null && (Funcoes.StringToBoolean(request.getAttribute("podeSelecionarServentia").toString()) == true)){ %>
						<label class="formEdicaoLabel" for="Id_Serventia">*Serventia
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  						
						onclick="MostrarBuscaPadrao('Campos_Classificador','Classificador','Consulta de Serventia', 'Digite a Serventia e clique em consultar.', 'Id_Serventia', 'Serventia', ['Serventia'], ['Uf'], '<%=(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
						<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%=Classificadordt.getId_Serventia()%>" />
						</label><br>  
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=Classificadordt.getServentia()%>"/><br />
						
					<% } %>
					<label class="formEdicaoLabel" for="Prioridade">*Prioridade</label><br> <input class="formEdicaoInput" name="Prioridade" id="Prioridade"  type="text" size="11" maxlength="11" value="<%=Classificadordt.getPrioridade()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
