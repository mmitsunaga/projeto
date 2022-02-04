<%@page contentType="text/html"%>
<%@page pageEncoding="LATIN1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.ne.UsuarioNe"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
    	<title>Processo Judicial</title>
    	<link rel="shortcut icon" href="./imagens/favicon.png">
   
       	<base target="userMainFrame"/>
                
        <%@include file="/css/menu.css"%>
        <link href="css/Principal.css" rel="stylesheet" type="text/css">
         
	   <style type="text/css"> #bkg_projudi{ display:none } </style>
        
        <script type='text/javascript' src='./js/Funcoes.js'></script>
        <script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>     
		<script type="text/javascript" src="./js/projudi.js"></script>
		<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>   
			
		<script type="text/javascript">
			var tempoMaximo = <%=request.getSession().getMaxInactiveInterval()%>;
    		var tempoAtual = tempoMaximo;					       
        	var navegacao_arquivos;

        	websocket = null;		
			var conectado = false;	
			var tentativas = 0;
			const WEBSOCKET_CLOSE_STATE = 3
			
<%-- 			var isAdvogado = <%=((UsuarioNe)request.getSession().getAttribute("UsuarioSessao")).isAdvogado()%>; --%>
			
			function isWebSocketConnect(){
				if (websocket==null){
					conectar();					
				}
				
				if (websocket.readyState == WEBSOCKET_CLOSE_STATE){				   
				   return false;
				}	
				return true;
			}
			
			function conectar(){

				if ('WebSocket' in window) {
					websocket = new WebSocket("wss://127.0.0.1:8443/events/");
				} else if ('MozWebSocket' in window) {
					websocket = new WebSocket("wss://127.0.0.1:8443/events/");
				} else {
					alert("Browser não suporta WebSocket");
				}
		
				if (websocket == undefined || websocket== null) {
					alert("Não foi possivel conectar no Serviço de Assinatura Digital e Impressão.\n Verifique se o Serviço de Assinatura Digital e Impressão S.A.I., está rodando e com o serviço iniciado.");
					return;
				}
				
				websocket.onopen = function() {
					conectado = true;
					console.log("Conectou com sucesso");
				};
		
				websocket.onclose = function() {
					console.log("Desconectou com sucesso");
					websocket==null;
					conectado = false;
				};
		
				websocket.onerror = function(evt) {
					console.log("Aconteceu um erro " + evt.data);
					if (!conectado){
						tentativas++
						if (tentativas>=2){
							window.open('./certificado_sai.html','_blank');
						}else{
							window.open('https://docs.tjgo.jus.br/projudi/jar/sai.jnlp','_blank');
							console.log("Baixando o Assinador " + evt.data);
							setTimeout(function() { conectar();	}, 10000);
						}
					}else{
						mostrarMensagemErro("Erro ",  evt.data);
					}
				};		
			}
        	
        	$(document).ready(function() {
        		criarMenu('menu','Principal','menuA','menuAHover');
				contadorSessao('cronometro');
// 				if (isAdvogado){
// 					conectar();
// 				}
        	});
                
			function Posicionar(){
				var $elementoCabecalho = $("div#Cabecalho");
				var $elementoWindow = $( window );
				$elementoCabecalho.css( 'left', ( ( $elementoWindow.width() / 2 ) - ( $elementoCabecalho.outerWidth( true ) / 2 ) ) + 'px' );
			}

			function tratarEventoLoadIframe(){
				tempoAtual = tempoMaximo;
				calcularTamanhoIframe();
			}
			
		</script>

    </head>
    <body class="fundo">        
        <div class="Cabecalho" id="Cabecalho">	    
            <%@ include file="/Cabecalho.html" %>   
                 
	            <div id="menu" class="menu">
	                   <%=request.getAttribute("Menu")%>
	                   
	                   <span id="cronometro" class='cronometro' title="Tempo para encerramento da sessão"></span> 
	            </div>
          	
        </div>
        <iframe id="Principal"  class="Tela" name="userMainFrame"  scrolling="no" align="top" frameborder="0" src="Usuario?PaginaAtual=-10" onload='tratarEventoLoadIframe();' ></iframe>
        				                 
    </body>
</html>
