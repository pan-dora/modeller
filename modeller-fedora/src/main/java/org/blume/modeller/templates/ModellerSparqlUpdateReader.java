package org.blume.modeller.templates;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ModellerSparqlUpdateReader
{
    public static void main(String[] args)
    {
        //Model model = RDFDataMgr.loadModel("data/res.n3") ;

        InputStream requestBodyStream = ModellerSparqlUpdateReader.class.getResourceAsStream("/data/res.update");

        try {
            final String requestBody = IOUtils.toString(requestBodyStream, UTF_8);
            System.out.println() ;
            System.out.println("#### ---- Write as application/sparql-update") ;
            System.out.println(requestBody) ;
        } catch ( final IOException ex ) {
        }
    }

}