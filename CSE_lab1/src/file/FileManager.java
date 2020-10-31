package file;

import id.Id;

public interface FileManager {
    File getFile(Id fileId);
    File newFile(Id fileId);
}
