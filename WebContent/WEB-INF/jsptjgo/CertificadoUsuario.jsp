<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<title>Certificado de Usuário</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
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
		<div class="area"><h2>&raquo; Cadastro de Identidade Digital</h2></div>
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		  <form action="Certificado" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
		  <form action="CertificadoUsuario" method="post" name="Formulario" id="Formulario">
		<%}%>
		  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
		  <input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
		  <input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
		  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
		  <div id="divPortaBotoes" class="divPortaBotoes">
		  	  <input id="imgNovo"  class="imgNovo" title="Novo Certificado - Identidade Digital" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >	
		      <input id="imgLocalizar" class="imgLocalizar" title="Localizar - Lista Certificados" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')">
		      <input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      <a class="divPortaBotoesLink" href="Ajuda/CertificadoUsuarioAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  </div>
		     
		  <input type="hidden" id="tempBuscaId_Usuario" name="tempBuscaId_Usuario">
		  <input type="hidden" id="tempBuscaUsuario" name="tempBuscaUsuario">
		  <%
		    String senha = (String) request.getAttribute("senha");
			String cSenha = (String) request.getAttribute("cSenha");
		  %>
		   
		 <div id="divEditar" class="divEditar">
			<fieldset class="formEdicao"  > 
		    	<legend class="formEdicaoLegenda">Criar Identidade Digital </legend>
		    		<fieldset id="VisualizaDados" class="VisualizaDados" class="formDN"  > 
		    			<legend class="formDNLegenda">Senha Identidade Digital</legend>
		    			<label for="validade">Validade:</label> 3 anos<br />
		    			<label class="formEdicaoLabel" for="senha">*Senha:</label><br> <input title="Senha" class="formEdicaoInput" name="senha" type="password" id="senha" value="<%=senha%>"  size="15" maxlength="15" onkeyup=" autoTab(this,60)"/><br />
						<label class="formEdicaoLabel" for="cSenha">*Confirma senha:</label><br> <input title="Confirma senha" class="formEdicaoInput " name="cSenha" type="password" id="cSenha" value="<%=cSenha%>" size="15" maxlength="15" onkeyup=" autoTab(this,60)"/><br />
					</fieldset/>
					<br />
					
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
		        	
						<button type="submit"  title="Criar Identidade Digital"  name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('Curinga','<%="E"%>')" >
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Criar" /> -->
							Criar
						</button>
						<br />
				</div>
			</fieldset>
		</div>
		</form>
	<%@include file="Padroes/Mensagens.jspf"%>
</div>
</body>
</html>