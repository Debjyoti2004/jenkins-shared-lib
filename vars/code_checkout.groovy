// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Clone the repository from the specified URL and branch using credentials.
// -----------------------------------------------------------------------------

def call(Map args) {
    def url = args.repoUrl
    def branch = args.branch
    def credentialsId = args.credentialsId ?: 'gitCredentials'

    echo "[INFO] Initiating Git checkout..."

    if (!url?.trim() || !branch?.trim()) {
        error "[ERROR] Git URL or branch name is missing. Aborting checkout."
    }

    try {
        git url: url, branch: branch, credentialsId: credentialsId
        echo "[SUCCESS] Checked out branch '${branch}' from '${url}'."
    } catch (e) {
        echo "[ERROR] Git checkout failed: ${e.message}"
        currentBuild.result = 'FAILURE'
        throw e
    }
}
