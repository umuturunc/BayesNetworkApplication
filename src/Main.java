public class Main {


    public static void main(String[] args) {
        // write your code here
        BayesNetwork bayesNetwork = new BayesNetwork();
        Results results = new Results();
        results.calculate();
        results.printValues();
        System.out.println("Her bir Node için CPT değerleri yazdırıldı.");
        System.out.println("Sonuçlar Results.txt dosyasında");

    }
}
