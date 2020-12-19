#!/bin/bash
set -e
set -o pipefail

## Update the homebrew descriptor (Inspired by https://github.com/holgerbrandl/kscript/issues/50)

FLOORPLAN_CLI_HOME=floorplan-cli
FLOORPLAN_CLI_BINARY_NAME=floorplan-cli
floorplan_version=$(grep 'floorPlanVersion' versioning.gradle | cut -f2 -d'=' | tr -d ' "')
FLOORPLAN_DISTRIBUTION_NAME="${FLOORPLAN_CLI_BINARY_NAME}-${floorplan_version}"

echo "Ready to release FloorPlan $floorplan_version to Homebrew."
read -p "Do you wish to continue? " -n 1 -r
echo    # move to a new line
if [[ ! $REPLY =~ ^[Yy]$ ]]; then exit 1; fi

ARCHIVE_DIR=cli-archive/

mkdir -p $ARCHIVE_DIR/
cp ${FLOORPLAN_CLI_HOME}/build/distributions/${FLOORPLAN_DISTRIBUTION_NAME}.zip $ARCHIVE_DIR/

if [ ! -f ${ARCHIVE_DIR}/${FLOORPLAN_DISTRIBUTION_NAME}.zip ]; then
    echo "Distribution binary zip file not found!"
    exit 1;
fi

archiveMd5=$(shasum -a 256 ${ARCHIVE_DIR}/${FLOORPLAN_DISTRIBUTION_NAME}.zip | cut -f1 -d ' ')

echo "MD5 SHA256 of zipped binary is $archiveMd5."

rm -rf homebrew-tap
git clone https://github.com/julioz/homebrew-tap.git
cd homebrew-tap

git config user.email "julioz@users.noreply.github.com"

cat - <<EOF > floorplan.rb
class FloorPlan < Formula
  desc "floorplan"
  homepage "https://github.com/julioz/FloorPlan"
  url "https://github.com/julioz/FloorPlan/releases/download/v${floorplan_version}/${FLOORPLAN_DISTRIBUTION_NAME}.zip"
  sha256 "${archiveMd5}"

  bottle :unneeded

  depends_on "kotlin"

  def install
    libexec.install Dir["*"] # Put the extracted files in to the 'private' libexec folder
    bin.install_symlink "#{libexec}/bin/floorplan-cli"
  end
end
EOF

git add floorplan.rb
git commit -m "v${floorplan_version} release"
git push

## to test use `brew install julioz/tap/floorplan`
