<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
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
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>" />
				<input id="RedirecionaOutraServentia" name="RedirecionaOutraServentia" type="hidden" value="" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			
				<div class="area"><h2>&raquo; Consulta de Processos por Relator </h2></div>
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda"> Consulta de Processos por Relator </legend>
					    <label class="formEdicaoLabel" for="Id_Relator">*Relator 
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarRelator" name="imaLocalizarRelator" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						</label><br>  
						<input type="hidden" name="Id_Relator" id="Id_Relator" value="<%=buscaProcessoDt.getId_Relator()%>">
						<input class="formEdicaoInputSomenteLeitura"  readonly name="Relator" id="Relator" type="text" size="100" value="<%=buscaProcessoDt.getRelator()%>"/>
						<br />
						
						<label class="formEdicaoLabel" for="Id_ProcessoTipo">Tipo de Processo
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						</label><br>  
						<input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=buscaProcessoDt.getId_ProcessoTipo()%>">
						<input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="100" maxlength="100" value="<%=buscaProcessoDt.getProcessoTipo()%>"/>
						<br />
					</fieldset>
				</div>
		
				<div id="divLocalizar" class="divLocalizar"> 
					<input type="hidden" id="Id_Processo" name="Id_Processo">
					
					<div id="divTabela" class="divTabela"> 
						<div align="left">
	  						<% 	if (request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>
							<input name="imgMultipla" type="submit" value="Movimentação em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
							<% }if (request.getAttribute("podeRedistribuir").toString().equalsIgnoreCase("true")){ %>
							<input name="imgMultipla" type="submit" value="Redistribuição em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','2');"/>
							<% }if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
							<input name="imgMultipla" type="submit" value="Trocar Responsável" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','6');"/>
							<% } %>
						</div>
						
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				            		<td class="colunaMinina">
				            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
							    				title="Alterar os estados de todos os itens da lista" />
							    	</td>
				                  	<td width="10%">N&uacute;mero</td>
				                  	<td width="35%">Polo Ativo</td>
				                  	<td width="35%">Polo Passivo</td>
				                  	<td width="10%">Distribuição</td>
				                  	<td class="colunaMinima">Selecionar</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								List liTemp = (List)request.getAttribute("ListaProcessos");
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
				           	<tfoot>
								<tr>
									<td colspan="6">Quantidade de processos: <span id="qtd"><%=contProcessos%></span></td>
								</tr>
							</tfoot>
				       	</table>
   					</div> 
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body> 
</html>