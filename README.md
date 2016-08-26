# modeller

This project serves as a client for interacting with Fedora 4
using Java to create IIIF data models.

The intention is to add configuration templates and a UI for simple
creation of LDP sequences that conform to the IIIF presentation data model.

This will replace [iiif-builder](https://github.com/blumenbach/iiif-builder)

Current maintainers:
* [Christopher Johnson](https://github.com/christopher-johnson)

The workflow:

1. select a directory of file resources.
2. enter metadata parameters for resource "Bag"
3. save "Bag" to filesystem
4. Create IIIF model containers (PUT)
5. PUT "bag" resources into Fedora.
6. PATCH fcr:metadata to file resources
7. Create XML descriptors for each file resource and store them to binary
serialization directory.
8. Create list, canvas sequence, and manifest sparql-update request bodies 
using UI form
9. PATCH IIIF model containers with metadata.