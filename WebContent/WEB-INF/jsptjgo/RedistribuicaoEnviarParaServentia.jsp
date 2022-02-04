<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="Redistribuicaodt" class= "br.gov.go.tj.projudi.dt.RedistribuicaoDt" scope="session"/>


<html>
<head>
	<title>Envio de Processo para Conciliação/Mediação </title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	      
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Encaminhar Processo Para Outra Serventia </h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />				
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("TituloPagina")%>" />
				<input id="tempFluxo2" name="tempFluxo2" type="hidden" value="5" />
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Encaminhamento de Processo</legend>
						
						<%
							List processos = Redistribuicaodt.getListaProcessos();
						%>

						<fieldset class="formEdicao">
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th>N&uacute;mero Processo</th>
										<th>Data de Distribuição</th>
										<th>Serventia Origem</th>
										<% if (processos.size() > 1){ %>
										<th></th>
										<% } %>
									</tr>
								</thead>
								<%		
								if (processos != null){
								for (int i=0;i<processos.size();i++){
									ProcessoDt processoDt = (ProcessoDt)processos.get(i);
								%>
								<tbody>
									<tr class="primeiraLinha">
										<td align="center"><%=i+1%></td>
										<td width="15%" align="center"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></td>
										<td width="15%" align="center"><%=processoDt.getDataRecebimento()%></td>
										<td width="70%"><%=processoDt.getServentia()%></td>
										<% if (processos.size() > 1){ %>
				      					<td>
				      						<a href="Redistribuicao?PaginaAtual=<%=Configuracao.Excluir%>&Id_Processo=<%=processoDt.getId()%>&posicao=<%=i%>">
				      						<img name="btnRetirar" id="btnRetirar" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" />
				      						</a>
				      					</td>
				      					<% } %>
									</tr>
								</tbody>
								<% 	}
								} else { %>
								<tbody>
									<tr>
										<td><em>Selecione processo para encaminhar.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>		
						</fieldset>	
						
						<fieldset class="formEdicao">
						<legend>Serventia de Destino </legend>
						
							<label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Área de Encaminhamento
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('opcaoRedistribuicao','3');AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
			    			</label><br>  
			    			<input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="80" maxlength="100" value="<%=Redistribuicaodt.getAreaDistribuicao()%> "/><br />
			    			
			    			<label class="formEdicaoLabel" for="Id_Serventia">*Serventia
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('opcaoRedistribuicao','3');AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
			    			</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" readonly name="Serventia" id="Serventia" type="text" size="80" maxlength="100" value="<%=Redistribuicaodt.getServentia()%>"/><br />
							
						</fieldset>
						
						<br />
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					</fieldset>
					
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
    				<div id="divSalvar" class="divSalvar" class="divsalvar">
        				<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')"  > <br />
        				<div class="divMensagemsalvar">Confirma o encaminhamento do processo?</div>
      				</div>
 					<% } %>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>