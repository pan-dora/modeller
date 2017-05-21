package cool.pandora.modeller;

import org.fcrepo.client.FcrepoOperationFailedException;

/**
 * ModellerClientFailedException
 *
 * @author Christopher Johnson
 */
public class ModellerClientFailedException extends Throwable {
    ModellerClientFailedException(final FcrepoOperationFailedException e) {
        super(e);
    }
}
