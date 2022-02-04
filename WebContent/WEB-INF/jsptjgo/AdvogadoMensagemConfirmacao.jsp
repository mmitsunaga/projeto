<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

 <%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
 <%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>   	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<div id="divPortaBotoes" class="divPortaBotoes">
						<input id="imgAtualizar" class="imgAtualizar" title="Voltar para tela principal" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		    			<a class="divPortaBotoesLink" href="Ajuda/AssistenteAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
					</div>
					<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
 					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_UsuarioServentia").toString()%>" name="<%=request.getAttribute("tempBuscaId_UsuarioServentia").toString()%>">
 					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
 					<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
					<%UsuarioDt advogadoDt = (UsuarioDt) request.getAttribute("advogadoDt");%><br />
					 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))
							&& (request.getAttribute("Curinga").toString().equalsIgnoreCase("CG"))) {%>
					<fieldset id="VisualizaDados" class="VisualizaDados" >
						<legend>Dados do Advogado</legend>
					    	<div>ID Usuário: </div> 
					      	<span><%=advogadoDt.getId()%></span/><br />
					      	<div> Nome:</div>
					      	<span style="width: 500px;"><%=advogadoDt.getNome()%></span/><br />
					      	<div> Login:</div>
					      	<span><%=advogadoDt.getUsuario()%></span/><br />
					      	<div> RG: </div>
					      	<span><%=advogadoDt.getRg()%></span/><br />
					      	<div> CPF:</div>
					      	<span><%=advogadoDt.getCpf()%></span/><br />
					      	<div>Serventia OAB: </div>
					      	<span style="width: 500px;"><%= advogadoDt.getServentia()+"-"+advogadoDt.getServentiaUf()%></span/><br />
					      	<div>OAB/Matrícula: </div>
					      	<span><%= advogadoDt.getUsuarioServentiaOab().getOabNumero()%></span/><br />
					      	<div>Usuário Serventia: </div>
					      	<span><font color="red" size="-1"><strong>INATIVO</strong></font></span/><br />
					 	</fieldset/><br />
						<div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							<div class="divMensagemsalvar">Clique para habilitar o(a) advogado(a) na Serventia (OAB).</div> 	
					        <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentia")%>','<%=advogadoDt.getId_UsuarioServentia()%>'); AlterarValue('Curinga','<%="G"%>')"/><br /> <br /> 
					    </div>
					 <%}%>
					 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))
							&& (request.getAttribute("Curinga").toString().equalsIgnoreCase("CF"))) {%>
					 
					 <fieldset id="VisualizaDados" class="VisualizaDados" >
					 	<legend>Dados do Advogado</legend>
					    	<div>ID Usuário: </div> 
					      	<span><%=advogadoDt.getId()%></span/><br />
					      	<div> Nome:</div>
					      	<span style="width: 500px;"><%=advogadoDt.getNome()%></span/><br />
					      	<div> Login:</div>
					      	<span><%=advogadoDt.getUsuario()%></span/><br />
					      	<div> RG: </div>
					      	<span><%=advogadoDt.getRg()%></span/><br />
					      	<div> CPF:</div>
					      	<span><%=advogadoDt.getCpf()%></span/><br />
					      	<div>Serventia OAB: </div>
					      	<span style="width: 500px;"><%= advogadoDt.getServentia()+"-"+advogadoDt.getServentiaUf()%></span/><br />
					      	<div>OAB/Matrícula: </div>
					      	<span><%=advogadoDt.getUsuarioServentiaOab().getOabNumero()%></span/><br />
					      	<div>Usuário Serventia: </div>
					      	<span><font color="red" size="-1"><strong>ATIVO</strong></font></span/><br />
					 	</fieldset/><br />
						<div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							<div class="divMensagemsalvar">Clique para desabilitar o(a) advogado(a) na Serventia (OAB).</div> 	
					        <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentia")%>','<%=advogadoDt.getId_UsuarioServentia()%>'); AlterarValue('Curinga','<%="F"%>')"/><br /> <br /> 
					   	</div>
					 	<%}%>
				</form>
			</div>  
	</div>
</body>
</html>