package Parte2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class App {
    private static final int QTD_VEZES = 50;
    private static final int TAM = 375000;



    /**
     * Troca dois elementos de posição em um vetor.
     * @param dados O vetor com os dados.
     * @pos1 Primeira posição para trocar.
     * @pos2 Posição a ser trocada com a primeira.
     */
    static void trocar(int[] dados, int pos1, int pos2){
        int aux = dados[pos1];
        dados[pos1] = dados[pos2];
        dados[pos2] = aux;
    }

    /**
     * Gerador de vetores aleatórios: recebe como parâmetros o tamanho do vetor e se ele deve ser gerado em ordem crescente ou não.
     * @param tamanho Tamanho do vetor a gerar (número positivo).
     * @param ordenado TRUE para receber um vetor ordenado; FALSE caso contrário.
     * @return Um vetor de inteiros nas condições especificadas pelos parâmetros.
     */
    static public int[] geraVetor(int tamanho, boolean ordenado){
        Random sorteio = new Random(42);
        int[] dados = new int[tamanho];
        for (int i = 0; i < dados.length; i++) {
            dados[i] = i+1;
        }
        if(!ordenado){
            for (int i = 0; i < dados.length*3; i++) {
                int pos1 = sorteio.nextInt(dados.length);
                int pos2 = sorteio.nextInt(dados.length);
                trocar(dados, pos1, pos2);
            }
        }
        return dados;
    }

    /**
     * Ordenação por bolha. Pára a ordenação se percebe o vetor já ordenado.
     * @param dados O vetor a ser ordenado.
     */
    static public void bolha(int[] dados){
        for (int i = dados.length-1; i > 0; i--) {
            int trocas = 0;
            for (int j = 0; j < i; j++) {
                if(dados[j] > dados[j+1]){
                    trocar(dados, j, j+1);
                    trocas++;
                }
            }
            if(trocas==0) return;
        }
    }

    /**
     * Particionamento do vetor para a ordenação quicksort. Usa como pivô a última posição do vetor.
     * @param dados Vetor a ser particionado.
     * @param inicio Posição inicial do particionamento.
     * @param fim Posição final do particionamento (inclusivo, ou seja: este valor é a posição do vetor que será usada como pivô).
     * @return A posição entre 'inicio' e 'fim' onde ocorreu o particionamento.
     */
    static public int particao(int[] dados, int inicio, int fim){
        int posicao = inicio-1;
        Random sorteio = new Random();
        int pivot = dados[sorteio.nextInt(dados.length)];
        for (int i = inicio; i < fim; i++) {
            if(dados[i]<pivot){
                posicao++;
                trocar(dados, posicao, i);
            }
        }
        posicao++;
        trocar(dados, posicao, fim);
        return posicao;
    }

    /**
     * Método quicksort recursivo para ordenar um vetor de inteiros.
     * @param dados O vetor com os dados.
     * @param inicio O limite inicial para a ordenação recursiva.
     * @param fim O limite final (inclusivo) para a ordenação recursiva.
     */
    static public void quicksort(int[] dados, int inicio, int fim){
        if(inicio>=fim)
            return;
        else{
            int particao = particao(dados, inicio, fim);
            quicksort(dados, inicio, particao-1);
            quicksort(dados, particao+1, fim);
        }
    }

    public static void stringToFile(String nomeArquivo, String texto) throws IOException {
        File f = new File(nomeArquivo);
        if (f.createNewFile()) {
            System.out.println("arquivo criado: " + f.getName());
        }

        try {
            FileWriter myWriter = new FileWriter(f.getName());
            myWriter.write(texto);
            myWriter.close();
        }
        catch(IOException  e){
            e.printStackTrace();
        }

    }
    public static ArrayList<Long> calculaTempoOrdenacao(String metodo, int[] vetor, int qtd_vezes){
        long start;
        long end;
        ArrayList<Long> tempos = new ArrayList<Long>();

        for (int i = 0; i < qtd_vezes; i++) {
            int[] vClone = vetor.clone();
            start = System.nanoTime();
            if (metodo.equals("BubbleSort")){
                bolha(vClone);
            } else  {
                quicksort(vClone,0, vClone.length - 1);
            }

            end = System.nanoTime();
            tempos.add(end - start);
        }

        return tempos;
    }

    /**
     * Escreva aqui no método principal sua automação do teste, de acordo com as especificações do exercício.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    long s = System.nanoTime();
    int[] vetOrdenado = geraVetor(TAM, true);
    int[] vetAleatorio = geraVetor(TAM, false);

    int[][] vetores = {vetOrdenado,vetAleatorio};

    // HashMap<Ordenado?, tempos>
    HashMap<Boolean, ArrayList<Long>> results = new HashMap<Boolean, ArrayList<Long>>();

    ArrayList<Long> temposOrdenado  = calculaTempoOrdenacao("QuickSort" ,vetores[0], QTD_VEZES);
    ArrayList<Long> temposAleatorio  = calculaTempoOrdenacao("QuickSort" ,vetores[1], QTD_VEZES);
    results.put(true, temposOrdenado);
    results.put(false, temposAleatorio);

    StringBuilder strResults = new StringBuilder();

    strResults.append("Ordenado?, MediaTempo(ns)\n");
    for (Boolean k : results.keySet()){

        double mediaTempo = results.get(k)
                .stream()
                .mapToLong(Long::longValue)
                .average()
                .getAsDouble();

        strResults.append(k + "," + mediaTempo + "\n");
    }

    System.out.println(strResults);
    stringToFile("resultadosParte2.csv", strResults.toString());
    System.out.println("Resultado exportado para: resultados.csv");

    long t = System.nanoTime() - s;

    System.out.println("Total runtime: " + TimeUnit.SECONDS.convert(t, TimeUnit.NANOSECONDS));
    


    }



}
