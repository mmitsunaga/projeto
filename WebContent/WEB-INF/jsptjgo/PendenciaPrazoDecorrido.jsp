<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Pend&ecirc;ncia</title>

    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
    
	<link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/checks.js'></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$("#nomeBusca").focus();
		});
		
		//Preenche o campo filtro e clica no botao
		function filtrar(numero){
			$("#nomeBusca").val(numero);
			
			$("#formLocalizarBotao").trigger("click");
		}
	</script>
      
	<%@ include file="./js/Paginacao.js"%>	
	<%@ include file="js/PendenciaPrazoDecorrido.js"%>
	
</head>
<body>
  <div class="divCorpo">
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
			<div class="area"><h2>&raquo; Pendências com Prazos Decorridos da Serventia</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
		<input name="fluxo" type="hidden" value="2" />
		<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
		<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />
		
		<%@ include file="Padroes/Mensagens.jspf"%>
		
		<div id="divLocalizar" class="divLocalizar">
			<fieldset id="formLocalizar" class="formLocalizar"  > 
				<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtro</legend>
				
		       	<label class="formLocalizarLabel">Processo
		       	<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('numeroProcesso', ''); return false;" title="Limpar o n&uacute;mero do processo" />
		       	</label><br>
		       	
		       	<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getParameter("numeroProcesso") != null?request.getParameter("numeroProcesso"):""%>" maxlength="60" />
		       
		       	<br />
				
				<input type="hidden" id="Id_PendenciaTipo" name="Id_PendenciaTipo" value="<%=request.getAttribute("Id_PendenciaTipo")%>" />
				<label class="lbl">Tipo de Pend&ecirc;ncia
				<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
					onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
							AlterarValue('tempBuscaId', 'Id_PendenciaTipo'); 
							AlterarValue('tempBuscaDescricao', 'PendenciaTipo');" 
								title="Selecione a pend&ecirc;ncia" />
				
				<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"
					onclick="LimparChaveEstrangeira('Id_PendenciaTipo', 'PendenciaTipo'); return false;" 
								title="Limpar a Tipo da pend&ecirc;ncia" />
				</label><br>
				
				
				<input class="somenteLeitura" name="PendenciaTipo" id="PendenciaTipo" readonly type="text" 
					size="50" maxlength="50" value="<%=request.getAttribute("PendenciaTipo")%>" />	
				
				<br />
				
				<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"
					onclick="buscaDados(0, <%=Configuracao.TamanhoRetornoConsulta %>); return false;"
					title="Consultar as pendências">
				<!--	<img src="imagens/22x22/btn_pesquisar.png" alt="Consultar publica&ccedil;&otilde;es" /> -->
					Consultar
				</button>	
				
				<button class="formLocalizarBotao" type="submit" name="operacao" value="MarcarVisto"
					title="Marcar Visto">
					<!--  <img src="imagens/22x22/btn_marcar_leitura.png" alt="MarcarVisto" /> -->
					Marcar Visto
				</button>
			</fieldset>
	   
			<div id="divTabela" class="divTabela" > 
			   	<table id="Tabela" class="Tabela">
			       	<thead>
			           	<tr>
			           		<th class="colunaMinima">
			           			<input type="checkbox" id="selecionarTodos" onclick="atualizarChecks(this, 'Tabela')" />
			           		</th>
			           		<th class="colunaMinima">Num.</th>
			                <th>Processo</th>
			                <th width="150">Tipo</th>
			                <th>Movimenta&ccedil;&atilde;o</th>
			                <th width="85" align="center">Data Limite</th>
			                <th class="colunaMinima">Detalhes</th>
						</tr>
					</thead>
					
					<%if (request.getAttribute("ListaPendencia") != null){
						List lista = (List)request.getAttribute("ListaPendencia");
						Iterator it = lista.iterator();
						int qtd = 0;
						%>
		
						<tfoot>
							<tr>
								<td colspan="8">Quantidade de pend&ecirc;ncias: <span id="qtd"><%=lista.size()%></span></td>
							</tr>
						</tfoot>
				          	
						<tbody id="tabListaDados">
							<%			
							while (it.hasNext()){
								PendenciaDt obj = (PendenciaDt)it.next();
							%>
								<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
									<td class="colunaMinima">
										<input type="checkbox" name="pendenciaSelecionada[]" value="<%=obj.getId()+' '+obj.getHash()%>" />
									</td>
									<td class="colunaMinima lista_id"><%=obj.getId()%></td>
									<td width="160" align="center">
										<%if (!obj.getId_Processo().equals("")){%>
											<a href="BuscaProcesso?Id_Processo=<%=obj.getId_Processo()%>">
												<%=obj.getProcessoNumero()%>
											</a>
										<%}%>
									</td>
									<td><%=obj.getPendenciaTipo()%></td>
									<td><%=obj.getMovimentacao()%></td>
									<td class="lista_data" width="85"><%=obj.getDataLimite()%></td>
									<td class="colunaMinima">
										<a href="Pendencia?PaginaAtual=6&amp;pendencia=<%=obj.getId()%>&amp;fluxo=4&amp;CodigoPendencia=<%=obj.getHash()%>&amp;op=PrazoDecorrido" 
											title="Ver detalhes da pend&ecirc;ncia n&uacute;mero <%=obj.getId()%>">
											<img src="imagens/imgLocalizarPequena.png" alt="Selecionar" />
										</a>
									</td>
								</tr>
							<%}%>
						</tbody>
					<%} else { %>
						<tbody>
							<tr>
								<td colspan="7">N&atilde;o h&aacute; pend&ecirc;ncias</td>
							</tr>
						</tbody>
					<%}%>
				</table>
				
				<%@ include file="./Padroes/Paginacao.jspf"%>
			</div>
		</div>
  </form>
 </div>
</body>
</html>