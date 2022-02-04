<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="certificadoDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertificadoDt"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Certificado Confiável</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
			
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
</head>
<body>
<div class="divCorpo">
	
	<%@ include file="Padroes/Mensagens.jspf"%>

	<form action="CertificadoConfiavel" method="post" name="Formulario" id="Formulario" enctype="multipart/form-data" onsubmit="return ChecaExtensaoArquivo(this)">
		<div class="area"><h2>&raquo;Inserir Certificado Confiável</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		<div id="divEditar" class="divEditar">
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
			</div>
			</br >
		
			<fieldset class="formEdicao"  >
				<legend class="formEdicaoLegenda">Inserir Certificado Confiável</legend> 
			    <label class="formEdicaoLabel" for="caConfiavel">*Certificado Confiável</label><br><input class="formEdicaoInputSomenteLeitura"  readonly="true" type="file"  name="arquivo" id="filename" size="90"><br />
				<%String filtroTipo = (String) request.getAttribute("certRaiz");%>
		       	<input type="radio" name="certRaiz"  value="1" <%if (filtroTipo.equals("1")){%> checked<%}%> " />Certificado Raiz
				           		
				<%if (request.getAttribute("PaginaAtual") != null && !request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
						<button type="submit" name="operacao" value="Responder" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')">
							<!--  <img src="imagens/22x22/ico_sucesso.png" alt="Inserir Certificao Confirmar" /> -->
								Salvar
						</button>
					</div>
				<%} %>
				
				<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
					    <% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
					    	<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
					    <%  }%> 
						<%if (request.getAttribute("PaginaAtual") != null && request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
							<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')" >
								<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
									Confirmar
								</button>
							<br />
						<%}%>
					</div>
				 <%}%>
			 </fieldset>
		 </div>
		 <script type="text/javascript">
			 		function ChecaExtensaoArquivo(formulario){
			   			var extensaoCer = ".cer";
			   			var extensaoCRT = ".crt";
			   			var extensao = formulario.arquivo.value.substr(formulario.arquivo.value.length - 4 ).toLowerCase();
			   			if(extensaoCer.indexOf( extensao ) == -1 && extensaoCRT.indexOf( extensao ) == -1){
			       			alert("Arquivo inválido. A extensão do mesmo deve ser do tipo '.cer' ou '.crt' ");
			       		return false;
			     		}
			   			return true;
			 		}
		</script>
	</form>
</div>
</body>
</html>