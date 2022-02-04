<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<html>
	<head>
		<title>Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo;|<%=request.getAttribute("tempPrograma")%>| Alterar Partes no Processo</h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
				<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Alterar Partes no Processo</legend>
					    
					    <label class="formEdicaoLabel"> Processo </label><br>
					    <span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a> </span>
					    
				    	<!-- RECORRENTES -->
		  				<input type="hidden" id="posicaoLista" name="posicaoLista">
			  	
		  				<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> *Polo Ativo(s) </legend>
							<%
								List listaPartes = processoDt.getProcessoDependenteDt().getPartesProcesso();
				 	    			for (int i=0;i < listaPartes.size();i++){
				  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
				  			  			if (parteDt.getDataBaixa().length() == 0){
				 				%>
							
					   				<div>
						   				<input name="Recorrente" id="Recorrente" type="checkbox" value="<%=parteDt.getNome()%>" 
						   				<%	
						   					List listaRecorrentes = (List) request.getSession().getAttribute("ListaRecorrentes");
											if (listaRecorrentes != null && listaRecorrentes.size()>0){
								            	for(int j = 0 ; j< (listaRecorrentes.size());j++) {
								              		ProcessoParteDt obj = (ProcessoParteDt) listaRecorrentes.get(j);
								                   	if (obj.getNome().equals(parteDt.getNome())){%> 
								                   	checked
								        <% 			}
								               	}
											}
										%>
										/>
					   				</div> 
					       			<span><%=parteDt.getNome()%> </span>
					       		
					       			<div> CPF </div>
					        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<% 			}
				 	    			}
							%>
							<blockquote id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','6');"> 
				    		</blockquote>
						</fieldset>
		
						<fieldset id="VisualizaDados" class="VisualizaDados">
					   		<legend> *Polo Passivo(s) 
					   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarPartePoloPassivo" name="imaLocalizarPartePoloPassivo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('PassoEditar','0');" title="Cadastrar Recorrido" /> <br />
							</legend>
					 		<%
					 			List listaRecorridos = processoDt.getListaPolosPassivos();
					 					 			if (listaRecorridos != null && listaRecorridos.size()>0){
					 				            		for(int i = 0 ; i < (listaRecorridos.size());i++) {
					 					   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaRecorridos.get(i);
					 		%>
					    		<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
					       		<div> CPF </div><span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
					       		<%	if (parteDt.getId().length()> 0){
				      		    		if (parteDt.getDataBaixa().equals("")) { %>
					      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados Promovido">
							   			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>')" title="Dar baixa em Promovido"/><br />
									<%	} else { %>
								       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Promovido">
										Baixa em <%=parteDt.getDataBaixa()%>
										<br />
						       	<% 		}
				      		    	}
				            		}
				            	}  else {
					   		%>
					   		<em> Insira uma parte recorrida. </em>
					   		<% } %>
						</fieldset>
						<br />
						<br />
						
			    		
					</fieldset>
			
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>