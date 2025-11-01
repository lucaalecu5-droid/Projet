
import java.util.*;
public class Benchmark {
    public static void run(int[] ladata) {
        int[] data;
        if (ladata.length > 0) {
            data = ladata;
        } else {
            Random rnd = new Random(System.currentTimeMillis());
            data = rnd.ints(10000, 0, 5000).toArray();
        }
        

        for (String type : List.of("cross", "nocross", "overflow")) {                           //types de bitpacking à tester
            Bitpacking bp = Bitpackingfactory.create(type);                                     //création d'une instance de Bitpacking en fonction du type

            long tCompress = Bitpacking.measure(() -> bp.compress(data), 5);               //mesure du temps de compression sur 5 runs
            int[] out = new int[data.length];                                                   //tableau pour stocker les données décompressées
            long tDecompress = Bitpacking.measure(() -> bp.decompress(out), 5);            //mesure du temps de décompression sur 5 runs
            long tGet = Bitpacking.measure(() -> bp.get(data.length / 2), 5);              //mesure du temps d'accès à un élément au milieu du tableau sur 5 runs

            double compressionRatio = (bp.compressed.length * 4.0) / (data.length * 4.0);       //calcul du ratio de compression
            double savedBytes = (1 - compressionRatio) * data.length * 4;                       //calcul du nombre d'octets économisés
            boolean verification = Arrays.equals(data, out);                                    //vérification de la justesse de la décompression
            if (!verification) {
                System.err.println("  Erreur : les tableaux diffèrent pour le type " + type);
                for (int i = 0; i < data.length; i++) {
                    if (data[i] != out[i]) {
                        System.err.printf(" - Différence à l’indice %d : attendu %d, obtenu %d%n",
                                i, data[i], out[i]);
                    }
                }
            }
            
            System.out.printf("""
                Type: %s
                Compression Time: %.2f ms
                Decompression Time: %.2f ms
                Get Time: %.2f µs
                Compression Ratio: %.2f%%
                Saved Bytes: %.0f
                Verification globale: %b%n
                """,
                type,
                tCompress / 1e6, tDecompress / 1e6, tGet / 1e3,
                compressionRatio * 100, savedBytes, verification);
            //System.out.println("Test de justesse decompression:");
            //for (int i = 0; i < Math.min(10, data.length); i++) {
            //    System.out.printf(" - indice %d : attendu %d, obtenu %d%n",
            //                i, data[i], out[i]);
            //}
            //System.out.println("Test de justesse get:");
            //for (int i = 0; i < Math.min(10, data.length); i++) {
            //    System.out.printf(" - indice %d : attendu %d, obtenu %d%n",
            //                i, data[i], bp.get(i));
            //}
            System.out.println("-----------------------------");
        }
    }
}
