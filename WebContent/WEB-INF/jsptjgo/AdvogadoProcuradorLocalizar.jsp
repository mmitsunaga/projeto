<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">


<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Advogados/Procuradores da Serventia  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
   	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Advogados/Procuradores da Serventia </h2></div>
		<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
		<div id="divPortaBotoes" class="divPortaBotoes">
			<input id="imgLocalizar" class="imgLocalizar" title="Habilitar Usuário na Serventia" name="imaLocalizar" type="image"  src="./imagens/imgAssistente.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>');  AlterarValue('tempFluxo1','<%="LA1"%>'); AlterarValue('Curinga','<%="vazio"%>');">  
		</div>
		<fieldset id="formLocalizar" class="formLocalizar"  > 
    		<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Advogados/Procuradores da Serventia</legend>
    		   <input type="hidden" id="Id_Usuario" name="Id_Usuario">
			   <input type="hidden" id="Id_UsuarioServentia" name="Id_UsuarioServentia">
			   <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			   <div id="divTabela" class="divTabela" > 
			   	  <table id="Tabela" class="Tabela">
			           <thead>
			               <tr class="TituloColuna">
			                  <th class="colunaMinima"></th>
			                  <th class="colunaMinima" align="center">Id</th>
			                  <th align="center">Nome</th>
			                  <th align="center">Login</th>
			                  <th align="center">OAB</th>
			                  <th align="center">Situação Usuário</th>
			                  <th COLSPAN="2" align="center">Situação na Serventia</th>
			                  <th COLSPAN="2" align="center">Master</th>
			               </tr>
			           </thead>
			           <tbody id="tabListaUsuario">
						<%
						  List liTemp = (List)request.getAttribute("ListaAdvogados");
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
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" class="colunaMinima"><%= objTemp.getId()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" width="40%"><%=objTemp.getNome()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" width="10%"><%= objTemp.getUsuario()%></td>
							        <td onclick="selecionaSubmete('<%=objTemp.getId()%>','<%=objTemp.getUsuario()%>')" class="Centralizado" width="12%"><%= objTemp.getOabNumero() + "-"+objTemp.getOabComplemento()+"   "+objTemp.getOabEstado()%></td>
							        <%
							        
							        if(objTemp.getAtivo().equalsIgnoreCase("true")) {
										%><td align="center" class="preto" width="8%">ATIVO</td><%
									} else {
										%><td align="center" class="vermelho" width="8%">INATIVO</td><%
									}
							        
							        if(objTemp.getUsuarioServentiaAtivo().equalsIgnoreCase("true")) {
										%><td align="center" class="azul" width="8%">
											ATIVO
										  </td>
										  <td align="left" class="colunaMinima" >
											<input name="formLocalizarimgEditar" type="image"  title="Clique para Inativar Advogado/Procurador na Serventia" src="./imagens/imgAtualizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>'); AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="HS2"%>')">
										  </td>
										<%
									} else {
										%><td align="center" class="vermelho" width="8%">
										     INATIVO
										</td>
										<td align="left" class="colunaMinima" >
										     <input name="formLocalizarimgEditar" type="image"  title="Clique para Ativar Advogado/Procurador na Serventia" src="./imagens/imgAtualizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>'); AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="HS3"%>')">
										</td>
										<%
									}
							        
							        if(objTemp.getUsu_Serventia_Oab_Master().equalsIgnoreCase("true")) {
										%><td align="center" class="azul" width="6%">
											SIM											
										  </td>
										  <td align="left" class="colunaMinima" >
										  	<input name="formLocalizarimgEditar" type="image"  title="Desmarcar Advogado/Procurador Como Master" src="./imagens/imgAtualizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>'); AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="HAP3"%>')">
										  </td>
										<%
									} else {
										%><td align="center" class="vermelho" width="6%">
											NÃO											
										  </td>
										  <td align="left" class="colunaMinima" >
										  	<input name="formLocalizarimgEditar" type="image"  title="Marcar Advogado/Procurador Como Master" src="./imagens/imgAtualizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>'); AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="HAP2"%>')">
										  </td>
										<%
									}  %>
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