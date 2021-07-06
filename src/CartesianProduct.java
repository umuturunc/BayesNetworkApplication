import java.util.ArrayList;

public class CartesianProduct {
    //2 tane parent node gönderildiği varsayılıyor
    public static ArrayList<ArrayList<Duple>> calculate(ArrayList<Node> parentNodes)
    //Gelen iki listenin kartezyen çarpımını döndür
    {
        ArrayList<ArrayList<Duple>> mustHaveDuplesList = new ArrayList<>();
        Node parent1 = parentNodes.get(0);
        Node parent2 = parentNodes.get(1);
//        Integer size = parent1.getAttributes().size() * parent2.getAttributes().size();
        for (int i = 0; i < parent1.getAttributes().size(); i++) {
            for (int j = 0; j < parent2.getAttributes().size(); j++) {
                Duple duple1 = new Duple(parent1.getName(),parent1.getAttributes().get(i).getName());
                Duple duple2 = new Duple(parent2.getName(),parent2.getAttributes().get(j).getName());
                ArrayList<Duple> duples = new ArrayList<>();
                duples.add(duple1);
                duples.add(duple2);
                mustHaveDuplesList.add(duples);
            }
        }
        return mustHaveDuplesList;

//        ArrayList<String> parentNodeNames = new ArrayList<>();
//        parentNodes.forEach(node ->parentNodeNames.add(node.getName()));
//        ArrayList<Integer> parentNodeAttributeCounts = new ArrayList<>();
//        parentNodes.forEach(node -> parentNodeAttributeCounts.add(node.getAttributes().size()));
//        Integer totalListCount = parentNodeAttributeCounts.stream().reduce(0,Integer::sum);

        
    }
}
