<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/menusimples.css" type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>  
	 <script type="text/javascript">
	    	function submeter(action, id_Pendencia, paginaAtual, preAnalise){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		AlterarValue('preAnaliseConclusao', preAnalise);
	    		document.Formulario.submit();
	    	}
	    </script>
	    <script type="text/javascript">
			$(document).ready(function() {			
				criarMenu('opcoes','Principal','menuA','menuAHover');
			});			
		</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises Simples </h2></div>
		<div id="divLocalizar" class="divLocalizar"> 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda"> Consulta de Pré-Análises </legend>
					
						<label id="formLocalizarLabel" class="formLocalizarLabel"> Número do Processo </label><br> 
						<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getAttribute("numeroProcesso")%>" size="30" maxlength="60" />
						<br>
						
						<label class="formEdicaoLabel" for="Id_Classificador">Classificador
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">  
						</label><br>  
						<input type="hidden" name="Id_Classificador" id="Id_Classificador" value="<%=request.getAttribute("id_Classificador")%>" >
						
						<input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="70" maxlength="100" value="<%=request.getAttribute("classificador")%>"/>
						<br />
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input id="formLocalizarBotao" class="formLocalizarInput" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=AnalisePendenciadt.getFluxo()%>');" />
						</div>
					</fieldset>
				</div>
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" />
				<input id="preAnaliseConclusao" name="preAnaliseConclusao" type="hidden" />
				

				<div id="divTabela" class="divTabela" > 
					<table id="Tabela" class="Tabela">
						<thead>
							<tr class="TituloColuna">
								<th width="1%"></th>
								<th width="13%">Processo</th>
								<th width="10%">Data Início</th>								
								<th width="10%">Data Pré-Análise</th>
								<th width="30%">Usuário Pré-Análise</th>
								<th width="14%" colspan="3">Ações</th>								
							</tr>
						</thead>
					<tbody>
						<%
						List liTemp = (List)request.getAttribute("ListaPreAnalises");
						long tipoClassificadortemp = -10;
						long tipoClassificador = 0;
						long tipoConclusaotemp = -10;
						long tipoConclusao = 0;
						ProcessoDt processoDt;
						String estiloLinha="TabelaLinha1";
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
							PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)liTemp.get(i);
							PendenciaDt pendenciaDt = pendenciaArquivoDt.getPendenciaDt();
							processoDt = pendenciaDt.getProcessoDt();
							tipoConclusao = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
							
							int j=i+1;
							
							if (processoDt.getId_Classificador().length() > 0){
								tipoClassificador = Funcoes.StringToLong(processoDt.getId_Classificador());
							} else tipoClassificador = 0;

							//Testa a necessidade de abrir uma linha para o tipo de conclusão
							if (tipoConclusaotemp == -10){
								tipoConclusaotemp = tipoConclusao;
							%>
							<tr>
								<th colspan="8" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							} else if (tipoConclusaotemp != tipoConclusao){
								tipoConclusaotemp = tipoConclusao;
								tipoClassificadortemp = -10;
							%>
							<tr>
								<th colspan="8" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							}//fim else
							 
							
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
							<tr class="<%=estiloLinha%>">
								<td><%=i+1 %></td>
								<% 
									boolean boUrgente = pendenciaDt.isProcessoPrioridade();
								%>
	                   			<td align="center">
	                   				<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getProcessoDt().getId()%>" title="<%=(boUrgente?pendenciaDt.getProcessoPrioridadeCodigoTexto():"")%>">
		                   				<%	if (boUrgente){ %>		 
			                   			<img src='./imagens/16x16/imgPrioridade<%=pendenciaDt.getNumeroImagemPrioridade()%>.png' alt="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>" title="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>"/>
			                   			<% } %>
			                   			<%=pendenciaDt.getProcessoDt().getProcessoNumero()%>&nbsp;<%=(pendenciaDt.getProcessoDt().getProcessoTipo() != null && pendenciaDt.getProcessoDt().getProcessoTipo().trim().length() > 0 ? pendenciaDt.getProcessoDt().getProcessoTipo().trim() : "") %>)</a>
	                   				</a>
	                   			</td>	
	                   			<td align="center"><%=pendenciaArquivoDt.getPendenciaDt().getDataInicio()%></td>
	                   			<td align="center"><%=pendenciaArquivoDt.getDataPreAnalise()%></td>
	                   			<td><%=(pendenciaArquivoDt.getAssistenteResponsavel().length() > 0?pendenciaArquivoDt.getAssistenteResponsavel():pendenciaArquivoDt.getJuizResponsavel())%></td>
	                   			
	                   			<td aling="center">
			                   		<div id="opcoes" class="menuEspecial"> 
			                   			<ul> <li> Opções<ul>         											  						               				 
				                   				 <li> <a href="#" onclick="javascript: submeter('PreAnalisarVotoEmenta', '<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>','false');return false;">Refazer</a></li>    			                   			
						                   	     <li> <a href="#" onclick="javascript: submeter('PreAnalisarVotoEmenta', '<%=pendenciaDt.getId()%>','<%=Configuracao.Excluir%>','false');return false;">Descartar</a></li>		
			                   			</ul></li>
			                   			</ul>
			                   		</div>
			                   	</td>
	                   			
               				<% } 
               				}
               			%>
               			</tbody>
					</table>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div> 
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>  
	</div>
</body>
</html>