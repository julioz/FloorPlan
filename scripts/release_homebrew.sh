#!/bin/bash

floorplan_version=$(grep 'floorPlanVersion' versioning.gradle | cut -f2 -d'=' | tr -d ' "')

echo "Ready to release FloorPlan $floorplan_version to Homebrew."
read -p "Do you wish to continue? " -n 1 -r
echo    # move to a new line
if [[ ! $REPLY =~ ^[Yy]$ ]]; thewn exit 1; fi

## Update the homebrew descriptor (Inspired by https://github.com/holgerbrandl/kscript/issues/50)
rm -rf homebrew-tap
git clone https://github.com/julioz/homebrew-tap.git
cd homebrew-tap

git config user.email "julioz@users.noreply.github.com"
#
#archiveMd5=$(shasum -a 256 ${KSCRIPT_ARCHIVE}/kscript-${floorplan_version}.zip | cut -f1 -d ' ')
#
#cat - <<EOF > floorplan.rb
#class FloorPlan < Formula
#  desc "floorplan"
#  homepage "https://github.com/julioz/FloorPlan"
#  url "https://github.com/holgerbrandl/kscript/releases/download/v${floorplan_version}/kscript-${floorplan_version}-bin.zip"
#  sha256 "${archiveMd5}"
#
#  depends_on "kotlin"
#
#  def install
#    libexec.install Dir["*"] # Put the extracted JAR in to the 'private' libexec folder
#    inreplace "#{libexec}/bin/kscript", /^jarPath=.*/, "jarPath=#{libexec}/bin/kscript.jar"
#    bin.install_symlink "#{libexec}/bin/kscript"
#  end
#end
#EOF
#
#git add kscript.rb
#git commit -m "v${floorplan_version} release"
#git push #origin releases
#
## to test use `brew install julioz/tap/floorplan`
