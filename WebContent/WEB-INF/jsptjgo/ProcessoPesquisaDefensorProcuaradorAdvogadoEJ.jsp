<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%><html>
	<head>
		<title>Busca de Processos pelos Dados do Defensor Público/Procurador/Advogado Escritório Jurídico</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="BuscaProcesso" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="id_UsuarioServentiaAtual" name="id_UsuarioServentiaAtual" type="hidden" value="" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos pelos Dados do Defensor Público/Procurador/Advogado Escritório Jurídico</legend>
                       	
					    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					    </label><br>
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=buscaProcessoDt.getId_Serventia()%>">  
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=buscaProcessoDt.getServentia()%>"/><br />
					    
					    <label class="formEdicaoLabel" for="Id_UsuarioServentia">Responsável Atual
				    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_UsuarioServentia" name="imaLocalizarId_UsuarioServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PosicaoPaginaAtual','0'); AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
		    			<input class="FormEdicaoimgLocalizar" name="imaLimparId_UsuarioServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_UsuarioServentia','UsuarioServentia'); return false;" title="Limpar Juiz Responsável">  
				    	</label><br>  				    	
		    			<input type="hidden" name="Id_UsuarioServentia" id="Id_UsuarioServentia" value="<%=buscaProcessoDt.getId_UsuarioServentia()%>">  	    					    				    			
		    			<input class="formEdicaoInputSomenteLeitura"  readonly name="UsuarioServentia" id="UsuarioServentia" type="text" size="80" maxlength="100" value="<%=buscaProcessoDt.getUsuarioServentia()%>"/><br />
					    <br />
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
						</div>
					</fieldset>		
				</div>
				
				<div id="divLocalizar" class="divLocalizar"> 
					<input type="hidden" id="Id_Processo" name="Id_Processo">
					
					<div id="divTabela" class="divTabela"> 
						<div align="left">
							<%if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
							<input name="imgMultipla" type="submit" value="Trocar Responsável" onclick="AlterarAction('Formulario','ProcessoProcuradorResponsavel');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('id_UsuarioServentiaAtual','<%=buscaProcessoDt.getId_UsuarioServentia()%>');AlterarValue('RedirecionaOutraServentia','6');"/>
							<% } %>
						</div>
						
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				            		<td class="colunaMinina">
				            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
							    				title="Alterar os estados de todos os itens da lista" />
							    	</td>
				                  	<td width="20%">N&uacute;mero</td>
				                  	<td width="40%">Nome Parte</td>
				                  	<td width="20%">Tipo de Parte</td>
				                  	<td width="20%">Distribuição</td>
				                  	<td class="colunaMinima">Selecionar</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								List liTemp = (List)request.getAttribute("ListaProcesso");
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
								  		<td><%=processoBuscaDt.getPoloAtivo()%></td>
			  	                		<td><%=processoBuscaDt.getOutraDescricaoGenerica()%></td>
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