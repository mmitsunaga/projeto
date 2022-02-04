<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>

<jsp:useBean id="AudienciaMovimentacaoDt" class= "br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>

<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<html>
<head>
	<title>Movimentar Processo</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      	  
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      
      <%@ include file="./js/MovimentacaoProcesso.js" %>
      
      <%@ include file="./js/InsercaoArquivo.js"%>
</head>

	<body onLoad="atualizarPendencias(); atualizarArquivos('false');">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
			<input id="ElaboracaoVoto" name="ElaboracaoVoto" type="hidden" value="false">
			<input id="SalvarRedistribuir" name="SalvarRedistribuir" type="hidden" value="">
			<!-- Variáveis para controlar Passos da Movimentação -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso1()%>">
			<%if (!AudienciaMovimentacaoDt.isIgnoraEtapa2Pendencias()){%>			
			<input id="Passo2" name="Passo2" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso2()%>">
			<% } %>
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso3()%>">
			<input id="SomentePreAnalisadas" name="SomentePreAnalisadas" type="hidden" value="<%=request.getAttribute("SomentePreAnalisadas")%>">
		
			<div id="divEditar" class="divEditar">
				<% if (!AudienciaMovimentacaoDt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
				<% } %>
				<%if (!AudienciaMovimentacaoDt.isIgnoraEtapa2Pendencias()){%> 
					<% if (!AudienciaMovimentacaoDt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<% } %>
				<% } %>
				<% if (!AudienciaMovimentacaoDt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>')">
				<% } %>
				
				<%
					AudienciaDt audienciaDt = AudienciaMovimentacaoDt.getAudienciaDt();
					AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
					boolean podeAnalisar_preAnalisar = audienciaProcessoDt.podeAnalisarOuPreanalisarVotoEmenta(String.valueOf(request.getAttribute("GrupoTipoUsuarioLogado")));
				%>
				
				<fieldset class="formEdicao">
					<legend>Confirmação Dados</legend>				
						<label class="formEdicaoLabel"> Data Audiência </label><br>
						<span class="spanDestaque"><%=audienciaDt.getDataAgendada()%></span>
						<br />
						
						<label class="formEdicaoLabel"> Processo </label><br>
						<span class="spanDestaque">
							<%if(request.getSession().getAttribute("processos") != null) {
								List listaProcessos = (List)request.getSession().getAttribute("processos");
								for (int i = 0; i < listaProcessos.size(); i++) { 
									ProcessoDt processoTemp = (ProcessoDt)listaProcessos.get(i); %>
									<a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=processoTemp.getId()%>"><%=processoTemp.getProcessoNumero()%></a>
									<%if(i < listaProcessos.size() - 1) {%>
									,&nbsp;
									<%} %>
								<% } 									
							  } else { %>
								<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>"><%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%></a>
							<%} %>
						</span>
						<br />			
										
						<label class="formEdicaoLabel">Status</label><br>  
						<span class="spanDestaque"><%=AudienciaMovimentacaoDt.getAudienciaStatus()%></span/>
						<br />
						
						<%if(request.getAttribute("SegundoGrau") !=null &&  String.valueOf(request.getAttribute("SegundoGrau")).equalsIgnoreCase("true")){%>						
							<label class="formEdicaoLabel">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br>  
							<span class="destaque"><%=AudienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")?"Sim":"Não"%></span>
							<br />
						<%}%>	
						
						<%if (!podeAnalisar_preAnalisar && (UsuarioSessao.isSegundoGrau() || UsuarioSessao.isGabineteSegundoGrau() || UsuarioSessao.isGabineteFluxoUPJ()) && !(AudienciaMovimentacaoDt.isMovimentacaoSessaoAdiada() || AudienciaMovimentacaoDt.isMovimentacaoSessaoIniciada())){%>
							<label class="formEdicaoLabel">Presidente</label><br>  
							<span class="spanDestaque"><%=(request.getSession().getAttribute("NovoServentiaCargoPresidente")!= null?String.valueOf(request.getSession().getAttribute("NovoServentiaCargoPresidente")):"")%></span/>
							<br />
							<label class="formEdicaoLabel">MP Serventia</label><br>  
							<span class="spanDestaque"><%=(request.getSession().getAttribute("NovaSeventiaMP")!= null?String.valueOf(request.getSession().getAttribute("NovaSeventiaMP")):"")%></span/>
							<br />
							<label class="formEdicaoLabel">MP Responsável</label><br>  
							<span class="spanDestaque"><%=(request.getSession().getAttribute("NovoServentiaCargoMP")!= null?String.valueOf(request.getSession().getAttribute("NovoServentiaCargoMP")):"")%></span/>
							<br />
							<label class="formEdicaoLabel">Por Maioria </label><br>  
							<span class="destaque"><%=(AudienciaMovimentacaoDt.isVotoPorMaioria()?"Sim":"Não")%></span>
							<br />
							<%if(AudienciaMovimentacaoDt.isVotoPorMaioria()){%>
							<label class="formEdicaoLabel">Redator </label><br>  
							<span class="destaque"><%=AudienciaMovimentacaoDt.getServentiaCargoRedator()%></span>
							<br />
							<%}%>
						<%}%>	
						
					<%if (!AudienciaMovimentacaoDt.isPreAnalise()){%>				
					<fieldset class="formLocalizar"> 	
						<legend>Arquivos Inseridos</legend>
						<div id="divTabela" class="divTabela">
							<table class="Tabela">
								<thead>
							    	<tr>
							      		<th width="40%">Descrição</th>
							      		<th width="40%">Nome</th>			  
							    	</tr>
							  	</thead>
								<% if (AudienciaMovimentacaoDt.getListaArquivos() != null && AudienciaMovimentacaoDt.getListaArquivos().size() > 0){ %>							  	
							  	<tbody id="corpoTabelaArquivo">
									<tr id="idArquivo" style="display:none;">
									  	<td><span id="tableDescricao">Descrição</span> </td>
								      	<td><span id="tableNome">Nome Arquivo</span></td>			  	  							    
								    </tr>
							  	</tbody>
							  	<% } else { %>
								<tbody>
								   	<tr>
								  		<td><em>Nenhum arquivo foi adicionado.</em></td>			  	  							    
								   	</tr>
							  	</tbody>								    
								<% } %>
							</table>
						</div>	
					</fieldset>
					<%} else {%>
					<fieldset class="formLocalizar">	
						<legend>Pré-Análise Relatório e Voto <input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
						                                          onclick="MostrarOcultar('divTextoEditor'); return false;" title="Abrir Editor de Texto" />
						</legend>			
						<div id="divTextoEditor" class="divTextoEditor" style="display:none">
							<%=AudienciaMovimentacaoDt.getTextoEditor() %>
						</div>
					</fieldset>
					
						<%if(!UsuarioSessao.isTurmaJulgadora()){%>
							<fieldset class="formLocalizar">	
								<legend>Pré-Análise Ementa <input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
								 								onclick="MostrarOcultar('divTextoEditorEmenta'); return false;" title="Abrir Editor de Texto" />
								 </legend>						
								<div id="divTextoEditorEmenta" class="divTextoEditor" style="display:none">
									<%=AudienciaMovimentacaoDt.getTextoEditorEmenta() %>
								</div>
							</fieldset>
							
						<%} %>
					
					<% } %>							
							
					<fieldset class="formLocalizar"> 	
						<legend>Lista das pendências</legend>
						<div id="divTabela" class="divTabela">
							<table class="Tabela">
								<thead>
							    	<tr>
							      		<th>Tipo</th>
							      		<th>Destinatário</th>
								  		<th style="display:none">Serventia/Usuário</th>
								  		<th>Prazo</th>
								  		<th class="colunaMinima">Urgente</th>
									  	<th class="colunaMinima">Intimação</th>
							    	</tr>
							  	</thead>
							  	<% if (AudienciaMovimentacaoDt.getListaPendenciasGerar() != null && AudienciaMovimentacaoDt.getListaPendenciasGerar().size() > 0){ %>
							  	<tbody id="corpoTabela">
							    	<tr id="identificador" style="display:none;">
							      		<td><span id="tableTipo">Tipo</span> </td>
							      		<td><span id="tableDestinatario">Destinatário</span></td>
							  	  		<td style="display:none"><span id="tableSerUsu">Usuário/Serventia</span></td>
							     		<td><span id="tablePrazo">Prazo</span></td>
								  		<td><span id="tableUrgente">Urgente</span></td>
								  		<td><span id="tableIntimacao">PessoalAdvogados</span></td>
							  	  		<td style="display:none"><span id="tableOnLine">Online</span></td>
							    	</tr>
							  	</tbody>
							  	<% } else { %>
								<tbody>
									<tr>
											<td><em>Nenhuma Pendência será gerada.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>
						</div>				
					</fieldset/>
					<br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<%if (UsuarioSessao.isMagistrado() && !AudienciaMovimentacaoDt.isPreAnalise()) { %>
						<input name="imgConcluir" type="submit" value="Concluir Análise" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');">
						<%} %> 						
						<%if (UsuarioSessao.isPodeExibirPendenciaAssinatura(false,  PendenciaTipoDt.CONCLUSO_RELATOR) && !UsuarioSessao.getUsuarioDt().isTurmaJulgadora()  ){ %>
							<input name="imgGuardar" type="submit" value="Guardar para Assinar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','2');" />						
						<%} %> 
						<%
							String textoBotaoSalvar = "Salvar";
							if (podeAnalisar_preAnalisar) {
								textoBotaoSalvar = "Salvar Pré-Análise";
							}
						%>

						<%if (UsuarioSessao.getUsuarioDt().isTurmaJulgadora() && AudienciaMovimentacaoDt.isPreAnalise() ){ %>
							<input name="imgConcluirPreanalise"  value="Salvar Pré-Análise Elaboração de Voto" type="submit" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','1'); AlterarValue('ElaboracaoVoto','true');" />
						<%}  %>
						
						<%if (!UsuarioSessao.getUsuarioDt().isTurmaJulgadora() || ( (!AudienciaMovimentacaoDt.isPreAnalise()) && (!UsuarioSessao.isMagistrado()) ) ){ %>
							<%if (UsuarioSessao.isPodeTrocarResponsavelDistribuicao()){ %>
								<input name="imgConcluirDistribuirPreanalise"  value="<%=textoBotaoSalvar + " e Distribuir"%>" type="submit" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','1'); AlterarValue('SalvarRedistribuir','true');" />
							<%}%>
							<input name="imgConcluirPreanalise" type="submit" value="<%=textoBotaoSalvar%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','1');" />
					 	<%}  %>
					 	
					 	<% if (podeAnalisar_preAnalisar && 
					 		   AudienciaMovimentacaoDt.getPendenciaArquivoDtEmenta() != null && 
					 		   AudienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId() != null &&
					 		  AudienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId().trim().length() > 0) {%>
					 		  
					 		<input name="imgDescartarPreanalise" type="submit" value="Descartar Pré-Análise" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('tempFluxo1','3');" />
					 	<% } %>
					</div>
				</fieldset>
			</div>
		</form>
 	</div>	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>


