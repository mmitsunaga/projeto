<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt"%>
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PonteiroCejuscDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="PonteiroCejuscdt" scope="session" class= "br.gov.go.tj.projudi.dt.PonteiroCejuscDt"/>
<jsp:useBean id="PonteiroCejuscStatudt" scope="session" class= "br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de PonteiroCejusc  </title>
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
				if (SeNulo(Pais, "O campo Nome é obrigatório!")) return false;
				if (SeNulo(PaisCodigo, "O campo Código é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de PonteiroCejusc</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="PonteiroCejusc" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="PonteiroCejusc" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />  
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de PonteiroCejusc </legend>
					<label class="formEdicaoLabel" for="Id_PonteiroCejusc">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_PonteiroCejusc"  id="Id_PonteiroCejusc"  type="text"  readonly="true" value="<%=PonteiroCejuscdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="">Situação</label><br> <input class="formEdicaoInputSomenteLeitura" name=""  id=""  type="text"  readonly="true" value="<%=PonteiroCejuscdt.getPonteiroCejuscStatus().toString()%>"><br />
					
<!-- 					<label class="formEdicaoLabel" for="Id_PonteiroCejuscStatu">*Situação -->
<%-- 					<input class="FormEdicaoimgLocalizar" id="imaLocalizarPonteiroCejuscStatu" name="imaLocalizarPonteiroCejuscStatu" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(PonteiroCejuscStatuDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > --%>
<%-- 					</label><br>   					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="imaLocalizarPonteiroCejuscStatu" id="imaLocalizarPonteiroCejuscStatu" type="text" size="60" maxlength="60" value="<%=PonteiroCejuscdt.getPonteiroCejuscStatus()%>"/><br /> --%>

					<label class="formEdicaoLabel" for="Id_PonteiroCejuscStatus">O conciliador/mediador compareceu?</label><br>  
						<input type="radio" name="Id_PonteiroCejuscStatus" value="<%= PonteiroCejuscStatudt.REALIZADO %>" <%if(String.valueOf(PonteiroCejuscStatudt.REALIZADO).equals(PonteiroCejuscdt.getId_PonteiroCejuscStatus()) ){%>checked="checked" <%}%> />Sim  
			       		<input type="radio" name="Id_PonteiroCejuscStatus" value="<%= PonteiroCejuscStatudt.FALTOU %>" <%if(String.valueOf(PonteiroCejuscStatudt.FALTOU).equals(PonteiroCejuscdt.getId_PonteiroCejuscStatus())){%>checked="checked" <%}%> />Não
			       		
					<br />
					
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
