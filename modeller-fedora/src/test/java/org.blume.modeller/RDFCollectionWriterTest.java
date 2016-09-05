package org.blume.modeller;

import org.blume.modeller.util.RDFCollectionWriter;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class RDFCollectionWriterTest {
    public static void main(String[] args) {
        RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection()
                .idList(getMockSequence())
                .collectionPredicate("http://iiif.io/api/presentation/2#hasCanvases")
                .build();
        String collection = collectionWriter.render();
        System.out.println(collection);
    }

    public static ArrayList<Integer> getMockSequence() {
        int numOfValues = 6;
        int[] array = IntStream.range(1, numOfValues + 1).toArray();
        ArrayList<Integer> idList = new ArrayList<>(array.length);
        for (int anArray : array) idList.add(anArray);
        return idList;
    }
}


