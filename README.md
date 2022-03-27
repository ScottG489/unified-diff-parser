[![Build Status](https://travis-ci.org/ScottG489/unified-diff-parser.svg?branch=master)](https://travis-ci.org/ScottG489/unified-diff-parser) [![Coverage Status](https://coveralls.io/repos/ScottG489/unified-diff-parser/badge.svg?branch=master&service=github)](https://coveralls.io/github/ScottG489/unified-diff-parser?branch=master) [![codecov.io](https://codecov.io/github/ScottG489/unified-diff-parser/coverage.svg?branch=master)](https://codecov.io/github/ScottG489/unified-diff-parser?branch=master) [![Codacy Badge](https://api.codacy.com/project/badge/grade/fceed9561154417b9b44605c42c6a829)](https://www.codacy.com/app/ScottG489/unified-diff-parser) [![Download](https://api.bintray.com/packages/scottg489/maven/unified-diff-parser/images/download.svg) ](https://bintray.com/scottg489/maven/unified-diff-parser/_latestVersion)
# unified-diff-parser
Extract metadata from unified diffs

## Releasing
Be sure to update the version in `build.gradle` to a version not ending with `-SNAPSHOT`. Then make a subsequent commit
with a snapshot for the next version. This will prevent old versions from being uploaded and staged in sonatype.

After artifacts are uploaded you'll need to manually release it here:

https://s01.oss.sonatype.org/#stagingRepositories

The steps to release are:
1. Delete any old artifacts that are staged. The CI build will upload all artifacts here that aren't snapshots
   (appended with `-SNAPSHOT`) so some may be in here. The release will fail if there are old artifacts because it
   will refuse to overwrite an existing version.
   1. Select the staging repository
   2. Select the "Content" tab in the lower panel
   3. Traverse the directory structure until you get to where versions are listed. Delete any versions you do not wish
      to release
2.  With the repository selected click on "Close" above the main panel. The version will run through a set of
    validations that could take up to 5-15 minutes. You can follow these by viewing the "Activity" tab in the lower
    panel. Once successful, you can then click the "Release" button above the upper panel. Note that the release can
    still fail, for instance, if there are existing artifacts that it will refuse to overwrite.

The version can take a bit of time to show up in the main repository found here:

https://repo1.maven.org/maven2/io/github/scottg489/unified-diff-parser/

It can also be found here I believe immediately:

https://s01.oss.sonatype.org/content/repositories/staging/io/github/scottg489/unified-diff-parser/
