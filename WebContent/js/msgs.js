/**
 * @author Ronneesley Moura Teles
 * @since 05/05/2008 - 14:54
 * Resolucao de mensagens
 */
$j(document).ready(function(){
	if ($j.trim($j("#MensagemOk").html()) == ""){
		$j("#MensagemOk").css("display", "none");
	}
	
	if ($j.trim($j("#MensagemErro").html()) == ""){
		$j("#MensagemErro").css("display", "none");
	}
});