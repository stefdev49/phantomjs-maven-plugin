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

import com.github.klieber.phantomjs.locate.Locator;
import com.github.klieber.phantomjs.locate.PhantomJsLocator;
import com.github.klieber.phantomjs.locate.PhantomJsLocatorOptions;
import com.github.klieber.phantomjs.locate.RepositoryDetails;
import org.apache.maven.plugin.MojoFailureException;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;

import java.io.File;
import java.util.List;

/**
 * Maven plugin for downloading and installing phantomjs binaries.
 * @goal install
 * @phase process-test-sources
 * @since 0.1
 */
public class InstallPhantomJsMojo extends AbstractPhantomJsMojo implements PhantomJsLocatorOptions {

  private static final String UNABLE_TO_INSTALL = "Failed to install phantomjs.";

  /**
   * The version of phantomjs to install.
   * @parameter property = "phantomjs.version"
   * @required
   * @since 0.1
   */
  private String version;

  /**
   * The base url the phantomjs binary can be downloaded from.
   * @parameter  property = "phantomjs.baseUrl"
   * @since 0.1
   */
  private String baseUrl;

  /**
   * The directory the phantomjs binary should be installed.
   * @parameter property = "phantomjs.outputDir" default-value = "${project.build.directory}/phantomjs-maven-plugin"
   * @required
   * @since 0.1
   */
  private File outputDirectory;

  /**
   * Check the system path for an existing phantomjs installation.
   * @parameter property = "phantomjs.checkSystemPath" default-value = "true"
   * @required
   * @since 0.2
   */
  private boolean checkSystemPath;

  /**
   * Require that the correct version of phantomjs is on the system path. You may either specify a boolean value
   * or starting with version 0.7 of the plugin you can also specify a version range following the same syntax
   * as the <a href="http://maven.apache.org/enforcer/enforcer-rules/versionRanges.html">Maven Enforcer Plugin</a>.
   *
   * @parameter property = "phantomjs.enforceVersion" default-value = "true"
   * @since 0.2
   */
  private String enforceVersion;

  /**
   * <p>The download source for the phantomjs binary.</p>
   * <p>Valid values:</p>
   * <ul>
   *   <li>REPOSITORY : download a copy from the maven central repository.</li>
   *   <li>URL : download directly from a url</li>
   * </ul>
   *
   * @parameter  property = "phantomjs.source" default-value = "REPOSITORY"
   * @required
   * @since 0.3
   */
  private PhantomJsLocatorOptions.Source source;

  /**
   * @parameter default-value = "${repositorySystemSession}"
   * @readonly
   */
  private RepositorySystemSession repositorySystemSession;

  /**
   * @parameter default-value = "${project.remoteProjectRepositories}"
   * @readonly
   */
  private List<RemoteRepository> remoteRepositories;

  /**
   * The entry point to Aether, i.e. the component doing all the work.
   * @component
   */
  private RepositorySystem repositorySystem;

  @Override
  public Source getSource() {
    return this.source;
  }

  @Override
  public String getVersion() {
    return this.version;
  }

  @Override
  public boolean isCheckSystemPath() {
    return this.checkSystemPath;
  }

  @Override
  public String getEnforceVersion() {
    return this.enforceVersion;
  }

  @Override
  public String getBaseUrl() {
    return this.baseUrl;
  }

  @Override
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  public void run() throws MojoFailureException {
    RepositoryDetails repositoryDetails = new RepositoryDetails();
    repositoryDetails.setRepositorySystem(this.repositorySystem);
    repositoryDetails.setRepositorySystemSession(this.repositorySystemSession);
    repositoryDetails.setRemoteRepositories(this.remoteRepositories);

    Locator locator = new PhantomJsLocator(this, repositoryDetails);
    String location = locator.locate();

    if (location == null) {
      throw new MojoFailureException(UNABLE_TO_INSTALL);
    }
    this.setPhantomJsBinary(location);
  }
}