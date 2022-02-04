<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
	<head>
		<title>Busca de Processo</title>	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>      	
	    <script type='text/javascript' src='./js/jquery.js'></script>
	    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>      	      
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; Consulta de Processos com Temas Transitados Julgados </h2></div>  			  		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="${PaginaAtual}" />				
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="${PaginaAnterior}">
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Gerar pendência "VERIFICAR PROCESSO COM TEMA(S) TRANSITADO JULGADO"</legend>
						<div>
							<input type="submit" onclick="AlterarValue('PaginaAtual','2');" value="Consultar processos" name="imgSubmeter">
							<c:if test="${ListaProcessos.size() > 0}">
								<input type="submit" onclick="AlterarValue('PaginaAtual','-2');" value="Gerar pendência" name="imgSubmeter">
							</c:if>
							<c:if test="${ListaProcessos.size() > 0}">
								<p><b>Os processos listados apresentam TODOS os temas com situação TRANSITADO JULGADO" ou "CANCELADO".</b></p>
							</c:if>
						</div>																	
						<div id="divTabela" class="divTabela">							
							<table id="Tabela" class="Tabela">
					        	<thead>
					            	<tr class="TituloColuna">
					            		<td width="8%" align="center">
					            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" 
					            			title="Alterar os estados de todos os itens da lista" ${isPodeGerarPendencia ? "" : "disabled=disabled"} />
								    	</td>
					                  	<td>N&uacute;mero Processo</td>
					                  	<td>Serventia</td>
					                  	<td>Data Sobrestamento</td>
					               	</tr>
					           	</thead>
					           	<tbody id="tabListaProcesso">
					           		<c:forEach var="item" items="${ListaProcessos}" varStatus="loop">
					           		<tr class="${loop.index % 2 == 0 ? 'TabelaLinha1' : 'TabelaLinha2'}">
					           			<td width="8px" align="center">
											<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="${item.id_Processo}" ${isPodeGerarPendencia ? "" : "disabled=disabled"}>
										</td>
					                   	<td><a href="BuscaProcesso?Id_Processo=${item.id_Processo}">${item.processoNumeroCompleto}</td>
					                   	<td>${item.serventia}</td>
					                   	<td>${item.dataTransitoJulgado}</td>
					           		</tr>
					           		</c:forEach>
					           	</tbody>
					           	<tfoot>
									<tr cellspacing="2">
										<td colspan="6">Quantidade de processos: <span id="qtd">${ListaProcessos.size()}</span></td>
									</tr>
								</tfoot>
				           	</table>
			           	</div>
			           	<br />
					</fieldset>
					<br />
					<div class="clear"></div>
					<div style="height:60px" align="center">
						<c:if test="${isPodeGerarPendencia}">
							<button name="imgConfirmarSalvar" value="Confirmar Gerar Pendência" onclick="Ocultar('divConfirmarSalvar'); AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')">
				               	<img src="./imagens/imgSalvar.png" alt="Confirmar Gerar Pendências">
				               	Confirmar
				            </button> <br />
				            <div class="divMensagemsalvar" style="margin:10px;">Clique para confirmar a criação das pendências</div>
						</c:if>
					</div>
				</div>				
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>		
	</body> 
</html>