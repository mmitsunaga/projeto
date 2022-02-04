<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoDt"%>

<jsp:useBean id="Cidadedt" scope="session" class= "br.gov.go.tj.projudi.dt.CidadeDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Gerenciamento de Cidades  </title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		
		<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
			<script language="javascript" type="text/javascript">
				function VerificarCampos() {
					if ($("#PaginaAtual").val() == '<%= String.valueOf(Configuracao.SalvarResultado) %>') {
						with(document.Formulario) {
							if (SeNulo(Cidade, "O campo Nome é obrigatório!")) {
								AlterarValue('PaginaAtual','-1');
								return false;
							}
							//if (SeNulo(CidadeCodigo, "O Campo CidadeCodigo é obrigatório!")) return false;
							if (SeNulo(Estado, "O campo UF é obrigatório!")){
								AlterarValue('PaginaAtual','-1');
								return false;
							}
// 							if (Id_Estado.localeCompare('1') == 0 && SeNulo(CodigoSPG, "O campo Código SPG é obrigatório!")) {
// 								AlterarValue('PaginaAtual','-1');
// 								return false;
// 							}
							submit();
						}	
					}					
				}
			</script>
		<%}%>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; Cadastro de Cidade</h2></div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
				<form action="Cidade" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
				<form action="Cidade" method="post" name="Formulario" id="Formulario">
			<%}%>
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
					<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
					<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
					<div id="divPortaBotoes" class="divPortaBotoes">
						<%@ include file="Padroes/Botoes.jspf"%>				
						<a class="divPortaBotoesLink" href="Ajuda/CidadeAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
						<input id="imgImprimir" alt="Imprimir"  class="imgImprimir" title="Imprimir - Gerar relatorio em pdf" name="imaImprimir" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" />
					</div>
			
					<div id="divEditar" class="divEditar"  >
						<fieldset class="formEdicao"  id="Campos_Cidade" > 
							<legend class="formEdicaoLegenda">Edita os dados Cidade </legend>
							<label class="formEdicaoLabel" for="Id_Cidade">Identificador</label><br> 
							<input class="formEdicaoInputSomenteLeitura" name="Id_Cidade"  id="Id_Cidade"  type="text"  readonly="true" value="<%=Cidadedt.getId()%>"/><br />
							<label class="formEdicaoLabel" for="Cidade">*Nome</label><br> 
							<input class="formEdicaoInput" name="Cidade" id="Cidade"  type="text" size="60" maxlength="60" value="<%=Cidadedt.getCidade()%>" onkeyup=" autoTab(this,60)"/><br />
							<label class="formEdicaoLabel" for="CidadeCodigo">Código</label><br> 
							<input class="formEdicaoInput" name="CidadeCodigo" id="CidadeCodigo" type="text" size="9" maxlength="9" value="<%=Cidadedt.getCidadeCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,9)"/><br />
							<label class="formEdicaoLabel" for="Id_Estado">*UF
							<input id="Id_Estado" name="Id_Estado" type="hidden" value="<%=Cidadedt.getId_Estado()%>" />
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarEstado" name="imaLocalizarEstado" type="image"  src="./imagens/imgLocalizarPequena.png" 
											onclick="MostrarBuscaPadrao('Campos_Cidade','Cidade','Consulta de Estado', 'Digite o Estado e clique em consultar.', 'Id_Estado', 'UF', ['Estado'], ['Estado'], '<%=(EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 
							</label><br>  
							<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="UF" id="UF" type="text" size="2" maxlength="2" value="<%=Cidadedt.getUf()%>"/><br />
							<label class="formEdicaoLabel" >*Estado</label><br />
							<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Estado" id="Estado" type="text" size="60" maxlength="60" value="<%=Cidadedt.getEstado()%>"/><br />
							<label class="formEdicaoLabel" for="CodigoSPG">*Código SPG</label><br> 
						    <input class="formEdicaoInput" name="CodigoSPG" id="CodigoSPG"  type="text" size="7" maxlength="7" value="<%=Cidadedt.getCodigoSPG()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,7)"/><br />
						</fieldset>
						<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					</div>
				</form>
		</div>
		<%@include file="Padroes/Mensagens.jspf"%>
	</body>	
</html>