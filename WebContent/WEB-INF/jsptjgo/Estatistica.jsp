<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="UsuarioSessao" scope="session"	class="br.gov.go.tj.projudi.ne.UsuarioNe" />

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> Estatísticas da Serventia </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/jquery.number.min.js"> </script>  
	<script type="text/javascript" src="./js/estatistica.js"> </script>
	
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;Estatísticas da Serventia</h2></div>
			
			<fieldset class="formEdicao"  id="campos_Estatistica" >	<legend class="formEdicaoLegenda">Serventia
						<%if(UsuarioSessao.isAdministrador()){ %>
							<img src="./imagens/imgLocalizarPequena.png" onclick="MostrarBuscaPadrao('campos_Estatistica','Serventia','Consulta de Serventia', 'Digite a Serventia.', 'Id_Serventia', 'Serventia', ['Serventia'], ['Serventia'], '<%=Configuracao.Localizar%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" >
						<%}%> </legend>			
			<div class='divEditar col100'>
				<div class='divEditar col35'>					
					<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=UsuarioSessao.getServentia()%>"/>
					<input id='Id_Serventia' type="hidden" value="<%=UsuarioSessao.getId_Serventia()%>">
				</div>
				<div class='AlinharDireita'>
					<button onclick="atualizarTudo()">Atualizar</button>
				</div>	
			</div>
			</fieldset>								
			<div id="divEstatistica">
			
			</div>
		
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
