<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>

<jsp:useBean id="Assistentedt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>

<html>
<head>
	<title>Assistente</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>		
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
	  <script type='text/javascript' src='./js/jquery.js'></script>
   	  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>

<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
<script language="javascript" type="text/javascript">
    function VerificarCampos() {
    if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
       with(document.Formulario) {
             if (Usuario.value == "") {
		         alert("Campo Usuario é obrigatório!");
		         Usuario.focus();
		         return false; 
		     }             
             if (Nome.value == "") {
		         alert("Campo Nome é obrigatório!");
		         Nome.focus();
		         return false; 
		     }
             if (Sexo.value == "") {
		         alert("Campo Sexo é obrigatório!");
		         Sexo.focus();
		         return false; 
		     }
             if (DataNascimento.value == "") {
		         alert("Campo Data de Nascimento é obrigatório!");
		         DataNascimento.focus();
		         return false; 
		     }
             if (Rg.value == "") {
		         alert("Campo RG é obrigatório!");
		         Rg.focus();
		         return false; 
		     }
             if (Cpf.value == "") {
		         alert("Campo CPF é obrigatório!");
		         Cpf.focus();
		         return false; 
		     }
             if (DataCadastro.value == "") {
		         alert("Campo Data de Cadastro é obrigatório!");
		         DataCadastro.focus();
		         return false; 
		     }             
		     if (PaginaAtual.value == <%=Configuracao.Curinga7%>){
		     	if (SenhaAtual==""){
		     		alert("Digite a senha atual");
		         	Ativo.focus();
		         	return false;
		     	}
		     	if (SenhaNova==""){
		     		alert("Digite a nova senha");
		         	Ativo.focus();
		         	return false;
		     	}
		     	if (SenhaNovaComparacao==""){
		     		alert("Digite a nova senha");
		         	Ativo.focus();
		         	return false;
		     	}
		     }
              submit();
       }
       }
     }
</script>

