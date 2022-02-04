<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt" %>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes" %>

<jsp:useBean id="Redistribuicaodt" class= "br.gov.go.tj.projudi.dt.RedistribuicaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<%@page import="br.gov.go.tj.projudi.ne.UsuarioNe"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%><html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>      	
	    <script type='text/javascript' src='./js/jquery.js'></script>
	    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>" />
				<input id="arquivarRedistribuir" name="arquivarRedistribuir" type="hidden" value="" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />
			
				<div class="area"><h2>&raquo; Consulta de Processos </h2></div>
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Selecione a Serventia para filtrar os processos</legend>
			    			<%if (UsuarioSessao.isDistribuidor()){ %>
				    			<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
				    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    			</label><br>
				    			<input class="formEdicaoInputSomenteLeitura" readonly name="Serventia" id="Serventia" type="text" size="80" maxlength="100" value="<%=Redistribuicaodt.getServentia()%>"/>	
				    			<input class="formEdicaoInputSomenteLeitura" readonly name="Id_Serventia" id="Id_Serventia" type="hidden" value="<%=Redistribuicaodt.getId_Serventia()%>"/>								
								<br />
							<%} %>
							
					    	<label class="formEdicaoLabel" for="Id_ServentiaCargo">Juiz Responsável
					    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
			    			<input class="FormEdicaoimgLocalizar" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" title="Limpar Juiz Responsável">
					    	</label><br>  
					    	<input type="hidden" name="Id_ServentiaCargo" id="Id_ServentiaCargo" value="<%=Redistribuicaodt.getId_ServentiaCargo()%>"/>
			    			
			    			<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
								<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="76" maxlength="100" value="<%=Redistribuicaodt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/>
							<% } else {%>
								<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="76" maxlength="100" value="<%=Redistribuicaodt.getServentiaCargo()%>"/>
							<% } %>
			    			<br>
			    			<label class="formEdicaoLabel">Tipo de Processo
			    			<input class="FormEdicaoimgLocalizar" name="imaLocalizarProcessoTipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
							<input class="FormEdicaoimgLocalizar" id="imaLimparProcessoTipo" name="imaLimparProcessoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ProcessoTipo','ProcessoTipo'); return false;" />
			    			</label><br>
							
							<input id="Id_ProcessoTipo" name="Id_ProcessoTipo" type="hidden" value="<%=Redistribuicaodt.getId_ProcessoTipo()%>"/>
							<input  class="formEdicaoInputSomenteLeitura" id="ProcessoTipo" name="ProcessoTipo" readonly="true" type="text" size="60" maxlength="60" value="<%=Redistribuicaodt.getProcessoTipo()%>"/><br />
			    			
			    			<label class="formEdicaoLabel" for="Id_Classificador">Classificador
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">
			    			</label><br>  
							<input type="hidden" name="Id_Classificador" id="Id_Classificador" value="<%=Redistribuicaodt.getId_Classificador()%>">
							
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="67" maxlength="100" value="<%=Redistribuicaodt.getClassificador()%>"/><br />
							
			    			<%if (UsuarioSessao.isDistribuidor()){ %>
								<input class="formEdicaoInput" name="Arquivado" id="Arquivado"  type="checkbox"  value="true" <%if (request.getAttribute("Arquivado") != null && request.getAttribute("Arquivado").toString().equals("true")){%> checked="true"<%}%>" ></input><label class="formEdicaoLabel" for="Arquivado">Arquivados</label><br />						
							<%} %>						
						
							<label class="formEdicaoLabel" for="qtdRegistros">Quantidade de registros</label>
							<select name="qtdRegistros" id="qtdRegistros" onchange="script:alterarEstadoNChecks('Tabela', 'checked', 'processos');">
								<option value="0">--</option>
								<option value="50">50</option>
								<option value="100">100</option>
								<option value="200">200</option>
								<option value="500">500</option>
								<option value="true">Todos</option>
							</select>
							
						
						</fieldset>
			 	</div>
				
				<div id="divLocalizar" class="divLocalizar"> 
			
					<input type="hidden" id="Id_Processo" name="Id_Processo">
  					<br />
  					
  					<div align="center">
  						<input name="imgSubmeter" type="submit" value="Consultar Processos" onclick="AlterarValue('PassoEditar','2')">
  						<%if (UsuarioSessao.isDistribuidor()){ %>
							<input name="imgMultipla" type="submit" value="Redistribuição em Lote" onclick="AlterarAction('Formulario','RedistribuicaoLote');AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');"/>
						<%} else { %>
							<input name="imgMultipla" type="submit" value="Redistribuir e Arquivar em Lote" onclick="AlterarAction('Formulario','RedistribuicaoLote');AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('arquivarRedistribuir','<%=1%>');"/>
						<%} %>
					</div>
				
					<div id="divTabela" class="divTabela"> 
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				            		<th class="colunaMinima"></th>
				            		<td class="colunaMinina">
				            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" title="Alterar os estados de todos os itens da lista" />
							    	</td>
				                  	<td width="16%">N&uacute;mero</td>
				                  	<td width="32%">Polo Ativo</td>
				                  	<td width="32%">Polo Passivo</td>
				                  	<td width="10%">Distribuição</td>
				                  	<td class="colunaMinima">Visualizar</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								List liTemp = (List)request.getAttribute("ListaProcesso");
								ProcessoDt processoBuscaDt;
								ProcessoParteDt processoParteDt;			
								String processoNumero="";
								boolean boLinha=false; 								
								//Percorre Lista Geral de Processos
								for(int i = 0 ;liTemp!=null && i< liTemp.size();i++) {
									processoBuscaDt = (ProcessoDt)liTemp.get(i); 
									String stUrgente = processoBuscaDt.getNumeroImagemPrioridade();
									String mensagemUrgente = processoBuscaDt.getProcessoPrioridadeCodigoTexto();%>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"> 
										<td > <%=i+1%></td>
										<td align="center">
											<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=processoBuscaDt.getId()%>">
										</td>
					                   	<td onclick="submete('<%=processoBuscaDt.getId()%>')">
						                   		<%	if (stUrgente.length()==1){ %>		 
						                   				<img src='./imagens/16x16/imgPrioridade<%=stUrgente%>.png' alt="<%=mensagemUrgente%>" title="<%=mensagemUrgente%>"/>
						                   		<% } %>
						                   		<%=processoBuscaDt.getProcessoNumero()%>
									  	</td>
					                   	
					                   	<td onclick="submete('<%=processoBuscaDt.getId()%>')">
									  		<ul class="partes">
									  	    <%
									  	    	List listaPromoventes = processoBuscaDt.getListaPolosAtivos();
									  	    						  	        	for (int y = 0; listaPromoventes != null && y < listaPromoventes.size(); y++){
									  	    						  	            	ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
									  	    %>
								  	                <li><%=promovente.getNome()%></li>
								  	   			<%}%>
									  		</ul>
									 	</td>
					  	            	<td onclick="submete('<%=processoBuscaDt.getId()%>')">
					  	            		<ul class="partes">
				  	                		<%
				  	                			List listaPromovidos = processoBuscaDt.getListaPolosPassivos();
				  	                			  	                				for (int y = 0; listaPromovidos != null && y < listaPromovidos.size(); y++){
				  	                			  	                					ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
				  	                		%>	
			  	                					<li><%=promovido.getNome()%> </li>
				  	                		<%}%>
				  	                		</ul>
				  	                	</td>
									  	<td onclick="submete('<%=processoBuscaDt.getId()%>')"><%= processoBuscaDt.getDataRecebimento()%></td>
					                   	<td align="center">
					                   		<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('Id_Processo','<%=processoBuscaDt.getId_Processo()%>');" >     
					                   	</td>
				               		</tr>
							<%}%>
				           	</tbody>
				       	</table>
				   	</div> 
				   	<%if(liTemp!=null){ %>
						<div id="Paginacao" class="Paginacao"> 
							<b>P&aacute;gina </b>
							<a href="RedistribuicaoLote?PassoEditar=2&PaginaAtual=<%=Configuracao.Localizar%>&amp;PosicaoPaginaAtual=0&amp;PassoBusca=<%=request.getAttribute("PassoBusca")%>">Primeira</a>
							<%
								long pagina = (Long)request.getAttribute("PosicaoPaginaAtual"); 
								long total = (Long)request.getAttribute("QuantidadePaginas");  	
								//Conta a qtde de registros que aparecerá em cada tela
								long loConte =1;
								//Guarda a última página selecionada
								long loConte1= pagina;
						
								//Determina quantas pag.serão necessarias de acordo com a qtde de registros.
								if ((total%500) != 0){
									total=total/500+1;
								}else{
									total=total/500;
								}
								
								//Se última pag.selecionada for maior q 5, entao a contagem começará da metade da qtde de registros q aparece por página
								if (pagina>(500/2)) 
									pagina-=(500/2); 
								else 
									pagina=1;
								//Aparecerá uma qtde de páginas a serem escolhidas, sendo que a ultima pag. escolhida ficara sempre marcada
								//Ex. : 1 2 3 4 5 |6| 7 8 9 10 (pag.escolhida 6)
								while(pagina<=total && loConte<=500){		 			   
									if ((pagina-1)==loConte1){ %> 
												<%="<b>| " + (pagina) + " |</b>"%>    					
									<%		} else { %>
												<a href="RedistribuicaoLote?PassoEditar=2&PaginaAtual=<%=Configuracao.Localizar%>&amp;PosicaoPaginaAtual=<%=(pagina-1)%>&amp;PassoBusca=<%=request.getAttribute("PassoBusca")%>"><%=pagina%></a>    
									<%		} 
									loConte++;
									pagina++;
								}	%>
								 
								<a href="RedistribuicaoLote?PassoEditar=2&PaginaAtual=<%=Configuracao.Localizar%>&amp;PosicaoPaginaAtual=<%=(total-1)%>&amp;PassoBusca=<%=request.getAttribute("PassoBusca")%>">Última</a>
								<input id="PosicaoPagina" name="PosicaoPagina" class="CaixaTextoPosicionar" value="<%=request.getAttribute("PosicaoPagina")%>" type="text" size="5" maxlength="10" />
								<input class="BotaoIr" type="button" value="Ir" onclick="javascript: submit();"/> 
						</div>
					<%}%>
					<br />
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
	<script type="text/javascript">
	
	function alterarEstadoNChecks(tabela, estado, nome){
		alterarEstadoChecks('Tabela', '', 'processos');
		
		if($("#qtdRegistros").val() == 'true') {
			alterarEstadoChecks('Tabela', 'checked', 'processos');
		} else {
			if (nome == null){
				filtro = "";
			} else {
				filtro = "[name*=" + nome + "]"
			}
			
			$("#" + tabela + " input[type=checkbox]" + filtro).slice(0,$("#qtdRegistros").val()).each(function(obj){
				//Somente marca os que possuem valores
				if (this.value != null && this.value != "")
					this.checked = estado;
			});
			
			alterarEstadoSelTodos(estado);
		}
		
	}
	
	</script> 
</html>