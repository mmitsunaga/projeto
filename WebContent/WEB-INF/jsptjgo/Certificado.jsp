<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="br.gov.go.tj.utils.Configuracao"%>




<jsp:useBean id="Certificadodt" scope="session" class= "br.gov.go.tj.projudi.dt.CertificadoDt"/>



<html>
<head>
	<title>Certificado</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
       <script type='text/javascript' src='./js/jquery.js'></script>
   	  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
</head>

<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
<script language="javascript" type="text/javascript">
    function VerificarCampos() {
    	if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
       with(document.Formulario) {
              submit();
       }
       }
     }
</script>

<%}%>
<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Certificados</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
	  	<form action="Certificado" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
	  	<form action="Certificado" method="post" name="Formulario" id="Formulario">
		<%}%>
		  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
		  <input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
		  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
		  <div id="divPortaBotoes" class="divPortaBotoes">
		  	  <input id="imgNovo"  class="imgNovo" title="Novo Certificado - Raiz ou Emissor" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>'); AlterarValue('Curinga','<%="F"%>')" >	
		      <input id="imgLocalizarRevogar" class="imgLocalizar" title="Localizar - Revogar Certificados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('Curinga','<%="A"%>')">
		      <input id="imgLocalizarLiberar" class="imgLocalizar" title="Localizar -  Liberar Certificados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('Curinga','<%="B"%>')">
		      <input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      <a class="divPortaBotoesLink" href="Ajuda/CertificadoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  </div>
		   <% if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(Configuracao.Editar)))  {%>
		   			<%if(request.getAttribute("Curinga").toString().equalsIgnoreCase("E")) {%> 
		    	 		<%@ include file="CertificadoEditarEmissor.jspf"%>
		   			<%} else if(request.getAttribute("Curinga").toString().equalsIgnoreCase("D")) {%> 
		    	 		<%@ include file="CertificadoEditarRaiz.jspf"%>
					<%} else if(request.getAttribute("Curinga").toString().equalsIgnoreCase("G")) {%>
					 	<%@ include file="CertificadoReciboEditar.jspf"%>	    	 		
		   			<%}else if(request.getAttribute("Curinga").toString().equalsIgnoreCase("F")){%> 
		            	 <%@ include file="CertificadoEditarRaiz.jspf"%>
		            	 <%@ include file="CertificadoEditarEmissor.jspf"%>
		            	 <%@ include file="CertificadoReciboEditar.jspf"%>
		           	<%}%> 
		   <%}%>
		
		</form>
	<%@include file="Padroes/Mensagens.jspf"%>
</div>
</body>
</html>