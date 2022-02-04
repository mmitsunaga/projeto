<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page  import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page  import="br.gov.go.tj.projudi.ne.PendenciaNe"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/tabelas.js"></script>
	<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
	
</head>
<body>
<div class="divCorpo">
	<form action="Pendencia" method="post" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="TipoPendencia" name="TipoPendencia" type="hidden" value="<%=request.getAttribute("TipoPendencia")%>" />
		<input id="fluxo" name="fluxo" type="hidden" value="0" />
		<input id="TipoPend" name="TipoPend" type="hidden" value="<%=request.getAttribute("TipoPend")%>" />
		<input id="opcao" name="opcao" type="hidden" value="<%=request.getAttribute("opcao")%>" />

		<%@ include file="Padroes/Mensagens.jspf"%>
		
		<div id="divLocalizar" class="divLocalizar" >
			<div class="area"><h2>&raquo; Pend&ecirc;ncias de Elaboração de Voto</h2></div>
			<fieldset id="formLocalizar" class="formLocalizar"> 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Opção</legend>		
				
				<button type="submit" name="opcao" value="AbertasServentiaCargo">
					<!-- <img src="imagens/22x22/btn_pesquisar.png" alt="Consultar" /> -->
					Consultar Todas
				</button>
		   	</fieldset>		
		
			<div id="divTabela" class="divTabela" >		
				<table id="Tabela" class="Tabela">
					<thead>
						<tr>
							<th class="colunaMinima lista_id">N&uacute;m.</th>
							<th class="colunaMinima">Processo</th>
							<th class="colunaMinima">Relator/Responsável</th>
							<th class="colunaMinima">Tipo</th>
							<th class="colunaMinima">Inicio</th>
							<th class="colunaMinima">Status</th>
							<th class="colunaMinima">Op&ccedil;&otilde;es</th>
						</tr>
					</thead>
	
					<%
					List lista = (List) request.getAttribute("ListaReservas");
					
					List<String> listaDatasSessoes = (List) request.getAttribute("listaSessoesElaboracao");
					String isTurma = (String)request.getAttribute("isTurma");				
					String dataSessaoTemp = "-10";
					String dataSessao = "";						

					long tipoClassificadortemp = -10;
					long tipoClassificador = 0;
					ProcessoDt processoDt;					
					PendenciaNe pendenciaNe = new PendenciaNe();
					
					
					%>
					<tfoot>
						<tr>
							<td colspan="10">Quantidade de reservas: <%=lista != null?lista.size():0%></td>
						</tr>
					</tfoot>
					
					<tbody>
						<%
						if (lista != null && lista.size() > 0 && listaDatasSessoes!=null){
							int qtd = 0;
							
							Iterator it = lista.iterator();
							int i = 0;							
							while(it.hasNext()){
								PendenciaDt pendenciaDt = (PendenciaDt)it.next();
								processoDt = pendenciaDt.getProcessoDt();
								
								tipoClassificador = Funcoes.StringToLong(processoDt.getId_Classificador(),0);
								
								dataSessao = (String) listaDatasSessoes.get(i);
								i++;
							
								if(isTurma != null && isTurma.equals("true") ){
								
									//Testa a necessidade de abrir uma linha para a Data da Sessão
									if (dataSessaoTemp == "-10"){
										dataSessaoTemp = dataSessao;	
									%>
										<tr>
											<th colspan="8" class="linhaDestaqueTitulo"> <%= dataSessao%> </th>
										</tr>
									<%
									}else if (!dataSessaoTemp.equals(dataSessao)){
										dataSessaoTemp = dataSessao;
										tipoClassificadortemp = -10;
									%>		
										<tr>
											<th colspan="8" class="linhaDestaqueTitulo"> <%= dataSessao%> </th>
										</tr>
									<%} //fim else
								}%>
							
							<%
							//Testa a necessidade de abrir uma linha para o tipo de classificador
							if (tipoClassificadortemp == -10){
								tipoClassificadortemp = tipoClassificador;	
							%>
								<tr>
									<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador().length()>0?processoDt.getClassificador():"Sem classificador")%> </th>
								</tr>
							<%
							}else if (tipoClassificadortemp != tipoClassificador){
								tipoClassificadortemp = tipoClassificador;
							%>		
								<tr>
									<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (processoDt.getClassificador().length()>0?processoDt.getClassificador():"Sem classificador")%> </th>
								</tr>
							<%
							} //fim else
							%>

							<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
								<td class="colunaMinima lista_id">
									<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar %>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">
										<%=pendenciaDt.getId()%>
									</a>
								</td>
								<td class="lista_data">
									<%if (pendenciaDt.getProcessoNumero()!= null && !pendenciaDt.getProcessoNumero().equals("")){%>
										<strong>
											<%=pendenciaDt.getProcessoNumero()%>
										</strong>
									<%} else { %>
										<b>--</b>
									<%}%>
								</td>
								<td  class="lista_data"  align="center" >
									<%=pendenciaDt.getNomeUsuarioCadastrador()%>
								</td>
								<td align="center"><%=pendenciaDt.getPendenciaTipo()%></td>
								<td class="lista_data"><%=pendenciaDt.getDataInicio()%></td>
								<td  align="center"><%=pendenciaDt.getPendenciaStatus()%></td>
								<td class="colunaMinima">
									<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar %>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">
										<img src="imagens/22x22/ico_solucionar.png" alt="Solucionar" title="Solucionar a pend&ecirc;ncia" />
									</a>
									
									
<!--                    							<div id="opcoes" class="menuEspecial">  -->
<!-- 			                   					<ul> <li> Opções<ul>	  	            					 -->
<%-- 				                   					<li> <a href="Pendencia?PaginaAtual=<%=Configuracao.Editar %>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">Solucionar Pendência</a></li>     			                   			 --%>
<%-- 				                   					<li> <a href="Pendencia?PaginaAtual=<%=Configuracao.Editar %>&amp;pendencia=<%=pendenciaDt.getId()%>&amp;CodigoPendencia=<%=pendenciaDt.getHash()%>&amp;NovaPesquisa=true">Guardar para Assinar</a></li> --%>
<!-- 			                   							</ul> -->
<!-- 			                   						</li>      -->
<!-- 			                   					</ul> -->
		                   					
<!-- 			                   				</div> -->
								</td>
							</tr>						
							<%
							}
						} else {
							%>
							<tr>
								<td colspan="10">N&atilde;o h&aacute; pend&ecirc;ncias reservadas</td>
							</tr>
							<%
						}
						%>
					</tbody>
				</table>
			</div>
		</div>		
  </form>
</div>
</body>
</html>
