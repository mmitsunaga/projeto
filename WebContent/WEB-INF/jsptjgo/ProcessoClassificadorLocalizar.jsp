<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
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
      	
		<script type='text/javascript' src='./js/ProcessoClassificador.js?v=14122020'></script>
		<script type='text/javascript' src='./js/buscaJson.js'></script>
		
		<script type='text/javascript'>
		function validaForm() {
			if( $("[name=processos]:checked").length < 1 &&  $("[name=TodosProcessosClassificados]:checked").length < 1){
				mostrarMensagemErro('Projudi - Erro', 'Selecione ao menos um processo.');
				return;
			}
			document.getElementById('Formulario').submit();
		}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
				<input id="ConsultaResponsavelPrimeiroGrau" name="ConsultaResponsavelPrimeiroGrau" type="hidden" value="<%=request.getAttribute("ConsultaResponsavelPrimeiroGrau")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="RedirecionaOutraServentia" name="RedirecionaOutraServentia" type="hidden" value="" />
			
				<div class="area"><h2>&raquo; Consulta de Processos por Classificador </h2></div>
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id="fieldsetDadosProcesso"> 
					    <legend class="formEdicaoLegenda"> Consulta de Processos por Classificador, Classe e Assunto </legend>
						<%if(!UsuarioSessao.isDesembargador()){ %>
							<br />
							<label class="formEdicaoLabel" for="Id_Classificador">Classificador
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'Classificador', 'Consulta de Classificadores', 'Digite o Classificador e clique em consultar.', 'Id_Classificador', 'Classificador', ['Classificador'], ['Prioridade'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">
							</label><br />  
							<input type="hidden" name="Id_Classificador" id="Id_Classificador" value="<%=buscaProcessoDt.getId_Classificador()%>">
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="100"  value="<%=buscaProcessoDt.getClassificador()%>"/>
							<br />
						<%} %>
						<label class="formEdicaoLabel" for="Id_ProcessoTipo">Tipo do Processo
   					    <input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=buscaProcessoDt.getId_ProcessoTipo()%>">
					    <input type="hidden" name="ProcessoTipoCodigo" id="ProcessoTipoCodigo" value="<%=buscaProcessoDt.getProcessoTipoCodigo()%>">
					     <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('fieldsetDadosProcesso','ProcessoTipo','Consulta de Tipos de Processo', 'Digite o Tipo do Processo e clique em consultar.', 'Id_ProcessoTipo', 'ProcessoTipo', ['ProcessoTipo'], [], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" />
					    <input class="FormEdicaoimgLocalizar" name="imaLimparProcessoTipoCodigo" type="image"  src="./imagens/16x16/edit-clear.png" onClick="LimparChaveEstrangeira('Id_ProcessoTipo','ProcessoTipo'); av('ProcessoTipoCodigo','null'); return false;" title="Limpar Processo Tipo">					    
					    </label> <br />
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="100" value="<%=buscaProcessoDt.getProcessoTipo()%>"/><br />
					    <label class="formEdicaoLabel" for="Id_ProcessoTipo">Assunto 
   					    <input type="hidden" name="Id_Assunto" id="Id_Assunto" value="<%=buscaProcessoDt.getId_Assunto()%>">					                          
					     <input class="FormEdicaoimgLocalizar" id="imaLocalizarAssunto" name="imaLocalizarAssunto" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('fieldsetDadosProcesso','Assunto','Consulta de Assuntos', 'Digite o Assunto e clique em consultar.', 'Id_Assunto', 'Assunto', ['Assunto'], ['Código do Assunto'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" />
					    <input class="FormEdicaoimgLocalizar" name="imaLimparAssunto" type="image"  src="./imagens/16x16/edit-clear.png" onClick="LimparChaveEstrangeira('Id_Assunto','Assunto'); return false;" title="Limpar Assunto">
					    </label> <br />
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Assunto" id="Assunto" type="text" size="100" value="<%=buscaProcessoDt.getAssunto()%>"/><br />
				    	<label class="formEdicaoLabel" for="Id_ServentiaCargo">Magistrado Responsável                                                                                       
				    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('fieldsetDadosProcesso','ServentiaCargo','Consulta de Magistrado Responsável', 'Digite o Nome e clique em consultar.', 'Id_ServentiaCargo', 'ServentiaCargo', ['Serventia Cargo'], ['Nome','CargoTipo'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" />
				    	<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" title="Limpar Juiz Responsável">
				    	</label><br />
				    	<input type="hidden" name="Id_ServentiaCargo" id="Id_ServentiaCargo" value=""/>
    	    			<input class="formEdicaoInputSomenteLeitura"  readonly name="Nome" id="Nome" type="text" size="103" maxlength="100" value=""/><br />

						<div class="clear space"></div>				
						<button id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Localizar" value="Consultar"
							onclick="buscaDados('0', 500); return false;"
							title="Consultar as publica&ccedil;&otilde;es">
							Consultar
						</button>
						
					</fieldset>
										
					<input type="hidden" id="Id_Processo" name="Id_Processo">
					<div align="left">
						<input class="formEdicaoCheckBox" name="TodosProcessosClassificados" type="checkbox" value="1">Todos os Classificados </br>
  						<% 	if (request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>	  					
						<input name="imgMultipla" type="button" value="Movimentação em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');validaForm();"/> 						
						<% }if (request.getAttribute("podeRedistribuir").toString().equalsIgnoreCase("true")){ %>
						<input name="imgMultipla" type="button" value="Redistribuição em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','2');validaForm();"/>
						<% }if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
						<input name="imgMultipla" type="button" value="Trocar Responsável" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','6');validaForm();"/>
						<% }if (request.getAttribute("podeEncaminhar").toString().equalsIgnoreCase("true")){ %>
						<input name="imgMultipla" type="submit" value="Encaminhar Processos" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','8');"/>
						<% } %>
					</div>
						
					<div id="divTabela" class="divEditar"> 				
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            		<tr class="TituloColuna">
				            		<td></td>
				            		<td class="colunaMinina">
				            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
							    				title="Alterar os estados de todos os itens da lista" />
							    	</td>
				                  	<td width="10%">Processo</td>
				                  	<td width="35%">Polo Ativo</td>
				                  	<td width="35%">Polo Passivo</td>
				                  	<td width="10%">Distribuição</td>
				                  	<td class="colunaMinima">Selecionar</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								List liTemp = (List)request.getAttribute("ListaProcessos");
								liTemp = null;
								ProcessoDt processoBuscaDt;
								String processoNumero="";
								int contProcessos = 0;
								boolean boLinha=false; 								
								//Percorre Lista Geral de Processos
								if (liTemp != null) {
									for(int i = 0 ; i< liTemp.size();i++) {
										processoBuscaDt = (ProcessoDt)liTemp.get(i); 
							%> 
									 
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"> 
										<td align="center">
											<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=processoBuscaDt.getId()%>">
										</td>
					                   	<td><%=processoBuscaDt.getProcessoNumero()%></td>
					                   	
					                   	<td>
									  		<ul class="partes">
									  	    <%
									  	    	List listaPromoventes = processoBuscaDt.getListaPolosAtivos();
									  	    							  	    	if (listaPromoventes != null){
									  	    							  	        	for (int y = 0; y < listaPromoventes.size(); y++){
									  	    							  	            	ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
									  	    %>
									  	                <li><%=promovente.getNome()%></li>
									  	   	<%		}
									  	     	}	%>
									  		</ul>
									 	</td>
					  	            	<td>
					  	            		<ul class="partes">
				  	                		<%
				  	                			List listaPromovidos = processoBuscaDt.getListaPolosPassivos();
				  	                				  	                			if (listaPromovidos != null){
				  	                				  	                				for (int y = 0; y < listaPromovidos.size(); y++){
				  	                				  	                					ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
				  	                		%>	
				  	                					<li><%=promovido.getNome()%> </li>
				  	                		<%
				  	                				}
				  	                			}  %>
				  	                		</ul>
				  	                	</td>
									  	<td><%= processoBuscaDt.getDataRecebimento()%></td>
									  	     	
					                   	<td align="center">
					                   		<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('Id_Processo','<%=processoBuscaDt.getId_Processo()%>');" >     
					                   	</td>
				               		</tr>
							<%
									contProcessos++;
									}
								}
							%>
				           	</tbody>
				       	</table>
				       
				       	<div id="Paginacao" class="Paginacao"></div>
				       	</div>
					</div> 				       	
   					
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body> 
</html>