package cool.pandora.modeller;

import cool.pandora.modeller.util.TextCollectionWriter;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class textCollectionWriterTest {
    public static void main(String[] args) {
        TextCollectionWriter collectionWriter;
        collectionWriter = TextCollectionWriter.collection().idList(getMockSequence())
                .collectionPredicate("http://iiif.io/api/presentation/2#hasCanvases")
                .resourceContainerIRI("http://localhost:8080/fcrepo/rest/collection/AIG/")
                .canvasURI("http://localhost:8080/fcrepo/rest/collection/test/007/canvas/001").build();
        String collection = collectionWriter.render();
        System.out.println(collection);
    }

    public static ArrayList<String> getMockSequence() {
        int numOfValues = 2;
        int[] array = IntStream.range(1, numOfValues + 1).toArray();
        ArrayList<String> idList = new ArrayList<>(array.length);
        for (int anArray : array)
            idList.add(String.valueOf(anArray));
        return idList;
    }
}


