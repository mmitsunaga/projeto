<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoObjetoArquivoMovimentacaodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ProcessoObjetoArquivoMovimentacao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script language="javascript" type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ProcessoObjetoArquivoMovimentacao, "O Campo ProcessoObjetoArquivoMovimentacao é obrigatório!")) return false;
				if (SeNulo(DataMovimentacao, "O Campo DataMovimentacao é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de ProcessoObjetoArquivoMovimentacao</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ProcessoObjetoArquivoMovimentacao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ProcessoObjetoArquivoMovimentacao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>					<a class="divPortaBotoesLink" href="Ajuda/ProcessoObjetoArquivoMovimentacaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				</div>
				<fieldset class="formEdicao" id="Campos_"ProcessoObjetoArquivoMovimentacao" > 
					<legend class="formEdicaoLegenda">Edita os dados ProcessoObjetoArquivoMovimentacao </legend>
					<div class='col10'>
						<label for="Id_ProcessoObjetoArquivoMovimentacao">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_ProcessoObjetoArquivoMovimentacao"  id="Id_ProcessoObjetoArquivoMovimentacao"  type="text"  readonly="true" value="<%=ProcessoObjetoArquivoMovimentacaodt.getId()%>">
					</div>
					<div class='col45'>
						<label  for="ProcessoObjetoArquivoMovimentacao">*ProcessoObjetoArquivoMovimentacao</label> <input class="formEdicaoInput" name="ProcessoObjetoArquivoMovimentacao" id="ProcessoObjetoArquivoMovimentacao"  type="text" size="60" maxlength="60" value="<%=ProcessoObjetoArquivoMovimentacaodt.getProcessoObjetoArquivoMovimentacao()%>" onkeyup=" autoTab(this,60)">
					</div>
					<div class='col45'>
						<input id='Id_Processoobjetoarquivo' name='Id_Processoobjetoarquivo' type='hidden' value='<%=ProcessoObjetoArquivoMovimentacaodt.getId_ProcessoObjetoArquivo()%>' />
						<label  for="Id_ProcessoObjetoArquivo">Processoobjetoarquivo
<img id="imaLocalizarProcessoobjetoarquivo" name="imaLocalizarProcessoobjetoarquivo" src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_ProcessoObjetoArquivoMovimentacao', 'Processoobjetoarquivo','Consulta de Processoobjetoarquivo', 'Digite o Processoobjetoarquivo e clique em consultar.', 'Id_Processoobjetoarquivo', 'Processoobjetoarquivo', ['Processoobjetoarquivo'], ['Processoobjetoarquivo'], '<%=( Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 
						</label>
						<input  name="Id_ProcessoObjetoArquivo" id="Id_ProcessoObjetoArquivo" type="hidden"  value="" />
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProcessoObjetoArquivo" id="ProcessoObjetoArquivo" type="text" size="60" maxlength="255" value="<%=ProcessoObjetoArquivoMovimentacaodt.getProcessoObjetoArquivo()%>"><br />
					</div>
					<div class='col45'>
						<label for="DataMovimentacao">*DataMovimentacao</label> <input class="formEdicaoInput" name="DataMovimentacao" id="DataMovimentacao"  type="text" size="7" maxlength="7" value="<%=ProcessoObjetoArquivoMovimentacaodt.getDataMovimentacao()%>" onkeypress="return DigitarHoraResumida(this, event)" onkeyup=" MascararHoraResumida(this); autoTab(this,7)" />
					</div>
					<div class='col45'>
						<label for="DataRetorno">DataRetorno</label> <input class="formEdicaoInput" name="DataRetorno" id="DataRetorno"  type="text" size="7" maxlength="7" value="<%=ProcessoObjetoArquivoMovimentacaodt.getDataRetorno()%>" onkeypress="return DigitarHoraResumida(this, event)" onkeyup=" MascararHoraResumida(this); autoTab(this,7)" />
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
