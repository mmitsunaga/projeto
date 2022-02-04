<%@page import="br.gov.go.tj.utils.Configuracao"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<jsp:useBean id="ImportarRetornoCadindt" scope="session" class= "br.gov.go.tj.projudi.dt.ImportarRetornoCadinDt"/>
	<head>	
		<title>Importar Retorno do Cadin</title>		
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');			
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
   		<script type="text/javascript">
	 		function ChecaExtensaoArquivo(formulario){
	   			var extensoesOk = ".csv";
	   			var extensao = formulario.arquivo.value.substr(formulario.arquivo.value.length - 4).toLowerCase();
	   			if(extensoesOk.indexOf( extensao ) == -1 ){
	       		mostrarMensagemErro("Projudi - Erro", "Arquivo inválido. A extensão do mesmo deve ser do tipo '.csv'");
	       		return false;
	     		}
	   			return true;
	 		}	 		
		</script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
	  	<div class="area"><h2>&raquo; Importar Retorno Cadin </h2></div>  
	  		<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
	  		<form action="ImportarRetornoCadin" method="post" name="Formulario" id="Formulario">
  			<% } else { %>
            <form action="ImportarRetornoCadin" method="post" name="Formulario" id="Formulario" enctype="multipart/form-data" onsubmit="return ChecaExtensaoArquivo(this)">
             <% }%> 				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
		  		<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
		  		<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
		  		<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		  						
			   	<div id="divEditar" class="divEditar">
			   		<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Importar Retorno Cadin </legend>
					    
					    <div class="obs">
							Selecione o arquivo desejado e clique em Importar para efetuar a importação.
							Deve possuir o separador igual a {TAB} 
						</div>
						<br />
						
						<label class="formEdicaoLabel" for="filename">Arquivo de Retorno</label><br>
						<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%>
						<input disabled type="text" class="formEdicaoInputSomenteLeitura" name="nomeArquivo" id="nomeArquivo" size="50" value="<%=ImportarRetornoCadindt.getNomeArquivo()%>" /> <br> 
					    <% } else { %>
					    <input type="file" name="arquivo" id="filename" size="50" /> <br>
					    <% } %>				        		        								        						      				
						<br />
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<%if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%>
							<input name="imgInserir" type="submit" value="Importar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
							<input name="imgCancelar" type="reset" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
							<% } else { %>
							<input name="imgCancelar" id="btnCancelar" type="submit" value="Cancelar/Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
							<% } %>							
					    </div>
					</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>