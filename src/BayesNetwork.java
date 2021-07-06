import java.util.*;

public class BayesNetwork {

    public static final String PARENTS = "Parents.txt";
    public static final String TRAIN = "Train.txt";
    public static final String TEST = "Test.txt";
    public static final String SPLITTER = ",";


    private ArrayList<Node> nodeList;
    private ArrayList<String> nodeNames;
    private Scanner scanner;
    private ArrayList<Query> queryList;


    public BayesNetwork() {
        nodeList = new ArrayList<>();
        nodeNames = new ArrayList<>();
        queryList = new ArrayList<>();
        createNodes();
        createParents();
        readQueries(TRAIN);
        createAttributes();
        train();
        printNodeCPTs();
        readQueries(TEST);
        test();
    }

    private void createNodes() {
        try {
            scanner = DataReader.readFile(TRAIN);
            List names = Arrays.asList(scanner.nextLine().split(SPLITTER));
            nodeNames.addAll(names);
            nodeNames.forEach(name -> nodeList.add(new Node(name)));
//            System.out.println(nodeList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createParents() {
        try {
            scanner = DataReader.readFile(PARENTS);
            while (scanner.hasNextLine()) {
                //Dosyadan node ilişkilerini oku
                List<String> nodesByName = Arrays.asList(scanner.nextLine().split(SPLITTER));
                //Okunan satırdaki ilk isim child node'dur.
                String childNodeName = nodesByName.get(0);
                Node childNode = getNodeByName(childNodeName);
                //Satırdaki bütün isimler child node'un ilişkili olduğu node'lardır.
                List<Node> relatedNodes = new ArrayList<>();
                nodesByName.forEach(name -> relatedNodes.add(getNodeByName(name)));
                //Child node'a ilişkili olduğu node'ları ekle (kendisi dahil)
                relatedNodes.forEach(node -> childNode.addRelatedNode(node));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Kod içerisinde okurken kolaylık olması için String'den ibaret olan Attribute nesnesi
    private void createAttributes() {
        for (Query query : queryList) {
            for (Duple duple : query.getDuples()) {
                getNodeByName(duple.getNodeName()).addAttribute(duple.getAttributeName());
            }
        }
    }

    //Dosyadan sorgu okuma
    private void readQueries(String fileName) {
        queryList = new ArrayList<>();
        try {
            scanner = DataReader.readFile(fileName);
            //İlk satırı atla
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                List<String> attributeNames = Arrays.asList(scanner.nextLine().split(SPLITTER));
                Query query = new Query();
                for (int i = 0; i < attributeNames.size(); i++) {
                    Duple duple = new Duple(nodeNames.get(i), attributeNames.get(i));
                    query.addDuple(duple);
                }
                queryList.add(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Eğitim aşaması
    private void train() {
        for (Query query : queryList) {
            for (Node node : nodeList) {
                Query newQuery = new Query();
                for (Node relatedNode : node.getRelatedNodes()) {
                    for (int i = 0; i < query.getDuples().size(); i++) {
                        if (query.getDuples().get(i).getNodeName().equals(relatedNode.getName())) {
                            newQuery.addDuple(query.getDuples().get(i));
                        }
                    }
                }
                node.addQuery(newQuery);
            }
        }
        nodeList.forEach(Node::normalizeCPT);
    }

    //    DUPLE İÇİN DEEP COPY METODU TANIMLA (ARAŞTIR)  --> Önemli
    //Yeni verilerin tahminlenmesi
    public void test() {
        Results results = new Results();

        ArrayList<String> originalRecords = new ArrayList<>();
        ArrayList<String> predictions = new ArrayList<>();
        ArrayList<Float> predictionProbabilities = new ArrayList<>();


        for (Query query : queryList) {
            Float probabilityBad = 1f;
            Query queryBad = new Query();
            query.getDuples().forEach(duple -> queryBad.addDuple(duple.clone()));
            queryBad.getDupleByNodeName("Overall Score").setAttributeName("bad");

            Float probabilityGood = 1f;
            Query queryGood = new Query();
            query.getDuples().forEach(duple -> queryGood.addDuple(duple.clone()));
            queryGood.getDupleByNodeName("Overall Score").setAttributeName("good");

            Float probabilitySum = 0f;

            for (Duple duple : query.getDuples()) {
                Node nodeLookedFor = getNodeByName(duple.getNodeName());
                probabilityBad *= getProbabilityOfQuery(queryBad, nodeLookedFor);
                probabilityGood *= getProbabilityOfQuery(queryGood, nodeLookedFor);
            }

            probabilitySum = probabilityBad + probabilityGood;

            probabilityBad = probabilityBad / probabilitySum;
            probabilityGood = probabilityGood / probabilitySum;

            originalRecords.add(query.getDupleByNodeName("Overall Score").getAttributeName());
            predictions.add(probabilityBad > probabilityGood ? "bad" : "good");
            predictionProbabilities.add(probabilityBad > probabilityGood ? probabilityBad : probabilityGood);

            results.addOriginalRecords(originalRecords);
            results.addPredictions(predictions);
            results.addPredictionProbabilities(predictionProbabilities);
        }
    }

    //Gelen yeni sorgunun olasılığı
    private Float getProbabilityOfQuery(Query query, Node node) {
        for (Map.Entry entry : node.CPT.entrySet()) {
            Query nodeQuery = (Query) entry.getKey();
            if (query.getDuples().containsAll(nodeQuery.getDuples()))
                return (Float) entry.getValue();
        }
        return 0f;
    }


    //Verilen isimle nodeList içerisindeki Node'u getir
    public Node getNodeByName(String name) {
        Node node = null;
        for (Node n : nodeList) {
            if (n.getName().equals(name)) {
                node = n;
                break;
            }
        }
        return node;
    }

    //Olasılık tablolarını yazdırır
    public void printNodeCPTs() {
        //RelatedNodes
//        nodeList.forEach(node -> {
//            System.out.print(node.getName() + "-> ");
//            node.getRelatedNodes().forEach(rnode -> {
//                System.out.print(rnode.getName() + ",");
//            });
//            System.out.println();
//        });

        //Attributes
//        nodeList.forEach(node -> {
//            System.out.print(node.getName() + "-> ");
//            node.getAttributes().forEach(attribute -> {
//                System.out.print(attribute.getName() + ",");
//            });
//            System.out.println();
//        });

        //Queries
        nodeList.forEach(node -> {
            System.out.println("----------------------");
            System.out.println(node.getName().toUpperCase());
            node.CPT.forEach((query, probability) -> {
                System.out.println(query + " = " + probability);
            });
            System.out.println();
        });
    }

}
