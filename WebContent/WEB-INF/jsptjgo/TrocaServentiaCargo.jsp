<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"%>

<jsp:useBean id="ServentiaCargodt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaCargoDt"/>

<html>
<head>
	<title>Habilitação de Usuário</title>

    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>	
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Troca de Usuário em Cargo</h2></div>
			
			<form action="TrocaServentiaCargo" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="Id_UsuarioServentia" name="Id_UsuarioServentia" type="hidden" />
				<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>" />
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Troca de Responsável pelo Cargo</legend> 
						
						<br />
						<label class="formEdicaoLabel" for="Id_CargoTipo">*Tipo Cargo</label><br>  
						<input  class="formEdicaoInputSomenteLeitura" readonly="true" name="CargoTipo" id="CargoTipo" type="text" size="70" maxlength="100" value="<%=ServentiaCargodt.getCargoTipo()%>"/><br />

						<label class="formEdicaoLabel" for="grupo">*Cargo</label><br>  
						<input class="formEdicaoInputSomenteLeitura"  readonly name="Grupo" id="Grupo" type="text" size="70" maxlength="80" value="<%=ServentiaCargodt.getServentiaCargo()%>"/>
						<br />

						<label class="formEdicaoLabel" for="Id_UsuarioServentiaGrupo">Usuário
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuarioServentiaGrupo" name="imaLocalizarUsuarioServentiaGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						</label><br>  
						<% if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir)))  {%>
						
						<% } %> 					
						<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="UsuarioServentiaGrupo" id="UsuarioServentiaGrupo" type="text" size="66" maxlength="80" value="<%=ServentiaCargodt.getNomeUsuario()%>">
					    <br /><br />
						
						<% if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir)))  {%>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				   			<input type="submit" id="imgsalvar" class="imgsalvar" value="Salvar" title="Salvar Serventia e Grupo" name="imasalvar"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');" >
				   		</div>
				   		<% } %> 					
					    <br />
				    </fieldset>
					    
			    	<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			    </div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>