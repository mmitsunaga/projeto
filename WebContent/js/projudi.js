/**
 * Informacoes do projdui
 * @author Ronneesley Moura teles
 * @since 12/09/2008 11:10
 */
var Projudi = function(url, buscar){
 	if (buscar == null) buscar = false;
 
 	this.url = url;
 	
 	//Se deseja buscar as informacoes
 	if (buscar) this.buscar(true);
};
 
Projudi.prototype = {
 	//Versao da classe
 	versaoClasse: "1.0", 
 	
 	//URL onde sera buscado as mensagens
 	url: null,
	 
	//Ultimo xml requisitado
	xml: null, 	
	
	//Versao do projudi
	versao: null,
 	
 	/**
 	 * Busca por js o conteudo das mensagens em XML
 	 * @author Ronneesley Moura Teles 	 
 	 * @since 12/09/2008 09:55
 	 * @param boolean as, caso deseje associar logo em seguida da busca
 	 */
 	buscar: function(as){
 	 	if (as == null) as = false;

 		this.xml = $.ajax({
 		 	type: "GET",
 		 	url: this.url, 
			data: null,
			async: false
		}).responseXML;
 		
 	 	//Se e para atribuir
	 	if (as) this.atribuir();
 	},
 	
 	/**
 	 * Atribuir os valores as propriedades
 	 * @author Ronneesley Moura Teles
 	 * @since 12/09/2008 11:14
 	 */
 	atribuir: function(){
 	 	//Verifica se o xml esta preenchido
 	 	if (this.xml == null) return false;

		//Pega a tag projudi
		var projudi = this.xml.getElementsByTagName("projudi");
		
		//Verifica se tem elemento
		if (projudi.length < 1) 
			return false;
		else 
			projudi = projudi[0];
		
		this.versao = projudi.getAttribute("versao");
 	}
} 