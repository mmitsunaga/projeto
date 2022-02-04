<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="Assuntodt" scope="session" class= "br.gov.go.tj.projudi.dt.AssuntoDt"/>

<%-----%>
<%--<jsp:setProperty name="objAssunto" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Assunto  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  


</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Estatística de Pendências de Usuário</h2></div>

		<form action="EstatisticaUsuarioPendencia" method="post" name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
    		<input name="Relatorio" type="hidden" value="0" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<a class="divPortaBotoesLink" href="Ajuda/AssuntoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
            	<fieldset> <legend>Relatório de Pendências dos Usuários</legend>
                	<div class="col30">
                	<label>Ano</label><br> <select id="Ano" name="Ano">
                       <option value="">Não Selecionado</option>
                      <option value="2007">2007</option>
                      <option value="2008">2008</option>
                      <option value="2009">2009</option>
                    </select> 
                    </div>
                   
                    <div class="col25">
                    <label>Mês</label><br> <select id="Mes" name="Mes">
                      <option value="">Não Selecionado</option>
                      <option value="1">Janeiro</option>
                      <option value="2">Fevereiro</option>
                      <option value="3">Março</option>
                      <option value="4">Abril</option>
                      <option value="5">Maio</option>
                      <option value="6">Junho</option>
                      <option value="7">Julho</option>                      
                      <option value="8">Agosto</option>                      
                      <option value="9">Setembro</option>
                      <option value="10">Outubro</option>
                      <option value="11">Novembro</option>                      
                      <option value="12">Dezembro</option> 
                     </select> <br />
                     </div>
                     <div class="col30 clear">
                     <br>
                    <label>Serventia</label><br> <select id="Id_Serventia" name="Id_Serventia"> 
                                          <option value="">Não Selecionado</option>
					                      <option value="100"> Fazenda Pública</option>
					                      <option value="39075">9º Juizado Especial Cível</option>                                          
					                      <option value="39079">4º Juizado Especial Criminal</option>                                                                                    
                    						</select> <br />
                    						</div>
                    						<div class="col30">
                    <br>
                    <label>Usuário</label><br> <select id="Id_Usuario" name="Id_Usuario"> 
                                          <option value="">Não Selecionado</option>
					                      <option value="5004829"> Fernando Ribeiro Montefusco</option>
					                      <option value="5017890">Liliana Bittencourt</option>                                          
					                      <option value="magistradoteste">Ari Ferreira de Queiroz</option>              
   					                      <option value="testejui">Teste Juiz</option>                                                                                                                       
                    
                    						</select> <br />   
                    						</div>
                    						<div class="clear"> </div>
                    						<br>                
					<input value="Gerar Relatório" type="submit" name="Relatorio" />
                </fieldset>
			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
