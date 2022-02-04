<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<jsp:useBean id="ServidorJudiciariodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>

<html>
<head>
	<title>Servidor Judiciário</title>

    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>		
   	
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>		
</head>

<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
<script language="javascript" type="text/javascript">
    function VerificarCampos() {
    	if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
	       with(document.Formulario) {
				if (SeNulo(Nome, "O Campo Nome é obrigatório!")) return false;
			    if (SeNulo(Sexo, "Campo Sexo é obrigatório!")) return false; 
			    if (SeNulo(DataNascimento, "Campo Data de Nascimento é obrigatório!")) return false; 
			    if (SeNulo(Rg, "Campo Rg é obrigatório!")) return false; 
			    if (SeNulo(Cpf, "Campo Cpf é obrigatório!")) return false; 
			    if (SeNulo(MatriculaTjGo, "Campo Matrícula é obrigatório!")) return false; 
	            submit();
	       	}
       	}
     }
</script>

<%}%>
<body>
	<div id="divCorpo" class="divCorpo"  >
  		<div class="area"><h2>&raquo; Cadastro de Usuário</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		  <form action="ServidorJudiciario" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		  <form action="ServidorJudiciario" method="post" name="Formulario" id="Formulario">
		<%}%>
		  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		  <input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		  <input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">  
		  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
		  <input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
		  
		  <div id="divPortaBotoes" class="divPortaBotoes">
		      <input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
			  <input id="imgsalvar" class="imgsalvar" title="salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('tempFluxo1','vazio')" >  
			  <input id="imgLocalizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('tempFluxo1','vazio')">  
			  <input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      <a class="divPortaBotoesLink" href="Ajuda/UsuarioAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		      <input id="imgDocumentos" class="imgDocumentos" title="Anexar Documentos do Usuário" name="imgDocumentos" type="image"  src="imagens/22x22/btn_anexo.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarAutoPai%>');">
		 	  <%if(ServidorJudiciariodt.getId() != null && !ServidorJudiciariodt.getId().equals("")){%>
			    	<input id="imgLimparSenha" class="imgLimpar" title="Limpar Senha" name="imgLimparSenha" type="image"  src='imagens/22x22/edit-clear.png'  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','L');">
			    	<%if(ServidorJudiciariodt.getAtivo().equalsIgnoreCase("true")) {%>			      		
			      		<input id="imgDesativarUsuario" alt="Bloquear Usuário" title="Bloquear Usuário" name="imgDesativarUsuario" type="image"  src="./imagens/22x22/ico_fechar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','D');">
			    	<%} else if(ServidorJudiciariodt.getAtivo().equalsIgnoreCase("false")) { %>			    		
			    		<input id="imgAtivarUsuario" alt="Desbloquear Usuário" title="Desbloquear Usuário" name="imgAtivarUsuario" type="image"  src="./imagens/22x22/ico_liberar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','A');">
			    	<%}%> 
			  <%} %>
		 
		  </div>
		  
		  <div id="divEditar" class="divEditar">
			<fieldset class="formEdicao"  > 
			    <legend class="formEdicaoLegenda">Cadastro de Servidor Judiciário <%if(ServidorJudiciariodt.getId() != null && !ServidorJudiciariodt.getId().equals("") && ServidorJudiciariodt.getAtivo().equalsIgnoreCase("false")){%> <font color="red" size="-1"><strong>&lt;Bloqueado&gt;</strong></font> <%}%></legend>
			    
			    <%-- Botao 'Visualizar Log' que retorna o log do usuário através da página UsuarioLog.jsp --%>
		  		<% if( ServidorJudiciariodt.getId() != null && !ServidorJudiciariodt.getId().equals("") ) { %>
				<div style="float:right"> 
					<input float="right" type="submit" value="Visualizar Log" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
				</div></br>
		  		<% } %>
			    
				<div class="col45">
				<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
				<input class="formEdicaoInput" name="Nome" id="Nome"  type="text" size="60" maxlength="255" value="<%=ServidorJudiciariodt.getNome()%>" onkeyup=" autoTab(this,255)"/><br />
				</div>
				
				<div class="col20">
  			    <label class="formEdicaoLabel" for="Cpf">*CPF</label><br> 
  			    <input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="20" maxlength="11" value="<%=ServidorJudiciariodt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/>
  			    </div>
  			    
  			    <div class="col30">
  			    <label class="formEdicaoLabel" for="Usuario">Usuário</label><br> 
			    <input class="formEdicaoInputSomenteLeitura" name="Usuario" id="Usuario"  type="text" size="30" maxlength="30" readonly="true" value="<%=ServidorJudiciariodt.getUsuario()%>" onkeyup=" autoTab(this,30)"/><br />
			    </div>
			    
			    <div class="col45 clear">
			   	<label class="formEdicaoLabel" for="Cidade">*Naturalidade
			   	<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			   	</label><br> 
			   	
			   	<input class="formEdicaoInputSomenteLeitura"  readonly name="Cidade" id="Cidade" type="text" size="57" maxlength="255" value="<%=ServidorJudiciariodt.getNaturalidade()%>"/><br />
			   	</div>
			    
			    <div class="col15">
			    <label class="formEdicaoLabel" for="Sexo">*Sexo</label><br> 
			    <select name="Sexo" id="Sexo" class="formEdicaoCombo" >
					<option <%if(ServidorJudiciariodt.getSexo() != null && ServidorJudiciariodt.getSexo().trim().equalsIgnoreCase("F")){%> selected <%}%>>F</option>
					<option <%if(ServidorJudiciariodt.getSexo() != null && ServidorJudiciariodt.getSexo().trim().equalsIgnoreCase("M")){%> selected <%}%>>M</option>				    
				</select>
				</div>
				
				<div class="col25">
				<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label><br> 
				<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=ServidorJudiciariodt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
				<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/><br />
			    </div>
			    
			    <div class="col25 clear">
			    <label class="formEdicaoLabel" for="Rg">*RG</label><br> 
			    <input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="30" value="<%=ServidorJudiciariodt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
			    </div>
			    
			    <div class="col25">
			    <label class="formEdicaoLabel" for="Id_RgOrgaoExpedidor">*Órgão Expedidor
			    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >   
			    </label><br>  
			    
			    <input  class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="Id_RgOrgaoExpedidor" type="text" size="24" maxlength="255" value="<%=ServidorJudiciariodt.getRgOrgaoExpedidor()%>"/>
			    </div>
			    
			    <div class="col25">
			    <label class="formEdicaoLabel" for="RgDataExpedicao">Data de Expedição</label><br> 
			    <input class="formEdicaoInput" name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" value="<%=ServidorJudiciariodt.getRgDataExpedicao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
			    <img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)"><br />
			    </div>
			    
			    <div class="col25 clear">
			    <label class="formEdicaoLabel" for="TituloEleitor">Título de Eleitor</label><br> 
			    <input class="formEdicaoInput" name="TituloEleitor" id="TituloEleitor"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getTituloEleitor()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)">
			    </div>
			    
			    <div class="col25">
			    <label class="formEdicaoLabel" for="TituloEleitorZona">Zona</label><br> 
			    <input class="formEdicaoInput" name="TituloEleitorZona" id="TituloEleitorZona"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getTituloEleitorZona()%>" onkeyup=" autoTab(this,20)">
			    </div>
			    
			    <div class="col25">
			    <label class="formEdicaoLabel" for="TituloEleitorSecao">Seção</label><br> 
			    <input class="formEdicaoInput" name="TituloEleitorSecao" id="TituloEleitorSecao"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getTituloEleitorSecao()%>" onkeyup=" autoTab(this,20)"/><br />
			    </div>
			    
			    <div class="col25 clear">
			    <label class="formEdicaoLabel" for="Ctps">CTPS</label><br> 
			    <input class="formEdicaoInput" name="Ctps" id="Ctps"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getCtps()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)">
			    </div>
			    
			    <div class="col25">
			    <label class="formEdicaoLabel" for="CtpsSerie">CTPS Série</label><br> 
			    <input class="formEdicaoInput" name="CtpsSerie" id="CtpsSerie"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getCtpsSerie()%>" onkeyup=" autoTab(this,20)">
			    </div>
			    
			    <div class="col25">
			    <label class="formEdicaoLabel" for="Pis">PIS</label><br> 
			    <input class="formEdicaoInput" name="Pis" id="Pis"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getPis()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/><br />
			    </div>
			    
			    <div class="col25 clear">
				<label class="formEdicaoLabel" for="Matricula">*Matrícula</label><br> 
				<input class="formEdicaoInput" name="MatriculaTjGo" id="MatriculaTjGo"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getMatriculaTjGo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"//>
				</div>
				
				<div class="col25">
			    <label class="formEdicaoLabel" for="NumeroConciliador">Número Conciliador</label><br> 
			    <input class="formEdicaoInput" name="NumeroConciliador" id="NumeroConciliador"  type="text" size="20" maxlength="20" value="<%=ServidorJudiciariodt.getNumeroConciliador()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
			    </div>
			    
			    
			   	<div class="col40 clear">
				<label class="formEdicaoLabel" for="EMail">E-Mail</label><br> 
				<input class="formEdicaoInput" name="EMail" id="EMail"  type="text" size="45" maxlength="50" value="<%=ServidorJudiciariodt.getEMail()%>" onkeyup=" autoTab(this,50)"/><br />
				</div>
				
				<div class="col20">
			    <label class="formEdicaoLabel" for="Telefone">Telefone Fixo</label><br> 
			    <input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="20" maxlength="30" value="<%=ServidorJudiciariodt.getTelefone()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)">
			    </div>
			    
			    <div class="col20">
			    <label class="formEdicaoLabel" for="Celular">Telefone Celular</label><br> 
			    <input class="formEdicaoInput" name="Celular" id="Celular"  type="text" size="20" maxlength="30" value="<%=ServidorJudiciariodt.getCelular()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)"/><br />
			    </div>
			    
			    <div class="clear space">&nbsp;</div>
			    <fieldset>
			    	<legend class="formEdicaoLegenda">Habilitar Usuário</legend> 
			    	<%if (ServidorJudiciariodt.isSalvo()) { %>
			   			<input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos Serventia e Grupo" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>'); AlterarValue('tempFluxo1','L')" >
			   			<input id="imgsalvar" class="imgsalvar" title="salvar - Salva os dados digitados da Serventia e Grupo" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('tempFluxo1','S')" >
			    	<%}%>
			    	
			    	<div class="clear"></div>
			    	<div class="col35">
			    	<label class="formEdicaoLabel" for="grupo">*Grupo
			    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarGrupo" name="imaLocalizarGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			    	</label><br>  
			    	
			    	<input  class="formEdicaoInputSomenteLeitura" readonly name="grupo" id="grupo" type="text" size="42" maxlength="50" value="<%=ServidorJudiciariodt.getGrupo()%>"/> 
					</div>
					
					<div class="col35">
					<label class="formEdicaoLabel" for="serventia">*Serventia
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					</label><br>  
					
					<input class="formEdicaoInputSomenteLeitura"  readonly name="serventia" id="serventia" type="text" size="50" maxlength="60" value="<%=ServidorJudiciariodt.getServentia()%>"><br />
			    	</div><br>
			    	<%if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("S")){%>
			  			<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
			     		 	<br><div id="divSalvar" class="divSalvar" class="divsalvar">
			          			<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('tempFluxo1','S')" /><br /> 
			           			<div class="divMensagemsalvar">Clique para confirmar a Habilitação </div> 
			     		 	</div>
			 			<%}%>
			  		<%}%>
			    	<%if (ServidorJudiciariodt.getListaUsuarioServentias() != null) { %>
			    	  <%@ include file="ServidorJudiciarioServentiaHabilitar.jspf"%> 
			    	<%}%> 

		    	</fieldset>
		    
		    
		    	<%@ include file="Padroes/CadastroEndereco.jspf"%> 
		</fieldset>
		 <%if (!request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("S")){%>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
		<%}%>
		</div>
  	  </form>
  	<%@ include file="Padroes/Mensagens.jspf" %>
  	</div>
</body>
</html>
