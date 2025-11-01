
public class Bitpackingnocross extends Bitpacking {
    @Override
    public void compress(int[] array) {                                     //méthode de compression sans croisement
        this.n = array.length;
        this.k = bitsNeeded(array);
        int perWord = 32 / k;                                               //nombre de valeurs pouvant être stockées dans un entier de 32 bits
        int m = (int)Math.ceil((double)n / perWord);                        //nombre d'entiers nécessaires pour stocker toutes les valeurs compressées
        compressed = new int[m];                                            //initialisation du tableau compressé

        for (int i = 0; i < n; i++) {                                       //pour chaque valeur dans le tableau d'origine, calculer sa position dans le tableau compressé et y stocker la valeur encodée
            int wordIdx = i / perWord;                                      //index de l'entier dans le tableau compressé
            int offset = (i % perWord) * k;                                 //décalage à l'intérieur de cet entier
            compressed[wordIdx] |= (array[i] & ((1 << k) - 1)) << offset;   //stockage de la valeur encodée à la position appropriée
        }
    }

    @Override
    public void decompress(int[] dest) {                                    //méthode de décompression sans croisement
        int perWord = 32 / k;                                               //nombre de valeurs stockées dans un entier de 32 bits
        for (int i = 0; i < n; i++) {                                       //pour chaque valeur à décompresser, calculer sa position dans le tableau compressé et extraire la valeur encodée
            int wordIdx = i / perWord;                                      //index de l'entier dans le tableau compressé
            int offset = (i % perWord) * k;                                 //décalage à l'intérieur de cet entier
            dest[i] = (compressed[wordIdx] >>> offset) & ((1 << k) - 1);    //extraction de la valeur encodée et stockage dans le tableau de destination
        }
    }

    @Override
    public int get(int i) {                                                 //méthode pour obtenir la valeur à l'index i sans croisement
        int perWord = 32 / k;                                               //nombre de valeurs stockées dans un entier de 32 bits
        int wordIdx = i / perWord;                                          //index de l'entier dans le tableau compressé
        int offset = (i % perWord) * k;                                     //décalage à l'intérieur de cet entier
        return (compressed[wordIdx] >>> offset) & ((1 << k) - 1);           //extraction et retour de la valeur encodée à l'index i
    }
}