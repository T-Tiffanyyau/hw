
/**
 * Write a description of Part3 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;

public class Part3 {
    public void processGenes(StorageResource sr){
        int countlonger60 = 0;
        int countcgRatio = 0;
        int longest = 0;
        for (String s: sr.data()){
            if (s.length() > 60){
                // print all the Strings in sr that are longer than 60 characters
                System.out.println(s + " is longer than 60");
                countlonger60 += 1;
            }
            
            // print the number of Strings in sr that are longer than 60 characters
            System.out.println("number of Strings longer than 60: " + countlonger60);

            if (cgRatio(s) > 0.35){
                // print the Strings in sr whose C-G-ratio is higher than 0.35
                System.out.println(s + "'s C-G-ratio is higher than 0.35");
                countcgRatio += 1;
            }
            // print the number of strings in sr whose C-G-ratio is higher than 0.35
            System.out.println("number where C-G-ratio > 0.35: " + countcgRatio);

            if (longest < s.length()){
                longest = s.length();
            }
            //print the length of the longest gene in sr
            System.out.println("the length of the longest gene: " + longest);
        }
    }

    public void testProcessGenes(){
        FileResource fr = new FileResource();
        String dna = fr.asString();
        StorageResource genes = getAllGenes(dna);
        processGenes(genes);
    }


    public float cgRatio(String dna){
        float numOfCnG = 0;
        String dnaupper = dna.toUpperCase();
        int cIndex = dnaupper.indexOf("C");
        int gIndex = dnaupper.indexOf("G");

        while (cIndex != -1 && cIndex < dna.length()){
            numOfCnG = numOfCnG + 1;
            cIndex = dnaupper.indexOf("C", cIndex + 1);
        }
        while (gIndex != -1 && gIndex < dna.length()){
            numOfCnG = numOfCnG + 1;
            gIndex = dnaupper.indexOf("G", gIndex + 1);
        }
        
        float ratio = (float) (numOfCnG / dna.length());
        return ratio;
    }

    public int findStopCodon(String dna, int startIndex, String stopCodon){
        // find the index of stopCodon
        int currIndex = dna.indexOf(stopCodon, startIndex + 3);
        
        while (currIndex != -1){
            if ((currIndex - startIndex) % 3 == 0 ){
                return currIndex;
            }
            else{
                currIndex = dna.indexOf(stopCodon, currIndex + 1);
            }
        }
        
        return -1;   
    }

    public String findGene(String dna, int where){
        int startIndex = dna.indexOf("ATG", where);
        if (startIndex == -1){
            return "";
        }

        int taaIndex = findStopCodon(dna, startIndex, "TAA");
        int tagIndex = findStopCodon(dna, startIndex, "TAG");
        int tgaIndex = findStopCodon(dna, startIndex, "TGA");

        int minIndex = 0;
        if (taaIndex == -1 || tagIndex != -1 && tagIndex < taaIndex){
            minIndex = tagIndex;
        }
        else{
            minIndex = taaIndex;
        }
        
        if (minIndex == -1 || tgaIndex != -1 && tgaIndex < minIndex){
            minIndex = tgaIndex;
        }
        
        if (minIndex != -1){
            return dna.substring(startIndex, minIndex + 3);
        }
        return "";
    }

    public StorageResource getAllGenes(String dna){
        StorageResource geneList = new StorageResource();
        int startIndex = 0;
        while (true){
            String currGene = findGene(dna, startIndex);
            if (currGene.isEmpty()){
                break;
            }
            // Add gene to genelist
            geneList.add(currGene);
            startIndex = dna.indexOf(currGene, startIndex) + currGene.length();
        }
        return geneList;
    }
}
