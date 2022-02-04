<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

 <%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
 <%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<div id="divPortaBotoes" class="divPortaBotoes">
			  			<input id="imgAtualizar" class="imgAtualizar" title="Voltar para tela principal" name="imaAtualizar" type="image"  src="./imagens/imgVoltar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		      			<a class="divPortaBotoesLink" href="Ajuda/AssistenteAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  			</div>
					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
 					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo").toString()%>" name="<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo").toString()%>">
 					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
 					<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
 					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">

					<%UsuarioDt assistenteDt = (UsuarioDt) request.getAttribute("assistenteDt");%><br /> 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))
							&& (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("CG"))) {%>
					 <fieldset id="VisualizaDados" class="VisualizaDados" >
					 	<legend>Dados do Assistente</legend>
					      <div>ID Assistente: </div> 
					      <span><%=assistenteDt.getId()%></span/><br />
					      <div> Nome:</div>
					      <span><%=assistenteDt.getNome()%></span/><br />
					      <div> Login:</div>
					      <span><%=assistenteDt.getUsuario()%></span/><br />
					      <div> RG: </div>
					      <span><%=assistenteDt.getRg()%></span/><br />
					      <div> CPF:</div>
					      <span><%=assistenteDt.getCpf()%></span/><br />
					        <div>Serventia: </div>
					     <span><%= assistenteDt.getServentia()+"-"+assistenteDt.getServentiaUf()%></span/><br />      
					      <div>Assis. Serventia: </div>
					      <span><font color="red" size="-1"><strong>INATIVO</strong></font></span/><br />
					 </fieldset/><br />
						    <div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
						 	  <div class="divMensagemsalvar">Clique para Ativar o Assistente na Serventia.</div> 	
					          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo")%>','<%=assistenteDt.getId_UsuarioServentiaGrupo()%>'); AlterarValue('tempFluxo1','<%="G"%>')"/><br /> <br /> 
					     </div>
					 <%}%>
					 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))
							&& (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("CF"))) {%>
					 <fieldset id="VisualizaDados" class="VisualizaDados" >
					 	<legend>Dados do Assistente</legend>
					      <div>ID Assistente: </div> 
					      <span><%=assistenteDt.getId()%></span/><br />
					      <div> Nome:</div>
					      <span><%=assistenteDt.getNome()%></span/><br />
					      <div> Login:</div>
					      <span><%=assistenteDt.getUsuario()%></span/><br />
					      <div> RG: </div>
					      <span><%=assistenteDt.getRg()%></span/><br />
					      <div> CPF:</div>
					      <span><%=assistenteDt.getCpf()%></span/><br />
					        <div>Serventia: </div>
					      <span><%= assistenteDt.getServentia()+"-"+assistenteDt.getServentiaUf()%></span/><br />      
					      <div>Assis. Serventia: </div>
					      <span><font color="red" size="-1"><strong>ATIVO</strong></font></span/><br />
					 </fieldset/><br />
						    <div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
						 	  <div class="divMensagemsalvar">Clique para Desativar o Assistente na Serventia.</div> 	
					          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentiaGrupo")%>','<%=assistenteDt.getId_UsuarioServentiaGrupo()%>'); AlterarValue('tempFluxo1','<%="F"%>')"/><br /> <br /> 
					     </div>
					 <%}%>
					
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))
							&& (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("S"))) {%>
					 <fieldset id="VisualizaDados" class="VisualizaDados" >
					 	<legend>Dados do Usuário</legend>
					      <div>ID Usuário: </div> 
					      <span><%=assistenteDt.getId()%></span/><br />
					      <div> Nome:</div>
					      <span><%=assistenteDt.getNome()%></span/><br />
					      <div> Login:</div>
					      <span><%=assistenteDt.getUsuario()%></span/><br />
					      <div> RG: </div>
					      <span><%=assistenteDt.getRg()%></span/><br />
					      <div> CPF:</div>
					      <span><%=assistenteDt.getCpf()%></span>     
					 </fieldset/><br />
						    <div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
						 	  <div class="divMensagemsalvar">Clique para Cadastrar Usuário como Assistente na Serventia.</div> 	
					          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_Usuario")%>','<%=assistenteDt.getId()%>'); AlterarValue('tempFluxo1','<%="S"%>')"/><br /> <br /> 
					     </div>
					 <%}%>
			</form>
		</div>  
	</div>
</body>
</html>