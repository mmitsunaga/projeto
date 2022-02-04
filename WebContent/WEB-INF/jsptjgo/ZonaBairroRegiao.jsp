<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.RegiaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="zonaBairroRegiaoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt"/>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ZonaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RegiaoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Zona/Bairro/Região  </title>
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
					if (SeNulo(Regiao, "O campo Bairro é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;						
					}
					if (SeNulo(RegiaoCodigo, "O campo Zona é obrigatório!")) {
						AlterarValue('PaginaAtual','-1');
						return false;
					}
					if (SeNulo(RegiaoCodigo, "O campo Região é obrigatório!")) {
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
		<div class="area"><h2>&raquo; Cadastro de Zona/Bairro/Região</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ZonaBairroRegiao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ZonaBairroRegiao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/RegiaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados de Zona/Bairro/Região </legend>
					
					<label class="formEdicaoLabel" for="Id_ZonaBairroRegiao">Identificador</label> <br><input class="formEdicaoInputSomenteLeitura" name="Id_ZonaBairroRegiao"  id="Id_ZonaBairroRegiao"  type="text"  readonly="true" value="<%=zonaBairroRegiaoDt.getId()%>"><br />
					
					<label class="formEdicaoLabel" for="label_Bairro">*Bairro
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > </label> <br>  
		    		<input  name='Id_Bairro' id='Id_Bairro' type='hidden'  value='<%=zonaBairroRegiaoDt.getId_Bairro()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="Bairro" id="Bairro" type="text" size="60" maxlength="60" value="<%=zonaBairroRegiaoDt.getBairro()%>"/><br />			
		    		
		    		<label class="formEdicaoLabel" for="label_Cidade">*Cidade</label>  <br>
		    		<input  name='Id_Cidade' id='Id_Cidade' type='hidden'  value='<%=zonaBairroRegiaoDt.getId_Cidade()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="60" maxlength="60" value="<%=zonaBairroRegiaoDt.getCidade()%>"/><br />
		    		
		    		<label class="formEdicaoLabel" for="label_Zona">*Zona
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarZona" name="imaLocalizarZona" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ZonaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  </label>  <br>
		    		<input  name='Id_Zona' id='Id_Zona' type='hidden'  value='<%=zonaBairroRegiaoDt.getId_Zona()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="Zona" id="Zona" type="text" size="60" maxlength="60" value="<%=zonaBairroRegiaoDt.getZona()%>"/><br />
		    		
		    		<label class="formEdicaoLabel" for="label_Regiao">*Regiao
		    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarRegiao" name="imaLocalizarRegiao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RegiaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > </label>  <br> 
		    		<input  name='Id_Regiao' id='Id_Regiao' type='hidden'  value='<%=zonaBairroRegiaoDt.getId_Regiao()%>'> 
		    		<input class="formEdicaoInputSomenteLeitura" readonly name="Regiao" id="Regiao" type="text" size="60" maxlength="60" value="<%=zonaBairroRegiaoDt.getRegiao()%>"/>
				
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
