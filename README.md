# Build Flow Toolbox

This plugin provides DSL extensions to the BuildFlow plugin.

Usage in a Build Flow DSL:

```groovy
def toolbox = extension."build-flow-toolbox"

// build then copy workspace from other job
// requires build-flow-plugin version 0.10 or below
// https://issues.jenkins-ci.org/browse/JENKINS-22725
other_job = build("some-other-job")
toolbox.copyFiles(other_job.workspace, build.workspace)
```
