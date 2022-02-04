<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Alteração de Senha  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
   	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
   	
 	<script language="javascript" type="text/javascript">
		function VerificarCampos() {		
			var s1 = $('#SenhaNova').val();
			var s2 = $('#SenhaNovaComparacao').val();
			
			if (s1!=s2) {
				alert('Digite a mesma senha nos campos Nova Senha e Repita a Nova Senha.')
				return false;
			}				
			submit();							
		}
	</script>
</head>
<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Alteração de Senha </h2></div>
		<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<div id="divEditar" class="divEditar">
				<input type="hidden" name="PassoSenha" id="PassoSenha" value="1"/>
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda"> Alteração da Senha de Acesso</legend>
					<div class='col45'>
						<%if(!UsuarioSessao.isLoginToken()){%>
						<div class='col45'>
							<label > Senha Atual </label> 
							<input type="password" name="SenhaAtual" id="SenhaAtual"  size="40" maxlength="60" onkeyup="autoTab(this,60)" value="<%=request.getAttribute("SenhaAtual")%>" />
						</div> <br />
						<%}%>
						<div class='col45'>
							<label > Nova Senha </label> 
							<input type="password" name="SenhaNova" id="SenhaNova" size="40" maxlength="60" onkeyup="autoTab(this,60)" value="<%=request.getAttribute("SenhaNova")%>"/> 
						</div> <br />
						<div class='col45'>
							<label > Repita a Nova Senha </label> 
							<input type="password" name="SenhaNovaComparacao" id="SenhaNovaComparacao" size="40" maxlength="60" value="<%=request.getAttribute("SenhaNovaComparacao")%>"/> 
						</div>
						<br />
						<div class='col45'>
						  <input class="imgsalvar" type="submit" value="Alterar">  
						</div>
					</div>
					<div class='col45'>
						<h3>Senha Forte</h3>
						<ul> A senha dever ter:
							<li>No mínimo 8 Caracteres </li>
							<li>No mínimo 1 Letra Maiúscula</li>
							<li>No mínimo 1 Letra Minúscula</li>
							<li>No mínimo 1 Número</li>
							<li>No mínimo 1 Símbolo. Ex. !@#$%¨&*()</li>
						</ul>
					</div>
				</fieldset>

			</div>
		</form>
	</div>  
	<%@ include file="Mensagens.jspf"%>
</div>
</body>
</html>