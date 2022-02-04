<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes" %>
<jsp:useBean id="CejuscDisponibilidadedt" scope="session" class= "br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioCejuscDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Disponibilidade CEJUSC </title>
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
				if (SeNulo(Nome, "O Campo Usuário CEJUSC é obrigatório!")) return false;
				if (SeNulo(AudiTipo, "O Campo Tipo de Audiência é obrigatório!")) return false;
				if (SeNulo(Serv, "O Campo Serventia é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Disponibilidade CEJUSC </h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="CejuscDisponibilidade" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="CejuscDisponibilidade" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os Dados de Disponibilidade Cejusc </legend>
					<label class="formEdicaoLabel" for="Id_CejuscDisponibilidade">Identificador</label><br />
					<input class="formEdicaoInputSomenteLeitura" name="Id_CejuscDisponibilidade"  id="Id_CejuscDisponibilidade"  type="text"  readonly="true" value="<%=CejuscDisponibilidadedt.getId()%>"><br />
					
					<!-- Usuário Cejusc -->
					<label class="formLocalizarLabel">*Usuário CEJUSC	
					<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeConsultaUsuarioCejusc")))) { %>														
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuariocejusc" name="imaLocalizarUsuariocejusc" readonly type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioCejuscDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
					<% } %>  
					</label>
					<br />					
					<input class="formEdicaoInputSomenteLeitura" name="Nome" id="Nome" type="text" size="70" maxlength="255" value="<%=CejuscDisponibilidadedt.getNome()%>" />
					<input type="hidden" id="Id_UsuCejusc" name="Id_UsuCejusc" value="" />
					<br />
					
					<label class="formLocalizarLabel">Situação	
					</label>
					<br>					
					<input class="formEdicaoInputSomenteLeitura" type="text" size="70" maxlength="255" 
					
					<%if( request.getSession().getAttribute("situacao") != null && request.getSession().getAttribute("situacao").equals("NÃO APROVADO") ){ %>
					
						style="color:red"
					
					<%}%>
					
					value="<%=request.getSession().getAttribute("situacao")!=null ? request.getSession().getAttribute("situacao") : ""%>" />
					<br />
					
					<label class="formLocalizarLabel">E-mail	
					</label>
					<br>					
					<input class="formEdicaoInputSomenteLeitura" type="text" size="70" maxlength="255" value="<%=request.getSession().getAttribute("email")%>" />
					<br />
					
					<label class="formLocalizarLabel">Telefone 1	
					</label>
					<br>					
					<input class="formEdicaoInputSomenteLeitura" type="text" size="70" maxlength="255" value="<%=request.getSession().getAttribute("telefone")%>" />
					<br />
					
					<label class="formLocalizarLabel">Telefone 2	
					</label>
					<br>					
					<input class="formEdicaoInputSomenteLeitura" type="text" size="70" maxlength="255" value="<%=request.getSession().getAttribute("celular")%>" />
					<br />
					
					<!-- Audiência tipo -->
					<label class="formLocalizarLabel">*Tipo da Audiência										
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaTipo" name="imaLocalizarAudienciaTipo" readonly type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
					</label>
					<br>					
					<input class="formEdicaoInputSomenteLeitura" name="AudienciaTipo" id="AudienciaTipo" type="text" size="70" maxlength="100" value="<%=CejuscDisponibilidadedt.getAudiTipo()%>" />
					<input type="hidden" id="Id_AudienciaTipo" name="Id_AudienciaTipo" value="<%=CejuscDisponibilidadedt.getId_AudiTipo()%>" />
					<br />
					
					<!-- Serventia -->
					<label class="formEdicaoLabel" for="Id_Serventia">*Serventia
				    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					</label>
					<br>				    
				    <input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=CejuscDisponibilidadedt.getId_Serv()%>">
				    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="70" maxlength="100" value="<%=CejuscDisponibilidadedt.getServ()%>"/>
				    <br />
					
					<label class="formEdicaoLabel" for="Domingo">*Domingo</label><br> 
				    <select name="Domingo" id="Domingo" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isDomingoNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isDomingoMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isDomingoVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isDomingoMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>
					<br />
					
					<label class="formEdicaoLabel" for="Segunda">*Segunda</label><br> 
				    <select name="Segunda" id="Segunda" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isSegundaNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isSegundaMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isSegundaVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isSegundaMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>
					<br />
					
					<label class="formEdicaoLabel" for="Terca">*Terça</label><br> 
				    <select name="Terca" id="Terca" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isTercaNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isTercaMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isTercaVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isTercaMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>
					<br />
					
					<label class="formEdicaoLabel" for="Quarta">*Quarta</label><br> 
				    <select name="Quarta" id="Quarta" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isQuartaNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isQuartaMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isQuartaVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isQuartaMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>
					<br />
					
					<label class="formEdicaoLabel" for="Quinta">*Quinta</label><br> 
				    <select name="Quinta" id="Quinta" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isQuintaNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isQuintaMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isQuintaVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isQuintaMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>
					<br />
					
					<label class="formEdicaoLabel" for="Sexta">*Sexta</label><br> 
				    <select name="Sexta" id="Sexta" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isSextaNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isSextaMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isSextaVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isSextaMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>
					<br />
					
					<label class="formEdicaoLabel" for="Sabado">*Sábado</label><br> 
				    <select name="Sabado" id="Sabado" class="formEdicaoCombo">
				    	<option value="-1" <%=(CejuscDisponibilidadedt.isSabadoNenhum()?"selected":"")%>>--Nenhum-- </option>
				    	<option value="7" <%=(CejuscDisponibilidadedt.isSabadoMatutino()?"selected":"")%>>Matutino </option>
				    	<option value="8" <%=(CejuscDisponibilidadedt.isSabadoVespertino()?"selected":"")%>>Vespertino </option>
				    	<option value="9" <%=(CejuscDisponibilidadedt.isSabadoMatutinoEVespertino()?"selected":"")%>>Matutino e Vespertino </option>				    	
					</select/>					
															
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>

<script type="text/javascript">
	$(document).ready(function() {
		$('#imgexcluir').hide();
		<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeConsultaUsuarioCejusc")))) { %>
		$('#imgNovo').hide();
		$('#imgsalvar').hide();		
		<% } %>
	});
	</script>
</html>
