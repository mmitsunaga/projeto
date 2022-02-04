<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<jsp:useBean id="UsuarioServentiaGrupodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"%>
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
			<div class="area"><h2>&raquo; Habilitação no Ministério Público</h2></div>
			
			<form action="UsuarioServentiaGrupo" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="Id_UsuarioServentiaGrupo" name="Id_UsuarioServentiaGrupo" type="hidden" />
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Habilitação/Desabilitação de Usuário</legend> 
						
						<br />
						<label class="formEdicaoLabel" for="serventia">*Usuário 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario" name="imaLocalizarUsuario" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
						</label><br> 
						<input class="formEdicaoInputSomenteLeitura" readonly name="Usuario" id="Usuario" type="text" size="70" maxlength="80" value="<%=UsuarioServentiaGrupodt.getUsuario()%>">
					    <br /><br />
						
						<fieldset class="formEdicao">
							<legend class="formEdicaoLegenda">Selecione o Grupo e Serventia</legend> 
							
							<label class="formEdicaoLabel" for="grupo">*Grupo 
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarGrupo" name="imaLocalizarGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
						   </label><br> 
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="Grupo" id="Grupo" type="text" size="70" maxlength="80" value="<%=UsuarioServentiaGrupodt.getGrupo()%>"/>
						    <br />
							
							<label class="formEdicaoLabel" for="serventia">*Serventia 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
							</label><br> 
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="70" maxlength="80" value="<%=UsuarioServentiaGrupodt.getServentia()%>">
							
					   		<input id="imgsalvar" class="imgsalvar" title="Salvar Serventia e Grupo" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');" >
						    <br />
					    </fieldset>
					    
				      	<fieldset>
							<legend class="formEdicaoLegenda">Habilitações Existentes</legend>
		   					<input id="ListaServidorJudiciario" name="ListaServidorJudiciario" type="hidden" value="<%=request.getAttribute("ListaServidorJudiciario")%>">
							<div id="divTabela" class="divTabela" > 
						    	<table id="Tabela" class="Tabela">
						        	<thead>
						            	<tr class="TituloColuna">
						                	<th></td>                  
						                  	<th>Serventia</th>                
						                  	<th>Grupo</th>
						                  	<th>Status</th>
						                  	<th>Opções</th>
						               	</tr>
						           	</thead>
						           	<tbody id="tabListaUsuario">
										<%
							  			List liTemp = UsuarioServentiaGrupodt.getListaServentiasGrupos();
							  			UsuarioServentiaGrupoDt objTemp;
							  			boolean boLinha = false;
							  			for(int i = 0 ; i< liTemp.size();i++) {
							      			objTemp = (UsuarioServentiaGrupoDt)liTemp.get(i); %>
											<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
							                	<td><%=i+1%></td>                   
							                   	<td><%= objTemp.getServentia()%></td>               
							                   	<td><%= objTemp.getGrupo()%></td>
							                   	<%if (objTemp.getAtivo().equalsIgnoreCase("true")){ %>
							                   	<td align="center">ATIVO</td>
							                   	<%} else {%>
							                   	<td align="center"><font color="red" size="-1"><strong>INATIVO</strong></font></td>
							                   	<%}
							      				if(objTemp.getAtivo().equalsIgnoreCase("true")) {%>
							      				<td align="center"><input align="middle" name="formLocalizarimgexcluir" type="image"  title="Desabilitar Servidor na Serventia" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Id_UsuarioServentiaGrupo','<%=objTemp.getId()%>');AlterarValue('Serventia','<%=objTemp.getServentia()%>');AlterarValue('Grupo','<%=objTemp.getGrupo()%>');"></td>
							     				<%} else { %>
							     				<td align="center"><input align="middle" name="formLocalizarimgEditar" type="image"  title="Habilitar Servidor na Serventia" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Id_UsuarioServentiaGrupo','<%=objTemp.getId()%>');AlterarValue('Serventia','<%=objTemp.getServentia()%>');AlterarValue('Grupo','<%=objTemp.getGrupo()%>');"></td>
							     				<%}%>
							               </tr>
										<%
											boLinha = !boLinha;
										}%>
						           </tbody>
		       					</table>
		   					</div> 
		   				</fieldset>
						</div>
			    	</fieldset>
			    	<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			    </div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>