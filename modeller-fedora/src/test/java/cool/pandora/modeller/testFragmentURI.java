package cool.pandora.modeller;


import java.net.URI;

public class testFragmentURI {

    public static void main(String[] args) {
        URI uri = URI.create("http://localhost:8080/fcrepo/rest/collection/test/003/canvas/19#xywh=2999,1542,62,56");
        String out = uri.toString();
        System.out.println(out);
    }
}