<%}%>
<body>
	<div id="divCorpo" class="divCorpo"  >
  		<div class="area"><h2>&raquo; Cadastro de Assessor</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
  			<form action="Assistente" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
  			<form action="Assistente" method="post" name="Formulario" id="Formulario">
		<%}%>
  			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
		  	<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
		  	<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
		  
		  	<div id="divPortaBotoes" class="divPortaBotoes">
		      	<input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
			  	<input id="imgsalvar" class="imgsalvar" title="salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" >
			  	<input id="imgLocalizar" class="imgLocalizar" title="Localizar Assessores Cadastrados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('tempFluxo1','<%="C2"%>')">  
			  	<input id="imgLocalizar" class="imgLocalizar" title="Localizar Usuário para habilitar como assistente" name="imaLocalizar" type="image"  src="./imagens/imgAssistente.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('tempFluxo1','<%="C1"%>')">
			  	<input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">

	      			      
		  	</div>	
			   
			  <div id="divEditar" class="divEditar">
			  <fieldset class="formEdicao"  >
			    <legend class="formEdicaoLegenda">Cadastro de Assessor </legend>
				
				<div class=col60>			    	
			    	<input type="image" alt="Bloqueado atualização do cadastro" title="Bloqueado atualização do cadastro" src="./imagens/22x22/ico_informacao.png">	            	
	            	<strong>A alteração do cadastro de um assistente é bloqueada por motivos de segurança.</strong><br />
	            	<span>Para realizar a edição, entre em contato com o suporte de Gerenciamento de Sistemas.</span><br /><br />
			    </div>
			    
			    <div class="clear"></div>

	            <div class="col60">
	    		  	<%if(Assistentedt.getId() != null && !Assistentedt.getId().equals("") && Assistentedt.isAssessorMagistrado()){%>			    	
		    	    	<%if(Assistentedt.isPodeGuardarParaAssinar()) {%>			      		
			          		<input id="imgBloquearGuardarAssinar" alt="Bloquear Guardar para Assinar" title="Bloquear Guardar para Assinar" name="imgBloquearGuardarAssinar" type="image"  src="./imagens/22x22/ico_liberar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','ASSINARNAO');">
				       		<font color="green" size="-1"><strong>Permitido Guardar para Assinar</strong></font>
				    	<%} else { %>			    		
				    		<input id="imgDesbloquearGuardarAssinar" alt="Permitir Guardar para Assinar" title="Permitir Guardar para Assinar" name="imgPermitirGuardarAssinar" type="image"  src="./imagens/22x22/ico_fechar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','ASSINARSIM');">
				    		<font color="red" size="-1"><strong>Não Permitido Guardar para Assinar</strong></font>
				    	<%}%> 
		  		    <%} %>				   
		  		</div> 
				<div class="col45">
				<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
				<input class="formEdicaoInput" name="Nome" id="Nome"  type="text" size="60" maxlength="255" value="<%=Assistentedt.getNome()%>" onkeyup=" autoTab(this,255)"/>
			    </div>
			    <div class="col45 clear">
			    <label class="formEdicaoLabel" for="Id_Cidade">*Naturalidade
			    <input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
			    </label><br>    <input  class="formEdicaoInputSomenteLeitura"  readonly name="Cidade" id="Id_Cidade" type="text" size="57" maxlength="255" value="<%=Assistentedt.getNaturalidade()%>"/><br />
			    </div>
			    <div class="col15">    
			    <label class="formEdicaoLabel" for="Sexo">*Sexo</label><br> 
			    <select name="Sexo" id="Sexo" class="formEdicaoCombo">
				        	<option>F</option>
					        <option>M</option>
				    	    <option selected><%=Assistentedt.getSexo()%></option>
				</select>
				</div>
				<div class="col25">
				<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label><br> <input class="formEdicaoInputSomenteLeitura"  readonly name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=Assistentedt.getDataNascimento()%>"> <img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/><br />
			    </div>
			    <div class="col30 clear">
			    <label class="formEdicaoLabel" for="Rg">*RG</label><br> <input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="30" value="<%=Assistentedt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
			    </div>
			    <div class="col30">
			    <label class="formEdicaoLabel" for="Id_RgOrgaoExpedidor">*Órgão Expedidor
			    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			    </label><br>   <input  class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="Id_RgOrgaoExpedidor" type="text" size="15" maxlength="255" value="<%=Assistentedt.getRgOrgaoExpedidor()%>"/>			    
			    </div>
			    <div class="col30">
			    <label class="formEdicaoLabel" for="RgDataExpedicao">Data de Expedição</label><br> <input class="formEdicaoInputSomenteLeitura"  readonly name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" value="<%=Assistentedt.getRgDataExpedicao()%>"> <img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)"><br />
			    </div>
			    <div class="col30 clear">
			    <label class="formEdicaoLabel" for="Cpf">*CPF</label><br> <input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="20" maxlength="20" value="<%=Assistentedt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/><br />
			    </div>
			    <div class="col30 clear">
			    <label class="formEdicaoLabel" for="Telefone">Telefone Fixo</label><br> <input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="20" maxlength="30" value="<%=Assistentedt.getTelefone()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)">
			    </div>
			    <div class="col30">
			    <label class="formEdicaoLabel" for="Celular">Telefone Celular</label><br> <input class="formEdicaoInput" name="Celular" id="Celular"  type="text" size="20" maxlength="30" value="<%=Assistentedt.getCelular()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)"/><br />
				</div>
				<div class="col30">
				<label class="formEdicaoLabel" for="EMail">E-mail</label><br> <input class="formEdicaoInput" name="EMail" id="EMail"  type="text" size="50" maxlength="50" value="<%=Assistentedt.getEMail()%>" onkeyup=" autoTab(this,50)"/><br />			    
			    </div>
			    <div class="clear"></div>
			    			    
			    <%@ include file="Padroes/CadastroEndereco.jspf"%> 
			</fieldset>
			<%if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("S") || request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("ASSINARNAO") || request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("ASSINARSIM")){%>
				<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
			     	<div id="divSalvar" class="divSalvar" class="divsalvar">
		          		<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('tempFluxo1','<%=request.getAttribute("tempFluxo1").toString()%>')" /><br />
		          		<%if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("S")) { %> 
			           		<div class="divMensagemsalvar">Clique para Salvar o usuário como assessor </div>
			           	<% } else if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("ASSINARNAO")) { %>
			           		<div class="divMensagemsalvar">Clique para Confirmar a retirada de permissão de guardar para assinar </div>
			           	<% } else if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("ASSINARSIM")) { %>
			           		<div class="divMensagemsalvar">Clique para Confirmar a permissão de guardar para assinar </div> 
			           	<% } %> 
			     	</div>
			 	<%}%>
			<%}%>
			<%if (!(request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("S") || request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("ASSINARNAO") || request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("ASSINARSIM"))){%>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			<%}%>
			</div>
  		</form>
  	</div>
		<%@include file="Padroes/Mensagens.jspf"%>
</body>
</html>
