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
				   <input id="Cadastrador" name="Cadastrador" type="hidden" value="<%=request.getSession().getAttribute("Cadastrador")%>">
				   <fieldset id="formLocalizar" class="formLocalizar"> 
				       <legend id="formLocalizarLegendaLiberar" class="formLocalizarLegenda">Consulta de Certificado </legend>
				       <label  id="formLocalizarLabelLiberar"   class="formLocalizarLabel">Digite o Usuário</label><br> 
				       <input  id="NomeBusca"   class="formLocalizarInput" name="NomeBusca" type="text" value="" size="60" maxlength="60"/>
				       <input  id="formLocalizarBotaoLiberar"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="buscaDados('0',<%=Configuracao.TamanhoRetornoConsulta %>); return false;" >
				   </fieldset/><br />
				   <input type="hidden" id="<%=request.getAttribute("tempBuscaId_Certificado").toString()%>" name="<%=request.getAttribute("tempBuscaId_Certificado").toString()%>">
				   <input type="hidden" id="tempBuscaId_Certificado" name="tempBuscaId_Certificado" value="<%=request.getAttribute("tempBuscaId_Certificado")%>">
				   <input type="hidden" id="<%=request.getAttribute("tempBuscaUsuarioCertificado").toString()%>" name="<%=request.getAttribute("tempBuscaUsuarioCertificado").toString()%>">
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
<%--  				<%
				  List<CertificadoDt> liTemp = (List<CertificadoDt>)request.getAttribute("ListaCertificado");
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
