<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Administracao</title>
	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		

		<style type="text/css">
			@import url('./css/Principal.css');
			/*@import url('./css/dataTable.css');*/
		</style>
	
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/jquery.dataTables.min.js'></script>
		
		<script type='text/javascript'>
			$(document).ready(function(){
			    $('#iTabela').dataTable( {"oLanguage": { "sUrl": "./js/TablePortugues.txt"    } 
			    						 , "bPaginate": false,"bLengthChange": true, "bFilter": true,"bSort": true, "bInfo": false, "bAutoWidth": false }
			    						 );
			});
		</script>
	</head>
<body>	
	<div id="idivCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Administração de Usuários Conectados</h2></div>
		<form action="Administrador" method="post" name="Formulario" id="Formulario">
	
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="Pagina" name="Pagina" type="hidden" value="<%=request.getAttribute("Pagina")%>">
			<input id="Id_Sessao" name="Id_Sessao" type="hidden" value="<%=request.getAttribute("Id_Sessao")%>">
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input type="image" src="./imagens/imgAtualizar.png" title="Atualizar a lista de usuários" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');"> 
			</div>
			
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			
			<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
				<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().trim()%></div>
			<%}%> 	
 			
	<div id="divTabela" class="divTabela" > 
		  <fieldset> <legend>Lista de Usuários Logados</legend>
		   	  <table id="iTabela" class="Tabela">
		           <thead>
		               <tr class="TituloColuna">
		                  <th class="colunaMinima">qtd</th>
		                  <th class="colunaMinima">Atualizar</th> 
		                  <th>Id_Usuario</th>
		                  <th>Usuário</th>
		                  <th>Ip origem</th>
		                  <th>Data/Hora</th>
		                  <th>Último Acesso</th>
		                  <th>Finalizar</th>
		               </tr>
		           </thead>
		           <tbody id="tabListaUsuario">
		<%
		  List liTemp = (List)request.getAttribute("ListaUsuarios");
		  UsuarioDt objTemp;
		  String stTempNome="";
		  for(int i = (liTemp.size()-1); i>= 0 ;i--) {
		      objTemp = (UsuarioDt)liTemp.get(i); %>
		<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
		                   <tr class="TabelaLinha1"> 
		<%}else{ stTempNome=""; %>    
		                   <tr class="TabelaLinha2">
		<%}%>
		                   <td ><%=i+1%></td>
		                   <td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" title="Atualizar as permisões do usuário" src="./imagens/imgAtualizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');  AlterarValue('Id_Sessao','<%=objTemp.getId()%>')" ></td>
		                   <td><%= objTemp.getId()%></td>
		                   <td><%= objTemp.getNome()%></td>
		                   <td><%= objTemp.getIpComputadorLog()%></td>
		                   <td><%= objTemp.getDataEntrada()%></td>
		                   <td><%= objTemp.getDataUlitmoAcesso()%></td>
		                   <td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" title="Finalizar a sessão do usuário"  src="./imagens/imgExcluir.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>'); AlterarValue('Pagina','0');  AlterarValue('Id_Sessao','<%=objTemp.getId()%>')" ></td>
		               </tr>
		<%}%>
		           </tbody>
		       </table>
		</fieldset>       
   </div> 
	</form>
 	</div>			 			
	
</body> 	
 </html>


