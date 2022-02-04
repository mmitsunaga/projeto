<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">


<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Assessores da Serventia  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
   	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_Usuario")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaUsuario")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';
		_PaginaExcluir = '<%=Configuracao.Excluir%>';
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Assessores da Serventia </h2></div>
		<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<div id="divPortaBotoes" class="divPortaBotoes">
			<input id="imgAtualizar" class="imgAtualizar" title="Voltar para tela principal" name="imaAtualizar" type="image"  src="./imagens/imgVoltar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		    <a class="divPortaBotoesLink" href="Ajuda/AssistenteAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		</div>
		<fieldset id="formLocalizar" class="formLocalizar"  > 
    		<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Assessores Cadastrados</legend>
			   <input type="hidden" id="<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo").toString()%>" name="<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo").toString()%>">
			   <input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
			   <input type="hidden" id="<%=request.getAttribute("tempBuscaUsuario").toString()%>" name="<%=request.getAttribute("tempBuscaUsuario").toString()%>">
			   <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			   <input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			   <div id="divTabela" class="divTabela" > 
			   	  <table id="Tabela" class="Tabela">
			           <thead>
			               <tr class="TituloColuna">
			                  <th class="colunaMinima"></th>
			                  <th>Identificador</th>
			                  <th>Nome</th>
			                  <th>Login</th>
			                  <th>RG</th>
			                  <th>CPF</th>
			                  <th>Situação</th>
			                  <th class="colunaMinima"></th>                 
			               </tr>
			           </thead>
			           <tbody id="tabListaUsuario">
						<%
						  List liTemp = (List)request.getAttribute("ListaAssistente");
						  if (liTemp != null) {
							  UsuarioDt objTemp;
							  String stTempNome="";
							  for(int i = 0 ; i< liTemp.size();i++) {
							      objTemp = (UsuarioDt)liTemp.get(i); %>
							 	<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                   <tr class="TabelaLinha1"> 
							 	<%}else{ stTempNome=""; %>    
							    	<tr class="TabelaLinha2">
							 	<%}%>
							        <td width="3%"> <%=i+1%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" class="Centralizado" width="12%"><%= objTemp.getId()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" width="27%"><%= objTemp.getNome()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" width="17%"><%= objTemp.getUsuario()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" class="Centralizado" width="16%"><%= objTemp.getRg()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" class="Centralizado" width="12%"><%= objTemp.getCpf()%></td>
							        <%
							        if(objTemp.getUsuarioServentiaGrupoAtivo().equalsIgnoreCase("true")) {
										%><td align="center" class="azul" width="10%">ATIVO</td><%
									} else {
										%><td align="center" class="vermelho" width="10%">INATIVO</td><%
									}
							      	if(objTemp.getUsuarioServentiaGrupoAtivo().equalsIgnoreCase("true")) {%>
							      		<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image"  title="Desativar Assessor na Serventia" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo")%>','<%=objTemp.getId_UsuarioServentiaGrupo()%>'); AlterarValue('tempFluxo1','<%="F"%>')"> </td>
							     	<%} else { %>
							     		<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  title="Ativar Assessor na Serventia" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo")%>','<%=objTemp.getId_UsuarioServentiaGrupo()%>'); AlterarValue('tempFluxo1','<%="G"%>')"> </td>
							     	<%}%> 
							        </tr>
							<%}%>
						<%}%>
					    </tbody>
				  </table>
			   </div> 
			</form>
		</div>  
		<%@ include file="Padroes/Mensagens.jspf" %>
</div>
</body>
</html>