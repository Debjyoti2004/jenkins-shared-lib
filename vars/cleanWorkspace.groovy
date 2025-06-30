// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Clean up the workspace.
// -----------------------------------------------------------------------------

def call() {
    echo "[INFO] Starting workspace cleanup..."

    try {
        cleanWs() 
        echo "[SUCCESS] Workspace cleaned successfully."
    } catch (error) {
        echo "[ERROR] Failed to clean workspace: ${error.message}"
        currentBuild.result = 'FAILURE'
        throw error
    }
}
