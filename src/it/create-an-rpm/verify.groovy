import java.io.File

def file = new File(basedir, "target")
assert file.exists(), "A target directory should be generated"
assert file.isDirectory()
def rpmWasMade = false
file.eachFileMatch(~/.*\.rpm/) {
    rpmWasMade = true
}
assert rpmWasMade, "An RPM was not created!"