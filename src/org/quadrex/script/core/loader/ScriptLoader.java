package org.quadrex.script.core.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScriptLoader {

    public Stream<File> getAllScriptFiles(File baseDirectory) {
        List<File> scriptFiles = new ArrayList<>();
        try {
            Files.walkFileTree(baseDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    File file = path.toFile();
                    if(file.getName().equalsIgnoreCase("script.json")) {
                        scriptFiles.add(file);
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch(IOException ex) {
            ex.printStackTrace();
            return scriptFiles.stream();
        }
        return scriptFiles.stream();
    }
}
