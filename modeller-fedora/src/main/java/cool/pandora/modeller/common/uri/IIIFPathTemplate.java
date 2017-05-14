package cool.pandora.modeller.common.uri;

/**
 * IIIFPathTemplate
 *
 * @author Christopher Johnson
 */
public final class IIIFPathTemplate extends Type {
    private IIIFPathTemplate(final int value, final String path) {
        super(value, path);
    }

    public static final IIIFPathTemplate BASE_URI_PATH = new IIIFPathTemplate(0, "/{FedoraAppRoot}/{RestServletURI}/");
    public static final IIIFPathTemplate COLLECTION_ROOT_PATH =
            new IIIFPathTemplate(1, "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/");
    public static final IIIFPathTemplate COLLECTION_ID_PATH =
            new IIIFPathTemplate(2, "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/");
    public static final IIIFPathTemplate OBJECT_ID_PATH =
            new IIIFPathTemplate(3, "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/");
    public static final IIIFPathTemplate CONTAINER_PATH = new IIIFPathTemplate(4,
            "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/{container}/");
    public static final IIIFPathTemplate RESOURCE_PATH = new IIIFPathTemplate(5,
            "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/{container}/{resource}");
    public static final IIIFPathTemplate MANIFEST_PATH = new IIIFPathTemplate(6,
            "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/{manifest}");
}
