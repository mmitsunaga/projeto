<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>

<jsp:useBean id="Bairrodt" scope="session" class= "br.gov.go.tj.projudi.dt.BairroDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Gerenciamento de Bairros  </title>
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
							if (SeNulo(Bairro, "O campo Descrição é obrigatório!")){
								AlterarValue('PaginaAtual','-1');
								return false;
							}
							if (SeNulo(Cidade, "O campo Cidade é obrigatório!")) {
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
			<div class="area"><h2>&raquo; Cadastro de Bairro</h2></div>
				<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
					<form action="Bairro" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
				<%} else {%>
					<form action="Bairro" method="post" name="Formulario" id="Formulario">
				<%}%>
						<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
						<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
						<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
						<div id="divPortaBotoes" class="divPortaBotoes">
							<%@ include file="Padroes/Botoes.jspf"%>				
							<a class="divPortaBotoesLink" href="Ajuda/BairroAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png"/> </a>
						</div>
						<div id="divEditar" class="divEditar">
							<fieldset class="formEdicao"  id="Campos_Bairro" > 
								<legend class="formEdicaoLegenda">Edita os dados de Bairro </legend>
								<label class="formEdicaoLabel" for="Id_Bairro">Identificador</label><br> 
								<input class="formEdicaoInputSomenteLeitura" name="Id_Bairro"  id="Id_Bairro"  type="text"  readonly="true" value="<%=Bairrodt.getId()%>"/><br />
								<label class="formEdicaoLabel" for="Bairro">*Descrição</label><br> 
								<input class="formEdicaoInput" name="Bairro" id="Bairro"  type="text" size="60" maxlength="60" value="<%=Bairrodt.getBairro()%>" onkeyup=" autoTab(this,60)"/><br />
								<label class="formEdicaoLabel" for="BairroCodigo">Código</label><br>								 
								<input class="formEdicaoInput" name="BairroCodigo" id="BairroCodigo"  type="text" size="11" maxlength="11" value="<%=Bairrodt.getBairroCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
								<input id="Id_Cidade" name="Id_Cidade" type="hidden" value="<%=Bairrodt.getId_Cidade()%>" />
								<label class="formEdicaoLabel" for="Id_Cidade">*Cidade
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarCidade" name="imaLocalizarCidade" type="image"  src="./imagens/imgLocalizarPequena.png"  								
								onclick="MostrarBuscaPadrao('Campos_Bairro','Bairro','Consulta de Cidade', 'Digite a Cidade e UF e clique em consultar.', 'Id_Cidade', 'Cidade', ['Cidade','UF'], ['Uf'], '<%=(CidadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 		
								</label> <br>
								<input  class="formEdicaoInputSomenteLeitura"  readonly name="Cidade" id="Cidade" type="text" size="60" maxlength="60" value="<%=Bairrodt.getCidade()%>"/><br />
								<label class="formEdicaoLabel" >Uf</label> <br>
								<input class="formEdicaoInputSomenteLeitura"  readonly name="Uf" id="Uf" type="text" size="2" maxlength="2" value="<%=Bairrodt.getUf()%>"/><br />
								<label class="formEdicaoLabel" for="CodigoSPG">*Código SPG</label><br> 
								<input class="formEdicaoInput" name="CodigoSPG" id="CodigoSPG"  type="text" size="11" maxlength="11" value="<%=Bairrodt.getCodigoSPG()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,10)"/> (Obrigatório somente para bairro de cidades do estado de Goiás. Informar o código da cidade seguido do código do bairro com 5 posições. Ex: 796300005 [Goiânia: 7963 - Setor Oeste: 00005]) <br />
								<label class="formEdicaoLabel" for="Zona">Zona</label> <br>
								<input class="formEdicaoInputSomenteLeitura" name="Zona"  id="Zona"  type="text"  readonly="true" value="<%=Bairrodt.getZona()%>"/> (Para alterar acesse a funcionalidade Zona/Bairro/Região)<br /> 
								<label class="formEdicaoLabel" for="Regiao">Regiâo</label> <br>
								<input class="formEdicaoInputSomenteLeitura" name="Regiao"  id="Regiao"  type="text"  readonly="true" value="<%=Bairrodt.getRegiao()%>"/>(Para alterar acesse a funcionalidade Zona/Bairro/Região) <br /> 							
							</fieldset>
							<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
						</div>
					</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>