package com.riotgames.maven.redline.integrationtest;

import org.redline_rpm.ReadableChannelWrapper;
import org.redline_rpm.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RpmInformation {

    private File rpm;

    public RpmInformation(File rpm) {
        this.rpm = rpm;
    }

    public String[] fileModes() throws IOException {
        ReadableByteChannel channel = new FileInputStream(rpm).getChannel();
        ReadableChannelWrapper channelWrapper = new ReadableChannelWrapper(channel);

        Scanner scanner = new Scanner();
        String rpmInformation = scanner.run(channelWrapper).toString();
        Matcher matcher = Pattern.compile(".*filemodes\\[[^\\]]*\\]\\n[^0-9-]*([^\\n]*).*", Pattern.DOTALL).matcher(rpmInformation);
        matcher.matches();
        return matcher.group(1).split(", ");
    }
}
