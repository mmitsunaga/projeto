<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
	<title>Usuario Parte</title>
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>		
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type="text/javascript" src="./js/DivFlutuante.js" > </script>
      <script type='text/javascript' src='./js/jquery.js'></script>
	  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
</head>

	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)) &&
		request.getAttribute("Curinga").toString().equalsIgnoreCase("C"))  {%>
	<script language="javascript" type="text/javascript">
	    function VerificarCampos() {
	       with(document.Formulario) {
	             if (Nome.value == "") {
			         alert("O Campo Nome é obrigatório!");
			         Nome.focus();
			         return false; 
			     }
			     if (Usuario.value == "") {
			         alert("O Campo Usuario é obrigatório!");
			         Usuario.focus();
			         return false; 
			     }    
	             if (Sexo.value == "") {
			         alert("O Campo Sexo é obrigatório!");
			         Sexo.focus();
			         return false; 
			     }
	             if (DataNascimento.value == "") {
			         alert("O Campo Data de Nascimento é obrigatório!");
			         DataNascimento.focus();
			         return false; 
			     }
	             if (Rg.value == "") {
			         alert("O Campo Rg é obrigatório!");
			         Rg.focus();
			         return false; 
			     }
	             if (Cpf.value == "") {
			         alert("O Campo Cpf é obrigatório!");
			         Cpf.focus();
			         return false; 
			     }          
			     if (PaginaAtual.value == <%=Configuracao.Curinga7%>){
			     	if (SenhaAtual==""){
			     		alert("Digite a senha atual");
			         	Ativo.focus();
			         	return false;
			     	}
			     	if (SenhaNova==""){
			     		alert("Digite a nova senha");
			         	Ativo.focus();
			         	return false;
			     	}
			     	if (SenhaNovaComparacao==""){
			     		alert("Digite a nova senha");
			         	Ativo.focus();
			         	return false;
			     	}
			     }
	              submit();
	       }
	     }
	</script>
	<%}%>
	
	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; Cadastro de Usuário Parte </h2></div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)) &&
					request.getAttribute("Curinga").toString().equalsIgnoreCase("C"))  {%>
  			<form action="UsuarioParte" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
  			<form action="UsuarioParte" method="post" name="Formulario" id="Formulario">
			<%}%>
  			
  				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
  				<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
  				<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
  
 				<fieldset id="VisualizaDados" class="VisualizaDados">
			      	<legend>Parte do Processo</legend>
			      	
			      	<div> Número Processo </div>
					<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>"><%=processoDt.getProcessoNumero()%></a></span/><br />
			   			    	
				    <!-- PROMOVENTES -->
			 		<fieldset id="VisualizaDados" class="VisualizaDados">   
			 			<legend> Polo Ativo </legend/><br />
						<%
							List listaPromoventes = processoDt.getListaPolosAtivos();
									   	if (listaPromoventes != null){
									   		for (int i=0;i < listaPromoventes.size();i++){
												ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
						%>
						   	<div> Nome </div> <span class="span1"><%=parteDt.getNome()%></span>
						   	<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
						   	<%	if (parteDt.getDataBaixa().equals("")) { %>
				        		<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('Id_ProcessoParte','<%=parteDt.getId_ProcessoParte()%>');" title="Editar Usuário da Parte"/><br />
					   	<% 		}
					   	    }
			  			}
			  			%>
						</fieldset>
			
			
						<!-- PROMOVIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Polo Passivo </legend/><br />
					 		<%
					 			List listaPromovidos = processoDt.getListaPolosPassivos();
					 					   		if (listaPromovidos != null){
					 					   			for (int i=0;i < listaPromovidos.size();i++){
					 					   		  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					 		%>
				   				<div> Nome </div> <span class="span1"><%=parteDt.getNome()%></span>
						       	<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
						   		<%	if (parteDt.getDataBaixa().equals("")) { %>
				        			<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('Id_ProcessoParte','<%=parteDt.getId_ProcessoParte()%>');" title="Editar Usuário da Parte"/><br />
					   		<% 		}
					   	    	}
			  				}
			  				%>
						</fieldset>
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>				
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
