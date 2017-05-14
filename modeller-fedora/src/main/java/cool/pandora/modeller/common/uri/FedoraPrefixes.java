package cool.pandora.modeller.common.uri;

/**
 * FedoraPrefixes
 *
 * @author Christopher Johnson
 */
public final class FedoraPrefixes {
    public static final String RDFS = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
    public static final String MODE = "PREFIX mode: <http://www.modeshape.org/1.0>";
    public static final String PREMIS = "PREFIX premis: <http://www.loc.gov/premis/rdf/v1#>";
    public static final String IMAGE = "PREFIX image: <http://www.modeshape.org/images/1.0>";
    public static final String SV = "PREFIX sv: <http://www.jcp.org/jcr/sv/1.0>";
    public static final String TEST = "PREFIX test: <info:fedora/test/>";
    public static final String NT = "PREFIX nt: <http://www.jcp.org/jcr/nt/1.0>";
    public static final String XSI = "PREFIX xsi: <http://www.w3.org/2001/XMLSchema-instance>";
    public static final String XMLNS = "PREFIX xmlns: <http://www.w3.org/2000/xmlns/>";
    public static final String RDF = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    public static final String FEDORA = "PREFIX fedora: <http://fedora.info/definitions/v4/repository#>";
    public static final String XML = "PREFIX xml: <http://www.w3.org/XML/1998/namespace>";
    public static final String JCR = "PREFIX jcr: <http://www.jcp.org/jcr/1.0>";
    public static final String EBUCORE = "PREFIX ebucore: <http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#>";
    public static final String LDP = "PREFIX ldp: <http://www.w3.org/ns/ldp#>";
    public static final String XS = "PREFIX xs: <http://www.w3.org/2001/XMLSchema>";
    public static final String FEDORACONFIG = "PREFIX fedoraconfig: <http://fedora.info/definitions/v4/config#>";
    public static final String MIX = "PREFIX mix: <http://www.jcp.org/jcr/mix/1.0>";

    private FedoraPrefixes() {
        throw new AssertionError();
    }
}
