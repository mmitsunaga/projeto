<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt" %>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt" %>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| <%=request.getAttribute("tempTituloFormulario")%>  </title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<link href="./css/Paginacao.css" type="text/css" rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript'>
	function VerificarCampos(codigoMandado) {
		if (SeNulo(codigoMandado, "O Campo Código do Mandado Judicial é obrigatório!")) {
			return false;
		}
		return true;
	}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| <%=request.getAttribute("tempTituloFormulario")%> </h2></div>
		<div id="divLocalizar" class="divLocalizar" >
			<form id="Formulario" name="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" OnSubmit="JavaScript:return VerificarCampos(Formulario.codigoMandado)">
			
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>"/>
				<input type="hidden" id="_tempBuscaId" name="_tempBuscaId" value="<%=request.getAttribute("_tempBuscaId")%>"/>
				<input type="hidden" id="_tempBuscaDescricao" name="_tempBuscaDescricao" value="<%=request.getAttribute("_tempBuscaDescricao")%>"/>
				<input type="hidden" id="_tempTipoConsulta" name="_tempTipoConsulta" value="<%=request.getAttribute("_tempTipoConsulta")%>"/>
			
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend id="formEdicaoLegenda" class="formLocalizarLegenda"><%=request.getAttribute("tempTituloFormulario")%> </legend>
						
						<label id="formLocalizarCodigoMandadoLabel" for="nrProcessoBusca" class="formLocalizarLabel">*Código Mandado </label><br>
						<input id="codigoMandado" class="formLocalizarInput" name="codigoMandado" type="text" value="<%=request.getAttribute("codigoMandado")%>" onkeypress="return DigitarSoNumero(this, event)" size="50" maxlength="10" />
						<br/>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="formLocalizarBotao" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>');" title="Buscar Mandado">
								<!--  <img src="imagens/22x22/btn_pesquisar.png" alt="Buscar Mandado" /> -->
								Buscar
							</button>
							
							<button type="button" name="operacao" value="Limpar" title="Limpar" onclick="AlterarValue('codigoMandado','');">
								<!-- <img src='imagens/22x22/edit-clear.png' alt="Limpar" /> -->
								Limpar
							</button>
						</div>
					</fieldset>
				</div>
			</form> 
		</div> 
	</div>
	 
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>