package org.blume.modeller;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.blume.modeller.common.uri.FedoraPrefixes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestTemplate {

    List<Item> items() {
        return Collections.singletonList(
                new Item(Arrays.asList(
                        new Prefix(FedoraPrefixes.RDFS),
                        new Prefix(FedoraPrefixes.MODE)),
                        "http://localhost:8888/iiif/")
        );
    }

    static class Item {
        Item(List<Prefix> prefixes, String serviceURI) {
            this.prefixes = prefixes;
            this.serviceURI = serviceURI;
        }

        List<Prefix> prefixes;
        String serviceURI;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }

    public static void main(String[] args) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("template/sparql-update.mustache");
        mustache.execute(new PrintWriter(System.out), new TestTemplate()).flush();
    }
}
