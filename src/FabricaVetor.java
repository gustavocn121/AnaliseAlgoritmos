import java.util.Random;


public class FabricaVetor{


    private int randomint(){
        Random rand = new Random();
        return rand.nextInt();        
    }

    private int[] preencheVetor(int[] vetor, int tamanho){
    }

    public int[] criaVetor(int tamanho) {
        int[] vetor = new int[tamanho];
        return vetor;
    }
}