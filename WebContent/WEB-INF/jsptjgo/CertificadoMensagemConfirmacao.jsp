 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

 <%@page import="br.gov.go.tj.projudi.dt.CertificadoDt"%>
 <%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<div id="divPortaBotoes" class="divPortaBotoes">
		  	  			<input id="imgNovo"  class="imgNovo" title="Novo Certificado - Identidade Digital" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >	
		      			<input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      			<a class="divPortaBotoesLink" href="Ajuda/CertificadoUsuarioAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  			</div>
 					<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Certificado").toString()%>" name="<%=request.getAttribute("tempBuscaId_Certificado").toString()%>">
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
					<%
					 	CertificadoDt certificadoDt = (CertificadoDt) request.getAttribute("certificadoDt");
					%>
					<br />
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.LiberarCertificado))) {%>
					 <fieldset id="VisualizaDados" class="VisualizaDados" >
					 	<legend>Dados do Certificado</legend>
					      <div>Número de série: </div> 
					      <span><%=certificadoDt.getId()%></span>
					      <br />
					      <div> Usuário </div>
					      <span><%=certificadoDt.getUsuarioCertificado()%></span>
					      <br />
					      <div> Data de Emissão </div>
					      <span><%=certificadoDt.getDataEmissao()%></span>
					      <br />
					      <div> Data de Validade </div>
					      <span><%=certificadoDt.getDataExpiracao()%></span>
					      <br />
					      <div> Situação </div>
					      <%
							 if(certificadoDt.ehValidoHoje()) {
								%><span>VÁLIDO</span><%
							} else {
								%><span>INVÁLIDO</span><%
							}
					      %>
					      <br />
					 </fieldset>
						 <br />
						 <div align="center" id="divLiberar" class="divsalvar">
						 	   <br />
						 	  <div class="divMensagemsalvar">Clique para Liberar Certificado</div> 	
					          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_Certificado")%>','<%=certificadoDt.getId()%>')"  > <br /> <br /> 
					     </div>
					 <%}%>
					 
					 <%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {%>
					 <fieldset id="VisualizaDados" class="VisualizaDados" >
					 	<legend>Dados do Certificado</legend>
					      <div>Número de série: </div> 
					      <span><%=certificadoDt.getId()%></span>
					      <br />
					      <div> Usuário </div>
					      <span><%=certificadoDt.getUsuarioCertificado()%></span>
					      <br />
					      <div> Data de Emissão </div>
					      <span><%=certificadoDt.getDataEmissao()%></span>
					      <br />
					      <div> Data de Validade </div>
					      <span><%=certificadoDt.getDataExpiracao()%></span>
					      <br />
					      <div> Situação </div>
					      <%
							 if(certificadoDt.ehValidoHoje()) {
								%><span>VÁLIDO</span><%
							} else {
								%><span>INVÁLIDO</span><%
							}
					      %>
					      <br />
					 </fieldset>
					 	 <br />
						 <div align="center" id="divRevogar" class="divsalvar">
						 	   <br /> 	
						 	  <div class="divMensagemsalvar">Clique para Revogar Certificado</div> 	
					          <input  class="imgexcluir" type="image" src="./imagens/imgExcluir.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_Certificado")%>','<%=certificadoDt.getId()%>')"  > <br /> <br /> 
					     </div>
					 <%}%> 
				</form>
		</div>  
	</div>
</body>
</html>
	