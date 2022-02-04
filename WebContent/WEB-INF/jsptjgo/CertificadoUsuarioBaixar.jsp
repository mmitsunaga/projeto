<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">


<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CertificadoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Certificados Usuário  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<%@ include file="./js/Paginacao.js"%> 
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Certificados Usuário  </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divPortaBotoes" class="divPortaBotoes">
		  	  		<input id="imgNovo"  class="imgNovo" title="Novo Certificado - Identidade Digital" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >	
		      		<input id="imgLocalizar" class="imgLocalizar" title="Localizar - Lista Certificados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')">
		      		<input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      		<a class="divPortaBotoesLink" href="Ajuda/CertificadoUsuarioAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  		</div>

			   <input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			   <input type="hidden" id="<%=request.getAttribute("tempBuscaId_Certificado").toString()%>" name="<%=request.getAttribute("tempBuscaId_Certificado").toString()%>">
			   <input type="hidden" id="<%=request.getAttribute("tempBuscaUsuarioCertificado").toString()%>" name="<%=request.getAttribute("tempBuscaUsuarioCertificado").toString()%>">
			   <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			    
			   <div id="divTabela" class="divTabela" > 
			       <table id="Tabela" class="Tabela">
			           <thead>
			               <tr>
			                   <td></td>
			                  <td>Número de série</td>
			                  <td>Usuário</td>
			                  <td>Data de Emissão</td>
			                  <td>Data de Validade</td>
			                  <td colspan="2">Situação</td>
			                  
			                  <td>Arquivo</td>
			               </tr>
			           </thead>
			           <tbody id="tabListaCertificado">
			<%
			  List liTemp = (List)request.getAttribute("ListaCertificado");
			  if (liTemp != null) {
				  CertificadoDt objTemp;
				  String stTempNome="";
				  for(int i = 0 ; i< liTemp.size();i++) {
				      objTemp = (CertificadoDt)liTemp.get(i); %>
				 <%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
				                  <tr class="TabelaLinha1"> 
				 <%}else{ stTempNome=""; %>    
				                  <tr class="TabelaLinha2">
				 <%}%>
				                  <td > <%=i+1%></td>
				                  <td><%= objTemp.getId()%></td>
				                   <td><%= objTemp.getUsuarioCertificado()%></td>
				                  <td><%= objTemp.getDataEmissao() %>
				                  <td><%= objTemp.getDataExpiracao() %></td>
				                  <%
				      				if(!objTemp.getDataRevogacao().equalsIgnoreCase("")) {
				      					%><td colspan="2" class="vermelho">REVOGADO</td><%
				     				} else {
										if(objTemp.ehValidoHoje()) {
											%><td class="azul">VÁLIDO</td>
							  				<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" title="Revogar Certificado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_Certificado")%>','<%=objTemp.getId()%>'); AlterarValue('<%=request.getAttribute("tempBuscaUsuarioCertificado")%>','<%=objTemp.getUsuarioCertificado()%>')"></td><%
										} else {
											%><td colspan="2" class="vermelho">INVÁLIDO</td><%
										}
				     				 }
				      				
				      				if(request.getAttribute("baixar") != null) {
				      					if(objTemp.ehValidoHoje()) {
				      			      		%><td><input name="formLocalizarimgsalvar"  type="image" src="./imagens/imgSalvar.png" title="Fazer Download do Certificado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_Certificado")%>','<%=objTemp.getId()%>'); AlterarValue('<%=request.getAttribute("tempBuscaUsuarioCertificado")%>','<%=objTemp.getUsuarioCertificado()%>')"></td><%
				      					} else {
				      					    %><td>-</td><%
				      					}
				      				}
				     		 %>
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
