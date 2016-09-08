package org.blume.modeller;

import org.fcrepo.client.FcrepoOperationFailedException;

public class ModellerClientFailedException extends Throwable {
    ModellerClientFailedException(FcrepoOperationFailedException e) {
        super(e);
    }
}
