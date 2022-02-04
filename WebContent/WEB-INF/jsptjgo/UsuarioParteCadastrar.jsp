<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>

<jsp:useBean id="UsuarioPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioParteDt"/>

<html>
<head>
	<title>Usuario Parte</title>

    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>		
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type="text/javascript" src="./js/DivFlutuante.js" > </script>
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Cadastro de Usuário </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
	    			<legend class="formEdicaoLegenda">Cadastro de Usuário para Parte </legend>

					<br />		    			
	    			<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
	    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=UsuarioPartedt.getNome()%>" onkeyup=" autoTab(this,255)"/><br />
	    			
	    			<label class="formEdicaoLabel" for="Usuario">*Usuário</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" readonly name="Usuario" id="Usuario"  type="text" size="60" maxlength="60" value="<%=UsuarioPartedt.getUsuario()%>" onkeyup=" autoTab(this,60)"/><br />
	    	
	    			<label class="formEdicaoLabel" for="Sexo">*Sexo</label><br> 
	    			<select name="Sexo" id="Sexo" class="formEdicaoCombo" >
		        		<option>F</option>
			        	<option>M</option>
		    	    	<option selected><%=UsuarioPartedt.getSexo()%></option>
		    	    </select>
					<br />
	    			
	    			<label class="formEdicaoLabel" for="DataNascimento">*Data Nascimento</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" readonly name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=UsuarioPartedt.getDataNascimento()%>"> <img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/><br />
	    			
	    			<label class="formEdicaoLabel" for="Id_Naturalidade">*Naturalidade 
	    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','0');" >  
	    			</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura" readonly name="Naturalidade" id="Id_Naturalidade" type="text" size="57" maxlength="255" value="<%=UsuarioPartedt.getNaturalidade()%>"/><br />
	    	
	    			<label class="formEdicaoLabel" for="Rg">*Rg</label><br> 
	    			<input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="30" value="<%=UsuarioPartedt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)"><br />

	    			<label class="formEdicaoLabel" for="Id_RgOrgaoExpedidor">*Orgão Expedidor 
	    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','0');" >   
	    			</label>
	    			<br> 
	    			<%if(request.getAttribute("RgOrgaoExpedidorEstado") != null){ %>
	    			<input class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="Id_RgOrgaoExpedidor" type="text" size="24" maxlength="255" value="<%=UsuarioPartedt.getRgOrgaoExpedidor()+"-"+request.getAttribute("RgOrgaoExpedidorEstado")%>"/>
	    			<% } else {%>
	    			<input class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="Id_RgOrgaoExpedidor" type="text" size="24" maxlength="255" value="<%=UsuarioPartedt.getRgOrgaoExpedidor()%>"/>
	    			<% } %>
	    			<br>
	    			
	    			<label class="formEdicaoLabel" for="RgDataExpedicao">Data Expedição</label><br> 
	    			<input class="formEdicaoInputSomenteLeitura"  readonly name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" value="<%=UsuarioPartedt.getRgDataExpedicao()%>"> 
	    			<img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)"><br />
	    			
	    			<label class="formEdicaoLabel" for="Cpf">*Cpf</label><br> 
	    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="20" maxlength="11" value="<%=UsuarioPartedt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/><br />
	    			
	    			<label class="formEdicaoLabel" for="TituloEleitor">Título Eleitor</label><br> 
	    			<input class="formEdicaoInput" name="TituloEleitor" id="TituloEleitor"  type="text" size="20" maxlength="20" value="<%=UsuarioPartedt.getTituloEleitor()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"><br>
	    			
	    			<label class="formEdicaoLabel" for="TituloEleitorZona">Zona</label><br> 
	    			<input class="formEdicaoInput" name="TituloEleitorZona" id="TituloEleitorZona"  type="text" size="20" maxlength="20" value="<%=UsuarioPartedt.getTituloEleitorZona()%>" onkeyup=" autoTab(this,20)"><br>
	    			
	    			<label class="formEdicaoLabel" for="TituloEleitorSecao">Seção</label><br> 
	    			<input class="formEdicaoInput" name="TituloEleitorSecao" id="TituloEleitorSecao"  type="text" size="20" maxlength="20" value="<%=UsuarioPartedt.getTituloEleitorSecao()%>" onkeyup=" autoTab(this,20)"/><br />
	    			
	    			<label class="formEdicaoLabel" for="Ctps">Ctps</label><br> 
	    			<input class="formEdicaoInput" name="Ctps" id="Ctps"  type="text" size="20" maxlength="20" value="<%=UsuarioPartedt.getCtps()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"><br>
	    			
	    			<label class="formEdicaoLabel" for="CtpsSerie">Ctps Série</label><br> 
	    			<input class="formEdicaoInput" name="CtpsSerie" id="CtpsSerie"  type="text" size="20" maxlength="20" value="<%=UsuarioPartedt.getCtpsSerie()%>" onkeyup=" autoTab(this,20)"><br>
	    			
	    			<label class="formEdicaoLabel" for="Pis">Pis</label><br> 
	    			<input class="formEdicaoInput" name="Pis" id="Pis"  type="text" size="20" maxlength="20" value="<%=UsuarioPartedt.getPis()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"//><br />

					<label class="formEdicaoLabel" for="Telefone">Telefone</label><br> 
	    			<input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="20" maxlength="30" value="<%=UsuarioPartedt.getTelefone()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)"><br>
	    
	    			<label class="formEdicaoLabel" for="Celular">Celular</label><br> 
	    			<input class="formEdicaoInput" name="Celular" id="Celular"  type="text" size="20" maxlength="30" value="<%=UsuarioPartedt.getCelular()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)"/><br />	    			
					
					<label class="formEdicaoLabel" for="EMail">E-Mail</label><br> 
					<input class="formEdicaoInput" name="EMail" id="EMail"  type="text" size="60" maxlength="50" value="<%=UsuarioPartedt.getEMail()%>" onkeyup=" autoTab(this,50)"//><br />
	    		
	    		
	    			
	    			<%@ include file="Padroes/CadastroEndereco.jspf"%>
	    			
	    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','S');AlterarValue('PassoEditar','0');"> 
			    	</div> 
				</fieldset>
		 		<%if (request.getAttribute("Curinga").toString().equalsIgnoreCase("S")){%>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				<%}%>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
