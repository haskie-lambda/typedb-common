/*
 * Copyright (C) 2021 Vaticle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.vaticle.typedb.common.test.console;

import com.vaticle.typedb.common.test.Runner;
import org.zeroturnaround.exec.StartedProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class TypeDBConsoleRunner extends Runner {

    public TypeDBConsoleRunner() throws InterruptedException, TimeoutException, IOException {
        super();
    }

    public int run(String address, boolean isCluster, Path scriptFile) {
        try {
            StartedProcess consoleProcess = executor.command(command(address, isCluster, scriptFile)).start();
            return consoleProcess.getProcess().waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int run(String address, boolean isCluster, String... commands) {
        try {
            StartedProcess consoleProcess = executor.command(command(address, isCluster, commands)).start();
            return consoleProcess.getProcess().waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> command(String address, boolean isCluster, Path scriptFile) {
        List<String> command = new ArrayList<>();
        command.addAll(getTypeDBBinary());
        command.add("console");
        command.add(isCluster ? "--cluster" : "--server");
        command.add(address);
        command.add("--script");
        command.add(scriptFile.toAbsolutePath().toString());
        return command;
    }

    private List<String> command(String address, boolean isCluster, String... commands) {
        List<String> command = new ArrayList<>();
        command.addAll(getTypeDBBinary());
        command.add("console");
        command.add(isCluster ? "--cluster" : "--server");
        command.add(address);
        for (String commandString : commands) {
            command.add("--command");
            command.add(commandString);
        }
        return command;
    }

    @Override
    protected File distributionArchive() {
        String[] args = System.getProperty("sun.java.command").split(" ");
        assert args.length > 2;
        return new File(args[2]);
    }

    @Override
    protected String name() {
        return "TypeDB Console";
    }
}
