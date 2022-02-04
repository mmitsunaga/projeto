<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Autos Conclusos  </title>
		
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
				criarMenu('opcoes','Principal','menuA','menuAHover');
				$("#accordion").accordion({ autoHeight: true, collapsible: true, fillSpace:true });
				$(".BotoesPrioridade").hover(function(){ $(this).children('div').show("slow");  },function(){$(this).children('div').hide();   });
				$(".BotoesPrioridade > div").hide();
			} );
											  
	    	function submeter(action, id_Pendencia){
	    		AlterarAction('Formulario',action);
	    		AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		document.Formulario.submit();
	    	}
	    	
	    	function submeter2(action, id_Pendencia, paginaAtual){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		document.Formulario.submit();
	    	}
	    	
	    	function submeterEmentaVoto(action, id_Pendencia, paginaAtual, analiseConclusao){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		AlterarValue('AnaliseConclusao', analiseConclusao);
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
	    	
	    	function submeterClassificar(action, id_Pendencia, paginaAtual, preAnalise, fluxoEditar){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		AlterarValue('preAnaliseConclusao', preAnalise);
	    		AlterarValue('tempFluxo1', fluxoEditar);	    		
	    		document.Formulario.submit();
	    	}
	    </script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Autos Conclusos </h2></div>				
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input id="consultaClassificador" name="consultaClassificador" type="hidden" />

					<div id="divEditar" class="divEditar">
						<fieldset class="formEdicao"> 
						    <legend class="formEdicaoLegenda"> Consulta de Autos Conclusos </legend>
							<label id="labelProcesso" class="formLocalizarLabel"> Número do Processo </label><br> 
							<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getAttribute("numeroProcesso")%>" size="30" maxlength="60" />
							<br />
							
							<label class="formEdicaoLabel" for="Id_Classificador">Classificador
							 <input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('consultaClassificador', '2');AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						   	<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">  
							</label><br>  
						    <input type="hidden" name="Id_Classificador" id="Id_Classificador" value="<%=request.getAttribute("id_Classificador")%>" />
						   
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="70" maxlength="100" value="<%=request.getAttribute("classificador")%>"/>
							<br />
							
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input id="btnLocalizar" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('tempFluxo1','1');AlterarAction('Formulario','<%=(request.getAttribute("podeAnalisar") != null && request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")?"AnalisarConclusao":"PreAnalisarConclusao")%>');" />
								<input id="btnLimpar" class="formLocalizarBotao" type="submit" name="Limpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('tempFluxo1','0');AlterarAction('Formulario','<%=(request.getAttribute("podeAnalisar") != null && request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")?"AnalisarConclusao":"PreAnalisarConclusao")%>');" />
							</div>
						</fieldset>
					</div>
					
					<div id="divLocalizar" class="divLocalizar"> 
			
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>"/>
					<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" />
					<input id="AnaliseConclusao" name="AnaliseConclusao" type="hidden" />
					<input id="preAnaliseConclusao" name="preAnaliseConclusao" type="hidden" />
					<input id="FluxoEditar" name="FluxoEditar" type="hidden" />
					
					<div id="divTabela" class="divTabela">
						<div style="height:33px">
							<% 	if (request.getAttribute("podeAnalisar") != null && request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
								<input name="imgMultipla" type="submit" value="Análise Múltipla" onclick="AlterarAction('Formulario','AnalisarConclusao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
							<% } %>
							<% 	if (request.getAttribute("podeAnalisarMultipla") != null && request.getAttribute("podeAnalisarMultipla").toString().equalsIgnoreCase("true")){ %>
								<input name="imgMultipla" type="submit" value="Pré-Análise Múltipla" onclick="AlterarAction('Formulario','PreAnalisarConclusao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
							<% } %>
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
					 	<div id="accordion"> 	    					
							<%
							List liTemp = (List)request.getAttribute("ListaConclusao");
							PendenciaDt pendenciaDt;
							ProcessoDt processoDt;
							boolean boLinha=false; 
							long tipoClassificadortemp = -10;
							long tipoClassificador = 0;
							long tipoConclusaotemp = -10;
							long tipoConclusao = 0;
							String stId_Area="";
							if (liTemp !=null){
								for(int i = 0 ; i< liTemp.size();i++) {
									pendenciaDt = (PendenciaDt)liTemp.get(i); 
									processoDt = pendenciaDt.getProcessoDt();
									tipoConclusao = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
									
									int j=i+1;
									
									if (processoDt.getId_Classificador().length() > 0){
										tipoClassificador = Funcoes.StringToLong(processoDt.getId_Classificador());
									} else tipoClassificador = 0;
									
									if (stId_Area.length()!=0 && !processoDt.getId_Area().equalsIgnoreCase(stId_Area)){	%>
											</table>
										</div>
										<br>
										<br>
									<%}
									if (stId_Area.length()==0 || !processoDt.getId_Area().equalsIgnoreCase(stId_Area)){%>
										<h3><a href="#">Conclusões <%=processoDt.getArea()%></a></h3> <div>
										
										<table id="field<%=processoDt.getId_Area()%>" class="Tabela">
											<thead id="CabecalhoTabela">
												<tr class="TituloColuna">
													<th width="3%"> </th>
													<th width="3%"><input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'field<%=processoDt.getId_Area()%>')" title="Marcar/Desmarcar todos os itens da lista" /> </th>
													<th width="14%">Processo</th>
													<th width="15%">Data/Hora</th>
													<th width="65%">Tipo da Ação</th>
													<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
													<th width="8%" colspan="2">Analisar</th>
													<% } else { %>
													<th width="8%" colspan="2">Pré-Analisar</th>
													<% } %>							
													<th width="8%">Classificar</th>		
												</tr>
											</thead>
										
									<% 	stId_Area=processoDt.getId_Area();
									}
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
										if (tipoClassificadortemp == -10)
										{
											tipoClassificadortemp = tipoClassificador;	
									%>
										<tr>
									<!-- 	<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador().length()>0?processoDt.getClassificador():"Sem classificador")%> </th> -->
											<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (!processoDt.getClassificador().equals("null - (Prioridade: null)")?processoDt.getClassificador():"Sem classificador")%> </th>
										</tr>
									<%
										}else if (tipoClassificadortemp != tipoClassificador)
										{
											tipoClassificadortemp = tipoClassificador;
									%>		
											<tr>
												<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (!processoDt.getClassificador().equals("null - (Prioridade: null)")?processoDt.getClassificador():"Sem classificador")%> </th>
											</tr>
									<%
										}
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
										
	                   					<td align="center" width="14%">
	                   					<%
		                   					if (stUrgente.length()==1)
		                   					{ 
		                   						if (!pendenciaDt.getProcessoPrioridadeCodigo().contentEquals("0"))
			                   					{
	                   					%>
	                   							<img src='./imagens/16x16/imgPrioridade<%=pendenciaDt.getNumeroImagemPrioridade()%>.png' alt="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>" title="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>"/>
			                   					<a style="display: inline" href="AnalisarConclusao?PaginaAtual=<%=Configuracao.Curinga8%>&retirarPrioridadePendencia_Id=<%=pendenciaDt.getId()%>" border="0">
													<img src='imagens/16x16/edit-clear.png' alt="Retirar Prioridade" title="Retirar Prioridade" border="0">
												</a>
										<%
			                   					}
											}
										%>
	                   						<a style="display: inline" href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>">
		                   						<%=processoDt.getProcessoNumero()%>
	                   						</a>
					  					</td>
					  					<td width="15%" align="left"><%= pendenciaDt.getDataInicio()%></td>
	                   		           	<td width="65%" align="left"><%= pendenciaDt.getProcessoDt().getProcessoTipo()%></td>
	  	            							                   			
			                   			<td align="center" colspan="2">
			                   				<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
				                   					<a href="#" onclick="javascript: submeter2('AnalisarConclusao','<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>');return false;">
				                   						<img src='./imagens/32x32/btn_localizar.png' alt="Analisar" title="Analisar"/>				                   					
				                   					</a>				                   								                   					     			                   			
				                   			<% } else { %>
				                   					<a href="#" onclick="javascript: submeter2('PreAnalisarConclusao', '<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>');return false;">
				                   						<img src='./imagens/32x32/btn_localizar.png' alt="Pré-Analisar" title="Pré-Analisar"/>
				                   					</a>
				                   			<%  } %>
			                   			</td>
			                   			<td align="center">
			                   				<a href="#" onclick="javascript: submeterClassificar('DescartarPendenciaProcesso', '<%=pendenciaDt.getId()%>','<%=Configuracao.LocalizarDWR%>','false', '1');return false;">
		                   						<img src='./imagens/32x32/btn_atualizar.png' alt="Classificar" title="Classificar"/>
		                   					</a>
			                   			</td>
					  	     		</tr>
									<%
								}
							}
							%>
						</div>
					</div> 
				</form>
			</div>
			<%@ include file="Padroes/Mensagens.jspf" %>  
	</body>
</html>