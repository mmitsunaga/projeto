<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioFoneDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> Celulares Usuário  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<%@ include file="./js/Paginacao.js"%> 
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2> Celulares Usuário </h2></div>
			<div id="divLocalizar" class="divLocalizar" >
			<form action="UsuarioFone" method="post" name="Formulario" id="Formulario">
			   <input  name=__Pedido__ type="hidden" value="<%=request.getAttribute("__Pedido__")%>">			   
			   <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			   <input id="Id_UsuarioFone" name="Id_UsuarioFone" type="hidden" value=""/>
			   <input id="bloquear" name="bloquear" type="hidden" value=""/>
			    
			   <div id="divTabela" class="divTabela" > 
			       <table id="Tabela" class="Tabela">
			           <thead>
			               <tr>
			                   <td></td>
			                  <td>Telefone</td>
			                  <td>IMEI</td>
			                  <td>Data Pedido</td>
			                  <td>Data Liberação</td>
			                  <td >Bloquear</td>
			                  <td >Liberar</td>
			                  <td >Excluir</td>
			               </tr>
			           </thead>
			           <tbody id="tabListaCertificado">
			<%
			  List liTemp = (List)request.getAttribute("ListaCelulares");
			  if (liTemp != null) {
				  UsuarioFoneDt objTemp;
				  String stTempNome="";
				  for(int i = 0 ; i< liTemp.size();i++) {
				      objTemp = (UsuarioFoneDt)liTemp.get(i); %>
				 <%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
                  	<tr class="TabelaLinha1"> 
				 <%}else{ stTempNome=""; %>    
    	            <tr class="TabelaLinha2">
				 <%}%>
	                  <td > <%=i+1%></td>
	                  <td><%=objTemp.getFone()%></td>
	                  <td><%=objTemp.getImei()%>
	                  <td><%=objTemp.getDataPedido()%></td>
	                  <td><%=objTemp.getDataLiberacao()%></td>	                  
	                  <%if(objTemp.isLiberado()){ %>
	                  <td align="center"><input type="image" src="./imagens/22x22/ico_liberar.png" title="Bloquear" alt="Bloquear" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_UsuarioFone','<%=objTemp.getId()%>'); AlterarValue('bloquear','true')"></td>	                  		
	                  <td ></td>
	                  <%}else{%>
	                  	<td></td>
	                  	<td align="center"><input type="image" src="./imagens/22x22/ico_fechar.png" title="Liberar" alt="Liberar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_UsuarioFone','<%=objTemp.getId()%>'); AlterarValue('bloquear','false')"></td>
	                  <%}%>
	                  <td align="center"><input  type="image" src="./imagens/imgExcluir.png" title="Excluir Celular" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('Id_UsuarioFone','<%=objTemp.getId()%>'); "></td>
			      </tr>
				 <%}%>
				<%}%>
			           </tbody>
			       </table>
			  	</div> 
			  	<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>
		</div> 
		<%@ include file="Padroes/Mensagens.jspf" %>
</div>
</body>
</html> 
