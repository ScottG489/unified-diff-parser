# unified-diff-parser
![CI](https://github.com/ScottG489/unified-diff-parser/workflows/CI/badge.svg)

Extract metadata from unified diffs

## Releasing
Releases are automatically deployed and publicly available. To release:
1. Bump `version` in `build.gradle` to a non-snapshot version
2. Commit and push
3. Bump version to next patch and append `-SNAPSHOT`
4. Commit and push

This avoids accidentally forgetting to add `-SNAPSHOT` before subsequent pushes. The build would otherwise fail because
it would refuse to overwrite the old version.
