<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaTipoDt"%>
<html>
	
	<head>
	
		<title>Processo Judicial</title>

		<link href="imagens/favicon.png" TYPE="image/gif" REL="icon">

		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

		<script language="javascript" type="text/javascript" src="./js/default.js"></script>
		<script type="text/javascript" src="./js/tecladoVirtual.js"></script>        
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type="text/javascript" src="./js/ui/jquery-ui.min.js"> </script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>	
		<script type="text/javascript" src="./js/jqDnR.js"> </script>     				
			
		<style type="text/css">

			@import url('css/Principal.css');
	   		@import url('css/LogonTJGO.css');
			
			dt {
				text-align: left;
				font-weight: bold;
			}
			dd {
				margin-left: 10px;
				text-align: left;
				border-bottom: 1px dotted #D7D7D7;		
			}
			#versao_projudi {
				text-align: right;
				font-family: arial;
				font-size: 10px;
				color: #066082;
				padding-right: 10px;
				padding-top: 5px;
			}

			#descricao_servidor {
				text-align: center;
				font-family: arial;
				font-size: 14px;
				color: #ff0000;
				padding-top: 10px;
			}

			#rodape {
				height: 25px;
				font-size: 10px;
				border-top: 1px solid #555555;
				background: #939393;
				color: #f9f9f9;
				text-align: center;
				padding-top: 10px;
				
			}

			#coluna-um, #coluna-dois, #coluna-tres {

				vertical-align: top;
			}

			#coluna-um, #coluna-tres {
				width: 50%;
				padding: 2px 2px 2px 2px;
			}
			
			#coluna-um {						
				float: left;
				border-collapse: collapse;
			}
			#coluna-tres {
				float:right;						
			}	

	
			#coluna-um h2, #coluna-dois h2, #coluna-tres h2 {
				width: 100%;
				height: 20px;
				padding-top: 9px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 15px;
				background-image: none;
				margin-right: 0px;
				margin-bottom: 0px;
				margin-top: 0px;				
				margin-left: 0px;
				background: #F6F6F6;
				font-weight: normal;
				clear: right;
				text-align: left;
				font-weight: bold;
				font-size: 11px;
				display: block;
				color: #494b49;
			}


			
			 #coluna-dois a {
				margin-top: 0px;				
				margin-right: 0px;
				margin-bottom: 0px;
				margin-left: 0px;
				padding-top: 0px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 0px;
			}

			#coluna-um ul, #coluna-dois ul {
				width: 95%;
				padding-top: 0pt;
				padding-right: 0pt;
				padding-bottom: 0pt;
				padding-left: 0pt;
				margin-top: 0px;				
				margin-right: 0px;
				margin-bottom: 25px;
				margin-left: 0px;
				list-style-type: none;
				list-style-image: none;
				list-style-position: outside;
			}

 			#coluna-um ul li {
				line-height: 30px;				
				margin-top: 0px;				
				margin-right: 0px;
				margin-bottom: 0px;
				margin-left: 0px;
				padding-top: 0px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 0px;
				border-bottom-width: 1px;
				border-bottom-style: dotted;
				border-bottom-color: #d7d7d7;
				background-color: transparent;
				background-image: none;
				background-repeat: repeat;
				background: #ffffff;
				text-align: left;
				padding-left: 12px;
				margin-left: 5px;
				background-color: transparent;
				background-repeat: no-repeat;
				background-attachment: scroll;
				background-position: 0pt 13px;
			}

 			
			#coluna-dois {
				width: 40%;
				float: right;
				

			}
			
			#coluna-dois h2, #coluna-um h2 {
				background: #FFFFFF;
				width: 90%;
				font-size: 20px;
				color:#1d4875;
				margin-bottom:25px;
			}

			#coluna-dois form {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #333333;
				width: 90%;
				padding-left: 50px;
			}

			#coluna-dois .textinput {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #333333;
				border: 1px solid #d5d5d5;
				width: 195px;
				height: 16px;
				margin: 0 0 10px 10px;
				padding: 8px 0 8px 5px;
				vertical-align: middle;
			}

			#coluna-dois .passwordinput {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #000000;
				background-color: #ffffff;
				border: 1px solid #d5d5d5;
				width: 195px;
				height: 16px;
				margin: 0 0 10px 10px;
				padding: 8px 0 8px 5px;
				vertical-align: middle;
			}

			#coluna-dois label {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 18px;
				color: #333333;
			
			}

			#coluna-dois .botao:hover {
				/*border: 1px solid #ccc;*/
				background-color: #287cc2;
				color: #fff;
			}

			#coluna-um ul li a {
				color: #333333;
				font-weight: normal;
			}
			
 			#coluna-um ul li a:hover {
				color: #3181c3 !important;
			}


			#coluna-tres h2 {
				width: 90%;
				padding-top: 5px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 15px;
				background-color: #f6f6f6;
				background-image: none;
				background-repeat: repeat;
				background-attachment: scroll;
				background-position: 0% 0%;
				letter-spacing: -0.5px;
				font-weight: bolder;
				color: #696969;
				margin-right: 0px;
				margin-bottom: 0px;
				margin-left: 0px;
				background: #F6F6F6;
				clear: right;
				background: #EFFAFF;
			}

			.mensagens {
				height: 100%;
				overflow: auto;
				color: #333;
				margin-left:50px;
				/*text-align: justify;*/
			}
			.texto {
				height: auto;
				width: 95%;
				padding: 3px;
				background: #EFFAFF;
				color: #333;
				text-align: justify;
			}				
			.mensagens p {				
				text-align: left;
			}	
			.mais_noticias{
				text-align: right;
				margin-top: 10px;
				}	
			.certificado{
				background-color: #183e65 !important;
				border: 0 !important;
			}
			.modal-bg{
				position:fixed;
				left:0;right:0;
				top:0;bottom:0;
				z-index:100;
				background-color:rgba(50,50,50,0.8);
				display:none;
			}		
			#modal{ 
				position:absolute;
				background-color:#e5e5e5;
				top:50%; 
				left:50%;
				z-index:9999;
				border-radius:3px;
				width:340px;
				height:160px;
				margin-top:-130px;
				margin-left:-170px;
				border-bottom: 3px solid #183e65;
				box-shadow:0 0 10px 0 rgba(0,0,0,0.3);
				display:none;
			}
			
			#modal span{
				display: block;
				background:#183e65;
				padding: 10px;
				color:#fff;
				
			}
			#close{
				float: right;
				color: #fff;
				font-family: serif;
				font-size: 15px;
			}
			#close:hover{
				color: #000;
			}
			.corpo-modal{
				padding:20px;
				margin: auto;
				text-align:center;
				
			}
			.logoTJ a{
				color:#fff !important;
			}
			.logoTJ a:hover{
				color:#fff !important;
			}
			.lista{
				margin:30px 0 0 0 !important;
			}
			.lista li{
				border: none !important;
				margin: 0 0 5px 30px !important;
				list-style: disc;
				line-height: normal !important;
				padding: 0 0 0 0 !important;
			}

			.lista li span{
				color: rgb(49, 129, 195);
				font-weight:600 !important;
			}
			.novopjd{
				margin: 0 0 50px 50px;
			}
			.novopjd img{
				border:1px solid #ddd;
			}
			
		</style>
        
	<script type="text/javascript">
		function testarNavegador(){
			var browserName = navigator.appName;
			
			if ( browserName == "Microsoft Internet Explorer" ) {
								
				$("#dialog").dialog( "option", "title", "Microsoft Internet Explorer - Não suportado");
				$("#dialog").html("Para a utilização do PROJUDI é necessário a Instalação do navegador Mozilla Firefox, pois, o Sistema é homologado somente para este navegador.");
				$('#dialog').dialog('open');
				return false;
			}		
		}
			
		$(document).ready(function() {
			
			  $("#chamateclado").click(function(evento){
			    $("#divTecladoVirtual").fadeToggle();	
				$("#senha").focus();
			  });
				
			  $("#wait").click(function(){
				  $("#modal").css("display","block");
				  $(".modal-bg").fadeIn();
			});
			
			$("#close").click(function(){
				  $(".modal-bg").fadeOut();		
				  $("#modal").fadeOut();
			  return false;
			});
			
			$("#accordion1").accordion({ autoHeight: false, collapsible: true });
			$("#accordion0").accordion({ autoHeight: false, collapsible: true });
			
			$( "#accordion0" ).accordion({
				  active: 0
			});
	  					
			mostraTecladoVirtual(); 
			tcvDefinirOpacity(); 						
					
	
		});
	
	</script>
	
		<meta name="keywords" content="projudi, processo judicial digital, processo judicial, tribunal de justiça do estado de goiás" />
		<meta name="description" content="Processo Judicial Digital" />
       
	</head>

	<body class="fundo">
	
	<%@ include file="/CabecalhoPublico.html" %>
		 	    
            <div class="divCorpo">

	<div id="coluna-um" >
	
	<h2><i class="fa fa-newspaper-o"></i> Notícias </h2>
		
		<div id="accordion0" class='mensagens' >
		    
				<%@include file="mensagens.html" %>			 		  
	
		</div>	
		<div class="mais_noticias"><a href="./noticias.html" target="_blank">+ notícias</a></div>				        	        			        	                                      	           
	</div>
	
	<div id="coluna-dois">

        <h2><i class="fa fa-sign-in"></i> Novo PJD 
	        <a href="http://www.cnj.jus.br/tecnologia-da-informacao/comite-nacional-da-tecnologia-da-informacao-e-comunicacao-do-poder-judiciario/modelo-nacional-de-interoperabilidade" target="_blank">
	        	<img src="imagens/mnimini.png" style="height: 17px" alt="Compatível com o Modelo Nacional de Interoperabilidade do CNJ" title="Compatível com o Modelo Nacional de Interoperabilidade do CNJ">
	        </a> 
	        <a href="https://projudi.tjgo.jus.br/IntercomunicacaoService?WSDL" target="_blank">
	        	<img src="imagens/brasaoGoias.png" style="height: 17px" alt="Modelo Nacional de Interoperabilidade do TJGO" title="Modelo Nacional de Interoperabilidade do TJGO">	        	
	        </a>
       </h2>
        <div style="float:right;margin:50px -5px -10px 0;"></div>
        <div class="novopjd">
				<a href="https://pjd.tjgo.jus.br/" target="_blank"><img src="./imagens/novopjd.jpg" alt="Advogado, avaliem a nova interface do Processo Judicial Digital"></a>
			</div>
        <h2><i class="fa fa-sign-in"></i> Acesso</h2>

			<form action="LogOn" method="post"  id="formLogin" OnSubmit="JavaScript:return testarNavegador()">
			<p>&nbsp;</p><p>&nbsp;</p>
			<jsp:include page="mapTecladoVirtual.html"/>
			
			<input type="hidden" name="PaginaAtual" value="7">
				
				<label for="login"><i class="fa fa-user"></i></label>

				<input name="Usuario" type="text" id="login" maxlength="20" class="textinput" placeholder="Usuário"/>

				<br />
				
				<label for="senha"><i class="fa fa-lock"></i> </label>

				<input name="Senha" type="password" id="senha" size="10" maxlength="25" class="passwordinput" placeholder="Senha"/>
				<div class="tooltip-container">
					<span style="font-size:18px; color:#999; vertical-align:middle; padding-bottom:10px">
						<a href="#" title="Teclado Virtual" id="chamateclado"><i class="fa fa-keyboard-o"></i></a>
						<span class="tooltip">Use o Teclado Virtual, ele é mais seguro e dificulta a entrada de vírus que capturam senhas.
							<br>Evite solicitar ajuda de estranhos e digitar sua senha na presença dos mesmos. <br> 
							<Strong>Lembre-se: </Strong> a senha de acesso é de sua total responsabilidade.
						</span>
					</span>
				</div>
				<br />
				
				<div style="display: none;" id="divTecladoVirtual">
					<img id="imgTecladoVirtual" src="./imagens/TecladoVirtual.png" usemap="#mapTecladoVirtual" alt="Teclado" align="center" style="vertical-align:top;margin-right:1px;" />
				</div>

				<div style="clear:both;height:2px;">&nbsp;</div>
				
				<div style=" margin-left:100px">
					<input type="submit" name="entrar" value="Entrar" class="botao" />
					<input type="reset" name="limpar" value="Limpar" class="botao" />
					<p ><a href="RecuperaSenha">Esqueceu a sua senha?</a></p>
				</div>
				<div style="clear:both;height:30px;">&nbsp;</div>

				<p style="margin-left:100px;font-weight:bold;"> -- OU -- </p>
                
                <div style="clear:both;height:30px;">&nbsp;</div>

				<!-- <input name="certificado" value="Acessar com certificado digital" class="botao" type="reset"> -->

  			</form>

	  		<form style="margin-left:13px" action="LogOn" method="post" id="formLoginCertificado">
				<input type="hidden" name="PaginaAtual" value="6">
		        <div>
		          	<input type="submit" class="certificado" value="Acessar com Certificado Digital"/>
		          	<div class="tooltip-container">	
		          	<span style="font-size:18px; color:#999; vertical-align:middle; padding-bottom:0">
						<a href="http://docs.tjgo.jus.br/projudi/pdf/ajuda-certificado.pdf" target="_blank" title="Ajuda - Certificado Digital"><i class="fa fa-question-circle"></i>
