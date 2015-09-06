package com.github.klieber.phantomjs.locate;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;

import java.util.List;

public class RepositoryDetails {

  private RepositorySystem repositorySystem;
  private RepositorySystemSession repositorySystemSession;
  private List<RemoteRepository> remoteRepositories;

  public RepositorySystem getRepositorySystem() {
    return repositorySystem;
  }

  public void setRepositorySystem(RepositorySystem repositorySystem) {
    this.repositorySystem = repositorySystem;
  }

  public RepositorySystemSession getRepositorySystemSession() {
    return repositorySystemSession;
  }

  public void setRepositorySystemSession(RepositorySystemSession repositorySystemSession) {
    this.repositorySystemSession = repositorySystemSession;
  }

  public List<RemoteRepository> getRemoteRepositories() {
    return remoteRepositories;
  }

  public void setRemoteRepositories(List<RemoteRepository> remoteRepositories) {
    this.remoteRepositories = remoteRepositories;
  }
}
