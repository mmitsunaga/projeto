<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.BancoDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Gerência de Arquivo do Banco  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Gerência de Arquivo do Banco </h2></div>
		
		<form action="GerenciaArquivoBanco" method="post" name="Formulario" id="Formulario" enctype="multipart/form-data">
		
			<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input type="hidden" id="idBanco" name="idBanco" value="<%=request.getAttribute("idBanco")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao" >
					<legend class="formEdicaoLegenda">Gerência Arquivo do Banco</legend>
					
					<label class="formEdicaoLabel" for="Id_Banco">*Banco
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarPais" name="imaLocalizarPais" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
					</label><br>
					
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="Banco" id="Banco" type="text" size="60" maxlength="60" value="<%= request.getAttribute("Banco") %>" />
					<br />
					
					<label class="formEdicaoLabel" for="arquivo">Arquivo</label><br>
					<input type="file" name="arquivo" id="filename" size="50" readonly="true"/>
					<br />
				</fieldset>
			</div>
			
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<input type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6 %>')"/>
				<input type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Limpar %>')"/>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
