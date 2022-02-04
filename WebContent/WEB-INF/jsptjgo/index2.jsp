<%@page contentType="text/html"%>
<%@page pageEncoding="LATIN1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    	<title>EXECPENWEB</title>
   
       	<base target="userMainFrame"/>
                
        <%@include file="/css/menu.css"%>
        <link href="css/Principal.css" rel="stylesheet" type="text/css">
         
	   <style type="text/css"> #bkg_projudi { display:none } </style>
        
        <script type='text/javascript' src='./js/Funcoes.js'></script>
        <script type="text/javascript" src="./js/jquery.js"> </script>		   
		<script type="text/javascript" src="./js/projudi.js"></script>
		<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>   	
		<script type="text/javascript">
			var tempoMaximo = <%=request.getSession().getMaxInactiveInterval()%>;
    		var tempoAtual = tempoMaximo;
			$(document).ready(function() {			
				criarMenu('menu','Principal','menuA','menuAHover');
				contadorSessao('cronometro');
			});			
		</script>
		
        <script type="text/javascript">
        	
        //onload="Posicionar();" onResize="Posicionar();"
			function AlterarTamanho( objeto){
				//var a = document.getElementById("corpo").height;
				var a = objeto.contentDocument.body.clientHeight + 73;						
				var winW = 630, winH = 460;
				
				//winW = window.innerWidth;
				winH = window.innerHeight;			
	
				//alert(a);
				if (a>winW)
					objeto.height = a ;
				else  objeto.height = winH - 73;
				tempoAtual = tempoMaximo;				
			}
	    
			function Posicionar(){
				var $elementoCabecalho = $("div#Cabecalho");
				var $elementoWindow = $( window );
				$elementoCabecalho.css( 'left', ( ( $elementoWindow.width() / 2 ) - ( $elementoCabecalho.outerWidth( true ) / 2 ) ) + 'px' );
			}
			function testeJEsus(){
				alert('mudar tamanho' + $('Principal').height);
			}
		</script>

    </head>
    <body class="fundo_execpen">        
        <div class="Cabecalho" id="Cabecalho">	    
            <%@ include file="/Cabecalho_execpenweb._jsp" %>        
            <div id="menu" class="menu">
                   <%=request.getAttribute("Menu")%>
                   
                   <span id="cronometro" class='cronometro'></span>  
            </div>
          
        </div>
        <iframe id="Principal"  class="Tela" name="userMainFrame"  scrolling="no" align="top" frameborder="0" src="Usuario?PaginaAtual=-10" onload="AlterarTamanho(this)" ></iframe>				                 
    </body>
</html>
