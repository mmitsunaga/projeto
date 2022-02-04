
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Administracao</title>
	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		

		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
	
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>
<body>	
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Administração de Usuários Conectados</h2></div>		
		
		<form action="Administrador" method="post" name="Formulario" id="Formulario">
						
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="Pagina" name="Pagina" type="hidden" value="<%=request.getAttribute("Pagina")%>">
			<input id="Id_Sessao" name="Id_Sessao" type="hidden" value="<%=request.getAttribute("Id_Sessao")%>">
			
			<input type="image" class="imgVoltar" src="./imagens/imgVoltarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');">
			
			<%UsuarioDt dtUsuario = (UsuarioDt)request.getAttribute("Usuario");%>
			<fieldset id="VisualizaDados" class="VisualizaDados"> <legend>Dados do usuário conectado</legend>
			 <div>Usuário</div><span><%= dtUsuario.getNome()%> </span/><br />
			 <div>Entrada</div><span><%= dtUsuario.getDataEntrada()%></span/><br />
			 <div>Último Acesso</div><span><%= dtUsuario.getDataUlitmoAcesso()%></span/><br />
			 <div>Ip Computador</div><span><%= dtUsuario.getIpComputadorLog()%></span/><br />
			</fieldset>
			 <div id="divConfirmar" class=""divConfirmar"">
			     <input class="imgConfirmar" type="image" src="./imagens/imgConfirmar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');  " /><br /> 
			     <div class="divMensagemexcluir">Clique para confirmar a finalização da sessão do usuário</div>    
			</div>

	</form>
 	</div>
 </body>
 </html>
			