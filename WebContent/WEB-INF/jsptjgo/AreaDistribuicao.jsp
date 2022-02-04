<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="AreaDistribuicaodt" scope="session" class= "br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ForumDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Área de Distribuição  </title>
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
			with(document.Formulario) {
				if (SeNulo(AreaDistribuicao, "O Campo Descrição é obrigatório!")) return false;
				if (SeNulo(AreaDistribuicaoCodigo, "O Campo Código é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Área de Distribuição</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="AreaDistribuicao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="AreaDistribuicao" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="tempLocalizar" name="tempLocalizar" type="hidden" value="0" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>"> 
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','vazio');">  
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
				<input id="imgexcluir" alt="Excluir" class="imgexcluir" title="Excluir - Exclui o registro localizado" name="imaexcluir" type="image"       src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Excluir)%>')" />
				<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')" />
				<%
				  String textoStatus = "";
				  if(AreaDistribuicaodt.getId() != null && !AreaDistribuicaodt.getId().trim().equals("") && AreaDistribuicaodt.getCodigoTemp() != null && !AreaDistribuicaodt.getCodigoTemp().trim().equals("")){%>				
					<%if(AreaDistribuicaodt.getCodigoTemp().equalsIgnoreCase(String.valueOf(AreaDistribuicaoDt.ATIVO))) {%>			      		
		      			<input id="imgBloquearAreaDist" alt="Bloquear Area de Distribuição para Cadastro de Processo" title="Bloquear Area de Distribuição para Cadastro de Processo" name="imgBloquearAreaDist" type="image"  src="./imagens/22x22/ico_liberar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','B');">
		      			<input id="imgInativarAreaDist" alt="Inativar Area de Distribuição" title="Inativar Area de Distribuição" name="imgInativarAreaDist" type="image"  src="./imagens/22x22/ico_sucesso.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','I');">
			    	<%} else if(AreaDistribuicaodt.getCodigoTemp().equalsIgnoreCase(String.valueOf(AreaDistribuicaoDt.BLOQUEADO))) { %>			    		
		    			<input id="imgDesbloquearAreaDist" alt="Desbloquear Area de Distribuição para Cadastro de Processo" title="Desbloquear Area de Distribuição para Cadastro de Processo" name="imgDesbloquearAreaDist" type="image"  src="./imagens/22x22/ico_fechar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','D');">
		      			<input id="imgInativarAreaDist" alt="Inativar Area de Distribuição" title="Inativar Area de Distribuição" name="imgInativarAreaDist" type="image"  src="./imagens/22x22/ico_sucesso.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','I');">
		      			<%textoStatus = "Bloqueada";%>		      			
			    	<%} else if(AreaDistribuicaodt.getCodigoTemp().equalsIgnoreCase(String.valueOf(AreaDistribuicaoDt.INATIVO))) { %>			    		
		    			<input id="imgBloquearAreaDist" alt="Bloquear Area de Distribuição para Cadastro de Processo" title="Bloquear Area de Distribuição para Cadastro de Processo" name="imgBloquearAreaDist" type="image"  src="./imagens/22x22/ico_liberar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','B');">
		    			<input id="imgAtivarAreaDist" alt="Ativar Area de Distribuição" title="Ativar Area de Distribuição" name="imgAtivarAreaDist" type="image"  src="./imagens/22x22/btn_excluir_2.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','A');">
		    			<%textoStatus = "Inativa";%>	
			    	<%}%>
			    <%} %>
		    	<a class="divPortaBotoesLink" href="Ajuda/AreaDistribuicaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao" id='Campos_AreaDist' > 
					<legend class="formEdicaoLegenda">Cadastro de Área de Distribuição <%if(textoStatus != null && !textoStatus.trim().equals("")){%><font color="red" size="-1"><strong>&lt;<%=textoStatus%>&gt;</strong></font><%}%></legend>
					<label class="formEdicaoLabel" for="Id_AreaDistribuicao">Identificador</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Id_AreaDistribuicao"  id="Id_AreaDistribuicao"  type="text"  readonly="true" value="<%=AreaDistribuicaodt.getId()%>"><br />
					
					<label class="formEdicaoLabel" for="AreaDistribuicao">*Descrição</label><br> 
					<input class="formEdicaoInput" name="AreaDistribuicao" id="AreaDistribuicao"  type="text" size="60" maxlength="60" value="<%=AreaDistribuicaodt.getAreaDistribuicao()%>" onkeyup=" autoTab(this,60)"><br />
					
					<label class="formEdicaoLabel" for="AreaDistribuicaoCodigo">*Código</label><br> 
					<input class="formEdicaoInput" name="AreaDistribuicaoCodigo" id="AreaDistribuicaoCodigo"  type="text" size="11" maxlength="11" value="<%=AreaDistribuicaodt.getAreaDistribuicaoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br />
										
					<label class="formEdicaoLabel" for="Id_ServentiaSubtipo">*Subtipo de Serventia
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaSubtipo" name="imaLocalizarServentiaSubtipo" type="image"  src="./imagens/imgLocalizarPequena.png"  					
					onclick="MostrarBuscaPadrao('Campos_AreaDist','AreaDistribuicao','Consulta de Serventia subtipo', 'Digite a serventia subtipo e clique em consultar.', 'Id_ServentiaSubtipo', 'ServentiaSubtipo', ['Serventia Subtipo'], [], '<%=(ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
					<input id="Id_ServentiaSubtipo" name="Id_ServentiaSubtipo" type="hidden" value="<%=AreaDistribuicaodt.getId_ServentiaSubtipo()%>" /> 					
					</label><br>  
					
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="ServentiaSubtipo" id="ServentiaSubtipo" type="text" size="60" maxlength="100" value="<%=AreaDistribuicaodt.getServentiaSubtipo()%>"><br />
					
					<label class="formEdicaoLabel" for="Id_Forum">*Fórum
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarIdForum" name="imaLocalizarIdForum" type="image"  src="./imagens/imgLocalizarPequena.png"  					
					onclick="MostrarBuscaPadrao('Campos_AreaDist','AreaDistribuicao','Consulta de Forum', 'Digite o Forum e clique em consultar.', 'Id_Forum', 'Forum', ['Forum'], [], '<%=(ForumDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
					<input id="Id_Forum" name="Id_Forum" type="hidden" value="<%=AreaDistribuicaodt.getId_Forum()%>" /> 					
					</label><br>  
					
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="Forum" id="Forum" type="text" size="60" maxlength="100" value="<%=AreaDistribuicaodt.getForum()%>"><br>
					<label class="formEdicaoLabel" for="Id_Comarca">Comarca</label><br>  
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Comarca" id="Comarca" type="text" size="50" maxlength="100" value="<%=AreaDistribuicaodt.getComarca()%>"><br />
					
					<!-- 
					<label class="formEdicaoLabel" for="Id_AreaDistribuicaoRelacionada">Área Relacionada</label><br>  
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%= Configuracao.Localizar %>'); AlterarValue('tempLocalizar','1');" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AreaDistribuicaoRelacionada','AreaDistribuicaoRelacionada'); return false;" > 
					<input name="Id_AreaDistribuicaoRelacionada" id="Id_AreaDistribuicaoRelacionada" type="hidden"  value="" />
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="AreaDistribuicaoRelacionada" id="AreaDistribuicaoRelacionada" type="text" size="60" maxlength="100" value="<%=AreaDistribuicaodt.getAreaDistribuicaoRelacionada()%>"><br />
					 -->
				
				<%	List liTemp = AreaDistribuicaodt.getListaServentias();
					if((liTemp != null) && (liTemp.size() > 0)){ %>
				
				<fieldset class="formEdicao"  >
				
				<legend class="formEdicaoLegenda">Serventias da Área de Distribuição</legend>
				
				
				<div>				   	
				   	<div id="divTabela" class="divTabela" > 
				       	<table id="Tabela" class="Tabela">
				           	<thead>
				               	<tr class="TituloColuna">
				                  	<th width="3%" ></th>
				                  	<th width="7%">Id</th>
				                	<th>Serventia</th>				                                  
				            	</tr>
				           	</thead>
				           	<tbody id="tabListaUsuario">
				           
							<%
							
							ServentiaDt objTemp;
							String stTempNome = "";
							
							 
							
							for(int i = 0 ; i < liTemp.size(); i++) {
								objTemp = (ServentiaDt)liTemp.get(i);
								if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							    	<tr class="TabelaLinha1"> 
								<%}else{ stTempNome=""; %>    
							    	<tr class="TabelaLinha2">
								<%}%>
								<td > <%=i+1%></td>								
								<td><%= objTemp.getId()%></td>
							    <td><%= objTemp.getServentia()%></td>
								</tr>
							<%}%>
							
				        	</tbody>
				    	</table>
					</div> 
				</div>
				
				
				
				
				</fieldset>
				
				<% } %>
				
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
