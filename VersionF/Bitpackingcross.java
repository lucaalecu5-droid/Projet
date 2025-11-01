

public class Bitpackingcross extends Bitpacking {
    @Override
    public void compress(int[] array) {                                   //méthode de compression avec croisement
        this.n = array.length;                                            //nombre d'éléments d'origine
        this.k = Math.max(1, bitsNeeded(array));                          // au moins 1 bit
        long totalBits = (long) n * k;                                    //nombre total de bits nécessaires pour stocker toutes les valeurs compressées
        int m = (int) ((totalBits + 31) / 32);                            //nombre d'entiers nécessaires pour stocker toutes les valeurs compressées
        compressed = new int[m];                                          //initialisation du tableau compressé

        long bitPos = 0;                                                  //position actuelle en bits
        long mask = (k == 64) ? -1L : ((1L << k) - 1L);                   //masque pour extraire k bits
        for (int v : array) {                                             //pour chaque valeur dans le tableau d'origine
            long val = ((long) v) & mask;                                 // valeur proprement limitée à k bits
            int wordIdx = (int) (bitPos / 32);                            //index de l'entier dans le tableau compressé
            int offset = (int) (bitPos % 32);                             //décalage à l'intérieur de cet entier

            // Écrire la partie basse dans wordIdx
            long cur = (compressed[wordIdx] & 0xFFFFFFFFL);               // valeur actuelle de l'entier (non signé)
            cur |= (val << offset);                                       // ajouter la partie basse
            compressed[wordIdx] = (int) cur;                              // stocker la nouvelle valeur

            // Si l'entier dépasse la frontière du mot, écrire la partie haute dans wordIdx+1
            if (offset + k > 32) {                                        
                int bitsInNext = offset + k - 32;                         // nombre de bits à écrire dans le mot suivant
                long highPart = (val >>> (32 - offset)) & ((1L << bitsInNext) - 1L);     // partie haute à écrire
                long next = (compressed[wordIdx + 1] & 0xFFFFFFFFL) | highPart;          // ajouter la partie haute
                compressed[wordIdx + 1] = (int) next;                     // stocker la nouvelle valeur
            }

            bitPos += k;
        }
    }

    @Override
    public void decompress(int[] dest) {                                  //méthode de décompression avec croisement
        long bitPos = 0;                                                  //position actuelle en bits
        long mask = (k == 64) ? -1L : ((1L << k) - 1L);                   //masque pour extraire k bits
        for (int i = 0; i < n; i++) {                                     //pour chaque valeur à décompresser
            int wordIdx = (int) (bitPos / 32);                            //index de l'entier dans le tableau compressé
            int offset = (int) (bitPos % 32);                             //décalage à l'intérieur de cet entier

            long low = (compressed[wordIdx] & 0xFFFFFFFFL) >>> offset;    // partie basse
            long val = low & mask;                                        // valeur partielle

            if (offset + k > 32) {
                int bitsInNext = offset + k - 32;                         // nombre de bits à lire dans le mot suivant
                long next = (compressed[wordIdx + 1] & 0xFFFFFFFFL) & ((1L << bitsInNext) - 1L);         // partie haute
                val |= next << (32 - offset);                             // combiner avec la partie basse
            }

            dest[i] = (int) val;
            bitPos += k;
        }
    }

    @Override
    public int get(int i) {
        long bitPos = (long) i * k;                                      // position actuelle en bits
        int wordIdx = (int) (bitPos / 32);                               // index de l'entier dans le tableau compressé
        int offset = (int) (bitPos % 32);                                // décalage à l'intérieur de cet entier
        long mask = (k == 64) ? -1L : ((1L << k) - 1L);                  // masque pour extraire k bits

        long low = (compressed[wordIdx] & 0xFFFFFFFFL) >>> offset;       // partie basse
        long val = low & mask;                                           // valeur partielle

        if (offset + k > 32) {
            int bitsInNext = offset + k - 32;                           // nombre de bits à lire dans le mot suivant
            long next = (compressed[wordIdx + 1] & 0xFFFFFFFFL) & ((1L << bitsInNext) - 1L);         // partie haute
            val |= next << (32 - offset);                               // combiner avec la partie basse
        }
        return (int) val;
    }
}