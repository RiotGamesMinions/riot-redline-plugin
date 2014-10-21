package com.riotgames.maven.redline.integrationtest;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

public class FindRpmFile {

    public List<File> findInFolder(File directory){
        List<File> rpms = Lists.newArrayList();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                //do nothing
            }
            else if (file.getName().endsWith(".rpm")) {
                rpms.add(file);
            }
        }
        return rpms;
    }
}
