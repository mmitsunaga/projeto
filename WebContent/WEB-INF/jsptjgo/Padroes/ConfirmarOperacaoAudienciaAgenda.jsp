<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>

<!-- BEANS -->
<jsp:useBean id="AudienciaAgendaDt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaAgendaDt" />
<!-- FIM BEANS -->

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Reserva ou Exclusão de Agendas para Audiências </title>
	
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<div id="divLocalizar" class="divLocalizar">
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
					<!-- Número aleatório para controlar multiplos submits -->
					<input type="hidden"  name=__Pedido__ value="<%=request.getAttribute("__Pedido__")%>" />
									
					<div id="divTabela" class="divTabela"> 
			    		<table id="Tabela" class="Tabela">
			        		<thead>
			        			<tr class="TituloColuna1">
									<th colspan="6">Serventia: <%=AudienciaAgendaDt.getServentia()%></th>
								</tr>
			            		<tr class="TituloColuna">
			                    	<th>Data da Audiência</th>
			                  		<th>Tipo da Audiência</th>
			                  		<th>Cargo da Serventia</th>
			                  		<th>Reservada?</th>
			               		</tr>
			           		</thead>
			           		<tbody id="tabListaAudiencia">
								<%
								List listaAudienciasSelecionadas = (List) request.getSession().getAttribute("ListaAudienciasSelecionadas");
			 					AudienciaDt audienciaDt;
			  					String stTempNome="";
			  					if (listaAudienciasSelecionadas != null){
			  	  					for(int i = 0 ; i < listaAudienciasSelecionadas.size();i++) {
			  	        				audienciaDt = (AudienciaDt)listaAudienciasSelecionadas.get(i); %>
			  	  						<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
			  	                    		<tr class="Linha1"> 
			  	  						<%}else{ stTempNome=""; %>    
			  	                     		<tr class="Linha2">
			  	  						<%}%>
			  	  							<td><%=audienciaDt.getDataAgendada()%></td>
			  	                      		<td><%=audienciaDt.getAudienciaTipo()%></td>
			  	                      		<td><%=audienciaDt.getAudienciaProcessoDt().getServentiaCargo()%></td>
			  	                      		<td>
			  	                      			<%if (audienciaDt.getReservada().equalsIgnoreCase("true")){%>
			  	                      				<%="Sim"%>    
			  	                      			<%}else{%>
			  	                      			    <%="Não"%>
			  	                      			<%}%>
			  	                      		</td>
		  	                 		    </tr>
			  	  					<%} 
			  					}%>
			           		</tbody>
						</table>
			   		</div>
			   	
					<!-- Salvar Resultado = Reservar Audiência(s) Livre(s) [(Agenda(s) Livre(s)] -->
					<%if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(Configuracao.Curinga6))) {%>
						<div id="divSalvar" class="divSalvar" class="divsalvar" align="center">
			    			<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" >
			    			<br />
		        			<div class="divMensagemsalvar">Clique para Confirmar a Reserva da(s) Agenda(s) de Audiência(s) Livre(s) </div>
						</div>
					<%}%>
				
					<!-- Excluir Resultado = Excluir Audiência(s) Livre(s) [Agenda(s) Livre(s)] -->
					<%if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {%>
						<div id="divExcluir" class="divExcluir" class="divexcluir" align="center">
				   			<input class="imgexcluir" type="image" src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>')" >
				   			<br />
				    		<div class="divMensagemexcluir">Clique para Confirmar a Exclusão da(s) Agenda(s) de Audiência(s) Livre(s) </div>
						</div>
					<%}%>
				</form>
			</div>  
		</div>
	</body>
</html>