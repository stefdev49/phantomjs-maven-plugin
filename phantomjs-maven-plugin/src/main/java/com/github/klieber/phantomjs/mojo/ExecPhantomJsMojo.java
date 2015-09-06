/*
 * Copyright (c) 2013 Kyle Lieber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.klieber.phantomjs.mojo;

import com.github.klieber.phantomjs.exec.ExecutionException;
import com.github.klieber.phantomjs.exec.PhantomJsExecutor;
import com.github.klieber.phantomjs.exec.PhantomJsProcessBuilder;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Maven plugin for downloading and installing phantomjs binaries.
 *
 * @since 0.2
 */
//@Mojo(name = "exec", defaultPhase = LifecyclePhase.TEST)
public class ExecPhantomJsMojo extends AbstractPhantomJsMojo {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExecPhantomJsMojo.class);

  private static final String EXECUTION_FAILURE = "Failed to execute PhantomJS command";
  private static final String ABNORMAL_EXIT_CODE = "PhantomJS execution did not exit normally (code = %d)";

  /**
   * Working Directory
   * @parameter  property = "phantomjs.workingDirectory defaultValue = "${project.basedir}"
   * @since 0.5
   */
  private String workingDirectory;

  /**
   * Command line options for phantomjs
   * @parameter property = "phantomjs.commandLineOptions"
   * @since 0.2
   */
  private String commandLineOptions;

  /**
   * Script to execute
   * @parameter property = "phantomjs.script"
   * @since 0.2
   */
  private String script;

  /**
   * Arguments for the script being executed
   * @parameter property = "phantomjs.args"
   * @since 0.2
   */
  private List<String> arguments;

  /**
   * Configuration file for phantomjs
   * @parameter  property = "phantomjs.configFile"
   * @since 0.2
   */
  private File configFile;

  /**
   * Fail on a non-zero exit code
   * @parameter property = "phantomjs.failOnNonZeroExitCode" defaultValue = "true"
   * @since 0.4
   */
  private boolean failOnNonZeroExitCode;

  private PhantomJsExecutor executor;

  public ExecPhantomJsMojo() {
    this(new PhantomJsExecutor());
  }

  public ExecPhantomJsMojo(PhantomJsExecutor executor) {
    this.executor = executor;
  }

  public void run() throws MojoFailureException {
    LOGGER.info("Executing phantomjs command");

    String binary = this.getPhantomJsBinary();

    PhantomJsProcessBuilder processBuilder = new PhantomJsProcessBuilder(binary)
        .configFile(this.configFile)
        .workingDirectory(this.workingDirectory)
        .commandLineOptions(this.commandLineOptions)
        .script(this.script)
        .arguments(this.arguments);

    try {
      int code = executor.execute(processBuilder);
      if (failOnNonZeroExitCode && code != 0) {
        throw new MojoFailureException(String.format(ABNORMAL_EXIT_CODE, code));
      }
    } catch (ExecutionException e) {
      throw new MojoFailureException(EXECUTION_FAILURE, e);
    }
  }
}
