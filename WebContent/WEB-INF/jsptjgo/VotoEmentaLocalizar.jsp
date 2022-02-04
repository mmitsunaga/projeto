<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%><html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>				
		<title> | <%=request.getAttribute("tempPrograma")%> | Aguardando Preparo para Julgamento </title>		
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/menusimples.css" type="text/css"  rel="stylesheet" />
		
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
    	<script type='text/javascript' src='./js/checks.js'></script>	
	    <script type='text/javascript' src='./js/jquery.js'></script>
	    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	    <script type="text/javascript" src="./js/acessibilidadeMenu.js"></script> 
	           	    	
	    <script type="text/javascript">
			$(document).ready(function() {
				$('#divTabelaSelecao').css('display','none');
				$(".BotoesPrioridade").hover(function(){ $(this).children('div').show("slow");  },function(){$(this).children('div').hide();   });
				$(".BotoesPrioridade > div").hide();
			} );							  
	    	
	    	
	    	function submeter2(action, id_Pendencia, paginaAtual){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		document.Formulario.submit();
	    	}	    	
	    	
	    	function ListarPedidoUrgente(pedido){
	    		if (pedido==0){
	    			$('.Urgente').show();
	    		}else{
	    			$('.Urgente').hide();
	    			$('.Urgente'+pedido).show();
	    		}
			}
	    </script>
	    
	    <script type="text/javascript">
			$(document).ready(function() {			
				criarMenu('opcoes','Principal','menuA','menuAHover');
			});			
		</script>
	    
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">						
		
			<div class="area"><h2>&raquo;  | <%=request.getAttribute("tempPrograma")%> | Aguardando Preparo para Julgamento </h2></div>
				
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					
					<div id="divEditar" class="divEditar">
						<fieldset class="formEdicao"> 
						    <legend class="formEdicaoLegenda"> Consulta de Voto/Ementa </legend>
							<label id="labelProcesso" class="formLocalizarLabel"> Número do Processo </label><br> 
							<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getAttribute("numeroProcesso")%>" size="30" maxlength="60" />
							<br />
							
							<label class="formEdicaoLabel" for="Id_Classificador">Classificador
							 <input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						   	<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">  
							</label><br>  
						    <input type="hidden" name="Id_Classificador" id="Id_Classificador" value="<%=request.getAttribute("id_Classificador")%>" />
						   
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="70" maxlength="100" value="<%=request.getAttribute("classificador")%>"/>
							<br />
							
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input id="btnLocalizar" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('tempFluxo1','1');AlterarAction('Formulario','<%=(request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")?"AnalisarVotoEmenta":"PreAnalisarVotoEmenta")%>');" />
								<input id="btnLimpar" class="formLocalizarBotao" type="submit" name="Limpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('tempFluxo1','0');AlterarAction('Formulario','<%=(request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")?"AnalisarVotoEmenta":"PreAnalisarVotoEmenta")%>');" />
							</div>
						</fieldset>
					</div>
					
					<div id="divLocalizar" class="divLocalizar"> 
			
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>"/>
					<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" />
					<input id="AnaliseConclusao" name="AnaliseConclusao" type="hidden" />
					
					<div id="divTabela" class="divTabela">
						<div style="height:33px">																			
							<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(0); return false;" title="Mostrar Todos" alt="Mostrar Todos" style="float:right"> Todos</button>
							<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(4); return false;" title="Antecipação de Tutela" alt="Antecipação de Tutela" style="float:right"> <img src='./imagens/16x16/imgPrioridade4.png'  /> <div class='botaoPrioridade' > Antecipação de Tutela</div></button>
							<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(4); return false;" title="Pedido de Liminar" alt="Pedido de Liminar" style="float:right"> <img src='./imagens/16x16/imgPrioridade4.png'  />     <div class='botaoPrioridade' >Pedido de Liminar</div></button>
					 		<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Réu Preso" alt="Réu Preso" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />             <div class='botaoPrioridade' >Réu Preso</div></button>
					 		<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Doença Grave" alt="Doença Grave" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />          <div class='botaoPrioridade' >Doença Grave</div></button>
					 		<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Maior de 60 Anos" alt="Maior de 60 Anos" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />      <div class='botaoPrioridade' >Maior de 60 Anos</div></button>					 	
					 		<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Maior de 80 Anos" alt="Maior de 80 Anos" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />      <div class='botaoPrioridade' >Maior de 80 Anos</div></button>							
							<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Adoção" alt="Adoção" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />     <div class='botaoPrioridade' >Adoção</div></button>
							<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Destituição de poder familiar" alt="Destituição de poder familiar" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />     <div class='botaoPrioridade' >Destituição de poder familiar</div></button>
							<button class="BotoesPrioridade" onclick="ListarPedidoUrgente(3); return false;" title="Pessoa com Deficiência" alt="Pessoa com Deficiência" style="float:right"> <img src='./imagens/16x16/imgPrioridade3.png'  />     <div class='botaoPrioridade' >Pessoa com Deficiência</div></button>       
					 	</div>
					 	<div id="divTabelaSelecao">
					 		<div id="divTabela" class="divTabela">
					 			<table id="TabelaSelecao" class="Tabela" >
									<thead >
										<tr >
											<th width="3%"> </th>
											<th width="3%"> </th>
											<th width="14%">Processo</th>
											<th width="15%">Data/Hora</th>
											<th width="65%">Tipo da Ação</th>
											<th width="14%" colspan="3" >Ações</th>																																											
										</tr>
									</thead>
									<tbody id="corpoSelecao">	
									</tbody>
								</table>
							</div>
					 	</div>
					 	
						<table id="Tabela" class="Tabela">
							<thead id="CabecalhoTabela">
								<tr class="TituloColuna">
									<th width="3%"> </th>
									<th width="3%"><input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" title="Marcar/Desmarcar todos os itens da lista" /> </th>
									<th width="20%">Processo</th>
									<th width="15%">Data/Hora</th>
									<th width="59%">Tipo da Ação</th>
									<th width="14%" colspan="3">Ações</th>									
								</tr>
							</thead>
							<%
							List liTemp = (List)request.getAttribute("ListaConclusao");
							PendenciaDt pendenciaDt;
							ProcessoDt processoDt;
							boolean boLinha=false; 
							long tipoClassificadortemp = -10;
							long tipoClassificador = 0;
							long tipoConclusaotemp = -10;
							long tipoConclusao = 0;
							if (liTemp !=null){
								for(int i = 0 ; i< liTemp.size();i++) {
									pendenciaDt = (PendenciaDt)liTemp.get(i); 
									processoDt = pendenciaDt.getProcessoDt();
									tipoConclusao = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
									
									int j=i+1;
									
									if (processoDt.getId_Classificador().length() > 0){
										tipoClassificador = Funcoes.StringToLong(processoDt.getId_Classificador());
									} else tipoClassificador = 0;
									
									
									//Testa a necessidade de abrir uma linha para o tipo de conclusão
									if (tipoConclusaotemp == -10){
										tipoConclusaotemp = tipoConclusao;
						
									%>
									<tr>
										<th colspan="8" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
									</tr>
									<%
									} else if (tipoConclusaotemp != tipoConclusao){
										tipoConclusaotemp = tipoConclusao;
										tipoClassificadortemp = -10;	
									%>
									<tr>
										<th colspan="8" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
									</tr>
									<%
									}//fim else
									
									//Testa a necessidade de abrir uma linha para o tipo de classificador
									if (tipoClassificadortemp == -10){
										tipoClassificadortemp = tipoClassificador;	
									%>
									<tr>
										<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador() != null && processoDt.getClassificador().trim().length()>0 && !processoDt.getClassificador().trim().equalsIgnoreCase("null")?processoDt.getClassificador():"Sem classificador")%> </th>
									</tr>
									<%
									}else if (tipoClassificadortemp != tipoClassificador){
										      tipoClassificadortemp = tipoClassificador;
									%>		
									<tr>
										<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador() != null && processoDt.getClassificador().trim().length()>0 && !processoDt.getClassificador().trim().equalsIgnoreCase("null")?processoDt.getClassificador():"Sem classificador")%> </th>
									</tr>
									<%
									} //fim else
									%>
										<% 
											String stUrgente = pendenciaDt.getNumeroImagemPrioridade();
											String mensagemUrgente = pendenciaDt.getProcessoPrioridadeCodigoTexto();
										%>
					   				<tr class='TabelaLinha<%=(boLinha?1:2)%> <%="Urgente" + stUrgente%> Urgente'> 
					   					<td align="center" width="3%"> <%=(i+1)%> </td>
										<td align="center" width="3%">
											<input class="formEdicaoCheckBox" name="pendencias" type="checkbox" value="<%=pendenciaDt.getId()%>">
										</td>									
										
	                   					<td align="left" width="20%">
	                   						<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>">
		                   						<%	if (stUrgente.length()==1){ %>		                   									
		                   							<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
		                   						<% } %>
		                   						<%=processoDt.getProcessoNumero()%>
		                   						&nbsp;<%=pendenciaDt.getProcessoTipoSessao()%>
	                   						</a>
					  					</td>
					  					<td align="left" width="15%"><%= pendenciaDt.getDataInicio()%></td>
	                   		           	<td align="left" width="59%"><%= pendenciaDt.getProcessoDt().getProcessoTipo()%></td>
	  	            							                   			
			                   			<td aling="center" width="14%">
			                   				<div id="opcoes" class="menuEspecial"> 
			                   					<ul> <li> Opções<ul>				
						  						<li> <a href="#" onclick="javascript: submeter2('PreAnalisarVotoEmenta', '<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>');return false;">Pré-Analisar</a></li>    			                   			
					                   			<!--li> <a href="#" onclick="javascript: submeter2('AnalisarVotoEmenta', '<%=pendenciaDt.getId()%>','<%=Configuracao.Excluir%>');return false;">Descartar</a></li-->		                   			
				                				</ul></li>
			                   					</ul>
			                   				</div>
			                   			</td>
			                   			
					  	     		</tr>
									<%
								}
							}
							%>
						</table>
					</div> 
				</form>
			</div>
			<%@ include file="Padroes/Mensagens.jspf" %>  
	</body>
</html>
