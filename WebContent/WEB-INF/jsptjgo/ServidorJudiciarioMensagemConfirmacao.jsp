<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

 <%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
 <%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
 					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_UsuarioServentia").toString()%>" name="<%=request.getAttribute("tempBuscaId_UsuarioServentia").toString()%>">
 					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
 					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
					<%UsuarioDt servidorJudiciarioDt = (UsuarioDt) request.getAttribute("servidorJudiciarioDt");%><br />
										 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))
							&& (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("CG"))) {%>
					<fieldset id="VisualizaDados" class="VisualizaDados" >
						<legend>Dados do Servidor Judiciário</legend>
					    <div>Código: </div> 
					    <span><%=servidorJudiciarioDt.getId()%></span/><br />
					    <div> Nome:</div>
					    <span style="width: 500px;"><%=servidorJudiciarioDt.getNome()%></span/><br />
					    <div> Login:</div>
					    <span><%=servidorJudiciarioDt.getUsuario()%></span/><br />
					    <div> RG: </div>
					    <span><%=servidorJudiciarioDt.getRg()%></span/><br />
					    <div> CPF:</div>
					    <span><%=servidorJudiciarioDt.getCpf()%></span/><br />
					    <div>Serventia: </div>
					    <span style="width: 500px;"><%= servidorJudiciarioDt.getServentia()+"-"+servidorJudiciarioDt.getServentiaUf()%></span/><br />
					    <div>Grupo: </div>
					    <span style="width: 500px;"><%= servidorJudiciarioDt.getGrupo()%></span/><br />
					    <div> Matrícula TJ: </div>
					    <span><%= servidorJudiciarioDt.getMatriculaTjGo()%></span/><br />
					    <div>Status: </div>
					    <span><font color="red" size="-1"><strong>INATIVO</strong></font></span/><br />
					</fieldset/><br />
					<div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
						<div class="divMensagemsalvar">Clique para Ativar o Servidor Judiciário na Serventia e Grupo.</div> 	
					    <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentia")%>','<%=servidorJudiciarioDt.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="G"%>')"/><br /> <br /> 
					</div>
					 <%}%>
					 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))
							&& (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("CF"))) {%>
					<fieldset id="VisualizaDados" class="VisualizaDados" >
						<legend>Dados do Servidor Judiciário</legend>
					    	<div>Código: </div> 
					      	<span><%=servidorJudiciarioDt.getId()%></span/><br />
					      	<div> Nome:</div>
					      	<span style="width: 500px;"><%=servidorJudiciarioDt.getNome()%></span/><br />
					      	<div> Login:</div>
					      	<span><%=servidorJudiciarioDt.getUsuario()%></span/><br />
					      	<div> RG: </div>
					      	<span><%=servidorJudiciarioDt.getRg()%></span/><br />
					      	<div> CPF:</div>
					      	<span><%=servidorJudiciarioDt.getCpf()%></span/><br />
					      	<div>Serventia: </div>
					      	<span style="width: 500px;"><%= servidorJudiciarioDt.getServentia()+"-"+servidorJudiciarioDt.getServentiaUf()%></span/><br />
					      	<div>Grupo: </div>
					      	<span style="width: 500px;"><%= servidorJudiciarioDt.getGrupo()%></span/><br />
					      	<div> Matrícula TJ: </div>
					     	<span><%= servidorJudiciarioDt.getMatriculaTjGo()%></span/><br />
					      	<div>Status: </div>
					      	<span><font color="red" size="-1"><strong>ATIVO</strong></font></span/><br />
						</fieldset/><br />
						<div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							<div class="divMensagemsalvar">Clique para Desativar o Servidor Judiciário na Serventia e Grupo.</div> 	
					    	<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_UsuarioServentia")%>','<%=servidorJudiciarioDt.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="F"%>')"/><br /> <br /> 
						</div>
					 	<%}%>
					</form>
				</div>  
	</div>
</body>
</html>


	