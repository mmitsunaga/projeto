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
		<div class="area"><h2>&raquo; |Advogado/Procurador| Mensagem de Confirmação </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<div id="divPortaBotoes" class="divPortaBotoes">
			  			<input id="imgAtualizar" class="imgAtualizar" title="Voltar para tela principal" name="imaAtualizar" type="image"  src="./imagens/imgVoltar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>'); AlterarValue('tempFluxo1','<%="HAP1"%>')">
		  			</div>
			   		<input type="hidden" id="Id_UsuarioServentia_Oab" name="Id_UsuarioServentia_Oab"  value="<%=request.getAttribute("Id_UsuarioServentia_Oab")%>">
			   		<input type="hidden" id="Id_UsuarioServentia" name="Id_UsuarioServentia"  value="<%=request.getAttribute("Id_UsuarioServentia")%>">
			   		<input type="hidden" id="statusAtualMaster" name="statusAtualMaster"  value="">
			   		<input type="hidden" id="statusAtualUsuarioServentia" name="statusAtualUsuarioServentia"  value="">
 					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
 					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">

					<%UsuarioDt advogadoDt = (UsuarioDt) request.getAttribute("advogadoDt");%><br /> 
					
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Curinga9))) {%>
						 <fieldset id="VisualizaDados" class="VisualizaDados" >
						 	<legend>Dados do Advogado/Procurador</legend>
						      <div>ID: </div> 
						      <span><%=advogadoDt.getId()%></span/><br />
						      <div> Nome:</div>
						      <span><%=advogadoDt.getNome()%></span/><br />
						      <div> Login:</div>
						      <span><%=advogadoDt.getUsuario()%></span/><br />
						      <div> OAB: </div>
						      <span><%=advogadoDt.getUsuarioServentiaOab().getOabNumero() + "-"+advogadoDt.getUsuarioServentiaOab().getOabComplemento()%></span/><br />
	   				          <div>Serventia: </div>
						      <span><%= advogadoDt.getServentia()+"-"+advogadoDt.getServentiaUf()%></span/><br />  
						      <div>ATIVO: </div>
						      <%if (advogadoDt.getUsuarioServentiaAtivo().equalsIgnoreCase("1")) { %>
						      	<span><font color="blue" size="-1"><strong>SIM</strong></font></span/><br />
						      <%} else { %>
						      	<span><font color="red" size="-1"><strong>Não</strong></font></span/><br />
						      <%} %>    
						      <div>Master: </div>
						      <%if (advogadoDt.getUsu_Serventia_Oab_Master().equalsIgnoreCase("true")) { %>
						      	<span><font color="blue" size="-1"><strong>SIM</strong></font></span/><br />
						      <%} else { %>
						      	<span><font color="red" size="-1"><strong>Não</strong></font></span/><br />
						      <%} %>
						 </fieldset/><br />
						 <%if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("HAP2")) {%>
							 <div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							 	  <div class="divMensagemsalvar">Clique para Marcar o Advogado/Procurador Como Master na Serventia.</div> 	
						          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>'); AlterarValue('Id_UsuarioServentia_Oab','<%=advogadoDt.getUsuarioServentiaOab().getId()%>'); AlterarValue('tempFluxo1','<%="HAP4"%>'); AlterarValue('statusAtualMaster','<%=advogadoDt.getUsu_Serventia_Oab_Master()%>')"/><br /> <br /> 
						     </div>
						 <%} else if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("HAP3")) {%>
						 		<div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							 	  <div class="divMensagemsalvar">Clique para Desmarcar o Advogado/Procurador Como Master na Serventia.</div> 	
						          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>'); AlterarValue('Id_UsuarioServentia_Oab','<%=advogadoDt.getUsuarioServentiaOab().getId()%>'); AlterarValue('tempFluxo1','<%="HAP5"%>'); AlterarValue('statusAtualMaster','<%=advogadoDt.getUsu_Serventia_Oab_Master()%>')"/><br /> <br /> 
						    	 </div>
						 <%} %>
						 
						 <%if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("HS2")) {%>
							 <div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							 	  <div class="divMensagemsalvar">Clique para INATIVAR o Advogado/Procurador na Serventia.</div> 	
						          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>'); AlterarValue('Id_UsuarioServentia','<%=advogadoDt.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="HS4"%>'); AlterarValue('statusAtualUsuarioServentia','<%=advogadoDt.getUsuarioServentiaAtivo()%>')"/><br /> <br />
						     </div>
						 <%} else if (request.getAttribute("tempFluxo1").toString().equalsIgnoreCase("HS3")) {%>
						 		<div id="divSalvar" class="divSalvar" class="divsalvar"/><br />
							 	  <div class="divMensagemsalvar">Clique para ATIVAR o Advogado/Procurador na Serventia.</div> 	
						          <input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" align="bottom" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>'); AlterarValue('Id_UsuarioServentia','<%=advogadoDt.getId_UsuarioServentia()%>'); AlterarValue('tempFluxo1','<%="HS5"%>'); AlterarValue('statusAtualUsuarioServentia','<%=advogadoDt.getUsuarioServentiaAtivo()%>')"/><br /> <br /> 
						    	 </div>
						 <%} %>
						 
					<% } %>
			</form>
			 <%@ include file="Padroes/Mensagens.jspf" %>
		</div>  
	</div>
</body>
</html>