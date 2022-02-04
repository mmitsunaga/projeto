<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CertificadoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Consulta de Certificado para Liberar </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<%@ include file="./js/Paginacao.js"%> 
	<script type='text/javascript' src='./js/CertificadoLiberar.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Consulta de Certificado para Liberar  </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario"> 
				   <input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
				   <div id="divPortaBotoes" class="divPortaBotoes">
		  	  			<input id="imgNovo"  class="imgNovo" title="Novo Certificado - Raiz ou Emissor" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>'); AlterarValue('Curinga','<%="F"%>')" >	
		      			<input id="imgLocalizarRevogar" class="imgLocalizar" title="Localizar - Revogar Certificados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('Curinga','<%="A"%>')">
		      			<input id="imgLocalizarLiberar" class="imgLocalizar" title="Localizar -  Liberar Certificados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('Curinga','<%="B"%>')">
		      			<input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      			<a class="divPortaBotoesLink" href="Ajuda/CertificadoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  		    </div>
				   
				   <fieldset id="formLocalizar" class="formLocalizar"> 
				       <legend id="formLocalizarLegendaLiberar" class="formLocalizarLegenda">Consulta de Certificado </legend>
				       <label  id="formLocalizarLabelLiberar"   class="formLocalizarLabel">Digite o Usuário</label><br> 
				       <input  id="NomeBusca"   class="formLocalizarInput" name="NomeBusca" type="text" value="" size="60" maxlength="60"/>
				       <input  id="formLocalizarBotaoLiberar"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="buscaDados('0',<%=Configuracao.TamanhoRetornoConsulta %>); return false;" >
				   </fieldset><br />
				   <input type="hidden" id="<%=request.getAttribute("tempBuscaId_Certificado")%>" name="<%=request.getAttribute("tempBuscaId_Certificado")%>">
				   <input type="hidden" id="tempBuscaId_Certificado" name="tempBuscaId_Certificado" value="<%=request.getAttribute("tempBuscaId_Certificado")%>">
				   <input type="hidden" id="<%=request.getAttribute("tempBuscaUsuarioCertificado")%>" name="<%=request.getAttribute("tempBuscaUsuarioCertificado")%>">
				   <input type="hidden" id="tempBuscaUsuario_Certificado" name="tempBuscaUsuario_Certificado" value="<%=request.getAttribute("tempBuscaUsuario_Certificado")%>">
				   <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
				   
				   <div id="divTabela" class="divTabela" > 
				       <table id="Tabela" class="Tabela">
				           <thead>
				               <tr>
				                   <th></th>
				                  <th>Número de série</th>
				                  <th>Usuário</th>
				                  <th>Nome</th>
				                  <th>Data de Emissão</th>
				                  <th>Data de Validade</th>
				                  <th>Situação</th>
				                  <th>Arquivo</th>
				               </tr>
				           </thead>
				           <tbody id="tabListaDados">
<%-- 				<%
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
				                   <td><%= objTemp.getNomeUsuario()%></td>
				                  <td><%= objTemp.getDataEmissao() %>
				                  <td><%= objTemp.getDataExpiracao() %>
				                   <%
									if(objTemp.getSituacao().equals("VÁLIDO")) {
										%><td class="azul"><%=objTemp.getSituacao()%></td>
							  			<td align="center"><input name="formLocalizarimgsalvar"  type="image" src="./imagens/22x22/ico_liberar.png" title="Liberar Certificado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.LiberarCertificado%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_Certificado")%>','<%=objTemp.getId()%>'); AlterarValue('<%=request.getAttribute("tempBuscaUsuarioCertificado")%>','<%=objTemp.getUsuarioCertificado()%>')"></td><%
									} else {
										%><td colspan="2" class="vermelho"><%=objTemp.getSituacao()%></td><%
									}
				                  %>
				      			</tr>
					 <%}%>
			     <%}%> --%>
				           </tbody>
				        </table>
			  	</div> 
			</form>
		</div>  

<%--  	<%if (liTemp != null) {%>
		<%@ include file="Padroes/Paginacao.jspf"%>
	<%}%> --%>
    <div id="Paginacao" class="Paginacao"></div>
	<%@ include file="Padroes/Mensagens.jspf" %> 
</div>
</body>
</html> 
