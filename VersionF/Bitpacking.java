
public abstract class Bitpacking {
    protected int n;                                       //nombre d'éléments d'origine
    protected int k;                                       //nombre de bits par entier
    protected int[] compressed;                            //tableau des entiers compressés

    public abstract void compress(int[] array);            //méthode de compression
    public abstract void decompress(int[] dest);           //méthode de décompression
    public abstract int get(int i);                        //méthode pour obtenir la valeur à l'index i

    public static int bitsNeeded(int[] array) {            //méthode pour calculer le nombre de bits nécessaires pour représenter les valeurs dans le tableau
        int max = 0;                                       //initialisation de la valeur maximale
        for (int v : array) max = Math.max(max, v);        //parcours du tableau pour trouver la valeur maximale
        return 32 - Integer.numberOfLeadingZeros(max);     //calcul du nombre de bits nécessaires pour représenter la valeur maximale
    }

    public static long measure(Runnable task, int runs) {  //méthode pour mesurer le temps d'exécution d'une tâche donnée sur un nombre spécifié de runs
        long best = Long.MAX_VALUE;                        //initialisation du meilleur temps à la valeur maximale
        for (int i = 0; i < runs; i++) {                   
            long t0 = System.nanoTime();                   //enregistrement du temps de début
            task.run();                                    //exécution de la tâche
            long t1 = System.nanoTime();                   //enregistrement du temps de fin
            best = Math.min(best, t1 - t0);                //mise à jour du meilleur temps si le temps actuel est inférieur
        }
        return best;
    }
}


