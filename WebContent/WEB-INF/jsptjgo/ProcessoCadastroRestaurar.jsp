<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoCadastroDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>	
		<title>Restaurar Dados Salvos</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');			
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; (<%=request.getAttribute("tempRetorno")%>) Carregar Dados </h2></div>
				<form action="ProcessoCadastroRestaurar" method="post" name="Formulario" id="Formulario" enctype="multipart/form-data" onsubmit="return ChecaExtensaoArquivo(this)">
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
					<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
		  			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
		  			<input id="tempRetorno" name="tempRetorno" type="hidden" value="<%=request.getAttribute("tempRetorno")%>" />
			   		<fieldset class="fieldEdicaoEscuro"> 
					    <legend class="formEdicaoLegenda">Carregar Dados </legend>
						<div class="obs">Selecione o arquivo desejado e clique em Carregar para efetuar a importação.</div> <br />
						<label class="formEdicaoLabel" for="filename">Arquivo</label><br> 
						<input type="file" name="arquivo" id="filename" size="50" /> <br/>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Carregar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');"> 
							<input name="imgCancelar" type="reset" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
					    </div>
					</fieldset>					    
					<script type="text/javascript">
				 		function ChecaExtensaoArquivo(formulario){
				   			var extensoesOk = ".projudi";
				   			var extensao = formulario.arquivo.value.substr(formulario.arquivo.value.length - 8 ).toLowerCase();
				   			if(extensoesOk.indexOf( extensao ) == -1 ){
				       			alert("Arquivo inválido. A extensão do mesmo deve ser do tipo '.projudi'");
					       		return false;
				     		}
				   			return true;
				 		}
					</script>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>