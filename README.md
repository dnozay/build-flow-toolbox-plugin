# Build Flow Toolbox

This plugin provides DSL extensions to the BuildFlow plugin.

# Usage in a Build Flow DSL

## Copying files

```groovy
other_job = build("some-other-job")

// build then copy workspace from other job
// requires build-flow-plugin version 0.10 or below
// https://issues.jenkins-ci.org/browse/JENKINS-22725
def toolbox = extension."build-flow-toolbox"
toolbox.copyFiles(other_job.workspace, build.workspace)
```

## Copying artifacts

```groovy
other_job = build("some-other-job")

// record same artifacts archived by other_job
def toolbox = extension."build-flow-toolbox"
toolbox.slurpArtifacts(other_job)
```
