<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
	<head>
		<title>Processo Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Processo de Execução - Consulta dados das Ações Penais </h2></div>
			
			<form action="ProcessoExecucao" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />

				<div id="divPortaBotoes" class="divPortaBotoes">
					<input id="imgImprimir"  class="imgImprimir" title="Imprimir Dados" name="imgImprimir" type="image" src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>');AlterarValue('PassoEditar','10');"/>	
		  		</div>

				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
						<table>
							<tr><td><div> Processo</div><span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span><br>
									<div> Serventia</div><span> <%=processoDt.getServentia()%> </a></span><br /><br />
									<div> Sentenciado</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNome()%></span><br>
									<div> Nome da Mãe</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNomeMae()%></span><br>
									<div> Data de Nascimento</div><span> <%=((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getDataNascimento()%></span>
								</td>
							</tr>
						</table>
					</fieldset><br /><br />


					<%List lista = (List)request.getSession().getAttribute("listaAcoesPenais"); 
					
					if (lista != null && lista.size() > 0){
						for (int i=0; i<lista.size();i++){
						ProcessoExecucaoDt processo = (ProcessoExecucaoDt)lista.get(i);%>
					<fieldset class="VisualizaDados"><legend class="VisualizaDados">Ação Penal <%=processo.getNumeroAcaoPenal()%></legend>
				   		<div style="width:240px">Comarca de Origem:</div> <span><%=processo.getCidadeOrigem() + "-" + processo.getUfOrigem()%></span><br />
				   		<div style="width:240px">Vara de Origem:</div> <span><%=processo.getVaraOrigem()%> </span><br /><br />
						<div style="width:240px">Distribuição da Ação Penal:</div> <span><%=processo.getDataDistribuicao()%></span>
						<div style="width:135px">Pronúncia:</div> <span><%=processo.getDataPronuncia()%></span><br />
						<div style="width:240px">Recebimento da Denúncia:</div> <span><%=processo.getDataDenuncia()%></span>
						<div style="width:135px">Arcórdão:</div> <span><%=processo.getDataAcordao()%></span><br />
						<div style="width:240px">Publicação da Sentença:</div> <span><%=processo.getDataSentenca()%></span>
						<div style="width:135px">Audiência Admonitória:</div> <span><%=processo.getDataAdmonitoria()%></span><br />
						<div style="width:240px">Trânsito em Julgado Acusação:</div> <span><%=processo.getDataTransitoJulgadoMP()%></span><br />
						<div style="width:240px">Trânsito em Julgado Defesa:</div> <span><%=processo.getDataTransitoJulgado()%></span><br />
						<div style="width:240px">Início de Cumprimento da Cond. (DICC):</div> <span><%=processo.getDataInicioCumprimentoPena()%></span><br />
						<br />
						<div style="width:240px">Pena:</div> <span><%=processo.getPenaExecucaoTipo()%></span><br />
						<div style="width:240px">Regime:</div> <span><%=processo.getRegimeExecucao()%></span><br />
						<div style="width:240px">Estabelecimento Penal:</div> <span><%=processo.getLocalCumprimentoPena()%></span><br />
						<div style="width:240px">SURSIS:</div> <span><%=processo.getTempoSursisAno() + "-" + processo.getTempoSursisMes() + "-" + processo.getTempoSursisDia()%></span><br />
						<br />
						<%List listaCondenacao = processo.getListaCondenacoes(); 
						if (listaCondenacao != null && listaCondenacao.size() > 0){%>
						<div>Condenações:</div><br />
						<%for (int j=0; j<listaCondenacao.size(); j++){ 
							CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt)listaCondenacao.get(j);%>
						<div>Crime:</div> <span style="width:800px"><%=condenacao.getCrimeExecucao()%></span><br />
						<div>Pena:</div> <span style="width:80px"><%=condenacao.getTempoPenaEmAnos()%></span>
						<div>Data do Fato:</div> <span style="width:50px"><%=condenacao.getDataFato()%></span>
						<div>Reincidente:</div> <span style="width:50px"><%=condenacao.isReincidente()?"Sim":"Não"%></span>
						<div>Extinção:</div> <span style="width:50px"><%=condenacao.getCondenacaoExecucaoSituacao()%></span><br /><br />
						<%}}%>
						<%List listaModalidade = processo.getListaModalidade(); 
						if (listaModalidade != null && listaModalidade.size() > 0){%>
						<div>Modalidades:</div><br />
						<%for (int j=0; j<listaModalidade.size(); j++){ 
							ProcessoEventoExecucaoDt modalidade = (ProcessoEventoExecucaoDt)listaModalidade.get(j);%>
						<div></div> <span><%=modalidade.getEventoRegimeDt().getRegimeExecucao()%></span><br />
						<%}}%>
					</fieldset><br /><br />
					<%}}%>

					<br />

				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>