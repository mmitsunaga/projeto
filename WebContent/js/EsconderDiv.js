// JavaScript Document
function AlterarEstado(imagem, NomeDiv, ImageSt1, ImageSt2){

	if (imagem.src.indexOf("Menos")!=-1){
		imagem.src=ImageSt1;
		document.getElementById(NomeDiv).style.display = "none";
	}else{
		imagem.src=ImageSt2;
		document.getElementById(NomeDiv).style.display = "block";
	}
}