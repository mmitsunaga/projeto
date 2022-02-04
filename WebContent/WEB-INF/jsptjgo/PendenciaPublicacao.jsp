<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
   <head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		
		<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Publicação </title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		<link rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
		
		<style type="text/css">
			.style1 {		
				margin-top: 15px;	
			}
		</style>
			
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	   	<script type="text/javascript" src="./js/tabelas.js"></script>
		<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
		<%@ include file="js/buscarArquivos.js"%>
		
		
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>		
		<%@ include file="./js/Paginacao.js"%>
						
		
	</head>
	
	<body>
	  <div  id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Publica&ccedil;&otilde;es</h2></div>
		
		<div id="divLocalizar" class="divLocalizar" >
			<form method="post" action="PendenciaPublicacao" id="Formulario"> 
				
			    <fieldset class="formLocalizar" id="fieldsetDadosProcesso" > 
			       <legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtrar</legend>
			       
			       <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			       <input type="hidden" id="Id_Serventia" name="Id_Serventia" value="<%=request.getAttribute("Id_Serventia")%>" />
			       
				   <div class="divEditar">
					   <div class="col60">
					   		<div class="col25">
							   <label>Serventia
							   <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
							   			onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'Serventia', 'Consulta de Serventia', 'Digite a Serventia e clique em consultar.', 'Id_Serventia', 'Serventia', ['Serventia'], ['Estado'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")" >
										
							    <input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"
										onclick="LimparChaveEstrangeira('Id_Serventia', 'Serventia'); return false;" 
										title="Limpar a serventia" />
									
							  </label>
									
							   <input class="formLocalizarInput" name="Serventia" readonly type="text" size="50" maxlength="50" id="Serventia" value="<%=request.getAttribute("Serventia")%>" />	
							</div>
					   </div>
						
				       <div class="col60">
					       <div class="col25">
							       <label>Data Inicial
							       <img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" 
							       		alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicial,'dd/mm/yyyy',this)" />
							       </label>
							       <input class="formLocalizarInput" name="dataInicial" id="dataInicial" type="text" value="" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" maxlength="60" title="Data inicial da publica&ccedil;&atilde;o" />
						   </div>
						   <div class="col25">   
									<label>Data Final 
									<img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" 
										alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)" />
									</label>
									<input class="formLocalizarInput" name="dataFinal" id="dataFinal" type="text" value="" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" maxlength="60" title="Data final da publica&ccedil;&atilde;o" />
							</div>	
						</div>
						<div class="col60 style1">
							<div class="col25">
								<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"
									onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;"
									title="Consultar as publica&ccedil;&otilde;es">
									
									Consultar
								</button>	
							</div>
						</div>	
				</div>	       
			</fieldset>
				   
		   <fieldset id="formLocalizar" class="formLocalizar"  > 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Validar Documento</legend>
		       	
		       	 <div class="divEditar">
					   <div class="col60">
					   		<div class="col25">
		       					<label class="formLocalizarLabel">Código de Validação</label><br>
		       					<input class="formLocalizarInput" name="codPublicacao" id="codPublicacao" type="text" value="" maxlength="32" title="Código do documento para validação" />
		   	   				</div>
		   	   			</div>
		   	   			 
		   	   			<div class="col60 style1">
								<div class="col25">
		   	   						<button type="submit" name="operacao" value="VerificarPublicacao" onclick="$('#Formulario').attr('target','_blank'); AlterarValue('PaginaAtual', '7');">Validar
									</button>
								</div>
						</div>
				</div>
		   	</fieldset>

			</form>
		</div>
			
				<div id="divTabela" class="divTabela" > 
				   	<table id="tabelaPublicacoes" class="Tabela">
				       	<thead>
				           	<tr>
				           		<th>Serventia</th>
				           		<th>Responsável</th>
								<th>Data da P&uacute;blica&ccedil;&atilde;o</th>
								<th class="colunaMinima">Selecionar</th>
							</tr>
						</thead>
						<tbody id="TabelaArquivos">
						<%if (request.getAttribute("ListaPendencia") != null){
							List lista = (List)request.getAttribute("ListaPendencia");
							Iterator it = lista.iterator();
							int qtd = 0;
							%>
								<%			
								while (it.hasNext()){
									PendenciaDt obj = (PendenciaDt)it.next();
								%>
									<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
										<td><%=obj.getServentiaUsuarioFinalizador()%></td>
										<td><%=obj.getNomeUsuarioFinalizador()%></td>
										<td><%=obj.getDataInicio()%></td>
										
										<td class="colunaMinima">
											<a href="PendenciaPublicacao?PaginaAtual=<%=Configuracao.Curinga6%>&amp;Id_Pendencia=<%=obj.getId()%>">
												<img src="imagens/22x22/ico_editar.png" alt="Abrir publicação" title="Abrir publicação" />
											</a> 
										</td>
								<%}%>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="8">Quantidade de p&uacute;blica&ccedil;&otilde;es: <span id="qtd"><%=lista.size()%></span></td>
								</tr>
							</tfoot>
						<%} else { %>
							<tbody>
								<tr>
									<td colspan="8">N&atilde;o h&aacute; p&uacute;blica&ccedil;&otilde;es</td>
								</tr>
							</tbody>
						<%}%>
					</table>
				</div>
		<%@ include file="Padroes/Paginacao.jspf"%>
		<%@ include file="Padroes/Mensagens.jspf"%>
	  </div>
	</body>
</html>