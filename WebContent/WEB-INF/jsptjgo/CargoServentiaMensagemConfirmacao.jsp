<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

 <%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
 <%@page  import="br.gov.go.tj.utils.Configuracao"%>


<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%><html>
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
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Mensagem de Confirmação</h2> </div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
					<input type="hidden" id="<%=request.getAttribute("tempBuscaId_ServentiaCargo").toString()%>" name="<%=request.getAttribute("tempBuscaId_ServentiaCargo").toString()%>">
 					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
					<%ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) request.getAttribute("serventiaCargoDt");%><br />
										 
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {%>
					<fieldset id="VisualizaDados" class="VisualizaDados" >
						<legend>Dados do Cargo da Serventia</legend>
					    <div>Identificador: </div> 
					    <span><%=serventiaCargoDt.getId()%></span/><br />
					    <div>Descrição</div>
					    <span><%=serventiaCargoDt.getServentiaCargo()%></span/><br />
					    <div>Serventia: </div>
					    <span style="width: 500px;"><%= serventiaCargoDt.getServentia()%></span/><br />
					    <div>Tipo de Cargo</div>
					    <span><%=serventiaCargoDt.getCargoTipo()%></span/><br />
					    <div> Servidor:</div>
					    <span style="width: 500px;"><%=serventiaCargoDt.getNomeUsuario()%></span/><br />
					    <div> Usuário:</div>
					    <span style="width: 500px;"><%=serventiaCargoDt.getUsuarioServentiaGrupo()%></span/><br />
					</fieldset/>
					<br />
					<div id="divExcluir" class="divExcluir" class="divsalvar" align="center"/><br />
						<div class="divMensagemExcluir">Clique para excluir o cargo da Serventia.</div> 	
					   	<input class="imgExcluir" type="image" src="./imagens/imgExcluir.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>'); AlterarValue('<%=request.getAttribute("tempBuscaId_ServentiaCargo")%>','<%=serventiaCargoDt.getId()%>')"/><br /> <br /> 
					</div>
					 <%}%>
					</form>
				</div>  
	</div>
</body>
</html>


	