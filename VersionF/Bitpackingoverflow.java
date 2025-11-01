import java.util.*;                                                   //import des bibliotheques nécessaires pour les structures de données utilisées
public class Bitpackingoverflow extends Bitpackingcross {
    private int baseBits;                                             //nombre de bits utilisés pour la partie "base" des valeurs
    private List<Integer> overflowValues;                             //liste des valeurs qui dépassent la capacité de base
    private Map<Integer, Integer> overflowIndex;                      //map pour retrouver l'index des valeurs d'overflow

    @Override
    public void compress(int[] array) {                               //méthode de compression avec gestion d'overflow
        //Déterminer baseBits (petite taille de champ)
        int globalBits = bitsNeeded(array);                           //nombre de bits nécessaires pour représenter les valeurs maximales du tableau
        this.baseBits = Math.max(1, globalBits / 2);                  //initialisation de baseBits à la moitié de globalBits, au moins 1 pour eviter les erreurs de compression

        overflowValues = new ArrayList<>();                           //liste pour stocker les valeurs d'overflow
        overflowIndex = new HashMap<>();                              //map pour stocker l'index des valeurs d'overflow 

        //Identifier les valeurs trop grandes
        int baseMax = (1 << baseBits);                                //valeur maximale pouvant être représentée avec baseBits
        for (int v : array) {                                         //pour chaque valeur du tableau verifier si elle est trop grande si oui la ranger parmis les valeurs d'overflow
            if (v >= baseMax) {
                if (!overflowIndex.containsKey(v)) {
                    overflowIndex.put(v, overflowValues.size());
                    overflowValues.add(v);
                }
            }
        }

        // Bits nécessaires pour indexer la zone d’overflow
        int overflowIndexBits = (overflowValues.isEmpty()) ? 0 :     //calcul du nombre de bits nécessaires pour indexer les valeurs d'overflow
                (32 - Integer.numberOfLeadingZeros(overflowValues.size() - 1));

        // S’assurer que l’indice de l’overflow rentre dans baseBits
        if (overflowIndexBits > baseBits) {                          //si le nombre de bits nécessaires pour indexer les valeurs d'overflow dépasse baseBits, ajuster baseBits en conséquence
            baseBits = overflowIndexBits;
            baseMax = (1 << baseBits);
        }

        // Ajouter 1 bit de flag
        this.k = baseBits + 1;

        // Construire le tableau encodé
        int[] encoded = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            int v = array[i];
            if (v < baseMax) {
                // flag=0, value=v
                encoded[i] = (0 << baseBits) | v;
            } else {
                int idx = overflowIndex.get(v);
                // flag=1, value=idx
                encoded[i] = (1 << baseBits) | idx;
            }
        }

        // Compression via BitPackingCross
        super.compress(encoded);
    }

    @Override
    public void decompress(int[] dest) {                              //méthode de décompression avec gestion d'overflow
        int[] tmp = new int[n];
        super.decompress(tmp);

        int baseMask = (1 << baseBits) - 1;                           //masque pour extraire la partie "base" des valeurs

        for (int i = 0; i < n; i++) {                                 //pour chaque valeur décompressée, vérifier le flag pour déterminer si c'est une valeur de base ou d'overflow
            int encoded = tmp[i];
            int flag = encoded >>> baseBits;
            int valueBits = encoded & baseMask;

            if (flag == 0) {
                dest[i] = valueBits;
            } else {
                dest[i] = overflowValues.get(valueBits);
            }
        }
    }

    @Override
    public int get(int i) {                                           //méthode pour obtenir la valeur à l'index i avec gestion d'overflow
        int encoded = super.get(i);                                   //récupération de la valeur encodée à l'index i
        int flag = encoded >>> baseBits;                              //extraction du flag
        int valueBits = encoded & ((1 << baseBits) - 1);              //extraction de la partie "base" des valeurs   

        if (flag == 0) {                                              //si le flag est 0, retourner la valeur de base
            return valueBits;
        } else {                                                      //si le flag est 1, retourner la valeur d'overflow correspondante
            return overflowValues.get(valueBits);
        }
    }
}