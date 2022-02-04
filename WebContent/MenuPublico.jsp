<%@page import="br.gov.go.tj.projudi.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%
ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
%>
<li class="has-sub">
	<a href="#" title="Consulta"><span><i class="fa fa-search"></i></span></a>
	<ul>
		<li><a href="./BuscaProcessoPublica?PaginaAtual=<%=Configuracao.Novo%>" title="Consultar Processos de 1&ordm e 2&ordm grau"> P&uacute;blica processual &nbsp;<i class="fa fa-search"></i></a></li>			
		<li><a href="./ConsultaPublicacao" title="Consultar Publica&ccedil;&otilde;es">Publica&ccedil;&otilde;es &nbsp;<i class="fa fa-search"></i></a></li>	
		<li><a href="./ConsultaJurisprudencia" title="Consultar Jurisprud&ecirc;ncias">Jurisprud&ecirc;ncias &nbsp;<i class="fa fa-search"></i></a></li>
		<li><a href="./BuscaProcessoPublica?PaginaAtual=<%=Configuracao.Curinga9%>&tipoConsultaProcesso=1" title="Consultar Processos por Advogado"> Processo por Advogado &nbsp;<i class="fa fa-search"></i></a></li>		
     	<li><a href="./BuscaProcessoCodigoAcesso?PaginaAtual=<%=Configuracao.Novo%>" title="Consultar Processos por C�digo de Acesso"> Processo por C�digo &nbsp;<i class="fa fa-search"></i></a></li>
		<li><a href="ListaJuizados" title="Lista das Serventias com Processo Judicial implantado" > Serventias Implantadas &nbsp;<i class="fa fa-search"></i></a></li>			
		<li><a href="/RelatorioInterrupcoes?PaginaAtual=<%=Configuracao.Novo%>"&Sistema=1 title="Relat&oacute;rio de Interrup&ccedil;&otilde;es"> Relat&oacute;rio de Interrup&ccedil;&otilde;es &nbsp;<i class="fa fa-search"></i></a></li>
		<li><a href="./PendenciaPublica" title="Valida��o de Documentos"> Valida��o de Documentos &nbsp;<i class="fa fa-search"></i></a></li>		
		<li><a href="./PendenciaPublica?PaginaAtual=2" title="Intima��es do Dia Anterior">Intima&ccedil;&otilde;es do Dia Anterior&nbsp;<i class="fa fa-search"></i></a></li>				
	</ul>
</li>
<li class="has-sub">
	<a href="#" title="Emitir e Validar Certid�es Nada Consta"><span><i class="fa fa-file-text"></i></span></a> 
	<ul>
    	<li class="has-sub">
        	<a href="#" title="Certid�es de 1� Grau"><span> Certid�es - 1� Grau &nbsp;<i class="fa fa-file-text"></i></span></a>
            <ul>
            	<li><a href="./CertidaoNegativaPositivaPublica?PaginaAtual=1&TipoArea=1&InteressePessoal=&Territorio=&Finalidade=" title="Emiss&atilde;o de Certid&otilde;es Pessoa F&iacute;sica - C�vel"> Pessoa F&iacute;sica - C&iacute;vel &nbsp;<i class="fa fa-file-text"></i></a></li>
            	<li><a href="./CertidaoNegativaPositivaPublica?PaginaAtual=1&TipoArea=2&InteressePessoal=S" title="Emiss&atilde;o de Certid&otilde;es Pessoa F&iacute;sica - Criminal"> Pessoa F&iacute;sica - Criminal &nbsp;<i class="fa fa-file-text"></i></a></li>
				<li><a href="./CertidaoNegativaPositivaPublicaPJ?PaginaAtual=1&TipoArea=" title="Emiss&atilde;o de Certid&otilde;es Pessoa Jur&iacute;dica"> Pessoa Jur&iacute;dica &nbsp;<i class="fa fa-file-text"></i></a></li>
				<li><a href="./CertidaoNarrativaComCustasPublica?PaginaAtual=9" title="Certid�o Narrativa com Custas"> Certid�o Narrativa com Custas &nbsp;<i class="fa fa-usd"></i></a></li>
            </ul>
        </li>
        <li class="has-sub">
        	<a href="#" title="Certid�es de 2� Grau"><span> Certid�es - 2� Grau &nbsp;<i class="fa fa-file-text"></i></span></a>
            <ul>
            	<li><a href="./CertidaoSegundoGrauNegativaPositivaPublica?PaginaAtual=1" title="Emiss&atilde;o de Certid&otilde;es Pessoa F&iacute;sica">Pessoa F&iacute;sica &nbsp;<i class="fa fa-file-text"></i> </a></li>
				<li><a href="./CertidaoSegundoGrauNegativaPositivaPublicaPJ?PaginaAtual=1" title="Emiss&atilde;o de Certid&otilde;es Pessoa Jur&iacute;dica">Pessoa Jur&iacute;dica &nbsp;<i class="fa fa-file-text"></i></a></li>
            </ul>
        </li>
        <li><a href="./CertidaoPublica" title="Validar Certid&otilde;es"> Valida��o de Certid�es &nbsp;<i class="fa fa-file-text"></i></a></li>
	</ul>
