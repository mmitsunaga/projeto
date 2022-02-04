<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.EscalaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.EscalaDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.ZonaDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="escalaDt" scope="session" class= "br.gov.go.tj.projudi.dt.EscalaDt"/>
<jsp:useBean id="serventiaCargoEscalaStatusDt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RegiaoDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Cadastro de Escala  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>

 
   
	<script language="javascript" type="text/javascript">

	var validar;
	function validaSubmit(opcao) {
		validar = opcao;
	}
	function VerificarCampos() {
		if( validar ) {
			with(document.Formulario) {
				if($('input[name=isEscalaEspecial]:checked').val() == '0'){
					if (SeNulo(Escala, "O campo Descrição é obrigatório!")) return false;
					if (SeNulo(Area, "O campo Área é obrigatório!")) return false;
					if (SeNulo(MandadoTipo, "O campo Tipo de Mandado é obrigatório!")) return false;
					if (SeNulo(Regiao, "O campo Região é obrigatório!")) return false;
					if (SeNulo(QuantidadeMandado, "O campo Quantidade de Mandados é obrigatório!")) return false;
				}
				submit();
			}
		}
	}
	</script>
 
	   
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Escala</h2></div>
			
			<form action="Escala" method="post" name="Formulario" id="Formulario">      <!--    OnSubmit="JavaScript:return VerificarCampos()">   -->  
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			
			<input type="hidden" id="Fluxo" name="Fluxo" value="<%=request.getAttribute("Fluxo")%>" />
			<input type="hidden" id="Id_EscalaTipo" name="Id_EscalaTipo" value="<%=escalaDt.getId_EscalaTipo()%>" />
			<input type="hidden" id="Id_MandadoTipo" name="Id_MandadoTipo" value="<%=escalaDt.getId_MandadoTipo()%>" />
			<input type="hidden" id="Id_Regiao" name="Id_Regiao" value="<%=escalaDt.getId_Regiao()%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="validaSubmit(false);AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>');" />
				<input id="imgSalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="validaSubmit(true);AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');" />  
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="validaSubmit(false);AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>');" />
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  >
					<legend class="formEdicaoLegenda">Edita os dados Escala</legend>
					
					<label class="formEdicaoLabel" for="Id_Escala">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_Escala"  id="Id_Escala"  type="text"  readonly="true" value="<%=escalaDt.getId()%>"/>
					<br />
				    
				    <% if (escalaDt == null || escalaDt.getId() == null || escalaDt.getId().isEmpty()) { %>
				    <label class="formEdicaoLabel" for="Escala">*Escala especial?</label><br>
					<input type="radio" name="isEscalaEspecial" value="0" <%= escalaDt.isTipoEspecialNormal() || (!escalaDt.isPlantao() && !escalaDt.isAdhoc()) ? "checked" : "" %>> Normal  &nbsp;
  					<input type="radio" name="isEscalaEspecial" value="1" <%= escalaDt.isPlantao() ? "checked" : "" %>> Plantão &nbsp;
  					<input type="radio" name="isEscalaEspecial" value="2" <%= escalaDt.isAdhoc()   ? "checked" : "" %>> Ad Hoc <br>
  					<%} %>
  				
  					<div id="divMudarVisualizacao" <%= escalaDt.isPlantao() || escalaDt.isAdhoc() ? "Style='display:none'" : "" %>>
					
						
						<% if (!escalaDt.getEscala().equalsIgnoreCase("")) { %>	 
						<label class="formEdicaoLabel" for="Escala">*Descrição</label><br>
						<input class="formEdicaoInput" readonly = true name="Escala" id="Escala"  type="text" size="80" maxlength="200" value="<%=escalaDt.getEscala()%>" onkeyup=" autoTab(this,60)"/>
						<br />
					    <%}%>
						
						<label class="formEdicaoLabel" for="Id_EscalaTipo">*Tipo de Escala
				        <% if (escalaDt.getId().equalsIgnoreCase("")) { %>
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarArea" name="imaLocalizarArea" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EscalaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
				        <%}%>		
						</label><br>
						<input class="formEdicaoInputSomenteLeitura" readonly="true" name="EscalaTipo" id="EscalaTipo" type="text" size="20" maxlength="20" value="<%=escalaDt.getEscalaTipo()%>"/>
						<br />
					
					    <label class="formEdicaoLabel" for="Id_MandadoTipo">*Tipo de Mandado
				        <% if (escalaDt.getId().equalsIgnoreCase("")) { %> 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarMandadoTipo" name="imaLocalizarMandadoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MandadoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
					    <%}%>
						</label><br>
						<input class="formEdicaoInputSomenteLeitura" readonly="true" name="MandadoTipo" id="MandadoTipo" type="text" size="60" maxlength="100" value="<%=escalaDt.getMandadoTipo()%>"/>
						<br />
					
					    <label class="formEdicaoLabel" for="Id_Regiao">*Região
					
					    <% if (escalaDt.getId().equalsIgnoreCase("")) { %>
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRegiao" name="imaLocalizarRegiao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RegiaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
						<%}%>
						</label><br>
						<input  class="formEdicaoInputSomenteLeitura" readonly="true" name="Regiao" id="Regiao" type="text" size="60" maxlength="255" value="<%=escalaDt.getRegiao()%>"/>
						<br />
						
						<% if (!escalaDt.isPlantao()) { %>						
						<label class="formEdicaoLabel" for="QuantidadeMandado">Quantidade de Mandados</label><br>
				    	<input class="formEdicaoInput" name="QuantidadeMandado" id="QuantidadeMandado"  type="text" size="11" maxlength="11" value="<%=escalaDt.getQuantidadeMandado()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
				    	<br />
						<%}%>			
				
					</div>
					
					
					<label class="formEdicaoLabel" for="escalaAtiva">*Ativa?</label><br>
					<input type="radio" id="escalaAtiva" name="escalaAtiva" class="formEdicaoInput" value="<%=EscalaDt.ATIVO%>" <%=(escalaDt.getAtivo() != null ? ((escalaDt.getAtivo().equals(EscalaDt.ATIVO.toString())?"checked":"")):"")%>/>Sim
				    <input type="radio" id="escalaAtiva" name="escalaAtiva" class="formEdicaoInput" value="<%=EscalaDt.INATIVO%>" <%=(escalaDt.getAtivo() != null ? ((escalaDt.getAtivo().equals(EscalaDt.INATIVO.toString())?"checked":"")):"")%>/>Não
				    <br />
					
				</fieldset>
				
				<%if (escalaDt.getId() != null) {%>
				<fieldset class="formEdicao">
			    	<legend class="formEdicaoLegenda">Lista de Oficiais da Escala</legend> 
			    	<%if (escalaDt.getListaServentiaCargo() != null) {%>
					    	<label class="formEdicaoLabel" for="grupo">Adicionar Oficial
					    	</label>
					    	<br/>
					    	<label class="formEdicaoLabel" for="Nome">Nome
					    	<input class="FormEdicaoimgLocalizar" id="imaAdicionarUsuario" name=imaAdicionarUsuario type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					    	</label>
							<br/>
							<input class="formEdicaoInputSomenteLeitura" readonly name="NomeUsuTemp" id="NomeUsuTemp" type="text" size="40" value="<%=(request.getAttribute("NomeUsuTemp") != null)?request.getAttribute("NomeUsuTemp"):""%>">
							<input type="hidden" name="Id_ServentiaCargoEscala" id="Id_ServentiaCargoEscala" value="<%=request.getAttribute("Id_ServentiaCargoEscala")%>">
							<input type="hidden" name="Id_ServentiaCargo" id="Id_ServentiaCargo" value="<%=request.getAttribute("Id_ServentiaCargo")%>">
							<br/>
				   			<label class="formEdicaoLabel" for="Status">Status				   			
<%-- 				   			<input class="FormEdicaoimgLocalizar" id="imaLocalizar" name="imaLocalizar" type="image" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoEscalaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  --%>
				   			</label>
				   			<br>
							<input type="hidden" name="Id_Status" id="Id_Status" value="<%=(serventiaCargoEscalaStatusDt.getId() != "")?serventiaCargoEscalaStatusDt.getId():((request.getAttribute("Id_Status") != null)?request.getAttribute("Id_Status"):"")%>">
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Status" id="Status" type="text" size="40" value="<%=(serventiaCargoEscalaStatusDt.getServentiaCargoEscalaStatus() != "")?serventiaCargoEscalaStatusDt.getServentiaCargoEscalaStatus():((request.getAttribute("Status") != null)?request.getAttribute("Status"):"")%>">
							<br>
				   			<input id="imgsalvar" class="imgsalvar" title="Salvar novo status" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>');AlterarValue('Fluxo','3');" 
							<%if(request.getAttribute("NovoUsuario") == null) {%>
								style="display:none">  
							<%} else {%>
								>
							<%}%>
							
							<input id="imgexcluir" class="imgexcluir" title="Salvar novo status" name="imaExcluir" type="image"  src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>');AlterarValue('Fluxo','2');" style="display:none">  
								
							
			   	</fieldset>
			    	<%}%>
					<br />
			    	<%if (escalaDt.getListaServentiaCargo() != null) {%>
						<div>
							<div id="divTabela" class="divTabela" > 
						    	<table id="Tabela" class="Tabela">
						        	<thead>
						            	<tr class="TituloColuna">
						                	<th></td>                  
						                  	<th>Usuário</th>                
						                  	<th>Data Vinculação</th>
						                  	<th>Status</th>
						                  	<th class="colunaMinima">Editar</th>  
						               	</tr>
						           	</thead>
						           	<tbody id="tabListaUsuario">
										<%
										List liTemp = escalaDt.getListaServentiaCargo();
										ServentiaCargoEscalaDt objTemp;
										String stTempNome="";
										for(int i = 0 ; i< liTemp.size();i++) {
											objTemp = (ServentiaCargoEscalaDt)liTemp.get(i);%>
											<%if (stTempNome.equalsIgnoreCase("")) { 
												stTempNome="a";%> 
							                	<tr class="TabelaLinha1"> 
											<%}else{ 
												stTempNome=""; %>    
							                	<tr class="TabelaLinha2">
											<%}%>
							                <td><%=i+1%></td>                   
							                <td><%= objTemp.getNomeUsuario()%></td>               
							                <td align="center"><%= objTemp.getDataVinculacao()%></td>
											<td align="center"><%= objTemp.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus()%></td>
									 
							                <% if(objTemp.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus() != null && !objTemp.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus().equals("")) { %>
		 					                <td><input type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('NomeUsuTemp','<%=objTemp.getNomeUsuario()%>');AlterarValue('Id_ServentiaCargoEscala','<%=objTemp.getId()%>');AlterarValue('Id_Status','<%=objTemp.getServentiaCargoEscalaStatusDt().getId()%>');AlterarValue('Status','<%=objTemp.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus()%>');Mostrar('imgexcluir');return false;"></td>	
							                <%} %>
							               	</tr>
										<%}%>
						           </tbody>
						       </table>
						   </div> 
						</div>
			    	<%}%> 
		    	</fieldset>
		    	<%}%>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
	<script type="text/javascript">
		$(function(){
			atualizaDisplay();
		});
		
		$('input[name=isEscalaEspecial]').change(function(){
			atualizaDisplay();
		});
		
		function atualizaDisplay() {
			if($('input[name=isEscalaEspecial]:checked').val() == '1' || $('input[name=isEscalaEspecial]:checked').val() == '2'){
				$('#divMudarVisualizacao').hide("fast",function(){});
			} else {
				$('#divMudarVisualizacao').show("fast",function(){});
			}
		}
	</script>
</body>
</html>
