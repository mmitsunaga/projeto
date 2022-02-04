<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Assuntodt" scope="session" class= "br.gov.go.tj.projudi.dt.AssuntoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Assunto  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<h2>>> Cadastro de Assunto</h2>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Assunto" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Assunto" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="id_retorno_busca" name="id_retorno_busca" type="hidden" value="" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/AssuntoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Assunto </legend>
					<label class="formEdicaoLabel" for="Id_Assunto">Identificador </label> <input class="formEdicaoInputSomenteLeitura" name="Id_Assunto"  id="Id_Assunto"  type="text"  readonly="true" value="<%=Assuntodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="Assunto">*Descrição </label> <input class="formEdicaoInput" name="Assunto" id="Assunto"  type="text" size="60" maxlength="60" value="<%=Assuntodt.getAssunto()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="AssuntoCodigo">*Código CNJ </label> <input class="formEdicaoInput" name="AssuntoCodigo" id="AssuntoCodigo"  type="text" size="22" maxlength="22" value="<%=Assuntodt.getAssuntoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="Id_AssuntoPai">Assunto Pai <input class="FormEdicaoimgLocalizar" id="imaLocalizarAssunto" name="imaLocalizarAssunto" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); AlterarValue('id_retorno_busca','Id_AssuntoPai')" > 					
					<input class="FormEdicaoimgLocalizar" id="imaLimparAssunto" name="imaLimparAssunto" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AssuntoPai','AssuntoPai'); return false;" ></label> 					
					<input  name='Id_AssuntoPai' id='Id_AssuntoPai' type='hidden'  value='<%=Assuntodt.getId_AssuntoPai()%>'>
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="AssuntoPai" id="AssuntoPai" type="text" size="60" maxlength="255" value="<%=Assuntodt.getAssuntoPai()%>"><br />
					<label class="formEdicaoLabel" for="IsAtivo">Ativo </label> <input type="checkbox" name="IsAtivo" id="IsAtivo" <%=Assuntodt.isAtivo()?"checked":""%> value="1">Sim<br />
					<label class="formEdicaoLabel" for="DispositivoLegal">Dispositivo Legal </label> <input class="formEdicaoInput" name="DispositivoLegal" id="DispositivoLegal"  type="text" size="60" maxlength="250" value="<%=Assuntodt.getDispositivoLegal()%>" onkeyup=" autoTab(this,250)"><br />
					<label class="formEdicaoLabel" for="Artigo">Artigo </label> <input class="formEdicaoInput" name="Artigo" id="Artigo"  type="text" size="60" maxlength="250" value="<%=Assuntodt.getArtigo()%>" onkeyup=" autoTab(this,250)"><br />
					<label class="formEdicaoLabel" for="Sigla">Sigla </label> <input class="formEdicaoInput" name="Sigla" id="Sigla"  type="text" size="50" maxlength="50" value="<%=Assuntodt.getSigla()%>" onkeyup=" autoTab(this,50)"><br />
				</fieldset>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
