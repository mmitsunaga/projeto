<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.AdvogadoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="Advogadodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>
<jsp:useBean id="UsuarioServentiaOabdt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt"/>

<html>

<head>
	<title>Advogados</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>		
</head>

<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
<script language="javascript" type="text/javascript">
    function VerificarCampos() {
    	if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
       		with(document.Formulario) {
		        if (SeNulo(Nome, "O Campo Nome é obrigatório!")) return false;   
		        
	        	if (SeNulo(Sexo, "O Campo Sexo é obrigatório!")) return false;
		        if (SeNulo(DataNascimento, "O Campo Data de Nascimento é obrigatório!")) return false;
		        if (SeNulo(Rg, "O Campo Rg é obrigatório!")) return false;
		       	if (SeNulo(Cpf,"O Campo Cpf é obrigatório!")) return false;
              	submit();
       		}
       	}
	}
</script>
<%}%>
<body>
	<div id="divCorpo" class="divCorpo">
  		<div class="area"><h2>&raquo; Cadastro de Advogado/Def. Público </h2></div>
		
		<form action="Advogado" method="post" name="Formulario" id="Formulario" <% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>OnSubmit="JavaScript:return VerificarCampos()"<%}%>>
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			  
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgsalvar" class="imgsalvar" title="salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" >  
				<input id="imgLocalizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('Curinga','<%="vazio"%>')">  
				<input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
			    <a class="divPortaBotoesLink" href="Ajuda/UsuarioAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
			    <input id="imgDocumentos" class="imgDocumentos" title="Anexar Documentos do Usuário" name="imgDocumentos" type="image"  src="imagens/22x22/btn_anexo.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarAutoPai%>');">
			    <%if(Advogadodt.getId() != null && !Advogadodt.getId().equals("")){%>
			    	<input id="imgLimparSenha" class="imgLimpar" title="Limpar Senha" name="imgLimparSenha" type="image"  src='imagens/22x22/edit-clear.png'  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','L');"> 
			    <%} %>
			</div>
			  
		    <div id="divEditar" class="divEditar">
		  		<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Cadastro de Advogado/ Def. Público</legend>
			    	
			    	<%-- Botao 'Visualizar Log' que retorna o log do usuário através da página AdvogadoLog.jsp --%>
			  		<% if( Advogadodt.getId() != null && !Advogadodt.getId().equals("") ) { %>
					<div style="float:right"> 
					<input float="right" type="submit" value="Visualizar Log" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" />
					</div></br>
			  		<% } %>	
			    	
			    	<div class="col45">
			    	<label class="formEdicaoLabel" for="Nome">*Nome</label><br> 
			    	<input class="formEdicaoInput" name="Nome" id="Nome"  type="text" size="60" maxlength="255" value="<%=Advogadodt.getNome()%>" onkeyup=" autoTab(this,255)"/><br />
			    	</div>
	
					<div class="col15">
					<label class="formEdicaoLabel" for="Cpf">*CPF</label><br> 
					<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="20" maxlength="20" value="<%=Advogadodt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/>
			    	</div>
			    	
			    	<div class="col30 clear">
			    	<label class="formEdicaoLabel" for="Usuario">Usuário</label><br> 
			    	<input name="Usuario" id="Usuario" class="formEdicaoInputSomenteLeitura" type="text" size="30" maxlength="30" readonly="true" value="<%=Advogadodt.getUsuario()%>" onkeyup=" autoTab(this,30)"/><br />
			    	</div>
			    	<div class="col15">
			    	<label class="formEdicaoLabel" for="Sexo">*Sexo</label><br> 
			    	<select name="Sexo" id="Sexo" class="formEdicaoCombo">
				        	<option>F</option>
					        <option>M</option>
				    	    <option selected><%=Advogadodt.getSexo()%></option>
				  	</select>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento</label><br> 
					<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=Advogadodt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
					<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/><br />
					</div>
					
					<div class="col100 clear">
			    	<label class="formEdicaoLabel" for="Cidade">*Naturalidade
			    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			    	</label><br>  
			    	<input  class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="60" maxlength="255" value="<%=Advogadodt.getNaturalidade()%>"/>
			    	
			    	
			    	</div>
					
					<div class="col25 clear">
			    	<label class="formEdicaoLabel" for="Rg">*RG</label><br> 
			    	<input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="20" value="<%=Advogadodt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
			    	</div>
			    	
			    	
			    	
			    	<div class="col25">
			    	<label class="formEdicaoLabel" for="RgOrgaoExpedidor">*Órgão Expedidor
			    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
			    	</label><br>    
			    	<input class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="RgOrgaoExpedidor" type="text" size="18" maxlength="255" value="<%=Advogadodt.getRgOrgaoExpedidor()%>"/>
			    	
			    	</div>
			    	
			    	<div class="col25">
			    	<label class="formEdicaoLabel" for="RgDataExpedicao">Data de Expedição</label><br> 
			    	<input class="formEdicaoInput" name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" value="<%=Advogadodt.getRgDataExpedicao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
			    	<img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)"><br />
			    	</div>
			    	
			    			    	
			    	<div class="col25 clear">
			    	<label class="formEdicaoLabel" for="Telefone">Telefone Fixo</label><br> 
			    	<input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="20" maxlength="30" value="<%=Advogadodt.getTelefone()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)">
			    	</div>
			    	
			    	<div class="col25">
			    	<label class="formEdicaoLabel" for="Celular">Telefone Celular</label><br> 
			    	<input class="formEdicaoInput" name="Celular" id="Celular"  type="text" size="20" maxlength="30" value="<%=Advogadodt.getCelular()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,30)"/><br />
			    	</div>
			    	
			    	<div class="col45">
			    	<label class="formEdicaoLabel" for="EMail">E-Mail</label><br> 
			    	<input class="formEdicaoInput" name="EMail" id="EMail"  type="text" size="45" maxlength="50" value="<%=Advogadodt.getEMail()%>" onkeyup=" autoTab(this,50)"/><br />
			    	</div>
			    	
			    	<div class="clear"></div>
			    	
			    	<!-- OAB's do Advogado -->
			    	<fieldset>
			    	
			    	<legend class="formEdicaoLegenda">Serventia/OAB</legend>
			    	
			    	
			    		<%if (Advogadodt.getListaUsuarioServentias() != null) { %>
			   			<input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da OAB" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>'); AlterarValue('Curinga','<%="L"%>')" >
			   			<input id="imgsalvar" class="imgsalvar" title="salvar - Salva os dados digitados da OAB" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('Curinga','<%="S"%>')" >
			    		<%}%>
			    	 
			    		<div class="clear"></div>
			    		
			    		<div class="col15">
			    		<label class="formEdicaoLabel" for="OabNumero">*Número</label><br> <input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="<%=UsuarioServentiaOabdt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
			   			</div>
			   			
			   			<div class="col15">
			   			<label class="formEdicaoLabel" for="OabComplemento">*Complemento</label><br> 
			   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
				        		<option>N</option>
					       		<option>A</option>
					        	<option>B</option>
			   		        	<option>S</option>
				    	    	<option selected><%=UsuarioServentiaOabdt.getOabComplemento()%></option/>
				    	</select>
				    	</div>
				    	<div class="col45">
				    	<label class="formEdicaoLabel" for="Oab">*Serventia
				    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarOab" name="imaLocalizarOab" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
				    	</label><br>    <input  class="formEdicaoInputSomenteLeitura"  readonly name="Oab" id="Oab" type="text" size="40" maxlength="60" value="<%=Advogadodt.getServentia()%>">
			    		</div>
			    		<div class="clear"></div>
			    		<%if (Advogadodt.getListaUsuarioServentias() != null) { %>
			    	  		<div>
								<input type="hidden" id="Id_UsuarioServentia" name="Id_UsuarioServentia">
								<div class="clear"></div>
					    		<%if (request.getAttribute("Curinga").toString().equalsIgnoreCase("S")){%>
					  				<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
					     		 	<div id="divSalvar" class="divSalvar" class="divsalvar">
					          			<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('Curinga','<%="S"%>')" /><br /> 
					           			<div class="divMensagemsalvar">Clique para confirmar os dados </div> 
					     		 	</div>
					 				<%}%>
					  			<%}%>
							   	<div id="divTabela" class="divTabela" > 
							    	<table id="Tabela" class="Tabela">
							        	<thead>
							            	<tr class="TituloColuna">
							                	<td></td>
							                  	<td>Serventia/OAB</td>                
							                  	<td>OAB</td>
							                  	<td>Status</td>
							                  	<td class="colunaMinima"></td>
							                  	<td class="colunaMinima">Editar</td>
							               	</tr>
							           	</thead>
							           	<tbody id="tabListaUsuario">
										<%
								  		List liTemp = Advogadodt.getListaUsuarioServentias();
							  			UsuarioDt objTemp;
							  			String stTempNome="";
							  			for(int i = 0 ; i< liTemp.size();i++) {
							      			objTemp = (UsuarioDt)liTemp.get(i); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                   <tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                   <tr class="TabelaLinha2">
											<%}%>
							                	<td> <%=i+1%></td>
							                   	
							                  	<td><%= objTemp.getServentia()+"-"+objTemp.getServentiaUf()%></td>               
							                   	<td><%= objTemp.getUsuarioServentiaOab().getOabNumero()+"-"+objTemp.getUsuarioServentiaOab().getOabComplemento()%></td>
							                   	<%if (objTemp.getUsuarioServentiaAtivo().equalsIgnoreCase("true")){ %>
							                   	<td>ATIVO</td>
							                   	<%} else {%>
							                   	<td> <font color="red" size="-1"><strong>INATIVO</strong></font></td>
							                   	<%}%>                    
							                   	<%
							      				if(objTemp.getUsuarioServentiaAtivo().equalsIgnoreCase("true")) {%>
							      				<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image"  title="Desabilitar Advogado Serventia(OAB)" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Usuario','<%=objTemp.getUsuario()%>');AlterarValue('Id_Usuario','');AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>');AlterarValue('Curinga','F')"> </td>
							     				<%} else { %>
							     				<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Habilitar Advogado  Serventia(OAB)" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Usuario','<%=objTemp.getUsuario()%>'); AlterarValue('Id_Usuario','');AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>');AlterarValue('Curinga','G')"> </td>
							     				<%}%>
							     				<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('Id_Usuario',''); AlterarValue('Usuario','<%=objTemp.getUsuario()%>'); AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>')">     </td>
							              	</tr>
										<%}%>
							           </tbody>
							       </table>							       
							   </div> 							   							   
							</div>
			    		<%}%> 
			    	</fieldset>
			    <%@include file="Padroes/CadastroEndereco.jspf" %> 
				</fieldset>
			    <%if (!request.getAttribute("Curinga").toString().equalsIgnoreCase("S")){%>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			    <%}%>
			    </div>
  		</form>
  		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
