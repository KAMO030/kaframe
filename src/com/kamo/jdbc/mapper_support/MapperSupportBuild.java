package com.kamo.jdbc.mapper_support;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MapperSupportBuild {
    private String entityPackage;
    private String mapperPackage;

    public MapperSupportBuild() {
    }

    public MapperSupportBuild(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public MapperSupportBuild setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
        return this;
    }

    public MapperSupportBuild setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
        return this;
    }

    public void build() throws IOException {
        build("mapper");
    }

    public void build(String packageName) throws IOException {
        Objects.requireNonNull(entityPackage, "实体类所在的包路径不能为 null");
        Objects.requireNonNull(packageName, "映射器所在的包路径不能为 null");
        int lastIndexOf = entityPackage.lastIndexOf('/');
        if (mapperPackage == null) {
            mapperPackage = entityPackage.substring(0, lastIndexOf + 1) + packageName;
        }
        String entityPackageName = entityPackage.substring(entityPackage.lastIndexOf('/')+1);
        File entityFile = new File(entityPackage);
        if (!entityFile.isDirectory()) {
            throw new IllegalArgumentException("实体类所在的包路径不是目录或不存在");
        }
        List<String> refEntityNames = new ArrayList<String>();
        String refEntityClass = null;
        for (File file : entityFile.listFiles()) {
            String name = file.getName();
            if (file.isFile() && name.endsWith(".java")) {
                refEntityNames.add(name.substring(0, name.lastIndexOf('.')));
                if (refEntityClass != null) {
                    continue;
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                    String line = reader.readLine();
                    refEntityClass = line.substring(line.indexOf(' ') + 1, line.length() - 1);
                }
            }
        }
        File mapperFile = new File(mapperPackage);
        if (!mapperFile.isDirectory()) {
            mapperFile.mkdirs();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("package ${mapperPackage};\nimport ${refEntityClass};\n")
                .append("import com.kamo.jdbc.mapper_support.MapperSupport;\n")
                .append("public interface ${mapperName} extends MapperSupport<${refEntityName}>{\n}");
        for (String name : refEntityNames) {
            String mapperName = name + "Mapper";
            mapperFile = new File(mapperPackage + '/' + mapperName + ".java");
            if (mapperFile.isFile()) {
                continue;
            }
            String content = sb.toString();
            content = content.replace("${mapperPackage}",refEntityClass.replace(entityPackageName, packageName))
                    .replace("${refEntityClass}",refEntityClass+"."+name)
                    .replace("${mapperName}",mapperName)
                    .replace("${refEntityName}",name);
            try(PrintWriter pw = new PrintWriter(mapperFile)) {
                pw .write(content);
            }

        }
    }
}