</li>
<li class="has-sub">
	<a href="#" title="Emitir Guias"><span><i class="fa fa-usd"></i></span></a> 
    <ul>
    	<li><a href="./GerarBoleto?PaginaAtual=4" title="Gerar Boleto">Gerar Boleto &nbsp;<i class="fa fa-usd"></i></a></li>
    	<li><a href="./GuiaLocomocaoPublica?PaginaAtual=9" title="Guia Locomo&ccedil;&atilde;o"> Guia Locomo&ccedil;&atilde;o &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaLocomocaoComplementarPublica?PaginaAtual=9" title="Guia Locomo&ccedil;&atilde;o Complementar"> Guia Locomo&ccedil;&atilde;o Complementar &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaInicial1GrauPublica?PaginaAtual=4" title="Guia Inicial para o 1� e 2� Grau"> Guia Inicial 1� e 2� Grau &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaInicialComplementarPublica?PaginaAtual=4" title="Guia Complementar da Inicial"> Guia Complementar da Inicial &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaPostagemPublica?PaginaAtual=9" title="Guia Postagem"> Guia Postagem &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaRecursoInominadoPublica?PaginaAtual=9" title="Guia Recurso Inominado"> Guia Recurso Inominado &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaCertidaoPraticaForensePublica?PaginaAtual=9" title="Guia de Certid�o de Pr�tica Forense"> Guia de Certid�o de Pr�tica Forense &nbsp;<i class="fa fa-usd"></i></a></li>
		<li><a href="./GuiaCertidaoNarrativaPublica?PaginaAtual=9" title="Guia de Certid�o Narrativa"> Guia de Certid�o Narrativa &nbsp;<i class="fa fa-usd"></i></a></li>
		
<!-- 		<li><a href="./GuiaMediacaoPublica?PaginaAtual=9" title="Guia Media&ccedil;�o"> Guia Media&ccedil;&atilde;o - 1� Grau &nbsp;<i class="fa fa-usd"></i></a></li> -->
<!-- 		<li><a href="./GuiaConciliacaoPublica?PaginaAtual=9" title="Guia Concilia&ccedil;&atilde;o"> Guia Concilia&ccedil;&atilde;o - 1� Grau &nbsp;<i class="fa fa-usd"></i></a></li> -->
<!-- 		<li><a href="./GuiaResolucaoConflitoPublica?PaginaAtual=9" title="Guia de Concilia��o/Media��o"> Guia de Concilia��o/Media��o - Semana de Concilia��o &nbsp;<i class="fa fa-usd"></i></a></li> -->
    </ul>
</li>
<li class="has-sub">
	<a href="#" title="Estat�stica P�blica"><span><i class="fa fa-bar-chart"></i></span></a> 
    <ul>
    	<li><a onclick="buscaEstatistica(1)" href="#" > Quantitativo de Processos &nbsp;<i class="fa fa-bar-chart"></i></a></li>
		<!--<li><a  onclick="buscaEstatistica(2)" href="#" >Quantitativo de Tipos de Processos<i class="fa fa-bar-chart"></i></a></li>-->
		<li><a onclick="buscaEstatistica(3)" href="#">Quantitativo de Serventias &nbsp;<i class="fa fa-bar-chart"></i></a></li>
	</ul>
</li>
<%@ include file="/MenuSistema.html" %>


<script type="text/javascript">

	function buscaEstatistica(_consulta){

		var reCaptcha = $("#g-recaptcha-response").val();		
		//window.location = '/EstatisticaPublica?PassoBusca=2&EstatisticaPublicaTipoConsulta='+_consulta+'&g-recaptcha-response='+ reCaptcha;
		window.open('/EstatisticaPublica?PassoBusca=2&EstatisticaPublicaTipoConsulta='+_consulta+'&g-recaptcha-response='+ reCaptcha, "_self");
		
	}
</script>