import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node {
    //Node isimleri (Overall Score, Luggage Size, Safety vs.)
    public String getName() {
        return name;
    }

    private String name;
    //Node'un alabileceği dilsel değişkenler
    //vhigh, high, med, low gibi
    private ArrayList<Attribute> attributes;
    //İlişkili olduğu node'lar listesi(kendisi dahil)
    private ArrayList<Node> relatedNodes;

    HashMap<Query, Float> CPT;

    //CPT tablosundaki sorguların toplam geçme sayıları, olasılığa dönüştürülüyor
    public void normalizeCPT() {
        ArrayList<Node> parentNodes = new ArrayList<>();
        parentNodes.addAll(relatedNodes);
        parentNodes.remove(this);
        //Parent node'a sahip değilse
        if (parentNodes.size() == 0) {
            float total = CPT.values().stream().reduce(0f, Float::sum);
            for (Map.Entry entry : CPT.entrySet()) {
                entry.setValue(((Float) entry.getValue()) / total);
            }
        }
        //1 tane parent node'u varsa
        else if (parentNodes.size() == 1) {
            ArrayList<Attribute> attributes = parentNodes.get(0).getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                Duple duple = new Duple(parentNodes.get(0).getName(), attributes.get(i).getName());
                float total = 0;
                for (Map.Entry entry : CPT.entrySet()) {
                    Query query = (Query) entry.getKey();
                    if (query.containsDuple(duple))
                        total += (Float) entry.getValue();
                }
                for (Map.Entry entry : CPT.entrySet()) {
                    Query query = (Query) entry.getKey();
                    if (query.containsDuple(duple))
                        entry.setValue(((Float) entry.getValue() / total));
                }
            }
        }
        //2 tane parent node'u varsa
        else if (parentNodes.size() == 2) {
            ArrayList<ArrayList<Duple>> mustHaveDuplesList = CartesianProduct.calculate(parentNodes);
            for (ArrayList<Duple> list : mustHaveDuplesList) {
                float sum = 0;
                for (Map.Entry entry : CPT.entrySet()) {
                    Query query = (Query) entry.getKey();
                    if (query.getDuples().containsAll(list)) {
                        sum += (Float) entry.getValue();
                    }
                }
                for (Map.Entry entry : CPT.entrySet()) {
                    Query query = (Query) entry.getKey();
                    if (query.getDuples().containsAll(list)) {
                        entry.setValue((Float) entry.getValue() / sum);
                    }
                }
            }
        }
    }

    //Sorgu ekle
    public void addQuery(Query query) {
//        if(CPT.containsKey(query))
//        {
//            CPT.put(query,CPT.get(query)+1f);
//            System.out.println("CONTAINS KEY!!!!");
//        }
//        else
//        {
//            CPT.put(query,1f);
//        }
        CPT.put(query, CPT.getOrDefault(query, 0f) + 1f);
    }

    public ArrayList<Node> getRelatedNodes() {
        return relatedNodes;
    }

    public Node(String name) {
        this.name = name;
        attributes = new ArrayList<>();
        relatedNodes = new ArrayList<>();
        CPT = new HashMap<>();
    }


    public void addRelatedNode(Node node) {
        relatedNodes.add(node);
    }

    public void addAttribute(String name) {
        if (getAttributeByName(name) == null)
            attributes.add(new Attribute(name));
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }


    public Attribute getAttributeByName(String name) {
        Attribute attribute = null;
        for (Attribute a : attributes) {
            if (a.getName().equals(name)) {
                attribute = a;
                break;
            }
        }
        return attribute;
    }
}