</a>
						<span class="tooltip">Tutorial para configuração do certificado digital no Mozilla Firefox
							
						</span>
					</span>
					</div>          
		        </div>
			</form> 
			<br>
			<form style="margin-left:13px" action="LogOn" method="post" id="formLoginCertificado">
				<input type="hidden" name="PaginaAtual" value="8">
		        <div>
		          	<input id="wait" type="submit" class="certificado" value="Cadastro com Certificado Digital"/>          
		        </div>
		        <%@ include file="/WEB-INF/jsptjgo/Padroes/reCaptcha.jspf" %>
			</form>
			
			
			
			<div style="clear:both;height:20px;">&nbsp;</div>
			<!-- <div><a href="#" id="wait">teste</a></div> -->                     

	</div>
	
	<div style="height:0px;clear:both;">&nbsp;</div>
		
	<div style="float:right;margin: 50px -2px -2px 0;"><a href="http://www.cnj.jus.br/tecnologia-da-informacao/comite-nacional-da-tecnologia-da-informacao-e-comunicacao-do-poder-judiciario/modelo-nacional-de-interoperabilidade" target="_blank"><img src="./imagens/mnimini.png" alt="Compatível com o Modelo Nacional de Interoperabilidade" title="Compatível com o Modelo Nacional de Interoperabilidade"></a></div>

	<div style="height:0px;clear:both;">&nbsp;</div>      
	
	
	
	<div class="modal-bg">
		<div id="modal">
			<span>Aguarde...<!-- <a href="#close" id="close">&#215;</a> --></span>
			<div class="corpo-modal">
				<img src="./imagens/loading_azul.gif" width="15%" height="15%"><p>Consultando base de dados da OAB</p>
			</div>
		</div>
	</div>
	
<%@include file="/WEB-INF/jsptjgo/Padroes/Mensagens.jspf"%>	

</body>
	
</html>
