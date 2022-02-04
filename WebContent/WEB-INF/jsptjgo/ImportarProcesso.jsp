<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="ProcessoCadastroDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>	
		<title>Importar Dados Processo</title>
		
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
  	<div class="area"><h2>&raquo; Importar Processo </h2></div>
  		
		<form action="ImportarDadosProcesso" method="post" name="Formulario" id="Formulario" enctype="multipart/form-data" onsubmit="return ChecaExtensaoArquivo(this)">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
	  		<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>">
	  		<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<!-- Variáveis para controlar Passos do Cadastro -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=ProcessoCadastroDt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=ProcessoCadastroDt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=ProcessoCadastroDt.getPasso3()%>">

		   	<div id="divEditar" class="divEditar">
		   		<% if (!ProcessoCadastroDt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=ProcessoCadastroDt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
				<% } if (!ProcessoCadastroDt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=ProcessoCadastroDt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (!ProcessoCadastroDt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=ProcessoCadastroDt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','2');">
				<% } %>
				
				<fieldset class="formEdicao"> 
				    <legend class="formEdicaoLegenda">Passo 1 - Importar Arquivo </legend>

					<div id="divAjuda" class="divAjuda" >
						<img src="./imagens/imgAjudaPequena.png" onclick="DivFlutuanteValoresIniciais('Importação Dados TCO', getMensagem(5),'200','360','0','0', this);" onmouseup="DivFlutuanteUp('Informe');"" width="16" height="16" border="0" />
					</div>
			
					<% if (ProcessoCadastroDt != null && !ProcessoCadastroDt.getTcoNumero().equals("")) {%>
					
					<div class="obs">
						Já foi selecionado um Arquivo para Importação
					</div>
					<br />
						
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>Dados do Arquivo Selecionado</legend>

						<div>Área Destino</div>
						<span style="width: 600px;"><%=ProcessoCadastroDt.getAreaDistribuicao()%></span><br />

						<div>Tipo Ação</div>
						<span style="width: 600px;"><%=ProcessoCadastroDt.getProcessoTipo()%></span><br />
							
						<% if (!ProcessoCadastroDt.getTcoNumero().equals("")) { %>
						<div>TCO</div>
						<span><%=ProcessoCadastroDt.getTcoNumero()%></span>
						<% } %>
					</fieldset>
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Continuar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');"> 
						<input name="imgCancelar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
				    </div>
					<% } else { %>
					
					<div class="obs">
						Selecione o arquivo desejado e clique em Inserir para efetuar a importação. 
					</div>
					<br />
					
					<label class="formEdicaoLabel" for="filename">Arquivo</label><br> 
					<input type="file" name="arquivo" id="filename" size="50" /> <br>				        		        								        						      				
					<br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Inserir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','1');"> 
						<input name="imgCancelar" type="reset" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
				    </div>
						
					<% } %>
					
				</fieldset>					    
								

				<script type="text/javascript">
			 		function ChecaExtensaoArquivo(formulario){
			   			var extensoesOk = ".tco";
			   			var extensao = formulario.arquivo.value.substr(formulario.arquivo.value.length - 4 ).toLowerCase();
			   			if(extensoesOk.indexOf( extensao ) == -1 ){
			       		mostrarMensagemErro("Projudi - Erro", "Arquivo inválido. A extensão do mesmo deve ser do tipo '.tco'");
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