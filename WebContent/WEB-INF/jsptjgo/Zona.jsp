<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ZonaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<jsp:useBean id="Zonadt" scope="session" class= "br.gov.go.tj.projudi.dt.ZonaDt"/>
<jsp:useBean id="RelatorioZonaHistoricodt" scope="session" class= "br.gov.go.tj.projudi.dt.RelatorioZonaHistoricoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Zona  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			if ($("#PaginaAtual").val() == '<%= String.valueOf(Configuracao.SalvarResultado) %>') {
				with(document.Formulario) {
					if (SeNulo(Zona, "O Campo Zona é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(ZonaCodigo, "O Campo Código Zona é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(Comarca, "O Campo Comarca é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(ValorCivel, "O Campo Valor Cível é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(ValorCriminal, "O Campo Valor Criminal é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(ValorContaVinculada, "O Campo Valor Conta Vinculada é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(ValorSegundoGrau, "O Campo Valor Segundo Grau é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(ValorSegundoGrauContadoria, "O Campo Valor Segundo Grau Contadoria é obrigatório!")) {
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
		<div class="area"><h2>&raquo; Cadastro de Zona</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Zona" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Zona" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ZonaAjuda.html" target="_blank">  
				<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				<input id="imgHistorico" alt="Histórico" class="imgAtualizar" title="Visualiza Histórico das Alterações" name="imgHistorico" type="image"  src="./imagens/historico.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Zona </legend>
					<label class="formEdicaoLabel" for="Id_Zona">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Zona"  id="Id_Zona"  type="text"  readonly="true" value="<%=Zonadt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Zona">*Zona</label><br> <input class="formEdicaoInput" name="Zona" id="Zona"  type="text" size="60" maxlength="60" value="<%=Zonadt.getZona()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="ZonaCodigo">*Código Zona</label><br> <input class="formEdicaoInput" name="ZonaCodigo" id="ZonaCodigo"  type="text" size="11" maxlength="11" value="<%=Zonadt.getZonaCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
					<label class="formEdicaoLabel" for="Id_Comarca">*Comarca
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br>
						
						<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Comarca" id="Comarca" type="text" size="60" maxlength="60" value="<%=Zonadt.getComarca()%>"><br />
					<%if (Zonadt.getId() != null && Zonadt.getId().trim().length() > 0) { %> 
						<label class="formEdicaoLabel" for="DataInicio">*Data Início</label>  <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataInicio" id="DataInicio" type="text" value="<%=Zonadt.getDataInicio()%>"><br />
					<% } %>
					<label class="formEdicaoLabel" for="ValorCivel">*Valor Cível</label><br> <input class="formEdicaoInput" name="ValorCivel" id="ValorCivel"  type="text" size="13" maxlength="13" value="<%=Zonadt.getValorCivel()%>" onkeyup="MascaraValor(this);autoTab(this,13)"><br />
					<label class="formEdicaoLabel" for="ValorCriminal">*Valor Criminal</label><br> <input class="formEdicaoInput" name="ValorCriminal" id="ValorCriminal"  type="text" size="13" maxlength="13" value="<%=Zonadt.getValorCriminal()%>" onkeyup="MascaraValor(this);autoTab(this,13)"><br />
					<label class="formEdicaoLabel" for="ValorContaVinculada">*Valor Conta Vinculada</label><br> <input class="formEdicaoInput" name="ValorContaVinculada" id="ValorContaVinculada"  type="text" size="13" maxlength="13" value="<%=Zonadt.getValorContaVinculada()%>" onkeyup=" MascaraValor(this);autoTab(this,13)"><br />
					<label class="formEdicaoLabel" for="ValorSegundoGrau">*Valor Segundo Grau</label><br> <input class="formEdicaoInput" name="ValorSegundoGrau" id="ValorSegundoGrau"  type="text" size="13" maxlength="13" value="<%=Zonadt.getValorSegundoGrau()%>" onkeyup=" MascaraValor(this);autoTab(this,13)"><br />
					<label class="formEdicaoLabel" for="ValorSegundoGrauContadoria">*Valor 2º Grau Contadoria</label><br> <input class="formEdicaoInput" name="ValorSegundoGrauContadoria" id="ValorSegundoGrauContadoria"  type="text" size="13" maxlength="13" value="<%=Zonadt.getValorSegundoGrauContadoria()%>" onkeyup=" MascaraValor(this);autoTab(this,13)"><br />					
				</fieldset>
				
				<%if (RelatorioZonaHistoricodt != null && RelatorioZonaHistoricodt.possuiHistorico()) {%>
				<fieldset class="formEdicao">	
					<legend>Registros de Históricos de Alterações </legend>			
					<div id="divTextoLog">
						<%=RelatorioZonaHistoricodt.getTextoHistorico() %>							
					</div>
				</fieldset>	
				<% } %>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
