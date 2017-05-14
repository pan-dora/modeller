package cool.pandora.modeller.common.uri;

/**
 * IIIFPrefixes
 *
 * @author Christopher Johnson
 */
public final class IIIFPrefixes {
    public static final String IIIF = "PREFIX iiif: <http://iiif.io/api/image/2#>";
    public static final String SC = "PREFIX sc: <http://iiif.io/api/presentation/2#>";
    public static final String EXIF = "PREFIX exif: <http://www.w3.org/2003/12/exif/ns#>";
    public static final String DC = "PREFIX dc: <http://purl.org/dc/elements/1.1/>";
    public static final String OA = "PREFIX oa: <http://www.w3.org/ns/oa#>";
    public static final String CNT = "PREFIX cnt: <http://www.w3.org/2011/content#>";
    public static final String DCTERMS = "PREFIX dcterms: <http://purl.org/dc/terms/>";
    public static final String DCTYPES = "PREFIX dctypes: <http://purl.org/dc/dcmitype/>";
    public static final String DOAP = "PREFIX doap: <http://usefulinc.com/ns/doap#>";
    public static final String FOAF = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>";
    public static final String XSD = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
    public static final String SVCS = "PREFIX svcs: <http://rdfs.org/sioc/services#>";
    public static final String AS = "PREFIX as: <http://www.w3.org/ns/activitystreams#>";

    private IIIFPrefixes() {
        throw new AssertionError();
    }
}
