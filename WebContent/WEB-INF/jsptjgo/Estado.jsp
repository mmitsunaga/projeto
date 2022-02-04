<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.EstadoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<jsp:useBean id="Estadodt" scope="session" class= "br.gov.go.tj.projudi.dt.EstadoDt"/>




<%@page import="br.gov.go.tj.projudi.dt.PaisDt"%>
<%-----%>
<%--<jsp:setProperty name="objPais" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Estado  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Estado, "O campo Nome é obrigatório!")) return false;
				if (SeNulo(EstadoCodigo, "O campo Código é obrigatório!")) return false;
				if (SeNulo(Pais, "O campo País é obrigatório!")) return false;
				if (SeNulo(Uf, "O campo UF é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Estado</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Estado" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Estado" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/EstadoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao" id='CamposEstado' > 
					<legend class="formEdicaoLegenda">Edita os dados de Estado </legend>
					<label class="formEdicaoLabel" for="Id_Estado">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Estado"  id="Id_Estado"  type="text"  readonly="true" value="<%=Estadodt.getId()%>"/><br />
					<label class="formEdicaoLabel" for="Estado">*Nome</label><br> <input class="formEdicaoInput" name="Estado" id="Estado"  type="text" size="60" maxlength="60" value="<%=Estadodt.getEstado()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="EstadoCodigo">*Código</label><br> <input class="formEdicaoInput" name="EstadoCodigo" id="EstadoCodigo"  type="text" size="9" maxlength="9" value="<%=Estadodt.getEstadoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,9)"/><br />
					<label class="formEdicaoLabel" for="Id_Pais">*País
					<input id="Id_Pais" name="Id_Pais" type="hidden" value="<%=Estadodt.getId_Pais()%>" />
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarPais" name="imaLocalizarPais" type="image"  src="./imagens/imgLocalizarPequena.png"
					onclick="MostrarBuscaPadrao('CamposEstado','Estado','Consulta de Pais', 'Digite o Pais e clique em consultar.', 'Id_Pais', 'Pais', ['Pais'], [], '<%=(PaisDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >  					
					</label><br>   					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Pais" id="Pais" type="text" size="60" maxlength="60" value="<%=Estadodt.getPais()%>"/><br />
					<label class="formEdicaoLabel" for="Uf">*UF</label><br> <input class="formEdicaoInput" name="Uf" id="Uf"  type="text" size="2" maxlength="2" value="<%=Estadodt.getUf()%>" onkeyup=" autoTab(this,2)"/><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
