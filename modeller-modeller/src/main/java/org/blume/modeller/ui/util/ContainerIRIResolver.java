package org.blume.modeller.ui.util;

import java.util.Map;

import org.blume.modeller.bag.BagInfoField;

public class ContainerIRIResolver {
    private String resolvedIRI;

    public ContainerIRIResolver(String resolvedIRI) {
        this.resolvedIRI = resolvedIRI;
    }

    public static ContainerIRIResolver.ContainerIRIResolverNormal resolve() {
        return new ContainerIRIResolver.ContainerIRIResolverNormal();
    }

    public String render() {
        return this.resolvedIRI;
    }

    public static class ContainerIRIResolverNormal {
        private Map<String, BagInfoField> map;
        private String baseURIKey;
        private String collectionRootKey;
        private String collectionKey;
        private String objektIDKey;
        private String containerKey;

        ContainerIRIResolverNormal() {}

        public ContainerIRIResolver.ContainerIRIResolverNormal map(Map<String, BagInfoField> map) {
            this.map = map;
            return this;
        }

        public ContainerIRIResolver.ContainerIRIResolverNormal baseURIKey(String baseURIKey) {
            this.baseURIKey = baseURIKey;
            return this;
        }

        public ContainerIRIResolver.ContainerIRIResolverNormal collectionRootKey(String CollectionRootKey) {
            this.collectionRootKey = CollectionRootKey;
            return this;
        }

        public ContainerIRIResolver.ContainerIRIResolverNormal collectionKey(String CollectionKey) {
            this.collectionKey = CollectionKey;
            return this;
        }

        public ContainerIRIResolver.ContainerIRIResolverNormal objektIDKey(String ObjektIDKey) {
            this.objektIDKey = ObjektIDKey;
            return this;
        }

        public ContainerIRIResolver.ContainerIRIResolverNormal containerKey(String ContainerKey) {
            this.containerKey = ContainerKey;
            return this;
        }

        private static String getMapValue(Map<String, BagInfoField> map, String key) {
            BagInfoField IIIFProfileKey = map.get(key);
            return IIIFProfileKey.getValue();
        }

        static String resolveIRI(Map<String, BagInfoField> map, String baseURIKey, String collectionRootKey,
                                 String collectionKey, String objektIDKey, String containerKey) {
            String baseURI = getMapValue(map, baseURIKey);
            String collectionRoot = getMapValue(map, collectionRootKey);
            String collection = getMapValue(map, collectionKey);
            String objektID = getMapValue(map, objektIDKey);
            String container = getMapValue(map, containerKey);
            return baseURI +
                    collectionRoot +
                    collection +
                    objektID +
                    container;
        }

        public ContainerIRIResolver build() {
            String resolvedIRI = resolveIRI(this.map, this.baseURIKey, this.collectionRootKey, this.collectionKey,
                    this.objektIDKey, this.containerKey);
            return new ContainerIRIResolver(resolvedIRI);
        }
    }


}
