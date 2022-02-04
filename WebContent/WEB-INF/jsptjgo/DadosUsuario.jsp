<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="usuarioDt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<%@ include file="js/buscarArquivos.js"%>
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivos.js'></script>

	</head>
	
	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; Dados do Usuário</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
  			
  				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
  				<input type="hidden" name="Id_Usuario" id="Id_Usuario" value="<%=usuarioDt.getId()%>"/>
  				<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">  
  				<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
  				
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend> Dados do Usuário </legend>
					
					<div> Tipo Usuário</div>
					<span><%=GrupoDt.getAtividadeUsuario(usuarioDt.getGrupoCodigo())%></span><br />
					
			   		<div> Nome </div>
					<span style="width: 500px;"><%=usuarioDt.getNome()%></span/><br />
					
			   		<div> RG </div>
		    	  	<span><%=usuarioDt.getRg()%> </span>
		    	  	
					<div> CPF</div>
					<span><%=usuarioDt.getCpf()%></span><br />
					
		    	  	<div> Dt. Nascimento </div>
					<span><%=usuarioDt.getDataNascimento()%></span><br />
					
					<% if (!usuarioDt.isAdvogado()){ %>
					<div> Matrícula </div>
					<span><%=usuarioDt.getMatriculaTjGo()%></span><br />
					<% } %>
					
					<div> Login</div>
					<span><%=usuarioDt.getUsuario()%></span>	
					
					<div> Status</div>
					<%if (usuarioDt.getAtivo().equalsIgnoreCase("true")){ %>
                   	<span><font color="blue"><strong> ATIVO </strong></font></span>
                   	<%} else {%>
                   	<span><font color="red"><strong>INATIVO</strong></font></span>
                   	<%}%>
					<br />
							
					<div> Telefone </div>
				    <span><%= usuarioDt.getTelefone()%></span>
				    <div class="space">&nbsp;</div>
						
					<fieldset id="VisualizaDados" class="VisualizaDados" style="background-color:#eee">
						<legend> Endereço </legend>
						    				
						<div> Logradouro</div> 
						<span style="width: 500px;"><%=usuarioDt.getEnderecoUsuario().getLogradouro()%></span>
						<br />
				    				
				    	<div> Número</div> 
				    	<span><%=usuarioDt.getEnderecoUsuario().getNumero()%></span>
								
						<div> Complemento</div> 
						<span><%=usuarioDt.getEnderecoUsuario().getComplemento()%></span>
						<br />
								
						<div> Bairro</div>  
						<span><%=usuarioDt.getEnderecoUsuario().getBairro()%></span>
						<br />
				
						<div> Cidade</div>  
						<span><%=usuarioDt.getEnderecoUsuario().getCidade()%></span>
									
				   		<div> Uf</div> 
						<span><%=usuarioDt.getEnderecoUsuario().getUf()%></span>
						<br />
						
						<div> Cep</div> 
						<span><%=usuarioDt.getEnderecoUsuario().getCep()%></span>
						<br />
					</fieldset>
					<br />
					
					<blockquote align="center">
						<%if(usuarioDt.getAtivo().equalsIgnoreCase("true")) {%>
			      		<button type="submit" name="operacao" value="Desativar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');">
							<img src="./imagens/imgExcluir.png" alt="Desativar Usuário" />
							Bloquear Usuário
						</button>
			    		<%} else if(usuarioDt.getAtivo().equalsIgnoreCase("false")) { %>
			    		<button type="submit" name="operacao" value="Ativar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','A');">
							<img src="./imagens/imgExcluir.png" alt="Ativar Usuário" />
							Ativar Usuário
						</button>
			    		<%}%>
				    	<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Curinga','L');">
							<img src="imagens/imgSenhaLimpar.png" alt="Confirmar" />
							Limpar Senha
						</button>	
						
				    	<button type="submit" name="operacao" value="Confirmar" onclick="AlterarAction('Formulario','UsuarioArquivo');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
							<!--  <img src="imagens/22x22/btn_anexo.png" alt="Confirmar" /> -->
							Documentos
						</button>		
						
					</blockquote>
					
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>